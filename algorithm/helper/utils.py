import json
from flask import Blueprint, jsonify, request
# from model import ClusterData, Cluster, Indicator, Node, NodeSingleData, Disk, NodeMultipleData
import sys


sys.path.append('../../')
import helper.data_process as dp
import tskit
import seaborn as sns
import random as rd 
import numpy as np
import math
import json
import matplotlib.pyplot as plt
import helper.synthetic as syn
from itertools import chain, repeat

sns.set_style('darkgrid')


def add_noise(metric_ts):
    """
    添加噪音使不平滑

    :param metric_ts 需要添加噪音的曲线
    """
    metric_ts = tskit.noise.add_gaussian_noise(metric_ts, clip=[-10, 10])
    metric_ts = tskit.noise.add_perlin_noise(metric_ts)
    metric_ts = tskit.noise.add_uniform_noise(metric_ts)
    return metric_ts


def smooth(metric_ts):
    """
    对给定的曲线进行平滑处理

    :param metric_ts 需要进行平滑的曲线
    """
    metric_ts = tskit.smoothing.ewma(metric_ts, alpha=0.5)
    return metric_ts


def make_periodic(metric_ts, tile_times):
    """
    使曲线具有周期性

    :param metric_ts 使具有周期性的曲线
    :param tile_times 为具有周期性平铺的次数
    """
    metric_ts = tskit.transform.tile(metric_ts, tile_times)
    return metric_ts


def find_combinations(lst, target_sum):
    n = len(lst)
    dp = [[False] * (target_sum + 1) for _ in range(n + 1)]
    dp[0][0] = True

    for i in range(1, n + 1):
        num = lst[i - 1]
        for j in range(target_sum + 1):
            dp[i][j] = dp[i - 1][j]
            if j >= num:
                dp[i][j] = dp[i][j] or dp[i - 1][j - num]

    if not dp[n][target_sum]:
        return []

    result = []
    i, j = n, target_sum
    while i > 0 and j > 0:
        if dp[i][j] and not dp[i - 1][j]:
            result.append(i - 1)
            j -= lst[i - 1]
        i -= 1

    return result


def differ_pattern_random_walk(related_metric_ts, weight):
    """
    模式间ts差异化 通过添加random_walk函数
    
    :param related_metric_ts 需要进行模式差异化的指标集合
    :param weight 为random_walk设置的权重参数
    """
    for index in range(len(related_metric_ts)):
        ts_random = tskit.generators.random_walk(length=100)
        related_metric_ts[index] = tskit.transform.add([related_metric_ts[index],ts_random],weights=[1.0,weight*rd.randint(1,len(related_metric_ts))],standardize_idx=[1])

    return related_metric_ts


def differ_pattern_multiple(related_metric_ts, weight):
    """
    模式间ts差异化 通过添加倍数

    :param related_metric_ts 需要进行模式差异的指标集合
    :param weight 为增加倍数设置的权重参数
    """
    for index in range(len(related_metric_ts)):
        
        related_metric_ts[index] = tskit.transform.add([related_metric_ts[index],related_metric_ts[index]],weights=[1.0,weight*rd.randint(1,len(related_metric_ts))],standardize_idx=[1])

    return related_metric_ts


def rand_pattern(related_metric_collections, metric_pattern, weight):
    """
    #随机模式生成(为避免相同，将模式数上传)

    :param related_metric_collections 进行曲线生成的指标集合
    :param metric_pattern 生成曲线的模式
    :param weight 调整模式间差异的超参数
    """
    related_metric_ts = []
    for index in related_metric_collections:
        related_metric_ts.append(metric_pattern)

    return differ_pattern_multiple(related_metric_ts,weight)


def generate_metric_list(related_metric_collections,periodicity_metric_collections_index,smoothness_num,pattern,tile_times,weight):
    """
    生成metric_ts_list实体
    
    :param related_metric_collections 关联指标集合
    :param periodicity_metric_collections_index 具有周期性的关联指标集合所对应的索引
    :param smoothness 平滑曲线数
    :param pattern 模式库
    """
    metric_ts_list = []

    #得到具有周期性的曲线的下标
    periodicity_metric_ts = []
    for index in range(len(related_metric_collections)):
        if index in periodicity_metric_collections_index:
            for periodicity_metric_ts_index in related_metric_collections[index]:
                periodicity_metric_ts.append(periodicity_metric_ts_index)
                
    #具有周期性的曲线由不具有周期性的曲线值重复得到
    metric_patterns = rd.sample(range(len(pattern)),len(related_metric_collections))
    for index in range(len(related_metric_collections)):
        related_metric_ts = rand_pattern(related_metric_collections[index],pattern[metric_patterns[index]],weight)
        #使具有周期性
        if index in periodicity_metric_collections_index:
            for related_metric_ts_index in range(len(related_metric_ts)):
                related_metric_ts[related_metric_ts_index] = make_periodic(related_metric_ts[related_metric_ts_index],tile_times)
        else:
            for related_metric_ts_index in range(len(related_metric_ts)):
                related_metric_ts[related_metric_ts_index] = tskit.transform.resize(related_metric_ts[related_metric_ts_index],len(related_metric_ts[related_metric_ts_index])*tile_times)
        metric_ts_list.extend(related_metric_ts)


    #添加噪音的ts的下标索引集合
    add_noise_ts_metric = rd.sample(range(len(metric_ts_list)), len(metric_ts_list)-smoothness_num)
    #调用平滑方法的ts的下标索引集合
    smooth_ts_metric=[]
    for metric_ts_index in range(len(metric_ts_list)):
        if metric_ts_index in add_noise_ts_metric:
            metric_ts_list[metric_ts_index] = add_noise(metric_ts_list[metric_ts_index])
        else:
            metric_ts_list[metric_ts_index] = smooth(metric_ts_list[metric_ts_index])
            smooth_ts_metric.append(metric_ts_index)

    return metric_ts_list,smooth_ts_metric,periodicity_metric_ts


def generate_related_metric_collections(correlation_num,periodicity_num,metric_num):
    """
    指定指标数和关联度自动生成关联指标集合
    
    :param correlation_num 指标模式数
    :param periodicity_num 周期曲线数
    :param metric_num 指标数
    """
    #判断关联指标集合是否满足周期度要求
    is_flag=False
    #具有周期性的关联指标集合的下标
    while(is_flag==False):
        all_metric = list(range(1, metric_num + 1))
        related_metric_collections = []

        for i in range(correlation_num - 1):
            num_metrics_to_select = rd.randint(1, len(all_metric) - correlation_num + i + 1)
            metric = rd.sample(all_metric, num_metrics_to_select)
            related_metric_collections.append(metric)
            all_metric = list(set(all_metric) - set(metric))

        related_metric_collections.append(all_metric)
        #替换索引
        replacement = list(range(0,metric_num))
        for sublist in related_metric_collections:
            for i in range(len(sublist)):
                sublist[i] = replacement.pop(0)  

        #记录每一个关联指标集合的长度
        related_metric_collections_len = []
        for i in range(len(related_metric_collections)):
            related_metric_collections_len.append(len(related_metric_collections[i]))
        periodicity_metric_collections_indexes  = find_combinations(related_metric_collections_len,periodicity_num)    
        #判断随机生成的关联指标集合是否能够满足周期度要求
        if(periodicity_metric_collections_indexes!=[]): 
            is_flag=True
        elif(periodicity_metric_collections_indexes==[] and periodicity_num==0):
            is_flag=True
        
    return related_metric_collections,periodicity_metric_collections_indexes


def generate_MTS(metric_num,smoothness,periodicity,correlation,all_pattern=None,tile_times=5,weight=0.01):
    """
    :param metric_num 实体指标数
    :param smoothness 平滑度
    :param periodicity 周期度
    :param correlation 关联度
    :param tile_times 曲线为具有周期性平铺的次数(默认为10)
    :param weight 调整模式间差异的超参数(默认为0.01)
    """
    #模式库
    if not all_pattern is None:
        pattern = all_pattern
    else:
        pattern = np.load('../backend/pattern/aperiodic_pattern.npy',allow_pickle=True)
    #计算平滑曲线数
    smoothness_num=math.ceil(smoothness * metric_num)
    #计算周期曲线数
    periodicity_num=math.ceil(periodicity * metric_num)
    #计算曲线模式数
    correlation_num=math.ceil((1 - correlation) *metric_num)
    #生成关联指标集合
    related_metric_collections,periodicity_metric_collections_index = generate_related_metric_collections(correlation_num,periodicity_num,metric_num)
    #生成指标对应的曲线集合
    metric_ts_list,smooth_metric_ts,periodicity_metric_ts = generate_metric_list(related_metric_collections,periodicity_metric_collections_index,smoothness_num,pattern,tile_times,weight)
    
    return metric_ts_list,smooth_metric_ts,periodicity_metric_ts,related_metric_collections


def generate_feature_values(metric_num):
    """
    :param metric_num 指标数
    """
    feature_values = []
    for index1 in range(10):
        for index2 in range(10):
            for index3 in range(9):
                for _ in range(5):
                    feature_value1 = index1/10+(0.1)*rd.random()
                    feature_value2 = index2/10+(0.1)*rd.random()
                    feature_value3 = index3/10+(0.1)*rd.random()
                    feature_values.append(change_feature_values(feature_value1,feature_value2,feature_value3,metric_num))
    for index1 in range(10):
        for index2 in {0,9}:
            for index3 in {9}:
                for _ in range(5):
                    feature_value1 = index1/10+(0.1)*rd.random()
                    feature_value2 = index2/10+(0.1)*rd.random()
                    feature_value3 = index3/10+(0.1)*rd.random()
                    feature_values.append(change_feature_values(feature_value1,feature_value2,feature_value3,metric_num))
    return feature_values


def are_arrays_unequal(arr1, arr2):  
   if len(arr1)!= len(arr2):  
       return True
   for i in range(len(arr1)):  
       if arr1[i]!= arr2[i]:  
           return True
   return False


def plot_data(data_list):  
#    for i, data in enumerate(data_list):  
       plt.figure(figsize=(14, 3))
       plt.plot(data_list)  
       plt.xlabel('Index')  
       plt.ylabel('Value')  
       plt.title(f'Sublist {1}')  
       plt.show()
