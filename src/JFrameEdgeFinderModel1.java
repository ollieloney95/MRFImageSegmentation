import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JFrameEdgeFinderModel1 extends JFrame implements ActionListener{
	
	JFileChooser fc = new JFileChooser();
	JButton ButtonChooseFile = new JButton("Choose Base Image"); 
	JButton ButtonRun = new JButton("Run"); 
	JButton ButtonChooseOutputLocation = new JButton("Choose Output Location"); 
	JButton ButtonChooseOutputName = new JButton("Choose Output Name"); 
	
	JLabel ChosenFile = new JLabel("no file chosen");
	JLabel ChosenOutputLocation = new JLabel("no location chosen");
	JLabel ChosenOutputName = new JLabel("output.jpg");
	JLabel AlphaLabel = new JLabel("Alpha");
	private static JLabel ChosenBaseImage = new JLabel(new ImageIcon(getScaledImage(new ImageIcon("C:\\Users\\Ollie Loney\\Desktop\\workspace\\Edge Analysis\\nofilechosen.jpg").getImage(),100,100)));
	private static JLabel OutputImage = new JLabel(new ImageIcon(getScaledImage(new ImageIcon("C:\\Users\\Ollie Loney\\Desktop\\workspace\\Edge Analysis\\nofilechosen.jpg").getImage(),300,300)));
	JTextField outputName = new JTextField("output");
	JTextField alpha = new JTextField("5000");
	
	public File BaseIm = null;
	public File OutputLocation = null;
	public String OutputName = "output.jpg";
	private static final long serialVersionUID = 1L;
	public static JFrameEdgeFinderModel1 frame;
	public static void main(String[] args){
		frame = new JFrameEdgeFinderModel1();
		frame.setVisible(true);
		ChosenBaseImage.setIcon((new ImageIcon(getScaledImage(ImageManipulation.convertToImage(new int[100][100]),100,100))));
		OutputImage.setIcon((new ImageIcon(getScaledImage(ImageManipulation.convertToImage(new int[100][100]),300,300))));
		
	}
	private JFrameEdgeFinderModel1(){
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
		
		add(ButtonChooseFile);
		add(ChosenFile);
		add(ButtonChooseOutputLocation);
		add(ChosenOutputLocation);
		add(ButtonChooseOutputName);
		add(outputName);
		add(ChosenOutputName);
		add(AlphaLabel);
		add(alpha);
		add(ButtonRun);
		add(ChosenBaseImage);
		add(OutputImage);
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
			System.out.println("run");
			if(BaseIm != null && OutputLocation != null){
				BufferedImage bufferedImage = null;
				int VALalpha = Integer.valueOf(alpha.getText());
				try {
					bufferedImage = ImageIO.read(BaseIm);
				} catch (IOException ee) {}
				int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
				File OutputFile = new File(OutputLocation.getAbsolutePath()+"\\"+OutputName);
				BufferedImage outputim = EdgeFinderModel1.run(image,VALalpha);
				OutputImage.setIcon((new ImageIcon(getScaledImage(outputim,300,300))));
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
		}
		
		
	}

	
}


