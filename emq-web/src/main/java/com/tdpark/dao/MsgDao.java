package com.tdpark.dao;
import org.apache.ibatis.annotations.Param;

import com.tdpark.common.domain.Entity;
public interface MsgDao {
   
    public Long insert(Entity msgEntity);
    public Entity pop(@Param("thread_no")int threadNo);
    public Long again(Entity msgEntity);
    public Long clean(Entity msgEntity);
    
}