package com.sqlchan.zhibo.controller;

import com.sqlchan.zhibo.aspect.LogAspect;
import com.sqlchan.zhibo.service.Zhiboservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

@Controller
public class IndexController {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    Zhiboservice zhiboservice;

    //请求访问
    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(HttpSession httpSession) {
        logger.info("index");
        return  zhiboservice.getMessage(1)+"Hello NowCoder" + httpSession.getAttribute("msg");
    }
    //参数解析
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", required = false) String key) {
        return String.format("Profile Page of %s / %d, t:%d k: %s", groupId, userId, type, key);
    }
    //freemark模板使用
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name", "[Angel -- 1111守护天使]");
        return "hello";
    }

    //第一次访问时，没有JSESSIONID
    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                          HttpServletRequest request,
                          HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE:" + sessionId);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");
        response.addHeader("nowcoderId", "hello");
        response.addCookie(new Cookie("username", "nowcoder"));
        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw  new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }
}
