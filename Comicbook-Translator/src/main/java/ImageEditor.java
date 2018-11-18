package main.java;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;

public class ImageEditor {

	private BufferedImage img;
	private String path;

	private BufferedImage modifiedImg;
	private BufferedImage gsImg;

	private ArrayList<Shape> boundingBoxes;
	private ArrayList<String> words;
	private ArrayList<Shape> contourBounds;
	private ArrayList<Point> contourCenters;

	public ImageEditor(String path) {
		boundingBoxes = new ArrayList<Shape>();
		words = new ArrayList<String>();
		contourBounds = new ArrayList<Shape>();
		contourCenters = new ArrayList<Point>();
		try {
			this.path = path;
			modifiedImg = ImageIO.read(new File(path));
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ImageEditor ie = new ImageEditor("test5.png");

//		// testing lines
//		Shape[] myShapes = new Shape[1];
//		myShapes[0] = new Rectangle(50,50,10,10);
//		String[] myStr = new String[1];
//		myStr[0] = "House of Potter";

		// ie.addText();
	}


	public void findContoursAndFill() throws Exception{
		// Matrix
		Mat src = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		// Grayscale the image
		// Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(src, src, new Size(3, 3));
		Mat blurred = new Mat();
		Mat thresh = new Mat();

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
			Point p = new Point((int) cX, (int) cY);
			contourCenters.add(p);
			Rect rect = Imgproc.boundingRect(c);
			Rectangle r = new Rectangle(rect.x, rect.y, rect.width, rect.height);
			contourBounds.add(r);

			Imgproc.drawContours(original, contours, -2, new Scalar(0, 0, 0));
			Imgproc.fillPoly(original, contours, new Scalar(255, 255, 255));
		}

		modifiedImg = Mat2BufferedImage(original); // convert the matrix to BufferedImage and work with this 
	}

	// Find the text and return the shapes the text are in
	public void findText() throws IOException, TesseractException{
		ITesseract it = new Tesseract();
		BufferedImage bi = ImageIO.read(new File(path));
		it.setLanguage("eng");

		for(int i = 0; i < contourBounds.size(); i++){
			String s = it.doOCR(bi, (Rectangle) contourBounds.get(i));
			words.add(s);
			System.out.println(s);
		}
	}

	// Draw the strings within the shapes onto the imgs
	public void addText(ArrayList<String> translated) {
		addText(translated, javax.swing.UIManager.getDefaults().getFont("Label.font"));
	}

	public void addText(ArrayList<String> translatedWords, Font tf) {
		Graphics2D graphic = modifiedImg.createGraphics();
		graphic.setColor(Color.BLACK);
		graphic.setFont(tf);
		for(int i = 0; i < boundingBoxes.size(); i++){
			Point location = contourCenters.get(i);
			graphic.drawString(translatedWords.get(i), (int)location.getX(), (int)location.getY() + contourBounds.get(i).getBounds().height);
		}
		// saveImgAsFile("jpg", "translated"); // only necessary if you want to save the new file now 
	}

	public void saveImgAsFile(String type, String extension){
		try {
			System.out.println("writing to file");
			ImageIO.write(modifiedImg, type, new File(path+extension+"."+type));
			System.out.println("Wrote to file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getWords(){
		return words;
	}

	static BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {        
		MatOfByte mob=new MatOfByte();
		Imgcodecs.imencode(".jpg", matrix, mob);
		byte ba[]=mob.toArray();

		BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
		return bi;
	}
}
