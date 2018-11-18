package main.java;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.*;

public class ImageRecognizer {

	private BufferedImage img;
	private String path;

	public static void main(String[] args){
		ImageRecognizer ie = new ImageRecognizer("46366153_737517113295458_5495548622466449408_n.jpg");
		try {
			ie.findText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(ie.getImgText(ie.path));

	}

	public ImageRecognizer(String path){
		try {
			this.path = path;
			BufferedImage img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			System.out.println(""+boundingBox);
			System.out.println(""+ word);
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
