package com.judy.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Judy
 * @create 2023-10-11-16:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {
    private String code;
    private String language;
    private List<String> inputList;
    /**
     * 选择的题目模式 1-ACM | 2-CCM | 4-counter
     */
    private Integer modeSelect;
}
