import javax.imageio.ImageIO;
import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JFrameSquareSampler extends JFrame implements ActionListener {
	
	//JFileChooser fc = new JFileChooser();
	JButton ButtonChoosePoint1 = new JButton("Set Point 1"); 
	JButton ButtonChoosePoint2 = new JButton("Set Point 2"); 
	//JButton ButtonChooseFile = new JButton("Choose Base Image"); 
	JButton Confirm = new JButton("Confirm"); 
	public static int numberOfSegments;
	static JLabel pos1Text = new JLabel("pos 1 (top left)   ");
	static JLabel pos2Text = new JLabel("pos 2 (lower right)   ");
	static JLabel segmentNumber = new JLabel("SegmentNumber:");
	static int[] point1 = {0,0};
	static int[] point2 = {0,0};
	static JLabel posCurrentlySelected = new JLabel("CURRENTLY SELECTED: x=   " +" y:   ");
	//public static File BaseIm = null;
	public static BufferedImage BaseImage = null;
	static int[] pos1 = {0,0};
	static int[] pos2 = {0,0};
	static int[] lastClick ={0,0};
	public static void setBaseImage(BufferedImage toSet){
		BaseImage = toSet;
	}
	public JFrameSquareSampler(BufferedImage im, int segs) {
		setBaseImage((BufferedImage) im);
		setNumberOfSegments(segs);
		System.out.println("set segs"+segs);
		setOutputImage((BufferedImage) im);
		IDENTIFIER=0;
	}
	private static JLabel ChosenBaseImage = new JLabel(new ImageIcon(getScaledImage(new ImageIcon("C:\\Users\\Ollie Loney\\Desktop\\workspace\\Edge Analysis\\nofilechosen.jpg").getImage(),100,100)));
	private static JLabel OutputImage = new JLabel(new ImageIcon(getScaledImage(new ImageIcon("C:\\Users\\Ollie Loney\\Desktop\\workspace\\Edge Analysis\\nofilechosen.jpg").getImage(),300,300)));
	private static final long serialVersionUID = 1L;
	public static JFrameSquareSampler frame;
	public static int IDENTIFIER;
	public static void main(String[] args){
		setup();
		/*
		frame = new JFrameSquareSampler();
		frame.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(java.awt.event.MouseEvent m) {
				
			}
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("clicked");
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("clicked");
			}
			@Override
			public void mousePressed(java.awt.event.MouseEvent m) {
				//System.out.println("clicked");
				System.out.println("X: " +m.getXOnScreen());
				System.out.println("Y: " +m.getYOnScreen());
				int xpos = m.getXOnScreen() - frame.OutputImage.getLocation().x-frame.getLocation().x-3;
				int ypos = m.getYOnScreen() - frame.OutputImage.getLocation().y-frame.getLocation().y-26;
				if(xpos>0 && xpos<300 && ypos>0 && ypos<300){
					lastClick[0]=xpos;
					lastClick[1]=ypos;
				}
				posCurrentlySelected.setText("CURRENTLY SELECTED:    x=" + lastClick[0] +"    y:"+lastClick[1]);
				
			}
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("clicked");
			}

		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		*/
		
	}
	public static void setup(){
		frame = new JFrameSquareSampler();
		((JComponent) frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
		frame.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(java.awt.event.MouseEvent m) {
				//System.out.println("clicked");
				System.out.println("X: " +m.getXOnScreen());
				System.out.println("Y: " +m.getYOnScreen());
				int xoffset = 14;
				int yoffset = 136;
				if(m.getXOnScreen() - frame.getLocation().x-xoffset>0 && m.getXOnScreen() - frame.getLocation().x-xoffset<300 && m.getYOnScreen() - frame.getLocation().y-yoffset>0 && m.getYOnScreen() - frame.getLocation().y-yoffset<300){
					lastClick[0]=m.getXOnScreen() - frame.getLocation().x-xoffset;
					lastClick[1]=m.getYOnScreen() - frame.getLocation().y-yoffset;
				}
				posCurrentlySelected.setText("CURRENTLY SELECTED:    x=" + lastClick[0] +"    y:"+lastClick[1]);
				
			}
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("clicked");
			}
			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("clicked");
			}
			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("clicked");
			}
			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("clicked");
			}

		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public void setNumberOfSegments(int n){
		numberOfSegments = n;
		System.out.println("setNumberOfSegments"+numberOfSegments);
	}
	//public void setBaseIm(File f){
		//BaseIm = f;
		//try {
			//BaseImage = ImageIO.read(BaseIm);
		//} catch (IOException er) {}
		//ChosenBaseImage.setIcon((new ImageIcon(getScaledImage(BaseImage,100,100))));
		//OutputImage.setIcon((new ImageIcon(getScaledImage(BaseImage,300,300))));
	//}
	JFrameSquareSampler(){
		super("Edge Finder");
		setSize(320,480);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		//fc.setCurrentDirectory(new File("C:\\Users\\Ollie Loney\\Desktop"));
		//fc.setDialogTitle("Base Image");
		//fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		//ButtonChooseFile.addActionListener(this);
		ButtonChoosePoint1.addActionListener(this);
		ButtonChoosePoint2.addActionListener(this);
		Confirm.addActionListener(this);
		
		//size
		
		
		//add(ButtonChooseFile);
		add(ButtonChoosePoint1);
		add(ButtonChoosePoint2);
		add(posCurrentlySelected);
		add(pos1Text);
		add(pos2Text);
		add(posCurrentlySelected);
		//add(ChosenBaseImage);
		add(OutputImage);
		add(Confirm);
		add(segmentNumber);
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

	private static void refreshImage(){
		// int[] point1 = read BaseIm to get width and height and convert pos1 and pos 2 to these
		BufferedImage bufferedImage = null;
		bufferedImage=BaseImage;
		int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
		point1[0] = (int)(pos1[0]*(image[0].length/300f));
		point1[1] = (int)(pos1[1]*(image.length/300f));
		point2[0] = (int)(pos2[0]*(image[0].length/300f));
		point2[1] =	(int)(pos2[1]*(image.length/300f));
		//System.out.println("point 1:  x=" + point1[0] + " y="+point1[1]);
		//System.out.println("point 2:  x=" + point2[0] + " y="+point2[1]);
		//System.out.println("pos 1:  x=" + pos1[0] + " y="+pos1[1]);
		//System.out.println("pos 2:  x=" + pos2[0] + " y="+pos2[1]);
		for(int r=Math.min(point2[1],point1[1]);r<Math.max(point2[1],point1[1]);r++){
			for(int c=Math.min(point2[0],point1[0]);c<Math.max(point2[0],point1[0]);c++){
				image[r][c] -= 1000;
				if(image[r][c] < 0){image[r][c] = 0;}
			}
		}
		OutputImage.setIcon(new ImageIcon(getScaledImage(ImageManipulation.convertToImage(image),300,300)));
		System.out.println("refreshed");
	}
	private static void reset(){
		segmentNumber.setText("Segment Number: " + (IDENTIFIER+1));
		pos1[0] = 0;
		pos1[1] = 0;
		pos2[0] = 0;
		pos2[1] = 0;
		point1[0] = 0;
		point1[1] = 0;
		point2[0] = 0;
		point2[1] = 0;
		lastClick[0] = 0;
		lastClick[1] = 0;
		pos1Text.setText("pos 1 (top left)   ");
		pos2Text.setText("pos 2 (lower right)   ");
		posCurrentlySelected.setText("CURRENTLY SELECTED: x=   " +" y:   ");
		ChosenBaseImage.setIcon((new ImageIcon(getScaledImage(BaseImage,100,100))));
		OutputImage.setIcon((new ImageIcon(getScaledImage(BaseImage,300,300))));
		
	}
	private static void paintPos(int whichPos){
		BufferedImage bufferedImage = null;
		bufferedImage = BaseImage;
		int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
		if(whichPos == 1){
			int[] point1 = {(int)(pos1[0]*(image[0].length/300f)),(int)(pos1[1]*(image.length/300f))};
			for(int r=0;r<7;r++){for(int c=0;c<7;c++){if(r==c || r-3==-c+3){
				if(point1[1]+r-3>=0 && point1[1]+r-3 <=image[0].length && point1[0]+c-3>=0 && point1[0]+c-3<image.length){
					image[point1[1]+r-3][point1[0]+c-3] = 0;
				}
			}}}
		}
		if(whichPos == 2){
			int[] point2 = {(int)(pos2[0]*(image[0].length/300f)),(int)(pos2[1]*(image.length/300f))};
			for(int r=0;r<7;r++){for(int c=0;c<7;c++){if(r==c || r-3==-c+3){
			image[point2[1]+r-3][point2[0]+c-3] = 0;
			}}}
		}
		OutputImage.setIcon(new ImageIcon(getScaledImage(ImageManipulation.convertToImage(image),300,300)));
		System.out.println("painted point:"+whichPos);
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
		
		if(name.equals("Set Point 1")){   ///note this is the bit to make work
			pos1[0] = lastClick[0];
			pos1[1] = lastClick[1];
			paintPos(1);
			pos1Text.setText("pos1: x=" + pos1[0] +" y:"+pos1[1]);
			if((pos1[0]!=0 ||pos1[1]!=0) && (pos2[0]!=0 ||pos2[1]!=0)){
				refreshImage();
			}
		}else if(name.equals("Set Point 2")){   ///note this is the bit to make work
			pos2[0] = lastClick[0];
			pos2[1] = lastClick[1];
			pos2Text.setText("pos2: x=" + pos2[0] +" y:"+pos2[1]);
			System.out.println("set pos 2");
			paintPos(2);
			if((pos1[0]!=0 ||pos1[1]!=0) && (pos2[0]!=0 ||pos2[1]!=0)){
				refreshImage();
			}
		}else if(name.equals("Confirm")){   ///note this is the bit to make work
			setNumberOfSegments(4);
			BufferedImage bufferedImage = null;
			bufferedImage=BaseImage;
			int[][] image = ImageManipulation.convertTo2DUsingGetRGB(bufferedImage);
			System.out.println(image[5][5]);
			System.out.println(point1[1]);
			float[] sam = GibbsDistributionOrder1.SquareSampler(point1[0], point2[0], point1[1], point2[1],image,false,IDENTIFIER);
			System.out.println("segs:"+numberOfSegments+"id"+IDENTIFIER);
			if(numberOfSegments -1>IDENTIFIER){
				//.sample.get(IDENTIFIER)[0] = sam[0];
				//.sample.get(IDENTIFIER)[1] = sam[1];
				MainFrame.sample.set(IDENTIFIER, new float[]{sam[0],sam[1]});
				reset();
				System.out.println("set sample:" + "mean"+IDENTIFIER+" = "+sam[0]+ "sig"+IDENTIFIER+" = "+sam[1]);
				IDENTIFIER=IDENTIFIER+1;
			}else{
				MainFrame.sample.set(IDENTIFIER, new float[]{sam[0],sam[1]});
				reset();
				System.out.println("set sample:" + "mean"+IDENTIFIER+" = "+sam[0]+ "sig"+IDENTIFIER+" = "+sam[1]);
				dispose();
			}
		}
		
		
	}
	
	
}

