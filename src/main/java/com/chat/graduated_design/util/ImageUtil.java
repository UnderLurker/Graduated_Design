package com.chat.graduated_design.util;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class ImageUtil {

	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @Author:
	 * @CreateTime:
	 * @param file base64编码字符串
	 * @param path 图片路径-具体到文件
	 * @return
	 */
	public static String generateImage(String file, String path) {
		// 解密
		try {
			String uuid = UUID.randomUUID().toString().toLowerCase();
			// 图片分类路径+图片名+图片后缀
			String imgClassPath = path.concat(uuid).concat(".png");
			// 解密
			Base64.Decoder decoder = Base64.getDecoder();
			// 去掉base64前缀 data:image/jpeg;base64,
			file = file.substring(file.indexOf(",", 1) + 1, file.length());
			byte[] b = decoder.decode(file);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			// 保存图片
			OutputStream out = new FileOutputStream(imgClassPath);
			out.write(b);
			out.flush();
			out.close();
			// 返回图片的相对路径 = 图片分类路径+图片名+图片后缀
			return uuid + ".png";
		} catch (IOException e) {
			return null;
		}
	}

	// 初始化人脸探测器
	static CascadeClassifier faceDetector;

	static String xmlPath="F:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml";	//配置文件路径

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try{
			faceDetector = new CascadeClassifier(xmlPath);
		}catch(UnsatisfiedLinkError e){

		}
	}

	// 灰度化主体
	public static Mat toMat(Mat img){
		Mat image=new Mat();
		// 灰度化
		Imgproc.cvtColor(img, image, Imgproc.COLOR_BGR2GRAY,0);
		// 探测人脸
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		// rect中人脸图片的范围
		for (Rect rect : faceDetections.toArray()) {
			Mat face = new Mat(image, rect);
			return face;
		}
		return null;
	}
	// 灰度化人脸
	public static Mat conv_Mat(String img) {
		Mat image;
		try{
			image = Imgcodecs.imread(img);
		}catch(Exception e){
			return null;
		}
		return toMat(image);
	}
	//数据流转图像并灰度化返回矩阵
	public static Mat conv_Mat_By_Stream(byte[] stream){
		Mat image;
		try{
			image = Imgcodecs.imdecode(new MatOfByte(stream), Imgcodecs.IMREAD_UNCHANGED);
		}catch(Exception e){
			return null;
		}
		return toMat(image);
	}


	//比较主体
	public static double compare_main(Mat mat_1,Mat mat_2){
		Mat hist_1 = new Mat();
		Mat hist_2 = new Mat();

		// 颜色范围
		MatOfFloat ranges = new MatOfFloat(0f, 256f);
		// 直方图大小， 越大匹配越精确 (越慢)
		MatOfInt histSize = new MatOfInt(1000);

		Imgproc.calcHist(Arrays.asList(mat_1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges);
		Imgproc.calcHist(Arrays.asList(mat_2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges);

		// CORREL 相关系数
		double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
		return res;
	}
	//比较图片直方图
	public static double compare_image_St(String img, byte[] stream) {
		Mat mat_1 = conv_Mat(img);
		Mat mat_2 = conv_Mat_By_Stream(stream);
		if(mat_1==null||mat_2==null) return -1;
		return compare_main(mat_1, mat_2);
	}
	public static double compare_image_SS(String img_0,String img_1){
		Mat mat_0 = conv_Mat(img_0);
		Mat mat_1 = conv_Mat(img_1);
		if(mat_1==null||mat_0==null) return -1;
		return compare_main(mat_0, mat_1);
	}

	//判断是否相似
	public static double isSame(String img_1,String img_2){
		double compareHist=compare_image_SS(img_1,img_2);
		return compareHist;
	}
	public static double isSame(String img_1,byte[] stream){
		double compareHist=compare_image_St(img_1, stream);
		return compareHist;
	}

	public static void main(String[] args) {
		String basePicPath = "F:/";
		double compareHist = ImageUtil.isSame(basePicPath + "1.png", basePicPath + "2.png");
		System.out.println(compareHist);
		
	}

}
