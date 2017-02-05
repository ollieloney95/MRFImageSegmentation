import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageSegmentationModel1 extends GibbsDistributionOrder1{  //inherits from 2
	public static File outputFile; 
	public static void main(String[] args){
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File("C:\\Users\\ollie\\Dropbox\\workspace\\Edge Analysis\\segments.jpg"));
		} catch (IOException e) {}
		int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
		runModel1(null,5, image,0.5f,0.995f,1f,4,false);
	}
	public static void SetOutputFile(File f){
		outputFile = f;
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
		ImageSegmentationModel1 tmp = new ImageSegmentationModel1();
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
	public static BufferedImage runModel1(List<float[]> sample,int iterations, int[][] image,float T0,float alpha,float beta,int numberOfSegments,boolean writeFinalFile){
		image=ImageManipulation.RGBtoIntensity(image);
		/*
		float[] sample1 = SquareSampler(50, 70, 60, 70, image,false);
		float[] sample2 = SquareSampler(3, 19, 3, 16, image,false);
		float[] sample3 = SquareSampler(3, 25, 45, 60, image,false);
		float[] sample4 = SquareSampler(80, 100, 80, 100, image,false);
		float[] means = new float[4];
		float[] sigs = new float[4];
		means[0] = sample1[0];
		means[1] = sample2[0];
		means[2] = sample3[0];
		means[3] = sample4[0];
		sigs[0] = sample1[1];
		sigs[1] = sample2[1];
		sigs[2] = sample3[1];
		sigs[3] = sample4[1];
		*/
		float[] means = new float[numberOfSegments];
		float[] sigs = new float[numberOfSegments];
		for(int i=0;i<numberOfSegments;i++){
			means[i] = sample.get(i)[0];
			sigs[i] = sample.get(i)[1];
		}
		//
		System.out.println("means:"+means[0]+"  "+means[1]+"  "+means[2]+"  "+means[3]+"  ");
		System.out.println("Sigs :"+sigs[0]+"  "+sigs[1]+"  "+sigs[2]+"  "+sigs[3]+"  ");
		List<int[][][]> cliqueList = getEdgeLattice2(image);
		//BufferedImage[] imageLabels = simulatedAnnealing(iterations,image,T0,beta,alpha,means,sigs,cliqueList,numberOfSegments);
		BufferedImage[] imageLabels = pointSimulatedAnnealing(iterations,image,T0,beta,alpha,means,sigs,cliqueList,numberOfSegments);
		if(writeFinalFile == true){
		ImageManipulation.writeImageList(imageLabels, outputFile, "Output");
		}
		return imageLabels[imageLabels.length-1];
	}
	
}
