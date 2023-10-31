package com.judy.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyParameter {
    private String name;
    private Class<?> type;
    private Object value;

}
