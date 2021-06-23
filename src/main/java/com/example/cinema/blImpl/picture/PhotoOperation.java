package com.example.cinema.blImpl.picture;

import org.apache.commons.lang3.tuple.Pair;
import org.bytedeco.javacpp.indexer.FloatRawIndexer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_core.CV_32FC3;
import static org.bytedeco.opencv.global.opencv_imgproc.CV_AA;

@Service
public class PhotoOperation {


    public  String savePath=System.getProperty("user.dir")+ "\\src\\main\\resources\\static\\photos\\";


    /**
     * 将融合之后形成的图片的黑底变成透明的颜色
     * @param path
     * @throws IOException
     */
    public void savePng(String path) throws IOException {

        BufferedImage temp =  ImageIO.read(new File(path));//取得图片
        int imgHeight = temp.getHeight();//取得图片的长和宽
        int imgWidth = temp.getWidth();
        int c = temp.getRGB(3, 3);
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight,
                BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
        for(int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时把背景设为透明
        {
            for(int j = 0; j < imgHeight; ++j)
            {
                if(temp.getRGB(i, j) == c)
                    bi.setRGB(i, j, c & 0x00ffffff);//这里把背景设为透明
                else
                    bi.setRGB(i, j, temp.getRGB(i, j));
            }
        }

        File outputfile = new File(path);
        ImageIO.write(bi, "png", outputfile);
        String property_1 = System.getProperty("user.dir");
        String path_result = property_1 + "\\image\\people\\女孩.png";
        File result_out=new File(path_result);
        ImageIO.write(bi,"png",result_out);
        String path_result1 = property_1 + "\\image\\people\\男孩.png";
        File result_out1=new File(path_result1);
        ImageIO.write(bi,"png",result_out1);
        String path_result2 = property_1 + "\\image\\people\\我.png";
        File result_out2=new File(path_result2);
        ImageIO.write(bi,"png",result_out2);

    }

    //给图像画点
    public  void drawPoints( Mat face1,List<Point2f> points1,String name){
        for(int i=0;i<points1.size();i++){
            opencv_imgproc.putText(face1,i+"", new Point((int)points1.get(i).x(),(int)points1.get(i).y()), opencv_imgproc.FONT_HERSHEY_SCRIPT_SIMPLEX,0.3, new Scalar(255, 0, 0, 0));
        }
        opencv_imgcodecs.imwrite(savePath+ File.separator + name +"_dian.jpg", face1);
    }
    //根据点给图像画线-三角形
    public  void drawTriangles(String name,Mat img,List<List<Point>> triangles, Scalar color){

        for (List<Point> points : triangles){

            opencv_imgproc.line(img, points.get(0), points.get(1), new Scalar(255, 255) , 1, CV_AA, 0);
            opencv_imgproc.line(img, points.get(1), points.get(2), new Scalar(255, 255) , 1, CV_AA, 0);
            opencv_imgproc.line(img, points.get(2), points.get(0), new Scalar(255, 255) , 1, CV_AA, 0);
        }
        opencv_imgcodecs.imwrite(savePath+ File.separator  +name +"_sanjiao.jpg", img);
    }






    /**
     * List exchange to MatOfPoint
     * @param points
     * @return
     */
    public  Mat list2MP(List<Point> points) {
        Mat points2 = new Mat(points.size());
        for(Point pf : points){
            Mat pm = new Mat(pf);
            points2.push_back(pm);
        }
        return points2;
    }

    /**
     * List exchange to MatOfPoint2f
     * @param points
     * @return
     */
    public  Mat list2MP2(List<Point2f> points) {
        Mat points2 = new Mat(points.size());
        for(Point2f pf : points){
            Mat pm = new Mat(pf);
            points2.push_back(pm);
        }
        return points2;
    }



    //计算凸包办法
    public Pair<List<Point>,List<Point2f>> getConvexHull(List<Point2f> points){
        // 计算凸包
        Mat convexPointsIdx2 = new Mat();
        //用了好久的时间实验出来的
        Mat points2m = new Mat(points.size());
        for(Point2f pf : points){
            Mat pm = new Mat(pf);
            points2m.push_back(pm);
        }
        //寻找凸包
        opencv_imgproc.convexHull(points2m, convexPointsIdx2,false,true);
        FloatRawIndexer matIndex = convexPointsIdx2.createIndexer();
        List<Point> convexPoints2 = new LinkedList<Point>();
        List<Point2f> convexPoints2f = new LinkedList<Point2f>();
        long rows = convexPointsIdx2.rows();
        for (int i = 0; i < rows; i++) {
            float x = matIndex.get(i,0);
            float y = matIndex.get(i,1);
            convexPoints2.add(new Point((int)x,(int)y));
            convexPoints2f.add(new Point2f(x,y));
        }
        return Pair.of(convexPoints2,convexPoints2f);

    }


    //计算凸包办法
    public  Pair<List<Point2f>,List<Point2f>> getConvexHull(Mat imgCV2, Mat imgCV1, List<Point2f> points1, List<Point2f> points2){
        // 计算凸包
        Mat imgCV1Warped = imgCV2.clone();
        imgCV1.convertTo(imgCV1, CV_32FC3);
        imgCV1Warped.convertTo(imgCV1Warped, CV_32FC3);

        Mat hull = new Mat();
        //用了好久的时间实验出来的
        Mat points2m = new Mat(points2.size());
        for(Point2f pf : points2){
            Mat pm = new Mat(pf);
            points2m.push_back(pm);
        }


        /***
         * void cv::convexHull (   InputArray  points,
         *                         OutputArray     hull,
         *                         bool    clockwise = false,
         *                         bool    returnPoints = true
         * )
         * ————————————————
         points:输入的二维点集，Mat类型数据即可
         hull:输出参数，用于输出函数调用后找到的凸包
         clockwise:操作方向，当标识符为真时，输出凸包为顺时针方向，否则为逆时针方向。
         returnPoints:操作标识符，默认值为true，此时返回各凸包的各个点，否则返回凸包各点的指数，当输出数组时std::vector时，此标识被忽略。
         ————————————————
         原文链接：https://blog.csdn.net/keith_bb/article/details/70194073
         */

        //得到凸包，hull是输出的带的凸包的参数
        opencv_imgproc.convexHull(points2m, hull,false,true);

        //hull2是调用opencv的convexHull方法得到对应的凸包的对应的位置
        //hull1是根据图像1的凸包的特征点的序号对应的得到的凸包位置
        List<Point2f> hull1 = new LinkedList<Point2f>();
        List<Point2f> hull2 = new LinkedList<Point2f>();
        // 保存组成凸包的关键点
        List<Point2f> hullPoinst = new LinkedList<Point2f>();
        FloatRawIndexer hullIndex = hull.createIndexer();
        long rows = hull.rows();
        for (int i = 0; i < rows; i++) {
            hullPoinst.add(new Point2f(hullIndex.get(i,0),hullIndex.get(i,1)));
        }
        if (hullPoinst.size() > 0) {
            for(Point2f hp : hullPoinst){
                for(int j=0, totalj=points1.size();j<totalj;j++){
                    if (hp.x() == points2.get(j).x() && hp.y() == points2.get(j).y()) {
                        hull1.add(points1.get(j));
                        hull2.add(points2.get(j));
                    }
                }
            }
        }


        //返回的就是两个图像的凸包的位置序列
        return  Pair.of(hull1, hull2);
    }




}
