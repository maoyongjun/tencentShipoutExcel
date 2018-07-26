package org.foxconn.tencent.shipoutExcel.util;

import java.util.ArrayList;
import java.util.List;

import org.foxconn.tencent.shipoutExcel.entity.BaseStringArray;


/**
* @author:myz
* @version 1.0 
* 创建时间：2016年8月14日 下午2:15:42
*/
public class ToStringArrayUtil {
	public static List<String []> toStringArray(List<? extends BaseStringArray> list){
		List<String[]> returnList = new ArrayList<String[]>();
//		if(list.size()>0){
//			returnList.add(list.get(0).getHeader());
//		}
		for(BaseStringArray t:list){
			returnList.add(t.toStringArray());
		}
		return returnList;
	}
}
