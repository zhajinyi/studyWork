package org.bureaumanager.bureaumanagerpro.sysConfig.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.bureaumanager.bureaumanagerpro.base.BaseController;
import org.bureaumanager.bureaumanagerpro.sysConfig.pojo.dto.SysUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }

    /**
     *
     * @param userDto
     * @param session
     * @return
     */
    @RequestMapping(value = "/loginRequest",method = {RequestMethod.POST, RequestMethod.GET})
    public String loginRequest(SysUserDto userDto,HttpSession session){
        String Md5_Pwd = DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes());
        UsernamePasswordToken token = new UsernamePasswordToken(userDto.getId(), Md5_Pwd);
        Subject subject = SecurityUtils.getSubject();
        try {
            logger.info("==========开始验证==========");
            subject.login(token);//登陆成功，放到session中
            SysUserDto user = (SysUserDto) subject.getPrincipal();
            session.setAttribute("user", user);
            logger.info("==========验证通过==========");
            return "redirect:/user/logon";
        } catch (Exception e) {
            logger.info("==========验证失败==========");
            return "redirect:/user/login";
        }

    }

    @RequestMapping(value = "/logon")
    public String logon(HttpSession session){
        SysUserDto user =(SysUserDto)session.getAttribute("user");
        String redirectPath;
        if ("T".equals(user.getUserType())){
            redirectPath="redirect:/sys/menu/teacher_index";
        }else {
            redirectPath="models/student/student_index";
        }
        return redirectPath;
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        System.out.println(url);
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append(request.getServletContext().getContextPath()).append("/").toString();
        if(request.getSession(false)==null){
            logger.info("Session has been invalidated!");
        }else{
            logger.info("Session is active!");
            request.getSession().removeAttribute("user");
            request.getSession().invalidate();
        }
        return "redirect:" + tempContextUrl + "user/login";
    }

}
