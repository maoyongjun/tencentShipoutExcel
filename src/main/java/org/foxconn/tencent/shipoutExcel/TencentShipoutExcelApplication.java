package org.foxconn.tencent.shipoutExcel;

import java.io.IOException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("org.foxconn.tencent.shipoutExcel.dao")
public class TencentShipoutExcelApplication   extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TencentShipoutExcelApplication.class); 
		
	}
	public static void main(String[] args) throws IOException {
		SpringApplication.run(TencentShipoutExcelApplication.class, args);
	}
}
	