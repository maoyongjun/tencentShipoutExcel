package org.foxconn.tencent.shipoutExcel.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
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
import org.foxconn.tencent.shipoutExcel.dao.OsMsgDao;
import org.foxconn.tencent.shipoutExcel.entity.Component;
import org.foxconn.tencent.shipoutExcel.entity.OsMsgModel;
import org.foxconn.tencent.shipoutExcel.entity.Result;
import org.foxconn.tencent.shipoutExcel.entity.SystemModel;
import org.foxconn.tencent.shipoutExcel.schedule.ScheduleRunner;
import org.foxconn.tencent.shipoutExcel.util.ToStringArrayUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@Service
@RestController
public class WriteExcelService {
	
	@Resource
	OsMsgDao osMsgDao;
	Logger logger = Logger.getLogger(ScheduleRunner.class);
	
	@Resource
	HttpServletRequest request;
	@Resource
	HttpServletResponse response;
	
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
	@PostMapping(value="/file")
	public String getWriteExcleName(String startTime,String endTime,String sn) throws ParseException{
		logger.info("---create excel begin---");
		Map<String,Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
		//System.out.println("-----------"+startTime);
		if(null!=startTime){
			map.put("startTime", startTime);
		}
		if(null!=sn){
			map.put("ssn", sn);
		}
		if(null!=endTime){
			map.put("endTime", endTime);
		}
		String fileName ="";
		try {
			fileName = writeExcle(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("---create excel  end---");
		return fileName;
	}
	
	public String writeExcle(Map<String,Object> map) throws IOException {
		Map<String,List<SystemModel>> allsystems = getOsMsg(map);
		List<Component> ls = null;
		FileOutputStream output = null;
		String filePath = new Date().getTime()+".xlsx";
		try {
			output = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 读取的文件路径
//		logger.info(ls);
		SXSSFWorkbook wb = new SXSSFWorkbook(10000);// 内存中保留 10000 条数据，以免内存溢出，其余写入 硬盘
		
		int sheetindex=0;
		for(Entry<String,List<SystemModel>> systemsEntry:allsystems.entrySet()){
			String sheetname = systemsEntry.getKey();
			List<SystemModel> systems = systemsEntry.getValue();
			Sheet sheet = wb.createSheet(String.valueOf(sheetindex));
			wb.setSheetName(sheetindex++, sheetname);
//			CellStyle cellStyle =  wb.createCellStyle();
//			cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
//			cellStyle.setBorderBottom(CellStyle.BORDER_DASHED);
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
//			createFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
			int index =1;
			for (int j = 0; j < systems.size(); j++) {
//				System.out.println(systems.get(j).toString());
				SystemModel system = systems.get(j);
				ls = system.getComponent();
				
				int width = 0;
//				System.out.println(ls);
				ArrayList<String[]> ls2 =null;
				if(ls!=null) {
					ls2 = (ArrayList<String[]>) ToStringArrayUtil.toStringArray(ls);
					
				}
				
				for (int i = 0; i < ls2.size(); i++) {
					row = sheet.createRow(index+i);
					
					if(i==0){
						String[] snmaster =system.toStringArray();
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
//						StylesTable table = new StylesTable();
//						CellStyle style = table.createCellStyle();
//						style.setBorderTop((short)6);
//						cell.setCellStyle(style);
//						if (null != s[cols]) {
//							sheet.setColumnWidth(cols, ((width=s[cols].length())<6?6:width)*384); //设置单元格宽度  
//						}
						cell.setCellValue(s[cols]);// 写入内容
					}
				}
				index+=ls2.size();

			}
		}
		
		
		try {
			if (null != output) {
				wb.write(output);
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	private Map<String,List<SystemModel>> getOsMsg(Map<String,Object> map){
		OsMsgModel model =null;
		try {
			model = osMsgDao.findAll(map);
		} catch (Exception e) {
			String errorMsg = e.getCause().toString();
			logger.error(errorMsg);
		}
		
		String json = model.getJson();
		if(json.indexOf("\"cpu\":{")!=-1){
			json = json.replace("\"cpu\":{", "\"cpu\":[{");
			json = json.replace("},\"hdd\"", "}],\"hdd\"");
		}
		json = json.replace(",,", ",");
		List<Result> results =  parseJson(json);
		//System.out.println(models);
		 Map<String,List<SystemModel>> lists = new HashMap<String,List<SystemModel>>();
		for(Result result:results){
			lists.put(result.getSkuno(), result.getSystem());
		}
		return lists;
	}
	
	
	private List<Result> parseJson(String json){
		if(json==null){
			return null;
		}
		SystemModel system =null;
		List<Result> results = JSON.parseArray(json, Result.class);
		return results;
	}
	
	
	private List<SystemModel> getSystemModel() throws IOException{
		File file = new File("");
		String path = file.getAbsolutePath();
		List<SystemModel> systems = new ArrayList<SystemModel>();
		String json = getJSON(path+"/src/main/resources/"+"7CE829P6TL.json");
		SystemModel system = null;
		Result result = JSON.parseObject(json, Result.class);
		if(result!=null){
			system = result.getSystem().get(0);
		}
		if(system!=null){
			systems.add(system);
		}
		json = getJSON(path+"/src/main/resources/"+"7CE830P1N7.json");
		result= new Result();
		result = JSON.parseObject(json, Result.class);
		if(result!=null){
			system = result.getSystem().get(0);
		}
		if(system!=null){
			systems.add(system);
		}
		return systems;
	}
	
	
	@GetMapping(value="/download")
	public void downLoadFile() throws Exception{
		String fileName = request.getParameter("fileName");
		//2、下载
		File file = new File(fileName);
		fileName = new String(fileName.getBytes("gb2312"),"ISO8859_1");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		int len = (int)file.length();
		byte []buf = new byte[len];
		FileInputStream fis = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		len = fis.read(buf);
		out.write(buf, 0, len);
		out.flush();
		fis.close();
		file.delete();
	}
}
