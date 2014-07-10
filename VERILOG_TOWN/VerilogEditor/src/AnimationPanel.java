import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AnimationPanel extends JPanel
{
	private Image 							image;
	private Image							go_s;
	private Image							go_n;
	private Image							go_e;
	private Image							go_w;
	private Image							left_s;
	private Image							left_n;
	private Image							left_w;
	private Image							left_e;
	private Image							right_n;
	private Image							right_s;
	private Image							right_w;
	private Image							right_e;
	private Image							forward_n;
	private Image							forward_s;
	private Image							forward_w;
	private Image							forward_e;
	private Image							stop_n;
	private Image							stop_s;
	private Image							stop_e;
	private Image							stop_w;
	private Image							blank;
	private JCheckBox 						cb[];
	private JFormattedTextField 			simulateInput;
	private MyMouseListener					myMouseListener;
	private String							simulationResults;
	
    
    public AnimationPanel(JFormattedTextField simulateInput) {
    	this.simulateInput = simulateInput;
    	myMouseListener = new MyMouseListener(simulateInput, this);
    	this.simulationResults = "00000";
    	URL imageURL = AnimationPanel.class.getResource("images/animationImage.png");
    	image = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/go_s.png");
    	go_s = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/go_n.png");
    	go_n = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/go_e.png");
    	go_e = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/go_w.png");
    	go_w = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/left_n.png");
    	left_n = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/left_s.png");
    	left_s = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/left_w.png");
    	left_w = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/left_e.png");
    	left_e = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/right_n.png");
    	right_n = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/right_s.png");
    	right_s = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/right_e.png");
    	right_e = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/right_w.png");
    	right_w = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/forward_n.png");
    	forward_n = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/forward_s.png");
    	forward_s = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/forward_e.png");
    	forward_e = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/forward_w.png");
    	forward_w = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/stop_n.png");
    	stop_n = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/stop_s.png");
    	stop_s = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/stop_w.png");
    	stop_w = new ImageIcon(imageURL).getImage();
    	imageURL = AnimationPanel.class.getResource("images/stop_e.png");
    	stop_e = new ImageIcon(imageURL).getImage();
    	
    	
        this.setLayout(new GridBagLayout());
        
        cb = new JCheckBox[8];
        for(int i = 0; i < 8; i++){
        	cb[i] = new JCheckBox();
        	cb[i].setOpaque(false);
        	cb[i].addMouseListener(myMouseListener);
        }
               
        addCheckBoxs();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0,this.getWidth(),this.getHeight(), this);
        for(int i = 0; i < 8; i++){
        	cb[i].setPreferredSize(new Dimension((int)(this.getWidth() / 4.0), (int)(this.getHeight() / 4.0)));
        }        
    }
    
    public String getInternalSignal(){
    	String temp = "";
    	for(int i = 7; i >= 0; i--){
    		if(cb[i].isSelected())
        		temp += "1";
        	else
        		temp += "0";
    	}    	
    	return temp;
    }
    
    public void setStates(String str){
    	for(int i = 0; i < 8; i++){
    		if(str.charAt(i) == '1')
    			cb[7 - i].setSelected(true);
    		else
    			cb[7 - i].setSelected(false);
    	}
    }
    
    public void setSimulationResults(String str){
    	this.simulationResults = str;
    }
    
    public void drawAnimation(Graphics g){
    	//"00000"first bit: simulation start; 
        //		 second bit: output signal facing N;
        //		 third bit: output signal facing S;
        //		 fourth bit: output signal facing E;
        //		 fifth bit: output signal facing W.
        if(this.simulationResults.startsWith("1")){
        	g.drawImage(image, 0, 0,this.getWidth(),this.getHeight(), this);
        	for(int i = 0; i < 8; i++){
        		cb[i].repaint();
        	}
        	        	
        	if(simulationResults.charAt(1) == '0')
        		g.drawImage(stop_n, 61, 62, 23, 23, this);
        	else if(simulationResults.charAt(1) == '1')
        		g.drawImage(forward_n, 61, 62, 23, 23, this);
        	else if(simulationResults.charAt(1) == '2')
        		g.drawImage(left_n, 61, 62, 23, 23, this);
        	else if(simulationResults.charAt(1) == '3')
        		g.drawImage(right_n, 61, 62, 23, 23, this);
        	else if(simulationResults.charAt(1) == '4')
        		g.drawImage(go_n, 61, 62, 23, 23, this);
        	
        	if(simulationResults.charAt(2) == '0')
        		g.drawImage(stop_s, 0, 0, 23, 23, this);
        	else if(simulationResults.charAt(2) == '1')
        		g.drawImage(forward_s, 0, 0, 23, 23, this);
        	else if(simulationResults.charAt(2) == '2')
        		g.drawImage(left_s, 0, 0, 23, 23, this);
        	else if(simulationResults.charAt(2) == '3')
        		g.drawImage(right_s, 0, 0, 23, 23, this);
        	else if(simulationResults.charAt(2) == '4')
        		g.drawImage(go_s, 0, 0, 23, 23, this);
        	
        	if(simulationResults.charAt(3) == '0')
        		g.drawImage(stop_e, 0, 62, 23, 23, this);
        	else if(simulationResults.charAt(3) == '1')
        		g.drawImage(forward_e, 0, 62, 23, 23, this);
        	else if(simulationResults.charAt(3) == '2')
        		g.drawImage(left_e, 0, 62, 23, 23, this);
        	else if(simulationResults.charAt(3) == '3')
        		g.drawImage(right_e, 0, 62, 23, 23, this);
        	else if(simulationResults.charAt(3) == '4')
        		g.drawImage(go_e, 0, 62, 23, 23, this);
        	
        	if(simulationResults.charAt(4) == '0')
        		g.drawImage(stop_w, 61, 0, 23, 23, this);
        	else if(simulationResults.charAt(4) == '1')
        		g.drawImage(forward_w, 61, 0, 23, 23, this);
        	else if(simulationResults.charAt(4) == '2')
        		g.drawImage(left_w, 61, 0, 23, 23, this);
        	else if(simulationResults.charAt(4) == '3')
        		g.drawImage(right_w, 61, 0, 23, 23, this);
        	else if(simulationResults.charAt(4) == '4')
        		g.drawImage(go_w, 61, 0, 23, 23, this);
        }
        else{
    		g.drawImage(image, 0, 0,this.getWidth(),this.getHeight(), this);
    		for(int i = 0; i < 8; i++){
        		cb[i].repaint();
        	}
        }
    }
    
    public void addCheckBoxs(){
    	GridBagConstraints ccb0 = new GridBagConstraints();
        ccb0.gridx = 1;
        ccb0.gridy = 1;
        
        GridBagConstraints ccb1 = new GridBagConstraints();
        ccb1.gridx = 2;
        ccb1.gridy = 1;
       
        GridBagConstraints ccb2 = new GridBagConstraints();
        ccb2.gridx = 1;
        ccb2.gridy = 2;
        
        GridBagConstraints ccb3 = new GridBagConstraints();
        ccb3.gridx = 2;
        ccb3.gridy = 2;
        
        GridBagConstraints ccb4 = new GridBagConstraints();
        ccb4.gridx = 1;
        ccb4.gridy = 0;
        
        GridBagConstraints ccb5 = new GridBagConstraints();
        ccb5.gridx = 0;
        ccb5.gridy = 2;
        
        GridBagConstraints ccb6 = new GridBagConstraints();
        ccb6.gridx = 2;
        ccb6.gridy = 3;
        
        GridBagConstraints ccb7 = new GridBagConstraints();
        ccb7.gridx = 3;
        ccb7.gridy = 1;
        
        this.add(cb[0], ccb0);
        this.add(cb[1], ccb1);
        this.add(cb[2], ccb2);
        this.add(cb[3], ccb3);
        this.add(cb[4], ccb4);
        this.add(cb[5], ccb5);
        this.add(cb[6], ccb6);
        this.add(cb[7], ccb7);
    }
}

class MyMouseListener implements MouseListener{
	private JFormattedTextField 			simulateInput;
	private AnimationPanel					animationPanel;
	public MyMouseListener(JFormattedTextField simulateInput, AnimationPanel animationPanel){
		this.simulateInput = simulateInput;
		this.animationPanel = animationPanel;
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		this.simulateInput.setText(animationPanel.getInternalSignal());
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.simulateInput.setText(animationPanel.getInternalSignal());
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{	
		this.simulateInput.setText(animationPanel.getInternalSignal());
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{				
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
}
