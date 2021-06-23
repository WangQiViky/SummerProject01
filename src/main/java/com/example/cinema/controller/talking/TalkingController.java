package com.example.cinema.controller.talking;

import com.alibaba.fastjson.JSONObject;
import com.example.cinema.bl.talking.GenerateImages;
import com.example.cinema.bl.talking.GetNone;
import com.example.cinema.bl.talking.TalkingService;
import com.example.cinema.blImpl.talking.*;
import com.example.cinema.vo.ResponseVO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController()
public class TalkingController {

    @Autowired
    private TalkingService talkingService;
    GenerateImages generateImages=new GenerateImageImlp();
    ArrayConvert arrayConvert=new ArrayConvert();
    NlpRequest nlpRequest=new NlpRequest();
    XunFeiTtsTool xunFeiTtsTool =new XunFeiTtsTool("604595a5","./image");
    GetNone getNone=new GetNoneImpl();

    String base = "./video/";

    @PostMapping("talking/upload")
    public Object uploadFile(@RequestParam(value="multipartFile",required = false) MultipartFile multipartFile,
                             @RequestParam(value="background",required = false) String background,
                             @RequestParam(value="pageName",required = false) String pageName,
                                     HttpServletRequest request) throws IOException, InterruptedException {
        if (multipartFile==null) {
            System.out.println("multipartFile是null");

        }
        System.out.println(multipartFile.getContentType());
        System.out.println(pageName);
        System.out.println(background);
        System.out.println("获得了文件");
        System.out.println(multipartFile.getOriginalFilename());
        System.out.println(multipartFile.getSize());
//        multipartFile.transferTo(new File(base+"music.wav"));

        /**生成音频文件并存储在本地*/
        File file=new File(base+"music.wav");
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);

        /**将音频文件转码成文字，调用zyc的方法，把返回的结果放在result中*/
        String result1=talkingService.wavToString();
        String re=arrayConvert.getAllInfo(result1);
        System.out.println("传给zyc的字符串是："+re);
        String result=nlpRequest.getWordShow(URLEncoder.encode(re, "utf-8"));
        System.out.println(result);
        /**此时，result中就是zyc传过来的东西*/

        JSONObject object = JSONObject
                .parseObject(result);
        //string
        String talking = object.getString("talking");
        System.out.println("此时是不是对话？"+talking);
        String hashName="";
        if(talking.equals("0")){
            int num = Integer.parseInt(object.getString("num"));
            if(num%2==0){
                /**talking=0且是第一次的时候要加入图片string*/
                String list=object.getString("noun");
                System.out.println("生成图片的数组："+list);
//                String[] li=list.split("\"");
//                List<String> l=new ArrayList<>();
//                for(int i=0;i<li.length;i++){
//                    String temp=li[i];
//                    if(temp.equals("[")|temp.equals("]")|temp.equals(",")){
//
//                    }else{
//                        l.add(li[i]);
//                    }
//                }

                List<String> l=getNone.getNo(re);
                System.out.println("调用提取名词："+l);

                ConvertImageContent convertImageContent=new ConvertImageContent(background);
                hashName=convertImageContent.convertImage(l);
                /**转化为图片的字符串哈希,把convertImage方法的注释去掉就行*/
                /**添加到里面去形成新的json*/
                object.put("hashName",hashName);
            }

        }
        else{
            //去掉重复的人
            String list=object.getString("people");
            System.out.println("生成图片的数组："+list);
            String[] li=list.split("\"");
            List<String> l=new ArrayList<>();
            for(int i=0;i<li.length;i++){
                String temp=li[i];
                if(temp.equals("[")|temp.equals("]")|temp.equals(",")){

                }else{
                    boolean isExist=false;
                    for(int j=0;j<l.size();j++){
                        if(l.get(j).equals(li[i])){
                            isExist=true;
                            break;
                        }
                    }
                    if(!isExist){
                        l.add(li[i]);
                    }

                }
            }

//            List<String> li=getNone.getNo(re);
//            List<String> l=new ArrayList<>();
//            for(int i=0;i<li.size();i++){
//                String temp=li.get(i);
//                if(!isExi(l,temp)){
//                    l.add(temp);
//                }
//            }
//            System.out.println("调用提取名词："+l);

            /**这个时候l是*/
            ConvertImageContent convertImageContent=new ConvertImageContent(background);
            hashName=convertImageContent.convertImage(l);
            /**转化为图片的字符串哈希,把convertImage方法的注释去掉就行*/
            /**添加到里面去形成新的json*/
            object.put("hashName",hashName);
        }

        /**加入语音生成的string*/

        String wav= xunFeiTtsTool.convertWord(object.getString("question"));

        object.put("wavName",wav);
        object.put("originWord",re);

        System.out.println("返回给前端的json为"+object);
        return object;
    }

    private boolean isExi(List<String> a,String s){
        boolean re=false;
        for(int i=0;i<a.size();i++){
            if((a.get(i)).equals(s)){
                re=true;
                break;
            }
        }
        return re;
    }

    @GetMapping("talking/uploadWav")
    public String getWav(@RequestParam String word) throws IOException {
        XunFeiTtsTool xunFeiTtsTool =new XunFeiTtsTool("604595a5","./image");
        String re= xunFeiTtsTool.convertWord(word);
//        return re;
        return "success";
    }

    @PostMapping("talking/uploadURL")
    public String uploadURL(@RequestParam String url) throws IOException {

        System.out.println(url);
        return "success";
    }

    @GetMapping("talking/uploadImage")
    public ResponseVO getHot(){
//        ResponseVO re=generateImages.getImageRe();
//        File file= (File) re.getContent();
//        System.out.println(file.length());
//        return re;
        return null;
    }

    @GetMapping("talking/toWav")
    public String getStrToWav(String wavStr) throws IOException {
        XunFeiTtsTool xunFeiTtsTool =new XunFeiTtsTool("604595a5","./image");
        String wav= xunFeiTtsTool.convertWord(wavStr);
        return wav;
    }
    }
