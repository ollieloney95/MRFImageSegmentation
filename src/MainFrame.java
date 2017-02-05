import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

public class MainFrame{
	
	File inputImageFile;
	File outputImageFile;
	File outputImageLocation;
	JLabel outputImageLocationLabel;
	JFrame frame;
	JList list;
	JButton run;
	static JLabel status;
	JButton ButtonChooseFile ;
	JButton ButtonChooseOutputFile;
	JPanel northPanel;
	JPanel southPanel;
	JPanel eastPanel;
	JPanel westPanel;
	JPanel centralPanel;
	Image outputImage;
	
	JLabel outputImageGraphic;
	JLabel title;
	//west panel stuff
	JFileChooser fc = new JFileChooser();
	BufferedImage inputImage;
	JLabel inputImageGraphic;
	
	//model 1 (edge finder)
	JLabel alphaLabel;
	JTextField alphaText;
	
	//model 2 (image seg supervised)
	JLabel betaLabel;
	JTextField betaText;
	JLabel iterationsLabel;
	JTextField iterationsText;
	JLabel T0Label;
	JTextField T0Text;
	JLabel numberOfSegmentsLabel;
	JTextField numberOfSegmentsText;
	JButton ButtonChooseSample ;
	public static List<float[]> sample = new ArrayList<float[]>();
	public static MainFrame gui;
	JTextField OutputFileName;
	JLabel OutputFileNameLabel;
	
	//model 3 (image seg unsipervised)
	JLabel supIterationsLabel;
	JTextField supIterationsText;
	JLabel subIterationsLabel;
	JTextField subIterationsText;
	
	//Texture Synthesis
	JTextField[] textureSynthesiserFields;
	JList textureSynthesiserMethodList;
	private String textureSynthesisModel;
	
	//H Texture Synthesis
	List<float[]> parameterFields;
	JList parameterMethodList;
	int segmentNumber = 1;
	JTextField[] globalParameters = new JTextField[2]; //ie this is iterations and number of MRF's in high level 
	String[] listParameterEntries= {"MRF Parameters1","MRF Parameters2","MRF Parameters3"};
	JScrollPane parameterMethodListScroller;
	JPanel MLLPanel;
	
	//H Texture segmentation
	JPanel mainParamPanel;
	JTextField[] mainParams = new JTextField[4]; //iterations,  num of MRFs,  T0,  alpha,  
	List<float[]> parameterFieldsSegmentation;
	JList parameterMethodListSegmentation;
	int segmentNumberSegmentation = 1;
	String[] listParameterEntriesSegmentation= {"MRF Parameters1","MRF Parameters2","MRF Parameters3"};
	JScrollPane parameterMethodListScrollerSegmentation;
	
	//add gaussian noise 
	JLabel[] addGaussianNoiseLabels = new JLabel[2];    //mean then sig
	JTextField[] addGaussianNoiseText = new JTextField[2];   //mean then sig
	
	
	public static void main(String[] args){
		gui = new MainFrame();
		gui.SetupUI();
	}
	public void SetupUI(){
		JFrame.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		run = new JButton("run");
		run.addActionListener(new MyRunListener());
		
		String[] listEntries = {"EdgeDetection","Supervised Auto-normal","Unsupervised Auto-normal","Texture synthesis","H Texture synthesis","H Texture sup seg MLL","H Texture sup seg noise", "Add Gaussian noise"};
		list = new JList(listEntries);
		JScrollPane modelScroller = new JScrollPane(list);
		modelScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		modelScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		list.setVisibleRowCount(4);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new MyModelListListener());
		ButtonChooseFile = new JButton("pick im"); 
		ButtonChooseFile.addActionListener(new MyChooseImageListener());
		ButtonChooseOutputFile = new JButton("pick Output Loc"); 
		ButtonChooseOutputFile.addActionListener(new MyChooseOutputImageListener());
		
		status = new JLabel("status...");
		
		northPanel = new JPanel();
		southPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		centralPanel = new JPanel();
		centralPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		eastPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		outputImageLocationLabel = new JLabel();
		inputImageGraphic = new JLabel();
		outputImageGraphic = new JLabel();
		eastPanel.setLayout(new BoxLayout(eastPanel,BoxLayout.Y_AXIS));
		westPanel.setLayout(new BoxLayout(westPanel,BoxLayout.Y_AXIS));
		centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.Y_AXIS));
		frame.getContentPane().add(BorderLayout.NORTH,northPanel);
		frame.getContentPane().add(BorderLayout.SOUTH,southPanel);
		frame.getContentPane().add(BorderLayout.EAST,eastPanel);
		frame.getContentPane().add(BorderLayout.WEST,westPanel);
		frame.getContentPane().add(BorderLayout.CENTER,centralPanel);
		outputImageLocationLabel.setMaximumSize(new Dimension(100, 25));
		outputImageLocationLabel.setText("Choose Output");
		OutputFileNameLabel = new JLabel("Output Name");
		OutputFileName = new JTextField("");
		OutputFileName.setMaximumSize(new Dimension(200, 25));
		title = new JLabel("Oliver Loney. Image Segmentation library");
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 30));
		
		eastPanel.add(run);
		northPanel.add(title);
		westPanel.add(modelScroller);
		westPanel.add(ButtonChooseFile);
		eastPanel.add(ButtonChooseOutputFile);
		eastPanel.add(outputImageLocationLabel);
		eastPanel.add(OutputFileNameLabel);
		eastPanel.add(OutputFileName);
		southPanel.add(status);
		westPanel.add(inputImageGraphic);
		eastPanel.add(outputImageGraphic);
		inputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(new int[100][100]),100,100))));
		outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(new int[100][100]),100,100))));
		
		frame.setSize(600,600);
		frame.setVisible(true);
	}
	public void ResetPanels(){
		//eastPanel.removeAll();
		outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(new int[100][100]),100,100))));
		outputImageFile=null;
		outputImageLocation=null;
		OutputFileName.setText("");
	}
	public void SetupModel1(){
		//sets up the central pane for model 1
		System.out.println("setup model 1");
		centralPanel.removeAll();
		centralPanel.repaint();
		alphaLabel = new JLabel("Alpha");
		alphaText = new JTextField(1);
		alphaText.setText("5000");
		alphaText.setMaximumSize(new Dimension(100, 25));
		centralPanel.add(alphaLabel);
		centralPanel.add(alphaText);
	}
	public void SetupModel2(){
		//sets up the central pane for model 1
		System.out.println("setup model 1");
		centralPanel.removeAll();
		centralPanel.repaint();
		alphaLabel = new JLabel("Alpha");
		alphaText = new JTextField(1);
		alphaText.setText("0.99");
		alphaText.setMaximumSize(new Dimension(100, 25));
		numberOfSegmentsLabel = new JLabel("# Segments");
		numberOfSegmentsText = new JTextField(1);
		numberOfSegmentsText.setText("4");
		numberOfSegmentsText.setMaximumSize(new Dimension(100, 25));
		betaLabel = new JLabel("Beta");
		betaText = new JTextField(1);
		betaText.setText("0.5");
		betaText.setMaximumSize(new Dimension(100, 25));
		T0Label = new JLabel("T0");
		T0Text = new JTextField(1);
		T0Text.setText("2");
		T0Text.setMaximumSize(new Dimension(100, 25));
		iterationsLabel = new JLabel("Iterations");
		iterationsText = new JTextField(1);
		iterationsText.setText("100");
		iterationsText.setMaximumSize(new Dimension(100, 25));
		ButtonChooseSample = new JButton("Choose Samples");
		ButtonChooseSample.addActionListener(new MyChooseSampleListener());
		centralPanel.add(alphaLabel);
		centralPanel.add(alphaText);
		centralPanel.add(betaLabel);
		centralPanel.add(betaText);
		centralPanel.add(T0Label);
		centralPanel.add(T0Text);
		centralPanel.add(iterationsLabel);
		centralPanel.add(iterationsText);
		centralPanel.add(numberOfSegmentsLabel);
		centralPanel.add(numberOfSegmentsText);
		centralPanel.add(ButtonChooseSample);
	}
	//Unsupervised Auto-normal
	public void SetupModel3(){
		//sets up the central pane for model 1
		System.out.println("setup Unsupervised Auto-normal model");
		centralPanel.removeAll();
		centralPanel.repaint();
		alphaLabel = new JLabel("Alpha");
		alphaText = new JTextField(1);
		alphaText.setText("0.96");
		alphaText.setMaximumSize(new Dimension(100, 25));
		numberOfSegmentsLabel = new JLabel("# Segments");
		numberOfSegmentsText = new JTextField(1);
		numberOfSegmentsText.setText("4");
		numberOfSegmentsText.setMaximumSize(new Dimension(100, 25));
		betaLabel = new JLabel("Beta");
		betaText = new JTextField(1);
		betaText.setText("0.5");
		betaText.setMaximumSize(new Dimension(100, 25));
		T0Label = new JLabel("T0");
		T0Text = new JTextField(1);
		T0Text.setText("2");
		T0Text.setMaximumSize(new Dimension(100, 25));
		supIterationsLabel = new JLabel("Sup Iterations");
		supIterationsText = new JTextField(1);
		supIterationsText.setText("10");
		supIterationsText.setMaximumSize(new Dimension(100, 25));
		subIterationsLabel = new JLabel("Sub Iterations");
		subIterationsText = new JTextField(1);
		subIterationsText.setText("100");
		subIterationsText.setMaximumSize(new Dimension(100, 25));
		centralPanel.add(alphaLabel);
		centralPanel.add(alphaText);
		centralPanel.add(betaLabel);
		centralPanel.add(betaText);
		centralPanel.add(T0Label);
		centralPanel.add(T0Text);
		centralPanel.add(supIterationsLabel);
		centralPanel.add(supIterationsText);
		centralPanel.add(subIterationsLabel);
		centralPanel.add(subIterationsText);
		centralPanel.add(numberOfSegmentsLabel);
		centralPanel.add(numberOfSegmentsText);
	}
	public void SetupTextureSynthesis(){
		//sets up the central pane for model 1
		JPanel MLLPanel = new JPanel();
		MLLPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		MLLPanel.setLayout(new GridLayout(12, 2));
		
		textureSynthesiserFields = new JTextField[11];
		System.out.println("setup model 1");
		centralPanel.removeAll();
		centralPanel.repaint();
		numberOfSegmentsLabel = new JLabel("# Segments");
		numberOfSegmentsText = new JTextField(1);
		numberOfSegmentsText.setText("4");
		numberOfSegmentsText.setMaximumSize(new Dimension(100, 25));
		JLabel beta1Label = new JLabel("Beta1");
		textureSynthesiserFields[0] = new JTextField(1);
		textureSynthesiserFields[0].setText("0.5");
		textureSynthesiserFields[0].setMaximumSize(new Dimension(100, 25));
		JLabel beta2Label = new JLabel("Beta2");
		textureSynthesiserFields[1] = new JTextField(1);
		textureSynthesiserFields[1].setText("0.5");
		textureSynthesiserFields[1].setMaximumSize(new Dimension(100, 25));
		JLabel beta3Label = new JLabel("Beta3");
		textureSynthesiserFields[2] = new JTextField(1);
		textureSynthesiserFields[2].setText("0.5");
		textureSynthesiserFields[2].setMaximumSize(new Dimension(100, 25));
		JLabel beta4Label = new JLabel("Beta4");
		textureSynthesiserFields[3] = new JTextField(1);
		textureSynthesiserFields[3].setText("0.5");
		textureSynthesiserFields[3].setMaximumSize(new Dimension(100, 25));
		
		JLabel y1Label = new JLabel("y1");
		textureSynthesiserFields[4] = new JTextField(1);
		textureSynthesiserFields[4].setText("0.5");
		textureSynthesiserFields[4].setMaximumSize(new Dimension(100, 25));
		JLabel y2Label = new JLabel("y2");
		textureSynthesiserFields[5] = new JTextField(1);
		textureSynthesiserFields[5].setText("0.5");
		textureSynthesiserFields[5].setMaximumSize(new Dimension(100, 25));
		JLabel y3Label = new JLabel("y3");
		textureSynthesiserFields[6] = new JTextField(1);
		textureSynthesiserFields[6].setText("0.5");
		textureSynthesiserFields[6].setMaximumSize(new Dimension(100, 25));
		JLabel y4Label = new JLabel("y4");
		textureSynthesiserFields[7] = new JTextField(1);
		textureSynthesiserFields[7].setText("0.5");
		textureSynthesiserFields[7].setMaximumSize(new Dimension(100, 25));
		
		JLabel e1Label = new JLabel("e1");
		textureSynthesiserFields[8] = new JTextField(1);
		textureSynthesiserFields[8].setText("0.5");
		textureSynthesiserFields[8].setMaximumSize(new Dimension(100, 25));
		
		String[] listMethodEntries = {"Metropolis Sampler","Gibbs Sampler","Simulated Annealing"};
		textureSynthesiserMethodList = new JList(listMethodEntries);
		JScrollPane textureSynthesiserMethodListScroller = new JScrollPane(textureSynthesiserMethodList);
		textureSynthesiserMethodListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textureSynthesiserMethodListScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textureSynthesiserMethodList.setVisibleRowCount(3);
		textureSynthesiserMethodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		textureSynthesiserMethodList.addListSelectionListener(new MyTextureSythesisListListener());
		
		JLabel MLLGraphic = new JLabel();
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(new int[100][50]),100,50))));
		BufferedImage MLLImage = null;
		try {
			MLLImage = ImageIO.read(new File("C:\\Users\\ollie\\Dropbox\\UNIVERSITY\\Year 3\\Project\\Latex Project\\Pictures\\MLL.png"));
		} catch (IOException e) {}
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(MLLImage,200,100))));
		iterationsLabel = new JLabel("Iterations");
		iterationsText = new JTextField(1);
		iterationsText.setText("100");
		iterationsText.setMaximumSize(new Dimension(100, 15));
		
		centralPanel.add(MLLGraphic);
		MLLPanel.add(new JLabel("Method"));
		MLLPanel.add(textureSynthesiserMethodListScroller);
		MLLPanel.add(beta1Label);
		MLLPanel.add(textureSynthesiserFields[0]);
		MLLPanel.add(beta2Label);
		MLLPanel.add(textureSynthesiserFields[1]);
		MLLPanel.add(beta3Label);
		MLLPanel.add(textureSynthesiserFields[2]);
		MLLPanel.add(beta4Label);
		MLLPanel.add(textureSynthesiserFields[3]);
		MLLPanel.add(y1Label);
		MLLPanel.add(textureSynthesiserFields[4]);
		MLLPanel.add(y2Label);
		MLLPanel.add(textureSynthesiserFields[5]);
		MLLPanel.add(y3Label);
		MLLPanel.add(textureSynthesiserFields[6]);
		MLLPanel.add(y4Label);
		MLLPanel.add(textureSynthesiserFields[7]);
		MLLPanel.add(e1Label);
		MLLPanel.add(textureSynthesiserFields[8]);
		MLLPanel.add(iterationsLabel);
		MLLPanel.add(iterationsText);
		MLLPanel.add(numberOfSegmentsLabel);
		MLLPanel.add(numberOfSegmentsText);
		centralPanel.add(MLLPanel);
		
		
	}
	public void SetupHTextureSynthesis(){
		//sets up the central pane for model 1
		System.out.println("setup model HTextureSynthesis");
		centralPanel.removeAll();
		centralPanel.repaint();
		
		parameterFields = new ArrayList<float[]>();
		parameterFields.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f,50f});
		parameterFields.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f,50f});
		parameterFields.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f,50f});
		
		JLabel numberOfMRFs = new JLabel("numberOfMRFs");
		globalParameters[1] = new JTextField(1);
		globalParameters[1].setText("3");
		globalParameters[1].setMaximumSize(new Dimension(100, 25));
		globalParameters[1].addActionListener(new NumberOfMRFsListener());
		
		JLabel highLevelMRFIterations = new JLabel("highLevelMRFIterations");
		globalParameters[0] = new JTextField(1);
		globalParameters[0].setText("50");
		globalParameters[0].setMaximumSize(new Dimension(100, 25));
		
		parameterMethodList = new JList(listParameterEntries);
		parameterMethodListScroller = new JScrollPane(parameterMethodList);
		parameterMethodListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		parameterMethodListScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		parameterMethodList.setVisibleRowCount(3);
		parameterMethodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		parameterMethodList.addListSelectionListener(new MyParameterMethodListListener());
		textureSynthesiserFields = new JTextField[11];
		MLLPanel = new JPanel();
		MLLPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		MLLPanel.setLayout(new GridLayout(14, 2));
		JLabel beta1Label = new JLabel("Beta1");
		textureSynthesiserFields[0] = new JTextField(1);
		textureSynthesiserFields[0].setText("0");
		textureSynthesiserFields[0].setMaximumSize(new Dimension(100, 25));
		JLabel beta2Label = new JLabel("Beta2");
		textureSynthesiserFields[1] = new JTextField(1);
		textureSynthesiserFields[1].setText("0");
		textureSynthesiserFields[1].setMaximumSize(new Dimension(100, 25));
		JLabel beta3Label = new JLabel("Beta3");
		textureSynthesiserFields[2] = new JTextField(1);
		textureSynthesiserFields[2].setText("0");
		textureSynthesiserFields[2].setMaximumSize(new Dimension(100, 25));
		JLabel beta4Label = new JLabel("Beta4");
		textureSynthesiserFields[3] = new JTextField(1);
		textureSynthesiserFields[3].setText("0");
		textureSynthesiserFields[3].setMaximumSize(new Dimension(100, 25));
		
		JLabel y1Label = new JLabel("y1");
		textureSynthesiserFields[4] = new JTextField(1);
		textureSynthesiserFields[4].setText("0");
		textureSynthesiserFields[4].setMaximumSize(new Dimension(100, 25));
		JLabel y2Label = new JLabel("y2");
		textureSynthesiserFields[5] = new JTextField(1);
		textureSynthesiserFields[5].setText("0");
		textureSynthesiserFields[5].setMaximumSize(new Dimension(100, 25));
		JLabel y3Label = new JLabel("y3");
		textureSynthesiserFields[6] = new JTextField(1);
		textureSynthesiserFields[6].setText("0");
		textureSynthesiserFields[6].setMaximumSize(new Dimension(100, 25));
		JLabel y4Label = new JLabel("y4");
		textureSynthesiserFields[7] = new JTextField(1);
		textureSynthesiserFields[7].setText("0");
		textureSynthesiserFields[7].setMaximumSize(new Dimension(100, 25));
		JLabel e1Label = new JLabel("e1");
		textureSynthesiserFields[8] = new JTextField(1);
		textureSynthesiserFields[8].setText("0");
		textureSynthesiserFields[8].setMaximumSize(new Dimension(100, 25));
		JLabel MLabel = new JLabel("number of segments");
		textureSynthesiserFields[9] = new JTextField(1);
		textureSynthesiserFields[9].setText("2");
		textureSynthesiserFields[9].setMaximumSize(new Dimension(100, 25));
		JLabel subIterationsLabel = new JLabel("iterations");
		textureSynthesiserFields[10] = new JTextField(1);
		textureSynthesiserFields[10].setText("50");
		textureSynthesiserFields[10].setMaximumSize(new Dimension(100, 25));
		
		JLabel MLLGraphic = new JLabel();
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(new int[100][50]),100,50))));
		BufferedImage MLLImage = null;
		try {
			MLLImage = ImageIO.read(new File("C:\\Users\\ollie\\Dropbox\\UNIVERSITY\\Year 3\\Project\\Latex Project\\Pictures\\MLL.png"));
		} catch (IOException e) {}
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(MLLImage,200,100))));
		
		centralPanel.add(highLevelMRFIterations);
		centralPanel.add(globalParameters[0]);
		centralPanel.add(numberOfMRFs);
		centralPanel.add(globalParameters[1]);
		centralPanel.add(MLLGraphic);
		MLLPanel.add(new JLabel("Method"));
		MLLPanel.add(parameterMethodListScroller);
		MLLPanel.add(beta1Label);
		MLLPanel.add(textureSynthesiserFields[0]);
		MLLPanel.add(beta2Label);
		MLLPanel.add(textureSynthesiserFields[1]);
		MLLPanel.add(beta3Label);
		MLLPanel.add(textureSynthesiserFields[2]);
		MLLPanel.add(beta4Label);
		MLLPanel.add(textureSynthesiserFields[3]);
		MLLPanel.add(y1Label);
		MLLPanel.add(textureSynthesiserFields[4]);
		MLLPanel.add(y2Label);
		MLLPanel.add(textureSynthesiserFields[5]);
		MLLPanel.add(y3Label);
		MLLPanel.add(textureSynthesiserFields[6]);
		MLLPanel.add(y4Label);
		MLLPanel.add(textureSynthesiserFields[7]);
		MLLPanel.add(e1Label);
		MLLPanel.add(textureSynthesiserFields[8]);
		MLLPanel.add(MLabel);
		MLLPanel.add(textureSynthesiserFields[9]);
		MLLPanel.add(subIterationsLabel);
		MLLPanel.add(textureSynthesiserFields[10]);
		centralPanel.add(MLLPanel);
	}
	public void SetupHTextureSupervisedSegmentationMLL(){
		//sets up the central pane for model 1
		System.out.println("setup model HTexture Supervised Segmentation MLL");
		centralPanel.removeAll();
		centralPanel.repaint();
		
		parameterFieldsSegmentation = new ArrayList<float[]>();
		parameterFieldsSegmentation.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f});
		parameterFieldsSegmentation.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f});
		parameterFieldsSegmentation.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f});
		
		mainParams = new JTextField[4];
		JLabel numberOfMRFs = new JLabel("numberOfMRFs");
		mainParams[1] = new JTextField(1);
		mainParams[1].setText("3");
		mainParams[1].setMaximumSize(new Dimension(100, 25));
		mainParams[1].addActionListener(new NumberOfMRFsSegmentationListener());
		
		JLabel highLevelMRFIterations = new JLabel("Iterations");
		mainParams[0] = new JTextField(1);
		mainParams[0].setText("50");
		mainParams[0].setMaximumSize(new Dimension(100, 25));
		
		JLabel T0 = new JLabel("T0");
		mainParams[2] = new JTextField(1);
		mainParams[2].setText("5");
		mainParams[2].setMaximumSize(new Dimension(100, 25));
		
		JLabel alpha = new JLabel("Alpha");
		mainParams[3] = new JTextField(1);
		mainParams[3].setText("0.99");
		mainParams[3].setMaximumSize(new Dimension(100, 25));
		
		parameterMethodListSegmentation = new JList(listParameterEntriesSegmentation);
		parameterMethodListScrollerSegmentation = new JScrollPane(parameterMethodListSegmentation);
		parameterMethodListScrollerSegmentation.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		parameterMethodListScrollerSegmentation.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		parameterMethodListSegmentation.setVisibleRowCount(3);
		parameterMethodListSegmentation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		parameterMethodListSegmentation.addListSelectionListener(new MyParameterMethodListSegmentationListener());
		textureSynthesiserFields = new JTextField[10];
		MLLPanel = new JPanel();
		MLLPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		MLLPanel.setLayout(new GridLayout(14, 2));
		JPanel mainParamPanel = new JPanel();
		mainParamPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		mainParamPanel.setLayout(new GridLayout(4, 2));
		JLabel beta1Label = new JLabel("Beta1");
		textureSynthesiserFields[0] = new JTextField(1);
		textureSynthesiserFields[0].setText("0");
		textureSynthesiserFields[0].setMaximumSize(new Dimension(100, 25));
		JLabel beta2Label = new JLabel("Beta2");
		textureSynthesiserFields[1] = new JTextField(1);
		textureSynthesiserFields[1].setText("0");
		textureSynthesiserFields[1].setMaximumSize(new Dimension(100, 25));
		JLabel beta3Label = new JLabel("Beta3");
		textureSynthesiserFields[2] = new JTextField(1);
		textureSynthesiserFields[2].setText("0");
		textureSynthesiserFields[2].setMaximumSize(new Dimension(100, 25));
		JLabel beta4Label = new JLabel("Beta4");
		textureSynthesiserFields[3] = new JTextField(1);
		textureSynthesiserFields[3].setText("0");
		textureSynthesiserFields[3].setMaximumSize(new Dimension(100, 25));
		
		JLabel y1Label = new JLabel("y1");
		textureSynthesiserFields[4] = new JTextField(1);
		textureSynthesiserFields[4].setText("0");
		textureSynthesiserFields[4].setMaximumSize(new Dimension(100, 25));
		JLabel y2Label = new JLabel("y2");
		textureSynthesiserFields[5] = new JTextField(1);
		textureSynthesiserFields[5].setText("0");
		textureSynthesiserFields[5].setMaximumSize(new Dimension(100, 25));
		JLabel y3Label = new JLabel("y3");
		textureSynthesiserFields[6] = new JTextField(1);
		textureSynthesiserFields[6].setText("0");
		textureSynthesiserFields[6].setMaximumSize(new Dimension(100, 25));
		JLabel y4Label = new JLabel("y4");
		textureSynthesiserFields[7] = new JTextField(1);
		textureSynthesiserFields[7].setText("0");
		textureSynthesiserFields[7].setMaximumSize(new Dimension(100, 25));
		JLabel e1Label = new JLabel("e1");
		textureSynthesiserFields[8] = new JTextField(1);
		textureSynthesiserFields[8].setText("0");
		textureSynthesiserFields[8].setMaximumSize(new Dimension(100, 25));
		JLabel MLabel = new JLabel("number of segments");
		textureSynthesiserFields[9] = new JTextField(1);
		textureSynthesiserFields[9].setText("2");
		textureSynthesiserFields[9].setMaximumSize(new Dimension(100, 25));
		//JLabel subIterationsLabel = new JLabel("iterations");
		//textureSynthesiserFields[10] = new JTextField(1);
		//textureSynthesiserFields[10].setText("50");
		//textureSynthesiserFields[10].setMaximumSize(new Dimension(100, 25));
		
		JLabel MLLGraphic = new JLabel();
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(new int[100][50]),100,50))));
		BufferedImage MLLImage = null;
		try {
			MLLImage = ImageIO.read(new File("C:\\Users\\ollie\\Dropbox\\UNIVERSITY\\Year 3\\Project\\Latex Project\\Pictures\\MLL.png"));
		} catch (IOException e) {}
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(MLLImage,200,100))));
		
		mainParamPanel.add(highLevelMRFIterations);
		mainParamPanel.add(mainParams[0]);
		mainParamPanel.add(numberOfMRFs);
		mainParamPanel.add(mainParams[1]);
		mainParamPanel.add(T0);
		mainParamPanel.add(mainParams[2]);
		mainParamPanel.add(alpha);
		mainParamPanel.add(mainParams[3]);
		centralPanel.add(mainParamPanel);
		centralPanel.add(MLLGraphic);
		MLLPanel.add(new JLabel("Method"));
		MLLPanel.add(parameterMethodListScrollerSegmentation);
		MLLPanel.add(beta1Label);
		MLLPanel.add(textureSynthesiserFields[0]);
		MLLPanel.add(beta2Label);
		MLLPanel.add(textureSynthesiserFields[1]);
		MLLPanel.add(beta3Label);
		MLLPanel.add(textureSynthesiserFields[2]);
		MLLPanel.add(beta4Label);
		MLLPanel.add(textureSynthesiserFields[3]);
		MLLPanel.add(y1Label);
		MLLPanel.add(textureSynthesiserFields[4]);
		MLLPanel.add(y2Label);
		MLLPanel.add(textureSynthesiserFields[5]);
		MLLPanel.add(y3Label);
		MLLPanel.add(textureSynthesiserFields[6]);
		MLLPanel.add(y4Label);
		MLLPanel.add(textureSynthesiserFields[7]);
		MLLPanel.add(e1Label);
		MLLPanel.add(textureSynthesiserFields[8]);
		MLLPanel.add(MLabel);
		MLLPanel.add(textureSynthesiserFields[9]);
		//MLLPanel.add(subIterationsLabel);
		//MLLPanel.add(textureSynthesiserFields[10]);
		centralPanel.add(MLLPanel);
	}
	public void SetupHTextureSupervisedSegmentationNoise(){
		//sets up the central pane for model 1
		System.out.println("setup model HTexture Supervised Segmentation Noise");
		centralPanel.removeAll();
		centralPanel.repaint();
		
		parameterFieldsSegmentation = new ArrayList<float[]>();
		parameterFieldsSegmentation.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f});
		parameterFieldsSegmentation.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f});
		parameterFieldsSegmentation.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f});
		
		
		mainParams = new JTextField[6];
		JLabel numberOfMRFs = new JLabel("numberOfMRFs");
		mainParams[1] = new JTextField(1);
		mainParams[1].setText("3");
		mainParams[1].setMaximumSize(new Dimension(100, 25));
		mainParams[1].addActionListener(new NumberOfMRFsSegmentationListener());
		
		JLabel highLevelMRFIterations = new JLabel("Iterations");
		mainParams[0] = new JTextField(1);
		mainParams[0].setText("50");
		mainParams[0].setMaximumSize(new Dimension(100, 25));
		
		JLabel T0 = new JLabel("T0");
		mainParams[2] = new JTextField(1);
		mainParams[2].setText("5");
		mainParams[2].setMaximumSize(new Dimension(100, 25));
		
		JLabel alpha = new JLabel("Alpha");
		mainParams[3] = new JTextField(1);
		mainParams[3].setText("0.99");
		mainParams[3].setMaximumSize(new Dimension(100, 25));
		
		JLabel noiseMean = new JLabel("Noise mean");
		mainParams[4] = new JTextField(1);
		mainParams[4].setText("0");
		mainParams[4].setMaximumSize(new Dimension(100, 25));
		
		JLabel noiseSig = new JLabel("noise sd");
		mainParams[5] = new JTextField(1);
		mainParams[5].setText("20");
		mainParams[5].setMaximumSize(new Dimension(100, 25));
		
		parameterMethodListSegmentation = new JList(listParameterEntriesSegmentation);
		parameterMethodListScrollerSegmentation = new JScrollPane(parameterMethodListSegmentation);
		parameterMethodListScrollerSegmentation.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		parameterMethodListScrollerSegmentation.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		parameterMethodListSegmentation.setVisibleRowCount(3);
		parameterMethodListSegmentation.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		parameterMethodListSegmentation.addListSelectionListener(new MyParameterMethodListSegmentationListener());
		textureSynthesiserFields = new JTextField[10];
		MLLPanel = new JPanel();
		MLLPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		MLLPanel.setLayout(new GridLayout(14, 2));
		JPanel mainParamPanel = new JPanel();
		mainParamPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
		mainParamPanel.setLayout(new GridLayout(6, 2));
		JLabel beta1Label = new JLabel("Beta1");
		textureSynthesiserFields[0] = new JTextField(1);
		textureSynthesiserFields[0].setText("0");
		textureSynthesiserFields[0].setMaximumSize(new Dimension(100, 25));
		JLabel beta2Label = new JLabel("Beta2");
		textureSynthesiserFields[1] = new JTextField(1);
		textureSynthesiserFields[1].setText("0");
		textureSynthesiserFields[1].setMaximumSize(new Dimension(100, 25));
		JLabel beta3Label = new JLabel("Beta3");
		textureSynthesiserFields[2] = new JTextField(1);
		textureSynthesiserFields[2].setText("0");
		textureSynthesiserFields[2].setMaximumSize(new Dimension(100, 25));
		JLabel beta4Label = new JLabel("Beta4");
		textureSynthesiserFields[3] = new JTextField(1);
		textureSynthesiserFields[3].setText("0");
		textureSynthesiserFields[3].setMaximumSize(new Dimension(100, 25));
		
		JLabel y1Label = new JLabel("y1");
		textureSynthesiserFields[4] = new JTextField(1);
		textureSynthesiserFields[4].setText("0");
		textureSynthesiserFields[4].setMaximumSize(new Dimension(100, 25));
		JLabel y2Label = new JLabel("y2");
		textureSynthesiserFields[5] = new JTextField(1);
		textureSynthesiserFields[5].setText("0");
		textureSynthesiserFields[5].setMaximumSize(new Dimension(100, 25));
		JLabel y3Label = new JLabel("y3");
		textureSynthesiserFields[6] = new JTextField(1);
		textureSynthesiserFields[6].setText("0");
		textureSynthesiserFields[6].setMaximumSize(new Dimension(100, 25));
		JLabel y4Label = new JLabel("y4");
		textureSynthesiserFields[7] = new JTextField(1);
		textureSynthesiserFields[7].setText("0");
		textureSynthesiserFields[7].setMaximumSize(new Dimension(100, 25));
		JLabel e1Label = new JLabel("e1");
		textureSynthesiserFields[8] = new JTextField(1);
		textureSynthesiserFields[8].setText("0");
		textureSynthesiserFields[8].setMaximumSize(new Dimension(100, 25));
		JLabel MLabel = new JLabel("number of segments");
		textureSynthesiserFields[9] = new JTextField(1);
		textureSynthesiserFields[9].setText("2");
		textureSynthesiserFields[9].setMaximumSize(new Dimension(100, 25));
		//JLabel subIterationsLabel = new JLabel("iterations");
		//textureSynthesiserFields[10] = new JTextField(1);
		//textureSynthesiserFields[10].setText("50");
		//textureSynthesiserFields[10].setMaximumSize(new Dimension(100, 25));
		
		JLabel MLLGraphic = new JLabel();
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(new int[100][50]),100,50))));
		BufferedImage MLLImage = null;
		try {
			MLLImage = ImageIO.read(new File("C:\\Users\\ollie\\Dropbox\\UNIVERSITY\\Year 3\\Project\\Latex Project\\Pictures\\MLL.png"));
		} catch (IOException e) {}
		MLLGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(MLLImage,200,100))));
		
		mainParamPanel.add(highLevelMRFIterations);
		mainParamPanel.add(mainParams[0]);
		mainParamPanel.add(numberOfMRFs);
		mainParamPanel.add(mainParams[1]);
		mainParamPanel.add(T0);
		mainParamPanel.add(mainParams[2]);
		mainParamPanel.add(alpha);
		mainParamPanel.add(mainParams[3]);
		mainParamPanel.add(noiseMean);
		mainParamPanel.add(mainParams[4]);
		mainParamPanel.add(noiseSig);
		mainParamPanel.add(mainParams[5]);
		centralPanel.add(mainParamPanel);
		centralPanel.add(MLLGraphic);
		MLLPanel.add(new JLabel("Method"));
		MLLPanel.add(parameterMethodListScrollerSegmentation);
		MLLPanel.add(beta1Label);
		MLLPanel.add(textureSynthesiserFields[0]);
		MLLPanel.add(beta2Label);
		MLLPanel.add(textureSynthesiserFields[1]);
		MLLPanel.add(beta3Label);
		MLLPanel.add(textureSynthesiserFields[2]);
		MLLPanel.add(beta4Label);
		MLLPanel.add(textureSynthesiserFields[3]);
		MLLPanel.add(y1Label);
		MLLPanel.add(textureSynthesiserFields[4]);
		MLLPanel.add(y2Label);
		MLLPanel.add(textureSynthesiserFields[5]);
		MLLPanel.add(y3Label);
		MLLPanel.add(textureSynthesiserFields[6]);
		MLLPanel.add(y4Label);
		MLLPanel.add(textureSynthesiserFields[7]);
		MLLPanel.add(e1Label);
		MLLPanel.add(textureSynthesiserFields[8]);
		MLLPanel.add(MLabel);
		MLLPanel.add(textureSynthesiserFields[9]);
		//MLLPanel.add(subIterationsLabel);
		//MLLPanel.add(textureSynthesiserFields[10]);
		centralPanel.add(MLLPanel);
	}
	public void SetupAddGaussianNoise(){
		//sets up the central pane for model 1
				System.out.println("setup model 1");
				centralPanel.removeAll();
				centralPanel.repaint();
				
				MLLPanel = new JPanel();
				MLLPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
				MLLPanel.setLayout(new GridLayout(14, 2));
				
				addGaussianNoiseLabels[0] = new JLabel("Mean");
				addGaussianNoiseText[0] = new JTextField(1);
				addGaussianNoiseText[0].setText("0");
				addGaussianNoiseText[0].setMaximumSize(new Dimension(100, 25));
				
				addGaussianNoiseLabels[1] = new JLabel("SD");
				addGaussianNoiseText[1] = new JTextField(1);
				addGaussianNoiseText[1].setText("30");
				addGaussianNoiseText[1].setMaximumSize(new Dimension(100, 25));
				
				MLLPanel.add(addGaussianNoiseLabels[0]);
				MLLPanel.add(addGaussianNoiseText[0]);
				MLLPanel.add(addGaussianNoiseLabels[1]);
				MLLPanel.add(addGaussianNoiseText[1]);
				centralPanel.add(MLLPanel);
	}
	
	
	public void paintComponent(Graphics g){
		g.drawImage(inputImage,100,100,null);
	}
	public void SetStatus(String toSet){
		status.setText(toSet);
	}
	class MyRunListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			String selection = (String)list.getSelectedValue();
			if(selection == "EdgeDetection"){
				if(OutputFileName.getText() != ""){outputImageFile = new File(outputImageLocation.getAbsolutePath()+"\\"+OutputFileName);}
				if(inputImageFile == null){status.setText("set input image");
				}else if(outputImageFile == null){status.setText("set output file");
				}else{
					BufferedImage bufferedImage = null;
					int VALalpha = Integer.valueOf(alphaText.getText());
					try {
						bufferedImage = ImageIO.read(inputImageFile);
					} catch (IOException ee) {}
					int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
					BufferedImage outputim = EdgeFinderModel1.run(image,VALalpha);
					outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(outputim,100,100))));
				}
			}
			if(selection == "Add Gaussian noise"){
				if(OutputFileName.getText() != ""){outputImageFile = new File(outputImageLocation.getAbsolutePath()+"\\"+OutputFileName);}
				if(inputImageFile == null){status.setText("set input image");
				}else if(outputImageFile == null){status.setText("set output file");
				}else{
					float VALmean = Float.valueOf(addGaussianNoiseText[0].getText());
					float VALsig = Float.valueOf(addGaussianNoiseText[1].getText());
					int[][] image = ImageManipulation.readImage(inputImageFile);
					image = ImageManipulation.addNoise(image, VALmean, VALsig);
					image = ImageManipulation.IntensityToRGB(image);
					ImageManipulation.writeImage(ImageManipulation.convertToImage(image),new File("C:\\Users\\ollie\\Desktop"),"noise");
					outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(ImageManipulation.convertToImage(image),100,100))));
				}
			}
			if(selection == "Supervised Auto-normal"){
				if(OutputFileName.getText() != ""){outputImageFile = new File(outputImageLocation.getAbsolutePath()+"\\"+OutputFileName);}
				if(inputImageFile == null){status.setText("set input image");
				}else if(outputImageFile == null){status.setText("set output file");
				}else{
					ImageSegmentationModel1.SetOutputFile(outputImageLocation);
					BufferedImage bufferedImage = null;
					float VALalpha = Float.valueOf(alphaText.getText());
					float VALbeta = Float.valueOf(betaText.getText());
					int VALiterations = Integer.valueOf(iterationsText.getText());
					float VALT0 = Float.valueOf(T0Text.getText());
					int VALnumberOfSegments = Integer.valueOf(numberOfSegmentsText.getText());
					try {
						bufferedImage = ImageIO.read(inputImageFile);
					} catch (IOException ee) {}
					int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
					BufferedImage outputim = ImageSegmentationModel1.runModel1(sample,VALiterations, image,VALT0,VALalpha,VALbeta,VALnumberOfSegments,true);
					//BufferedImage outputim = ImageSegmentationModel1.run(image,VALalpha);
					outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(outputim,100,100))));
				}
			}
			if(selection == "Unsupervised Auto-normal"){
				if(OutputFileName.getText() != ""){outputImageFile = new File(outputImageLocation.getAbsolutePath()+"\\"+OutputFileName);}
				if(inputImageFile == null){status.setText("set input image");
				}else if(outputImageFile == null){status.setText("set output file");
				}else{
					ImageSegmentationModel2.SetOutputFile(outputImageLocation);
					BufferedImage bufferedImage = null;
					float VALalpha = Float.valueOf(alphaText.getText());
					float VALbeta = Float.valueOf(betaText.getText());
					int VALsupIterations = Integer.valueOf(supIterationsText.getText());
					int VALsubIterations = Integer.valueOf(subIterationsText.getText());
					float VALT0 = Float.valueOf(T0Text.getText());
					int VALnumberOfSegments = Integer.valueOf(numberOfSegmentsText.getText());
					try {
						bufferedImage = ImageIO.read(inputImageFile);
					} catch (IOException ee) {}
					int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
					BufferedImage outputim = ImageManipulation.convertToImage(ImageManipulation.correctImageLabelsAv(image,  ImageSegmentationModel2.runModel2(VALsupIterations,VALsubIterations, image,VALT0,VALalpha,VALbeta,VALnumberOfSegments)));
					//BufferedImage outputim = ImageSegmentationModel1.run(image,VALalpha);
					outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(outputim,100,100))));
				}
			}
			if(selection == "Texture synthesis"){
				if(textureSynthesisModel ==null){
					System.out.println("Choose a model");
					SetStatus("Choose a model");
					return;
				}
				SetStatus("Starting synthesis");
				ImageSegmentationModel1.SetOutputFile(outputImageLocation);
				float VALbeta1 = Float.valueOf(textureSynthesiserFields[0].getText());
				float VALbeta2 = Float.valueOf(textureSynthesiserFields[1].getText());
				float VALbeta3 = Float.valueOf(textureSynthesiserFields[2].getText());
				float VALbeta4 = Float.valueOf(textureSynthesiserFields[3].getText());
				float VALy1 = Float.valueOf(textureSynthesiserFields[4].getText());
				float VALy2 = Float.valueOf(textureSynthesiserFields[5].getText());
				float VALy3 = Float.valueOf(textureSynthesiserFields[6].getText());
				float VALy4 = Float.valueOf(textureSynthesiserFields[7].getText());
				float VALe1 = Float.valueOf(textureSynthesiserFields[8].getText());
				int VALiterations = Integer.valueOf(iterationsText.getText());
				int VALnumberOfSegments = Integer.valueOf(numberOfSegmentsText.getText());
				float[] beta = new float[]{VALbeta1,VALbeta2,VALbeta3,VALbeta4,VALy1,VALy2,VALy3,VALy4,VALe1};
				TextureSampler TS = new TextureSampler(gui);
				//BufferedImage outputim = TS.runModel(VALiterations,1f,0.99f,beta,VALnumberOfSegments);
				BufferedImage outputim = null;
				if(textureSynthesisModel == "Metropolis Sampler"){
					outputim = TS.runSampleAlgorithm1(VALiterations,beta,VALnumberOfSegments);
				}
				if(textureSynthesisModel == "Gibbs Sampler"){
					outputim = TS.runSampleAlgorithm2(VALiterations,beta,VALnumberOfSegments);
				}
				if(textureSynthesisModel == "Simulated Annealing"){
					outputim = TS.runPointModel(VALiterations,1f,0.99f,beta,VALnumberOfSegments);
				}
				System.out.println("textureSynthesisModel is:  "+ textureSynthesisModel);
				//BufferedImage outputim = ImageSegmentationModel1.run(image,VALalpha);
				ImageManipulation.writeImage(outputim, outputImageLocation,"texture");
				outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(outputim,100,100))));
				
			}
			if(selection == "H Texture synthesis"){
				if(outputImageLocation == null || outputImageLocationLabel.getText() == ""){
					System.out.println("select output folder and name");
				}
				SetStatus("Starting H synthesis");
				BufferedImage outputim = null;
				TextureSampler TS = new TextureSampler(gui);
				int iterations = Integer.parseInt(globalParameters[0].getText());
				int numberOfSegments = Integer.parseInt(globalParameters[1].getText());
				List<float[]> betas = new ArrayList<float[]>();
				for(int i=0;i<parameterFields.size();i++){
					float[] tmp = new float[9];
					for(int j=0;j<9;j++){tmp[j] = parameterFields.get(i)[j];}
					betas.add(tmp);
				}
				
				outputim = TS.runHeirarchicalTextureSynthesis(betas, iterations, numberOfSegments, outputImageLocation, OutputFileName.getText());
				System.out.println("textureSynthesisModel is:  "+ textureSynthesisModel);
				outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(outputim,100,100))));
				
			}
			if(selection == "H Texture sup seg MLL"){
				if(inputImageFile == null){
					System.out.println("select input image");
				}
				if(outputImageLocation == null || outputImageLocationLabel.getText() == ""){
					System.out.println("select output folder and name");
				}
				SetStatus("Starting H Texture sup seg");
				BufferedImage outputim = null;
				//TextureSampler TS = new TextureSampler(gui);
				int iterations = Integer.parseInt(mainParams[0].getText());
				int numberOfSegments = Integer.parseInt(mainParams[1].getText());
				float T0 = Float.parseFloat(mainParams[2].getText());
				float alpha = Float.parseFloat(mainParams[3].getText());
				List<float[]> betas = new ArrayList<float[]>();
				for(int i=0;i<parameterFieldsSegmentation.size();i++){
					float[] tmp = new float[9];
					for(int j=1;j<9;j++){tmp[j] = parameterFieldsSegmentation.get(i)[j];}
					betas.add(tmp);
				}
				int[][] image = ImageManipulation.readImage(inputImageFile);
				outputim = ImageSegmentationModel4.runPointModelMLL(image, iterations, T0, alpha,betas,numberOfSegments, outputImageLocation, OutputFileName.getText());
				//outputim = TS.runHeirarchicalTextureSynthesis(betas, iterations, numberOfSegments, outputImageLocation, outputImageLocationLabel.getText());
				outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(outputim,100,100))));
				
			}
			if(selection == "H Texture sup seg noise"){
				if(inputImageFile == null){
					System.out.println("select input image");
				}
				if(outputImageLocation == null || outputImageLocationLabel.getText() == ""){
					System.out.println("select output folder and name");
				}
				SetStatus("Starting H Texture sup noise");
				BufferedImage outputim = null;
				//TextureSampler TS = new TextureSampler(gui);
				int iterations = Integer.parseInt(mainParams[0].getText());
				int numberOfSegments = Integer.parseInt(mainParams[1].getText());
				float T0 = Float.parseFloat(mainParams[2].getText());
				float alpha = Float.parseFloat(mainParams[3].getText());
				float noiseMean = Float.parseFloat(mainParams[4].getText());
				float noiseSig = Float.parseFloat(mainParams[5].getText());
				List<float[]> betas = new ArrayList<float[]>();
				for(int i=0;i<parameterFieldsSegmentation.size();i++){
					float[] tmp = new float[9];
					for(int j=1;j<9;j++){tmp[j] = parameterFieldsSegmentation.get(i)[j];}
					betas.add(tmp);
				}
				int[][] image = ImageManipulation.readImage(inputImageFile);
				outputim = ImageSegmentationModel4.runPointModel(image, iterations, T0, alpha,betas,numberOfSegments, noiseMean, noiseSig, outputImageLocation, OutputFileName.getText());
				//outputim = TS.runHeirarchicalTextureSynthesis(betas, iterations, numberOfSegments, outputImageLocation, outputImageLocationLabel.getText());
				outputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(outputim,100,100))));
				
			}
		}
	}//close inner class
	class MyChooseImageListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			System.out.println("choose image");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(fc.showOpenDialog(ButtonChooseFile) == JFileChooser.APPROVE_OPTION){	
			}
			inputImageFile = fc.getSelectedFile();
			//inputImage =new ImageIcon(fc.getSelectedFile().getAbsolutePath()).getImage();
			try{
				inputImage = ImageIO.read(fc.getSelectedFile());
			}catch (IOException e) {}
			//inputImageGraphic = new JLabel(new ImageIcon(inputImageFile.getPath()));
			inputImageGraphic.setIcon((new ImageIcon(ImageManipulation.getScaledImage(new ImageIcon(fc.getSelectedFile().getAbsolutePath()).getImage(),100,100))));
		}
	}//close inner class
	class MyChooseSampleListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			System.out.println("choose sample");
			int VALnumberOfSegments = Integer.valueOf(numberOfSegmentsText.getText());
			float[] flt = {0f,0f};
			for(int i=0;i<VALnumberOfSegments;i++){
				sample.add(flt);
			}
			if(VALnumberOfSegments>0 && inputImageFile !=null){
				System.out.println(VALnumberOfSegments);
				JFrameSquareSampler ThisSample = new JFrameSquareSampler(inputImage,VALnumberOfSegments);
				ThisSample.setup();
				System.out.println("openedSampler");
			}else{
				System.out.println("Not Specified input image or number of segments 0");
			}
	}//close inner class
	}
    class MyChooseOutputImageListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			System.out.println("choose out loc");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(fc.showOpenDialog(ButtonChooseOutputFile) == JFileChooser.APPROVE_OPTION){	
			}
			outputImageLocation = fc.getSelectedFile();
			//outputImageLocationLabel.setText(outputImageLocation.getAbsolutePath());
			outputImageLocationLabel.setText("Chosen");
		}
	}//close inner class
	class MyModelListListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent event){
			if(!event.getValueIsAdjusting()){
				ResetPanels();
				String selection = (String)list.getSelectedValue();
				System.out.println(selection);
				SetStatus(selection);
				if(selection == "EdgeDetection"){
					SetupModel1();
				}
				if(selection == "Supervised Auto-normal"){
					SetupModel2();
				}
				if(selection == "Unsupervised Auto-normal"){
					SetupModel3();
				}
				if(selection == "Texture synthesis"){
					SetupTextureSynthesis();
				}
				if(selection == "H Texture synthesis"){
					SetupHTextureSynthesis();
				}
				if(selection == "H Texture sup seg MLL"){
					SetupHTextureSupervisedSegmentationMLL();
				}
				if(selection == "H Texture sup seg noise"){
					SetupHTextureSupervisedSegmentationNoise();
				}
				if(selection == "Add Gaussian noise"){
					SetupAddGaussianNoise();
				}
				
			}
		}
	}//close inner class
	class MyParameterMethodListListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent event){
			if(!event.getValueIsAdjusting()){
				ResetPanels();
				String selection = (String)parameterMethodList.getSelectedValue();
				System.out.println(selection);
				SetStatus(selection);
				if(selection == null){selection = "MRF Parameters1";}
				float VALbeta1 = Float.valueOf(textureSynthesiserFields[0].getText());
				float VALbeta2 = Float.valueOf(textureSynthesiserFields[1].getText());
				float VALbeta3 = Float.valueOf(textureSynthesiserFields[2].getText());
				float VALbeta4 = Float.valueOf(textureSynthesiserFields[3].getText());
				float VALy1 = Float.valueOf(textureSynthesiserFields[4].getText());
				float VALy2 = Float.valueOf(textureSynthesiserFields[5].getText());
				float VALy3 = Float.valueOf(textureSynthesiserFields[6].getText());
				float VALy4 = Float.valueOf(textureSynthesiserFields[7].getText());
				float VALe1 = Float.valueOf(textureSynthesiserFields[8].getText());
				int VALiterations = Integer.valueOf(textureSynthesiserFields[9].getText());
				int VALnumberOfSegments = Integer.valueOf(textureSynthesiserFields[10].getText());
				float[] beta = new float[]{VALbeta1,VALbeta2,VALbeta3,VALbeta4,VALy1,VALy2,VALy3,VALy4,VALe1,VALiterations,VALnumberOfSegments};
				parameterFields.set(segmentNumber-1, beta);
				
				//segmentNumber = Integer.parseInt(""+selection.charAt(selection.length()-1));
				
				segmentNumber = Integer.parseInt(selection.replaceAll("[\\D]", ""));
				for(int i=0;i<9;i++){textureSynthesiserFields[i].setText(String.valueOf(parameterFields.get(segmentNumber-1)[i]));}
				// may need to repaint the labels here to update them.
			}
		}
	}//close inner class
	class MyParameterMethodListSegmentationListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent event){
			if(!event.getValueIsAdjusting()){
				ResetPanels();
				String selection = (String)parameterMethodListSegmentation.getSelectedValue();
				System.out.println(selection);
				SetStatus(selection);
				if(selection == null){selection = "MRF Parameters1";}
				float VALbeta1 = Float.valueOf(textureSynthesiserFields[0].getText());
				float VALbeta2 = Float.valueOf(textureSynthesiserFields[1].getText());
				float VALbeta3 = Float.valueOf(textureSynthesiserFields[2].getText());
				float VALbeta4 = Float.valueOf(textureSynthesiserFields[3].getText());
				float VALy1 = Float.valueOf(textureSynthesiserFields[4].getText());
				float VALy2 = Float.valueOf(textureSynthesiserFields[5].getText());
				float VALy3 = Float.valueOf(textureSynthesiserFields[6].getText());
				float VALy4 = Float.valueOf(textureSynthesiserFields[7].getText());
				float VALe1 = Float.valueOf(textureSynthesiserFields[8].getText());
				int VALnumberOfSegments = Integer.valueOf(textureSynthesiserFields[9].getText());
				float[] beta = new float[]{VALbeta1,VALbeta2,VALbeta3,VALbeta4,VALy1,VALy2,VALy3,VALy4,VALe1,VALnumberOfSegments};
				parameterFieldsSegmentation.set(segmentNumber-1, beta);
				
				//segmentNumber = Integer.parseInt(""+selection.charAt(selection.length()-1));
				
				segmentNumber = Integer.parseInt(selection.replaceAll("[\\D]", ""));
				for(int i=0;i<9;i++){textureSynthesiserFields[i].setText(String.valueOf(parameterFieldsSegmentation.get(segmentNumber-1)[i]));}
				// may need to repaint the labels here to update them.
			}
		}
	}//close inner class
	class MyTextureSythesisListListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent event){
			if(!event.getValueIsAdjusting()){
				ResetPanels();
				String selection = (String)textureSynthesiserMethodList.getSelectedValue();
				System.out.println(selection);
				SetStatus(selection);
				textureSynthesisModel = selection;
		
				
			}
		}
	}//close inner class
	class NumberOfMRFsListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println("changed number of MRFs");
			int number = Integer.parseInt(globalParameters[1].getText());
			while(listParameterEntries.length>number){
				String[] tmp = new String[listParameterEntries.length-1];
				for(int i=0;i<listParameterEntries.length-1;i++){tmp[i] = listParameterEntries[i];}
				listParameterEntries = tmp;
				parameterFields.remove(parameterFields.size()-1);
				//repaint here
			}
			while(listParameterEntries.length<number){
				String[] tmp = new String[listParameterEntries.length+1];
				for(int i=0;i<listParameterEntries.length;i++){tmp[i] = listParameterEntries[i];}
				tmp[tmp.length-1] = "MRF Parameters"+(listParameterEntries.length+1);
				listParameterEntries = tmp;
				parameterFields.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f,50f});
				//repaint here
				
			}
			parameterMethodList.setListData(listParameterEntries);
			
			
			
			for(String entry : listParameterEntries){System.out.println(entry);}
		  }

	}//close inner class
	class NumberOfMRFsSegmentationListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.out.println("changed number of MRFs");
			int number = Integer.parseInt(mainParams[1].getText());
			while(listParameterEntriesSegmentation.length>number){
				String[] tmp = new String[listParameterEntriesSegmentation.length-1];
				for(int i=0;i<listParameterEntriesSegmentation.length-1;i++){tmp[i] = listParameterEntriesSegmentation[i];}
				listParameterEntriesSegmentation = tmp;
				System.out.println("number" + number);
				parameterFieldsSegmentation.remove(parameterFieldsSegmentation.size()-1);
				
				//repaint here
			}
			while(listParameterEntriesSegmentation.length<number){
				String[] tmp = new String[listParameterEntriesSegmentation.length+1];
				for(int i=0;i<listParameterEntriesSegmentation.length;i++){tmp[i] = listParameterEntriesSegmentation[i];}
				tmp[tmp.length-1] = "MRF Parameters"+(listParameterEntriesSegmentation.length+1);
				listParameterEntriesSegmentation = tmp;
				parameterFieldsSegmentation.add(new float[]{0f,0f,0f,0f,0f,0f,0f,0f,0f,3f});
				//repaint here
				
			}
			parameterMethodListSegmentation.setListData(listParameterEntriesSegmentation);
			
			
			
			for(String entry : listParameterEntriesSegmentation){System.out.println(entry);}
		  }

	}//close inner class
	
}
