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

# from model import ClusterData, Cluster, Indicator, Node, NodeSingleData, Disk, NodeMultipleData

ai = Blueprint("anomaly_inject", __name__)


def hello_world():
    return 'anomaly_inject'

def change_feature_values(feature_value1,feature_value2,feature_value3,metric_num):
    """
    :param feature_value1 数据特征1
    :param feature_value2 数据特征2
    :param feature_value3 数据特征3
    :param metric_num 指标数
    """
    #计算平滑曲线数
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
        return (feature_value1,feature_value2,feature_value3)
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
    return (feature_value1,feature_value2,feature_value3, least_pattern_num)


def get_min_ratio(data, ratio, radius):
    
    return max(math.ceil(radius / (len(data))*100)/100, ratio)


# @ai.route('/test', strict_slashes=False, methods=["POST", "GET"])
def synthetic_anomaly(data, anomaly_info=None):

    np.random.seed(100)
    # print(len(data[0]))
    multivariate_data = syn.MultivariateDataGenerator(data)
    if anomaly_info is None:
        multivariate_data.point_global_outliers(dim_no=0, ratio=0.01, factor=3.5, radius=5)
        multivariate_data.point_contextual_outliers(dim_no=1, ratio=0.01, factor=5, radius=5)
        multivariate_data.collective_global_outliers(dim_no=2, ratio=0.1, facot = 10, radius=20, option='square', coef=1.5, noise_amp=0.03, level=20, freq=0.04, offset=0.0)
        multivariate_data.collective_trend_outliers(dim_no=4, ratio=0.3, factor=0.05, radius=20)
        multivariate_data.collective_seasonal_outliers(dim_no=3, ratio=0.2, factor=10, radius=100)
        return multivariate_data

    if len(anomaly_info["point_global_outliers"]["metric_index"]) > 0:
        for i in anomaly_info["point_global_outliers"]["metric_index"]:
            ratio_in=anomaly_info["point_global_outliers"]["ratio"]
            factor_in=anomaly_info["point_global_outliers"]["factor"]
            radius_in=anomaly_info["point_global_outliers"]["radius"]
            multivariate_data.point_global_outliers(dim_no=i, 
                                                    ratio=ratio_in, 
                                                    factor=factor_in, 
                                                    radius=radius_in)
    if len(anomaly_info["point_contextual_outliers"]["metric_index"]) > 0:
        for i in anomaly_info["point_contextual_outliers"]["metric_index"]:
            
            multivariate_data.point_contextual_outliers(dim_no=i, 
                                                    ratio=anomaly_info["point_contextual_outliers"]["ratio"], 
                                                    factor=anomaly_info["point_contextual_outliers"]["factor"], 
                                                    radius=anomaly_info["point_contextual_outliers"]["radius"])
    if len(anomaly_info["collective_global_outliers"]["metric_index"]) > 0:
        for i in anomaly_info["collective_global_outliers"]["metric_index"]:
            ratio_in=anomaly_info["collective_global_outliers"]["ratio"]
            factor_in=anomaly_info["collective_global_outliers"]["factor"]
            radius_in=anomaly_info["collective_global_outliers"]["radius"]
            shape_in = anomaly_info["collective_global_outliers"]["shape"]
            ratio_in = get_min_ratio(data[i], ratio_in, radius_in)
            multivariate_data.collective_global_outliers(dim_no=i, 
                                                    ratio=ratio_in, 
                                                    factor=factor_in, 
                                                    radius=radius_in, 
                                                    option=shape_in, coef=1.5, noise_amp=0.03, level=20, freq=0.04, offset=0.0)      
    if len(anomaly_info["collective_trend_outliers"]["metric_index"]) > 0:
        for i in anomaly_info["collective_trend_outliers"]["metric_index"]:
            multivariate_data.collective_trend_outliers(dim_no=i, 
                                                    ratio=anomaly_info["collective_trend_outliers"]["ratio"], 
                                                    factor=anomaly_info["collective_trend_outliers"]["factor"], 
                                                    radius=anomaly_info["collective_trend_outliers"]["radius"])
    if len(anomaly_info["collective_seasonal_outliers"]["metric_index"]) > 0:
        for i in anomaly_info["collective_seasonal_outliers"]["metric_index"]:
            multivariate_data.collective_seasonal_outliers(dim_no=i, 
                                                    ratio=anomaly_info["collective_seasonal_outliers"]["ratio"], 
                                                    factor=anomaly_info["collective_seasonal_outliers"]["factor"], 
                                                    radius=anomaly_info["collective_seasonal_outliers"]["radius"])
            
    return multivariate_data


# def test():
#     # data = MTS_list_values[0]["data"][0]
#     anomaly_data = synthetic_anomaly(MTS_list_values[0]["data"])
#     # plot_data(MTS_list_values[0]["data"])
#     # plot_data(anomaly_data.data[4])
#     # plot_data(anomaly_data.data_origin[4])
#     # print(are_arrays_unequal(anomaly_data.data[3],anomaly_data.data_origin[3]))
#     plot_data(anomaly_data.label)


def test_ai():
    sourceName = "normal_second"
    labName = "anomaly_second"
    anomaly_info = {
        "point_global_outliers": {
            "metric_index": [0,1],
            "ratio": 0.01,
            "factor": 3.5, 
            "radius": 5
        },
        "point_contextual_outliers": {
            "metric_index": [1,2,3],
            "ratio": 0.01,
            "factor": 3.5, 
            "radius": 5
        },
        "collective_global_outliers": {
            "metric_index": [4,5,6],
            "ratio": 0.01,
            "factor": 3.5, 
            "radius": 5
        },
        "collective_trend_outliers": {
            "metric_index": [5,6,7],
            "ratio": 0.01,
            "factor": 3.5, 
            "radius": 5
        },
        "collective_seasonal_outliers": {
            "metric_index": [1,2,3,5],
            "ratio": 0.01,
            "factor": 3.5, 
            "radius": 5
        }
    }
    print(anomaly_inject_data(sourceName, labName, anomaly_info))

def formmat_strarray(strarray):
    if len(strarray) > 0:
        strarray = strarray.split(',')
        return list(map(int, strarray))
    else:
        return []
    
def format_float(number):
    if len(number) > 0:
        return float(number)
    else:
        return 0.0

@ai.route('/inject', strict_slashes=False, methods=["POST", "GET"])
def anomaly_inject_data():
    if request.method == "POST":
        # try:
            temp_shape_choice = []
            
            sourceName = request.form["sourceName"]
            labName = request.form["labName"]

            shape_list = ['square', 'triangle']
            temp_shape_choice = formmat_strarray(request.form["patterns"])
            if len(temp_shape_choice) == 1:
                 shape_choice = shape_list[temp_shape_choice[0]]
            else:
                shape_choice = rd.choice(shape_list)

            anomaly_info = {
                "point_global_outliers": {
                    "metric_index": formmat_strarray(request.form["globalIndex"]),
                    "ratio":format_float(request.form["globalRatio"]),
                    "factor": format_float(request.form["globalFactor"]), 
                    "radius": format_float(request.form["globalRadius"])
                },
                "point_contextual_outliers": {
                    "metric_index": formmat_strarray(request.form["contextIndex"]),
                    "ratio":format_float(request.form["contextRatio"]),
                    "factor": format_float(request.form["contextFactor"]), 
                    "radius": format_float(request.form["contextRadius"])
                },
                "collective_global_outliers": {
                    "metric_index": formmat_strarray(request.form["patternIndex"]),
                    "ratio":format_float(request.form["patternRatio"]),
                    "factor": format_float(request.form["patternFactor"]), 
                    "radius": format_float(request.form["patternRadius"]),
                    "shape": shape_choice
                },
                "collective_trend_outliers": {
                    "metric_index": formmat_strarray(request.form["trendIndex"]),
                    "ratio":format_float(request.form["trendRatio"]),
                    "factor": format_float(request.form["trendFactor"]), 
                    "radius": format_float(request.form["trendRadius"])
                },
                "collective_seasonal_outliers": {
                    "metric_index": formmat_strarray(request.form["seasonalIndex"]),
                    "ratio":format_float(request.form["seasonalRatio"]),
                    "factor": format_float(request.form["seasonalFactor"]), 
                    "radius": format_float(request.form["seasonalRadius"])
                }
            }

            with open(f'../frontend/static/synthetic/{sourceName}.json','r') as f:
                MTS_list_values = json.load(f)

            # MTS_list_values = json.loads('../../../frontend/static/synthetic/labName.json')
            anomaly_data = synthetic_anomaly(MTS_list_values[sourceName]["data"], anomaly_info)

            metric_num = MTS_list_values[sourceName]['metricsNum']
            smoothness = MTS_list_values[sourceName]['smoothness']
            periodicity = MTS_list_values[sourceName]['periodicity']
            correlation = MTS_list_values[sourceName]['correlation']
            smoothness_metric_index = MTS_list_values[sourceName]['smoothness_metric_index']
            periodicity_metric_index = MTS_list_values[sourceName]['periodicity_metric_index']
            related_metric_index = MTS_list_values[sourceName]['related_metric_index']
            # plot_data(MTS_list_values['labName']["data"])

            # plot_data(anomaly_data.data[4])
            # plot_data(anomaly_data.data_origin[4])
            # print(are_arrays_unequal(anomaly_data.data[3],anomaly_data.data_origin[3]))
            # plot_data(anomaly_data.label)

            save_dict = {
                labName: {
                    "metricsNum": metric_num,
                    "smoothness": smoothness,
                    "periodicity": periodicity,
                    "correlation": correlation,
                    "smoothness_metric_index":smoothness_metric_index,
                    "periodicity_metric_index":periodicity_metric_index,
                    "related_metric_index":related_metric_index,
                    "data": anomaly_data.data.tolist(),
                    "label": anomaly_data.label.tolist()
                }
            }
            # return
            save_path = f'../frontend/static/synthetic/{labName}.json'
            with open(save_path, 'w') as f:
                json.dump(save_dict, f)

            return "anomaly inject successfully"
        # except Exception as e:
        #     print(e,"!!!!")
        #     return "anomaly inject error"


# test_ai()
# test_all()