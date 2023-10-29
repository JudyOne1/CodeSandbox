package com.judy.codesandbox;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SpringBootTest
class IDEATests {

    @Test
    void contextLoads() {

    }

    public static void solute(int[] arr,int a) {
        System.out.println(Arrays.toString(arr));
        System.out.println(a);
    }
    //main后面直接return solute（）  之家进行拼接
    public static void main(String[] args) {
        //gifts = [5,1,4,null,null,3,6], k = 4
        String input = "gifts = [5,1,4,null,null,3,6], k = 4";
        //核心代码模式测试  可以使用args传入字符串参数

        //获取参数名 gifts 和 k
        //先通过正则分离  找找有没有“, ”
        StringBuilder stringBuilder1 = new StringBuilder();


        //获取参数值 [5,1,4,null,null,3,6] 和 4

        //组装参数


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

        //分割成多段
        String[] split = input.split(", ");
        //有多个
        if (split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                //取数组字符串
                StringBuilder stringBuilder = new StringBuilder(input);
                int i1 = stringBuilder.indexOf("[");
                int i2 = stringBuilder.lastIndexOf("]");
                String arr;
                if (i2 != -1 && i1 != -1) {
                    //是数组
                    arr = stringBuilder.substring(i1, i2 + 1).toString();
                    //再去取，看看是不是二维数组
                    stringBuilder = new StringBuilder(input);
                    i1 = stringBuilder.indexOf("[");
                    i2 = stringBuilder.lastIndexOf("]");
                    if (i2 != -1 && i1 != -1) {
                        //arr是二维数组
                        int[][] arr2 = parseStringTo2Array(arr);
                    }else {
                        //arr是一维数组
                        int[] arr1 = parseStringTo1Array(arr);
                    }

                }else {
                    //不是数组

                }
            }
        } else {
            //传参只有一个值
            //取数组字符串
            StringBuilder stringBuilder = new StringBuilder(input);
            int i1 = stringBuilder.indexOf("[");
            int i2 = stringBuilder.lastIndexOf("]");
            String arr;
            if (i2 != -1 && i1 != -1) {
                //是数组
                arr = stringBuilder.substring(i1, i2 + 1).toString();
                //再去取，看看是不是二维数组
                stringBuilder = new StringBuilder(input);
                i1 = stringBuilder.indexOf("[");
                i2 = stringBuilder.lastIndexOf("]");
                if (i2 != -1 && i1 != -1) {
                    //arr是二维数组
                    int[][] arr2 = parseStringTo2Array(arr);
                } else {
                    //arr是一维数组
                    int[] arr1 = parseStringTo1Array(arr);
                }
            }


        }
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
    public static int[][] parseStringTo2Array(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<int[][]>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    //将[5,1,4,null,null,3,6]转换为一维数组
    public static int[] parseStringTo1Array(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<int[]>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}
