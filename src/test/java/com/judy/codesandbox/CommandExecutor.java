package com.judy.codesandbox;

import org.apache.tomcat.util.buf.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {
    public static void main(String[] args) {
        try {
            // 要执行的命令
            String command = "ping www.baidu.com";
            
            // 使用Runtime.getRuntime().exec()执行命令
            Process runProcess = Runtime.getRuntime().exec(command);
            
            // 等待程序执行，获取错误码
            int exitValue = runProcess.waitFor();
     
            // 正常退出
            if (exitValue == 0) {
                //获取命令执行的输出流  分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
            } else {
                // 异常退出
                System.out.println("执行失败，错误码： " + exitValue);
                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                // 分批获取进程的错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));

                List<String> errorOutputStrList = new ArrayList<>();
                // 逐行读取
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorOutputStrList.add(errorCompileOutputLine);
                }
            }
            System.out.println("命令执行完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
