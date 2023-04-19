package com.edu.hzau.cocs.fe.utils;

import com.edu.hzau.cocs.fe.pojo.datalog.Datalog;
import com.edu.hzau.cocs.fe.pojo.datalog.Relationship;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yue
 */
@Slf4j
@Component
public class CommonUtils {
    static Map<String, String> map = new HashMap<>();
    static {
        map.put("is_host_of-changes_the_expression_by_microbiota-has_gene_kegg_info", "question-template-1");
        map.put("generates-has_hmdb_info", "question-template-2");
    }
    public String getQuestionTemplate(String datalogStr) {
        String body = datalogStr.substring(datalogStr.indexOf(":-") + 2, datalogStr.length() - 1).trim();
        String[] relationships = body.split("relationship:");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < relationships.length; i++) {
            if (i == 0) continue;
            if (i == relationships.length - 1) {
                sb.append(relationships[i], 0, relationships[i].indexOf("("));
            }else {
                sb.append(relationships[i], 0, relationships[i].indexOf("(")).append("-");
            }
        }
        String s = sb.toString();
        return map.getOrDefault(s, "null");
    }
}
