package com.rdq.ruiji.filter;

import com.alibaba.fastjson.JSON;
import com.rdq.ruiji.common.BaseContext;
import com.rdq.ruiji.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * 检查用户是否完成登录
 * */
//检查登录拦截所有路径
@WebFilter(filterName = "检查登录", urlPatterns = "/*")
@Slf4j
@Component
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("过滤器初始化");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URL
        String requestURI = request.getRequestURI();
        //打印日志拦截到的地址
        log.info("本次的请求地址是{}", requestURI);
        /*判断请求是否需要处理，不需要则放行*/
        //不需要处理的请求放行
        String[] urls= new String[] {
          "/employee/login",
                /*不拦截页面，只拦截controller的请求*/
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/common/**",
                "/user/login"

        };
        //判断请求是否需要处理
        boolean b = checkLogin(requestURI,urls);
        //如果不需要处理则直接放行
        if (b) {
            //直接放行
            log.info("此页面{}符合逻辑不需要拦截，直接放行",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //如果是登陆了状态则直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("此页面已经登录不需要拦截，id是{}",request.getSession().getAttribute("employee"));
            Long id = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        //如果未登录返回登录状态通过输出流的方式向客户端页面直接响应输出数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;

    }
    /*路径匹配检查本次请求是否需要放行*/
    public boolean checkLogin(String requestURI,String[] urls){
        for (String url : urls) {
            //match匹配路径
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
