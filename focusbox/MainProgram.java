package focusbox;

import java.util.*;

public class MainProgram {
	MainGui gui;
	TaskMenu mainMenu; 
	Thread mainTimer;
	Runnable runTimer;
	
	int currTask = 0;
	boolean hasTask = false;
	ArrayList<TaskClass> tasks = new ArrayList<TaskClass>();
	
	public MainProgram(){
		mainMenu = new TaskMenu(this);
		runTimer = new RunTimer(this);
		mainTimer = new Thread(runTimer);
		gui = new MainGui(this);
	}
	
	public int getNumTasks(){
		return tasks.size();
	}
	
	public TaskClass getCurrTask(){
		return tasks.get(currTask);
	}
	
	public void resetProg(){
		currTask = 0;
		tasks.clear();
		hasTask = false;
	}
	
	public static void main(String[] args){
		new MainProgram();
	}
}
