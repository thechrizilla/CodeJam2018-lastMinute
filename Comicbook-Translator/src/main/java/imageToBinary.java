package main.java;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.*;
import javax.imageio.ImageIO;

public class imageToBinary {

	public static BufferedImage returnImage(String path){
		File originalImage = new File (path);

		BufferedImage img = null;
		BufferedImage grayscaleImage = null;
		try {
			img = ImageIO.read(originalImage);

			//Image for grayscalling
			grayscaleImage = new BufferedImage(
					img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

			for (int i=0;i<img.getWidth();i++) {
				for (int j=0;j<img.getHeight();j++) {
					//Get RGB Color on each pixel
					Color c = new Color(img.getRGB(i,j));
					int r = c.getRed();
					int g = c.getGreen();
					int b = c.getBlue();
					int a = c.getAlpha();

					// Simple graysacaling = (r+g+b/ 3
					int gr = 0;
					if(r < 245 && g < 245 && b < 245) 
						gr = 0;
					else 
						gr = ((r+g+b)/3);

					//Create graycolor
					Color gColor = new Color (gr, gr, gr, a);
					grayscaleImage.setRGB(i, j, gColor.getRGB());
				}
			}
			// ImageIO.write(grayscaleImage, "png", new File("itmighthaveworked.png"));

		} catch (IOException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grayscaleImage;

	}

	public static void main(String[] args) {
		returnImage("");
	}
}
