package com.example.cinema.controller.picture;

import com.example.cinema.bl.picture.PhotoService;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {

    @Autowired
    PhotoService photoService;

    @GetMapping(value = "/photo")
    public ResponseVO takePicture(){
        return photoService.takePicture();
    }


}
