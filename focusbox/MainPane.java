package focusbox;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainPane extends JPanel{
	ImageIcon workCharIMG = new ImageIcon(getClass().getResource("/images/idle2.png"));
	ImageIcon pauseCharIMG = new ImageIcon(getClass().getResource("/images/sit.png"));
	ImageIcon sleepCharIMG = new ImageIcon(getClass().getResource("/images/sit.png"));
	
	JLabel workLabel, pauseLabel, sleepLabel, timeDisp, totalTimeDisp;
	CardLayout charL;
	MainProgram prog = null;
	MainGui mainFrame = null;
	JPanel charPanel, underBar;
	int posX, posY;
	
	public MainPane(MainProgram p){
		prog = p;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		//character displayed using card layout
		charPanel = new JPanel();
		JPanel workPane = new JPanel(); 
		JPanel pausePane = new JPanel();
		JPanel sleepPane = new JPanel();
		
		charPanel.setBackground(new Color(0, 0, 0, 0));
		workPane.setBackground(new Color(0, 0, 0, 0));
		pausePane.setBackground(new Color(0, 0, 0, 0));
		sleepPane.setBackground(new Color(0, 0, 0, 0));
		
		makeLabels();
		
		charL = new CardLayout();
		charPanel.setLayout(charL);	
		workPane.add(workLabel);
		pausePane.add(pauseLabel);
		sleepPane.add(sleepLabel);
		
		charPanel.add(workPane, "Working Char");
		charPanel.add(pausePane, "Paused Char");
		charPanel.add(sleepPane, "Sleeping Char");
		charL.show(charPanel, "Sleeping Char");
		
		c.gridy = 0;
		add(charPanel, c);
		
		//size of character changes size of panel/window 
		//click character to start and stop timer
		//timer under character
		underBar = new JPanel(new GridBagLayout()); // holds the timer under the character
		GridBagConstraints timeC = new GridBagConstraints();
		underBar.setBackground(new Color(0, 0, 0, 0));
		timeDisp = new JLabel("00:00:00");
		timeC.fill = GridBagConstraints.HORIZONTAL;
		timeC.gridy = 0;
		underBar.add(timeDisp, timeC);
		totalTimeDisp = new JLabel("00:00:00");
		totalTimeDisp.setForeground(new Color(0,0,0,0.5f));
		timeC.gridy = 1;
		underBar.add(totalTimeDisp, timeC);
		
		c.gridy = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		add(underBar, c);
	}
	
	public void makeLabels(){
		workLabel = new charLabel(resizeImage(workCharIMG, 100));
		pauseLabel = new charLabel(resizeImage(pauseCharIMG, 100));
		sleepLabel = new charLabel(resizeImage(sleepCharIMG, 100));
		
		pauseLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON1){
					charL.show(charPanel, "Working Char");
					//set timers
					//	if task is new - elapsed time is 0
					//	if task is old - elapsed time is not 0
					prog.getCurrTask().start();
					//((RunTimer)prog.runTimer).startTimer();
					((RunTimer)prog.runTimer).offBreak();
				}
			}
		});
		workLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON1){
					charL.show(charPanel, "Paused Char");
					prog.getCurrTask().stop();
					//((RunTimer)prog.runTimer).stopTimer();
					((RunTimer)prog.runTimer).onBreak();
				}
			}
		});
		sleepLabel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON1){
					if(prog.hasTask){
						charL.show(charPanel, "Working Char");
						if(prog.mainTimer.getState() == Thread.State.NEW){
							((RunTimer)prog.runTimer).stop = false;
							prog.mainTimer.start();
						}
						else{
							((RunTimer)prog.runTimer).startTimer();
						}
						prog.getCurrTask().start();
					}
				}
			}
		});
	}
	
	class charLabel extends JLabel{
		public charLabel(ImageIcon i){
			super(i);
			addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					mainFrame = (MainGui) SwingUtilities.getAncestorOfClass(JFrame.class, charLabel.this);
					posX = e.getX();
					posY = e.getY();
				}
			});
			addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(e.getButton() == MouseEvent.BUTTON3)
						mainFrame.rcMenu.show(charLabel.this, e.getX(), e.getY());
				}
			});
			addMouseMotionListener(new MouseMotionListener(){
				public void mouseDragged(MouseEvent e){
					mainFrame.setLocation(mainFrame.getLocation().x + e.getX() - posX,
							mainFrame.getLocation().y + e.getY() - posY);
				}
				public void mouseMoved(MouseEvent e) {
				}
			});
		}
	}
	
	public int getImageWidth(){
		return workCharIMG.getIconWidth();
	}
	
	public int getImageHeight(){
		return workCharIMG.getIconHeight();
	}
	
	public ImageIcon resizeImage(ImageIcon icon, int size) {
		Image img = icon.getImage();
		Image ret = img.getScaledInstance(size, size, java.awt.Image.SCALE_FAST);
		return new ImageIcon(ret);
	}
}
