import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JFrameImageSegmentationModel1 extends JFrame implements ActionListener{
	
	JFileChooser fc = new JFileChooser();
	JButton ButtonChooseFile = new JButton("Choose Base Image"); 
	JButton ButtonRun = new JButton("Run"); 
	JButton Sampler = new JButton("Sample"); 
	JButton ConfirmNumberOfSegments = new JButton("Confirm Number Of Segments"); 
	JButton ButtonChooseOutputLocation = new JButton("Choose Output Location"); 
	JButton ButtonChooseOutputName = new JButton("Choose Output Name"); 
	
	JLabel ChosenFile = new JLabel("no file chosen");
	JLabel ChosenOutputLocation = new JLabel("no location chosen");
	JLabel ChosenOutputName = new JLabel("output.jpg");
	JLabel AlphaLabel = new JLabel("Alpha");
	JLabel BetaLabel = new JLabel("Beta");
	JLabel T0Label = new JLabel("T0");
	JLabel IterationsLabel = new JLabel("Iterations");
	public static JLabel Stats = new JLabel("Waiting For input.....");
	JLabel numberOfSegmentsLabel = new JLabel("Number Of Segments");
	//ImageIcon im = new ImageIcon("C:\\Users\\Ollie Loney\\Desktop\\workspace\\Edge Analysis\\boat.jpg");
	//Image imm = getScaledImage(im.getImage(),100,100);
	public static List<float[]> sample = new ArrayList<float[]>();
	private static JLabel ChosenBaseImage = new JLabel(new ImageIcon(getScaledImage(new ImageIcon("C:\\Users\\ollie\\Dropbox\\workspace\\Edge Analysis\\nofilechosen.jpg").getImage(),100,100)));
	private static JLabel OutputImage = new JLabel(new ImageIcon(getScaledImage(new ImageIcon("C:\\Users\\ollie\\Dropbox\\workspace\\Edge Analysis\\nofilechosen.jpg").getImage(),300,300)));
	JTextField outputName = new JTextField("output");
	JTextField alpha = new JTextField("0.995");
	JTextField beta = new JTextField("0.5");
	JTextField T0 = new JTextField("0.5");
	JTextField Iterations = new JTextField("100");
	JTextField numberOfSegments = new JTextField("4");
	public static int NumberOfSegments;
	public static File BaseIm = null;
	public static File OutputLocation = null;
	public String OutputName = "output.jpg";
	private static final long serialVersionUID = 1L;
	public static JFrameImageSegmentationModel1 frame;
	public static void main(String[] args){
		frame = new JFrameImageSegmentationModel1();
		frame.setVisible(true);
		ChosenBaseImage.setIcon((new ImageIcon(getScaledImage(ImageManipulation.convertToImage(new int[100][100]),100,100))));
		OutputImage.setIcon((new ImageIcon(getScaledImage(ImageManipulation.convertToImage(new int[100][100]),300,300))));
		setUpSample();
	}
	static void setUpSample(){
		float[] flt = {0f,0f};
		for(int i=0;i<4;i++){
			sample.add(flt);
		}
	}
	private JFrameImageSegmentationModel1(){
		super("Edge Finder");
		setSize(600,600);
		setResizable(false);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		//JButton ButtonChooseFile = new JButton("Choose Base Image"); 
		//JButton ButtonRun = new JButton("run edge finder"); 
		
		//JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("C:\\Users\\Ollie Loney\\Desktop"));
		fc.setDialogTitle("Base Image");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		
		
		ButtonChooseFile.addActionListener(this);
		ButtonRun.addActionListener(this);
		ButtonChooseOutputLocation.addActionListener(this);
		ButtonChooseOutputName.addActionListener(this);
		Sampler.addActionListener(this);
		ConfirmNumberOfSegments.addActionListener(this);
		
		add(ButtonChooseFile);
		add(ChosenFile);
		add(ButtonChooseOutputLocation);
		add(ChosenOutputLocation);
		add(ButtonChooseOutputName);
		add(outputName);
		add(ChosenOutputName);
		add(IterationsLabel);
		add(Iterations);
		add(AlphaLabel);
		add(alpha);
		add(BetaLabel);
		add(beta);
		add(T0Label);
		add(T0);
		add(numberOfSegmentsLabel);
		add(numberOfSegments);
		add(ButtonRun);
		add(ChosenBaseImage);
		add(OutputImage);
		add(Stats);
		add(Sampler);
		add(ConfirmNumberOfSegments);
	}
	public static void setStats(String toSet){
		//frame.Stats.setText(toSet);
		System.out.println("changed stats");
	}
	public static void setOutputImage(BufferedImage toSet){
		OutputImage.setIcon((new ImageIcon(getScaledImage(toSet,300,300))));
		System.out.println("changed image");
		//OutputImage.setIcon(icon);
	}
	private static Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	@Override
	public void actionPerformed(ActionEvent e){
		String name = e.getActionCommand();
		
		if(name.equals("Choose Base Image")){
			System.out.println("choose file");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(fc.showOpenDialog(ButtonChooseFile) == JFileChooser.APPROVE_OPTION){	
			}
			BaseIm = fc.getSelectedFile();
			ChosenFile.setText(fc.getSelectedFile().getAbsolutePath());
			//ChosenBaseImage = new JLabel(new ImageIcon(fc.getSelectedFile().getAbsolutePath()));
			ChosenBaseImage.setIcon((new ImageIcon(getScaledImage(new ImageIcon(fc.getSelectedFile().getAbsolutePath()).getImage(),100,100))));
			
		}else if(name.equals("Run")){   ///note this is the bit to make work
			System.out.println("samppel:"+" mean1:"+sample.get(0)[0]+" mean1:"+sample.get(1)[0]+" mean1:"+sample.get(2)[0]);
			System.out.println("run");
			if(BaseIm != null && OutputLocation != null){
				float VALbeta = Float.valueOf(beta.getText());
				float VALt0 = Float.valueOf(T0.getText());
				float VALalpha = Float.valueOf(alpha.getText());
				int VALiterations = Integer.valueOf(Iterations.getText());
				int VALnumberofsegments = Integer.valueOf(numberOfSegments.getText());
				//note BaseIm is a file
				
				BufferedImage bufferedImage = null;
				try {
					bufferedImage = ImageIO.read(BaseIm);
				} catch (IOException ee) {}
				int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
				File OutputFile = new File(OutputLocation.getAbsolutePath()+"\\"+OutputName);
				//BufferedImage outputim = ImageSegmentationModel1.runAction(BaseIm,OutputFile,VALbeta,VALalpha,VALt0,VALiterations,VALnumberofsegments);
				BufferedImage outputim = ImageSegmentationModel1.runModel1(sample,VALiterations, image,VALt0,VALalpha,VALbeta,VALnumberofsegments,true);
				OutputImage.setIcon((new ImageIcon(getScaledImage(outputim,300,300))));
				//OutputImage.setIcon((new ImageIcon(getScaledImage(new ImageIcon(OutputLocation.getAbsolutePath()+"\\"+OutputName).getImage(),300,300))));
			}
			//OutputImage.setIcon((new ImageIcon(getScaledImage(new ImageIcon(OutputLocation.getAbsolutePath()+"\\"+OutputName).getImage(),300,300))));
		}else if(name.equals("Choose Output Location")){
			System.out.println("choose out loc");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(fc.showOpenDialog(ButtonChooseOutputLocation) == JFileChooser.APPROVE_OPTION){	
			}
			OutputLocation = fc.getSelectedFile();
			ChosenOutputLocation.setText(fc.getSelectedFile().getAbsolutePath());
			
		}else if(name.equals("Choose Output Name")){
			OutputName = outputName.getText()+".jpg";
			ChosenOutputName.setText(OutputName);
		}else if(name.equals("Sample")){
			if(NumberOfSegments>0){
				JFrameSquareSampler ThisSample = new JFrameSquareSampler();
				ThisSample.IDENTIFIER=0;
				ThisSample.main(null);
				System.out.println("openedSampler");
			}else{
				System.out.println("Not Specified number of segments");
			}
		}else if(name.equals("Confirm Number Of Segments")){
			NumberOfSegments = Integer.valueOf(numberOfSegments.getText());
		}
		
		
	}

	
}

