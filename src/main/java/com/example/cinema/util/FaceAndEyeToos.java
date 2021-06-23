package com.example.cinema.util;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;

import java.util.List;

public class FaceAndEyeToos {
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(Integer.MAX_VALUE);
	}
	
	/**
	 * 保存头像
	 * @param faces
	 * @param oriFrame
	 * @param addToModel 是否添加到识别模型中
	 */
	public static void saveFace(RectVector faces, Mat oriFrame, boolean addToModel){
		if(faces == null || faces.empty()){
			return ;
		}
		
		Mat imageGray = doColorHist(oriFrame);
		
		Mat saveFace = null; 
		for (Rect rect : faces.get()) {
			saveFace = new Mat(oriFrame, rect); 
			//opencv_imgproc.resize(saveFace, saveFace, new Size(Common.faceWidth,Common.faceHeight));
			/*if(saveFace.sizeof() > 0){
				doSave(saveFace,Common.saveFacePath,addToModel);
			} */
		}
		
		saveFace.close();
		imageGray.close();
	}
	
	/**
	 * 在人脸上画框
	 * @param faces
	 * @param oriFrame
	 */
	public static void drawRectangleFace(RectVector faces,Mat oriFrame){  
		if(faces == null || faces.empty()){
			return ;
		}
		for (Rect rect : faces.get()) { 
			// 在原图上进行画框
			opencv_imgproc.rectangle(oriFrame, new Point(rect.x(), rect.y()),
					new Point(rect.x() + rect.width(), rect.y() + rect.height()), new Scalar(0, 255, 0, 0));
		} 
	} 
	
	/**
	 * 在人眼睛上画圈
	 * @param faces
	 * @param oriFrame
	 */
	public static void drawCircleEye(RectVector faces, Mat oriFrame){  
		if(faces == null || faces.empty()){
			return ;
		}
		//脸数值
		for (int i = 0, total = faces.get().length; i < total; i++) { 
			
			Mat faceROI = new Mat(oriFrame, faces.get()[i]);
			// 存储找到的眼睛矩形。
			RectVector eyes = CheckFaceAndEye.findEyes(faceROI);  
			if(eyes == null || eyes.empty()){
				continue;
			}
			//眼睛数组
			for (int j = 0; j < eyes.get().length; j++) {
				//眼睛的坐标点
				Point eyeCenter = new Point(faces.get()[i].x() + eyes.get()[j].x() + eyes.get()[j].width() / 2,
						faces.get()[i].y() + eyes.get()[j].y() + eyes.get()[j].height() / 2);
				//半径
				int radius = (int) Math.round((eyes.get()[j].width() + eyes.get()[j].height()) * 0.25);
				//添加圈
				opencv_imgproc.circle(oriFrame, eyeCenter, radius, new Scalar(255, 0, 0, 0), 4, 8, 0);
			}
		}  
	}
	
	/**
	 * 保存原图
	 * @param oriFrame
	 */
	public static void saveOriFace(Mat oriFrame){

		//doSave(oriFrame,Common.saveOriFacePath,false);
	}
	
	/**
	 * 图片保存
	 * @param oriFrame
	 * @param path
//	 */
//	public static void doSave(Mat oriFrame,String path,boolean addToModel) throws Exception {
//		String fileName = WorkId.sortUID() + ".jpg";
//		opencv_imgcodecs.imwrite(path + fileName ,oriFrame);
//		/*if(addToModel){
//			IdentityPeople.updateModel(opencv_imgcodecs.imread(path + fileName,opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE));
//		} */
//	}
	
	/**
	 * 图片保存
	 * @param oriFrame
	 */
	public static Mat doColorHist(Mat oriFrame){ 
		if(oriFrame.channels() == 3){
			Mat imageGray = new Mat();
			// 将视频帧转换至灰度图 
			opencv_imgproc.cvtColor(oriFrame, imageGray, opencv_imgproc.COLOR_BGRA2GRAY);
			// 直方图均衡化，增加对比度方便处理
			opencv_imgproc.equalizeHist(imageGray, imageGray);
			
			return imageGray;
		}
		return oriFrame;
	}
	/**
	 * 在视频上显示用户信息
	 * @param faces
	 * @param oriFrame
	 * @param msg 
	 */
	 
	public static Mat putMsg(RectVector faces, Mat oriFrame, List<String> msg){
		
		for(int i=0, total=faces.get().length; i<total; i++){
			 /*不支持中文*/
			//opencv_imgproc.putText(oriFrame,msg.get(i), new Point(faces.get()[i].x(),faces.get()[i].y()), opencv_imgproc.CV_FONT_HERSHEY_SCRIPT_COMPLEX, 1, new Scalar(255, 0, 0, 0));
			/*转成 bufferdImage进行文字添加*/ 
			//oriFrame = ConverterData.b2M(ConverterData.m2B(oriFrame),oriFrame.type(),msg.get(i),faces.get()[i].x(),faces.get()[i].y());
		} 
		return oriFrame;
	}
	
}