package com.example.cinema.blImpl.talking;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ArrayConvert {
    public static void main(String[] args){
        String info="[{\"bg\":\"420\",\"ed\":\"2830\",\"onebest\":\"小红走到了外婆的家里。\",\"speaker\":\"0\"},{\"bg\":\"420\",\"ed\":\"2830\",\"onebest\":\"小红走到了外婆的家里。\",\"speaker\":\"0\"}]";
        ArrayConvert arrayConvert=new ArrayConvert();
        System.out.println(arrayConvert.getAllInfo(info));
    }
    public String getAllInfo(String info){
        String re="";
        int head=0;
        int tail=0;
        for(int i=1;i<info.length();i++){
            String temp=info.substring(i,i+1);
            if(temp.equals("{")){
                head=i;
            }else if(temp.equals("}")){
                tail=i;
                if(head<tail){
//                    System.out.println("开始调用方法，此时head="+head+",tail="+tail+"截取出的字符串是："+info.substring(head,tail));
                    re=re+getInfo(info.substring(head,tail+1));
                }
            }
        }
        return re;
    }
    /**字符串,找出一个json里的onebest*/
    public String getInfo(String info){
        JSONObject object = JSONObject
                .parseObject(info);
        //string
        String s = object.getString("onebest");
        System.out.println(s);
        return s;
    }

    public void testJson() {
        JSONObject object = new JSONObject();
        //string
        object.put("string","string");
        //int
        object.put("int",2);
        //boolean
        object.put("boolean",true);
        //array
        List<Integer> integers = Arrays.asList(1,2,3);
        object.put("list",integers);
        //null
        object.put("null",null);

        System.out.println(object);
    }

    public void testJson2() {

        JSONObject object = JSONObject
                .parseObject("{\"boolean\":true,\"string\":\"string\",\"list\":[1,2,3],\"int\":2}");
        //string
        String s = object.getString("string");
        System.out.println(s);
        //int
        int i = object.getIntValue("int");
        System.out.println(i);
        //boolean
        boolean b = object.getBooleanValue("boolean");
        System.out.println(b);
        //list
        List<Integer> integers = JSON.parseArray(object.getJSONArray("list").toJSONString(),Integer.class);
        integers.forEach(System.out::println);
        //null
        System.out.println(object.getString("null"));
    }

}
