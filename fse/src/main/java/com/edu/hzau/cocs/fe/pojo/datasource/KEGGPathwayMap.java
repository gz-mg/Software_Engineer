package com.edu.hzau.cocs.fe.pojo.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Author yue
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KEGGPathwayMap {
    private String geneSymbol;
    private List<String> pathwayMap;
    private String brite;
}
