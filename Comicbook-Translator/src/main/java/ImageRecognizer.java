package main.java;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import net.sourceforge.tess4j.*;

public class ImageRecognizer {

	private String path;
	private BufferedImage srcImg;
	private BufferedImage gsImg;

	private Mat srcGray = new Mat();
	private int threshold = 100;

	private ArrayList<Shape> bubbles;
	private ArrayList<String> words;
	private ArrayList<Shape> boundingBoxes;

	public ImageRecognizer(String path){
		boundingBoxes = new ArrayList<Shape>();
		words = new ArrayList<String>();
		try {
			this.path = path;
			BufferedImage img = ImageIO.read(new File(path));
			this.srcImg = img;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ImageRecognizer ie = new ImageRecognizer("test4.jpg");
		try {

			ie.findContours2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(ie.getImgText(ie.path));
	}


	public void findContours(){
		// Matrix
		Mat src = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		// Grayscale the image
		// Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Mat cannyOutput = new Mat();
		Mat blurred = new Mat();
		Mat thresh = new Mat();

		Imgproc.GaussianBlur(src, blurred, new Size(5,5), 0.0);
		Imgproc.threshold(blurred, thresh, 60, 255, Imgproc.THRESH_BINARY);

		Imgproc.Canny(src, cannyOutput, threshold, threshold * 2);
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		for(MatOfPoint c : contours){
			Moments m = Imgproc.moments(c);
			double cX = m.m10 / m.m00;
			double cY = m.m01 / m.m00;
			Point p = new Point(cX, cY);


			Graphics2D graphic = srcImg.createGraphics();
			graphic.setColor(Color.RED);
			graphic.fill(new Rectangle((int) cX,(int) cY, 10, 10));


			// Imgproc.circle(src, p, 7, new Scalar(0), -1);
		}

		saveImgAsFile();

	}

	public void findContours2(){
		// Matrix
		//Mat src = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_COLOR);
		Mat src = Imgcodecs.imread(path);
		// Grayscale the image
		// Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Mat filtered = new Mat();
		Mat cannyOutput = new Mat();
		Mat blurred = new Mat();
		Mat thresh = new Mat();
		Mat hierarchy = new Mat();

		Imgproc.bilateralFilter(src, filtered, 5, 175, 175);
		Imgproc.Canny(filtered, cannyOutput, 75, 200);

		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


		for(MatOfPoint c : contours){
			Moments m = Imgproc.moments(c);
			double cX = m.m10 / m.m00;
			double cY = m.m01 / m.m00;
			Point p = new Point(cX, cY);


			Graphics2D graphic = srcImg.createGraphics();
			graphic.setColor(Color.RED);
			graphic.fill(new Rectangle((int) cX,(int) cY, 10, 10));


			// Imgproc.circle(src, p, 7, new Scalar(0), -1);
		}

		saveImgAsFile();

	}

	private void saveImgAsFile(){
		try {
			ImageIO.write(srcImg, "jpg", new File("saved.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Shape findBubbles(){

		return null;
	}

	// Find the text and return the shapes the text are in
	public void findText() throws IOException{
		ITesseract it = new Tesseract();
		BufferedImage bufferedImage = ImageIO.read(new File(path));
		it.setLanguage("eng");

		long start = System.currentTimeMillis();

		for (Word word : it.getWords(bufferedImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE)) {
			Rectangle boundingBox = word.getBoundingBox();
			boundingBoxes.add(boundingBox);
			words.add("" + word.toString());

			System.out.println("" + boundingBox);	
			System.out.println("" + word);
		}
		System.out.println("time = " + (System.currentTimeMillis() - start));
	}



	public String getImgText(String imageLocation) {

		ITesseract instance = new Tesseract();
		try{
			String imgText = instance.doOCR(new File(imageLocation));
			return imgText;
		} 
		catch (TesseractException e){
			e.getMessage();
			return "Error while reading image";
		}
	}

	public ArrayList<Shape> getBoundingBoxes(){
		return boundingBoxes;
	}

	public ArrayList<String> getWords(){
		return words;
	}

}
