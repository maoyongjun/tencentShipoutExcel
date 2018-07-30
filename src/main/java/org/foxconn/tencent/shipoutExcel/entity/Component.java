package org.foxconn.tencent.shipoutExcel.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.foxconn.tencent.shipoutExcel.sap.MMprodmasterSAPClient;

import com.sap.conn.jco.JCoException;

public class Component extends BaseStringArray{
	private String pn;
	private String sn;
	private String fw;
	private String type;
	public static Map<String,String> pnmap= new HashMap<String, String>();
	
	private List<Component> efoxCpu= new ArrayList<Component>();
	
	private List<Component> efoxbp= new ArrayList<Component>();
	
	
	public List<Component> getEfoxbp() {
		return efoxbp;
	}

	public void setEfoxbp(List<Component> efoxbp) {
		this.efoxbp = efoxbp;
	}

	public List<Component> getEfoxCpu() {
		return efoxCpu;
	}

	public void setEfoxCpu(List<Component> efoxCpu) {
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
		return new String[]{getType(),pn,sn,fw};
	}
	@Override
	public String[] getHeader() {
		// TODO Auto-generated method stub
		return new String[]{"type","pn","sn","fw"};
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
			if("CABLE".equals(type)){
				client = new MMprodmasterSAPClient();
				String mfrpn;
				String pn = sub.getPn(); 				
				mfrpn = pnmap.get(pn);
				//设置值的时候会覆盖掉原值
				if(null==mfrpn){
					try {
						mfrpn = client.downMMprodmastercalls(sub.getPn());
						pnmap.put(pn, mfrpn);
						sub.setPn(mfrpn);
						logger.info("pn:"+pn+",mfrpn:"+mfrpn);
					} catch (JCoException e) {
						logger.error(e.getCause().toString());
					}
				}else{
					sub.setPn(mfrpn);
				}
				
			}
		}
		
		client=null;
		
		if("CPU".equals(type)){
			int index=0;
			if(efoxCpu.size()>=subcomponent.size()){
				for(Component sub:subcomponent){
					sub.setSn(efoxCpu.get(index++).getSn());
				}
			}
		}
		
		if("BP".equals(type)){
			int index=0;
			if(efoxbp.size()>=subcomponent.size()){
				for(Component sub:subcomponent){
					sub.setSn(efoxbp.get(index++).getSn());
					sub.setPn(efoxbp.get(index++).getPn());
				}
			}
		}
		component.addAll(subcomponent);
	}
	
	public void addList(List<Component> component,Component sub,String type){
		if(sub.getType()==null||"".equals(sub.getType().trim())){
			sub.setType(type);
		}
		component.add(sub);
	}
	
}
