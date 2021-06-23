package com.example.cinema.bl.picture;

import com.example.cinema.vo.ResponseVO;

public interface PhotoService {

    /**
     * 调用摄像头拍照，并将照片保存到本地
     * @return
     */
    ResponseVO takePicture();
}
