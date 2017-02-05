import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageSegmentationModel4 extends GibbsDistributionOrder2{
	//public static File outputFile; 
	private MainFrame gui;
	public ImageSegmentationModel4(MainFrame gui) {
		super(gui);
	    this.gui = gui;
	}
	public static void main(String[] args){
		List<boolean[]> lowLevelCliquesToInclude = new ArrayList<boolean[]>();
		lowLevelCliquesToInclude.add(new boolean[]{true,true,true,true,false,false,false,false,false});
		lowLevelCliquesToInclude.add(new boolean[]{true,true,true,true,false,false,false,false,false});
		List<float[]> lowLevelBeta = new ArrayList<float[]>();;
		lowLevelBeta.add(new float[]{-1f,-1f,1f,1f,0f,0f,0f,0f,0f,0f});
		lowLevelBeta.add(new float[]{1f,1f,-1f,-1f,0f,0f,0f,0f,0f,0f});
		int numberOfSegments=2;
		int iterations = 200;
		float T0 = 10f;
		float alpha = 0.99f;
		
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File("C:\\Users\\ollie\\Desktop\\texture.jpg"));
		} catch (IOException e) {}
		int[][] image = ImageManipulation.RGBtoIntensity(ImageManipulation.convertTo2DUsingGetRGB(bufferedImage));
		for(int r=0;r<image.length;r++){for(int c=0;c<image[0].length;c++){			
			if(image[r][c] <100){
				image[r][c] =1;
			}else{
				image[r][c] =0;
			}
		}}
		//runPointModel(image, iterations, T0, alpha,lowLevelBeta,numberOfSegments);
		
	}
	public static BufferedImage runPointModel(int[][] image, int iterations,float T0,float alpha,List<float[]> lowLevelBeta,int numberOfSegs, float noiseMean, float noiseSig, File outputFolder, String outputName){
		List<boolean[]> lowLevelCliquesToInclude = new ArrayList<boolean[]>();
		for(int i=0;i<lowLevelBeta.size();i++){
			boolean[] tmpbeta = new boolean[]{false,false,false,false,false,false,false,false,false};
			for(int j =0;j<lowLevelBeta.get(0).length;j++){
				if(lowLevelBeta.get(i)[j] != 0){tmpbeta[j] = true;}
			}
			lowLevelCliquesToInclude.add(tmpbeta);
		}
		BufferedImage[] outputs = HpointSimulatedAnnealing( iterations, image,T0,lowLevelBeta, alpha,numberOfSegs,lowLevelCliquesToInclude, noiseMean, noiseSig);
		//ImageManipulation.writeImageList(outputs, outputFile, "Output");
		ImageManipulation.writeImage(outputs[outputs.length-1], outputFolder, outputName);
		//return(outputs[outputs.length-1]);
		return outputs[outputs.length-1];
	}
	public static BufferedImage runPointModelMLL(int[][] image, int iterations,float T0,float alpha,List<float[]> lowLevelBeta,int numberOfSegs, File outputFolder, String outputName){
		List<boolean[]> lowLevelCliquesToInclude = new ArrayList<boolean[]>();
		for(int i=0;i<lowLevelBeta.size();i++){
			boolean[] tmpbeta = new boolean[]{false,false,false,false,false,false,false,false,false};
			for(int j =0;j<lowLevelBeta.get(0).length;j++){
				if(lowLevelBeta.get(i)[j] != 0){tmpbeta[j] = true;}
			}
			lowLevelCliquesToInclude.add(tmpbeta);
		}
		BufferedImage[] outputs = HpointSimulatedAnnealingMLL( iterations, image,T0,lowLevelBeta, alpha,numberOfSegs,lowLevelCliquesToInclude);
		//ImageManipulation.writeImageList(outputs, outputFile, "Output");
		ImageManipulation.writeImage(outputs[outputs.length-1], outputFolder, outputName);
		//return(outputs[outputs.length-1]);
		return outputs[outputs.length-1];
	}


}