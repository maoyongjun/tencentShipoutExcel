package org.foxconn.tencent.shipoutExcel.dao;

import java.util.List;
import java.util.Map;

import org.foxconn.tencent.shipoutExcel.entity.OsMsgModel;
import org.springframework.dao.DataAccessException;

public interface OsMsgDao{
	public List<OsMsgModel> findAll(Map<String,Object> map)  throws DataAccessException;
}
