package com.example.cinema.po;

/**存储一个图应该包含的内容的对象*/
public class ImageContent {
    String background;
    String fileName;

    public void printContent(){

    }
    /**判断这个Image Content的内容是否完成*/
    public boolean isValid(){
        if(background==null){
            return false;
        }
        if(fileName==null){
            return false;
        }
        return true;
    }

    public String getBackground() {
        return background;
    }

    public String getFileName() {
        return fileName;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
