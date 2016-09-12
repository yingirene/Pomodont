package focusbox;

import java.util.*;
import java.text.DateFormat;

public class ProgTimer extends Thread{
	public void run(){
		//code to be run every second
		try{
			Thread.sleep(1000); //sleep 1 sec
		}catch (InterruptedException e){}
	}
}
