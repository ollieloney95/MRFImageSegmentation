import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class EdgeFinderModel1 extends GibbsDistributionOrder1 {

	private static List<int[][]> getEdgeLattice(int[][] image,float a){
		int[][] horizontalLattice = new int[image.length-1][image[0].length];
		int[][] verticalLattice = new int[image.length][image[0].length-1];
		for(int r1=0;r1<image.length-1;r1++){
			for(int c1=0;c1<image[0].length;c1++){
				if(difference(image[r1][c1],image[r1+1][c1],a)){
					horizontalLattice[r1][c1] = 0;
				}else{
					horizontalLattice[r1][c1] = 1;
				}
			}
		}
		for(int r1=0;r1<image.length;r1++){
			for(int c1=0;c1<image[0].length-1;c1++){
				if(difference(image[r1][c1],image[r1][c1+1],a)){
					verticalLattice[r1][c1] = 0;
				}else{
					verticalLattice[r1][c1] = 1;
				}
			}
		}
		List<int[][]> edgeLattice = new ArrayList<int[][]>();
		edgeLattice.add(horizontalLattice);
		edgeLattice.add(verticalLattice);
		return edgeLattice;
	}
	private static int[][] edgesToImage2(List<int[][]> edgeLattice){
		int[][] edgeImage = new int[edgeLattice.get(0).length+1][edgeLattice.get(0)[0].length];
		//horizontal image 
		for(int r=0;r<edgeImage.length-1;r++){
			for(int c=0;c<edgeImage[0].length;c++){
				edgeImage[r][c]+=edgeLattice.get(0)[r][c];
			}
		}
		//vertical image 
		for(int r=0;r<edgeImage.length;r++){
			for(int c=0;c<edgeImage[0].length-1;c++){
				edgeImage[r][c]+=edgeLattice.get(1)[r][c];
			}
		}
		return(edgeImage);
	}
	public static void main(String[] args) {
		BufferedImage bufferedImage = null;
		//read image
		try {
			bufferedImage = ImageIO.read(new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\sat.jpg"));
		} catch (IOException e) {}
		System.out.println("read image");
		int[][] img = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
		img = edgesToImage2(getEdgeLattice(img,5000));
		BufferedImage outputImage = ImageManipulation.convertToImage(ImageManipulation.correctBinaryColor(img));
		File f = new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\satedge.jpg");
		try{
			ImageIO.write(outputImage, "jpg", f);
		}catch (IOException e) {}
		System.out.println("wrote image");
	}
	private static boolean difference(int col1, int col2,float a){
		Color color1 = new Color(col1);
		Color color2 = new Color(col2);
		if(Math.pow(color1.getBlue()-color2.getBlue(),2) +Math.pow(color1.getRed()-color2.getRed(),2) +Math.pow(color1.getGreen()-color2.getGreen(),2)<a){
			return true;
		}
		return false;
	}
	public static BufferedImage run(int[][] image,int sensitivity){
		image = edgesToImage2(getEdgeLattice(image,sensitivity));
		BufferedImage outputImage = ImageManipulation.convertToImage(ImageManipulation.correctBinaryColor(image));
		return outputImage;
	}
}
