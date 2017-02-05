import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* To Do:
 * test out the sampler, ie make it draw on the picture from where its sampling
 * then input the samplers values into the test
 * make allcliques run much faster.
 */

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class GibbsDistributionOrder1 {
	
	public void displayImageLabels(int[][] imageLabels, File f){
		int red = 16711680;
		int green = 65280;
		int blue = 255;
		int yellow = 16776960;
		for(int r=0;r<imageLabels.length;r++){
			for(int c=0;c<imageLabels[0].length;c++){
				if(imageLabels[r][c] == 1){imageLabels[r][c] = red;}
				if(imageLabels[r][c] == 2){imageLabels[r][c] = green;}
				if(imageLabels[r][c] == 3){imageLabels[r][c] = blue;}
				if(imageLabels[r][c] == 4){imageLabels[r][c] = yellow;}
			}
		}
		try{
			ImageIO.write(ImageManipulation.convertToImage(imageLabels), "jpg", f);
		}catch (IOException e) {}
	}
	public static float[] SquareSampler(int x1, int x2, int y1, int y2,int[][] image,boolean writeToFile,int sampleNumber){
		int[][] im = ImageManipulation.copyImage(image);
		image = ImageManipulation.RGBtoIntensity(image);
		int[][] tmp = ImageManipulation.copyImage(image);
		int[] PixelList = new int[Math.abs((Math.max(y1, y2)-Math.min(y1, y2))*(Math.max(x1, x2)-Math.min(x1, x2)))];
		System.out.println("PixelListLength"+PixelList.length);
		int n=0;
		for(int r=Math.min(x1,x2);r<Math.max(x1,x2);r++){
			for(int c=Math.min(y1,y2);c<Math.max(y1, y2);c++){
				PixelList[n] = image[c][r];
				tmp[c][r] = 0;
				im[c][r] = 0;
				n++;
			}
		}
		float mean = 0f;
		float sig = 0f;
		for(int f:PixelList){
			mean+=f/((float)PixelList.length);
		}
		
		for(int f:PixelList){
			sig+=Math.pow(f-mean, 2)/((float)PixelList.length);
		}
		
		float[] toReturn = new float[2];
		toReturn[0] = mean;
		toReturn[1] = (float)Math.sqrt(sig);
		File outputFile = new File("C:\\Users\\ollie\\Dropbox\\workspace\\Edge Analysis\\Supervised\\sample"+sampleNumber+".jpg");
		if(writeToFile == true){
			try{
				ImageIO.write(ImageManipulation.convertToImage(im), "jpg", outputFile);
			}catch (IOException e) {}
		}
		return(toReturn);
	}
	public static List<int[][][]> getEdgeLattice2(int[][] image){
		//note return (r1,c1,r2,c2)
		int[][][] horizontalLattice = new int[image.length-1][image[0].length][4];
		int[][][] verticalLattice = new int[image.length][image[0].length-1][4];
		for(int r1=0;r1<image.length-1;r1++){
			for(int c1=0;c1<image[0].length;c1++){
					horizontalLattice[r1][c1][0] = r1;
					horizontalLattice[r1][c1][1] = c1;
					horizontalLattice[r1][c1][2] = r1+1;
					horizontalLattice[r1][c1][3] = c1;
			}
		}
		for(int r1=0;r1<image.length;r1++){
			for(int c1=0;c1<image[0].length-1;c1++){
				verticalLattice[r1][c1][0] = r1;
				verticalLattice[r1][c1][1] = c1;
				verticalLattice[r1][c1][2] = r1;
				verticalLattice[r1][c1][3] = c1+1;
			}
		}
		List<int[][][]> edgeLattice = new ArrayList<int[][][]>();
		edgeLattice.add(horizontalLattice);
		edgeLattice.add(verticalLattice);
		return edgeLattice;
	}
	public float cliquePotentialPair(int[] clique,int[][] imageLabels,int[][] image, float beta, float[] means,float[] sigs){
		if(imageLabels[clique[0]][clique[1]]   ==  imageLabels[clique[2]][clique[3]]){
			return -beta;
		}else{
			return beta;
		}
	}
	public float cliquePotentialSingle(int[] clique,int[][] imageLabels,int[][] image, float beta, float[] means,float[] sigs){
		//return((float)(Math.log((10)*Math.pow(2*Math.PI,0.5)) + Math.pow(image[clique[0]][clique[1]] - means[imageLabels[clique[0]][clique[1]]-1], 2)/(2*Math.pow(10, 2))));
		return((float)(Math.log((sigs[imageLabels[clique[0]][clique[1]]-1]+10)*Math.pow(2*Math.PI,0.5)) + Math.pow(image[clique[0]][clique[1]] - means[imageLabels[clique[0]][clique[1]]-1], 2)/(2*Math.pow(sigs[imageLabels[clique[0]][clique[1]]-1]+10, 2))));
	}
	public static float getEnergy2(float beta,int[][] image, float[] means,float[] sigs, int[][] imageLabels,List<int[][][]> cliques){
		float z=0f;
		GibbsDistributionOrder1 tmp = new GibbsDistributionOrder1();
		for(int r = 0;r<cliques.get(0).length;r++){
			for(int c = 0;c<cliques.get(0)[0].length;c++){
				z+=tmp.cliquePotentialPair(cliques.get(0)[r][c],imageLabels,image,beta,means,sigs);
			}
		}
		for(int r = 0;r<cliques.get(1).length;r++){
			for(int c = 0;c<cliques.get(1)[0].length;c++){
				z+=tmp.cliquePotentialPair(cliques.get(1)[r][c],imageLabels,image,beta,means,sigs);
			}
		}
		for(int r = 0;r<image.length;r++){
			for(int c = 0;c<image[0].length;c++){
				z+=tmp.cliquePotentialSingle(new int[]{r,c},imageLabels,image,beta,means,sigs);
			}
		}
		return z;	
	}
	//////sort out this function for new edge lattice and image lattice
	public static BufferedImage[] simulatedAnnealing(int iterations, int[][] image,float T0,float beta, float alpha, float[] means,float[] sigs,List<int[][][]> cliques,int numberOfSegments){
		float T = T0;
		BufferedImage[] outputImages = new BufferedImage[iterations];
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		System.out.println("starting iterations");
		List<int[][][]> smallImageCliques = getEdgeLattice2(new int[3][3]);
		for(int i=0;i<iterations;i++){
			imageLabels = AnnealIteration(false,image,imageLabels,T,beta,alpha,means,sigs,cliques,smallImageCliques,numberOfSegments);
			T=alpha*T;
			outputImages[i] = ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
			System.out.println("iteration: "+i);
		}
		System.out.println("finished iterations");
		return(outputImages);
	}
	private  static int[][] AnnealIteration(boolean sampleRandomly,int[][] image,int[][] imageLabels,float T,float beta, float alpha, float[] means,float[] sigs,List<int[][][]> cliques,List<int[][][]> smallImageCliques,int NumberOfSegments){
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			int[][] originalImageLabels = ImageManipulation.copyImage(imageLabels);
			int row;
			int col;
			if(sampleRandomly == true){
				row = HandyFunctions.randomInt(1,image.length);
				col = HandyFunctions.randomInt(1,image[0].length);
			}else{
				row = r+1;
				col = c+1;
			}
			imageLabels[row-1][col-1]=HandyFunctions.randomInt(1,NumberOfSegments);
			
			//////-----------------note here could just use image around changed pixel.----------------------------
			//float du = getEnergy2(beta,image,means,sigs,imageLabels,cliques) - getEnergy2(beta,image,means,sigs,originalImageLabels,cliques);
			
			///note need to size these correctly----------------------------------------------------------
			int[][] smallImage = new int[3][3];
			int[][] smallImageLabels = new int[3][3];
			int[][] smallOriginalImageLabels = new int[3][3];
			for(int rr=0;rr<smallImage.length;rr++){for(int cc=0;cc<smallImage.length;cc++){
			smallImage[rr][cc] = 1;
			smallImageLabels[rr][cc] = 1;
			smallOriginalImageLabels[rr][cc] = 1;
			if(row+rr-2 >0 && row+rr-2 < image.length && col+cc-2 >0 && col+cc-2 < image[0].length ){
				smallImage[rr][cc] = image[row+rr-2][col+cc-2];
				smallImageLabels[rr][cc] = imageLabels[row+rr-2][col+cc-2];
				smallOriginalImageLabels[rr][cc] = originalImageLabels[row+rr-2][col+cc-2];
			}}}
			float du = getEnergy2(beta,smallImage,means,sigs,smallImageLabels,smallImageCliques) - getEnergy2(beta,smallImage,means,sigs,smallOriginalImageLabels,smallImageCliques);
			//////---------------------------------------------------------------------------------------------------
			if(du<=0){
				//leave the new imageLabels
			}else{
				if(Math.random() < Math.exp(-du / T)){
					//leave the new imageLabels
				}else{
					imageLabels = originalImageLabels;
				}
			}
		}
		}
		System.out.println("completed iteration");
		return(imageLabels);
	}
	public static int[][] imageLabelToimg(int[][] imageLabels){
		int red = 16711680;
		int green = 65280;
		int blue = 255;
		int yellow = 16776960;
		int[][] tempImageLabels = new int[imageLabels.length][imageLabels[0].length];
		for(int r=0;r<imageLabels.length;r++){
			for(int c=0;c<imageLabels[0].length;c++){
				if(imageLabels[r][c] == 1){tempImageLabels[r][c] = red;}
				if(imageLabels[r][c] == 2){tempImageLabels[r][c] = green;}
				if(imageLabels[r][c] == 3){tempImageLabels[r][c] = blue;}
				if(imageLabels[r][c] == 4){tempImageLabels[r][c] = yellow;}
			}
		}
		return tempImageLabels;
	}
	public static float cliquePair(int[] x1y1x2y2, int[][] imageLabels,int[][] image,float beta,float[] means,float[] sigs){
		if(imageLabels[x1y1x2y2[0]][x1y1x2y2[1]]   ==  imageLabels[x1y1x2y2[2]][x1y1x2y2[3]]){
			return -beta;
		}else{
			return beta;
		}
	}
	public static List<List<int[]>> getEdges(int[][] image,int r, int c){
		//not horEdges is a list of integers where each int[] is the point on left of edge
		//not verEdges is a list of integers where each int[] is the point on bottom of edge
		ArrayList<int[]> horEdges = new ArrayList<int[]>();
		ArrayList<int[]> verEdges = new ArrayList<int[]>();
		if(r>0){
			horEdges.add(new int[]{r-1,c,r,c});
		}
		if(r<image.length-1){
			horEdges.add(new int[]{r,c,r+1,c});
		}
		if(c>0){
			verEdges.add(new int[]{r,c-1,r,c});
		}
		if(c<image[0].length-1){
			verEdges.add(new int[]{r,c,r,c+1});
		}
		List<List<int[]>> edgeLattice = new ArrayList<List<int[]>>();
		edgeLattice.add(horEdges);
		edgeLattice.add(verEdges);
		return edgeLattice;
	}
	public static float getPointEnergy(int r, int c, float beta,int[][] image, float[] means,float[] sigs, int[][] imageLabels){
		float z=0f;
		List<List<int[]>> edges = getEdges(image,r, c);
		GibbsDistributionOrder1 tmp = new GibbsDistributionOrder1();
		for(int[] i:edges.get(0)){
			z+=cliquePair(i,imageLabels,image,beta,means,sigs);
		}
		for(int[] i:edges.get(1)){
			z+=cliquePair(i,imageLabels,image,beta,means,sigs);
		}
		z+=((float)(Math.log((sigs[imageLabels[r][c]-1]+10)*Math.pow(2*Math.PI,0.5)) + Math.pow(image[r][c] - means[imageLabels[r][c]-1], 2)/(2*Math.pow(sigs[imageLabels[r][c]-1]+10, 2))));
		return z;	
	}
	public static BufferedImage[] pointSimulatedAnnealing(int iterations, int[][] image,float T0,float beta, float alpha, float[] means,float[] sigs,List<int[][][]> cliques,int numberOfSegments){
		float T = T0;
		BufferedImage[] outputImages = new BufferedImage[iterations];
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		System.out.println("starting iterations");
		for(int i=0;i<iterations;i++){
			imageLabels = pointAnnealIteration(false,image,imageLabels,T,beta,alpha,means,sigs,numberOfSegments);
			T=alpha*T;
			outputImages[i] = ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
			System.out.println("iteration: "+i);
		}
		System.out.println("finished iterations");
		return(outputImages);
	}
	private  static int[][] pointAnnealIteration(boolean sampleRandomly,int[][] image,int[][] imageLabels,float T,float beta, float alpha, float[] means,float[] sigs,int NumberOfSegments){
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			int row;
			int col;
			if(sampleRandomly == true){
				row = HandyFunctions.randomInt(1,image.length);
				col = HandyFunctions.randomInt(1,image[0].length);
			}else{
				row = r+1;
				col = c+1;
			}
			float oldEnergy =getPointEnergy(r,c,beta,image,means,sigs,imageLabels);
			int originalLabel = imageLabels[row-1][col-1];
			imageLabels[row-1][col-1]=HandyFunctions.randomInt(1,NumberOfSegments);
			float newEnergy =getPointEnergy(r,c,beta,image,means,sigs,imageLabels);
			float du = newEnergy-oldEnergy;
			if(du<=0){
				//leave the new imageLabels
			}else{
				if(Math.random() < Math.exp(-du / T)){
					//leave the new imageLabels
				}else{
					imageLabels[row-1][col-1]=originalLabel;
				}
			}
		}
		}
		System.out.println("completed iteration");
		return(imageLabels);
	}
	
}
