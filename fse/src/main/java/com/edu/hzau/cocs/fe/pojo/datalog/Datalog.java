package com.edu.hzau.cocs.fe.pojo.datalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author yue
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Datalog {
    private List<String> header;
    private Map<String, Relationship> relationships;
}
