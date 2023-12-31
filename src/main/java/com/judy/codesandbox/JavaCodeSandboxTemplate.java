package com.judy.codesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.judy.codesandbox.model.ExecuteCodeRequest;
import com.judy.codesandbox.model.ExecuteCodeResponse;
import com.judy.codesandbox.model.ExecuteMessage;
import com.judy.codesandbox.model.JudgeInfo;
import com.judy.codesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Judy
 * @create 2023-10-23-17:32
 */
@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_ACM_JAVA_CLASS_NAME = "Main.java";
    private static final String GLOBAL_CCM_JAVA_CLASS_NAME = "Solution.java";

    private static final long TIME_OUT = 5000L;

    private static final int ERROR_EXECUTE = 3;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        Integer modeSelect = executeCodeRequest.getModeSelect();

        //1. 把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code, modeSelect);

        //2. 编译代码，得到 class 文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        if (compileFileExecuteMessage.getExitValue() != 0) {
            ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
            executeCodeResponse.setMessage("编译错误");
            executeCodeResponse.setStatus(0);
            executeCodeResponse.setOutputList(null);
            executeCodeResponse.setJudgeInfo(null);
            return executeCodeResponse;
        }
        System.out.println(compileFileExecuteMessage);

        //3. 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList, modeSelect);

        //4. 收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageList, modeSelect);

        //5. 文件清理
        boolean b = deleteFile(userCodeFile);
        if (!b) {
            log.error("deleteFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
        }
//        System.out.println(outputResponse);
        return outputResponse;
    }


    /**
     * 1. 把用户的代码保存为文件
     *
     * @param code 用户代码
     * @return
     */
    public File saveCodeToFile(String code, Integer modeSelect) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码目录是否存在，没有则新建目录
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath;
        if (modeSelect == 1) {
            userCodePath = userCodeParentPath + File.separator + GLOBAL_ACM_JAVA_CLASS_NAME;
        } else {
            userCodePath = userCodeParentPath + File.separator + GLOBAL_CCM_JAVA_CLASS_NAME;
        }

        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    /**
     * 2、编译代码
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        System.out.println(userCodeFile.getAbsolutePath());
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue() != 0) {
                //todo 编译错误后的异常处理
                //正确返回的errorMessage是什么？
                executeMessage.setMessage("编译错误");
                executeMessage.setErrorMessage("编译错误");
                return executeMessage;
//                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (Exception e) {
            //      return getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 3、执行文件，获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList, Integer modeSelect) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        if (inputList == null || inputList.isEmpty()) {
            //一般是cm模式
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Solution", userCodeParentPath);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = null;
                executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行CCMmode/CMmode");


                System.out.println(executeMessage);
                System.out.println("--------------运行完了--------------");
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException("执行错误", e);
            }
        } else {
            for (String inputArgs : inputList) {
                System.out.println(inputArgs);
                String runCmd;
                if (modeSelect == 1) {
                    runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main", userCodeParentPath);
                } else {
                    runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Solution %s", userCodeParentPath, inputArgs);
                }

                try {
                    Process runProcess = Runtime.getRuntime().exec(runCmd);
                    // 超时控制
                    new Thread(() -> {
                        try {
                            Thread.sleep(TIME_OUT);
                            runProcess.destroy();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                    ExecuteMessage executeMessage = null;
                    if (modeSelect == 1) {
                        //inputArgs参数由这里传入
                        executeMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess, inputArgs);
                    } else {
                        executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行CCMmode/CMmode");
                    }

                    System.out.println(executeMessage);
                    System.out.println("--------------运行完了--------------");
                    executeMessageList.add(executeMessage);
                } catch (Exception e) {
                    throw new RuntimeException("执行错误", e);
                }
            }
        }

        return executeMessageList;
    }

    /**
     * 4、获取输出结果
     *
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList, Integer modeSelect) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        // 取用时最大值，便于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 用户提交的代码执行中存在错误
                executeCodeResponse.setStatus(ERROR_EXECUTE);
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
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 5、删除文件
     *
     * @param userCodeFile
     * @return
     */
    public boolean deleteFile(File userCodeFile) {
        if (userCodeFile.getParentFile() != null) {
            String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }

    /**
     * 6、获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
