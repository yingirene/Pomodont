package focusbox;

import javax.swing.*;

public class TaskMenu extends JPanel {
	MainProgram prog = null;
	int selected;
	
	public TaskMenu(MainProgram p){
		super();
		prog = p;
	}
	
	public void addTask(String s, boolean toSetCurr){
		TaskClass t = new TaskClass(s);
		prog.tasks.add(t);
		if(toSetCurr){
			updateTaskDisp(prog.tasks.get(prog.currTask).taskText);
			selectTask(prog.tasks.indexOf(t));
			setTask();
		}
	}
	
	public void selectTask(int ind){
		//select the task to perform actions on
		selected = ind; //selected task
	}
	
	public void setTask(){
		updateTaskDisp(prog.tasks.get(prog.currTask).taskText);
		prog.currTask = selected;
		prog.hasTask = true;
	}
	
	public void removeTask(){
		//get selected task
		//remove task from the list
		removeTask(selected);
	}
	
	public void removeTask(int ind){
		//get selected task
		//remove task from the list
		prog.tasks.remove(ind);
		if(ind == prog.currTask){
			prog.hasTask = false;
			//change the current charPane to sleepCharPane
			getMain().charL.show(getMain().charPanel, "Sleeping Char");
			prog.gui.setTimerDisp("00:00:00");
			prog.gui.setTotTimerDisp("00:00:00");
			((TaskPane)prog.gui.taskPanel).taskDisp.setText("Add a task...");
			prog.gui.pack();
		}
	}
	
	public void saveTask(){
		
	}
	
	public void updateTaskDisp(String s){
		((TaskPane)prog.gui.taskPanel).taskDisp.setText(s);
		prog.gui.pack();
	}
	
	public MainPane getMain(){
		return (MainPane) prog.gui.mainPanel;
	}
}
