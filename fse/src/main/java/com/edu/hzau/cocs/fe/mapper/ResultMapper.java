package com.edu.hzau.cocs.fe.mapper;

import com.alibaba.fastjson2.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author yue
 */
@Component
public class ResultMapper {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public JSONArray queryForJson(String sql) {
        return new JSONArray(jdbcTemplate.queryForList(sql));
    }
}
