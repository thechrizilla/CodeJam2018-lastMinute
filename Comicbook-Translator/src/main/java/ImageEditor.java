
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageEditor {

    private BufferedImage img;

    public static void main(String[] args) {
        ImageEditor ie = new ImageEditor("src/Comicbook-Translator/calvin_and_hobbes_4.jpg");
        
        // testing lines
        Shape[] myShapes = new Shape[1];
        myShapes[0] = new Rectangle(50,50,10,10);
        String[] myStr = new String[1];
        myStr[0] = "House of Potter";
        ie.addText(myStr, myShapes);
    }

    public ImageEditor(String path) {
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Find the text and return the shapes the text are in
    public Shape[] findText() {

        return null;
    }

    // Remove the text on the imgs by putting shapes over the text
    public void removeText(Shape[] shapes) {
        Graphics2D graphic = img.createGraphics();
        graphic.setColor(Color.WHITE);
        for (Shape shape : shapes) {
            graphic.fill(shape);
        }
        saveImgAsFile(); // only necessary if you want to save the new file now 
    }

    // Draw the strings within the shapes onto the imgs
    public void addText(String[] strings, Shape[] shapes) {
        addText(strings, shapes, javax.swing.UIManager.getDefaults().getFont("Label.font"));
    }
    
    public void addText(String[] strings, Shape[] shapes, Font tf) {
        Graphics2D graphic = img.createGraphics();
        graphic.setColor(Color.BLACK);
        graphic.setFont(tf);
        for(int i = 0; i < shapes.length; i++){
            Point location = shapes[i].getBounds().getLocation();
            graphic.drawString(strings[i], (int)location.getX(), (int)location.getY());
        }
        saveImgAsFile(); // only necessary if you want to save the new file now 
    }
    
    private void saveImgAsFile(){
        try {
            ImageIO.write(img, "jpg", new File("src/Comicbook-Translator/saved.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
