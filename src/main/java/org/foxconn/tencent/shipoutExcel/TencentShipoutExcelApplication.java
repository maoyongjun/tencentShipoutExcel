package org.foxconn.tencent.shipoutExcel;

import java.io.IOException;

import org.foxconn.tencent.shipoutExcel.schedule.ScheduleRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("org.foxconn.bootstrapTest.dao")
public class TencentShipoutExcelApplication  extends SpringBootServletInitializer{
	
	public static void main(String[] args) throws IOException {
		ScheduleRunner runner = new ScheduleRunner();
		runner.run();
	}
}
	