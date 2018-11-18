package main.java;

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
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.sourceforge.tess4j.*;

public class ImageRecognizer {

	private BufferedImage srcImg;
	private BufferedImage gsImg;
	private String path;
	private Mat srcGray = new Mat();
    private int threshold = 100;
	
	public ImageRecognizer(String path){
		try {
			this.path = path;
			BufferedImage img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		ImageRecognizer ie = new ImageRecognizer("46458735_326815428111627_254870742393421824_n.png");
		try {
			ie.findText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(ie.getImgText(ie.path));
	}

	
	
	public void findContours(){
		Mat src = Imgcodecs.imread("path/to/img", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		
        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(srcGray, srcGray, new Size(3, 3));
        Mat cannyOutput = new Mat();
        Imgproc.Canny(srcGray, cannyOutput, threshold, threshold * 2);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        
        
        
	}

	public Shape findBubbles(){
		
		
		return null;
	}
	
	// Find the text and return the shapes the text are in
	public Shape[] findText() throws IOException{

		ITesseract it = new Tesseract();
		it.setLanguage("eng");
		long start = System.currentTimeMillis();
		BufferedImage bufferedImage = ImageIO.read(new File(path));
		for (Word word : it.getWords(bufferedImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE)) {
			Rectangle boundingBox = word.getBoundingBox();
			System.out.println(""+boundingBox);		// Store these in an array
			System.out.println(""+ word);			// store in array
		}
		System.out.println("time = " + (System.currentTimeMillis() - start));

		return null;
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

}
