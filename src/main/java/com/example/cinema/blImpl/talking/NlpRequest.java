package com.example.cinema.blImpl.talking;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NlpRequest {

    public String getWordShow(String info){
//        String ss=HttpRequest.sendGet("http://82.156.210.61:5000/", "sen=\""+info+"\"");
        String ss=HttpRequest.sendGet("http://47.100.83.177:5000/", "sen=\""+info+"\"");
        System.out.println(ss);
        return ss;
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        NlpRequest nlpRequest=new NlpRequest();
        String info="女孩说，我要跟你一起去。狼说，不行。";
        String str = new String(info.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        String str1 = URLEncoder.encode(info, "utf-8");
        nlpRequest.getWordShow(str1);
    }
}
