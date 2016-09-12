package focusbox;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import static java.util.concurrent.TimeUnit.*;

public class RunTimer implements Runnable {
	ImageIcon workChar1IMG = new ImageIcon(getClass().getResource("/images/idle1.png"));
	ImageIcon workChar2IMG = new ImageIcon(getClass().getResource("/images/idle2.png"));
	ImageIcon alertCharIMG = new ImageIcon(getClass().getResource("/images/alert.png"));
	ImageIcon pauseCharIMG = new ImageIcon(getClass().getResource("/images/sit.png"));
	ImageIcon angryChar1IMG = new ImageIcon(getClass().getResource("/images/angerup.png"));
	ImageIcon angryChar2IMG = new ImageIcon(getClass().getResource("/images/angerdown.png"));
	
	MainProgram prog;
	boolean stop, isBreak = false;
	alertNotify alert;
	lazyNotify lazy;
	animateChar anim;
	boolean runAlert, runLazy = false;
	// Duration of time for working
	int workDur = 1;
	// Duration of break time in minutes
	int breakDur = 1/2;

	
	ReentrantLock lock = new ReentrantLock();
	
	public RunTimer(MainProgram p){
		workChar1IMG = resizeImage(workChar1IMG, 100);
		workChar2IMG = resizeImage(workChar2IMG, 100);
		alertCharIMG = resizeImage(alertCharIMG, 100);
		pauseCharIMG = resizeImage(pauseCharIMG, 100);
		angryChar1IMG = resizeImage(angryChar1IMG, 100);
		angryChar2IMG = resizeImage(angryChar2IMG, 100);
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
		((MainPane)prog.gui.mainPanel).pauseLabel.setIcon(workChar2IMG);
		isBreak = false;
		alert.reset();
		lazy.reset();
	}
	
	public void addThreadsToPool(){
		ScheduledThreadPoolExecutor eventPool = new ScheduledThreadPoolExecutor(5);
		
		eventPool.scheduleAtFixedRate(new changeTime(), 0, 1, SECONDS);
		eventPool.scheduleAtFixedRate(alert = new alertNotify(), 0, 1, MINUTES);
		eventPool.scheduleAtFixedRate(lazy = new lazyNotify(), 0, 1, MINUTES);
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
		boolean frame;
		int count;
		int buffer = 50;
		public animateChar(){
			frame = true;
			count = 0;
		}
		public void run(){
			if(runAlert && !isBreak)
				alertAnimate();
			else if(runLazy && isBreak)
				lazyAnimate();
			else if(!isBreak && !runAlert) {
				idleAnimate();
			}
			count++;
			if(count > buffer) {
				frame = !frame;
				count = 0;
			}
		}
		public void idleAnimate(){
			if(!frame){
				((MainPane)prog.gui.mainPanel).workLabel.setIcon(workChar1IMG);
			}
			else if(frame){
				((MainPane)prog.gui.mainPanel).workLabel.setIcon(workChar2IMG);
			}
		}
		public void alertAnimate(){
			if(!frame){
				((MainPane)prog.gui.mainPanel).workLabel.setIcon(alertCharIMG);
			}
			else if(frame){
				((MainPane)prog.gui.mainPanel).workLabel.setIcon(workChar2IMG);
			}
		}
		public void lazyAnimate(){
			if(!frame){
				((MainPane)prog.gui.mainPanel).pauseLabel.setIcon(angryChar1IMG);
			}
			else if(frame){
				((MainPane)prog.gui.mainPanel).pauseLabel.setIcon(angryChar2IMG);
			}
		}
	}
	
	public ImageIcon resizeImage(ImageIcon icon, int size) {
		Image img = icon.getImage();
		Image ret = img.getScaledInstance(size, size, java.awt.Image.SCALE_FAST);
		return new ImageIcon(ret);
	}
}
