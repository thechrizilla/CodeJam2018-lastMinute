package main.java;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
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

import org.opencv.core.Core;
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

	private String path;
	private BufferedImage modifiedImg;

	private ArrayList<String> words;
	private ArrayList<Shape> contourBounds;
	private ArrayList<Point> contourCenters;
	private ArrayList<Shape> textBounds;

	public ImageEditor(String path) {
		words = new ArrayList<String>();
		contourBounds = new ArrayList<Shape>();
		contourCenters = new ArrayList<Point>();
		textBounds = new ArrayList<Shape>();
		try {
			this.path = path;
			modifiedImg = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// For testing
	public static void main(String[] args) throws Exception 
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		translatePage("test2.png", "fr");
	}
	

	public void findContoursAndFill() throws Exception{
		// Import image
		Mat src = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		
		// Pre process image for contour recognition. Blur to remove noise then threshold
		Imgproc.blur(src, src, new Size(3, 3));
		Mat blurred = new Mat();
		Mat thresh = new Mat();
		Imgproc.GaussianBlur(src, blurred, new Size(5,5), 0.0);
		Imgproc.threshold(blurred, thresh, 250, 255, Imgproc.THRESH_BINARY);

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		Mat original = Imgcodecs.imread(path, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

		// Handle contours
		for(MatOfPoint c : contours){
			// Obtain the center point of the contour using moments
			Moments m = Imgproc.moments(c);
			double cX = m.m10 / m.m00;
			double cY = m.m01 / m.m00;
			Point p = new Point((int) cX, (int) cY);
			contourCenters.add(p);
			
			// Obtain the bounding rectangle of the contour
			Rect rect = Imgproc.boundingRect(c);
			Rectangle r = new Rectangle(rect.x, rect.y, rect.width, rect.height);
			contourBounds.add(r);

			// Fill the contours on top of the image
			Imgproc.drawContours(original, contours, -2, new Scalar(0, 0, 0));
			Imgproc.fillPoly(original, contours, new Scalar(255, 255, 255));
		}

		modifiedImg = Mat2BufferedImage(original); // convert the matrix to BufferedImage and work with this 
	}

	// Find the text inside the contourBounds
	// We can apply some NLP here to avoid adding invalid strings that may be obtained by the OCR when reading the image
	public void findText() throws IOException, TesseractException{
		ITesseract it = new Tesseract();
		BufferedImage bi = ImageIO.read(new File(path));
		it.setLanguage("eng");

		for(int i = 0; i < contourBounds.size(); i++){
			String s = it.doOCR(bi, (Rectangle) contourBounds.get(i));
			if (s.trim().length() > 0) {
				s = s.replaceAll("\n", " ");
				textBounds.add(contourBounds.get(i));
				words.add(s);
				System.out.println(s);
			}
		}
	}

	// Draw the strings within the shapes onto the imgs
	public void addText(ArrayList<String> translated) {
		addText(translated, javax.swing.UIManager.getDefaults().getFont("Label.font"));
	}

	public void addText(ArrayList<String> translatedWords, Font tf) {
		Graphics2D graphic = modifiedImg.createGraphics();
		graphic.setColor(Color.RED);
		graphic.setFont(tf);
		for(int i = 0; i < textBounds.size(); i++){
			// Point location = contourCenters.get(i);
			drawCenteredString(graphic, translatedWords.get(i), (Rectangle) textBounds.get(i), tf);
			// graphic.drawString(translatedWords.get(i), (int)location.getX(), (int)location.getY());
		}
		// saveImgAsFile("jpg", "translated"); // only necessary if you want to save the new file now 
	}
	
	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}

	// Write the image out
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

	// Convert the image matrix into a BufferedImage 
	static BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {        
		MatOfByte mob=new MatOfByte();
		Imgcodecs.imencode(".jpg", matrix, mob);
		byte ba[]=mob.toArray();

		BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
		return bi;
	}
	
	// Main function to translate the page
	public static void translatePage(String path, String destLang){
		ImageEditor ie = new ImageEditor(path);

		try {
			ie.findContoursAndFill();
			ie.findText();
//			ArrayList<String> translated = Translator.translateAll(ie.getWords(), "en", destLang);
			ie.addText(ie.getWords());
			ie.saveImgAsFile("png", "-translated");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
