package com.tdpark.service;

import com.tdpark.params.EmqParams;
import com.tdpark.vo.Result;

public interface EmqService {

	public Result make(EmqParams emqParams);
}
