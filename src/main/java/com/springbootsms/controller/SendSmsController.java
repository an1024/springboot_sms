package com.springbootsms.controller;

import com.springbootsms.service.ISendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
public class SendSmsController {

    @Autowired
    private ISendSms sendSms;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phone}")
    public String code(@PathVariable("phone") String phone) {
        //调用发送方法
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return phone + ":" + code + "已存在,没有过期";
        }
        //生成验证码并存储在redis
        code = UUID.randomUUID().toString().substring(0, 6);
        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);

        boolean isSend = sendSms.send(phone, "你的签名名称", param);
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code,5, TimeUnit.SECONDS);
            return phone + ":" + code + "发送成功";
        }else{
            return phone + ":" + code + "发送失败";
        }
    }
}
