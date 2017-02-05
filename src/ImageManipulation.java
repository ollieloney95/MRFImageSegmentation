import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageManipulation {
	
	public static void main(String[] args){
		int[][] im1 = readImage(new File("C:\\Users\\ollie\\Desktop\\labels.png"));
		int[][] im2 = readImage(new File("C:\\Users\\ollie\\Desktop\\recon.png"));
		int[][] output = whereDifferent(im1,im2);
		writeImage(convertToImage(output),new File("C:\\Users\\ollie\\Desktop"),"difference");
	}
	
	public static int[][] RGBtoIntensity(int[][] image){
		int[][] intensityImage = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				Color col = new Color(image[r][c]);
				int red = col.getRed();
				int blue = col.getBlue();
				int green = col.getGreen();
				intensityImage[r][c] = (int)(0.2989f*red+0.5870f*green+0.1140f*blue);
			}
		}
		return(intensityImage); 
	}
	public static int[][] IntensityToRGB(int[][] image){
		int[][] RGBimage = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
				int intensity = image[r][c];
				if(intensity<0){intensity=0;}
				if(intensity>255){intensity=255;}
				Color col = new Color(intensity,intensity,intensity);
				RGBimage[r][c] = col.getRGB();
		}}
		return(RGBimage); 
	}
	
	public static int[][] copyImage(int[][] image){
    	int[][] originalImage = new int[image.length][image[0].length];
    	for(int r=0;r<image.length;r++){
    		for(int c=0;c<image[0].length;c++){
    			originalImage[r][c] = image[r][c];
        	}	
    	}
    	return(originalImage);
    }
	public static BufferedImage convertToImage(int[][] im){
		BufferedImage output = null;
		output = new BufferedImage(im[0].length, im.length, BufferedImage.TYPE_INT_RGB);
		for (int row = 0; row < im[0].length; row++) {
	         for (int col = 0; col < im.length; col++) {
	            output.setRGB(row, col, im[col][row]);
	         }
	    }
		return output;
	}
	public static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
	      int width = image.getWidth();
	      int height = image.getHeight();
	      int[][] result = new int[height][width];

	      for (int row = 0; row < height; row++) {
	         for (int col = 0; col < width; col++) {
	            result[row][col] = image.getRGB(col, row);
	         }
	      }

	      return result;
	   }
	public static void randomiseBinaryImage(int[][] image){
		for(int i = 0; i<image.length;i++){
			for(int j = 0;j<image[0].length;j++){
				if(Math.random()<0.5){image[i][j]=1;}
			}
		}
	}
	public static int[][] randomiseBinaryIm(int[][] image){
		int[][] im=copyImage(image);
		for(int i = 0; i<image.length;i++){
			for(int j = 0;j<image[0].length;j++){
				if(Math.random()<0.5){im[i][j]=1;}
			}
		}
		return(im);
	}
	public static int[][] correctBinaryColor(int[][] image){
		int[][] outputImage = image;
		for(int r = 0; r<image.length;r++){
			for(int c = 0;c<image[0].length;c++){
				if(outputImage[r][c]==0){outputImage[r][c] =16777215;}
				if(outputImage[r][c]==1){outputImage[r][c] =0;}
			}
		}
		return outputImage;
	}
	public static void writeImage(BufferedImage im,File outputFolder,String name){
		File f = new File(outputFolder+"\\"+name+".png");
		try{
			if(im!=null){
				ImageIO.write(im, "png", f);
			}
		}catch (IOException e) {}
	}
	public static void writeImageList(BufferedImage[] imList,File outputFolder,String baseName){
		for(int i=0;i<imList.length;i++){
			File f = new File(outputFolder+"\\"+baseName+"00"+i+".png");
			if(i<10){
				f = new File(outputFolder+"\\"+baseName+"00"+i+".png");
			}else{
				if(i<100){
					f = new File(outputFolder+"\\"+baseName+"0"+i+".png");
				}else{
					f = new File(outputFolder+"\\"+baseName+""+i+".png");
				}
			}
			try{
				if(imList[i]!=null){
					ImageIO.write(imList[i], "png", f);
				}
			}catch (IOException e) {}
		}
	}
	public static int[][] correctImageLabelsAv(int[][] image,int[][] imageLabels){
		int[][] outputIm = new int[image.length][image[0].length];
		ArrayList<Integer> segments = new ArrayList<>();
		// create and calculate number of segments////////////////////////////////////////////////////////
		int NumberOfSegments;
		for(int r = 0; r < imageLabels.length;r++){for(int c = 0; c < imageLabels[0].length;c++){
			if(segments.contains(imageLabels[r][c]-1) == false){
				segments.add(imageLabels[r][c]-1);
			}
		}}
		NumberOfSegments = segments.size();
		int[] sample = getSegmentMeanColors(image,imageLabels);
		for(int r=0;r<imageLabels.length;r++){
			for(int c=0;c<imageLabels[0].length;c++){
				outputIm[r][c] = sample[imageLabels[r][c]-1];
			}
		}
		//try{
			//ImageIO.write(ImageManipulation.convertToImage(outputIm), "jpg", f);
		//}catch (IOException e) {}
		return outputIm;
	}
	public static int[][] displayImageLabelsAv(int[][] image,int[][] imageLabels, File f){
		int[][] outputIm = new int[image.length][image[0].length];
		ArrayList<Integer> segments = new ArrayList<>();
		// create and calculate number of segments////////////////////////////////////////////////////////
		int NumberOfSegments;
		for(int r = 0; r < imageLabels.length;r++){for(int c = 0; c < imageLabels[0].length;c++){
			if(segments.contains(imageLabels[r][c]-1) == false){
				segments.add(imageLabels[r][c]-1);
			}
		}}
		NumberOfSegments = segments.size();
		int[] sample = getSegmentMeanColors(image,imageLabels);
		for(int r=0;r<imageLabels.length;r++){
			for(int c=0;c<imageLabels[0].length;c++){
				outputIm[r][c] = sample[imageLabels[r][c]-1];
			}
		}
		//try{
			//ImageIO.write(ImageManipulation.convertToImage(outputIm), "jpg", f);
		//}catch (IOException e) {}
		return outputIm;
	}
	public static int[] getSegmentMeanColors(int[][] image,int[][] imageLabels){
		ArrayList<float[]> stats = new ArrayList<float[]>();
		ArrayList<Integer> segments = new ArrayList<>();
		// create and calculate number of segments////////////////////////////////////////////////////////
		int numberOfSegments;
		for(int r = 0; r < image.length;r++){for(int c = 0; c < image[0].length;c++){
			if(segments.contains(imageLabels[r][c]-1) == false){
				segments.add(imageLabels[r][c]-1);
			}
		}}
		numberOfSegments = segments.size();
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		for(int i=0;i<numberOfSegments;i++){
			stats.add(new float[]{0f,0f,0f,0f});
		}
		// note stats is red green blue averages
		for(int r = 0; r < image.length;r++){for(int c = 0; c < image[0].length;c++){
			Color col = new Color(image[r][c]);
			stats.get(imageLabels[r][c]-1)[0] += col.getRed();
			stats.get(imageLabels[r][c]-1)[1] += col.getGreen();
			stats.get(imageLabels[r][c]-1)[2] += col.getBlue();
			stats.get(imageLabels[r][c]-1)[3] += 1;
		}}
		for(int i = 0;i<numberOfSegments;i++){      //divide by number of pixels in each segment to get means
			stats.get(i)[0]/=stats.get(i)[3];
			stats.get(i)[1]/=stats.get(i)[3];
			stats.get(i)[2]/=stats.get(i)[3];
		}
		Color[] cols = new Color[numberOfSegments];
		int[] colors = new int[numberOfSegments];
		for(int i = 0;i<numberOfSegments;i++){
			cols[i] = new Color((int)stats.get(i)[0],(int)stats.get(i)[1],(int)stats.get(i)[2]);
			colors[i] = cols[i].getRGB();
		}
		return colors;
		
	}
	public static Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	public static int[][] readImage(File f){
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(f);
		} catch (IOException e) {}
		int[][] image = ImageManipulation.RGBtoIntensity(ImageManipulation.convertTo2DUsingGetRGB(bufferedImage));
		/*
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			System.out.println(image[r][c]);
		}}
		*/
		return image;
	}
	public static int[][] addNoise(int[][] image, float mean, float sig){
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			//System.out.println(image[r][c]);
		}}
		Random rand = new Random();
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			image[r][c] += (int)(rand.nextGaussian()*sig+mean);
		}}
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			//System.out.println(image[r][c]);
		}}
		return image;
	}
	public static int[][] whereDifferent(int[][] image1, int[][] image2){
		int[][] output = new int[100][100];
		for(int r=0;r<output.length;r++){for(int c=0;c<output[0].length;c++){
			if(image1[r][c] == image2[r][c]){
				output[r][c] = Color.green.getRGB();
			}else{
				output[r][c] = Color.red.getRGB();
			}
		}}
		return output;
	}
}
