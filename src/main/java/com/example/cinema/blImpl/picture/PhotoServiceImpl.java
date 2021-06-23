package com.example.cinema.blImpl.picture;

import com.example.cinema.bl.picture.PhotoService;
import com.example.cinema.util.VideoPanel;
import com.example.cinema.vo.ResponseVO;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PhotoServiceImpl implements PhotoService {

    UploadPhotoImpl uploadPhoto=new UploadPhotoImpl();

    @Override
    public ResponseVO takePicture(){
        try{
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Welcome to OpenCV " + Core.VERSION);

            // 新建窗口
            JFrame cameraFrame = new JFrame("camera");
            cameraFrame.setAlwaysOnTop(true);
            cameraFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            cameraFrame.setSize(640, 480);
            cameraFrame.setBounds(0, 0, cameraFrame.getWidth(), cameraFrame.getHeight());
            cameraFrame.setLocationRelativeTo(null);
            VideoPanel videoPanel = new VideoPanel();
            cameraFrame.setContentPane(videoPanel);
            cameraFrame.setVisible(true);

            CascadeClassifier faceCascade = new CascadeClassifier();
            String property_1 = System.getProperty("user.dir");
            String path_1 = property_1 + "\\src\\main\\resources\\static\\haarcascade_frontalface_alt.xml";
            //String path_1 = "D:\\app\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml";

            // 获取模型文件
            //  faceCascade.load("D:\\app\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml");
            faceCascade.load(path_1);

            // 调用摄像头
            VideoCapture capture = new VideoCapture();
            try {
                capture.open(0);
                if (capture.isOpened()) {
                    Mat image = new Mat();
                    int i=0;
                    while(i<100) {
                        capture.read(image);
                        i++;
                        if (!image.empty()) {
                            detectAndDisplay(image, faceCascade);
                            videoPanel.setImageWithMat(image);
                            cameraFrame.repaint();
                            Thread.sleep(10);

                        } else {
                            break;
                        }
                    }
                    capture.read(image);
                    videoPanel.saveImageWithMat(image);
                    capture.release();
                    cameraFrame.dispose();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                capture.release();
            }

            String property = System.getProperty("user.dir");
            String path = property + "\\src\\main\\resources\\static\\photos\\photo.png";

            System.out.println(path);
            // uploadPictureToAdvice(path,rp);
            //String name="photo.jpg";
            String result=uploadPhoto.uploadFromService("photo.png");

            return ResponseVO.buildSuccess(result);


        }catch (Exception e){
            System.out.println(e.toString());
            return ResponseVO.buildFailure("后端调用摄像头出错");
        }
    }


    /**
     * 将后端生成的图片直接传到前端
     * @param path
     * @param rp
     */
    public static void uploadPictureToAdvice(String path,HttpServletResponse rp){

//        String property = System.getProperty("user.dir");
//        String path = property + "\\src\\main\\resources\\static\\photos\\photo.jpg";
        File imageFile = new File(path);
        if (imageFile.exists()) {
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                fis = new FileInputStream(imageFile);
                os = rp.getOutputStream();
                int count = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    // os.flush();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        System.out.println(rp);
    }


    /**
     * 绘制图形界面
     * @param frame
     * @param faceCascade
     */
    public static void detectAndDisplay(Mat frame, CascadeClassifier faceCascade)
    {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        int absoluteFaceSize = 0;
        int height = grayFrame.rows();
        if (Math.round(height * 0.2f) > 0) {
            absoluteFaceSize = Math.round(height * 0.2f);
        }

        // detect faces
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
    }



    /**
     *
     * @param f 表示帧
     * @param targetFileName 存储路径
     */
    public static void doExecuteFrame(Frame f, String targetFileName) {
        if (null ==f ||null ==f.image) {
            return;
        }
        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage bi =converter.getBufferedImage(f);
        File output =new File(targetFileName);
        try {
            ImageIO.write(bi,"jpg",output);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
