package org.foxconn.tencent.shipoutExcel.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.foxconn.tencent.shipoutExcel.entity.Component;
import org.foxconn.tencent.shipoutExcel.entity.Result;
import org.foxconn.tencent.shipoutExcel.entity.SystemModel;
import org.foxconn.tencent.shipoutExcel.util.ToStringArrayUtil;

import com.alibaba.fastjson.JSON;

public class WriteExcelService {
	

	public String getJSON(String path) throws IOException {
		File file = new File(path);
		Reader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		StringBuffer sb = new StringBuffer("");
		String str = "";
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
//		System.out.println(sb);
//		Map map = (Map) JSON.parse(sb.toString());
//		System.out.println(map);
		str= sb.toString().replace("/t", "");
		return sb.toString();
	}

	public void writeExcle() throws IOException {
		List<SystemModel> systems = getSystemModel();
		List<Component> ls = null;
		FileOutputStream output = null;
		try {
			output = new FileOutputStream("d:\\整機部件驗收清單.xlsx");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 读取的文件路径
		SXSSFWorkbook wb = new SXSSFWorkbook(10000);// 内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
		Sheet sheet = wb.createSheet(String.valueOf(0));
		wb.setSheetName(0, "整機部件驗收清單");
//		CellStyle cellStyle =  wb.createCellStyle();
//		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
//		cellStyle.setBorderBottom(CellStyle.BORDER_DASHED);
		String[] headers = new String[]{"采购单号","服务器固资号","服务器SN号","厂商","机型","设备类型"
				,"版本","部件类型","部件原厂PN（支持采集的为OS采集PN)","部件原厂SN （支持采集的为OS采集SN）"
				,"部件FW版本"};
		Row row = sheet.createRow(0);
		Font createFont = wb.createFont();
		CellStyle createCellStyle = wb.createCellStyle();
		createFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		createCellStyle.setFont(createFont);
		createCellStyle.setWrapText(true);//
		createCellStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		createCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		createCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		createCellStyle.setBorderRight(CellStyle.BORDER_THIN);
		createCellStyle.setBorderTop(CellStyle.BORDER_THIN);
		createCellStyle.setFillBackgroundColor(CellStyle.SOLID_FOREGROUND);
		CellStyle createCellStyle2 = wb.createCellStyle();
		createCellStyle2.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		createCellStyle2.setBorderBottom(CellStyle.BORDER_THIN);
		createCellStyle2.setBorderLeft(CellStyle.BORDER_THIN);
		createCellStyle2.setBorderRight(CellStyle.BORDER_THIN);
		createCellStyle2.setBorderTop(CellStyle.BORDER_THIN);
		
		CellStyle createCellStyle3 = wb.createCellStyle();
		createCellStyle3.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		createCellStyle3.setBorderBottom(CellStyle.BORDER_THIN);
		createCellStyle3.setBorderLeft(CellStyle.BORDER_THIN);
		createCellStyle3.setBorderRight(CellStyle.BORDER_THIN);
		createCellStyle3.setBorderTop(CellStyle.BORDER_THIN);
		createCellStyle3.setFillForegroundColor(HSSFColor.YELLOW.index);
	    //solid 填充  foreground  前景色
		createCellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    
		for(int i=0;i<headers.length;i++){
			Cell cell0 = row.createCell(i);
			cell0.setCellType(XSSFCell.CELL_TYPE_STRING);// 文本格式
			cell0.setCellValue(headers[i]);// 写入内容
			cell0.setCellStyle(createCellStyle);
			if(i<5){
				sheet.setColumnWidth(i,4000);
			}else{
				sheet.setColumnWidth(i,8000);
			}
		}
//		createFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		int index =1;
		for (int j = 0; j < systems.size(); j++) {
			System.out.println(systems.get(j).toString());
			SystemModel system = systems.get(j);
			ls = system.getComponent();
			
			int width = 0;
			System.out.println(ls);
			ArrayList<String[]> ls2 =null;
			if(ls!=null) {
				ls2 = (ArrayList<String[]>) ToStringArrayUtil.toStringArray(ls);
				
			}
			
			for (int i = 0; i < ls2.size(); i++) {
				row = sheet.createRow(index+i);
				
				if(i==0){
					String[] snmaster = new String[]{"POGO2018***","TYSV180404**",system.getSn(),"FOXCONN","RM760-FX","SC3-10G","6.0.0.10"};
					for(int k=0;k<snmaster.length;k++){
						Cell cell0 = row.createCell(k);
						cell0.setCellType(XSSFCell.CELL_TYPE_STRING);// 文本格式
						cell0.setCellValue(snmaster[k]);// 写入内容
						if(k<=3){
							cell0.setCellStyle(createCellStyle2);
						}else{
							cell0.setCellStyle(createCellStyle3);
							
						}
					}
				}else{
					String[] snmaster = new String[]{"","","","","","",""};
					for(int k=0;k<snmaster.length;k++){
						Cell cell0 = row.createCell(k);
						cell0.setCellType(XSSFCell.CELL_TYPE_STRING);// 文本格式
						cell0.setCellValue(snmaster[k]);// 写入内容
						cell0.setCellStyle(createCellStyle2);
					}
				}
				
				String[] s = ls2.get(i);
				for (int cols = 0; cols < s.length; cols++) {
					Cell cell = row.createCell(cols + 7);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);// 文本格式
					cell.setCellStyle(createCellStyle2);
					XSSFCellBorder border = new XSSFCellBorder();
					border.setBorderStyle(BorderSide.TOP,BorderStyle.MEDIUM);
//					StylesTable table = new StylesTable();
//					CellStyle style = table.createCellStyle();
//					style.setBorderTop((short)6);
//					cell.setCellStyle(style);
//					if (null != s[cols]) {
//						sheet.setColumnWidth(cols, ((width=s[cols].length())<6?6:width)*384); //设置单元格宽度  
//					}
					cell.setCellValue(s[cols]);// 写入内容
				}
			}
			index+=ls2.size();

		}
		try {
			if (null != output) {
				wb.write(output);
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private List<SystemModel> getSystemModel() throws IOException{
		File file = new File("");
		String path = file.getAbsolutePath();
		List<SystemModel> systems = new ArrayList<SystemModel>();
		String json = getJSON(path+"/src/main/resources/"+"7CE829P6TL.json");
		SystemModel system = null;
		Result result = JSON.parseObject(json, Result.class);
		if(result!=null){
			system = result.getSystem();
		}
		if(system!=null){
			systems.add(system);
		}
		json = getJSON(path+"/src/main/resources/"+"7CE830P1N7.json");
		result= new Result();
		result = JSON.parseObject(json, Result.class);
		if(result!=null){
			system = result.getSystem();
		}
		if(system!=null){
			systems.add(system);
		}
		return systems;
	}
}
