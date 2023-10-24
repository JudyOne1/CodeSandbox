package com.judy.codesandbox;

import com.judy.codesandbox.model.ExecuteCodeRequest;
import com.judy.codesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;

/**
 * Java原生实现 直接复用模板
 *
 * @author Judy
 * @create 2023-10-23-17:50
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
