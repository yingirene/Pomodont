package focusbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import static java.util.concurrent.TimeUnit.*;

public class RunTimer implements Runnable {
	ImageIcon workCharIMG = new ImageIcon(getClass().getResource("/images/cloud-refresh.png"));
	ImageIcon alertCharIMG = new ImageIcon(getClass().getResource("/images/toaster.png"));
	ImageIcon pauseCharIMG = new ImageIcon(getClass().getResource("/images/cloud-up.png"));
	ImageIcon angryChar1IMG = new ImageIcon(getClass().getResource("/images/packman.png"));
	ImageIcon angryChar2IMG = new ImageIcon(getClass().getResource("/images/eye.png"));
	
	MainProgram prog;
	boolean stop, isBreak = false;
	alertNotify alert;
	lazyNotify lazy;
	animateChar anim;
	boolean runAlert, runLazy = false;
	final int MINUTE = 60;
	// Duration of time for working
	int workDur = 30 * MINUTE;
	// Duration of break time in minutes
	int breakDur = 20 * MINUTE;

	
	ReentrantLock lock = new ReentrantLock();
	
	public RunTimer(MainProgram p){
		prog = p;
		stop = true;
	}
	
	@Override
	public void run() {
			lock.lock();
			//code to be run every second
			addThreadsToPool();
			lock.unlock();
			try{
				Thread.sleep(1000);
			}catch (InterruptedException e){}
	}
	
	public void stopTimer(){
		stop = true;
		alert.reset();
		lazy.reset();
	}
	
	public void startTimer(){
		stop = false;
		offBreak();
	}
	
	public void onBreak(){
		((MainPane)prog.gui.mainPanel).pauseLabel.setIcon(pauseCharIMG);
		isBreak = true;
		alert.reset();
		lazy.reset();
	}
	
	public void offBreak(){
		((MainPane)prog.gui.mainPanel).workLabel.setIcon(workCharIMG);
		isBreak = false;
		alert.reset();
		lazy.reset();
	}
	
	public void addThreadsToPool(){
		ScheduledThreadPoolExecutor eventPool = new ScheduledThreadPoolExecutor(5);
		
		eventPool.scheduleAtFixedRate(new changeTime(), 0, 1, SECONDS);
		eventPool.scheduleAtFixedRate(alert = new alertNotify(), 0, 1, SECONDS);
		eventPool.scheduleAtFixedRate(lazy = new lazyNotify(), 0, 1, SECONDS);
		eventPool.scheduleAtFixedRate(anim = new animateChar(), 0, 10, MILLISECONDS);
	}
	
	class changeTime implements Runnable{
		public changeTime(){
		}
		public void run(){
			if(!isBreak && !stop)
				prog.gui.setTimerDisp(prog.getCurrTask().currTimeString());
			if(!stop)
				prog.gui.setTotTimerDisp(prog.getCurrTask().currTotalTimeString());
		}
	}
	
	/* Alert after given time for breaks and stuff */
	class alertNotify implements Runnable{
		int minPassed = 0;
		public alertNotify(){
		}
		public void run(){
			if(!isBreak && !stop){
				if(minPassed >= workDur){
					runAlert = true;
				}
				minPassed++;
			}
		}
		public void reset(){
			runAlert = false;
			minPassed = 0;
		}
	}
	
	/* Paused char gets angry after being paused for a given amount of time */
	class lazyNotify implements Runnable{
		int minPassed = 0;
		Timer t;
		int frame = 0;
		public lazyNotify(){
		}
		public void run(){
			if(isBreak && !stop){
				if(minPassed >= breakDur){
					runLazy = true;
				}
				minPassed++;
			}
		}
		public void reset(){
			runLazy = false;
			minPassed = 0;
		}
	}
	
	class animateChar implements Runnable{
		int frame;
		public animateChar(){
			frame = 0;
		}
		public void run(){
			if(runAlert && !isBreak)
				alertAnimate();
			else if(runLazy && isBreak)
				lazyAnimate();
		}
		public void alertAnimate(){
			if(frame == 0){
				((MainPane)prog.gui.mainPanel).workLabel.setIcon(alertCharIMG);
				frame = 1;
			}
			else if(frame == 1){
				((MainPane)prog.gui.mainPanel).workLabel.setIcon(workCharIMG);
				frame = 0;
			}
		}
		public void lazyAnimate(){
			if(frame == 0){
				((MainPane)prog.gui.mainPanel).pauseLabel.setIcon(angryChar1IMG);
				frame = 1;
			}
			else if(frame == 1){
				((MainPane)prog.gui.mainPanel).pauseLabel.setIcon(angryChar2IMG);
				frame = 0;
			}
		}
	}
}
