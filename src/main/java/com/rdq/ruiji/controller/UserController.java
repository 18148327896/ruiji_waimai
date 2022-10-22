package com.rdq.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rdq.ruiji.common.Mail;
import com.rdq.ruiji.common.R;
import com.rdq.ruiji.common.ValidateCodeUtils;
import com.rdq.ruiji.entity.User;
import com.rdq.ruiji.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.xml.bind.util.ValidationEventCollector;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        //用户信息是
        log.info("用户信息是{}",user);
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            //生成四位数验证吗
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码是{}",code);
            Mail.sendMail("3243480675@qq.com","瑞吉外卖，您的验证码是"+code+"请勿将验证码透露给他人哦","瑞吉外卖验证码");
            session.setAttribute("codeSession",code);
            return R.success("验证码发送成功");
        }

        return R.error("验证码发送失败");
    }

    /*
    * 移动端手机登录
    * */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        //从session中获取保存的验证码,进行比对,比对完成后进行登录
        Object codeInSession = session.getAttribute("codeSession");
        //判断当前手机号是否在用户表中，如果是新用户则自动完成注册
        if (codeInSession != null&&codeInSession.equals(code)) {
            //比对成功，登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User ::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                //如果为新用户则添加新用户
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("employee",user.getId());
            return R.success(user);
        }
        return R.error("验证码不正确或信息不存在哦亲");
    }
}
