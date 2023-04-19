package com.edu.hzau.cocs.fe.pojo.datalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author yue
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
    private String relationship;
    private Map<String, String> attributes;
}
