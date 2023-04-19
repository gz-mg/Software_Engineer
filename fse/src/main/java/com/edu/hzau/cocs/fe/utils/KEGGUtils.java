package com.edu.hzau.cocs.fe.utils;

import com.edu.hzau.cocs.fe.pojo.Gene;
import com.edu.hzau.cocs.fe.pojo.KEGGEntity;
import com.edu.hzau.cocs.fe.pojo.datasource.KEGGPathwayMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description 爬取KEGG数据
 * @Author yue
 */
@Slf4j
@Component
public class KEGGUtils {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String KEGG_URL = "https://rest.kegg.jp/conv/genes/ncbi-geneid:";
    private static final String KEGG_PATHWAY_URL = "https://www.kegg.jp/kegg-bin/search_pathway_text?keyword=";
    /**
     * 根据ncbi_gene_id获取keggId
     */
    public List<String> getKeggIds(List<String> ncbiGeneIdsList) throws IOException {
        List<String> keggIds = new ArrayList<>();
        for (String ncbiGeneId : ncbiGeneIdsList) {
            // 配置socket，防止阻塞
            SocketConfig socketConfig = SocketConfig.custom()
                    .setSoKeepAlive(false)
                    .setSoReuseAddress(true)
                    .setSoTimeout(10000)
                    .setTcpNoDelay(true).build();
            CloseableHttpResponse response;
            try {
                // 发起请求
                CloseableHttpClient client = HttpClientBuilder.create()
                        .setDefaultSocketConfig(socketConfig).build();
                HttpGet httpGet = new HttpGet(KEGG_URL + ncbiGeneId);
                response = client.execute(httpGet);
            } catch (IOException e ) {
                log.warn("request time out..., failed ncbiGeneId: {}", ncbiGeneId);
                continue;
            }
            // 获取结果
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(content));
            String line = br.readLine();
            if (line.length() <= 0) {
                continue;
            }
            int prefixLen = ncbiGeneId.length() + 12;
            String keggId = line.substring(prefixLen).trim();
            log.info("{} queried", keggId);
            keggIds.add(keggId);
        }
        return keggIds;
    }

    /**
     * 根据keggId获取实体
     */
    public List<KEGGEntity> getKeggEntity(List<String> keggIds) throws RserveException, REXPMismatchException {
        RConnection rc = new RConnection();
        REXP eval;
        List<KEGGEntity> entities = new ArrayList<>();
        // 加载kegg库
        rc.eval("library(KEGGREST)");
        // 解析entry
        for (String keggId : keggIds) {
            KEGGEntity entry = new KEGGEntity();
            String ncbiGeneId = keggId.replaceAll(".*[^\\d](?=(\\d+))","");
            entry.setNcbiGeneId(ncbiGeneId);

            // 根据keggId查询
            rc.eval("query <- keggGet(c('" + keggId + "'))");
            entry.setKeggId(keggId);

            // 查询symbol
            eval = rc.eval("query[[1]]$SYMBOL");
            String keggSymbol = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggSymbol(keggSymbol);

            // 查询name
            eval = rc.eval("query[[1]]$NAME");
            String keggName = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggName(keggName);

            // 查询orthology
            eval = rc.eval("query[[1]]$ORTHOLOGY");
            String keggOrthology = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggOrthology(keggOrthology);

            // 查询organism
            eval = rc.eval("query[[1]]$ORGANISM");
            String keggOrganism = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggOrganism(keggOrganism);

            // 查询pathway
            eval = rc.eval("query[[1]]$PATHWAY");
            String keggPathway = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggPathway(keggPathway);

            // 查询network
            eval = rc.eval("as.character(query[[1]]$NETWORK[1])");
            String keggNetwork = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggNetWork(keggNetwork);

            // 查询networkelement
            eval = rc.eval("as.character(query[[1]]$NETWORK$ELEMENT)");
            String keggNetworkElement = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggNetWorkElement(keggNetworkElement);

            // 查询brite
            eval = rc.eval("query[[1]]$BRITE");
            String keggBrite;
            if (eval.isNull()) {
                keggBrite = null;
            } else {
                String[] strings = eval.asStrings();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < strings.length - 1; i++) {
                    if (strings[i + 1].trim().startsWith(ncbiGeneId)) {
                        sb.append(strings[i].trim());
                        sb.append("; ");
                    }
                }
                keggBrite = sb.toString();
            }
            entry.setKeggBrite(keggBrite);

            // 查询motif
            eval = rc.eval("as.character(query[[1]]$MOTIF)");
            String keggMotif = eval.isNull() ? null : Arrays.toString(eval.asStrings());
            entry.setKeggMotif(keggMotif);
            entities.add(entry);
            log.info(ncbiGeneId + " queried");
        }
        rc.close();
        return entities;
    }

    /**
     * 获取带有图片的gene pathway
     */
    public List<KEGGPathwayMap> getKeggPathwayMap(List<Gene> geneList) throws RserveException, IOException, REXPMismatchException {
        List<KEGGPathwayMap> res = new ArrayList<>();
        for (Gene gene : geneList) {
            Document document = null;
            try {
                 document = Jsoup.parse(new URL(KEGG_PATHWAY_URL + gene.getGeneSymbol()), 20000);
            } catch (IOException e) {
                log.warn("{} is invalid", gene.getGeneSymbol());
                continue;
            }
            Elements trs = document.getElementsByTag("tr");
            // 获取pathway + map
            KEGGPathwayMap keggPathwayMap = new KEGGPathwayMap();
            keggPathwayMap.setGeneSymbol(gene.getGeneSymbol());
            List<String> pathwayMap = new ArrayList<>();
            for (int i = 2; i < trs.size(); i++) {
                Elements td = trs.get(i).getElementsByTag("td");
                String pathway = td.get(1).html();
                String map = td.get(0).getElementsByTag("a").html();
                String mapUrl = String.format("https://www.kegg.jp/pathway/map=%s&keyword=%s", map, gene.getGeneSymbol());
                String sb = pathway + "(" + mapUrl + ")";
                pathwayMap.add(sb);
            }
            keggPathwayMap.setPathwayMap(pathwayMap);

            // 获取brite
            List<String> keggIds = getKeggIds(Collections.singletonList(gene.getNcbiGeneId()));
            if (keggIds != null) {
                String keggId = keggIds.get(0);
                RConnection rc = new RConnection();
                REXP eval;
                List<KEGGEntity> entities = new ArrayList<>();
                // 加载kegg库
                rc.eval("library(KEGGREST)");
                rc.eval("query <- keggGet(c('" + keggId + "'))");
                // 查询brite
                eval = rc.eval("query[[1]]$BRITE");
                String keggBrite;
                if (eval.isNull()) {
                    keggBrite = null;
                } else {
                    String[] strings = eval.asStrings();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < strings.length - 1; i++) {
                        if (strings[i + 1].trim().startsWith(gene.getNcbiGeneId())) {
                            sb.append(strings[i].trim());
                            sb.append("; ");
                        }
                    }
                    keggBrite = sb.toString();
                }
                keggPathwayMap.setBrite(keggBrite);
            }
            // 存储
            res.add(keggPathwayMap);
        }
        return res;
    }


}
