package com.winning.server.dao;

import com.winning.server.dao.impl.SchedualServiceHiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//Feign自带熔断器，默认关闭，需要在配置文件中打开feign.hystrix.enabled=true
//在注解@FeignClient中增加fallback的制定类
//制定类需要实现本接口，并且注入到IOC容器中，@Component
@FeignClient(value = "eureka-client1",fallback = SchedualServiceHiHystrix.class)
public interface SchedualServiceHi {
    @RequestMapping(value="/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam(value = "name") String name);

}
