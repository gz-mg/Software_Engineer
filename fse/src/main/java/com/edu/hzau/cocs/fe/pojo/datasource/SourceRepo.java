package com.edu.hzau.cocs.fe.pojo.datasource;

import com.edu.hzau.cocs.fe.utils.Constants;
import fr.lirmm.graphik.graal.store.rdbms.driver.AbstractRdbmsDriver;
import fr.lirmm.graphik.graal.store.rdbms.driver.MysqlDriver;
import lombok.Data;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author yue
 */
@Data
public class SourceRepo {
    private String repoName;
    private Set<Source> sourcePool;
    private static SourceRepo sourceRepo;

    public SourceRepo (String repoName, Set<Source> sourcePool) {
        this.repoName = repoName;
        this.sourcePool = sourcePool;
    }

    public static SourceRepo getSwineSourceRepo() {
        if (sourceRepo == null || !sourceRepo.repoName.equals("swineRepo")) {
            sourceRepo = new SourceRepo("swineRepo", getSource(Constants.SWINE_SOURCE, Constants.DATALOG_REULE_PATH_SWINE));
        }
        return sourceRepo;
    }

    public static SourceRepo getBioSourceRepo() {
        if (sourceRepo == null || !sourceRepo.repoName.equals("bioRepo")) {
            sourceRepo = new SourceRepo("bioRepo", getSource(Constants.BIO_SOURCE, Constants.DATALOG_RULE_PATH_BIO));
        }
        return sourceRepo;
    }

    public static Set<Source> getSource(String[] sources, String rulePath){
        Set<Source> sourcePool = new HashSet<>();
        for (String source : sources) {
            try {
                AbstractRdbmsDriver dirver = new MysqlDriver("jdbc:mysql://localhost:3306/" + source + "?user=root&password=Gxz(001205)&useUnicode=true&characterEncoding=utf8");
                RDBMSSource rdbmsSource = new RDBMSSource(source, Constants.MYSQL_SOURCE_TYPE, rulePath, dirver);
                sourcePool.add(rdbmsSource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sourcePool;
    }
}
