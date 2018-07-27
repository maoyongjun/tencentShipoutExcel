package org.foxconn.tencent.shipoutExcel.entity;

import java.util.ArrayList;
import java.util.List;

public class SystemModel extends Component{
	private BoardModel board;
	private List<CpuModel> cpu;
	private List<HddModel> hdd;
	private List<MemoryModel> memory;
	private List<NicModel> nic;
	private List<PsuModel> psu;
	List<Component> component= new ArrayList<Component>();
	private RaidModel raid;
	private HbaModel hba;
	private List<BpModel> bp= new ArrayList<BpModel>();
	private String po;
	private String accecode;
	private String factoryname;
	private String machine;
	private String deviceType;
	private String ver;
	
	
	
	
	@Override
	public String[] toStringArray() {
		// TODO Auto-generated method stub
		return new String[]{po,accecode,getSn(),factoryname,machine,deviceType,ver};
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

	public RaidModel getRaid() {
		return raid;
	}

	public void setRaid(RaidModel raid) {
		this.raid = raid;
	}

	public HbaModel getHba() {
		return hba;
	}

	public void setHba(HbaModel hba) {
		this.hba = hba;
	}

	public List<BpModel> getBp() {
		return bp;
	}

	public void setBp(List<BpModel> bp) {
		this.bp = bp;
	}

	public String getType() {
		return "SYSTEM";
	}
	
	public List<Component> getComponent() {
		component.add(board);
		
		if(null!=cpu) {
			component.addAll(cpu);
		}
		if(null!=hdd) {
			component.addAll(hdd);
		}
		if(null!=memory) {
			component.addAll(memory);
		}
		if(null!=nic) {
			component.addAll(nic);
		}
		if(null!=psu) {
			component.addAll(psu);
		}
		return component;
	}
	public void setComponent(List<Component> component) {
		this.component = component;
	}
	public BoardModel getBoard() {
		return board;
	}
	public void setBoard(BoardModel board) {
		this.board = board;
	}
	
	public List<CpuModel> getCpu() {
		return cpu;
	}

	public void setCpu(List<CpuModel> cpu) {
		this.cpu = cpu;
	}

	public List<HddModel> getHdd() {
		return hdd;
	}
	public void setHdd(List<HddModel> hdd) {
		this.hdd = hdd;
	}
	public List<MemoryModel> getMemory() {
		return memory;
	}
	public void setMemory(List<MemoryModel> memory) {
		this.memory = memory;
	}
	public List<NicModel> getNic() {
		return nic;
	}
	public void setNic(List<NicModel> nic) {
		this.nic = nic;
	}
	public List<PsuModel> getPsu() {
		return psu;
	}
	public void setPsu(List<PsuModel> psu) {
		this.psu = psu;
	}
	
}
