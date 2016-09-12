package focusbox;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import focusbox.MainGui.RightClickMenu;

class MakeTaskDialog extends JDialog{
	MainProgram prog;
	boolean toSetCurr = true;
	JLabel instructionText;
	
	public MakeTaskDialog(MainProgram p, boolean toEdit){
		super();
		prog = p;
		setTitle("Add a Task");
		setAlwaysOnTop(true);
		setSize(250, 200);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1; 
		
		JTextArea taskTextArea = new JTextArea("");
		taskTextArea.setLineWrap(true);
		taskTextArea.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(taskTextArea);
		
		if(!toEdit){
			instructionText = new JLabel("Type in your task.");
		}
		else{
			instructionText = new JLabel("Edit your task.");
			taskTextArea.setText(prog.getCurrTask().taskText);
		}
		c.insets = new Insets(10,10,0,10);
		c.gridy = 0;
		getContentPane().add(instructionText, c);
		
		c.insets = new Insets(10,10,0,10);
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		c.weighty = 1; 
		getContentPane().add(scroll, c);
		
		/* If multiple tasks are used */
		useMultipleTasks(c, false);
		
		if(!toEdit){
			JButton addTaskButton = new JButton("Add Task");
			addTaskButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					prog.mainMenu.addTask(taskTextArea.getText(), toSetCurr);
					((RightClickMenu)prog.gui.rcMenu).resetItem.setEnabled(true);
					((RightClickMenu)prog.gui.rcMenu).editItem.setEnabled(true);
					((RightClickMenu)prog.gui.rcMenu).addItem.setEnabled(false);
					dispose();
				}
			});
			c.insets = new Insets(10,10,10,10);
			c.gridy = 3;
			c.weighty = 0; 
			getContentPane().add(addTaskButton, c);
		}
		else{
			JButton editTaskButton = new JButton("Save Changes");
			editTaskButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					prog.getCurrTask().taskText = taskTextArea.getText();
					prog.mainMenu.updateTaskDisp(taskTextArea.getText());
					dispose();
				}
			});
			c.insets = new Insets(10,10,10,10);
			c.gridy = 3;
			c.weighty = 0; 
			getContentPane().add(editTaskButton, c);
		}
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void useMultipleTasks(GridBagConstraints c, boolean use){
		if(use){
			JCheckBox enableSet = new JCheckBox("Set Current Task");
			enableSet.setMnemonic(KeyEvent.VK_C); 
			enableSet.setSelected(true);
			enableSet.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
					Object source = e.getItemSelectable();
					if(source == enableSet){
						toSetCurr = false;
					}
				}
			});
			c.insets = new Insets(10,10,0,10);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridy = 2;
			c.weighty = 0; 
			getContentPane().add(enableSet, c);
		}
	}
}