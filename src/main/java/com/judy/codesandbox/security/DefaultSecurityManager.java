package com.judy.codesandbox.security;

import java.security.Permission;

/**
 * @author Judy
 * @create 2023-10-18-9:48
 */
public class DefaultSecurityManager extends SecurityManager {

    @Override
    public void checkPermission(Permission perm) {
//        super.checkPermission(perm);
    }

    /**
     * 限制读权限
     * 很麻烦，需要自己添加白名单
     * @param file the system-dependent file name.
     */
    @Override
    public void checkRead(String file) {
        System.out.println(file);
//        if (file.contains("G:\\MyDocuments\\OJ\\code-sandbox")) {
//            return;
//        }
//        throw new SecurityException("checkRead 权限异常：" + file);
    }

    /**
     * 限制写文件权限
     *
     * @param file the system-dependent filename.
     */
    @Override
    public void checkWrite(String file) {
        throw new SecurityException("checkWrite 权限异常：" + file);
    }

    /**
     * @param cmd the specified system command.
     */
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    /**
     * 限制网络连接权限
     *
     * @param host the host name port to connect to.
     * @param port the protocol port to connect to.
     */
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
    }
}
