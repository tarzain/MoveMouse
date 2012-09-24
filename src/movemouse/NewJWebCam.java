/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package movemouse;
/*
 * Created on May 25, 2005
 */
import com.lti.civil.*;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.List;

import com.lti.civil.CaptureDeviceInfo;
import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver;
import com.lti.civil.CaptureStream;
import com.lti.civil.CaptureSystem;
import com.lti.civil.CaptureSystemFactory;
import com.lti.civil.DefaultCaptureSystemFactorySingleton;
import com.lti.civil.Image;
import com.lti.civil.VideoFormat;
import com.lti.civil.awt.AWTImageConverter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Robot;
import java.io.*;
import java.util.*;
import com.sun.image.codec.jpeg.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import java.lang.Math;
import javax.imageio.*;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Ken Larson
 */
public class NewJWebCam
{
        public CaptureStream captureStream;
	public static void beluga() throws Exception
	{
                Robot rob = new Robot();
                rob.setAutoDelay(50); // 0.5s
                int centerx=0;
                int centery=0;
                rob.mouseMove(centerx, centery);
                int locationx=0;
                int locationy=0;
                CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance();
		CaptureSystem system = factory.createCaptureSystem();
		system.init();
		List list = system.getCaptureDeviceInfoList();
		for (int i = 0; i < list.size(); ++i)
		{
			CaptureDeviceInfo info = (CaptureDeviceInfo) list.get(i);

			System.out.println("Device ID " + i + ": " + info.getDeviceID());
			System.out.println("Description " + i + ": " + info.getDescription());


			CaptureStream captureStream = system.openCaptureDeviceStream(info.getDeviceID());

			System.out.println("Current format " + videoFormatToString(captureStream.getVideoFormat()));

			captureStream.setObserver(new MyCaptureObserver());
			System.out.println("Available formats:");
			for (VideoFormat format : captureStream.enumVideoFormats())
			{
				System.out.println(" " + videoFormatToString(format));
			}

			final int MAX_FORMATS = 2;
			int count=0;
			for (VideoFormat format : captureStream.enumVideoFormats())
			{
				if (count > MAX_FORMATS)
				{	System.out.println("Stopping after " + MAX_FORMATS + " formats.");	// could be a lot
					break;
				}
				System.out.println("Choosing format: " + videoFormatToString(format));
				captureStream.setVideoFormat(format);

				System.out.println("Capturing for 2 seconds...");
				captureStream.start();
				for(int b=0;b<Math.pow(10, 10); b++){
                                    //System.out.println("b:"+b);
                                }
                                captureStream.stop();
                                System.out.println("Capture stopped");
                                System.out.println("beluga whale");
                                count++;
			}
			System.out.println("disposing stream...");

			captureStream.dispose();

		}
		System.out.println("disposing system...");
		system.dispose();
		System.out.println("done.");


	}

	public static String videoFormatToString(VideoFormat f)
	{
		return "Type=" + formatTypeToString(f.getFormatType()) + " Width=" + f.getWidth() + " Height=" + f.getHeight() + " FPS=" + f.getFPS();
	}

	private static String formatTypeToString(int f)
	{
		switch (f)
		{
			case VideoFormat.RGB24:
				return "RGB24";
			case VideoFormat.RGB32:
				return "RGB32";
			default:
				return "" + f + " (unknown)";
		}
	}

}

class MyCaptureObserver implements CaptureObserver
{
	public void onError(CaptureStream sender, CaptureException e)
	{	System.err.println("onError " + sender);
		e.printStackTrace();
	}


	public void onNewImage(CaptureStream sender, Image image)
	{
		final BufferedImage bimg;
                //System.out.println("Jahousafats");
		try
		{
			final VideoFormat format = image.getFormat();
			System.out.println("onNewImage format=" + NewJWebCam.videoFormatToString(format) + " length=" + image.getBytes().length);
			bimg = AWTImageConverter.toBufferedImage(image);
                        Main.onImageCapture(bimg);
		}
		catch (Exception e)
		{	e.printStackTrace();
			return;
		}

		 //Encode as a JPEG
		try
		{
			FileOutputStream fos = new FileOutputStream("out.jpg");
			JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(fos);
			jpeg.encode(bimg);
			fos.close();
                        System.out.println("done outputting the file jaunt");
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}

}
