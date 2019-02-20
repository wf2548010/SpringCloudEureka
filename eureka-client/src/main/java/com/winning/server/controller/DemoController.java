package com.winning.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping(value = "/urlparameterdemo")
    public String  urlParameterDemo(@RequestParam(value = "name")String name,
                                    @RequestParam(value = "password")String password){
        return "name:"+name+"-password:"+password;
    }

}
