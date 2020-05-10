package com.springbootsms.service;

import java.util.Map;

public interface ISendSms {
    public boolean send(String phoneNum, String templateCode, Map<String, Object> code);
}
