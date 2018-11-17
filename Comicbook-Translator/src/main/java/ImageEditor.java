
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class ImageEditor {

    private BufferedImage img;

    public static void main(String[] args) {
        ImageEditor ie = new ImageEditor("calvin_and_hobbes_4.jpg");
    }

    public ImageEditor(String path) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
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
        for (Shape shape : shapes) {
            graphic.draw(shape);
        }
        
//        File outputfile = new File("saved.jpg");
//        try {
//            ImageIO.write(img, "jpg", outputfile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // Draw the strings within the shapes onto the imgs
    public void addText(String[] strings, Shape[] shapes) {
        Graphics2D graphic = img.createGraphics();
        for(int i = 0; i < shapes.length; i++){
            Point location = shapes[i].getBounds().getLocation();
            graphic.drawString(strings[i], (int)location.getX(), (int)location.getY());
        }
    }
}
