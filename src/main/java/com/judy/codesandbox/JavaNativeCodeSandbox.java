package com.judy.codesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.judy.codesandbox.model.ExecuteCodeRequest;
import com.judy.codesandbox.model.ExecuteCodeResponse;
import com.judy.codesandbox.model.ExecuteMessage;
import com.judy.codesandbox.model.JudgeInfo;
import com.judy.codesandbox.security.DefaultSecurityManager;
import com.judy.codesandbox.utils.ProcessUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Judy
 * @create 2023-10-16-17:38
 */
public class JavaNativeCodeSandbox implements CodeSandbox {
    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";
    public static final String JAVA_CLASS_NAME = "Main.java";
    public static final long TIME_OUT = 5000l;


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
//        System.setSecurityManager(new DefaultSecurityManager());
        String code = executeCodeRequest.getCode();
//        String language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();

        String userDir = System.getProperty("user.dir");
        // tempCode目录
        String CodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        if (FileUtil.exist(CodePathName)) {
            FileUtil.mkdir(CodePathName);
        }
        // UUID目录
        String userCodePathName = CodePathName + File.separator + UUID.randomUUID().toString();
        // Main.java 路径
        String userCodePath = userCodePathName + File.separator + JAVA_CLASS_NAME;
        //1.保存为文件
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        //文件父路径
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        System.out.println("CodePathName: " + CodePathName);
        System.out.println("userCodePathName" + userCodePathName);
        System.out.println("userCodePath" + userCodePath);
        System.out.println("userCodeParentPath" + userCodeParentPath);

//        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.command("ping www.baidu.com");
        //2.编译
        String compileCMD = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCMD);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //3.执行
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCMD = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodePathName, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCMD);
                //超时控制
                new Thread(()->{
                    try {
                        Thread.sleep(TIME_OUT);
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "执行");
                executeMessageList.add(executeMessage);
                System.out.println(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        //4. 整理为输出对象
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        // todo 扩展为每个用例都记录时间和内存
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 用户提交的代码执行中存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }
        // 正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        // 要借助第三方库来获取内存占用，非常麻烦，此处不做实现
//        judgeInfo.setMemory();
        executeCodeResponse.setJudgeInfo(judgeInfo);

        //5.文件清理
        if (userCodeFile.getParentFile() != null) {
            FileUtil.del(userCodePath);
        }

        //错误处理

        return null;
    }
}
