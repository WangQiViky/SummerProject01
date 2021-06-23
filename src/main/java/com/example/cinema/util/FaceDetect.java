package com.example.cinema.util;

//import com.example.cinema.bean.FaceV3DetectBean;
//import com.baidu.aip.face.AipFace;
//import com.baidu.aip.util.Util;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.Facemark;
import org.bytedeco.opencv.opencv_face.FacemarkLBF;
//import org.json.JSONObject;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: FaceDetect
 * @description: 人脸检测接口返回处理方法
 **/
public class FaceDetect {
    //    private static AipFace aipFace;
//    //请填写自己应用的appid apikey secretkey
//    static {
//        aipFace = new AipFace("","","");
//    }
    static Facemark facemark = FacemarkLBF.create();
    static{
        try{
            String property = System.getProperty("user.dir");
            String path = property + "\\src\\main\\resources\\static\\lbfmodel.yaml";
            facemark.loadModel(path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static List<Point2f> detect(Mat frame){
        Mat gray = new Mat();
        // 将视频帧转换至灰度图, 因为Face Detector的输入是灰度图
        opencv_imgproc.cvtColor(frame, gray, opencv_imgproc.COLOR_BGR2GRAY);
        // 存储人脸矩形框的容器
        RectVector faces = CheckFaceAndEye.findFaces(gray);
        // 人脸关键点的容器
        Point2fVectorVector landmarks = new Point2fVectorVector();
        if(faces == null){
//            Assert.notNull(faces,"没有检测到人脸数据");
            List<Point2f> poinst1 = new ArrayList<>();
            return poinst1;
        }
        // 运行人脸关键点检测器（landmark detector）
        boolean success = facemark.fit(frame, faces, landmarks);
        Assert.isTrue(success,"获取人脸关键点失败");
        List<Point2f> poinst = new ArrayList<>();
        //获取一张脸的
        if(success){
            Point2fVector pf = landmarks.get(0);
            for(Point2f f : pf.get()){
                poinst.add(new Point2f(f.x()-3,f.y()));
            }
            return  poinst;
        }
        return  null;
    }
}
