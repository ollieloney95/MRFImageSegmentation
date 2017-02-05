import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageSegmentationModel2 extends GibbsDistributionOrder1{
	public static File outputFile;
	public static List<BufferedImage> imgList; 
	public static void main(String[] args){
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\Sign.jpg"));
			//bufferedImage = ImageIO.read(new File("C:\\Users\\ollie\\Dropbox\\workspace\\Edge Analysis\\Buffalo.jpg"));
		} catch (IOException e) {}
		int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
		int[][] outputImLabels = runModel2(30,150, image,0.5f,0.99f,0.5f,4);
		//File file = new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\OUTPUT.jpg");
		File file = new File("C:\\Users\\ollie\\Dropbox\\workspace\\Edge Analysis\\OUTPUT.jpg");
		try{
				ImageIO.write(ImageManipulation.convertToImage(imageLabelToimg(outputImLabels)), "jpg", file);
		}catch (IOException e) {}
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
	public static List<float[]> SampleSegments(int[][] image,int[][] imageLabels){
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
			stats.add(new float[]{0f,0f,0f});
		}
		for(int r = 0; r < image.length;r++){for(int c = 0; c < image[0].length;c++){
			//System.out.println("r:"+r+" c:"+c+"image dim"+image.length+image[0].length+"image labels dim"+imageLabels.length+imageLabels[0].length);
			stats.get(imageLabels[r][c]-1)[0] += image[r][c];
			stats.get(imageLabels[r][c]-1)[2] += 1;
		}}
		for(int i = 0;i<numberOfSegments;i++){      //divide by number of pixels in each segment to get means
			stats.get(i)[0]/=stats.get(i)[2];
		}
		for(int r = 0; r < image.length;r++){for(int c = 0; c < image[0].length;c++){
			stats.get(imageLabels[r][c]-1)[1]+=Math.pow(image[r][c]-stats.get(imageLabels[r][c]-1)[0], 2);
		}}
		for(int i = 0;i<numberOfSegments;i++){      //divide by number of pixels in each segment to get sds
			stats.get(i)[1]/=stats.get(i)[2];
			stats.get(i)[1] = (float)Math.sqrt(stats.get(i)[1]);
		}
		for(int i = 0;i<numberOfSegments;i++){      //read stats
		}
		return stats;
	}
	public static int[][] pointSubSimulatedAnnealing(int iterations, int[][] image,float T0,float beta, float alpha, float[] means,float[] sigs,List<int[][][]> cliques,int numberOfSegments){
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
			imgList.add(outputImages[i]);
			System.out.println("iteration: "+i);
			
		}
		
		System.out.println("finished iterations");
		return(imageLabels);
		
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
	public static int[][] runModel2(int supIterations,int subIterations, int[][] image,float T0,float alpha,float beta,int numberOfSegments){
		imgList = new ArrayList<BufferedImage>();
		int[][] orImage = ImageManipulation.copyImage(image);
		image=ImageManipulation.RGBtoIntensity(image);
		float[] means = new float[numberOfSegments];
		float[] sigs = new float[numberOfSegments];
		System.out.println("Randomise Labels");
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		//File file = new File("C:\\Users\\ollie\\Dropbox\\workspace\\Edge Analysis\\Unsupervised\\OUTPUT"+0+".jpg");
		File file = new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\Unsupervised3\\OUTPUT"+0+".jpg");
		//try{
			//	ImageIO.write(ImageManipulation.convertToImage(imageLabelToimg(imageLabels)), "jpg", file);
		//}catch (IOException e) {}
		List<int[][][]> cliqueList = getEdgeLattice2(image);
		for(int j=0;j<supIterations;j++){
			List<float[]> sample = SampleSegments(image,imageLabels);
			for(int i=0;i<numberOfSegments;i++){
				means[i] = sample.get(i)[0];
				sigs[i] = sample.get(i)[1];
			}
			imageLabels = pointSubSimulatedAnnealing(subIterations,image,T0,beta,alpha,means,sigs,cliqueList,numberOfSegments);
			//imageLabels = pointSubSimulatedAnnealing(subIterations,image,T0,beta,alpha,means,sigs,cliqueList,numberOfSegments);	
		}
		BufferedImage[] outputImages = new BufferedImage[imgList.size()];
		int i=0;
		for(BufferedImage img : imgList){
			outputImages[i] = img;
			i++;
		}
		ImageManipulation.writeImageList(outputImages, new File("C:\\Users\\ollie\\Desktop\\Unsupervised3"), "output");
		return (imageLabels);
	}
}
