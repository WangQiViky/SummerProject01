package com.example.cinema.blImpl.picture;

import com.example.cinema.bl.picture.AdjustService;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AdjustServiceImpl implements AdjustService {
    @Autowired
    EyeAndMouthAdjust eyeAndMouthAdjust;


    UploadPhotoImpl uploadPhoto=new UploadPhotoImpl();

    @Override
    public ResponseVO adjustEye(String path, String savePath, String type) throws IOException {
        boolean isTrue=eyeAndMouthAdjust.findPoints(path, savePath, type);

        if(isTrue==false){
            return ResponseVO.buildSuccess("false");
        }
        String result=uploadPhoto.uploadFromService("result.png");
        return ResponseVO.buildSuccess(result);
    }

    @Override
    public ResponseVO adjustMouth(String path, String savePath, String type) throws IOException {
        boolean isTrue=eyeAndMouthAdjust.findPoints(path,savePath,type);

        if(isTrue==false){
            return ResponseVO.buildSuccess("false");
        }
        String result=uploadPhoto.uploadFromService("result.png");
        return ResponseVO.buildSuccess(result);
    }
}
