import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageSegmentationModel3 extends GibbsDistributionOrder1{
	public static float MinSegmentDensity;
	public static float SegmentSimilarTolerance;
	public static void main(String[] args){
		MinSegmentDensity=0.1f;
		SegmentSimilarTolerance=5;
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\Elmur.jpg"));
		} catch (IOException e) {}
		int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
		int[][] outputImLabels = runModel3(50,100, image,0.1f,0.99f,0.3f,10);
		File file = new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\OUTPUT.jpg");
		try{
				ImageIO.write(ImageManipulation.convertToImage(imageLabelToimg(outputImLabels)), "jpg", file);
		}catch (IOException e) {}
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
	public static int[][] changeLabelToRandom(int label, int[][] imageLabels){
		ArrayList<Integer> segments = new ArrayList<>();
		// create and calculate number of segments////////////////////////////////////////////////////////
		int numberOfSegments;
		for(int r = 0; r < imageLabels.length;r++){for(int c = 0; c < imageLabels[0].length;c++){
			if(segments.contains(imageLabels[r][c]-1) == false){
				segments.add(imageLabels[r][c]-1);
			}
		}}
		numberOfSegments = segments.size();
		for(int r = 0; r < imageLabels.length;r++){for(int c = 0; c < imageLabels[0].length;c++){
			if(imageLabels[r][c]>label+1){
				imageLabels[r][c]-=1;
			}else if(imageLabels[r][c]==label+1){
				imageLabels[r][c]=HandyFunctions.randomInt(1, numberOfSegments-1);
			}
		}}
		///
		segments.clear();
		for(int r = 0; r < imageLabels.length;r++){for(int c = 0; c < imageLabels[0].length;c++){
			if(segments.contains(imageLabels[r][c]-1) == false){
				segments.add(imageLabels[r][c]-1);
			}
		}}
		for(Integer i :segments){
			System.out.println("segment"+i);
		}
		numberOfSegments = segments.size();
		///
		return imageLabels;
	}
	public static float[] SegmentsDensity(int[][] image,int[][] imageLabels){
		ArrayList<Integer> segments = new ArrayList<>();
		// create and calculate number of segments////////////////////////////////////////////////////////
		int numberOfSegments;
		for(int r = 0; r < image.length;r++){for(int c = 0; c < image[0].length;c++){
			if(segments.contains(imageLabels[r][c]-1) == false){
				segments.add(imageLabels[r][c]-1);
			}
		}}
		numberOfSegments = segments.size();
		float[] density = new float[numberOfSegments];
	    //////////////////////////////////////////////////////////////////////////////////////////////////
		for(int i=0;i<numberOfSegments;i++){
			density[i] =0f;
		}
		for(int r = 0; r < image.length;r++){for(int c = 0; c < image[0].length;c++){
			density[imageLabels[r][c]-1] += 1;
		}}
		for(int i=0;i<numberOfSegments;i++){
			density[i] /=(float)(imageLabels.length)*(float)(imageLabels[0].length);
		}
		return density;
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
			System.out.println("iteration: "+i);
			
		}
		float[] segmentDensities = SegmentsDensity(image,imageLabels);
		int mindensindex=0;
		float mindens;
		for(int i=0;i<numberOfSegments;i++){
			if(mindensindex<segmentDensities[i]){
				mindensindex=i;
				mindens=segmentDensities[i];
			}
		}
		
		if(segmentDensities[mindensindex]<MinSegmentDensity){
			numberOfSegments-=1;
			System.out.println("Reduced number of segments to:"+numberOfSegments);
			//note want to change label i+1's
			imageLabels = changeLabelToRandom(mindensindex,imageLabels);
			System.out.println("finished iterations");
			return(imageLabels);
		}
		imageLabels = SegmentsSimilar(image,imageLabels);
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
	public static int[][] runModel3(int supIterations,int subIterations, int[][] image,float T0,float alpha,float beta,int numberOfSegments){
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
		List<int[][][]> cliqueList = getEdgeLattice2(image);
		for(int j=0;j<supIterations;j++){
			List<float[]> sample = SampleSegments(image,imageLabels);
			numberOfSegments = sample.size();
			for(int i=0;i<numberOfSegments;i++){
				means[i] = sample.get(i)[0];
				sigs[i] = sample.get(i)[1];
			}
			imageLabels = pointSubSimulatedAnnealing(subIterations,image,T0,beta,alpha,means,sigs,cliqueList,numberOfSegments);	
			File file = new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\Unsupervised\\OUTPUT"+(j+1)+".jpg");
			try{
					ImageIO.write(ImageManipulation.convertToImage(imageLabelToimg(imageLabels)), "jpg", file);
			}catch (IOException e) {}
			//ImageManipulation.writeImageList(imageLabels, new File("C:\\Users\\Ollie Loney\\Dropbox\\workspace\\Edge Analysis\\test"), "filefilefile");
		}
		return (imageLabels);
	}
	public static int[][] mergeSegments(int i, int j, int[][] imageLabels){
		for(int r=0;r<imageLabels.length;r++){for(int c=0;c<imageLabels.length;c++){
			if(imageLabels[r][c] == j){imageLabels[r][c] = i;}
			if(imageLabels[r][c] > j){imageLabels[r][c] -= 1;}
		}}
		return(imageLabels);
	}
	public static int[][] SegmentsSimilar(int[][] image,int[][] imageLabels){
		int[][] im = new int[image.length][image[0].length];
		List<float[]> sample = SampleSegments(image, imageLabels);
		int label1=0;
		int label2=0;
		for(float[] mean1:sample){
			label1+=1;
			for(float[] mean2:sample){
				label2+=1;
				if(mean1[0]!=mean2[0]){
					if(Math.abs(mean1[0]-mean2[0]) < SegmentSimilarTolerance){
						im = mergeSegments(Math.min(label1,label2),Math.max(label1,label2),imageLabels);
						System.out.println("CLUSTERING HAS RAN");
						return(im);
					}
				}
			}
		}
		return(imageLabels);
	}
}
////use Sample segments and see if two segments are very similar ?
