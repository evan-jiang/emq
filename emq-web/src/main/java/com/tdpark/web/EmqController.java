package com.tdpark.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tdpark.common.annotaction.Description;
import com.tdpark.common.cache.StatusCache;
import com.tdpark.common.cache.WhiteCache;
import com.tdpark.common.channel.BrotherChannel;
import com.tdpark.common.config.Config;
import com.tdpark.params.EmqParams;
import com.tdpark.run.init.EmqInit;
import com.tdpark.service.EmqService;
import com.tdpark.vo.Result;

@Controller
@RequestMapping("emq")
public class EmqController {

    @Autowired
    private Config config;
    @Autowired
    private EmqService emqService;
    @Autowired
    private WhiteCache whiteCache;
    @Autowired
    private BrotherChannel brotherChannel;
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EmqController.class);

    @RequestMapping("make")
    @ResponseBody
    @Description("创建消息")
    public Object make(EmqParams emqParams, HttpServletRequest request) {
        return emqService.make(emqParams);
    }

    @RequestMapping("full_pause")
    @ResponseBody
    @Description("所有节点停止消费消息")
    public Object fullPause(HttpServletRequest request) {
        LOGGER.info(">>>>>>>>>>>>full node pause<<<<<<<<<<<<");
        brotherChannel.fullPause();
        return new Result();
    }

    /**
     * 当前节点停止消费消息
     * 
     * @param request
     * @return
     */
    @RequestMapping("pause")
    @ResponseBody
    public Object pause(HttpServletRequest request) {
        LOGGER.info(">>>>>>>>>>>>node pause<<<<<<<<<<<<");
        StatusCache.slefPauseOrResume(true);
        return new Result();
    }

    @RequestMapping("full_resume")
    @ResponseBody
    @Description("full_pause的逆操作，所有节点开始消费消息")
    public Object fullResume(HttpServletRequest request) {
        LOGGER.info(">>>>>>>>>>>>full node resume<<<<<<<<<<<<");
        brotherChannel.fullResume();
        return new Result();
    }

    /**
     * pause的逆操作，当前节点开始消费消息
     * 
     * @param request
     * @return
     */
    @RequestMapping("resume")
    @ResponseBody
    public Object resume(HttpServletRequest request) {
        LOGGER.info(">>>>>>>>>>>>node resume<<<<<<<<<<<<");
        StatusCache.slefPauseOrResume(false);
        return new Result();
    }

    /**
     * 通知线程有新消息可以消费
     * 
     * @param request
     * @return
     */
    @RequestMapping("notify")
    @ResponseBody
    public Object notify(@RequestParam(BrotherChannel.THREAD_KEY) int threadNo,
            @RequestParam(BrotherChannel.EXECUTE_TIME_KEY) long newExecuteTime,
            HttpServletRequest request) {
        brotherChannel.notifyThread(threadNo, newExecuteTime);
        return new Result();
    }
    @RequestMapping("apply_join")
    @ResponseBody
    public Object applyJoin(@RequestParam(EmqInit.NODE_IDX_KEY) int nodeIdx,
            @RequestParam(EmqInit.NODE_HOST_KEY) String nodeHost,
            @RequestParam(EmqInit.NODE_TOTAL_KEY) int nodeTotal,
            @RequestParam(EmqInit.THREAD_NUM_KEY) int threadNum,
            HttpServletRequest request) {
        Result result = brotherChannel.applyJoin(nodeIdx, nodeHost, nodeTotal, threadNum);
        LOGGER.info(">>>>>>>>>>>>node["+nodeIdx+"] apply join["+result.getCode()+"]<<<<<<<<<<<<");
        return result;
    }
    @RequestMapping("apply_quit")
    @ResponseBody
    public Object applyQuit(@RequestParam(EmqInit.NODE_IDX_KEY) int nodeIdx,
            HttpServletRequest request) {
        LOGGER.info(">>>>>>>>>>>>node["+nodeIdx+"] apply quit<<<<<<<<<<<<");
        return brotherChannel.applyQuit(nodeIdx);
    }
    @RequestMapping("config")
    @ResponseBody
    @Description("获取集群配置信息")
    public Object config(HttpServletRequest request){
        Map<Integer, String> nodes = config.getNodes();
        ConfigResult result = new ConfigResult();
        List<Object> data = new ArrayList<Object>();
        for(Entry<Integer, String> e : nodes.entrySet()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("nodeIdx", e.getKey());
            map.put("nodeStatus", StatusCache.connectionStatus(e.getKey()));
            map.put("nodeHost", e.getValue());
            data.add(map);
        }
        result.setData(data);
        return result;
    }
    
    @SuppressWarnings("serial")
    public static class ConfigResult extends Result{
        public List<Object> data;

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }
    }
    @RequestMapping("white/list")
    @ResponseBody
    @Description("获取白名单列表")
    public Object whiteList(HttpServletRequest request){
        WhiteResult result = new WhiteResult();
        result.setData(whiteCache.whiteList());
        return result;
    }
    @SuppressWarnings("serial")
    public static class WhiteResult extends Result{
        public Set<String> data;
        public Set<String> getData() {
            return data;
        }
        public void setData(Set<String> data) {
            this.data = data;
        }
    }
    @RequestMapping("white/init")
    @ResponseBody
    public Object initWhite(HttpServletRequest request){
        whiteCache.init();
        return new Result();
    }
    @RequestMapping("white/add")
    @ResponseBody
    @Description("添加白名单")
    public Object addWhite(@RequestParam(value="host",defaultValue="")String host,HttpServletRequest request){
        return whiteCache.addHost(host);
    }
    @RequestMapping("white/remove")
    @ResponseBody
    @Description("移除白名单")
    public Object removeWhite(@RequestParam(value="host",defaultValue="")String host,HttpServletRequest request){
        return whiteCache.removeHost(host);
    }
    @RequestMapping("help")
    @ResponseBody
    @Description("帮助")
    public Object help(HttpServletRequest request){
        Class<?> clazz = EmqController.class;
        String fristName = "";
        RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
        if(mapping != null){
            fristName = mapping.value()[0];
        }
        String requestUrl = request.getRequestURL().toString();
        int hidx = requestUrl.indexOf("help",requestUrl.indexOf(fristName));
        String baseUrl = requestUrl.substring(0,hidx);
        
        ConfigResult result = new ConfigResult();
        List<Object> data = new ArrayList<Object>();
        
        
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            Description description = m.getAnnotation(Description.class);
            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
            if(description == null || requestMapping == null){
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            String url = baseUrl + requestMapping.value()[0];
            map.put("url", url);
            map.put("description", description.value());
            data.add(map);
        }
        result.setData(data);
        return result;
    }
    public static void main(String[] args) {
        String requestUrl = "http://127.0.0.1:2080/emq//help";
        String requestURI = "/emq//help";
        String base = requestUrl.substring(0,requestUrl.length() - requestURI.length());
        String projectName = requestURI.replace("//", "/").replace("emq/help", "").replace("//", "/");
        System.out.println(requestUrl);
        System.out.println(requestURI);
        System.out.println(base);
        System.out.println(projectName);
    }
}
