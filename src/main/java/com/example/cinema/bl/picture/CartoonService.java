package com.example.cinema.bl.picture;

import com.example.cinema.vo.ResponseVO;

import java.io.IOException;

public interface CartoonService {

    /**
     * 将真实的人脸直接ps在卡通图片上面
     */
    ResponseVO ps(String path_real, String path_cartoon);


    /**
     * 将真实的人脸直接和卡通人脸进行融合
     */
    ResponseVO fix(String path_real, String path_cartoon);


    /**
     * 将直接选择的卡通图片保存到本地更名为result
     */
    ResponseVO getCartoon(String path_cartoon, String save_path) throws IOException;
}
