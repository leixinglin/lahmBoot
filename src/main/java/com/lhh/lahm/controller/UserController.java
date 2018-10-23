package com.lhh.lahm.controller;

import com.lhh.lahm.dao.UsersMapper;
import com.lhh.lahm.entity.Users;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UsersMapper usersMapper;

    @RequestMapping("/loginPage")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestParam String userName,@RequestParam String passWord){
        Map<String,String> resultMap=new HashMap<>();
        resultMap.put("code","0");
        Users users=usersMapper.loginFind(userName,passWord);
        if(users!=null){
            Subject currUser= SecurityUtils.getSubject();
            UsernamePasswordToken token=new UsernamePasswordToken(userName, passWord);
            token.setRememberMe(true);
            currUser.login(token);
            resultMap.put("code","1");
            resultMap.put("message","ok!");
        }else{
            resultMap.put("message","账号或密码错误!");
        }

        return resultMap;
    }

    @RequestMapping("/unauth")
    public String unauth(){
        return "unauth";
    }

    @RequestMapping("/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "login";
    }

}
