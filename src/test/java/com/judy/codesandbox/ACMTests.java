package com.judy.codesandbox;

import cn.hutool.core.util.StrUtil;
import com.judy.codesandbox.model.ExecuteMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;

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
        String userCodeParentPath = "G:\\MyDocuments\\1知识星球\\OJ\\code-sandbox\\tmpCode";

        String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main", userCodeParentPath);
        Process runProcess = Runtime.getRuntime().exec(runCmd);
        String inputStr = "A man, a plan, a canal: Panama";
//        System.out.println(inputStr);
        ExecuteMessage executeMessage = runInteractProcessAndGetMessage(runProcess, inputStr);
        System.out.println(executeMessage);
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
