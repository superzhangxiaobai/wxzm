package com.wf.ew.light.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.exception.BusinessException;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.light.dao.LampMapper;
import com.wf.ew.light.model.Lamp;
import com.wf.ew.light.service.LampService;

@Service
public class LampServiceImpl extends ServiceImpl<LampMapper, Lamp> implements LampService {

	@Override
	public Lamp getByLampname(String lampname) {
		
		return baseMapper.selectByLampname(lampname);
	}

	@Override
	public PageResult<Lamp> listLamp(Integer page, Integer limit, String searchKey, String searchValue) {
		if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
		QueryWrapper<Lamp> wrapper = new QueryWrapper<>() ;
		
		if (StringUtil.isNotBlank(searchValue) && StringUtil.isNotBlank(searchKey)) {
			if (searchKey.equals("nickName")) {
				searchKey = "nick_name" ;
				wrapper.like(searchKey, searchValue);
			}else if (searchKey.equals("bucunzai")) {
				wrapper.eq("state", 0);
			}else {
				wrapper.eq(searchKey, searchValue);
			}
            //wrapper.eq(searchKey, searchValue);
        }
		
		IPage<Lamp> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
		return new PageResult<>(iPage.getRecords(), iPage.getTotal());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean add(Lamp lamp) {
		if (baseMapper.selectByLampname(lamp.getLampname()) != null) {
            throw new BusinessException("账号已经存在");
        }
        boolean rs = baseMapper.insert(lamp) > 0;
		return rs;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(Lamp lamp) {
		//lamp.setLampname(null);  // 账号不能修改
        boolean rs = baseMapper.updateById(lamp) > 0;
		return rs;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateBatch(List<Lamp> lamps) {
		boolean rs = updateBatchById(lamps);
		return rs;
	}

}
