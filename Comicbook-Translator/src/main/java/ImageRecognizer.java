package main.java;

import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.*;

public class ImageRecognizer {

	private BufferedImage img;
	private String path;

	public static void main(String[] args){
		ImageRecognizer ie = new ImageRecognizer("fa834c8512816c23dc35f3e327faa132.jpg");
		System.out.println(ie.getImgText(ie.path));
		
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

	// Find the text and return the shapes the text are in
	public Shape[] findText(){

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
