package focusbox;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class MakeSettingsDialog extends JDialog{
	MainProgram prog;
	
	public MakeSettingsDialog(MainProgram p){
		super();
		prog = p;
		setTitle("Settings");
		setAlwaysOnTop(true);
		setSize(250, 200);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,10,10);
		c.weightx = 1; 
		
		c.gridy = 0;
		getContentPane().add(new JLabel("Set break/work times (in minutes)"), c);
		c.gridy = 1; 
		getContentPane().add(new JLabel("Work:"), c);
		JTextArea workTimeArea = new JTextArea("40"); 
		getContentPane().add(workTimeArea, c);
		c.gridy = 2;
		getContentPane().add(new JLabel("Break:"), c);
		JTextArea breakTimeArea = new JTextArea("20");
		getContentPane().add(breakTimeArea, c);
		c.gridy = 3;
		getContentPane().add(new JLabel("Set Theme"), c);
		c.gridy = 4;
		JRadioButton lightButton = new JRadioButton("Light", true);
		lightButton.setActionCommand("light");
		getContentPane().add(lightButton, c);
		JRadioButton darkButton = new JRadioButton("Dark");
		darkButton.setActionCommand("dark");
		getContentPane().add(darkButton, c);
		
		ButtonGroup themeButtonGroup = new ButtonGroup();
		themeButtonGroup.add(lightButton);
		themeButtonGroup.add(darkButton);
		
		RadioListener myListener = new RadioListener();
		lightButton.addActionListener(myListener);
		darkButton.addActionListener(myListener);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	class RadioListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String themeColor = e.getActionCommand();
			if(themeColor.equals("light")){
				// code to change the theme
					// change font colors	
			}
			else if(themeColor.equals("dark")){
				
			}
		}
	}
	
}
