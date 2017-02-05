import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class TextureSampler extends GibbsDistributionOrder2{  //inherits from 2
	private MainFrame gui;
	public TextureSampler(MainFrame gui) {
		super(gui);
	    this.gui = gui;
	}
	public static File outputFile = new File("C:\\Users\\ollie\\Dropbox\\workspace\\MathProject\\test"); 
	public static void main(String[] args){
		//int numberOfSegments = 4;
		//float[] beta = new float[]{1f,1f,1f,-1f,0f,0f,0f,0f,0f};
		//runModel(10,0.5f,0.995f,beta,numberOfSegments);
		//boolean[] cliquesToInclude = {true,true,false,false,false,false,false,false,false};
		//System.out.println(getCliques(image,cliquesToInclude).get(0).get(20).get(1)[0]);
	}
	public BufferedImage runModel(int iterations,float T0,float alpha,float[] beta,int numberOfSegments){
		int[][] image = new int[100][100];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				image[r][c] = HandyFunctions.randomInt(0, numberOfSegments-1);
			}
		}
		boolean[] cliquesToInclude = {true,true,true,true,true,true,true,true,true};
		
		List<List<List<int[]>>> cliqueList = getCliques(image,cliquesToInclude);
		BufferedImage[] outputs = simulatedAnnealing(iterations, image, T0, beta, alpha,cliqueList, numberOfSegments);
		
		ImageManipulation.writeImageList(outputs, outputFile, "Output");
		this.gui.SetStatus("Finished Texture Synthesis");
		return(outputs[outputs.length-1]);
	}
	public BufferedImage runPointModel(int iterations,float T0,float alpha,float[] beta,int numberOfSegments){
		int[][] image = new int[100][100];
		for(int r=0;r<image.length;r++){
			for(int c=0;c<image[0].length;c++){
				image[r][c] = HandyFunctions.randomInt(0, numberOfSegments-1);
			}
		}
		boolean[] cliquesToInclude = {false,false,false,false,false,false,false,false,false};
		int i=0;
		for(float b : beta){
			if(b!=0){
				cliquesToInclude[i]=true;
			}
			i++;
		}
		BufferedImage[] outputs = pointSimulatedAnnealing(iterations, image, T0, beta, alpha, numberOfSegments,cliquesToInclude);
		
		ImageManipulation.writeImageList(outputs, outputFile, "Output");
		this.gui.SetStatus("Finished Texture Synthesis");
		return(outputs[outputs.length-1]);
	}
	public BufferedImage runSampleAlgorithm1(int iterations,float[] beta,int numberOfSegments){
		boolean[] cliquesToInclude = {false,false,false,false,false,false,false,false,false};
		int i=0;
		for(float b : beta){
			if(b!=0){
				cliquesToInclude[i]=true;
			}
			i++;
		}
		System.out.println("b1"+cliquesToInclude[0]+"b2"+cliquesToInclude[1]+"b3"+cliquesToInclude[2]+"b4"+cliquesToInclude[3]);
		System.out.println("b1"+beta[0]+"b2"+beta[1]+"b3"+beta[2]+"b4"+beta[3]);
		BufferedImage[] outputs = samplingAlgorithm1(iterations,beta,numberOfSegments,cliquesToInclude);
		this.gui.SetStatus("Finished Texture Synthesis");
		return(outputs[outputs.length-1]);
	}
	public BufferedImage runSampleAlgorithm2(int iterations,float[] beta,int numberOfSegments){
		boolean[] cliquesToInclude = {false,false,false,false,false,false,false,false,false};
		int i=0;
		for(float b : beta){
			if(b!=0){
				cliquesToInclude[i]=true;
			}
			i++;
		}
		//System.out.println("b1"+cliquesToInclude[0]+"b2"+cliquesToInclude[1]+"b3"+cliquesToInclude[2]+"b4"+cliquesToInclude[3]+"y1"+cliquesToInclude[4]+"y2"+cliquesToInclude[5]+"y3"+cliquesToInclude[6]+"y4"+cliquesToInclude[7]);
		BufferedImage[] outputs = samplingAlgorithm2(iterations,beta,numberOfSegments,cliquesToInclude);
		this.gui.SetStatus("Finished Texture Synthesis");
		return(outputs[outputs.length-1]);
	}

	public BufferedImage runHeirarchicalTextureSynthesis(List<float[]> betas,int iterations, int numberOfSegments, File outputFolder, String outputName){
		
		int[][] HighLevelLabels=samplingHighLevelMRF(iterations,new float[]{1f,1f,1f,1f,0,0,0,0,0},numberOfSegments);
		//
		//float[] betaSection1 = new float[]{-1f,-1f,1f,1f,0,0,0,0,0};
		//float[] betaSection2 = new float[]{1f,1f,-1f,-1f,0,0,0,0,0};
		//float[] betaSection3 = new float[]{1f,-1f,-1f,1f,0,0,0,0,0};
		//
		iterations = 100;
		List<int[][]> models = new ArrayList<int[][]>();
		
		//
		//models.add(samplingHighLevelMRF(iterations,betaSection1,2));
		//models.add(samplingHighLevelMRF(iterations,betaSection2,2));
		//models.add(samplingHighLevelMRF(iterations,betaSection3,3));
		//
		for(int i=0;i<betas.size();i++){
			//models.add(samplingHighLevelMRF(iterations,betas.get(i),numberOfSegments));
			models.add(samplingMLL(iterations,betas.get(i),numberOfSegments));
			System.out.println("ran samplingMLL");
			
		}
		
		int[][] output = new int[100][100];
		for(int r=0;r<100;r++){for(int c=0;c<100;c++){
			int label = HighLevelLabels[r][c]-1;
			output[r][c] = models.get(label)[r][c];
		}}
		//ImageManipulation.writeImage(ImageManipulation.convertToImage(imageLabelToimg(output)), new File("C:\\Users\\ollie\\Desktop"), "texture");
		//ImageManipulation.writeImage(ImageManipulation.convertToImage(imageLabelToimg(HighLevelLabels)), new File("C:\\Users\\ollie\\Desktop"), "textureCorrectLABELS");
		ImageManipulation.writeImage(ImageManipulation.convertToImage(imageLabelToimg(output)), outputFolder, outputName);
		ImageManipulation.writeImage(ImageManipulation.convertToImage(imageLabelToimg(HighLevelLabels)), outputFolder, outputName + "Labels");
		return(ImageManipulation.convertToImage(imageLabelToimg(output)));
	}

}
