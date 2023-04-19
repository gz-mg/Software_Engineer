package com.edu.hzau.cocs.fe.utils;

import com.edu.hzau.cocs.fe.pojo.HMDBEntity;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 爬取HMDB数据
 * @Author yue
 */
@Component
@Slf4j
public class HMDBUtils {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String HMDB_URL = "https://hmdb.ca/metabolites/";

    /**
     * 爬取pathway和kegg_url，封装为hmdbEntity
     */
    public List<HMDBEntity> getHmdbEntity(List<String> hmdbIds) {
        List<HMDBEntity> HMDBEntityList = new ArrayList<>();
        for (String hmdbId : hmdbIds) {
            Document document = null;
            try {
                document = Jsoup.parse(new URL(HMDB_URL + hmdbId), 50000);
            } catch (IOException e) {
                log.warn("{} is invalid", hmdbId);
                continue;
            }
            Element div = document.getElementById("metabolite-pathway-links");
            if (div == null) continue;
            Elements trs = div.getElementsByTag("tr");
            for (int i = 1; i < trs.size(); i++) {
                Elements tds = trs.get(i).getElementsByTag("td");
                try {
                    String pathway = tds.get(0).html();
                    Element td = tds.get(2);
                    if (td.getElementsByTag("span").isEmpty()) {
                        String imgSrc = td.getElementsByTag("img").attr("src");
                        HMDBEntity hmdbEntity = new HMDBEntity(hmdbId, pathway, imgSrc);
                        log.info(String.valueOf(hmdbEntity));
                        HMDBEntityList.add(hmdbEntity);
                    }
                }catch (IndexOutOfBoundsException e) {
                    log.warn("{} pathway is null", hmdbId);
                }
            }
        }
        return HMDBEntityList;
    }

    /**
     * 从csv文件中读取hmdbId
     */
    public List<String> getHmdbIdsFromCsv() {
        List<String> hmdbIds = new ArrayList<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("data/metabolism_all.csv"));
            CSVReader csvReader = new CSVReader(reader);
            String[] columns = null;
            // 去除表头
            csvReader.readNext();
            while ((columns = csvReader.readNext()) != null) {
                String hmdbId = columns[2];
                if (!hmdbId.equals("NA")) {
                    hmdbIds.add(hmdbId);
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.warn(e.getMessage());
        }
        return hmdbIds;
    }
}
