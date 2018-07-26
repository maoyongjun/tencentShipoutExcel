package org.foxconn.tencent.shipoutExcel.config;

import javax.annotation.Resource;

import org.foxconn.tencent.shipoutExcel.schedule.ScheduleRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfig {
	@Resource
	ScheduleRunner runer;
	
	@Bean
	public ScheduleManger scheduleManger(){
		runer.run();
		ScheduleManger manager = new ScheduleManger();
		return manager;
	}
	
}
