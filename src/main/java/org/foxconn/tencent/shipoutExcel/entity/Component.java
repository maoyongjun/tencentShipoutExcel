package org.foxconn.tencent.shipoutExcel.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashPrintJobAttributeSet;

import org.apache.log4j.Logger;
import org.foxconn.tencent.shipoutExcel.sap.MMprodmasterSAPClient;

import com.sap.conn.jco.JCoException;

public class Component extends BaseStringArray{
	private String pn;
	private String sn;
	private String fw;
	private String type;
	private String efoxpn;
	private String efoxsn;
	public static Map<String,String> pnmap= new HashMap<String, String>();
	
	private List<Component> efoxCpu= new ArrayList<Component>();
	
	private List<Component> efoxbp= new ArrayList<Component>();
	
	private List<Component> efoxSSD= new ArrayList<Component>();
	

	private List<Component> efoxmemory= new ArrayList<Component>();
	
	private List<Component> efoxpsu= new ArrayList<Component>();
	
	private List<Component> efoxhdd= new ArrayList<Component>();
	
	private List<Component> efoxboard= new ArrayList<Component>();
	
	protected Map<String, List<Component>> efoxComponent = new HashMap<String, List<Component>>();
	
	public List<Component> getEfoxhdd() {
		return efoxhdd;
	}

	public void setEfoxhdd(List<Component> efoxhdd) {
		efoxComponent.put("HDD", efoxhdd);
		this.efoxhdd = efoxhdd;
	}

	public List<Component> getEfoxboard() {
		return efoxboard;
	}

	public void setEfoxboard(List<Component> efoxboard) {
		efoxComponent.put("MB", efoxboard);
		//logger.info(efoxboard);
		this.efoxboard = efoxboard;
	}

	public List<Component> getEfoxSSD() {
		return efoxSSD;
	}

	public void setEfoxSSD(List<Component> efoxSSD) {
		efoxComponent.put("SSD", efoxSSD);
		this.efoxSSD = efoxSSD;
	}

	public List<Component> getEfoxmemory() {
		return efoxmemory;
	}

	public void setEfoxmemory(List<Component> efoxmemory) {
		efoxComponent.put("MEMORY", efoxmemory);
		this.efoxmemory = efoxmemory;
	}

	public List<Component> getEfoxpsu() {
		
		return efoxpsu;
	}

	public void setEfoxpsu(List<Component> efoxpsu) {
		efoxComponent.put("PSU", efoxpsu);
		this.efoxpsu = efoxpsu;
	}

	public String getEfoxpn() {
		return efoxpn;
	}

	public void setEfoxpn(String efoxpn) {
		this.efoxpn = efoxpn;
	}

	public String getEfoxsn() {
		return efoxsn;
	}

	public void setEfoxsn(String efoxsn) {
		this.efoxsn = efoxsn;
	}

	public List<Component> getEfoxbp() {
		return efoxbp;
	}

	public void setEfoxbp(List<Component> efoxbp) {
		efoxComponent.put("BP", efoxbp);
		this.efoxbp = efoxbp;
	}

	public List<Component> getEfoxCpu() {
		return efoxCpu;
	}

	public void setEfoxCpu(List<Component> efoxCpu) {
		efoxComponent.put("CPU", efoxCpu);
		this.efoxCpu = efoxCpu;
	}

	
	private Logger logger = Logger.getLogger(Component.class);
	public String getFw() {
		return fw;
	}
	public void setFw(String fw) {
		if(null==fw || "null".equalsIgnoreCase(fw)){
			fw="";
		}
		this.fw = fw;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	@Override
	public String toString() {
		return "Component [pn=" + pn + ", sn=" + sn + ", fw=" + fw + "]";
	}
	@Override
	public String[] toStringArray() {
		// TODO Auto-generated method stub
		return new String[]{getType(),pn,sn,fw,efoxpn,efoxsn};
	}
	@Override
	public String[] getHeader() {
		// TODO Auto-generated method stub
		return new String[]{"type","pn","sn","fw","efoxpn","efoxsn"};
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void addList(List<Component> component,List<Component> subcomponent,String type){
		MMprodmasterSAPClient client =null;
		
		for(Component sub:subcomponent){
			if(sub.getType()==null||"".equals(sub.getType().trim())){
				sub.setType(type);
			}
			//从sap获取供应商料号信息
			if("CABLE".equals(type)||"FAN".equals(type)){
				client = new MMprodmasterSAPClient();
				String mfrpn=null;
				String pn = sub.getPn(); 				
				mfrpn = pnmap.get(pn);
				//设置值的时候会覆盖掉原值
				if(null==mfrpn){
					  for(int i=0;i<3;i++){
							if(mfrpn==null){
								try {
									mfrpn = client.downMMprodmastercalls(sub.getPn());
								} catch (JCoException e) {
									logger.error(e.getCause().toString());
								}
							}
						}
						pnmap.put(pn, mfrpn);
						sub.setPn(mfrpn);
						logger.info("pn:"+pn+",mfrpn:"+mfrpn);
					
				}else{
					sub.setPn(mfrpn);
				}
				
			}
		
		}
		
		
		
		
		client=null;
		
	 if("BP".equals(type)||"MEMORY".equals(type)||"PSU".equals(type)||"HDD".equals(type)||"CPU".equals(type)||"NIC".equals(type)){
			int index=0;
			
			for(Component sub:subcomponent){
				if("BP".equals(type)){
					if(sub.getFw()!=null&&!"".equals(sub.getFw().trim())&&null!=efoxComponent.get(type)&&efoxComponent.get(type).size()>0){
						sub.setSn(efoxComponent.get(type).get(0).getSn());
						sub.setPn(efoxComponent.get(type).get(0).getPn());
						sub.setEfoxpn(efoxComponent.get(type).get(index).getPn());
						sub.setEfoxsn(efoxComponent.get(type).get(index++).getSn());
					}
				}else{
					if(efoxComponent.get(type)==null||efoxComponent.get(type).size()<=index){
						break;
					}
					if("CPU".equals(type)){
						sub.setSn(efoxComponent.get(type).get(index).getSn());
					}
					if("NIC".equals(type)){
						if(sub.getSn().indexOf(":")!=-1){
							continue;
						}else{
							sub.setEfoxpn(efoxComponent.get(type).get(index).getPn());
							sub.setEfoxsn(efoxComponent.get(type).get(index).getSn());
							continue;
						}
					}
					sub.setEfoxpn(efoxComponent.get(type).get(index).getPn());
					sub.setEfoxsn(efoxComponent.get(type).get(index++).getSn());
				}
				
			}
		
		}
		component.addAll(subcomponent);
	}
	
	public void addList(List<Component> component,Component sub,String type){
		if(sub.getType()==null||"".equals(sub.getType().trim())){
			sub.setType(type);
		}
		if("MB".equals(sub.getType())){
			
			if(efoxComponent.get(type)!=null||efoxComponent.get(type).size()>0){
				sub.setEfoxpn(efoxComponent.get(type).get(0).getPn());
				sub.setEfoxsn(efoxComponent.get(type).get(0).getSn());
			}
		}
		component.add(sub);
	}
	
}
