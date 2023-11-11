package com.judy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;

//@Slf4j
class Solution {
    public static void main(String[] args) {

        //输入参数：gifts = [5,1,4,null,null,3,6], k = 4
        String input = args[0];
        //核心代码模式测试 -> 使用args传入字符串参数

        //获取参数名、参数类型、参数值 gifts 和 k
//        ArrayList<String> params = getParams(input);

        ArrayList<ParamsInfo> params = getParamsByClass(input);
        //获取目标方法的参数信息
        Parameter[] ParameterInfos = reflectMethod("solute", params);
        if (ParameterInfos == null) {
            throw new RuntimeException("出现未知错误");
        }
        if (params.size() != ParameterInfos.length) {
            throw new RuntimeException("系统出现未知错误");
        }
//-------------------------------------------------------------
//        int[] gifts;
//        int k;
        int i = 0;
//        gifts = JAVAparseStringTo1ArrayNoNull(params.get(i++).getValue());
//        k = Integer.parseInt(params.get(i).getValue());
        System.out.println(solute(JAVAparseStringTo1ArrayNoNull(params.get(i++).getValue()), Integer.parseInt(params.get(i).getValue())));
//--------------------------------------------------------------
    }

    public static int solute(int[] gifts, int k) {
        // 用户编写的代码
        System.out.println(Arrays.toString(gifts));
        System.out.println(k);
        return 10086;
    }
//下面是自己编写的getParams()方法、reflectMethod()方法、JAVAparseStringTo1ArrayNoNull()方法

    static class ParamsInfo {
        String name;
        String value;
        Class className;

        public ParamsInfo(String name, String value, Class className) {
            this.name = name;
            this.value = value;
            this.className = className;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Class getClassName() {
            return className;
        }

        public void setClassName(Class className) {
            this.className = className;
        }
    }

    /**
     * 获取参数名、参数类型、参数值 如gifts 和 k
     *
     * @param input 输入字符串
     * @return 封装成类，此类相当于定义一个参数
     */
    public static ArrayList<String> getParams(String input) {

        ArrayList<String> paramsInfos = new ArrayList<>();
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
//                    log.info("arr: " + arr);
                    //再去取，看看是不是二维数组
                    stringBuilder = new StringBuilder(arr);
                    i1 = stringBuilder.indexOf("[");
                    i2 = stringBuilder.lastIndexOf("]");
                    //剔除最外围的圈 [ ]
                    String subArr = stringBuilder.substring(1, arr.length() - 1).toString();
                    stringBuilder = new StringBuilder(subArr);
                    i1 = stringBuilder.indexOf("[");
                    i2 = stringBuilder.lastIndexOf("]");
//                    log.info("subArr: " + subArr);
                    if (i2 != -1 && i1 != -1) {
//                        log.error("arr: " + arr);
                        //arr是二维数组
                        int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);
//                        ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr2, jsonConfig), arr2.getClass());
                        paramsInfos.add(Arrays.deepToString(arr2));
                    } else {
//                        log.error("arr: " + arr);
                        //arr是一维数组
                        int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);
//                        ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr1, jsonConfig), arr1.getClass());
                        paramsInfos.add(Arrays.toString(arr1));
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
//                        ParamsInfo paramsInfo = new ParamsInfo(name, valueInt.toString(), valueInt.getClass());
                        paramsInfos.add(String.valueOf(valueInt));
                    } else {
                        //是字符串
//                        ParamsInfo paramsInfo = new ParamsInfo(name, value, value.getClass());
                        paramsInfos.add(value);
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
                    int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);
//                    ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr2, jsonConfig), arr2.getClass());
                    paramsInfos.add(Arrays.deepToString(arr2));
                } else {
                    //arr是一维数组
                    int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);
//                    ParamsInfo paramsInfo = new ParamsInfo(name, JSONUtil.toJsonStr(arr1, jsonConfig), arr1.getClass());
                    paramsInfos.add(Arrays.toString(arr1));
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
//                        ParamsInfo paramsInfo = new ParamsInfo(name, valueInt.toString(), valueInt.getClass());
                    paramsInfos.add(String.valueOf(valueInt));
                } else {
                    //是字符串
//                        ParamsInfo paramsInfo = new ParamsInfo(name, value, value.getClass());
                    paramsInfos.add(value);
                }
            }
        }
        return paramsInfos;
    }

    public static Parameter[] reflectMethod(String methodName, ArrayList<ParamsInfo> params) {
        try {
            // 获取目标类的Class对象
            Class<?> targetClass = Solution.class;

            // 获取目标方法的Method对象
            Class<?>[] parameterTypes = new Class<?>[params.size()];
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i).getClassName() == Integer[].class) {
                    parameterTypes[i] = int[].class;
                } else if (params.get(i).getClassName() == Integer[][].class) {
                    parameterTypes[i] = int[][].class;
                } else if (params.get(i).getClassName() == Integer.class) {
                    parameterTypes[i] = int.class;
                } else {
                    parameterTypes[i] = params.get(i).getClassName();
                }
            }

            Method targetMethod = targetClass.getDeclaredMethod(methodName, parameterTypes);

            // 获取目标方法的参数
            Parameter[] parameters = targetMethod.getParameters();

            return parameters;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

/*public static int[] parseStringTo1ArrayNoNull(String json) throws JSONException {
    JSONArray jsonArray = new JSONArray(json);
    int[] result = new int[jsonArray.length()];
    for (int i = 0; i < jsonArray.length(); i++) {
        result[i] = jsonArray.getInt(i);
    }
    return result;
}

    public static int[][] parseStringTo2ArrayNoNull(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        int[][] result = new int[jsonArray.length()][];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray innerArray = jsonArray.getJSONArray(i);
            int[] innerResult = new int[innerArray.length()];
            for (int j = 0; j < innerArray.length(); j++) {
                innerResult[j] = innerArray.getInt(j);
            }
            result[i] = innerResult;
        }
        return result;
    }*/

    public static int[] JAVAparseStringTo1ArrayNoNull(String json) {
        // 将json字符串转换为数组元素字符串
        String[] arrStr = json.replace("[", "").replace("]", "").split(",");

        // 创建一个int数组来存储转换后的整数
        int[] arrInt = new int[arrStr.length];

        // 将数组元素字符串转换为int类型
        for (int i = 0; i < arrStr.length; i++) {
            arrInt[i] = Integer.parseInt(arrStr[i].trim());
        }

        return arrInt;
    }

    public static int[][] JAVAparseStringTo2ArrayNoNull(String json) {
        // 将json字符串转换为二维数组元素字符串
        String[] arrStr1 = json.replace("[[", "").replace("]]", "").split("\\],\\[");

        // 创建一个二维int数组来存储转换后的整数
        int[][] arrInt = new int[arrStr1.length][];

        // 将数组元素字符串转换为int类型
        for (int i = 0; i < arrStr1.length; i++) {
            String[] arrStr2 = arrStr1[i].split(",");
            arrInt[i] = new int[arrStr2.length];
            for (int j = 0; j < arrStr2.length; j++) {
                arrInt[i][j] = Integer.parseInt(arrStr2[j].trim());
            }
        }

        return arrInt;
    }

    /**
     * 获取参数名、参数类型、参数值 如gifts 和 k
     *
     * @param input 输入字符串
     * @return 封装成类，此类相当于定义一个参数
     */
    public static ArrayList<ParamsInfo> getParamsByClass(String input) {

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
//                    log.info("arr: " + arr);
                    //再去取，看看是不是二维数组
                    stringBuilder = new StringBuilder(arr);
                    i1 = stringBuilder.indexOf("[");
                    i2 = stringBuilder.lastIndexOf("]");
                    //剔除最外围的圈 [ ]
                    String subArr = stringBuilder.substring(1, arr.length() - 1).toString();
                    stringBuilder = new StringBuilder(subArr);
                    i1 = stringBuilder.indexOf("[");
                    i2 = stringBuilder.lastIndexOf("]");
//                    log.info("subArr: " + subArr);
                    if (i2 != -1 && i1 != -1) {
//                        log.error("arr: " + arr);
                        //arr是二维数组
                        int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);
                        ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.deepToString(arr2), arr2.getClass());
                        paramsInfos.add(paramsInfo);
                    } else {
//                        log.error("arr: " + arr);
                        //arr是一维数组
                        int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);
                        ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.toString(arr1), arr1.getClass());
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
                    } else {
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
//                        log.error("arr: " + arr);
                    //arr是二维数组
                    int[][] arr2 = JAVAparseStringTo2ArrayNoNull(arr);
                    ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.deepToString(arr2), arr2.getClass());
                    paramsInfos.add(paramsInfo);
                } else {
//                        log.error("arr: " + arr);
                    //arr是一维数组
                    int[] arr1 = JAVAparseStringTo1ArrayNoNull(arr);
                    ParamsInfo paramsInfo = new ParamsInfo(name, Arrays.toString(arr1), arr1.getClass());
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
                    ParamsInfo paramsInfo = new ParamsInfo(name, valueInt.toString(), valueInt.getClass());
                    paramsInfos.add(paramsInfo);
                } else {
                    //是字符串
                    ParamsInfo paramsInfo = new ParamsInfo(name, value, value.getClass());
                    paramsInfos.add(paramsInfo);
                }
            }
        }
        return paramsInfos;
    }


}