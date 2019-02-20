package com.winning.service.dao;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    //该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断
    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name){
        return restTemplate.getForObject("http://EUREKA-CLIENT1/hi?name="+name,String.class);
    }

    public String hiError(String name){
        return "hi,"+name+",Sorry Error!";
    }
}
