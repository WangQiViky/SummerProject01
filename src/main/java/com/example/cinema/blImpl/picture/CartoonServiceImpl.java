package com.example.cinema.blImpl.picture;

import com.example.cinema.bl.picture.CartoonService;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
public class CartoonServiceImpl implements CartoonService {
    @Autowired
    OpenCVFaceSwap openCVFaceSwap;

    @Autowired
    PhotoOperation photoOperation;


    @Autowired
    UploadPhotoImpl uploadPhoto;
    // UploadPhotoImpl uploadPhoto=new UploadPhotoImpl();

    public static String savePath ;

    @Override
    public ResponseVO ps(String path_real, String path_cartoon){
        try{
            String property_1 = System.getProperty("user.dir");
            String savePath = property_1 + "\\src\\main\\resources\\static\\photos\\";

            System.out.println(path_cartoon);
            boolean isTrue=openCVFaceSwap.faceMerge(path_real,path_cartoon,savePath,"opencv",false);

            if(isTrue==false){
                return ResponseVO.buildSuccess("false");
            }
            String result_path=property_1+"\\src\\main\\resources\\static\\photos\\result_ps.png";
            System.out.println(result_path);
            String result=uploadPhoto.uploadFromService("result.png");

            return ResponseVO.buildSuccess(result);

        }catch (Exception e){
            System.out.println(e.toString());
//            return ResponseVO.buildFailure("直接ps卡通脸失败");
            return ResponseVO.buildSuccess("false");
        }
    }


    @Override
    public ResponseVO fix(String path_real, String path_cartoon){
        try{

            String property_1 = System.getProperty("user.dir");
            String savePath = property_1 + "\\src\\main\\resources\\static\\photos\\";

            System.out.println(path_cartoon);
            boolean isTrue=openCVFaceSwap.faceMerge(path_real,path_cartoon,savePath,"opencv",true);

            if(isTrue==false){
                return ResponseVO.buildSuccess("false");
            }


            String result_path=property_1+"\\src\\main\\resources\\static\\photos\\result_fix.png";
            System.out.println(result_path);

            String result=uploadPhoto.uploadFromService("result.png");
            return ResponseVO.buildSuccess(result);

        }catch(Exception e){
            System.out.println(e.toString());
            return ResponseVO.buildSuccess("false");
//            return ResponseVO.buildFailure("人脸和卡通人脸融合失败");
        }
    }


    @Override
    public ResponseVO getCartoon(String path_cartoon, String save_path) throws IOException {
        File file1=new File(path_cartoon);
        File file2=new File(save_path);
        byte[] b=new byte[(int)file1.length()];
        FileInputStream in=null;
        FileOutputStream out=null;
        try {
            in=new FileInputStream(file1);
            //没有指定文件则会创建
            out=new FileOutputStream(file2);
            //read()--int，-1表示读取完毕
            while(in.read(b)!=-1){
                out.write(b);
            }
            out.flush();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result=uploadPhoto.uploadFromService("result.png");

        String property_1 = System.getProperty("user.dir");
        String path_result = property_1 + "\\src\\main\\resources\\static\\photos\\result.png";

        photoOperation.savePng(path_result);
        return ResponseVO.buildSuccess(result);


    }


}


