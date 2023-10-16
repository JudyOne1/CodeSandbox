package com.judy.codesandbox;

import cn.hutool.core.io.FileUtil;
import com.judy.codesandbox.model.ExecuteCodeRequest;
import com.judy.codesandbox.model.ExecuteCodeResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @author Judy
 * @create 2023-10-16-17:38
 */
public class JavaNativeCodeSandbox implements CodeSandbox {
    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";
    public static final String JAVA_CLASS_NAME = "Main.java";


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();

        String userDir = System.getProperty("user.dir");
        String CodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        if (FileUtil.exist(CodePathName)) {
            FileUtil.mkdir(CodePathName);
        }
        String userCodePathName = CodePathName + File.separator + UUID.randomUUID().toString();
        String userCodePath = userCodePathName + File.separator + JAVA_CLASS_NAME;
        //保存为文件
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        System.out.println("CodePathName: " + CodePathName);
        System.out.println("userCodePathName" + userCodePathName);
        System.out.println("userCodePath" + userCodePath);


        //编译
        String compileCMD = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCMD);
            int exitValue = compileProcess.waitFor();
            if (exitValue == 0) {
                //编译成功
                System.out.println("success");
                //获取控制台正常输出  输入流读
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
                String compileOutputLine;
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                //逐行获取输出结果
                while ((compileOutputLine = bufferedReader.readLine()) != null){
//                    System.out.println(compileOutputLine);
                    compileOutputStringBuilder.append(compileOutputLine);
                }
                System.out.println(compileOutputStringBuilder);
            } else {
                System.out.println("error code: " + exitValue);
                //获取控制台异常输出  错误流读
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                String errorCompileOutputLine;
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                //逐行获取输出结果
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null){
//                    System.out.println(errorCompileOutputLine);
                    compileOutputStringBuilder.append(errorCompileOutputLine);
                }
                System.out.println(compileOutputStringBuilder);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //执行
        for (String inputArgs : inputList) {
            String runCMD = String.format("java -cp %s Main %s",userCodePathName,inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCMD);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        //输出

        //清理

        //错误处理

        return null;
    }
}
