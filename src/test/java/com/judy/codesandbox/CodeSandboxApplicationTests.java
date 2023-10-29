package com.judy.codesandbox;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Scanner;


@SpringBootTest
class CodeSandboxApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        //-------拼接这一部分-----------
//        //String inputString = "1 2";
//        String inputString;
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String arg : args) {
//            stringBuilder.append(arg).append(" ");
//        }
//        stringBuilder.setLength(stringBuilder.length() - 1); // 删除最后一个空格
//        inputString = stringBuilder.toString();
//        System.setIn(new ByteArrayInputStream(inputString.getBytes()));

//        try {
//            File file = new File("input.txt");
//            System.setIn(new FileInputStream(file));
//            //--------------------------------
//            Scanner in = new Scanner(System.in);
//            while(in.hasNext())
//            {
//                int a = in.nextInt();
//                int b = in.nextInt();
//                System.out.println(a+b);
//            }
//            //----------------------------------
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        int[] nums = new int[0];
        try {
            File file = new File("input.txt");
            System.setIn(new FileInputStream(file));
            // 创建一个BufferedReader对象
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            // 读取第一行数据
            String line = br.readLine();
            // 将字符串根据空格进行分隔
            String[] strings = line.trim().split(" ");
            // 分别将其中的每个数值读出
            int n = Integer.parseInt(strings[0]);
            // 读取第二行数据
            line = br.readLine();
            strings = line.trim().split(" ");
            // 创建一个int型的数组用来储存第二行的多个数字
            nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = Integer.parseInt(strings[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 测试输入是否正确
        for (int num : nums) {
            System.out.print(num + " ");
        }


    }

}
