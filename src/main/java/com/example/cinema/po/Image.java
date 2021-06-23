package com.example.cinema.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Image {

    /**
     * 图片id
     */
    private int id;
    /**
     * 页面名字
     */
    private String page_name;
    /**
     * 哈希值
     */
    private String hash_name;

    public Image() {
    }


}
