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
import com.tsystems.javaschool.logiweb.model.status.UserRole;

public class MangerRestrictionFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(MangerRestrictionFilter.class);
    
    public void doFilter(ServletRequest request,  ServletResponse response, FilterChain chain)
              throws IOException, ServletException {
        
      HttpServletRequest requestHttp = (HttpServletRequest) request;
      HttpServletResponse responseHttp = (HttpServletResponse) response;
        
      if(AuthUtils.getUserRole(requestHttp) == UserRole.MANAGER) {
          chain.doFilter(request, response);
      } else {
          responseHttp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
          return;
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