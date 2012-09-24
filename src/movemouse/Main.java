package movemouse;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Robot;
import java.io.*;
import java.util.*;
import com.sun.image.codec.jpeg.*;
import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.lang.Math;
import javax.imageio.*;
import java.awt.geom.AffineTransform;

public class Main {


    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) throws Exception {
        Robot rob = new Robot();
        rob.setAutoDelay(50); // 0.5s
        Toolkit tk = Toolkit.getDefaultToolkit ();
        Dimension dim=tk.getScreenSize();
        int centerx=(int)(dim.getWidth()/2);
        int centery=(int)(dim.getHeight()/2);
        int count=1;
        boolean exit=false;
        ArrayList minLoc=new ArrayList();
        ArrayList min=new ArrayList();

        rob.mouseMove(centerx, centery);
        int locationx=0;
        int locationy=0;
        int locationgrtstx=1;
        int locationgrtsty=1;
        NewJWebCam.beluga();
        
   }

        public static void onImageCapture(BufferedImage bimg) throws Exception{
            Scanner reader=new Scanner(System.in);
            int locationx;
            int locationy;
            int locationgrtstx;
            int locationgrtsty;
            Robot rob = new Robot();
            rob.setAutoDelay(100);
            double g=0;
            Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
            int centerx=(int)(640/2);
            int centery=(int)(480/2);
            locationgrtstx=centerx;
            locationgrtsty=centery;

            ArrayList<Integer[]> minLoc=new ArrayList();
            ArrayList min=new ArrayList();
            for(int x=1;x<bimg.getWidth()-1;x++){
                for(int y=1;y<bimg.getHeight()-1;y++){
                    //System.out.println(f(x,y,bimg)+", "+f(locationgrtstx,locationgrtsty,bimg));
                    if((Math.abs(f(x,y,bimg)))>Math.abs(f(locationgrtstx,locationgrtsty,bimg))){
                        locationgrtstx=x;
                        locationgrtsty=y;
                    }
                }
            }
            locationx=(int)(locationgrtstx*(dim.getWidth()/640));
            locationy=(int)(locationgrtsty*(dim.getHeight()/480));
            System.out.println("Max: "+locationx+", "+locationy);
            double minPartDeriv=Math.pow(10, -2.4066);
            if(reader.nextLine().equals("Stop")){
                
            }
//            for(int x=1;x<bimg.getWidth()-1;x++){
//                for(int y=1;y<bimg.getHeight()-1;y++){
//                    g=g(x,y,bimg);
//                    if(Math.abs(g)<minPartDeriv&&g!=0){
//                        Integer[] loc={x,y};
//                        minLoc.add(loc);
//                        min.add(new Integer((int)f(x,y,bimg)));
//                        System.out.println("added new min"+g(x,y,bimg));
//                    }
//                }
//            }

                if(minLoc.size()==1){
                    Integer[] locationBigInt=minLoc.get(0);
                    int[] location={(int)locationBigInt[0],(int)locationBigInt[1]};
                    rob.mouseMove(location[0],location[1]);
                    System.out.println("Location: "+location[0]+","+location[1]);
                }
                else if(minLoc.size()==2){
                    rob.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    rob.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                else if(minLoc.size()==3){
                    rob.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    rob.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }

        }
        public static int[] getRGBArray(int aRGB){
            
            long argb = (long)aRGB;
            int red   = (int)(argb % 0x1000000 / 0x10000);  
            int green = (int)(argb % 0x10000 / 0x100);  
            int blue  = (int)(argb % 0x100);
            int[] RGBArray={red,green,blue};
            return RGBArray;
        }
        
        public static double f(int x,int y,BufferedImage bimg){
            //System.out.println("x: "+x+", y: "+y);
            int[] RGB=getRGBArray(bimg.getRGB(x, y));
            float[] beluga=Color.RGBtoHSB(RGB[0],RGB[1],RGB[2],null);
            return beluga[2];
        }

        public static double g(int x, int y,BufferedImage bimg){
            return f(x,y,bimg)-f(x-1,y,bimg);
        }




}