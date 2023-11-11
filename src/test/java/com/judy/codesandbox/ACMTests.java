package com.judy.codesandbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


/**
 * @author Judy
 * @create 2023-11-03-19:34
 */
@SpringBootTest
@Slf4j
public class ACMTests {
    /**
     *
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        String input = "10 5\n" +
                "1 2 3 6 7 9 11 22 44 50";
        ExecuteMessage executeMessage = runACMJudge("G:\\MyDocuments\\1知识星球\\OJ\\code-sandbox\\tmpCode", input);
        System.out.println(executeMessage.getMessage());
        double runtimeMemerary = getRuntimeMemerary();
        System.out.println(runtimeMemerary+"KB");
    }

    public static double getRuntimeMemerary(){
//        // 获取当前Java虚拟机的运行时对象
//        Runtime runtime = Runtime.getRuntime();
//
//        // 获取Java程序运行时占用的最大内存（以字节为单位）
//        long maxMemory = runtime.maxMemory();
//
//        // 将最大内存转换为合适的单位（例如KB、MB或GB）
//        double maxMemoryInKB = maxMemory / 1024.0;
//
//        // 打印结果
//        System.out.println("Java程序运行时占用的最大内存为：");
//        return maxMemoryInKB;
        // 获取 Java 虚拟机的运行时
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

        // 获取操作系统的管理器
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // 获取当前进程的 PID
        String processName = runtime.getName();
        String pid = processName.split("@")[0];

        // 获取该进程的 CPU 占用和内存占用
        double cpuUsage = os.getProcessCpuLoad();
        long memoryUsage = os.getTotalPhysicalMemorySize()-os.getFreePhysicalMemorySize();

        System.out.println("CPU 占用率: " + cpuUsage);
        System.out.println("内存占用: " + memoryUsage);
        return memoryUsage;
    }

    public static ExecuteMessage runACMJudge(String userCodeParentPath, String inputStr) throws IOException {

        String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main", userCodeParentPath);
        Process runProcess = Runtime.getRuntime().exec(runCmd);
        ExecuteMessage executeMessage = runInteractProcessAndGetMessage(runProcess, inputStr);
        return executeMessage;
    }

    final static class ExecuteMessage {

        private Integer exitValue;

        private String message;

        private String errorMessage;

        private Long time;

        private Long memory;


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

    }


    /**
     * 执行交互式进程并获取信息(scanner)
     * ACM模式
     *
     * @param runProcess
     * @param args
     * @return
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 向控制台输入程序
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            //TODO ACM模式输入功能完善

//            String[] s = args.split(" ");
//            String join = StrUtil.join("\n", s) + "\n";
            outputStreamWriter.write(args+"\n");
            // 相当于按了回车，执行输入的发送
            outputStreamWriter.flush();

            // 分批获取进程的正常输出
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());
            // 记得资源的释放，否则会卡死
            outputStreamWriter.close();
            outputStream.close();
            inputStream.close();
            runProcess.destroy();
        } catch (Exception e) {
            executeMessage.setErrorMessage("ERROR!!!");
            e.printStackTrace();
        }
        return executeMessage;
    }
}
