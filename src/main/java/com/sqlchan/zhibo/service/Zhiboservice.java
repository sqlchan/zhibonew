package com.sqlchan.zhibo.service;

import org.springframework.stereotype.Service;

@Service
public class Zhiboservice {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
