package com.judy.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Judy
 * @create 2023-10-11-16:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 接口信息
     */
    private String message;
    /**
     * 执行状态
     */
    private int status;
    /**
     * 输出结果
     */
    private List<String> outputList;
    /**
     * 执行信息
     */
    private JudgeInfo judgeInfo;
}
