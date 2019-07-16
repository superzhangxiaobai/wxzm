package com.wf.ew.light.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.ew.common.PageResult;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.light.dao.EnergyMapper;
import com.wf.ew.light.model.Energy;
import com.wf.ew.light.service.EnergyService;

@Service
public class EnergyServiceImpl extends ServiceImpl<EnergyMapper, Energy> implements EnergyService {

	@Override
	public PageResult<Energy> listEnergy(Integer page, Integer limit, String searchKey, String searchValue) {
		if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
		QueryWrapper<Energy> wrapper = new QueryWrapper<>();
		if (StringUtil.isNotBlank(searchValue) && StringUtil.isNotBlank(searchKey)) {
			System.out.println("=============="+searchValue);
            //wrapper.eq(searchKey, searchValue);
			
            if (searchValue.equals("curdate()")) {
				wrapper.apply("date(update_time) = curdate()", null);
			}
            if (searchValue.equals("month")) {
            	wrapper.apply("DATE_FORMAT( update_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )", null);
			}
            if (searchKey.equals("lamp_id")) {
            	wrapper.eq(searchKey, searchValue);
			}
            if (searchKey.equals("weekly")) {
            	wrapper.apply("date(update_time) = "+searchValue+"", null);
			}
        }
		//IPage<Energy> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
		List<Energy> data = baseMapper.selectList(wrapper);
		//return new PageResult<>(iPage.getRecords(), iPage.getTotal());
		return new PageResult<>(data, data.size());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean add(Energy energy) {
		boolean rs = baseMapper.insert(energy) > 0;
		return rs;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean update(Energy energy) {
		boolean rs = baseMapper.updateById(energy) > 0;
		return rs;
	}

	@Override
	public PageResult<Energy> list(Integer page, Integer limit, String startDate, String endDate, String lampname) {
		if (page == null || limit == null) {
            page = 1;
            limit = 10;
        }
		QueryWrapper<Energy> wrapper = new QueryWrapper<>();
		wrapper.orderByDesc("update_time");
		if (StringUtil.isBlank(startDate)) {
            startDate = null;
        } else {
            startDate += " 00:00:00";
            wrapper.gt("update_time", startDate);
        }
        if (StringUtil.isBlank(endDate)) {
            endDate = null;
        } else {
            endDate += " 23:59:59";
            wrapper.lt("update_time", endDate);
        }
        if (StringUtil.isBlank(lampname)) {
        	lampname = null;
        }else {
        	wrapper.like("lampname", lampname);
		}
		/*if (StringUtil.isNotBlank(searchValue) && StringUtil.isNotBlank(searchKey)) {
			System.out.println("=============="+searchValue);
            wrapper.eq(searchKey, searchValue);
			
        }*/
		IPage<Energy> iPage = baseMapper.selectPage(new Page<>(page, limit), wrapper);
		return new PageResult<>(iPage.getRecords(), iPage.getTotal());
	}

	@Override
	public PageResult<Energy> report(String startDate, String endDate, String lampname) {
		QueryWrapper<Energy> wrapper = new QueryWrapper<>();
		if (StringUtil.isBlank(startDate)) {
            startDate = null;
        } else {
            startDate += " 00:00:00";
            wrapper.gt("update_time", startDate);
        }
        if (StringUtil.isBlank(endDate)) {
            endDate = null;
        } else {
            endDate += " 23:59:59";
            wrapper.lt("update_time", endDate);
        }
        if (StringUtil.isBlank(lampname)) {
        	lampname = null;
        }else {
        	wrapper.like("lampname", lampname);
		}
		List<Energy> selectList = baseMapper.selectList(wrapper);
		return new PageResult<>(selectList, selectList.size());
	}

}
