package focusbox;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.plaf.LayerUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainGui extends JFrame{
	MainProgram prog;
	JPopupMenu rcMenu;
	JPanel taskPanel, mainPanel;
	
	public MainGui(MainProgram p){
		prog = p;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exits program when closed
		setTitle("focusBox");
		setSize(350, 200);
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setAlwaysOnTop(true);
		
		JPanel content = new JPanel();
		content.setBackground(new Color(0, 0, 0, 0));
		setContentPane(content);
		content.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();

		taskPanel = new TaskPane(prog);
		taskPanel.setBackground(new Color(0, 0, 0, 0));
		/* Panel to hold character and timer */
		mainPanel = new MainPane(prog);
		mainPanel.setBackground(new Color(0, 0, 0, 0));
		
		int mainW = ((MainPane) mainPanel).getImageWidth() + 20;
		int mainH = ((MainPane) mainPanel).getImageHeight();
		mainH += ((TaskPane) taskPanel).getPanelHeight() + 20;
		Dimension minDim = new Dimension(mainW, mainH);
		setMinimumSize(minDim);
		//setPreferredSize(minDim);
		
		c.fill = GridBagConstraints.BOTH;
		//c.anchor = GridBagConstraints.NORTH;
		c.gridy = 0;
		c.weighty = 1;
		content.add(taskPanel, c);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridy = 1;
		content.add(mainPanel, c);
		
		rcMenu = new RightClickMenu();
		pack();
		setLocationRelativeTo(null); //centers frame on screen
		setVisible(true);
	}
	
	public void setTimerDisp(String s){
		((MainPane)mainPanel).timeDisp.setText(s);
	}
	
	public void setTotTimerDisp(String s){
		((MainPane)mainPanel).totalTimeDisp.setText(s);
	}
	
	/* The Right-Click Menu
	 * quitItem - Can quit program
	 * */
	class RightClickMenu extends JPopupMenu{
		JMenuItem addItem, editItem, saveItem, resetItem, settingsItem, quitItem;
		
		public RightClickMenu(){
			addItem = new JMenuItem("Set Task");
			addItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					new MakeTaskDialog(prog, false);
				}
			});
			add(addItem);
			

			editItem = new JMenuItem("Edit Task");
			editItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					new MakeTaskDialog(prog, true);
				}
			});
			editItem.setEnabled(false);
			add(editItem);
			
			saveItem = new JMenuItem("Save");
			saveItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					prog.mainMenu.saveTask();
				}
			});
			saveItem.setEnabled(false);
			add(saveItem);
			
			resetItem = new JMenuItem("Reset");
			resetItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(prog.getNumTasks() > 0){
						if(prog.mainTimer.isAlive())
							((RunTimer)prog.runTimer).stopTimer();
						prog.gui.setTimerDisp("00:00:00");
						prog.gui.setTotTimerDisp("00:00:00");
						((TaskPane)prog.gui.taskPanel).taskDisp.setText("Add a task...");
						prog.gui.pack();
						//delete current task
						prog.resetProg();
						((RunTimer)prog.runTimer).stop = true;
						//change the current charPane to sleepCharPane
						((MainPane) prog.gui.mainPanel).charL.show(((MainPane) prog.gui.mainPanel).charPanel, "Sleeping Char");
						resetItem.setEnabled(false);
						editItem.setEnabled(false);
						addItem.setEnabled(true);
					}

				}
			});
			resetItem.setEnabled(false);
			add(resetItem);
			
			settingsItem = new JMenuItem("Settings");
			settingsItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					new MakeSettingsDialog(prog);
				}
			});
			add(settingsItem);
			
			quitItem = new JMenuItem("Quit");
			quitItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					dispose();
					System.exit(0);
				}
			});
			add(quitItem);
		}
	}
}
