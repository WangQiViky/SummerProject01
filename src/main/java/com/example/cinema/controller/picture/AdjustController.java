package com.example.cinema.controller.picture;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cinema.bl.picture.AdjustService;
import com.example.cinema.bl.talking.GenerateImages;
import com.example.cinema.bl.talking.TalkingService;
import com.example.cinema.blImpl.talking.GenerateImageImlp;
import com.example.cinema.vo.ResponseVO;
import com.iflytek.msp.lfasr.LfasrClient;
import com.iflytek.msp.lfasr.model.Message;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
public class AdjustController {

    @Autowired
    AdjustService adjustService;

    @Autowired
    private TalkingService talkingService;
    GenerateImages generateImages=new GenerateImageImlp();

    String base = "./video/";
    private static final String APP_ID = "604595a5";
    private static final String SECRET_KEY = "7b894a101dc51b36e74b384f3effad00";
    private static final String AUDIO_FILE_PATH = "./video/adjust.wav";


    @GetMapping(value = "/adjust/eye")
    public ResponseVO adjustEye() throws IOException {

        String property_1 = System.getProperty("user.dir");
        String path = property_1 + "\\src\\main\\resources\\static\\photos\\result.png";
        String savePath=property_1 + "\\src\\main\\resources\\static\\photos\\";

        return adjustService.adjustEye(path,savePath,"eye");


    }

    @GetMapping(value = "/adjust/mouth")
    public ResponseVO adjustMouth() throws IOException {

        System.out.println("mouth");

        String property_1 = System.getProperty("user.dir");
        String path = property_1 + "\\src\\main\\resources\\static\\photos\\result.png";
        String savePath=property_1 + "\\src\\main\\resources\\static\\photos\\";


        return adjustService.adjustMouth(path,savePath,"mouth");
    }

//    @PostMapping("/adjust")
//    public ResponseVO adjustEyeAndMouth(@RequestParam(value="multipartFile",required = false) MultipartFile multipartFile,
//                                        @RequestParam(value="background",required = false) String background,
//                                        @RequestParam(value="pageName",required = false) String pageName,
//                                        HttpServletRequest request) throws IOException, InterruptedException {
//        if (multipartFile==null) {
//            System.out.println("multipartFile???null");
//
//        }
//        System.out.println(multipartFile.getContentType());
//        System.out.println(pageName);
//        System.out.println(background);
//        System.out.println("???????????????");
//        System.out.println(multipartFile.getOriginalFilename());
//        System.out.println(multipartFile.getSize());
////        multipartFile.transferTo(new File(base+"music.wav"));
//
//        /**????????????????????????????????????*/
//        File file=new File(base+"adjust.wav");
//        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
//
//        /**??????????????????????????????*/
//        String result=standard();
//
//
//        String property_1 = System.getProperty("user.dir");
//        String path = property_1 + "\\src\\main\\resources\\static\\photos\\result.jpg";
//        String savePath=property_1 + "\\src\\main\\resources\\static\\photos\\";
//
//        boolean isHasEye=result.contains("???");
//        boolean isHasMouth=result.contains("???");
//
//        //int i=0;
//        if(isHasEye==true){
//            return adjustService.adjustEye(path,savePath,"eye");
//        }
//        if(isHasMouth==true){
//            return adjustService.adjustMouth(path,savePath,"mouth");
//        }
//        else{
//            return ResponseVO.buildSuccess("??????????????????????????????");
//        }
//
//        //return ResponseVO.buildSuccess(result);
//    }


    private static String standard() throws InterruptedException {
        //1????????????????????????
        LfasrClient lfasrClient = LfasrClient.getInstance(APP_ID, SECRET_KEY);

        //2?????????
        Message task = lfasrClient.upload(AUDIO_FILE_PATH);
        String taskId = task.getData();
        System.out.println("???????????? taskId???" + taskId);

        //3?????????????????????
        int status = 0;
        while (status != 9) {
            Message message = lfasrClient.getProgress(taskId);
            JSONObject object = JSON.parseObject(message.getData());
            status = object.getInteger("status");
            System.out.println(message.getData());
            TimeUnit.SECONDS.sleep(2);
        }
        //4???????????????
        Message result = lfasrClient.getResult(taskId);
        System.out.println("????????????: \n" + result.getData());

        return result.getData();
        //????????????????????????????????????????????????main??????????????????
//        System.exit(0);
    }

}
