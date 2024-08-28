package com.example.practical_train_backend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
public class JsonUtil {

    //json转对象
    public void main(String myPath){
//        String myPath = "templateData/normal_1.json";
        String path = JsonUtil.class.getClassLoader().getResource(myPath).getPath();
        String s = readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);
        System.out.println("data"+jobj.get("normal_1"));
    }

    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}