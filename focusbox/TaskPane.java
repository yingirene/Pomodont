package focusbox;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class TaskPane extends JPanel{
	ImageIcon toMenuIMG = new ImageIcon(getClass().getResource("/images/piggy.png"));
	ImageIcon toTaskIMG = new ImageIcon(getClass().getResource("/images/umbrella.png"));
	ImageIcon addTaskIMG = new ImageIcon(getClass().getResource("/images/watch.png"));
	ImageIcon delTaskIMG = new ImageIcon(getClass().getResource("/images/paper-plane.png"));
	ImageIcon finTaskIMG = new ImageIcon(getClass().getResource("/images/glove.png"));
	
	MainProgram prog = null;
	CardLayout cl;
	JTextArea taskDisp;
	
	public TaskPane(MainProgram p){
		prog = p;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		class bubbleBorder extends AbstractBorder{
			public bubbleBorder(){
			}
			
			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
				Color col = Color.BLACK;
				g.setColor(col);
			}
		}
		AbstractBorder border = new bubbleBorder();
		setBorder(border);
		
		//taskPanel: maybe cardLayout? can always see task menu button
		
		makeTextArea(c);
		dblClkText();
		add(taskDisp, c);
		//c.gridy = 1;
		//add((JPanel)useTaskButtons(), c);
		
	}
	
	public void dblClkText(){
		taskDisp.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event)
			{
			  if (event.getClickCount() == 2) {
			    if(prog.hasTask){
			    	new MakeTaskDialog(prog, true);
			    }
			    else{
			    	new MakeTaskDialog(prog, false);
			    }
			  }
			}
		});
	}
	
	public void makeTextArea(GridBagConstraints c){
		taskDisp = new JTextArea("Task text...");
		taskDisp.setEditable(false);
		taskDisp.setBackground(null);
		taskDisp.setBorder(null);
		taskDisp.setOpaque(false);
		taskDisp.setLineWrap(true);
		taskDisp.setWrapStyleWord(true);
		taskDisp.setMargin(new Insets(0,0,0,0));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 5;
	}
	
	public JPanel useTaskButtons(){
		JPanel enclosingPane = new JPanel();
		JPanel noTaskPane = new JPanel();
		JPanel mainTaskPane = new JPanel(new GridBagLayout()); //display current task and buttons
		JPanel taskMenuPane = new JPanel();
		
		enclosingPane.setBackground(new Color(0, 0, 0, 0));
		noTaskPane.setBackground(new Color(0, 0, 0, 0));
		mainTaskPane.setBackground(new Color(0, 0, 0, 0));
		taskMenuPane.setBackground(new Color(0, 0, 0, 0));
		
		CardLayout cl = new CardLayout();
		
		class taskMenuButton extends iconButton {
			public taskMenuButton(CardLayout cl){
				super(toMenuIMG);
				addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						cl.show(enclosingPane, "Task Menu");
					}
				});
			}
		}
		
		JButton buttonMainTask = new iconButton(toTaskIMG);
		JButton buttonMenu1 = new taskMenuButton(cl);
		JButton buttonMenu2 = new taskMenuButton(cl);
		
		enclosingPane.setLayout(cl);
		
		enclosingPane.add(noTaskPane, "No Task");
		enclosingPane.add(mainTaskPane, "Has Task");
		enclosingPane.add(taskMenuPane, "Task Menu");
		if(!prog.hasTask)
			cl.show(enclosingPane, "No Task");
		else
			cl.show(enclosingPane, "Has Task");

		buttonMainTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if(prog.hasTask){
					cl.show(enclosingPane, "Has Task");
					taskDisp.setText(prog.tasks.get(prog.currTask).taskText);
					prog.gui.pack();
				}
				else
					cl.show(enclosingPane, "No Task");
			}
		});
		
		/* noTaskPane - if no task selected card */
		noTaskPane.add(buttonMenu1);
		//choose a task/add a task by going to the task menu
		
		/* mainTaskPane - if task is selected card */
		
		JButton delTask = new iconButton(delTaskIMG);
		delTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(prog.getNumTasks() > 0){
					if(prog.mainTimer.isAlive())
						((RunTimer)prog.runTimer).stopTimer();
					//delete current task
					prog.mainMenu.removeTask(prog.currTask);
					if(prog.getNumTasks() < 1)
						cl.show(enclosingPane, "No Task");
				}
			}
		});
		JButton finishTask = new iconButton(finTaskIMG); //or double click to finish
		finishTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				if(prog.getNumTasks() > 0){
					if(prog.mainTimer.isAlive())
						((RunTimer)prog.runTimer).stopTimer();
					prog.getCurrTask().finishTask();
					//delete current task
					prog.mainMenu.removeTask(prog.currTask);
					if(prog.getNumTasks() < 1)
						cl.show(enclosingPane, "No Task");
				}
			}
		});
		
		GridBagConstraints mainC = new GridBagConstraints();
		
		mainC.fill = GridBagConstraints.NONE;
		//mainC.anchor = GridBagConstraints.PAGE_END;
		mainC.gridx = 0;
		mainC.gridy = 0;
		mainC.weighty = 0;
		mainTaskPane.add(buttonMenu2, mainC);
		mainC.gridx = 1;
		mainTaskPane.add(delTask, mainC);
		mainC.gridx = 2;
		mainTaskPane.add(finishTask, mainC);
		
		/* Task Menu Pane
		 * - Add tasks
		 * */
		//task menu(button for menu is always available when not in menu)
			// only add task from task menu
			// after task is added, switch to pausedChar
		JButton buttonAddTask = new iconButton(addTaskIMG);
		buttonAddTask.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				new MakeTaskDialog(prog, false);
			}
		});
		
		taskMenuPane.add(buttonMainTask);
		taskMenuPane.add(buttonAddTask);
		
			//to do list
			// click task in list to set current task
			// add task button
				//new dialog to add task
				//	maybe new menu
		return enclosingPane;
	}
	
	public int getPanelHeight(){
		return 100;
	}
}
