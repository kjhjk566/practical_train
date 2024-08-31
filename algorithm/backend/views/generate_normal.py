import json
from flask import Blueprint, jsonify, request
# from model import ClusterData, Cluster, Indicator, Node, NodeSingleData, Disk, NodeMultipleData
import sys

sys.path.append('../../')
from helper.utils import *
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
import os

sns.set_style('darkgrid')

gn = Blueprint("generate_normal", __name__)

# @gn.route('/test', strict_slashes=False, methods=["POST", "GET"])
# def hello_world():
#     return 'generate_normal'

@gn.route('/feature', strict_slashes=False, methods=["POST", "GET"])
def change_feature_values():
    """
    :param feature_value1 数据特征1
    :param feature_value2 数据特征2
    :param feature_value3 数据特征3
    :param metric_num 指标数
    """
    if request.method == "GET":
        feature_value1 = float(0.12)
        feature_value2 = float(0.22)
        feature_value3 = float(0.58)
        metric_num = int(10)

        try:
            smoothness_num=rd.randint(math.ceil(feature_value1*metric_num),math.floor(math.floor((feature_value1 + 0.1) * 10) / 10*metric_num))
        except ValueError:
            smoothness_num=math.floor(math.floor((feature_value1 + 0.1) * 10) / 10*metric_num)
        #更新新的平滑度
        feature_value1=smoothness_num/metric_num
        #计算周期曲线数并更新新的周期度
        if(feature_value2<0.1 and feature_value3>0.9):
            periodicity_num=0
            feature_value2=0
            feature_value3=0.9
            # return f"{feature_value1},{feature_value2},{feature_value3}"
            return jsonify([feature_value1, feature_value2, feature_value3])
        else:
            
            try:
                periodicity_num=rd.randint(math.ceil(feature_value2*metric_num),math.floor(math.floor((feature_value2 + 0.1) * 10) / 10*metric_num))
            except ValueError:
                periodicity_num=math.floor(math.floor((feature_value2 + 0.1) * 10) / 10*metric_num)
            feature_value2=periodicity_num/metric_num
        #计算曲线模式数
        
        try:
            correlation_num=rd.randint(math.ceil((1-feature_value3)*metric_num),math.floor(math.floor((1-feature_value3 + 0.1) * 10) / 10*metric_num))
        except ValueError:
            correlation_num=math.floor(math.floor((1-feature_value3 + 0.1) * 10) / 10*metric_num)
        #更新新的周期度
        feature_value3=(metric_num-correlation_num)/metric_num

        least_pattern_num = math.ceil((1 - feature_value3) *metric_num)
        # return f"{feature_value1},{feature_value2},{feature_value3}"
        return jsonify([feature_value1, feature_value2, feature_value3])
    #计算平滑曲线数
    if request.method == "POST":
        feature_value1 = float(request.form["smoothness"])
        feature_value2 = float(request.form["periodicity"])
        feature_value3 = float(request.form["correlation"])
        metric_num = int(request.form["metricNum"])

        try:
            smoothness_num=rd.randint(math.ceil(feature_value1*metric_num),math.floor(math.floor((feature_value1 + 0.1) * 10) / 10*metric_num))
        except ValueError:
            smoothness_num=math.floor(math.floor((feature_value1 + 0.1) * 10) / 10*metric_num)
        #更新新的平滑度
        feature_value1=smoothness_num/metric_num
        #计算周期曲线数并更新新的周期度
        if(feature_value2<0.1 and feature_value3>0.9):
            periodicity_num=0
            feature_value2=0
            feature_value3=0.9
            # return f"{feature_value1},{feature_value2},{feature_value3}"
            return jsonify([feature_value1, feature_value2, feature_value3])
        else:
            
            try:
                periodicity_num=rd.randint(math.ceil(feature_value2*metric_num),math.floor(math.floor((feature_value2 + 0.1) * 10) / 10*metric_num))
            except ValueError:
                periodicity_num=math.floor(math.floor((feature_value2 + 0.1) * 10) / 10*metric_num)
            feature_value2=periodicity_num/metric_num
        #计算曲线模式数
        
        try:
            correlation_num=rd.randint(math.ceil((1-feature_value3)*metric_num),math.floor(math.floor((1-feature_value3 + 0.1) * 10) / 10*metric_num))
        except ValueError:
            correlation_num=math.floor(math.floor((1-feature_value3 + 0.1) * 10) / 10*metric_num)
        #更新新的周期度
        feature_value3=(metric_num-correlation_num)/metric_num

        least_pattern_num = math.ceil((1 - feature_value3) *metric_num)
        # return f"{feature_value1},{feature_value2},{feature_value3}"
        return jsonify([feature_value1, feature_value2, feature_value3])


def test_gn():
    pattern_index = [3,4,5,6,7,8]
    metric_num = 15

    f1, f2, f3, least_pattern = change_feature_values(0.8, 0.7, 0.6, metric_num)

    while len(pattern_index) < least_pattern:
        rand_int = rd.choice([i for i in range(metric_num) if i not in pattern_index])  # 从不存在于列表中的整数中随机选择一个
        pattern_index.append(rand_int)

    # print(pattern_index)
    print(generate_normal_data('normal_third', metric_num, f1, f2, f3, pattern_index))


# def test():
#     metric_num = 10
#     feature_values = []
#     # 排除非法参数设置
#     f1, f2, f3, _ = change_feature_values(0.4, 0.9, 0.5, metric_num)
#     feature_values.append((f1, f2, f3))
#     f1, f2, f3, _ = change_feature_values(0.1, 0.2, 0.5, metric_num)
#     feature_values.append((f1, f2, f3))
    
#     # data = MTS_list_values[0]["data"][0]
#     anomaly_data = synthetic_anomaly(MTS_list_values[0]["data"])
#     plot_data(anomaly_data.data[4])
#     plot_data(anomaly_data.data_origin[4])
#     # print(are_arrays_unequal(anomaly_data.data[3],anomaly_data.data_origin[3]))
#     plot_data(anomaly_data.label)

@gn.route('/generate', strict_slashes=False, methods=["POST", "GET"])
def generate_normal_data():
    if request.method == "POST":
        labName = request.form["labName"]
        username = request.form["username"]
        metric_num = int(request.form["metricNum"])
        smoothness = float(request.form["smoothness"])
        periodicity = float(request.form["periodicity"])
        correlation = float(request.form["correlation"])
        pattern_index = request.form["patterns"]
        if len(pattern_index) > 0:
            pattern_index = list(map(int, pattern_index.split(',')))
        else:
            pattern_index = []
        # print(f'request.form["patterns"]: {request.form["patterns"]}')

        try:
            pattern = np.load('backend/pattern/aperiodic_pattern.npy',allow_pickle=True)
            all_pattern = np.array([])
            least_pattern_num = math.ceil((1 - correlation) *metric_num)
            # print(f'least_pattern_num: {least_pattern_num}')
            while len(pattern_index) < least_pattern_num:
                rand_int = rd.choice([i for i in range(10) if i not in pattern_index])  # 从不存在于列表中的整数中随机选择一个
                pattern_index.append(rand_int)
            # print(f'pattern_index: {pattern_index}')
            for index_num in pattern_index:
                temp_pattern = pattern[index_num]
                all_pattern = np.append(all_pattern, temp_pattern)

            MTS, smooth_metric_ts,periodicity_metric_ts,related_metric_collections = generate_MTS(metric_num, smoothness, periodicity, correlation, all_pattern)
            MTS_values = []
            for metric in MTS:
                MTS_values.append([round(value, 2) for value in metric.values.tolist()])
            # print(len(MTS_values[0]))
            
            label = [0] * len(MTS_values[0])
            # json.dump(, MTS_values)
            save_dict = {
                labName: {
                    "metricsNum": metric_num,
                    "smoothness": smoothness,
                    "periodicity": periodicity,
                    "correlation": correlation,
                    "smoothness_metric_index":smooth_metric_ts,
                    "periodicity_metric_index":periodicity_metric_ts,
                    "related_metric_index":related_metric_collections,
                    "data": MTS_values,
                    "label": label
                }
            }

            folder_path = f'../backend/practical_train_backend/src/main/resources/templateData/{username}'
            if not os.path.exists(folder_path):
                # 创建文件夹
                os.makedirs(folder_path)
            with open(f'../backend/practical_train_backend/src/main/resources/templateData/{username}/{labName}.json', 'w') as f:
                json.dump(save_dict, f)
            return "generate normal data successfully"
        except Exception as e:
            print(e)
            return "generate normal data error"


# test_gn()