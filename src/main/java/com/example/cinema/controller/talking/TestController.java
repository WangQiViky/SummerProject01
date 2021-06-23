package com.example.cinema.controller.talking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("test/getAllImageName")
    public List<Map<String, Object>> getDbType(){
        String sql = "select * from image";
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            Set<Map.Entry<String, Object>> entries = map.entrySet( );
            if(entries != null) {
                Iterator<Map.Entry<String, Object>> iterator = entries.iterator( );
                while(iterator.hasNext( )) {
                    Map.Entry<String, Object> entry =(Map.Entry<String, Object>) iterator.next( );
                    Object key = entry.getKey( );
                    Object value = entry.getValue();
                    System.out.println(key+":"+value);
                }
            }
        }
        /**list的每一项是一个map，对应了列名和值*/
        return list;
    }

    @RequestMapping("test/getImageNameByPageName")
    public List<Map<String, Object>> getDbTypeByPageName(String page_name){
        System.out.println(page_name);
        String sql = "select * from image where page_name=";
        sql=sql+page_name;
        List<Map<String, Object>> list =  jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : list) {
            Set<Map.Entry<String, Object>> entries = map.entrySet( );
            if(entries != null) {
                Iterator<Map.Entry<String, Object>> iterator = entries.iterator( );
                while(iterator.hasNext( )) {
                    Map.Entry<String, Object> entry =(Map.Entry<String, Object>) iterator.next( );
                    Object key = entry.getKey( );
                    Object value = entry.getValue();
                    System.out.println(key+":"+value);
                }
            }
        }
        /**list的每一项是一个map，对应了列名和值*/
        return list;
    }
    @RequestMapping("test/storageImage")
    public void storageDbType(String page_name,String hash_name){
        System.out.println("page_name:"+page_name+",hash_name:"+hash_name);
        List<Map<String, Object>> list=this.getDbTypeByPageName(page_name);
        if(list.size()!=0){
            this.deleteDbType(page_name);
        }
        String sql = "INSERT INTO image VALUES (?, ?);";
        jdbcTemplate.update(sql, hash_name.substring(1,hash_name.length()-1), page_name.substring(1,page_name.length()-1));

    }

    @RequestMapping("test/deleteImage")
    public void deleteDbType(String page_name){
        System.out.println("进入删除行的地方");
        String sql1 = "DELETE FROM image WHERE page_name=?;";
        int i = jdbcTemplate.update(sql1, page_name.substring(1,page_name.length()-1));
        System.out.println("影响的行数: " + i);
    }


}