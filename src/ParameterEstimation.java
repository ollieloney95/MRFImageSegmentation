import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class ParameterEstimation{
	public static float[][][][] R;
	public static int[] A;
	public static void main(String[] args){
		//test code
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File("C:\\Users\\ollie\\Desktop\\texture.png"));
		} catch (IOException e) {}
		int[][] image = ImageManipulation.RGBtoIntensity(ImageManipulation.convertTo2DUsingGetRGB(bufferedImage));
		MLLParameterEstimation1(image,2);
		
		float L = LogPseudoLikelihood(new float[]{0f,0f,-0.3333f,1f,0f,0f,0f,0f,0f}) / Math.abs(1.33333f);
		System.out.println("L:  "+L);
		float L2 = LogPseudoLikelihood(new float[]{0.5f,-0.5f,0.5f,-0.5f,0f,0f,0f,0f,0f})/2f;
		System.out.println("L2:  "+L2);
	}
	public static float[] optim(){
		float currentMin = 0;
		float[] beta= new float[4];
		for(int a=0;a<7;a++){for(int b=0;b<7;b++){for(int c=0;c<7;c++){for(int d=0;d<7;d++){
			if(LogPseudoLikelihood(new float[]{1f-a/3f,1f-b/3f,1f-c/3f,1f-d/3f,0f,0f,0f,0f,0f}) < (Math.abs((1f-a/3f))+ Math.abs((1f-b/3f))+ Math.abs((1f-c/3f))+ Math.abs((1f-d/3f))) * currentMin){
				currentMin = LogPseudoLikelihood(new float[]{1f-a/3f,1f-b/3f,1f-c/3f,1f-d/3f,0f,0f,0f,0f,0f});
				beta = new float[]{1f-a/3f,1f-b/3f,1f-c/3f,1f-d/3f};
			}
		}}}}
		System.out.println("beta[0]" + beta[0] +"beta[1]" + beta[1] +"beta[2]" + beta[2] +"beta[3]" + beta[3] );
		return(beta);
	}
	public static float[] MLLParameterEstimation1(int[][] imageLabels,int numberOfSegments){
		// we start by only looking at b1,b2,b3,b4
		float[] betas = new float[4];
		A = new int[4];
		List<List<List<int[]>>> cliqueList =  GibbsDistributionOrder2.getCliques(imageLabels,new boolean[]{true,true,true,true,false,false,false,false,false});
		/*
		int k=0;
		for(List<List<int[]>> cliqueListOfType : cliqueList){
			for(List<int[]> clique : cliqueListOfType){
				A[k] -= GibbsDistributionOrder2.cliquePotential(clique,imageLabels, 1f);
			}
			k++;
		}
		 */
		///*
			for(int r=0;r<imageLabels.length-1;r++){for(int c=0;c<imageLabels[0].length;c++){
				if(imageLabels[r][c] == imageLabels[r+1][c]){
					A[0] += 1;
				}else{
					A[0] -= 1;
				}
			}}
			for(int r=0;r<imageLabels.length;r++){for(int c=0;c<imageLabels[0].length-1;c++){
				if(imageLabels[r][c] == imageLabels[r][c+1]){
					A[1] += 1;
				}else{
					A[1] -= 1;
				}
			}}
			for(int r=0;r<imageLabels.length-1;r++){for(int c=0;c<imageLabels[0].length-1;c++){
				if(imageLabels[r][c] == imageLabels[r+1][c+1]){
					A[3] += 1;
				}else{
					A[3] -= 1;
				}
			}}
			for(int r=0;r<imageLabels.length-1;r++){for(int c=0;c<imageLabels[0].length-1;c++){
				if(imageLabels[r+1][c] == imageLabels[r][c+1]){
					A[2] += 1;
				}else{
					A[2] -= 1;
				}
			}}
	    //*/
		//test A  
		System.out.println("A1"+A[0]+"   A2"+A[1]+"   A3"+A[2]+"   A4"+A[3]);
		
		R = new float[2][4][imageLabels.length][imageLabels[0].length];
		for(int r=0;r<imageLabels.length;r++){for(int c=0;c<imageLabels[0].length;c++){
			List<List<List<int[]>>> pointCliques = GibbsDistributionOrder2.getPointCliques(r,c, imageLabels,new boolean[]{true,true,true,true,false,false,false,false,false});
			for(int kk = 0; kk<4;kk++){
				for(int M=0;M<2;M++){
					int[][] newIm = ImageManipulation.copyImage(imageLabels);
					newIm[r][c] = M;
					boolean[] cliquesToInclude = new boolean[]{false,false,false,false,false,false,false,false,false};
					float[] beta = new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f};
					cliquesToInclude[kk] = true;
					beta[kk] = 1f;
					R[M][kk][r][c] = -GibbsDistributionOrder2.getPointEnergy(r, c, beta,newIm,newIm,pointCliques, cliquesToInclude);
					//System.out.println("R[M][kk][r][c]"+R[M][kk][r][c]);
				}
			}
		}}
		//test R  
		System.out.println("R"+R[1][1][23][20]+"     R"+R[0][1][2][6]);
		//test log function float L = LogPseudoLikelihood(new float[]{1f,1f,1f,-0f,0f,0f,0f,0f,0f});
		//System.out.println("L:  "+L);
		
		double start[] = {0,0,0,0};
		int dim = 4;
		double eps = 1.0e-6;
		double scale = 0;
		int i;
		NMSimplex simplex = new NMSimplex(start,dim,eps,scale);
		for (i=0; i<dim; i++) {
			System.out.format("%f\n",start[i]);
		}
		
		return betas;
	}
	public static float LogPseudoLikelihood(float[] beta){
		float L=0;
		for(int k =0;k<A.length;k++){
			L+= A[k]*beta[k];
		}
		for(int r=0;r<R[0][0].length;r++){for(int c=0;c<R[0][0][0].length;c++){
			float temp = 0f;
			for(int G =0;G<2;G++){
				float temp2 = 0f;
				for(int k = 0;k<R[0].length;k++){
					temp2 += R[G][k][r][c] * beta[k];
				}
				temp += Math.exp(temp2);
			}
			L -= Math.log(temp);
		}}
		return L;
	}

}
