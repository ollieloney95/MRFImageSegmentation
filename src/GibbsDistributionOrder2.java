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

public class GibbsDistributionOrder2 {
	private MainFrame gui;
	public GibbsDistributionOrder2(MainFrame gui) {
	    this.gui = gui;
	}
	//getCliques returns a list of cliques by type.
	public static List<List<List<int[]>>> getCliques(int[][] image,boolean[] cliquesToInclude){
		//cliquesToInclude is a list of boolean values,  [b1,b2,...]
		List<List<List<int[]>>> cliqueList = new ArrayList<List<List<int[]>>>();
		List<List<int[]>> beta1 = new ArrayList<List<int[]>>();
		List<List<int[]>> beta2 = new ArrayList<List<int[]>>();
		List<List<int[]>> beta3 = new ArrayList<List<int[]>>();
		List<List<int[]>> beta4 = new ArrayList<List<int[]>>();
		List<List<int[]>> y1 = new ArrayList<List<int[]>>();
		List<List<int[]>> y2 = new ArrayList<List<int[]>>();
		List<List<int[]>> y3 = new ArrayList<List<int[]>>();
		List<List<int[]>> y4 = new ArrayList<List<int[]>>();
		List<List<int[]>> e1 = new ArrayList<List<int[]>>();
		if(cliquesToInclude[0]==true){
			for(int r1=0;r1<image.length;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1,c1});
					points.add(new int[]{r1,c1+1});
					beta2.add(points);
				}
			}
		}
		if(cliquesToInclude[1]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1,c1});
					points.add(new int[]{r1+1,c1});
					beta1.add(points);
				}
			}
		}
		if(cliquesToInclude[2]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1+1,c1});
					points.add(new int[]{r1,c1+1});
					beta3.add(points);
				}
			}
		}
		if(cliquesToInclude[3]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1,c1});
					points.add(new int[]{r1+1,c1+1});
					beta4.add(points);
				}
			}
		}
		if(cliquesToInclude[4]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1,c1});
					points.add(new int[]{r1+1,c1+1});
					points.add(new int[]{r1+1,c1});
					y1.add(points);
				}
			}
		}
		if(cliquesToInclude[5]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1,c1});
					points.add(new int[]{r1,c1+1});
					points.add(new int[]{r1+1,c1});
					y2.add(points);
				}
			}
		}
		if(cliquesToInclude[6]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1+1,c1});
					points.add(new int[]{r1,c1+1});
					points.add(new int[]{r1+1,c1+1});
					y3.add(points);
				}
			}
		}
		if(cliquesToInclude[7]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1,c1});
					points.add(new int[]{r1,c1+1});
					points.add(new int[]{r1+1,c1+1});
					y4.add(points);
				}
			}
		}
		if(cliquesToInclude[8]==true){
			for(int r1=0;r1<image.length-1;r1++){
				for(int c1=0;c1<image[0].length-1;c1++){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r1,c1});
					points.add(new int[]{r1,c1+1});
					points.add(new int[]{r1+1,c1});
					points.add(new int[]{r1+1,c1+1});
					e1.add(points);
				}
			}
		}
		cliqueList.add(beta1);
		cliqueList.add(beta2);
		cliqueList.add(beta3);
		cliqueList.add(beta4);
		cliqueList.add(y1);
		cliqueList.add(y2);
		cliqueList.add(y3);
		cliqueList.add(y4);
		cliqueList.add(e1);
		return cliqueList;
	}
	//cliquePotential returns the -beta if all labels in the clique are the same, otherwise beta.
	public static float cliquePotential(List<int[]> clique,int[][] imageLabels, float beta){
		// note 
		int n = clique.size();
		boolean cliqueIsGood = true;
		int val = imageLabels[clique.get(0)[0]][clique.get(0)[1]];
		for(int i = 1; i< n; i++){
			if(val == imageLabels[clique.get(i)[0]][clique.get(i)[1]]){
			}else{
				cliqueIsGood = false;
			}
		}
		if(n==0){System.out.println("error");}
		if(cliqueIsGood == true){
			return -beta;
		}else{
			return beta;
		}
	}
	//goes through the list of cliques and sums energy
	public static float getEnergy(float[] beta,int[][] image, int[][] imageLabels,List<List<List<int[]>>> cliques){
		float z=0f;
		int i=0;
		for(List<List<int[]>> cliqueListByType : cliques){
			for(List<int[]> clique : cliqueListByType){
				z+=cliquePotential(clique,imageLabels,beta[i]);
			}
			i++;
		}
		return z;	
	}
	//sort out this function for new edge lattice and image lattice
	public BufferedImage[] simulatedAnnealing(int iterations, int[][] image,float T0,float[] beta, float alpha,List<List<List<int[]>>> cliques,int numberOfSegments){
		float T = T0;
		BufferedImage[] outputImages = new BufferedImage[iterations];
		//randomise image labels
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		//----
		System.out.println("starting iterations");
		
		for(int i=0;i<iterations;i++){
			imageLabels = AnnealIteration(image,imageLabels,T,beta,alpha,cliques,numberOfSegments);
			T=alpha*T;
			outputImages[i] = ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
			System.out.println("iteration: "+i);
			this.gui.SetStatus("lalalallal");
		}
		System.out.println("finished iterations");
		return(outputImages);
	}
	private  static int[][] AnnealIteration(int[][] image,int[][] imageLabels,float T,float[] beta, float alpha,List<List<List<int[]>>> cliques,int NumberOfSegments){
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			int[][] originalImageLabels = ImageManipulation.copyImage(imageLabels);
			imageLabels[HandyFunctions.randomInt(1,image.length)-1][HandyFunctions.randomInt(1,image[0].length)-1]=HandyFunctions.randomInt(1,NumberOfSegments);
			float du = getEnergy(beta, image,imageLabels,cliques) - getEnergy(beta, image,originalImageLabels,cliques);
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
		List<Integer> labels = new ArrayList<Integer>();
		for(int r=0;r<imageLabels.length;r++){
			for(int c=0;c<imageLabels[0].length;c++){
				if(labels.contains(imageLabels[r][c]-1) ==false){
					labels.add(imageLabels[r][c]-1);
				}
			}
		}
		int numberOfSegments = labels.size();
		System.out.println("numberOfSegments from output" +numberOfSegments);
		//System.out.println("numberOfSegments:  "+numberOfSegments);
		int[] colors = new int[numberOfSegments];
		Color[] tempcolors = new Color[numberOfSegments];
		for(int i=0;i<numberOfSegments;i++){
			tempcolors[i] = new Color(i*255/(numberOfSegments-1),i*255/(numberOfSegments-1),i*255/(numberOfSegments-1));
			colors[i] = tempcolors[i].getRGB();
			System.out.println("color:"+i+" is " +colors[i]);
		}
		int[][] tempImageLabels = new int[imageLabels.length][imageLabels[0].length];
		for(int r=0;r<imageLabels.length;r++){
			for(int c=0;c<imageLabels[0].length;c++){
				tempImageLabels[r][c] = colors[imageLabels[r][c]-1];
			}
		}
		return tempImageLabels;
		/*
		int red = 16711680;
		int green = 65280;
		int blue = 255;
		int yellow = 16776960;
		int[][] tempImageLabels = new int[imageLabels.length][imageLabels[0].length];
		for(int r=0;r<imageLabels.length;r++){
			for(int c=0;c<imageLabels[0].length;c++){
				if(imageLabels[r][c] == 1){tempImageLabels[r][c] = Color.white.getRGB();}
				if(imageLabels[r][c] == 2){tempImageLabels[r][c] = 0;}
				if(imageLabels[r][c] == 5){tempImageLabels[r][c] = red;}
				if(imageLabels[r][c] == 6){tempImageLabels[r][c] = green;}
				if(imageLabels[r][c] == 3){tempImageLabels[r][c] = blue;}
				if(imageLabels[r][c] == 4){tempImageLabels[r][c] = yellow;}
			}
		}
		return tempImageLabels;
		*/
	}
	//getCliques returns a list of cliques by type.
	public static List<List<List<int[]>>> getPointCliques(int r, int c, int[][] image,boolean[] cliquesToInclude){
			//cliquesToInclude is a list of boolean values,  [b1,b2,...]
			List<List<List<int[]>>> cliqueList = new ArrayList<List<List<int[]>>>();
			List<List<int[]>> beta1 = new ArrayList<List<int[]>>();
			List<List<int[]>> beta2 = new ArrayList<List<int[]>>();
			List<List<int[]>> beta3 = new ArrayList<List<int[]>>();
			List<List<int[]>> beta4 = new ArrayList<List<int[]>>();
			List<List<int[]>> y1 = new ArrayList<List<int[]>>();
			List<List<int[]>> y2 = new ArrayList<List<int[]>>();
			List<List<int[]>> y3 = new ArrayList<List<int[]>>();
			List<List<int[]>> y4 = new ArrayList<List<int[]>>();
			List<List<int[]>> e1 = new ArrayList<List<int[]>>();
			
			if(cliquesToInclude[0]==true){
				if(r>=0 && c>=0 && r<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r,c+1});
					beta1.add(points);
				}
				if(r>=0 && c-1>=0 && r<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r,c-1});
					beta1.add(points);
				}
			}
			if(cliquesToInclude[1]==true){
				if(r-1>=0 && c>=0 && r<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r-1,c});
					points.add(new int[]{r,c});
					beta2.add(points);
				}
				if(r>=0 && c>=0 && r+1<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r+1,c});
					beta2.add(points);
				}
			}
			if(cliquesToInclude[2]==true){
				if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r+1,c-1});
					points.add(new int[]{r,c});
					beta3.add(points);
				}
				if(r-1>=0 && c>=0 && r<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r-1,c+1});
					beta3.add(points);
				}
			}
			if(cliquesToInclude[3]==true){
				if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r+1,c+1});
					beta4.add(points);
				}
				if(r-1>=0 && c-1>=0 && r<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r-1,c-1});
					beta4.add(points);
				}
			}
			if(cliquesToInclude[4]==true){
				if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r+1,c+1});
					points.add(new int[]{r+1,c});
					y1.add(points);
				}
				if(r-1>=0 && c-1>=0 && r<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r-1,c-1});
					points.add(new int[]{r,c});
					points.add(new int[]{r,c-1});
					y1.add(points);
				}
				if(r-1>=0 && c>=0 && r<image.length&&c+1< image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r-1,c});
					points.add(new int[]{r,c+1});
					points.add(new int[]{r,c});
					y1.add(points);
				}
			}
			if(cliquesToInclude[5]==true){
				if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r,c+1});
					points.add(new int[]{r+1,c});
					y2.add(points);
				}
				if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c-1});
					points.add(new int[]{r,c});
					points.add(new int[]{r+1,c-1});
					y2.add(points);
				}
				if(r-1>=0 && c>=0 && r<image.length&&c+1< image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r-1,c});
					points.add(new int[]{r-1,c+1});
					points.add(new int[]{r,c});
					y2.add(points);
				}
			}
			if(cliquesToInclude[6]==true){
				if(r-1>=0 && c>=0 && r<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r-1,c+1});
					points.add(new int[]{r,c+1});
					y3.add(points);
				}
				if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r+1,c-1});
					points.add(new int[]{r,c});
					points.add(new int[]{r+1,c});
					y3.add(points);
				}
				if(r-1>=0 && c-1>=0 && r<image.length&&c< image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c-1});
					points.add(new int[]{r-1,c});
					points.add(new int[]{r,c});
					y3.add(points);
				}
			}
			if(cliquesToInclude[7]==true){
				if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r,c+1});
					points.add(new int[]{r+1,c+1});
					y4.add(points);
				}
				if(r>=0 && c-1>=0 && r+1<image.length&&c< image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c-1});
					points.add(new int[]{r,c});
					points.add(new int[]{r+1,c});
					y4.add(points);
				}
				if(r-1>=0 && c-1>=0 && r<image.length&&c< image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r-1,c-1});
					points.add(new int[]{r-1,c});
					points.add(new int[]{r,c});
					y4.add(points);
				}
			}
			if(cliquesToInclude[8]==true){
				if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c});
					points.add(new int[]{r,c+1});
					points.add(new int[]{r+1,c});
					points.add(new int[]{r+1,c+1});
					e1.add(points);
				}
				if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r,c-1});
					points.add(new int[]{r,c});
					points.add(new int[]{r+1,c-1});
					points.add(new int[]{r+1,c});
					e1.add(points);
				}
				if(r-1>=0 && c>=0 && r<image.length&&c+1 < image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r-1,c});
					points.add(new int[]{r-1,c+1});
					points.add(new int[]{r,c});
					points.add(new int[]{r,c+1});
					e1.add(points);
				}
				if(r-1>=0 && c-1>=0 && r<image.length&&c< image[0].length){
					List<int[]> points = new ArrayList<int[]>();
					points.add(new int[]{r-1,c-1});
					points.add(new int[]{r-1,c});
					points.add(new int[]{r,c-1});
					points.add(new int[]{r,c});
					e1.add(points);
				}
			}
			cliqueList.add(beta1);
			cliqueList.add(beta2);
			cliqueList.add(beta3);
			cliqueList.add(beta4);
			cliqueList.add(y1);
			cliqueList.add(y2);
			cliqueList.add(y3);
			cliqueList.add(y4);
			cliqueList.add(e1);
			//testGetCliques(cliqueList);
			return cliqueList;
		}	
	public static float getPointEnergy(int r, int c, float[] beta,int[][] image,int[][] imageLabels,List<List<List<int[]>>> cliques, boolean[] cliquesToInclude){
		float z=0f;
		//for(List<List<int[]>> cliqueListByType : cliques){
		List<List<int[]>> cliqueListByType;
		for(int i=0;i<9;i++){
			cliqueListByType = cliques.get(i);
			if(cliquesToInclude[i]==true){
				for(List<int[]> clique : cliqueListByType){
					z+=cliquePotential(clique,imageLabels,beta[i]);
					//System.out.println("i:"+i+" new z:"+z);
				}
			}
			//i++;
		}
		return z;
	}
	public BufferedImage[] pointSimulatedAnnealing(int iterations, int[][] image,float T0,float[] beta, float alpha,int numberOfSegments,boolean[] cliquesToInclude){
		float T = T0;
		BufferedImage[] outputImages = new BufferedImage[iterations];
		//randomise image labels
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		//----
		System.out.println("starting iterations");
		
		for(int i=0;i<iterations;i++){
			imageLabels = pointAnnealIteration(image,imageLabels, T, beta, alpha, numberOfSegments, cliquesToInclude);
			T=alpha*T;
			outputImages[i] = ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
			System.out.println("iteration: "+i);
			this.gui.SetStatus("lalalallal");
		}
		System.out.println("finished iterations");
		return(outputImages);
	}
	private  static int[][] pointAnnealIteration(int[][] image,int[][] imageLabels,float T,float[] beta, float alpha,int NumberOfSegments,boolean[] cliquesToInclude){
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			int[][] originalImageLabels = ImageManipulation.copyImage(imageLabels);
			//r=HandyFunctions.randomInt(1,image.length)-1;
			//c=HandyFunctions.randomInt(1,image[0].length)-1;
			List<List<List<int[]>>> cliques = getPointCliques(r, c, image, cliquesToInclude);
			imageLabels[r][c]=HandyFunctions.randomInt(1,NumberOfSegments);
			float du = getPointEnergy(r,c,beta, image,imageLabels,cliques,cliquesToInclude) - getPointEnergy(r,c,beta, image,originalImageLabels,cliques,cliquesToInclude);
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
	public static BufferedImage[] samplingAlgorithm1(int iterations,float[] beta,int numberOfSegments,boolean[] cliquesToInclude){
		BufferedImage[] outputImages = new BufferedImage[iterations];
		int[][] image = new int[100][100];
		//List<List<List<int[]>>> cliqueList = getCliques(image,cliquesToInclude);
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				image[r][c] = HandyFunctions.randomInt(0, numberOfSegments-1);
			}
		}
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		for(int i=0;i<iterations;i++){
			System.out.print("iteration: "+i);
			for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
				float p=1f;
				int[][] originalImageLabels = ImageManipulation.copyImage(imageLabels);
				imageLabels[r][c]=HandyFunctions.randomInt(1,numberOfSegments);
				
				List<List<List<int[]>>> cliques = getPointCliques(r, c, image, cliquesToInclude);
				
				float p2 = (float) Math.exp(getPointEnergy(r,c,beta, image,originalImageLabels,cliques,cliquesToInclude) - getPointEnergy(r,c,beta, image,imageLabels,cliques,cliquesToInclude));
				//float p2 = (float) Math.exp(getEnergy(beta, image,originalImageLabels,cliqueList)-getEnergy(beta, image,imageLabels,cliqueList));
				p=Math.min(p, p2);
				if(p<Math.random()){
					imageLabels=originalImageLabels;
				}
			}}
			outputImages[i]=ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
		}
		return outputImages;
	}
	public static void testGetCliques(List<List<List<int[]>>> cliques){
		for(List<List<int[]>> cliqueType : cliques){
			for(List<int[]> clique : cliqueType){
				for(int[] pos : clique){
					System.out.print(" r: "+pos[0]+" c:"+pos[1]+"--");
				}
				System.out.println("done clique");
			}
		}
	}
	public static BufferedImage[] samplingAlgorithm2(int iterations,float[] beta,int numberOfSegments,boolean[] cliquesToInclude){
		BufferedImage[] outputImages = new BufferedImage[iterations];
		int[][] image = new int[100][100];
		//List<List<List<int[]>>> cliqueList = getCliques(image,cliquesToInclude);
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				image[r][c] = HandyFunctions.randomInt(0, numberOfSegments-1);
			}
		}
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		for(int i=0;i<iterations;i++){
			System.out.print("iteration: "+i);
			for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
				float[] energys=new float[numberOfSegments];
				/*
				for(int j=1;j<=numberOfSegments;j++){
					imageLabels[r][c]=j;
					energys[j-1]=getEnergy(beta, image,imageLabels,cliqueList);
					summedEnergy+=energys[0];
				}
				*/
				List<List<List<int[]>>> cliques = getPointCliques(r, c, image, cliquesToInclude);	
				for(int j=1;j<=numberOfSegments;j++){
					imageLabels[r][c]=j;
					energys[j-1]=getPointEnergy(r,c,beta, image,imageLabels,cliques,cliquesToInclude);
				}
				float[] probs = new float[numberOfSegments];
				float probSum = 0f;
				for(int j=0;j<numberOfSegments;j++){
					float otherProbs = 0f;
					for(int k=0;k<numberOfSegments;k++){
						otherProbs+=(float)(Math.exp(energys[j]-energys[k]));
					}
					probs[j] = 1/otherProbs;
					probSum+=probs[j];
				}
				//System.out.println("prob0"+probs[0]+"prob1"+probs[1]);
				float p=(float)Math.random();
				float pLeft=1f;
				int chosenLabel=0;
				for(int j=0;j<numberOfSegments;j++){
					pLeft-=probs[j];
					if(p>pLeft && chosenLabel==0){
						chosenLabel=j+1;
					}
				}
				if(chosenLabel==0){chosenLabel=numberOfSegments;System.out.println("errorlad");}
				imageLabels[r][c]=chosenLabel;
			}}
			outputImages[i]=ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
		}
		return outputImages;
	}
	public static int[][] samplingMLL(int iterations,float[] beta,int numberOfSegments){
		boolean[] cliquesToInclude = {false,false,false,false,false,false,false,false,false};
		int z=0;
		for(float b : beta){
			if(b!=0){
				cliquesToInclude[z]=true;
			}
			z++;
		}
		//System.out.println("b1"+cliquesToInclude[0]+"b2"+cliquesToInclude[1]+"b3"+cliquesToInclude[2]+"b4"+cliquesToInclude[3]+"y1"+cliquesToInclude[4]+"y2"+cliquesToInclude[5]+"y3"+cliquesToInclude[6]+"y4"+cliquesToInclude[7]);
		//System.out.println("b1"+beta[0]+"b2"+beta[1]+"b3"+beta[2]+"b4"+beta[3]);
		int[][] outputImages = new int[100][100];
		int[][] image = new int[100][100];
		//List<List<List<int[]>>> cliqueList = getCliques(image,cliquesToInclude);
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				image[r][c] = HandyFunctions.randomInt(0, numberOfSegments-1);
			}
		}
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		for(int i=0;i<iterations;i++){
			System.out.print("iteration: "+i);
			for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
				float[] energys=new float[numberOfSegments];
				/*
				for(int j=1;j<=numberOfSegments;j++){
					imageLabels[r][c]=j;
					energys[j-1]=getEnergy(beta, image,imageLabels,cliqueList);
					summedEnergy+=energys[0];
				}
				*/
				List<List<List<int[]>>> cliques = getPointCliques(r, c, image, cliquesToInclude);	
				for(int j=1;j<=numberOfSegments;j++){
					imageLabels[r][c]=j;
					energys[j-1]=getPointEnergy(r,c,beta, image,imageLabels,cliques,cliquesToInclude);
				}
				float[] probs = new float[numberOfSegments];
				float probSum = 0f;
				for(int j=0;j<numberOfSegments;j++){
					float otherProbs = 0f;
					for(int k=0;k<numberOfSegments;k++){
						otherProbs+=(float)(Math.exp(energys[j]-energys[k]));
					}
					probs[j] = 1/otherProbs;
					probSum+=probs[j];
				}
				//System.out.println("prob0"+probs[0]+"prob1"+probs[1]);
				float p=(float)Math.random();
				float pLeft=1f;
				int chosenLabel=0;
				for(int j=0;j<numberOfSegments;j++){
					pLeft-=probs[j];
					if(p>pLeft && chosenLabel==0){
						chosenLabel=j+1;
					}
				}
				if(chosenLabel==0){chosenLabel=numberOfSegments;System.out.println("errorlad");}
				imageLabels[r][c]=chosenLabel;
			}}
			outputImages=imageLabels;
		}
		return outputImages;
	}
	public static int[][] samplingHighLevelMRF(int iterations,float[] beta,int numberOfSegments){
		boolean[] cliquesToInclude = {false,false,false,false,false,false,false,false,false};
		int m=0;
		for(float b : beta){
			if(b!=0){
				cliquesToInclude[m]=true;
			}
			m++;
		}
		int[][] image = new int[100][100];
		//List<List<List<int[]>>> cliqueList = getCliques(image,cliquesToInclude);
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				image[r][c] = HandyFunctions.randomInt(0, numberOfSegments-1);
			}
		}
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		for(int i=0;i<iterations;i++){
			System.out.print("iteration: "+i);
			for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
				float[] energys=new float[numberOfSegments];
				/*
				for(int j=1;j<=numberOfSegments;j++){
					imageLabels[r][c]=j;
					energys[j-1]=getEnergy(beta, image,imageLabels,cliqueList);
					summedEnergy+=energys[0];
				}
				*/
				List<List<List<int[]>>> cliques = getPointCliques(r, c, image, cliquesToInclude);	
				for(int j=1;j<=numberOfSegments;j++){
					imageLabels[r][c]=j;
					energys[j-1]=getPointEnergy(r,c,beta, image,imageLabels,cliques,cliquesToInclude);
				}
				float[] probs = new float[numberOfSegments];
				for(int j=0;j<numberOfSegments;j++){
					float otherProbs = 0f;
					for(int k=0;k<numberOfSegments;k++){
						otherProbs+=(float)(Math.exp(energys[j]-energys[k]));
					}
					probs[j] = 1/otherProbs;
				}
				//System.out.println("prob0"+probs[0]+"prob1"+probs[1]);
				float p=(float)Math.random();
				float pLeft=1f;
				int chosenLabel=0;
				for(int j=0;j<numberOfSegments;j++){
					pLeft-=probs[j];
					if(p>pLeft && chosenLabel==0){
						chosenLabel=j+1;
					}
				}
				if(chosenLabel==0){chosenLabel=numberOfSegments;System.out.println("errorlad");}
				imageLabels[r][c]=chosenLabel;
			}}
		}
		return imageLabels;
	}

////-------HEirarchical methods
	//// MLL NOISE
	public static BufferedImage[] HpointSimulatedAnnealing(int iterations, int[][] image,float T0,List<float[]> lowLevelBeta, float alpha,int numberOfSegments,List<boolean[]> lowLevelCliquesToInclude, float noiseMean, float noiseSig){
		float T = T0;
		BufferedImage[] outputImages = new BufferedImage[iterations];
		//randomise image labels
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		//----
		System.out.println("starting iterations");
		
		for(int i=0;i<iterations;i++){
			imageLabels = HpointAnnealIteration(image,imageLabels, T, lowLevelBeta, alpha, numberOfSegments, lowLevelCliquesToInclude, noiseMean, noiseSig);
			T=alpha*T;
			outputImages[i] = ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
			System.out.println("iteration: "+i);
		}
		System.out.println("finished iterations");
		return(outputImages);
	}
	private  static int[][] HpointAnnealIteration(int[][] image,int[][] imageLabels,float T,List<float[]> lowLevelBeta, float alpha,int NumberOfSegments, List<boolean[]> lowLevelCliquesToInclude, float noiseMean, float noiseSig){
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			int[][] originalImageLabels = ImageManipulation.copyImage(imageLabels);
			List<List<List<int[]>>> cliques = getPointCliques(r, c, image, new boolean[]{true,true,false,false,false,false,false,false,false});
			imageLabels[r][c] = HandyFunctions.randomInt(1,NumberOfSegments);
			//imageLabels[r][c] = Math.abs(imageLabels[r][c]-2)+1; 
			boolean[] highLevelCliquesToInclude = new boolean[]{true,true,false,false,false,false,false,false,false};
			float[] highLevelBeta = new float[]{1f,1f,0f,0f,0f,0f,0f,0f,0f};
			float HighLeveldu = getPointEnergy(r,c,highLevelBeta, image,imageLabels,cliques,highLevelCliquesToInclude) - getPointEnergy(r,c,highLevelBeta, image,originalImageLabels,cliques,highLevelCliquesToInclude);
			//float LowLeveldu = getPointEnergy(r,c,lowLevelBeta.get(imageLabels[r][c]-1), image,imageLabels,cliques,lowLevelCliquesToInclude.get(imageLabels[r][c]-1)) - getPointEnergy(r,c,lowLevelBeta.get(originalImageLabels[r][c]-1), image,originalImageLabels,cliques,lowLevelCliquesToInclude.get(originalImageLabels[r][c]-1));
			float LowLeveldu = getHPointEnergy(r,c,lowLevelBeta.get(imageLabels[r][c]-1), image,imageLabels,lowLevelCliquesToInclude.get(imageLabels[r][c]-1),  noiseMean,  noiseSig) - getHPointEnergy(r,c,lowLevelBeta.get(originalImageLabels[r][c]-1), image,originalImageLabels,lowLevelCliquesToInclude.get(originalImageLabels[r][c]-1),  noiseMean,  noiseSig);
			//////---------------------------------------------------------------------------------------------------
			float du = 10* LowLeveldu + HighLeveldu;
			
			//System.out.println("LowLeveldu: " +LowLeveldu + "     HighLeveldu: " + HighLeveldu);
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
	public static float getHPointEnergy(int r, int c, float[] beta,int[][] image,int[][] imageLabels, boolean[] cliquesToInclude, float noiseMean, float noiseSig){
		float z=0f;
		List<List<List<int[]>>> cliques = getHPointCliques(r,c, image, imageLabels,cliquesToInclude);
		//note that image has 0 or 1's which are the image labels of the low level MRF 
		//for(List<List<int[]>> cliqueListByType : cliques){
		List<List<int[]>> cliqueListByType;
		for(int i=0;i<9;i++){
			cliqueListByType = cliques.get(i);
			if(cliquesToInclude[i]==true){
				for(List<int[]> clique : cliqueListByType){
					//z+=cliquePotential(clique,image,beta[i]);
					z+=HcliquePotential(2,clique,image,beta[i], noiseMean, noiseSig);
					//System.out.println("i:"+i+" new z:"+z);
				}
			}
			//i++;
		}
		return z;
	}
	public static List<List<List<int[]>>> getHPointCliques(int r, int c, int[][] image, int[][] imageLabels,boolean[] cliquesToInclude){
		//cliquesToInclude is a list of boolean values,  [b1,b2,...]
		List<List<List<int[]>>> cliqueList = new ArrayList<List<List<int[]>>>();
		List<List<int[]>> beta1 = new ArrayList<List<int[]>>();
		List<List<int[]>> beta2 = new ArrayList<List<int[]>>();
		List<List<int[]>> beta3 = new ArrayList<List<int[]>>();
		List<List<int[]>> beta4 = new ArrayList<List<int[]>>();
		List<List<int[]>> y1 = new ArrayList<List<int[]>>();
		List<List<int[]>> y2 = new ArrayList<List<int[]>>();
		List<List<int[]>> y3 = new ArrayList<List<int[]>>();
		List<List<int[]>> y4 = new ArrayList<List<int[]>>();
		List<List<int[]>> e1 = new ArrayList<List<int[]>>();
		
		if(cliquesToInclude[0]==true){
			if(r>=0 && c>=0 && r<image.length&&c+1 < image[0].length && imageLabels[r][c] == imageLabels[r][c+1]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r,c+1});
				beta1.add(points);
			}
			if(r>=0 && c-1>=0 && r<image.length&&c < image[0].length&& imageLabels[r][c] == imageLabels[r][c-1]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r,c-1});
				beta1.add(points);
			}
		}
		if(cliquesToInclude[1]==true){
			if(r-1>=0 && c>=0 && r<image.length&&c < image[0].length&& imageLabels[r][c] == imageLabels[r-1][c]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r-1,c});
				points.add(new int[]{r,c});
				beta2.add(points);
			}
			if(r>=0 && c>=0 && r+1<image.length&&c < image[0].length && imageLabels[r][c] == imageLabels[r+1][c]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r+1,c});
				beta2.add(points);
			}
		}
		if(cliquesToInclude[2]==true){
			if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length && imageLabels[r][c] == imageLabels[r+1][c-1]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r+1,c-1});
				points.add(new int[]{r,c});
				beta3.add(points);
			}
			if(r-1>=0 && c>=0 && r<image.length&&c+1 < image[0].length && imageLabels[r][c] == imageLabels[r-1][c+1]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r-1,c+1});
				beta3.add(points);
			}
		}
		if(cliquesToInclude[3]==true){
			if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length && imageLabels[r][c] == imageLabels[r+1][c+1]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r+1,c+1});
				beta4.add(points);
			}
			if(r-1>=0 && c-1>=0 && r<image.length&&c < image[0].length && imageLabels[r][c] == imageLabels[r-1][c-1]){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r-1,c-1});
				beta4.add(points);
			}
		}
		if(cliquesToInclude[4]==true){
			if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r+1,c+1});
				points.add(new int[]{r+1,c});
				y1.add(points);
			}
			if(r-1>=0 && c-1>=0 && r<image.length&&c < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r-1,c-1});
				points.add(new int[]{r,c});
				points.add(new int[]{r,c-1});
				y1.add(points);
			}
			if(r-1>=0 && c>=0 && r<image.length&&c+1< image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r-1,c});
				points.add(new int[]{r,c+1});
				points.add(new int[]{r,c});
				y1.add(points);
			}
		}
		if(cliquesToInclude[5]==true){
			if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r,c+1});
				points.add(new int[]{r+1,c});
				y2.add(points);
			}
			if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c-1});
				points.add(new int[]{r,c});
				points.add(new int[]{r+1,c-1});
				y2.add(points);
			}
			if(r-1>=0 && c>=0 && r<image.length&&c+1< image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r-1,c});
				points.add(new int[]{r-1,c+1});
				points.add(new int[]{r,c});
				y2.add(points);
			}
		}
		if(cliquesToInclude[6]==true){
			if(r-1>=0 && c>=0 && r<image.length&&c+1 < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r-1,c+1});
				points.add(new int[]{r,c+1});
				y3.add(points);
			}
			if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r+1,c-1});
				points.add(new int[]{r,c});
				points.add(new int[]{r+1,c});
				y3.add(points);
			}
			if(r-1>=0 && c-1>=0 && r<image.length&&c< image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c-1});
				points.add(new int[]{r-1,c});
				points.add(new int[]{r,c});
				y3.add(points);
			}
		}
		if(cliquesToInclude[7]==true){
			if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r,c+1});
				points.add(new int[]{r+1,c+1});
				y4.add(points);
			}
			if(r>=0 && c-1>=0 && r+1<image.length&&c< image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c-1});
				points.add(new int[]{r,c});
				points.add(new int[]{r+1,c});
				y4.add(points);
			}
			if(r-1>=0 && c-1>=0 && r<image.length&&c< image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r-1,c-1});
				points.add(new int[]{r-1,c});
				points.add(new int[]{r,c});
				y4.add(points);
			}
		}
		if(cliquesToInclude[8]==true){
			if(r>=0 && c>=0 && r+1<image.length&&c+1 < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c});
				points.add(new int[]{r,c+1});
				points.add(new int[]{r+1,c});
				points.add(new int[]{r+1,c+1});
				e1.add(points);
			}
			if(r>=0 && c-1>=0 && r+1<image.length&&c < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r,c-1});
				points.add(new int[]{r,c});
				points.add(new int[]{r+1,c-1});
				points.add(new int[]{r+1,c});
				e1.add(points);
			}
			if(r-1>=0 && c>=0 && r<image.length&&c+1 < image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r-1,c});
				points.add(new int[]{r-1,c+1});
				points.add(new int[]{r,c});
				points.add(new int[]{r,c+1});
				e1.add(points);
			}
			if(r-1>=0 && c-1>=0 && r<image.length&&c< image[0].length){
				List<int[]> points = new ArrayList<int[]>();
				points.add(new int[]{r-1,c-1});
				points.add(new int[]{r-1,c});
				points.add(new int[]{r,c-1});
				points.add(new int[]{r,c});
				e1.add(points);
			}
		}
		cliqueList.add(beta1);
		cliqueList.add(beta2);
		cliqueList.add(beta3);
		cliqueList.add(beta4);
		cliqueList.add(y1);
		cliqueList.add(y2);
		cliqueList.add(y3);
		cliqueList.add(y4);
		cliqueList.add(e1);
		//testGetCliques(cliqueList);
		return cliqueList;
	}	
	public static float HcliquePotential(int numberOfSegments, List<int[]> clique,int[][] imageLabels, float beta, float mean, float sig){
		// note 
		/*
		int n = clique.size();
		float probCliqueIsGood = 1f;
		int val = imageLabels[clique.get(0)[0]][clique.get(0)[1]];
		for(int i = 1; i< n; i++){
			probCliqueIsGood *= (1 / (Math.sqrt(2*Math.PI*sig*sig)))*Math.exp(-Math.pow((val-imageLabels[clique.get(i)[0]][clique.get(i)[1]]),2)/(2*sig*sig));
			//System.out.println(probCliqueIsGood);
		}
		if(n==0){System.out.println("error");}
		//return -1*beta*probCliqueIsGood;
		*/
		
		float[] vals = new float[numberOfSegments];
		for(int i = 0;i<numberOfSegments;i++){
			int intensity = i * 255 / (numberOfSegments-1);
			vals[i] = (int)(0.2989f*intensity+0.5870f*intensity+0.1140f*intensity);
		}
		float p=0f;
		List<float[]> probs = new ArrayList<float[]>(); //// list of probabilities that each pixel = each label,  ie probs.get(1)[0] is prob that the 2nd pixel in clique is labeled 1
		for(int j =0; j< clique.size();j++){
			probs.add(new float[numberOfSegments]);
			float[] tmp = probs.get(j);
			for(int i = 0;i<numberOfSegments;i++){
				tmp[i] = (float) ((1 / (Math.sqrt(2*Math.PI*sig*sig)))*Math.exp(-Math.pow((vals[i]-imageLabels[clique.get(i)[0]][clique.get(i)[1]]),2)/(2*sig*sig))); 
			}
			probs.set(j,tmp);
		}
		//normalise the probabilities
		for(int i = 0;i<numberOfSegments;i++){
			float probSum = 0f;
			for(float prob:probs.get(i)){
				probSum +=prob;
			}
			float[] tmp2 = probs.get(i);
			for(int j = 0;j<tmp2.length;j++){
				tmp2[j] /= probSum;
			}
			probs.set(i, tmp2);
		}
		//calc prob pixels in clique all the same
		float probSame=0f;
		for(int i=0;i<numberOfSegments;i++){
			float tmpProb=1f;
			for(int j=0;j<clique.size();j++){
				tmpProb *= probs.get(j)[i];
			}
			probSame+=tmpProb;
		}
		return -2*beta*probSame+beta;
	}
	
//////MLL NO NOISE
	public static BufferedImage[] HpointSimulatedAnnealingMLL(int iterations, int[][] image,float T0,List<float[]> lowLevelBeta, float alpha,int numberOfSegments,List<boolean[]> lowLevelCliquesToInclude){
		float T = T0;
		BufferedImage[] outputImages = new BufferedImage[iterations];
		//randomise image labels
		int[][] imageLabels = new int[image.length][image[0].length];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				imageLabels[r][c] = HandyFunctions.randomInt(1, numberOfSegments);
			}
		}
		//----
		System.out.println("starting iterations");
		
		for(int i=0;i<iterations;i++){
			imageLabels = HpointAnnealIterationMLL(image,imageLabels, T, lowLevelBeta, alpha, numberOfSegments, lowLevelCliquesToInclude);
			T=alpha*T;
			outputImages[i] = ImageManipulation.convertToImage(imageLabelToimg(imageLabels));
			System.out.println("iteration: "+i);
		}
		System.out.println("finished iterations");
		return(outputImages);
	}
	private  static int[][] HpointAnnealIterationMLL(int[][] image,int[][] imageLabels,float T,List<float[]> lowLevelBeta, float alpha,int NumberOfSegments, List<boolean[]> lowLevelCliquesToInclude){
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){
			int[][] originalImageLabels = ImageManipulation.copyImage(imageLabels);
			List<List<List<int[]>>> cliques = getPointCliques(r, c, image, new boolean[]{true,true,false,false,false,false,false,false,false});
			imageLabels[r][c] = HandyFunctions.randomInt(1,NumberOfSegments);
			//imageLabels[r][c] = Math.abs(imageLabels[r][c]-2)+1; 
			boolean[] highLevelCliquesToInclude = new boolean[]{true,true,false,false,false,false,false,false,false};
			float[] highLevelBeta = new float[]{1f,1f,0f,0f,0f,0f,0f,0f,0f};
			float HighLeveldu = getPointEnergy(r,c,highLevelBeta, image,imageLabels,cliques,highLevelCliquesToInclude) - getPointEnergy(r,c,highLevelBeta, image,originalImageLabels,cliques,highLevelCliquesToInclude);
			//float LowLeveldu = getPointEnergy(r,c,lowLevelBeta.get(imageLabels[r][c]-1), image,imageLabels,cliques,lowLevelCliquesToInclude.get(imageLabels[r][c]-1)) - getPointEnergy(r,c,lowLevelBeta.get(originalImageLabels[r][c]-1), image,originalImageLabels,cliques,lowLevelCliquesToInclude.get(originalImageLabels[r][c]-1));
			float LowLeveldu = getHPointEnergyMLL(r,c,lowLevelBeta.get(imageLabels[r][c]-1), image,imageLabels,lowLevelCliquesToInclude.get(imageLabels[r][c]-1)) - getHPointEnergyMLL(r,c,lowLevelBeta.get(originalImageLabels[r][c]-1), image,originalImageLabels,lowLevelCliquesToInclude.get(originalImageLabels[r][c]-1));
			//////---------------------------------------------------------------------------------------------------
			float du =  LowLeveldu + HighLeveldu;
			
			//System.out.println("LowLeveldu: " +LowLeveldu + "     HighLeveldu: " + HighLeveldu);
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
	public static float getHPointEnergyMLL(int r, int c, float[] beta,int[][] image,int[][] imageLabels, boolean[] cliquesToInclude){
		float z=0f;
		List<List<List<int[]>>> cliques = getHPointCliques(r,c, image, imageLabels,cliquesToInclude);
		//note that image has 0 or 1's which are the image labels of the low level MRF 
		//for(List<List<int[]>> cliqueListByType : cliques){
		List<List<int[]>> cliqueListByType;
		for(int i=0;i<9;i++){
			cliqueListByType = cliques.get(i);
			if(cliquesToInclude[i]==true){
				for(List<int[]> clique : cliqueListByType){
					//z+=cliquePotential(clique,image,beta[i]);
					z+=cliquePotential(clique,image,beta[i]);
					//System.out.println("i:"+i+" new z:"+z);
				}
			}
			//i++;
		}
		return z;
	}
	
	
	
}
