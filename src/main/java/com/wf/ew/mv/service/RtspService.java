package com.wf.ew.mv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wf.ew.common.PageResult;
import com.wf.ew.mv.model.Rtsp;


/**
 * 摄像头视频表 服务类
 *
 * @author null
 * @since 2019-03-21
 */
public interface RtspService extends IService<Rtsp> {

	PageResult<Rtsp> listRtsp (Integer page ,Integer limit ,String searchKey , String searchValue);
	
	boolean add(Rtsp rtsp);
	
	boolean update(Rtsp rtsp);
	
}
