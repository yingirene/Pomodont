package focusbox;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class TaskClass {
	long startTime, endTime;
	long sessionStart, sessionEnd;
	long timeElapsed; //in seconds
	String timeString, totalTimeString = "00:00:00";
	String taskText = "";
	boolean newTask = true; 
	int h, m, s;
	int h2, m2, s2;
	
	public TaskClass(String s){
		taskText = s;
		timeElapsed = 0;
	}
	
	public void start(){
		sessionStart = System.nanoTime();
		if(newTask){
			startTime = sessionStart;
			newTask = false;
		}
	}
	
	public void stop(){
		sessionEnd = System.nanoTime();
	}
	
	public void finishTask(){
		stop();
		endTime = sessionEnd;
	}
	
	public String currTimeString(){
		s++;
		if(s > 59){
			m++;
			s = 0;
		}
		if(m > 59){
			h++;
			m = 0;
		}
		String st = "";
		if(h < 10)
			st += "0" + h + ":";
		else
			st += h + ":";
		if(m < 10)
			st += "0" + m + ":";
		else
			st += m + ":";
		if(s < 10)
			st += "0" + s;
		else
			st += s;
		timeString = st;
		return st;
	}
	
	public String currTotalTimeString(){
		s2++;
		if(s2 > 59){
			m2++;
			s2 = 0;
		}
		if(m2 > 59){
			h2++;
			m2 = 0;
		}
		String st = "";
		if(h2 < 10)
			st += "0" + h2 + ":";
		else
			st += h2 + ":";
		if(m2 < 10)
			st += "0" + m2 + ":";
		else
			st += m2 + ":";
		if(s2 < 10)
			st += "0" + s2;
		else
			st += s2;
		totalTimeString = st;
		return st;
	}
	
	public String endTimeString(){
		/*
		String s = "";
		int hour = (int)(timeElapsed/3600);
		int min = (int)(timeElapsed%3600);
		int sec = (int)(timeElapsed%3600);
		if(hour < 10)
			s += "0" + hour + ":";
		else
			s += hour + ":";
		if(min < 10)
			s += "0" + min + ":";
		else
			s += min + ":";
		if(sec < 10)
			s += "0" + sec;
		else
			s += sec;
		timeString = s;
		return s;
		*/
		return currTotalTimeString();
	}
	
	public long getTimeElapsed(){
		timeElapsed = ((sessionStart - sessionEnd) / 1000000);
		return timeElapsed;
	}
	
	public String getFormattedTime(){
		return timeString;
	}
}
