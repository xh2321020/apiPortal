/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.base;

import java.io.StringReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * apiPortal
 * Created by Damon on 2016/11/15.
 */
public class ClobMigrate {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        for(int index=0;index<20;index++){
            final String sql="select id,content from (" +
                    "select id,content,rownum as rn from pb2_article where content is not null) " +
                    "where rn BETWEEN "+index*1000+" AND " +(index+1)*1000;
            System.out.println("Query SQL:"+sql);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    Connection srcConnection=
                            DriverManager.getConnection("jdbc:oracle:thin:@10.17.12.141:1521/fqecmdb",
                                    "cnnp","Passw0rd");


                    Connection desConnection=
                            DriverManager.getConnection("jdbc:oracle:thin:@10.17.16.171:1521:fqportal",
                                    "cnnp","password");

                        Map<Long, String[]> clobMap = new HashMap<Long, String[]>();
                        PreparedStatement srcStat = srcConnection
                                .prepareStatement(sql);
                        ResultSet rs = srcStat.executeQuery();
                      //  int i = 0;
                        while (rs.next()) {
                            oracle.sql.CLOB contentClob = (oracle.sql.CLOB) rs
                                    .getClob("content");

                            String content = contentClob == null ? "" : contentClob.getSubString(1, (int) contentClob.length());
                            String pure_content ="";
                            clobMap.put(rs.getLong("id"), new String[]{content, pure_content});
                          //  System.out.println(i++);
                        }
                        rs.close();

                        if(clobMap.isEmpty()){
                            return;
                        }
                        System.out.println("end query .....");
                        System.out.println("start write .....");
                        desConnection.setAutoCommit(true);

                        // 1.这种方法写入CLOB字段可以。
                        PreparedStatement desStat = desConnection
                                .prepareStatement("update pb2_article set content=? where id=?");

                        for (Long key : clobMap.keySet()) {
                            String[] valueArray = clobMap.get(key);

                            StringReader contentReader = new StringReader(valueArray[0]);
                            desStat.setCharacterStream(1, contentReader, valueArray[0].length());

//                            StringReader pureContentReader = new StringReader(valueArray[1]);
//                            desStat.setCharacterStream(2, pureContentReader, valueArray[1].length());

                            desStat.setLong(2, key);
                            desStat.addBatch();
                        }
                        desStat.executeBatch();

                        desStat.close();
                        desConnection.commit();
                        srcConnection.close();
                        desConnection.close();
                        System.out.println("end write .....");
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally {

                    }
                }
            }).start();

        }







    }


}
