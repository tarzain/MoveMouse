/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movemouse;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Robot;
import java.io.*;
import javax.media.*;
import javax.media.datasink.*;
import javax.media.format.*;
import javax.media.protocol.*;
import javax.media.util.*;
import javax.media.control.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import com.sun.image.codec.jpeg.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.lang.Math;
import javax.imageio.*;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Yahya
 */
public class MainOld {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) throws java.awt.AWTException {
        Robot rob = new Robot();
        rob.setAutoDelay(50); // 0.5s
        Toolkit tk = Toolkit.getDefaultToolkit ();
        Dimension dim=tk.getScreenSize();
        int centerx=(int)(dim.getWidth()/2);
        int centery=(int)(dim.getHeight()/2);
        int count=1;
        boolean exit=false;

        rob.mouseMove(centerx, centery);
        int locationx=0;
        int locationy=0;
        int locationgrtstx=1;
        int locationgrtsty=1;
        
            try
            {
            JWebCam2 myWebCam = new JWebCam2 ( "Web Cam Capture" );
            myWebCam.setVisible ( true );

            if ( !myWebCam.initialise() )
            {
                System.out.println ("Web Cam not detected / initialised");
            }

            else{
            Thread.sleep(10000);
            for(count=1;count<2000;count++){
            String im2str=myWebCam.grabFrameImage().toString();
            Buffer buf=myWebCam.grabFrameBuffer();
            Image im= myWebCam.grabFrameImage();
            BufferedImage imb=toBufferedImage(im);
//                System.out.println("The First Image has been taken");
            
//                System.out.println("The Second Image is about to be taken");
            Thread.sleep(5); //I changed the time in ms from 1 to 5
            Image im2=myWebCam.grabFrameImage();
            BufferedImage imb2=toBufferedImage(im2);
//                System.out.println("The Second Image has been taken and is about to be differentiated");
            BufferedImage imbd=subtract(imb2,imb);
//            for(int g=1; g<imbd.getWidth();g++){
//                for(int h=1;h<imbd.getHeight();h++){
//                    if(imbd.getRGB(g,h)!=-16777216){
//                        rob.mouseMove(g+400,h+100);
//                        exit=true;
//                        break;
//                    }
//                if(exit)break;
//                }
//                if(exit)break;
//            }
            
            
            //beginning of the new part

            for(int g=1; g<imbd.getWidth();g++){
                for(int h=1;h<imbd.getHeight();h++){
                    if(imbd.getRGB(g,h)!=-16777216){
                        if(imbd.getRGB(g, h)>imbd.getRGB(locationgrtstx,locationgrtsty)){
                            //I just added this distance part for clicking experimentally
                            //if(Math.pow(g-locationgrtstx),2))
                            locationgrtstx=g;
                            locationgrtsty=h;
                        }
                        
                        if(g==639&&h==478)
                        {
                            exit=true;
                            break;
                        }
                        
                    }
                if(exit)break;
                }
                if(exit)break;
            }
            //The following line is that which scales the input for the entire screen and must be customized for different monitor sizes/resolutions
            //rob.mouseMove((int)((locationgrtstx-5)*2.134),(int)((locationgrtsty+30)*1.6));
            //The preceding line is that which scales the input for the entire screen and must be customized for different monitor sizes/resolutions
            rob.mouseMove(locationgrtstx, locationgrtsty);
            rob.keyPress(KeyEvent.VK_CONTROL);
            rob.keyRelease(KeyEvent.VK_CONTROL);


            //end of the new part

            exit=false;
            if(count==1){
                    try {
                    File outputfile = new File("C:\\Users\\Yahya\\Desktop\\saved"+count+".png");
                    ImageIO.write(imbd, "png", outputfile);
                        System.out.println("Saved");
                    }
                    catch (IOException e) {
                    System.out.println(""+e);
                    }
                    }
                }



            }

            
            }
            catch ( Exception ex )
                {
                    ex.printStackTrace();
                }
        
        
        }

   public static BufferedImage subtract(BufferedImage imb, BufferedImage imb2){
       int width=imb.getWidth();
       int height=imb2.getHeight();
       int k=0;
       int locationx=0;
       int locationy=0;
       int lastk=0;
       double kd=0.0;
       boolean criticalLoc=true;
       BufferedImage imbd=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
       for(int i=1; i<width; i++){
           for(int j=1; j<height; j++){
               k=Math.abs(imb.getRGB(i, j))-(imb2.getRGB(i, j));
               imbd.setRGB(i, j, k);
           }
       }
       return imbd;
   }

   public static BufferedImage flip(BufferedImage imb){
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-imb.getHeight(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        imb= op.filter(imb, null);
        return imb;

   }






   public static BufferedImage toBufferedImage(Image image) {
    if (image instanceof BufferedImage) {
        return (BufferedImage)image;
    }

    // This code ensures that all the pixels in the image are loaded
    image = new ImageIcon(image).getImage();

    // Determine if the image has transparent pixels; for this method's
    // implementation, see Determining If an Image Has Transparent Pixels
    boolean hasAlpha = hasAlpha(image);

    // Create a buffered image with a format that's compatible with the screen
    BufferedImage bimage = null;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    try {
        // Determine the type of transparency of the new buffered image
        int transparency = Transparency.OPAQUE;
        if (hasAlpha) {
            transparency = Transparency.BITMASK;
        }

        // Create the buffered image
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        bimage = gc.createCompatibleImage(
            image.getWidth(null), image.getHeight(null), transparency);
    } catch (HeadlessException e) {
        // The system does not have a screen
    }

    if (bimage == null) {
        // Create a buffered image using the default color model
        int type = BufferedImage.TYPE_INT_RGB;
        if (hasAlpha) {
            type = BufferedImage.TYPE_INT_ARGB;
        }
        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
    }

    // Copy image to buffered image
    Graphics g = bimage.createGraphics();

    // Paint the image onto the buffered image
    g.drawImage(image, 0, 0, null);
    g.dispose();

    return bimage;
}

// This method returns true if the specified image has transparent pixels
public static boolean hasAlpha(Image image) {
    // If buffered image, the color model is readily available
    if (image instanceof BufferedImage) {
        BufferedImage bimage = (BufferedImage)image;
        return bimage.getColorModel().hasAlpha();
    }

    // Use a pixel grabber to retrieve the image's color model;
    // grabbing a single pixel is usually sufficient
     PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
    try {
        pg.grabPixels();
    } catch (InterruptedException e) {
    }

    // Get the image's color model
    ColorModel cm = pg.getColorModel();
    return cm.hasAlpha();
}




}
