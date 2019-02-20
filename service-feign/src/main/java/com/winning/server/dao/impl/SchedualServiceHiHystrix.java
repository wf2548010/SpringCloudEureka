package com.winning.server.dao.impl;

import com.winning.server.dao.SchedualServiceHi;
import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystrix implements SchedualServiceHi {
    @Override
    public String sayHi(String name) {
        return "Sorry! "+name;
    }
}
