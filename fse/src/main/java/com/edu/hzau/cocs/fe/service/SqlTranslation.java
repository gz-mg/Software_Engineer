package com.edu.hzau.cocs.fe.service;

import java.sql.*;
import java.util.*;

public class SqlTranslation {
    public static String translate(String datalogRewriting) throws SQLException, ClassNotFoundException {

        StringBuilder sql = new StringBuilder("select distinct");
        HashMap<String, List<String>> whereEqualMap = new HashMap<>();//用来存储where条件中相等的两个量
        Map<String, List<String>> whereConstantMap = new HashMap<>();//存储where中带<>的已知量，如<SalmonellaEnterica>，<human>
        ArrayList<String> tableNameList = new ArrayList<>();//存储数据库表名
        String[] headerAndBody = datalogRewriting.split(" :-");

        String header = headerAndBody[0].substring(2, headerAndBody[0].length() - 1);
        String[] fields = header.split(",");

        String body = headerAndBody[1];//去除？那段后剩下的部分
        String[] bodyLines = body.split("\\),");//把每一行分割出来

        for (String line : bodyLines) {
            String tableName = line.split("\\(")[0];
            tableNameList.add(tableName);
            String column = line.split("\\(")[1];
            String[] columnFields = column.split(", ");
            LinkedHashMap<Integer, String> tableColumns = getTableColumns(tableName);  // 在数据库中查找表列名
            for (int i = 0; i < columnFields.length; i++) {
                if (!columnFields[i].contains("VAR") && !columnFields[i].contains("<")) {
                    if (whereEqualMap.get(columnFields[i]) != null) {
                        List<String> arrayList = whereEqualMap.get(columnFields[i]);
                        arrayList.add(tableName + "." + tableColumns.get(i + 1));
                    }
                    if (whereEqualMap.get(columnFields[i]) == null) {
                        List<String> arrayList = new ArrayList<>();
                        arrayList.add(tableName + "." + tableColumns.get(i + 1));
                        whereEqualMap.put(columnFields[i], arrayList);
                    }
                }
                if (columnFields[i].contains("<")) {
                    if (whereConstantMap.get(columnFields[i]) == null) {
                        List<String> arrayList = new ArrayList<>();
                        arrayList.add(tableName + "." + tableColumns.get(i + 1));
                        if (columnFields[i].contains(".")) {
                            whereConstantMap.put(columnFields[i].substring(1, columnFields[i].length() - 4), arrayList);
                        } else {
                            whereConstantMap.put(columnFields[i].substring(1, columnFields[i].length() - 1), arrayList);
                        }
                    }
                    if (whereEqualMap.get(columnFields[i].substring(1, columnFields[i].length() - 1)) != null) {
                        List<String> arrayList = new ArrayList<>();
                        arrayList.add(tableName + "." + tableColumns.get(i + 1));
                    }
                }
            }
        }
//      -----------开始select的拼接-------
        for (int i = 0; i < fields.length - 1; i++) {
            List<String> que = whereEqualMap.get(fields[i]);
            sql.append(que.get(0)).append(" as ").append(fields[i]).append(",");
        }
        List<String> que = whereEqualMap.get(fields[fields.length - 1]);
        sql.append(que.get(0)).append(" as ").append(fields[fields.length - 1]).append(" from ");
//      ------------完成select的拼接-------


//      -----------开始from的拼接-------
        List<String> newTable = remove(tableNameList);
        Object[] newTableStrs = new HashSet<>(newTable).toArray();
        for (int i = 0; i < newTableStrs.length - 1; i++) {
            sql.append(newTableStrs[i]).append(",");
        }
        sql.append(newTableStrs[newTableStrs.length - 1]).append(" where ");
//      -----------完成from的拼接-------


//      -----------开始where的拼接-------
        for (Map.Entry<String, List<String>> entry : whereConstantMap.entrySet()) {
            List<String> arrayList = entry.getValue();
            String value = arrayList.get(0);
            sql.append(value).append(" = ").append("'").append(entry.getKey()).append("'").append(" and ");
        }

        for (List<String> value : whereEqualMap.values()) {
            for (int i = 0; i < value.size() - 1; i++) {
                sql.append(value.get(i)).append(" = ").append(value.get(i + 1)).append(" and ");
            }
        }
//      -----------完成where的拼接-------

        return sql.substring(0, sql.length() - 5);
    }

    public static LinkedHashMap<Integer, String> getTableColumns(String TableName) throws SQLException {
        //2、用户信息和url
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf8", "root", "Gxz(001205)");

        //4、执行sql的对象
        Statement statement = conn.createStatement();
        //5、执行sql的对象去执行sql，可能存在结果，查看返回结果
        String sql = "select * from " + TableName;
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData data = resultSet.getMetaData();
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        for (int i = 1; i <= data.getColumnCount(); i++) {
            map.put(i, data.getColumnName(i));
        }

        //6、释放连接
        resultSet.close();
        statement.close();
        conn.close();
        return map;
    }

    public static ArrayList<String> remove(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i + 1; j < arrayList.size(); j++) {
                if (arrayList.get(i).equals(arrayList.get(j))) {
                    arrayList.remove(j);
                }
            }
        }
        return arrayList;
    }
}
