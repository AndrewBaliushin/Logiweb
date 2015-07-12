package com.tsystems.javaschool.logiweb.controllers.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.controllers.ext.AuthUtils;

public class LoginFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(LoginFilter.class);

    private static final String LOGIN_PAGE = "/login";
    
    private static final String RESOURCES_REGEXP = ".*\\/[css|js].*";
    
    public void doFilter(ServletRequest request,  ServletResponse response, FilterChain chain)
              throws IOException, ServletException {
        
      HttpServletRequest requestHttp = (HttpServletRequest) request;
      HttpServletResponse responseHttp = (HttpServletResponse) response;
        
      String path = requestHttp.getRequestURI();
      boolean loggedIn = AuthUtils.isLoggedIn(((HttpServletRequest) request));
      String pathLoginPage = requestHttp.getContextPath() + LOGIN_PAGE;
      
        if (loggedIn || path.matches(RESOURCES_REGEXP)
                || path.equals(pathLoginPage)) {
            chain.doFilter(request, response);
        } else {
            try {
                responseHttp.sendRedirect(requestHttp.getContextPath()
                        + LOGIN_PAGE);
            } catch (IOException e) {
                LOG.warn("IO exception", e);
            }
        }
    }

    @Override
    public void destroy() {
        /* not required */
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        /* not required */
    }
  }