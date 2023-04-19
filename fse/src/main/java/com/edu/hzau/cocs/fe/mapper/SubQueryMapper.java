package com.edu.hzau.cocs.fe.mapper;

import com.edu.hzau.cocs.fe.pojo.*;
import com.edu.hzau.cocs.fe.pojo.datasource.KEGGPathwayMap;
import com.edu.hzau.cocs.fe.utils.Constants;
import com.edu.hzau.cocs.fe.utils.HMDBUtils;
import com.edu.hzau.cocs.fe.utils.KEGGUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author yue
 */
//@Component
@Service
@Slf4j
public class SubQueryMapper {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    KEGGUtils keggUtils;
    @Autowired
    HMDBUtils hmdbUtils;

    //biomed SubQueryMapper
    public List<DisDrugTarGenePath> getDrugByRareDis(String sql){
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DisDrugTarGenePath disDrugTarGenePath = new DisDrugTarGenePath();
            disDrugTarGenePath.setOrphanId(rs.getInt("ORPHAN_id"));
            disDrugTarGenePath.setOrphanName(String.valueOf(rs.getString("ORPHAN_name")));
            disDrugTarGenePath.setDrugId(String.valueOf(rs.getString("drug_id")));
            disDrugTarGenePath.setDrugName(String.valueOf(rs.getString("drug_name")));
            return disDrugTarGenePath;
        });

        //return List<disDrugTarGenePath>;
    }
    public List<DisDrugTarGenePath> getTarByDrug(String sql, List<DisDrugTarGenePath> disDrugTarGenePathList1) {
        List<DisDrugTarGenePath> disDrugTarGenePathList2 = new ArrayList<>();
        for (DisDrugTarGenePath disDrugTarGenePath :disDrugTarGenePathList1){
            //String sqlQuery = sql + "and druginfo.drug_id =  " + disDrugTarGenePath.getDrugId();
            String sqlQuery = sql + String.format(" and druginfo.drug_id = '%s'",disDrugTarGenePath.getDrugId());
            //System.out.println(sqlQuery); //
            List<Map<String, Object>> resMaps = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> map : resMaps) {
                DisDrugTarGenePath disDrugTarGenePathNew =disDrugTarGenePath.clone();
                disDrugTarGenePathNew.setDrugName(String.valueOf(map.get("drug_name")));
                disDrugTarGenePathNew.setTargetId(String.valueOf(map.get("target_id")));
                disDrugTarGenePathNew.setTargetName(String.valueOf(map.get("target_name")));
                disDrugTarGenePathList2.add(disDrugTarGenePathNew);
            }
        }
        return disDrugTarGenePathList2;
    }
    public List<DisDrugTarGenePath> getGeneByTar(String sql, List<DisDrugTarGenePath> disDrugTarGenePathList1) {
        List<DisDrugTarGenePath> disDrugTarGenePathList2 = new ArrayList<>();
        for (DisDrugTarGenePath disDrugTarGenePath :disDrugTarGenePathList1){
            // String sqlQuery = sql + "and targetinfo.target_id = " + disDrugTarGenePath.getTargetId();
            String sqlQuery = sql + String.format(" and targetinfo.target_id = '%s'",disDrugTarGenePath.getTargetId());
            List<Map<String, Object>> resMaps = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> map : resMaps) {
                DisDrugTarGenePath disDrugTarGenePathNew =disDrugTarGenePath.clone();
                disDrugTarGenePathNew.setTargetName(String.valueOf(map.get("target_name")));
                disDrugTarGenePathNew.setGeneId(String.valueOf(map.get("gene_id")));
                disDrugTarGenePathNew.setGeneSymbol(String.valueOf(map.get("gene_symbol")));
                disDrugTarGenePathList2.add(disDrugTarGenePathNew);
            }
        }
        return disDrugTarGenePathList2;
    }
    public List<DisDrugTarGenePath> getPathByGene(String sql, List<DisDrugTarGenePath> disDrugTarGenePathList1) {
        List<DisDrugTarGenePath> disDrugTarGenePathList2 = new ArrayList<>();
        for (DisDrugTarGenePath disDrugTarGenePath :disDrugTarGenePathList1){
            //String sqlQuery = sql + "and geneinfo.gene_id = " + disDrugTarGenePath.getGeneId();
            String sqlQuery = sql + String.format(" and geneinfo.gene_id = '%s'",disDrugTarGenePath.getGeneId());
            List<Map<String, Object>> resMaps = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> map : resMaps) {
                DisDrugTarGenePath disDrugTarGenePathNew =disDrugTarGenePath.clone();
                disDrugTarGenePathNew.setGeneSymbol(String.valueOf(map.get("gene_symbol")));
                disDrugTarGenePathNew.setPathId(String.valueOf(map.get("path_id")));
                disDrugTarGenePathNew.setPathName(String.valueOf(map.get("path_name")));
                disDrugTarGenePathNew.setPathGraphUrl(String.valueOf(map.get("path_url")));
                disDrugTarGenePathList2.add(disDrugTarGenePathNew);
            }
        }
        return disDrugTarGenePathList2;
    }
   // biomed Over

    public List<SwineMicrobeGeneKeggRes> getMicrobeBySwine(String sql) {
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SwineMicrobeGeneKeggRes swineMicrobeGeneKeggRes = new SwineMicrobeGeneKeggRes();
            swineMicrobeGeneKeggRes.setSwineIndex(rs.getInt("swine_index"));
            swineMicrobeGeneKeggRes.setMicrobeId(rs.getInt("microbe_id"));
            swineMicrobeGeneKeggRes.setMicrobeName(String.valueOf(rs.getString("microbe_Name")));
            return swineMicrobeGeneKeggRes;
        });
    }

    public List<SwineMicrobeGeneKeggRes> getGeneByMicrobe(String sql, List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggResList) {
        List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggAns = new ArrayList<>();
        for (SwineMicrobeGeneKeggRes swineMicrobeGeneKeggRes : swineMicrobeGeneKeggResList) {
            String sqlQuery = sql + " and microbe.microbe_id = " + swineMicrobeGeneKeggRes.getMicrobeId();
            List<Map<String, Object>> resMaps = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> map : resMaps) {
                SwineMicrobeGeneKeggRes swineMicrobeGeneKeggResNew = swineMicrobeGeneKeggRes.clone();
                swineMicrobeGeneKeggResNew.setNcbiGeneId(Integer.parseInt((String) map.get("NCBI_gene_id")));
                swineMicrobeGeneKeggResNew.setGeneSymbol(String.valueOf(map.get("Gene_Symbol")));
                swineMicrobeGeneKeggAns.add(swineMicrobeGeneKeggResNew);
            }
        }
        return swineMicrobeGeneKeggAns;
    }

    public List<SwineMicrobeGeneKeggRes> getKeggByGene(String sql, List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggResList) {
        List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggAns = new ArrayList<>();
        for (SwineMicrobeGeneKeggRes swineMicrobeGeneKeggRes : swineMicrobeGeneKeggResList) {
            String sqlQuery = sql + " and gene.NCBI_gene_id = " + swineMicrobeGeneKeggRes.getNcbiGeneId();
            List<Map<String, Object>> resMaps = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> map : resMaps) {
                SwineMicrobeGeneKeggRes swineMicrobeGeneKeggResNew = swineMicrobeGeneKeggRes.clone();
                swineMicrobeGeneKeggResNew.setGeneKeggIndex((Integer) map.get("Gene_kegg_index"));
                swineMicrobeGeneKeggResNew.setGeneKeggPathway(String.valueOf(map.get("Gene_kegg_pathway")));
                swineMicrobeGeneKeggAns.add(swineMicrobeGeneKeggResNew);
            }
        }
        return swineMicrobeGeneKeggAns;
    }
    public List<SwineMicrobeGeneKeggRes> getKeggByGeneOnline(List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggResList)  {
        List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggResAns = new ArrayList<>();
        for (SwineMicrobeGeneKeggRes swineMicrobeGeneKeggRes : swineMicrobeGeneKeggResList) {
            Gene gene = new Gene(swineMicrobeGeneKeggRes.getGeneSymbol(), String.valueOf(swineMicrobeGeneKeggRes.getNcbiGeneId()));
            try {
                List<Gene> geneList = new ArrayList<>();
                geneList.add(gene);
                List<KEGGPathwayMap> keggPathwayMapList = keggUtils.getKeggPathwayMap(geneList);
                KEGGPathwayMap keggPathwayMap = keggPathwayMapList.get(0);
                SwineMicrobeGeneKeggRes swineMicrobeGeneKeggResNew = swineMicrobeGeneKeggRes.clone();
                swineMicrobeGeneKeggResNew.setGeneKeggPathway(keggPathwayMap.getPathwayMap().toString());
                swineMicrobeGeneKeggResAns.add(swineMicrobeGeneKeggResNew);
            } catch (REXPMismatchException | IOException | RserveException e) {
                log.warn("{} get gene pathway online fail.", gene.getGeneSymbol());
            }
        }
        return swineMicrobeGeneKeggResAns;
    }

    public List<SwineMetabolismHmdbRes> getMetabolismBySwine(String sql) {
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SwineMetabolismHmdbRes swineMetabolismHmdbRes = new SwineMetabolismHmdbRes();
            swineMetabolismHmdbRes.setSwineIndex(rs.getInt("swine_index"));
            swineMetabolismHmdbRes.setMetabolismIndex(rs.getInt("metabolism_index"));
            swineMetabolismHmdbRes.setMetabolismName(rs.getString("metabolism_name"));
            return swineMetabolismHmdbRes;
        });
    }

    public List<SwineMetabolismHmdbRes> getHmdbByMetabolism(String sql, List<SwineMetabolismHmdbRes> swineMetabolismHmdbResList) {
        List<SwineMetabolismHmdbRes> swineMetabolismHmdbResAns = new ArrayList<>();
        for (SwineMetabolismHmdbRes swineMetabolismHmdbRes : swineMetabolismHmdbResList) {
            String sqlQuery = sql + " and metabolism.metabolism_index = " + swineMetabolismHmdbRes.getMetabolismIndex();
            List<Map<String, Object>> resMaps = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> resMap : resMaps) {
                SwineMetabolismHmdbRes swineMetabolismHmdbResNew = swineMetabolismHmdbRes.clone();
                swineMetabolismHmdbResNew.setHmdbInfoIndex((Integer) resMap.get("hmdb_info_index"));
                swineMetabolismHmdbResNew.setHmdbPathway(String.valueOf(resMap.get("hmdb_pathway")));
                swineMetabolismHmdbResNew.setMetabolismHmdbInfoIndex(String.valueOf(resMap.get("metabolism_hmdb_info_index")));
                swineMetabolismHmdbResNew.setHmdbPathwayUrl(String.valueOf(resMap.get("kegg_url")));
                swineMetabolismHmdbResAns.add(swineMetabolismHmdbResNew);
            }
        }
        return swineMetabolismHmdbResAns;
    }

    public List<SwineMetabolismHmdbRes> getHmdbByMetabolismOnline(String sql, List<SwineMetabolismHmdbRes> swineMetabolismHmdbResList) {
        List<SwineMetabolismHmdbRes> swineMetabolismHmdbResAns = new ArrayList<>();
        for (SwineMetabolismHmdbRes swineMetabolismHmdbRes : swineMetabolismHmdbResList) {
            String sqlQuery = sql + " and metabolism.metabolism_index = " + swineMetabolismHmdbRes.getMetabolismIndex();
            List<Map<String, Object>> resMaps = jdbcTemplate.queryForList(sqlQuery);
            for (Map<String, Object> resMap : resMaps) {
                SwineMetabolismHmdbRes swineMetabolismHmdbResNew = swineMetabolismHmdbRes.clone();
                swineMetabolismHmdbResNew.setHmdbInfoIndex((Integer) resMap.get("hmdb_info_index"));
                swineMetabolismHmdbResNew.setMetabolismHmdbInfoIndex(String.valueOf(resMap.get("metabolism_hmdb_info_index")));
                swineMetabolismHmdbResAns.add(swineMetabolismHmdbResNew);
            }
        }
        for (SwineMetabolismHmdbRes swineMetabolismHmdbResAn : swineMetabolismHmdbResAns) {
            List<String> hmdbid = new ArrayList<>();
            hmdbid.add(swineMetabolismHmdbResAn.getMetabolismHmdbInfoIndex());
            List<HMDBEntity> hmdbEntity = hmdbUtils.getHmdbEntity(hmdbid);
            swineMetabolismHmdbResAn.setHmdbPathway(hmdbEntity.get(0).getPathway());
            swineMetabolismHmdbResAn.setHmdbPathwayUrl(hmdbEntity.get(0).getPathway());
        }
        return swineMetabolismHmdbResAns;
    }



    public List<String> getTableStructure(String tableName) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(String.format("select * from %s limit 0", tableName));
        return Arrays.asList(sqlRowSet.getMetaData().getColumnNames());
    }
}
