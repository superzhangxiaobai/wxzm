package com.wf.ew.light.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.PageResult;
import com.wf.ew.light.model.Lamp;

public interface LampService extends IService<Lamp> {

	Lamp getByLampname (String lampname) ;
	
	PageResult<Lamp> listLamp(Integer page, Integer limit, String searchKey, String searchValue);
	
	boolean add (Lamp lamp) ;
	
	boolean update (Lamp lamp) ;
	
	boolean updateBatch (List<Lamp> lamps) ;
	
}
