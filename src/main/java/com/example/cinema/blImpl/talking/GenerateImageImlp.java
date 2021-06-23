package com.example.cinema.blImpl.talking;

import com.example.cinema.bl.talking.GenerateImages;
import com.example.cinema.po.ImageContent;
import com.example.cinema.vo.ResponseVO;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GenerateImageImlp implements GenerateImages {
    /**
     *
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     * @param file
     *            源文件(图片)
     * @param waterFile
     *            水印文件(图片)
     * @param x
     *            距离右下角的X偏移量
     * @param y
     *            距离右下角的Y偏移量
     * @param alpha
     *            透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage watermark(File file, File waterFile, int x, int y, float alpha) throws IOException {
        // 获取底图
        BufferedImage buffImg = ImageIO.read(file);
        // 获取层图
        BufferedImage waterImg = ImageIO.read(waterFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
        int waterImgHeight = waterImg.getHeight();// 获取层图的高度
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }

    /**
     * 输出水印图片
     *
     * @param buffImg
     *            图像加水印之后的BufferedImage对象
     * @param savePath
     *            图像加水印之后的保存路径
     */
    private void generateWaterFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
            ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     *
     * @param args
     * @throws IOException
     *             IO异常直接抛出了
     * @author bls
     * @return
     */
    /**参数是一个imagecontent的对象，返回保存在本地的图片名*/

    public String generateImage(ImageContent args) throws IOException {
        String qianzhui="./image/";
        String sourceFilePath = "background-"+args.getBackground()+".png";
        System.out.println(sourceFilePath);
        String peoplePartner = "people/花木兰公主.png";
        String people="people/白雪公主.png";
        String tool="tool/水晶球.png";
        String saveFilePath = args.getFileName()+".png";
        GenerateImageImlp newImageUtils = new GenerateImageImlp();
        // 构建叠加层
        BufferedImage buffImg = GenerateImageImlp.watermark(new File(qianzhui+sourceFilePath),
                new File(qianzhui+peoplePartner),
                350, 100, 1.0f);
        // 输出水印图片
        newImageUtils.generateWaterFile(buffImg, qianzhui+saveFilePath);

        buffImg=GenerateImageImlp.watermark(new File(qianzhui+saveFilePath),
                new File(qianzhui+people),
                50, 100, 1.0f);
        newImageUtils.generateWaterFile(buffImg, qianzhui+saveFilePath);

        buffImg=GenerateImageImlp.watermark(new File(qianzhui+saveFilePath),
                new File(qianzhui+tool),
                250, 100, 1.0f);
        newImageUtils.generateWaterFile(buffImg, qianzhui+saveFilePath);

        return saveFilePath;
    }
    public ResponseVO getImageRe(){
        String saveFilePath = "./image/result.png";
        try{
            return ResponseVO.buildSuccess(new File(saveFilePath));

        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.toString());
            return ResponseVO.buildFailure("生成图片失败");
        }
    }
    public static void main(String[] args) throws IOException {
        GenerateImages generateImages=new GenerateImageImlp();
        ImageContent imageContent=new ImageContent();
        imageContent.setBackground("ocean");
        imageContent.setFileName("page1");
        generateImages.generateImage(imageContent);
    }
}
