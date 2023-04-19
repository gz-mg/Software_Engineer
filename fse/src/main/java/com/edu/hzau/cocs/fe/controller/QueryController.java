package com.edu.hzau.cocs.fe.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.edu.hzau.cocs.fe.mapper.SubQueryMapper;
import com.edu.hzau.cocs.fe.pojo.Gene;
import com.edu.hzau.cocs.fe.pojo.datalog.Datalog;
import com.edu.hzau.cocs.fe.service.DatalogParser;
import com.edu.hzau.cocs.fe.service.QueryService;
import com.edu.hzau.cocs.fe.service.SubQueryService;
import com.edu.hzau.cocs.fe.utils.Constants;
import com.edu.hzau.cocs.fe.utils.KEGGUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Author yue
 */
@Slf4j
@RestController
public class QueryController {
    @Autowired
    private QueryService queryService;
@Autowired
    private SubQueryMapper subQueryMapper;

    @PostMapping("/query")
    public JSONArray query(@RequestBody JSONObject jsonObject) throws REXPMismatchException, IOException, RserveException {
        return queryService.query(jsonObject);
    }

}
