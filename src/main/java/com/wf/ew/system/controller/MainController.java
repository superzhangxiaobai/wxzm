package com.wf.ew.system.controller;

//import com.wf.captcha.utils.CaptchaUtil;
import com.wf.ew.common.BaseController;
import com.wf.ew.common.JsonResult;
import com.wf.ew.common.shiro.UserRealm;
import com.wf.ew.common.utils.DateUtil;
import com.wf.ew.common.utils.StringUtil;
import com.wf.ew.common.utils.UserAgentGetter;
import com.wf.ew.light.utils.captcha.Captcha;
import com.wf.ew.light.utils.captcha.GifCaptcha;
import com.wf.ew.light.utils.captcha.utils.CaptchaUtil;
import com.wf.ew.system.model.Authorities;
import com.wf.ew.system.model.LoginRecord;
import com.wf.ew.system.model.User;
import com.wf.ew.system.service.AuthoritiesService;
import com.wf.ew.system.service.LoginRecordService;
import com.wf.ew.system.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * MainController
 */
@Controller
public class MainController extends BaseController implements ErrorController {
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private UserService userService;

    // 主页
    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        if (getLoginUser() == null) {
            return "redirect:login";
        }
        List<Authorities> authorities = authoritiesService.listByUserId(getLoginUserId());
        List<Map<String, Object>> menuTree = getMenuTree(authorities, -1);
        model.addAttribute("menus", menuTree);
        //model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("loginUser", userService.getByUsername(getLoginUserName()));
        return "index.html";
    }

    // 登录页
    @GetMapping("/login")
    public String login() {
        if (getLoginUser() != null) {
            return "redirect:index";
        }
        return "login.html";
    }

    // 登录
    @ResponseBody
    @PostMapping("/login")
    public JsonResult doLogin(String username, String password, String code, HttpServletRequest request) {
        if (StringUtil.isBlank(username, password)) {
            return JsonResult.error("账号密码不能为空");
        }
        if (!CaptchaUtil.ver(code, request)) {
            // CaptchaUtil.clear(request);
            return JsonResult.error("验证码不正确");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);
            addLoginRecord(getLoginUserId(), request);
            return JsonResult.ok("登录成功");
        } catch (IncorrectCredentialsException ice) {
            return JsonResult.error("密码错误");
        } catch (UnknownAccountException uae) {
            return JsonResult.error("账号不存在");
        } catch (LockedAccountException e) {
            return JsonResult.error("账号被锁定");
        } catch (ExcessiveAttemptsException eae) {
            return JsonResult.error("操作频繁，请稍后再试");
        }
    }

    // 图形验证码，用assets开头可以排除shiro拦截
    @RequestMapping("/assets/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            CaptchaUtil.out(4, request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @ResponseBody
    @RequestMapping("/assets/mycaptcha")
    public String mycaptcha(HttpServletRequest request, HttpServletResponse response) {
        try {
        	Captcha captcha = new GifCaptcha(130, 48, 4);
        	String SESSION_KEY = "captcha";
        	request.getSession().setAttribute(SESSION_KEY, captcha.text().toLowerCase());
        	return captcha.text();
            //CaptchaUtil.out(4, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // 控制台
    @RequestMapping("/console")
    public String console() {
        return "system/console.html";
    }
    
    // 欢迎页
    @RequestMapping("/welcome")
    public String welcome(Model model) {
    	InetAddress address;
		try {
			//获取的是本地的IP地址 
			address = InetAddress.getLocalHost();
			String hostAddress = address.getHostAddress();//192.168.0.121 
			String hostName = address.getHostName();
			Properties props=System.getProperties(); //系统属性
			
			model.addAttribute("serverIp", hostAddress);
			model.addAttribute("serverDomain", hostName);
			model.addAttribute("serverTime", DateUtil.getCurrentDate());
			model.addAttribute("javaVersion", props.getProperty("java.version"));
			model.addAttribute("serverOsName", props.getProperty("os.name"));
			model.addAttribute("serverVmName", props.getProperty("java.vm.name"));
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        return "system/welcome.html";
    }
    
    // 个人信息
    @RequestMapping("/personalInfo")
    public String personalInfo(Model model) {
    	if (getLoginUser() == null) {
            return "redirect:login";
        }
    	//清除ehcache中所有用户权限缓存
    	RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();
    	UserRealm authRealm = (UserRealm)rsm.getRealms().iterator().next();
    	authRealm.updateShiroUser(getLoginUserName());
    	User byUsername = userService.getByUsername(getLoginUserName());
        
        model.addAttribute("loginUser", byUsername);
        return "system/personalInfo.html";
    }

    // 消息弹窗页
    @RequestMapping("/tpl/message")
    public String message() {
        return "system/tpl-message.html";
    }

    // 修改密码弹窗页
    @RequestMapping("/tpl/password")
    public String password() {
        return "system/tpl-password.html";
    }

    // 主题设置弹窗页
    @RequestMapping("/tpl/theme")
    public String theme() {
        return "system/tpl-theme.html";
    }

    // 便签设置弹窗页
    @RequestMapping("/tpl/note")
    public String note() {
        return "system/tpl-note.html";
    }

    // 错误页
    @RequestMapping("/error")
    public String error(String code) {
        if ("403".equals(code)) {
            return "error/403.html";
        }
        return "error/404.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    // 递归转化树形菜单
    private List<Map<String, Object>> getMenuTree(List<Authorities> authorities, Integer parentId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < authorities.size(); i++) {
            Authorities temp = authorities.get(i);
            if (temp.getIsMenu() == 0 && parentId == temp.getParentId()) {
                Map<String, Object> map = new HashMap<>();
                map.put("menuName", temp.getAuthorityName());
                map.put("menuIcon", temp.getMenuIcon());
                map.put("menuUrl", StringUtil.isBlank(temp.getMenuUrl()) ? "javascript:;" : (temp.getMenuUrl()));
                map.put("subMenus", getMenuTree(authorities, authorities.get(i).getAuthorityId()));
                list.add(map);
            }
        }
        return list;
    }

    // 添加登录日志
    private void addLoginRecord(Integer userId, HttpServletRequest request) {
        UserAgentGetter agentGetter = new UserAgentGetter(request);
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setUserId(userId);
        loginRecord.setOsName(agentGetter.getOS());
        loginRecord.setDevice(agentGetter.getDevice());
        loginRecord.setBrowserType(agentGetter.getBrowser());
        loginRecord.setIpAddress(agentGetter.getIpAddr());
        loginRecordService.save(loginRecord);
    }
}
