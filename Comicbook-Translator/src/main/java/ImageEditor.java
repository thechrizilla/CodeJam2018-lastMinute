package main.java;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

public class ImageEditor {

	private BufferedImage img;
	private String path;

	private BufferedImage srcImg;
	private BufferedImage gsImg;

	private ArrayList<Shape> bubbles;
	// private ArrayList<String> words;
	// private ArrayList<Shape> boundingBoxes;

	private ArrayList<Shape> boundingBoxes;
	private ArrayList<String> words;

	public ImageEditor(String path) {
		boundingBoxes = new ArrayList<Shape>();
		words = new ArrayList<String>();
		try {
			this.path = path;
			srcImg = ImageIO.read(new File(path));
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ImageEditor ie = new ImageEditor("src/Comicbook-Translator/calvin_and_hobbes_4.jpg");

		// testing lines
		Shape[] myShapes = new Shape[1];
		myShapes[0] = new Rectangle(50,50,10,10);
		String[] myStr = new String[1];
		myStr[0] = "House of Potter";
		
		// ie.addText();
	}

	// Find the text and return the shapes the text are in
	public void findText() throws IOException{
		ITesseract it = new Tesseract();
		BufferedImage bufferedImage = ImageIO.read(new File(path));
		it.setLanguage("eng");

		// long start = System.currentTimeMillis();

		for (Word word : it.getWords(bufferedImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE)) {
			Rectangle boundingBox = word.getBoundingBox();
			System.out.println("" + boundingBox);	
			boundingBoxes.add(boundingBox);
			System.out.println("Boxes size: " + boundingBoxes.size());
			System.out.println("" + word);
			words.add("" + word.toString());
		}
		System.out.println("boxes final size: " + boundingBoxes.size());
		// System.out.println("time = " + (System.currentTimeMillis() - start));
	}

	// Remove the text on the imgs by putting shapes over the text
	public void removeText() {
		System.out.println("Boxes size: " + boundingBoxes.size());
		Graphics2D graphic = img.createGraphics();
		graphic.setColor(Color.WHITE);
		for (Shape shape : boundingBoxes) {
			graphic.fill(shape);
		}
		// saveImgAsFile("jpg", "translated"); // only necessary if you want to save the new file now 
	}

	// Draw the strings within the shapes onto the imgs
	public void addText(ArrayList<String> translated) {
		addText(translated, javax.swing.UIManager.getDefaults().getFont("Label.font"));
	}

	public void addText(ArrayList<String> translatedWords, Font tf) {
		System.out.println("print rraylist");
		
		Graphics2D graphic = img.createGraphics();
		graphic.setColor(Color.BLACK);
		graphic.setFont(tf);
		for(int i = 0; i < boundingBoxes.size(); i++){
			Point location = boundingBoxes.get(i).getBounds().getLocation();
			graphic.drawString(translatedWords.get(i), (int)location.getX(), (int)location.getY()+ boundingBoxes.get(i).getBounds().height);
		}
		// saveImgAsFile("jpg", "translated"); // only necessary if you want to save the new file now 
	}

	public void saveImgAsFile(String type, String extension){
		try {
			System.out.println("writing to file");
			ImageIO.write(img, type, new File(path+extension+"."+type));
			System.out.println("Wrote to file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getWords(){
		return words;
	}
}
