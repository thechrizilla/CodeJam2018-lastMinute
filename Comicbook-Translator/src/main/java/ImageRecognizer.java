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
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
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
	private ArrayList<Shape> dialogueShapes;
	private ArrayList<Shape> contourBounds;
	private ArrayList<Point> contourCenters;

	public ImageRecognizer(String path){
		dialogueShapes = new ArrayList<Shape>();
		contourBounds = new ArrayList<Shape>();
		contourCenters = new ArrayList<Point>();
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

		ImageRecognizer ie = new ImageRecognizer("test5.png");
		try {

			ie.findContoursAndFill();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(ie.getImgText(ie.path));
	}


	public void findContoursAndFill() throws Exception{
		// Matrix
		Mat src = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		// Grayscale the image
		// Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Mat cannyOutput = new Mat();
		Mat blurred = new Mat();
		Mat thresh = new Mat();
		Mat thresh2 = new Mat();

		Imgproc.GaussianBlur(src, blurred, new Size(5,5), 0.0);
		Imgproc.threshold(blurred, thresh, 250, 255, Imgproc.THRESH_BINARY);

//		Imgproc.Canny(src, cannyOutput, 250, 255);
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		Mat original = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
		
		for(MatOfPoint c : contours){
			Moments m = Imgproc.moments(c);
			double cX = m.m10 / m.m00;
			double cY = m.m01 / m.m00;
			Point p = new Point(cX, cY);
			contourCenters.add(p);
			Rect rect = Imgproc.boundingRect(c);
			Rectangle r = new Rectangle(rect.x, rect.y, rect.width, rect.height);
			contourBounds.add(r);
			
			Imgproc.drawContours(original, contours, -2, new Scalar(0, 0, 0));
			Imgproc.fillPoly(original, contours, new Scalar(255, 255, 255));
		}

		srcImg = Mat2BufferedImage(original);
		System.out.println("end of method");
		saveImgAsFile();
	}

	static BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {        
		MatOfByte mob=new MatOfByte();
		Imgcodecs.imencode(".jpg", matrix, mob);
		byte ba[]=mob.toArray();

		BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
		return bi;
	}

	public void findContours2(){
		srcImg = imageToBinary.returnImage(path);


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
			ImageIO.write(srcImg, "png", new File("saved.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Shape findBubbles(){

		return null;
	}

	// Find the text and return the shapes the text are in
	public void findText() throws IOException, TesseractException{
		ITesseract it = new Tesseract();
		BufferedImage bufferedImage = ImageIO.read(new File(path));
		it.setLanguage("eng");

		// long start = System.currentTimeMillis();
		ArrayList<Shape> boundingBoxes = new ArrayList<Shape>();
		boundingBoxes.add(null);

		for(int i = 0; i < boundingBoxes.size(); i++){
			String s = it.doOCR(srcImg, (Rectangle) boundingBoxes.get(i));
			words.add(s);
			System.out.println(s);
		}
	}

	public void findText2() throws IOException{
		ITesseract it = new Tesseract();
		BufferedImage bufferedImage = ImageIO.read(new File(path));
		it.setLanguage("eng");

		// long start = System.currentTimeMillis();

		for (Word word : it.getWords(bufferedImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE)) {
			Rectangle boundingBox = word.getBoundingBox();
			System.out.println("" + boundingBox);	
			dialogueShapes.add(boundingBox);
			System.out.println("Boxes size: " + dialogueShapes.size());
			System.out.println("" + word);
			words.add("" + word.toString());
		}
		System.out.println("boxes final size: " + dialogueShapes.size());
		// System.out.println("time = " + (System.currentTimeMillis() - start));
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

	public ArrayList<String> getWords(){
		return words;
	}

}
