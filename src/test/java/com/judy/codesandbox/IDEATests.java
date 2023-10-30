package com.judy.codesandbox;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import com.judy.codesandbox.config.NullValueHandlingTypeAdapter;
import com.judy.codesandbox.model.ParamsInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
@Slf4j
class IDEATests {

    @Test
    void contextLoads() {
//        String piecesStr = "[5,1,null,4,3,3,6]";
        String json = "[[88,18],[null],[6,66]]";
//        int[][] pieces = parseStringTo2Array(piecesStr);
//        int[] pieces = parseStringTo1Array(piecesStr);
//        System.out.println("pieces: " + Arrays.deepToString(pieces));
//        String json = "[5,1,4,null,null,3,6]";

//        System.out.println("array "+ Arrays.deepToString(array));
    }

    //将[[88],[15]]转换为二维数组
    public static Integer[][] parseStringTo2Array(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new NullValueHandlingTypeAdapter())
                .create();
        Integer[][] array = gson.fromJson(json, Integer[][].class);
        return array;
    }

    //将[5,1,4,null,null,3,6]转换为一维数组
    public static Integer[] parseStringTo1Array(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new NullValueHandlingTypeAdapter())
                .create();
        Integer[] array = gson.fromJson(json, Integer[].class);
        return array;
    }




    public static void solute(int[] arr, int a) {
        System.out.println(Arrays.toString(arr));
        System.out.println(a);
    }

    public static ArrayList<ParamsInfo> getParams(String input) {
        JSONConfig jsonConfig = new JSONConfig();
        jsonConfig.setIgnoreNullValue(false);
//        HashMap<String, String> map = new HashMap<>();
        ArrayList<ParamsInfo> paramsInfos = new ArrayList<>();
        //分割成多段
        String[] totalSplit = input.split(", ");

        //有多个
        if (totalSplit.length > 0) {
            for (int i = 0; i < totalSplit.length; i++) {
                //获取参数名 name
                String name;
                String[] subSplit = totalSplit[i].split(" = ");
                name = subSplit[0];
                //传参只有一个值
                //取数组字符串
                StringBuilder stringBuilder = new StringBuilder(subSplit[1]);
                int i1 = stringBuilder.indexOf("[");
                int i2 = stringBuilder.lastIndexOf("]");

                if (i2 != -1 && i1 != -1) {
                    //是数组
                    String arr = stringBuilder.substring(i1, i2 + 1).toString();
                    log.info("arr: " + arr);
                    //再去取，看看是不是二维数组
                    stringBuilder = new StringBuilder(arr);
                    i1 = stringBuilder.indexOf("[");
                    i2 = stringBuilder.lastIndexOf("]");
                    //剔除最外围的圈 [ ]
                    String subArr = stringBuilder.substring(1,arr.length()-1).toString();
                    stringBuilder = new StringBuilder(subArr);
                    i1 = stringBuilder.indexOf("[");
                    i2 = stringBuilder.lastIndexOf("]");
                    log.info("subArr: " + subArr);
                    if (i2 != -1 && i1 != -1) {
                        log.error("arr: " + arr);
                        //arr是二维数组
                        Integer[][] arr2 = parseStringTo2Array(arr);
                        ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr2,jsonConfig), arr2.getClass());
                        paramsInfos.add(paramsInfo);
                    } else {
                        log.error("arr: " + arr);
                        //arr是一维数组
                        Integer[] arr1 = parseStringTo1Array(arr);
                        ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr1,jsonConfig), arr1.getClass());
                        paramsInfos.add(paramsInfo);
                    }
                } else {
                    //不是数组 取值
                    String value = subSplit[1];
                    stringBuilder = new StringBuilder(input);
                    i1 = stringBuilder.indexOf("\"");
                    i2 = stringBuilder.lastIndexOf("\"");
                    if (i2 == -1 && i1 == -1) {
                        //不是字符串 强转成int
                        Integer valueInt = Integer.parseInt(value);
                        ParamsInfo paramsInfo = new ParamsInfo(name, valueInt.toString(), valueInt.getClass());
                        paramsInfos.add(paramsInfo);
                    }else {
                        //是字符串
                        ParamsInfo paramsInfo = new ParamsInfo(name, value, value.getClass());
                        paramsInfos.add(paramsInfo);
                    }
                }
            }
        } else {
            //获取参数名 name
            String name;
            String[] nameSplit = input.split(" = ");
            name = nameSplit[0];
            //传参只有一个值
            //取数组字符串
            StringBuilder stringBuilder = new StringBuilder(input);
            int i1 = stringBuilder.indexOf("[");
            int i2 = stringBuilder.lastIndexOf("]");

            if (i2 != -1 && i1 != -1) {
                //是数组
                String arr = stringBuilder.substring(i1, i2 + 1).toString();
                //再去取，看看是不是二维数组
                stringBuilder = new StringBuilder(input);
                i1 = stringBuilder.indexOf("[");
                i2 = stringBuilder.lastIndexOf("]");
                if (i2 != -1 && i1 != -1) {
                    //arr是二维数组
                    Integer[][] arr2 = parseStringTo2Array(arr);
                    ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr2,jsonConfig), arr2.getClass());
                    paramsInfos.add(paramsInfo);
                } else {
                    //arr是一维数组
                    Integer[] arr1 = parseStringTo1Array(arr);
                    ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr1,jsonConfig), arr1.getClass());
                    paramsInfos.add(paramsInfo);
                }
            } else {
                //不是数组 取值
                String value = nameSplit[1];
                stringBuilder = new StringBuilder(input);
                i1 = stringBuilder.indexOf("\"");
                i2 = stringBuilder.lastIndexOf("\"");
                if (i2 == -1 && i1 == -1) {
                    //不是字符串 强转成int
                    Integer valueInt = Integer.parseInt(value);
                    ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(valueInt,jsonConfig), valueInt.getClass());
                    paramsInfos.add(paramsInfo);
                }
            }
        }
        return paramsInfos;
    }


    //main后面直接return solute（）  之家进行拼接
    public static void main(String[] args) {
        //gifts = [5,1,4,null,null,3,6], k = 4
        String input = "gifts = [5,1,4,null,null,3,6], k = \"aabb\"";
        //核心代码模式测试  可以使用args传入字符串参数

        //1获取参数名 gifts 和 k
        //1.1先通过正则分离  找找有没有“, ” KMP？
        ArrayList<ParamsInfo> params = getParams(input);
        for (ParamsInfo param : params) {
            System.out.println(param.toString());
        }
        //2获取参数值 [5,1,4,null,null,3,6] 和 4

        //3组装参数  条件判断参数类型  反射获取？

        //参数列表

//        try {
//            // 获取目标类的Class对象
//            Class<?> targetClass = IDEATests.class;
//
//            // 获取目标方法的Method对象
//            Method targetMethod = targetClass.getDeclaredMethod("solute", int[].class, int.class);
//
//            // 获取目标方法的参数
//            Parameter[] parameters = targetMethod.getParameters();
//
//            // 输出参数信息
//            for (Parameter parameter : parameters) {
//                System.out.println("参数名：" + parameter.getName());
//                System.out.println("参数类型：" + parameter.getType());
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }


//        solute(arr,a);


//        int[] gifts = parseGifts(input);
//        int k = parseK(input);
//        System.out.println("gifts: " + Arrays.toString(gifts));
//        System.out.println("k: " + k);
//        //--------------------------------------
//        String piecesStr = "[[88],[15]]";


//        stringBuilder = new StringBuilder(piecesStr);
//        i1 = stringBuilder.indexOf("[");
//        i2 = stringBuilder.lastIndexOf("]");
//        arr = stringBuilder.substring(i1, i2 + 1).toString();
//        System.out.println(arr);
//        int[][] pieces = parseStringTo2Array(piecesStr);
//        System.out.println("pieces: " + Arrays.deepToString(pieces));
    }


    //将[[88],[15]]转换为二维数组
    public static int[][] parseStringTo2ArrayNoNull(String json) {
        log.warn("parseStringTo2Array: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<int[][]>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    //将[5,1,4,null,null,3,6]转换为一维数组
    public static int[] parseStringTo1ArrayNoNull(String json) {
        log.warn("parseStringTo1Array: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<int[]>() {
        }.getType();
        return gson.fromJson(json, type);
    }



}
