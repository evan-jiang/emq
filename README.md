### 基于MySql，支持定时延时执行消息，具有线程安全特性！

# 定时消息
可以指定任意时间之后执行

# 线程安全队列
相同的消息顺序被消费保证线程安全

# 支持Http和Hessian协议

### Http调用方式
- 地址：`http://线下从开发者处获取/emq/make`
- 请求参数：

|字段|类型|必需|描述|
| --- | --- | --- | --- |
|title|String|Y|每条消息都必须有一个标题(不参与逻辑)|
|url|String|Y|消息被消费的地址|
|params|String|N|消息被消费时需要携带的参数(参数自行解析)|
|plan_times|int|N|默认为1，消息在执行失败的情况下最多允许重复执行的次数|
|interval|long|N|plan_times大于1时必需传且必需大于5000(毫秒)，消息在执行失败的情况下重复执行的时间间隔|
|match_value|String|Y|消息被执行时返回值包含match_value则表示执行成功|
|delay|long|N|默认为0(毫秒)，指定执行消息的时间|
|block|boolean|N|默认为false，是否要保证线程安全|
|group|String|N|block为true时，通过group保证线程安全，group为空时通过params保证线程安全，params为空时通过url保证线程安全|

- 响应参数：

|字段|类型|必需|描述|
| --- | --- | --- | --- |
|code|String|Y|返回码|
|msg|String|Y|异常信息|

- 响应实例：

```json
{
	"code":"SUCCESS",
	"msg":null
}

OR

{
	"code":"FAILED",
	"msg":"[url] Illegal format!"
}
```
### Hessian调用方式
- 配置项：

```
<!-- pom.xml -->
<dependency>
	<groupId>cn.putao</groupId>
	<artifactId>pt-msg-service</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- spring-hessian -->
<bean id="emqService" class="org.springframework.remoting.caucho.HessianProxyFactoryBean">
     <property name="serviceUrl" value="${emq.service.url}" />
     <property name="serviceInterface" value="com.tdpark.service.EmqService" />
</bean>
```
- URL(emq.service.url)：`http://线下从开发者处获取/emq_service`
- 接口：`Result EmqService.make(EmqParams emqParams)`

- EmqParams结构：

|字段|类型|必需|描述|
| --- | --- | --- | --- |
|title|String|Y|每条消息都必须有一个标题(不参与逻辑)|
|url|String|Y|消息被消费的地址|
|params|String|N|消息被消费时需要携带的参数(参数自行解析)|
|plan_times|int|N|默认为1，消息在执行失败的情况下最多允许重复执行的次数|
|interval|long|N|plan_times大于1时必需传且必需大于5000(毫秒)，消息在执行失败的情况下重复执行的时间间隔|
|match_value|String|Y|消息被执行时返回值包含match_value则表示执行成功|
|delay|long|N|默认为0(毫秒)，指定执行消息的时间|
|block|boolean|N|默认为false，是否要保证线程安全|
|group|String|N|block为true时，通过group保证线程安全，group为空时通过params保证线程安全，params为空时通过url保证线程安全|

- Result结构：

|字段|类型|必需|描述|
| --- | --- | --- | --- |
|code|String|Y|返回码|
|msg|String|Y|异常信息|

- 响应实例：

```json
{
	"code":"SUCCESS",
	"msg":null
}

OR

{
	"code":"FAILED",
	"msg":"[url] Illegal format!"
}
```
