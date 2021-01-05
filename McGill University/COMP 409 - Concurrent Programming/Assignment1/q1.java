package main;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.LinkedList;
import java.util.concurrent.*;

public class q1 implements Runnable {

    // Parameters
    public static int n = 1;
    public static int width;
    public static int height;
    public static int k;
    public static BufferedImage outputimage;
    public static LinkedList<Rectangle> activeRectangles;

    public void run() {
    	int x1, y1, x2, y2, argb;
    	boolean loop;
    	Rectangle rect;
    	activeRectangles = new LinkedList<Rectangle>();
    	
    	//Loop until a return is hit
    	while (true) {
    		do {
        		//Random Rectangle coordinates NOTE: TOP LEFT IS (0, 0)
        		x1 = ThreadLocalRandom.current().nextInt(0, width);
        		y1 = ThreadLocalRandom.current().nextInt(0, height);
        		x2 = ThreadLocalRandom.current().nextInt(0, width);
        		y2 = ThreadLocalRandom.current().nextInt(0, height);
        		
        		//Asserting that x1 is the leftmost x value and y1 is the (visually, technically it's the lower value) topmost y value
        		if (x1 > x2) {
        			int temp = x1;
        			x1 = x2;
        			x2 = temp;
        		}
        		if (y1 > y2) {
        			int temp = y1;
        			y1 = y2;
        			y2 = temp;
        		}
        		
            	//Acquire random argb value and assert alpha = 0xff
            	argb = ThreadLocalRandom.current().nextInt();
            	argb = argb | 0xff000000;
            	
            	//Store the data in an organized fashion
            	rect = new Rectangle(x1, y1, x2, y2);
            	
            	//Synchronized lock on the k and activeRectangles shared variables.
            	synchronized (this) {
            		//If k (the number of rectangles to be drawn) is 0 then return, thus terminating the thread
            		if (k <= 0) return;
            		//Check if the rectangle can be drawn without colliding with other rectangles currently being drawn.
            		if (isValid(rect)) {
            			//the loop variable gets us out of the do-while loop for checking if our random variables represent a rectangle that is valid.
            			loop = false;
            			
            			//decrement the number of rectangles left to draw and add the current rectangle to the list of rectangles being currently drawn.
            			k--;
            			activeRectangles.add(rect);
            		}
            		else loop = true;
            	}
    		} while(loop);
        	drawRectangle(rect, argb);
        	synchronized (this) {
        		activeRectangles.remove(rect);
        	}
    	}
    }
    
    //Check to see if any of the active (in progress) rectangles overlap with the passed in rectangle
    public boolean isValid(Rectangle rect) {
    	for(Rectangle active : activeRectangles) {
    		if (active.isColliding(rect))
    			return false;
    	}
    	return true;
    }

    //Draw the rectangle as per the instructions of the assignment
    public void drawRectangle(Rectangle rect, int argb) {
    	int black = 0xff000000;
    	for (int i = rect.x1; i <= rect.x2; i++) {
   			outputimage.setRGB(i, rect.y1, black);
   			outputimage.setRGB(i, rect.y2, black);
    	}
    	for (int j = rect.y1; j <= rect.y2; j++) {
    		outputimage.setRGB(rect.x1, j, black);
    		outputimage.setRGB(rect.x2, j, black);
    	}
    	for (int i = rect.x1 + 1; i < rect.x2; i++) {
    		for (int j = rect.y1 + 1; j < rect.y2; j++) {
    	    	outputimage.setRGB(i, j, argb);
    		}
    	}
    }
    
    public static void main(String[] args) {
        try {
        	// Exit program if the incorrect number of input arguments are given.
        	if (args.length != 4) {
        		System.out.println("Please provide the arguments: width height number_of_threads number_of_rectangles");
        		return;
        	}
        	
        	width =	Integer.parseInt(args[0]);
        	height =Integer.parseInt(args[1]);
        	n =		Integer.parseInt(args[2]);
        	k =		Integer.parseInt(args[3]);
             // once we know what size we want we can create an empty image
            outputimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // ------------------------------------
            // Your code would go here
            // The easiest mechanisms for getting and setting pixels are the
            // BufferedImage.setRGB(x,y,value) and getRGB(x,y) functions.
            // Note that setRGB is synchronized (on the BufferedImage object).
            // Consult the javadocs for other methods.

            // The getRGB/setRGB functions return/expect the pixel value in ARGB format, one byte per channel.  For example,
            //  int p = img.getRGB(x,y);
            // With the 32-bit pixel value you can extract individual colour channels by shifting and masking:
            //  int red = ((p>>16)&0xff);
            //  int green = ((p>>8)&0xff);
            //  int blue = (p&0xff);
            // If you want the alpha channel value it's stored in the uppermost 8 bits of the 32-bit pixel value
            //  int alpha = ((p>>24)&0xff);
            // Note that an alpha of 0 is transparent, and an alpha of 0xff is fully opaque.
            // ------------------------------------
 
            //Setup all n threads
            q1 runnable = new q1();
            Thread threads[] = new Thread[n];
            for (int i = 0; i < n; i++) {
            	threads[i] = new Thread(runnable);
            }
            
            long t0 = System.currentTimeMillis();
            
            //Start all n threads
            for (int i = 0; i < n; i++) {
            	threads[i].start();
            }
            
            //Join all n threads
            for (int i = 0; i < n; i++) {
            	threads[i].join();
            }
            
            long t1 = System.currentTimeMillis();
            System.out.println("Time Taken (writing to disk not included): " + (t1 - t0));

            // Write out the image
            File outputfile = new File("outputimage.png");
            ImageIO.write(outputimage, "png", outputfile);

        }
        catch (NumberFormatException e) {
        	System.out.println("Please ensure that all arguments are in integer format.");
        	System.out.println("Error " + e);
        	e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("ERROR " + e);
            e.printStackTrace();
        }
    }
}

//Class to store rectangle information and to check for axis aligned overlaps.
class Rectangle {
	int x1, y1, x2, y2;
	
	public Rectangle(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	//Simple check for axis aligned collisions
	public boolean isColliding(Rectangle other) {
		if (this.x1 < other.x2 && this.x2 > other.x1 && this.y1 < other.y2 && this.y2 > other.y1)
			return true;
		return false;
	}
}