package org.foxconn.tencent.shipoutExcel.entity;

import java.util.ArrayList;
import java.util.List;

public class SystemModel extends Component{
	
	private String po;
	private String accecode;
	private String factoryname;
	private String machine;
	private String deviceType;
	private String ver;
	
	private Component board;
	private Component raid;
	private Component hba;
	
	private List<Component> cpu= new ArrayList<Component>();
	private List<Component> hdd= new ArrayList<Component>();
	private List<Component> memory= new ArrayList<Component>();
	private List<Component> nic= new ArrayList<Component>();
	private List<Component> psu= new ArrayList<Component>();
	private List<Component> bp= new ArrayList<Component>();
	private List<Component> cable= new ArrayList<Component>();
	private List<Component> cache= new ArrayList<Component>();
	private List<Component> fan= new ArrayList<Component>();
	private List<Component> frontio= new ArrayList<Component>();
	private List<Component> hbacard= new ArrayList<Component>();
	private List<Component> heatsink= new ArrayList<Component>();
	private List<Component> ocpcard= new ArrayList<Component>();
	private List<Component> raidcard= new ArrayList<Component>();
	private List<Component> rear2hddbp= new ArrayList<Component>();
	private List<Component> risercard= new ArrayList<Component>();
	private List<Component> sas93008i= new ArrayList<Component>();
	private List<Component> vrochwkey= new ArrayList<Component>();
	
	



	List<Component> component= new ArrayList<Component>();
	
	@Override
	public String[] toStringArray() {
		// TODO Auto-generated method stub
		return new String[]{po,accecode,getSn(),factoryname,machine,deviceType,ver};
	}




	public Component getBoard() {
		return board;
	}




	public void setBoard(Component board) {
		this.board = board;
	}




	public List<Component> getCpu() {
		return cpu;
	}




	public void setCpu(List<Component> cpu) {
		this.cpu = cpu;
	}




	public List<Component> getHdd() {
		return hdd;
	}




	public void setHdd(List<Component> hdd) {
		this.hdd = hdd;
	}




	public List<Component> getMemory() {
		return memory;
	}




	public void setMemory(List<Component> memory) {
		this.memory = memory;
	}




	public List<Component> getNic() {
		return nic;
	}




	public void setNic(List<Component> nic) {
		this.nic = nic;
	}




	public List<Component> getPsu() {
		return psu;
	}




	public void setPsu(List<Component> psu) {
		this.psu = psu;
	}




	public List<Component> getComponent() {
		addList(component,board,"BOARD");
		addList(component,cpu,"CPU");
		addList(component,hdd,"HDD");
		addList(component,memory,"MEMORY");
		addList(component,nic,"NIC");
		addList(component,psu,"PSU");
		addList(component,raid,"RAID");
		addList(component,hba,"HBA");
		addList(component,bp,"BP");
		addList(component,cable,"CABLE");
		addList(component,cache,"CACHE");
		addList(component,fan,"FAN");
		addList(component,frontio,"FRONTIO");
		addList(component,hbacard,"HBACARD");
		addList(component,heatsink,"HEATSINK");
		addList(component,ocpcard,"OCPCARD");
		addList(component,raidcard,"RAIDCARD");
		addList(component,rear2hddbp,"REAR2HDDBP");
		addList(component,risercard,"RISERCARD");
		addList(component,sas93008i,"SAS93008I");
		addList(component,vrochwkey,"VROCHWKEY");
		return component;
	}




	public void setComponent(List<Component> component) {
		this.component = component;
	}










	public List<Component> getBp() {
		return bp;
	}




	public void setBp(List<Component> bp) {
		this.bp = bp;
	}




	public String getPo() {
		return po;
	}




	public void setPo(String po) {
		this.po = po;
	}




	public String getAccecode() {
		return accecode;
	}




	public void setAccecode(String accecode) {
		this.accecode = accecode;
	}




	public String getFactoryname() {
		return factoryname;
	}




	public void setFactoryname(String factoryname) {
		this.factoryname = factoryname;
	}




	public String getMachine() {
		return machine;
	}




	public void setMachine(String machine) {
		this.machine = machine;
	}




	public String getDeviceType() {
		return deviceType;
	}




	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}




	public String getVer() {
		return ver;
	}




	public void setVer(String ver) {
		this.ver = ver;
	}




	public List<Component> getCable() {
		return cable;
	}




	public void setCable(List<Component> cable) {
		this.cable = cable;
	}




	public List<Component> getCache() {
		return cache;
	}




	public void setCache(List<Component> cache) {
		this.cache = cache;
	}




	public List<Component> getFan() {
		return fan;
	}




	public void setFan(List<Component> fan) {
		this.fan = fan;
	}




	public List<Component> getFrontio() {
		return frontio;
	}




	public void setFrontio(List<Component> frontio) {
		this.frontio = frontio;
	}




	public List<Component> getHbacard() {
		return hbacard;
	}




	public void setHbacard(List<Component> hbacard) {
		this.hbacard = hbacard;
	}




	public List<Component> getHeatsink() {
		return heatsink;
	}




	public void setHeatsink(List<Component> heatsink) {
		this.heatsink = heatsink;
	}




	public List<Component> getOcpcard() {
		return ocpcard;
	}




	public void setOcpcard(List<Component> ocpcard) {
		this.ocpcard = ocpcard;
	}




	public List<Component> getRaidcard() {
		return raidcard;
	}




	public void setRaidcard(List<Component> raidcard) {
		this.raidcard = raidcard;
	}




	public List<Component> getRear2hddbp() {
		return rear2hddbp;
	}




	public void setRear2hddbp(List<Component> rear2hddbp) {
		this.rear2hddbp = rear2hddbp;
	}




	public List<Component> getRisercard() {
		return risercard;
	}




	public void setRisercard(List<Component> risercard) {
		this.risercard = risercard;
	}




	public List<Component> getSas93008i() {
		return sas93008i;
	}




	public void setSas93008i(List<Component> sas93008i) {
		this.sas93008i = sas93008i;
	}




	public List<Component> getVrochwkey() {
		return vrochwkey;
	}




	public void setVrochwkey(List<Component> vrochwkey) {
		this.vrochwkey = vrochwkey;
	}




	public Component getRaid() {
		return raid;
	}




	public void setRaid(Component raid) {
		this.raid = raid;
	}




	public Component getHba() {
		return hba;
	}




	public void setHba(Component hba) {
		this.hba = hba;
	}



	
	
}