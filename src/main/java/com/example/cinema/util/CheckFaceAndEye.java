package com.example.cinema.util;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

public class CheckFaceAndEye {
	//正面人脸
	static CascadeClassifier faceDetector;
	//眼睛
	static CascadeClassifier eyesDetector;
	static {
		try{
			String property = System.getProperty("user.dir");
			String path1 = property + "\\src\\main\\resources\\static\\lbpcascade_frontalface.xml";
			String path2=property+"\\src\\main\\resources\\static\\haarcascade_eye_tree_eyeglasses.xml";
//			faceDetector = new CascadeClassifier("D:\\openCV\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
//			eyesDetector = new CascadeClassifier("D:\\openCV\\opencv\\sources\\data\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml");

			faceDetector = new CascadeClassifier(path1);
			eyesDetector = new CascadeClassifier(path2);

		}catch (Exception e){
			e.printStackTrace();
		}

	}

	
	/**
	 * 检测是否有正面人脸
	 * @param image
	 * @return boolean
	 */
	public static boolean checkHasFace(Mat image) {
		RectVector faces = findFaces(image);
		boolean flag =  faces != null;
		faces.clear();
		return flag;
	}
	
	/**
	 * 检测是否有人的眼睛
	 * @param image
	 * @return boolean
	 */
	public static boolean checkHasEye(Mat image) {
		RectVector eyes = findEyes(image);
		boolean flag =  eyes != null;
		eyes.clear();
		return flag;
	}
	
	/**
	 * 获取人脸数据
	 * @param image
	 * @return RectVector
	 */
	public static RectVector findFaces(Mat image) {  
		Mat imageGray = FaceAndEyeToos.doColorHist(image); 
		//进行人脸识别
		RectVector faceDetections = new RectVector(); 
		
		faceDetector.detectMultiScale(imageGray, faceDetections);
		if(faceDetections.empty()){
			return null;
		}   
		
		return faceDetections;
	}
	
	/**
	 * 获取人眼数据
	 * @param image
	 * @return RectVector
	 */
	public static RectVector findEyes(Mat image) { 
		Mat imageGray = FaceAndEyeToos.doColorHist(image); 
		// 存储找到的眼睛矩形。
		RectVector eyes = new RectVector();
		
		eyesDetector.detectMultiScale(imageGray, eyes); 
		if(eyes.empty()){
			return null;
		} 
		
		return eyes;
	}
}