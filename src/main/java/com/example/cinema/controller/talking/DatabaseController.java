package com.example.cinema.controller.talking;

import com.example.cinema.blImpl.talking.DataBaseOpe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class DatabaseController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("data/getAllImageName")
    public List<Map<String, String>> getDbType(){
        DataBaseOpe dataBaseOpe=new DataBaseOpe();
        List<Map<String, String>> list =  dataBaseOpe.getAll();
        /**list的每一项是一个map，对应了列名和值*/
        return list;
    }

//    @RequestMapping("data/getImageNameByPageName")
//    public List<Map<String, Object>> getDbTypeByPageName(String page_name){
//
//    }
    @RequestMapping("data/storageImage")
    public boolean storageDbType(String page_name,String hash_name,String wav){
       DataBaseOpe dataBaseOpe=new DataBaseOpe();
       dataBaseOpe.delete(page_name);
       return dataBaseOpe.add(page_name,hash_name,wav);
    }

    @RequestMapping("data/deleteImage")
    public boolean deleteDbType(String page_name){
        DataBaseOpe dataBaseOpe=new DataBaseOpe();
        return dataBaseOpe.delete(page_name);
    }
    @RequestMapping("data/deleteImageAll")
    public boolean deleteDbTypeAll(){
        DataBaseOpe dataBaseOpe=new DataBaseOpe();
        return dataBaseOpe.deleteAll();
    }

}
