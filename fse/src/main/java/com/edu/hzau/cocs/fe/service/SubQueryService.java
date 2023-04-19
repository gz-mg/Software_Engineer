package com.edu.hzau.cocs.fe.service;

import  com.edu.hzau.cocs.fe.mapper.SubQueryMapper;
import com.edu.hzau.cocs.fe.pojo.DisDrugTarGenePath;
import com.edu.hzau.cocs.fe.pojo.SwineMetabolismHmdbRes;
import com.edu.hzau.cocs.fe.pojo.SwineMicrobeGeneKeggRes;
import com.edu.hzau.cocs.fe.pojo.datalog.Datalog;
import com.edu.hzau.cocs.fe.pojo.datalog.Relationship;
import com.edu.hzau.cocs.fe.utils.Constants;
import com.edu.hzau.cocs.fe.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yue
 */
@Slf4j
@Service
public class SubQueryService {
    @Autowired
    private SubQueryMapper subQueryMapper;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtils securityUtils;

    private static final Map<String, String> mapper = new HashMap<>();
    static {
        String[] k = {"p_value_dpf_tpf_difference", "microbe_time", "group",
                "p_age_difference", "metabolome_difference", "metabolism_time","ORPHAN_name"};
        String[] v = {"fsmm.microbe.microbe_dpf_tpf_difference", "fsmm.microbe.days",
                "fsmm.microbe.col", "fsmm.microbe.microbe_age_difference", "fsmm.metabolism.metabolome_difference",
                "metabolism_time","biomedentity.raredisinfo.ORPHAN_name"};
        for(int i = 0; i < k.length; i++) {
            mapper.put(k[i], v[i]);
        }
    }

    //biomed start
    public List<DisDrugTarGenePath> curesOf(Datalog datalog) {
        StringBuilder subQuerySql = new StringBuilder(Constants.CURES_OF);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("cures_of")) {
            Relationship cures_of = relationships.get("cures_of");
            Map<String, String> attributeMap = cures_of.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s ='%s'", nk, v));
            });
        }
        System.out.println(subQuerySql);
        return subQueryMapper.getDrugByRareDis(subQuerySql.toString());
    }
    public List<DisDrugTarGenePath> hasFunctionsIn(Datalog datalog, List<DisDrugTarGenePath> DisDrugTarGenePathList) {
        StringBuilder subQuerySql = new StringBuilder(Constants.HAS_FUNCTIONS_IN);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("has_functions_in")) {
            Relationship has_functions_in = relationships.get("has_functions_in");
            Map<String, String> attributeMap = has_functions_in.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s ='%s'", nk, v));
            });
        }
        //System.out.println(subQuerySql);
        return subQueryMapper.getTarByDrug(subQuerySql.toString(), DisDrugTarGenePathList);
    }

    public List<DisDrugTarGenePath> isEncodedByGene(Datalog datalog, List<DisDrugTarGenePath> DisDrugTarGenePathList) {
        StringBuilder subQuerySql = new StringBuilder(Constants.IS_ENCODED_BY_GENE);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("is_encoded_by_gene")) {
            Relationship is_encoded_by_gene = relationships.get("is_encoded_by_gene");
            Map<String, String> attributeMap = is_encoded_by_gene.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s ='%s'", nk, v));
            });
        }
        //System.out.println(subQuerySql);
        return subQueryMapper.getGeneByTar(subQuerySql.toString(), DisDrugTarGenePathList);
    }

    public List<DisDrugTarGenePath> takesPartIn(Datalog datalog, List<DisDrugTarGenePath> DisDrugTarGenePathList) {
        StringBuilder subQuerySql = new StringBuilder(Constants.TAKES_PART_IN);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("takes_part_in")) {
            Relationship takes_part_in = relationships.get("takes_part_in");
            Map<String, String> attributeMap = takes_part_in.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s ='%s'", nk, v));
            });
        }
        //System.out.println(subQuerySql);
        return subQueryMapper.getPathByGene(subQuerySql.toString(), DisDrugTarGenePathList);
    }
    //biomed end

    public List<SwineMicrobeGeneKeggRes> isHostOf(Datalog datalog) {
        StringBuilder subQuerySql = new StringBuilder(Constants.IS_HOST_OF);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("is_host_of")) {
            Relationship is_host_of = relationships.get("is_host_of");
            Map<String, String> attributeMap = is_host_of.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s = '%s'", nk, v));
            });
        }
        /**securityUtils.logInAs("user_00");
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 1));
        for (Task task : taskPage.getContent()) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            log.info("> 任务完成: {}", task);
        }*/
        return subQueryMapper.getMicrobeBySwine(subQuerySql.toString());
    }

    public List<SwineMicrobeGeneKeggRes> changeTheExpressionByMicrobiota(Datalog datalog, List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggResList) {
        StringBuilder subQuerySql = new StringBuilder(Constants.CHANGE_THE_EXPRESSION_BY_MICROBIOTA);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("changes_the_expression_by_microbiota")) {
            Relationship changes_the_expression_by_microbiota = relationships.get("changes_the_expression_by_microbiota");
            Map<String, String> attributeMap = changes_the_expression_by_microbiota.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s = '%s'", nk, v));
            });
        }
        /** securityUtils.logInAs("user_00");
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 1));
        for (Task task : taskPage.getContent()) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            log.info("> 任务完成: {}", task);
        } */
        return subQueryMapper.getGeneByMicrobe(subQuerySql.toString(), swineMicrobeGeneKeggResList);
    }

    public List<SwineMicrobeGeneKeggRes> hasGeneKeggInfo(Datalog datalog, List<SwineMicrobeGeneKeggRes> swineMicrobeGeneKeggResList) throws REXPMismatchException, IOException, RserveException {
        StringBuilder subQuerySql = new StringBuilder(Constants.HAS_GENE_KEGG_INFO);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("has_gene_kegg_info")) {
            Relationship has_gene_kegg_info = relationships.get("has_gene_kegg_info");
            Map<String, String> attributeMap = has_gene_kegg_info.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s = '%s'", nk, v));
            });
        }
       /** securityUtils.logInAs("user_00");
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 1));
        for (Task task : taskPage.getContent()) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            log.info("> 任务完成: {}", task);
        }*/
        return subQueryMapper.getKeggByGene(subQuerySql.toString(), swineMicrobeGeneKeggResList);
//        return subQueryMapper.getKeggByGeneOnline(swineMicrobeGeneKeggResList);
    }

    public List<SwineMetabolismHmdbRes> generates(Datalog datalog) {
        StringBuilder subQuerySql = new StringBuilder(Constants.GENERATES);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("generates")) {
            Relationship generates = relationships.get("generates");
            Map<String, String> attributeMap = generates.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s = '%s'", nk, v));
            });
        }
        /** securityUtils.logInAs("user_00");
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 1));
        for (Task task : taskPage.getContent()) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            log.info("> 任务完成: {}", task);
        }*/
        //System.out.println(subQuerySql);
        return subQueryMapper.getMetabolismBySwine(subQuerySql.toString());
    }

    public List<SwineMetabolismHmdbRes> hasHmdbInfo(Datalog datalog, List<SwineMetabolismHmdbRes> swineMetabolismHmdbResList) {
        StringBuilder subQuerySql = new StringBuilder(Constants.HAS_HMDB_INFO);
        Map<String, Relationship> relationships = datalog.getRelationships();
        if (relationships.containsKey("has_hmdb_info")) {
            Relationship has_hmdb_info = relationships.get("has_hmdb_info");
            Map<String, String> attributeMap = has_hmdb_info.getAttributes();
            attributeMap.forEach((k, v) -> {
                String nk = mapper.get(k);
                subQuerySql.append(String.format(" and %s = '%s'", nk, v));
            });
        }
        /** securityUtils.logInAs("user_00");
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 1));
        for (Task task : taskPage.getContent()) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
            log.info("> 任务完成: {}", task);
        } */
       // System.out.println(subQuerySql);
//        return subQueryMapper.getHmdbByMetabolismOnline(subQuerySql.toString(), swineMetabolismHmdbResList);
        return subQueryMapper.getHmdbByMetabolism(subQuerySql.toString(), swineMetabolismHmdbResList);
    }
}
