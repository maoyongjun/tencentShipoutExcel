 package org.foxconn.tencent.shipoutExcel.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.foxconn.tencent.shipoutExcel.service.WriteExcelService;

public class ScheduleRunner {
	Logger logger = Logger.getLogger(ScheduleRunner.class);
	ScheduledExecutorService taskService = Executors.newScheduledThreadPool(10);;
	List<Runnable> oneDayRunnables = new ArrayList<Runnable>();
	public void run() {
		addRunnables();
		for(Runnable runnable : oneDayRunnables){
			taskService.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.SECONDS);
		}
	}
	
	public void addRunnables(){
		addSendExcelRunnable();
	}
	
	public void addSendExcelRunnable(){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				logger.info("send pcs task Begin");
				WriteExcelService service = new WriteExcelService();
				try {
					service.writeExcle();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		oneDayRunnables.add(runnable);
	}
	
}
