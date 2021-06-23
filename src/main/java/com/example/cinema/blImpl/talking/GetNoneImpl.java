package com.example.cinema.blImpl.talking;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cinema.bl.talking.GetNone;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNoneImpl implements GetNone {
    // webapi接口地址
    private static final String WEBTTS_URL = "http://ltpapi.xfyun.cn/v1/cws";
    private static final String WEBTTS_URL1 = "http://ltpapi.xfyun.cn/v1/pos";
    // 应用ID
    private static final String APPID = "604595a5";
    // 接口密钥
    private static final String API_KEY = "2c5925728d9f41b296add6cfd147fbdf";


    private static final String TYPE = "dependent";

    public static void main(String[] args) throws IOException {
        String TEXT = "女孩来到森林里，遇见了一只狼，他们一起聊了一会天，女孩就回家了";
        GetNoneImpl getNoneImpl =new GetNoneImpl();
        List<String> re= getNoneImpl.getNo(TEXT);
        System.out.println(re);
    }

    public List<String> getNo(String TEXT) throws UnsupportedEncodingException {
        GetNoneImpl getNoneImpl =new GetNoneImpl();
        String[] word= getNoneImpl.getWord(TEXT);
        String[] word2= getNoneImpl.getPos(TEXT);
        ArrayList<String> re=new ArrayList<>();
        for(int i=0;i<word.length;i++){
            if(word2[i].equals("n")){
                re.add(word[i]);
            }
        }
        return re;
    }

    public String[] getPos(String info) throws UnsupportedEncodingException {
        String word=info.replaceAll( "[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "");
        System.out.println(word);

        System.out.println(word.length());
        Map<String, String> header = buildHttpHeader();
        String result = HttpUtil.doPost1(WEBTTS_URL1, header, "text=" + URLEncoder.encode(word, "utf-8"));
        System.out.println("itp 接口调用结果：" + result);


        JSONObject object = JSONObject.parseObject(result);
        //string
        String data = object.getString("data");
//        System.out.println(data);
        JSONObject object1 = JSONObject.parseObject(data);
        JSONArray wordStr=object1.getJSONArray("pos");
        String[] re=new String[wordStr.size()];
        for(int i=0;i<wordStr.size();i++){
//            System.out.println(wordStr.getString(i));
            re[i]=wordStr.getString(i);
        }
        return re;
    }
    public String[] getWord(String info) throws UnsupportedEncodingException {
        String word=info.replaceAll( "[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "");
        System.out.println(word);

        System.out.println(word.length());
        Map<String, String> header = buildHttpHeader();
        String result = HttpUtil.doPost1(WEBTTS_URL, header, "text=" + URLEncoder.encode(word, "utf-8"));
        System.out.println("itp 接口调用结果：" + result);


        JSONObject object = JSONObject.parseObject(result);
        //string
        String data = object.getString("data");
//        System.out.println(data);
        JSONObject object1 = JSONObject.parseObject(data);
        JSONArray wordStr=object1.getJSONArray("word");
        String[] re=new String[wordStr.size()];
        for(int i=0;i<wordStr.size();i++){
//            System.out.println(wordStr.getString(i));
            re[i]=wordStr.getString(i);
        }
        return re;
    }

    /**
     * 组装http请求头
     */
    private static Map<String, String> buildHttpHeader() throws UnsupportedEncodingException {
        String curTime = System.currentTimeMillis() / 1000L + "";
        String param = "{\"type\":\"" + TYPE +"\"}";
        String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
        String checkSum = DigestUtils.md5Hex(API_KEY + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", APPID);
        return header;
    }
}
