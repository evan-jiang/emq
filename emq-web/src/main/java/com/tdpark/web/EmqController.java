package com.tdpark.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tdpark.common.cache.WhiteCache;
import com.tdpark.common.config.Hold;
import com.tdpark.params.EmqParams;
import com.tdpark.run.lock.LockContainer;
import com.tdpark.service.EmqService;
import com.tdpark.vo.Result;

@Controller
@RequestMapping("/emq")
public class EmqController {
	
	@Autowired
	private EmqService emqService;
	@Autowired
	private WhiteCache whiteCache;
	
	@RequestMapping("make")
	@ResponseBody
	public Object make(EmqParams emqParams,HttpServletRequest request){
		return emqService.make(emqParams);
	}
	
	@RequestMapping("white/reload")
	@ResponseBody
	public Object reload(HttpServletRequest request){
		whiteCache.reload();
		return new Result();
	}
	@RequestMapping("wait")
	@ResponseBody
	public Object wait(HttpServletRequest request){
		Hold.WAITING.set(true);
		LockContainer.notifyALL();
		return new Result();
	}
	@RequestMapping("resume")
	@ResponseBody
	public Object resume(HttpServletRequest request){
		Hold.WAITING.set(false);
		LockContainer.notifyALL();
		return new Result();
	}
}
