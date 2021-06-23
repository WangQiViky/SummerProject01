package com.example.cinema.bl.talking;

import com.example.cinema.po.ImageContent;
import com.example.cinema.vo.ResponseVO;

import java.io.IOException;

public interface GenerateImages {
    public String generateImage(ImageContent args) throws IOException;
//    public ResponseVO getImageRe();
}