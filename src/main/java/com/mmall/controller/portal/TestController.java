package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.IMailSend;
import com.mmall.service.impl.PwdMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/")
public class TestController {
    @Autowired
    private PwdMailSender pwdMailSender;
    @RequestMapping("sendMail.do")
    @ResponseBody
    ServerResponse mailTest(){
        pwdMailSender.send("lee", "在这里填写你所需要的内容", "ccppjj1981115@163.com");
        return ServerResponse.createBySuccess();
    }
}
