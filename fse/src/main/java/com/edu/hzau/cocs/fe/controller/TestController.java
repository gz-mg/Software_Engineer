package com.edu.hzau.cocs.fe.controller;

import com.edu.hzau.cocs.fe.mapper.SubQueryMapper;
import com.edu.hzau.cocs.fe.pojo.DisDrugTarGenePath;
import com.edu.hzau.cocs.fe.pojo.datalog.Datalog;
import com.edu.hzau.cocs.fe.service.DatalogParser;
import com.edu.hzau.cocs.fe.service.SubQueryService;
import com.edu.hzau.cocs.fe.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description
 * @Author yue
 * @Date 2023/3/14 22:05
 */
@Controller
public class TestController {
    @Autowired
    SubQueryService subQueryService;
    @Autowired
    DatalogParser datalogParser;
    @Autowired
    SubQueryMapper subQueryMapper;

    @RequestMapping("/test")
    @ResponseBody
    public List<DisDrugTarGenePath> testQ1(){
        String datalog5 = "?(ORPHAN_name,Drug_name,Target_name,Gene_name,Pathway_name,Pathway_graph_url):-\n" +
                "relationship:cures_of(ORPHAN_id,Drug_id,<cures_of>),\n" +
                "attribute:ORPHAN_name(ORPHAN_id,<Benign adult familial myoclonic epilepsy>),\n" +
                "attribute:drug_name(Drug_id,Drug_name),\n" +
                // "attribute:drug_type(Drug_id,<Protein>),\n" +
                "relationship:has_functions_in(Drug_id,Target_id,<has_functions_in>),\n" +
                "attribute:target_name(Target_id,Target_name),\n" +
                "relationship:is_encoded_by_gene(Target_id,Gene_id),\n" +
                "attribute:gene_symbol(Gene_id,Gene_symbol),\n" +
                "relationship:takes_part_in(Gene_id,Pathway_id,takes_part_in),\n" +
                "attribute:pathway_name(Pathway_id,Pathway_name),\n" +
                "attribute:pathway_graph(Pathway_id,Pathway_graph_url).";
        Datalog datalog = datalogParser.parseDatalog(datalog5);
        //System.out.println(datalog);
        //String sql = Constants.CURES_OF;
        //System.out.println(sql);
        //List<DisDrugTarGenePath> drugByRareDis = subQueryMapper.getDrugByRareDis(sql);
        //System.out.println(drugByRareDis);
        List<DisDrugTarGenePath> disDrugTarGenePathList1 = subQueryService.curesOf(datalog);
        List<DisDrugTarGenePath> disDrugTarGenePathList2 = subQueryService.hasFunctionsIn(datalog,disDrugTarGenePathList1);
        List<DisDrugTarGenePath> disDrugTarGenePathList3 = subQueryService.isEncodedByGene(datalog,disDrugTarGenePathList2);
        List<DisDrugTarGenePath> disDrugTarGenePathList4 = subQueryService.takesPartIn(datalog,disDrugTarGenePathList3);
        //System.out.println(disDrugTarGenePathList4);
        for (DisDrugTarGenePath disDrugTarGenePath : disDrugTarGenePathList4) {
            System.out.println(disDrugTarGenePath);
        }
        return disDrugTarGenePathList4;
    }
}
