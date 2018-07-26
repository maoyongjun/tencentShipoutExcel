package org.foxconn.tencent.shipoutExcel.entity;

import java.util.ArrayList;
import java.util.List;

public class SystemModel extends Component{
	private BoardModel board;
	private CpuModel cpu;
	private List<HddModel> hdd;
	private List<MemoryModel> memory;
	private List<NicModel> nic;
	private List<PsuModel> psu;
	List<Component> component= new ArrayList<Component>();
	
	public String getType() {
		return "SYSTEM";
	}
	
	public List<Component> getComponent() {
		component.add(board);
		component.add(cpu);
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
	public CpuModel getCpu() {
		return cpu;
	}
	public void setCpu(CpuModel cpu) {
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
