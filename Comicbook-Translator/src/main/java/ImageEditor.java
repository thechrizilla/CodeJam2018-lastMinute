package main.java;

import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageEditor {

	private BufferedImage img;
	
	public static void main(String[] args){
		ImageEditor ie = new ImageEditor("calvin_and_hobbes_4.jpg");
	}
	
	public ImageEditor(String path){
		try {
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
	
	// Remove the text on the imgs by putting shapes over the text
	public void removeText(Shape[] shapes){
		
	}
	
	// Draw the strings within the shapes onto the imgs
	public void addText(String[] strings, Shape[] shapes){
		
	}
	
}
