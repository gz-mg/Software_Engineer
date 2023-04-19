package com.edu.hzau.cocs.fe.utils;

/**
 * @Author yue
 */
public interface Constants {
    // dlp 文件路径
    String DATALOG_RULE_PATH = "./datalogrules/";
    String DATALOG_RULE_PATH_BIO = DATALOG_RULE_PATH + "gut_microbiota_ontology_v1.2_q4.dlp";
    String DATALOG_REULE_PATH_SWINE = DATALOG_RULE_PATH + "swine_microbiota_ontology.dlp";

    // 数据源
    String[] SWINE_SOURCE =  {"fsmm", "gene_info", "relationship_entity"};
    String[] BIO_SOURCE = {"gutmgene","kegg","relationship","gutmdisorder"};
    String[] BIOMED_SOURCE = {"biomedentity","biomedrelation"};
    String MYSQL_SOURCE_TYPE = "MySQL";

    // 关系/BPMN服务
    String IS_HOST_OF = "select distinct swine.swine_index, microbe.microbe_id, microbe.microbe_Name " +
            "from relationship_entity.is_host_of, fsmm.swine, fsmm.microbe " +
            "where is_host_of.swine_index = swine.swine_index " +
            "and is_host_of.microbe_id = microbe.microbe_id ";

    String CHANGE_THE_EXPRESSION_BY_MICROBIOTA = "select distinct microbe.microbe_id, microbe.microbe_Name, gene.NCBI_gene_id, gene.Gene_symbol " +
            "from relationship_entity.change_the_expression_by_microbiota, fsmm.microbe, gene_info.gene " +
            "where change_the_expression_by_microbiota.microbe_id = microbe.microbe_id " +
            "and change_the_expression_by_microbiota.NCBI_gene_id = gene.NCBI_gene_id ";

    String HAS_GENE_KEGG_INFO = "select distinct gene.NCBI_gene_id, gene.Gene_symbol, gene_kegg_info.Gene_kegg_index, gene_kegg_info.Gene_kegg_pathway " +
            "from relationship_entity.has_gene_kegg_info, gene_info.gene, gene_info.gene_kegg_info " +
            "where has_gene_kegg_info.NCBI_gene_id = gene.NCBI_gene_id " +
            "and has_gene_kegg_info.Gene_kegg_index = gene_kegg_info.Gene_kegg_index ";

    String HAS_HMDB_INFO = "select distinct metabolism.metabolism_index, metabolism.metabolism_name, metabolism_hmdb_info.hmdb_info_index, metabolism_hmdb_info.metabolism_hmdb_info_index, metabolism_hmdb_info.hmdb_pathway ,metabolism_hmdb_info.kegg_url " +
            "from relationship_entity.has_hmdb_info, gene_info.metabolism_hmdb_info, fsmm.metabolism " +
            "where has_hmdb_info.metabolism_index = metabolism.metabolism_index " +
            "and has_hmdb_info.hmdb_info_index = metabolism_hmdb_info.hmdb_info_index ";

    String GENERATES = "select distinct swine.swine_index, metabolism.metabolism_index, metabolism.metabolism_name " +
            "from relationship_entity.generates, fsmm.swine, fsmm.metabolism " +
            "where generates.swine_index = swine.swine_index " +
            "and generates.metabolism_index = metabolism.metabolism_index ";

    //biomed sql
    String CURES_OF ="select distinct raredisinfo.ORPHAN_id, raredisinfo.ORPHAN_name, druginfo.drug_id, druginfo.drug_name "+
            "from biomedrelation.rd_drug, biomedentity.raredisinfo, biomedentity.druginfo " +
            "where rd_drug.ORPHAN_id = raredisinfo.ORPHAN_id " +
            "and rd_drug.drug_id = druginfo.drug_id ";

    String HAS_FUNCTIONS_IN ="select distinct druginfo.drug_id, druginfo.drug_name, targetinfo.target_id, targetinfo.target_name "+
            "from biomedrelation.drug_tar, biomedentity.druginfo, biomedentity.targetinfo " +
            "where drug_tar.target_id = targetinfo.target_id " +
            "and drug_tar.drug_id = druginfo.drug_id ";

    String IS_ENCODED_BY_GENE ="select distinct targetinfo.target_id, targetinfo.target_name, geneinfo.gene_id, geneinfo.gene_symbol "+
            "from biomedrelation.tar_gene, biomedentity.targetinfo, biomedentity.geneinfo " +
            "where tar_gene.target_id = targetinfo.target_id " +
            "and tar_gene.gene_id = geneinfo.gene_id ";

    String TAKES_PART_IN ="select distinct  geneinfo.gene_id, geneinfo.gene_symbol, pathinfo.path_id, pathinfo.path_name "+
            "from biomedrelation.gene_path, biomedentity.geneinfo, biomedentity.pathinfo " +
            "where gene_path.gene_id = geneinfo.gene_id " +
            "and gene_path.path_id = pathinfo.path_id ";





}
