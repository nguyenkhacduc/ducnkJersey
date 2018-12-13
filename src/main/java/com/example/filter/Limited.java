package com.example.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class Limited implements Filter {
    private static Logger logger;
    private static Marker marker;
    private static final long DISTANCE_TIME = 7777;
    private static HashMap<String, Long> infoIP = new HashMap<String, Long>();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
        marker = MarkerFactory.getMarker("");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest){
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String ipAddress = request.getRemoteAddr();

            Long currentTime = System.currentTimeMillis();
            if (infoIP.containsKey(ipAddress)){

                Long lastTime = infoIP.get(ipAddress);
                if (currentTime - lastTime < DISTANCE_TIME){

                    Long waitingTime = DISTANCE_TIME - currentTime + lastTime;
                    logger.info(marker, String.format("IP address %s wait %d miliseconds", ipAddress, waitingTime));
                    RequestDispatcher requestDispatcher = servletRequest.getRequestDispatcher("index.html");
                    requestDispatcher.include(servletRequest, servletResponse);
                    HttpServletResponse response = (HttpServletResponse) servletResponse;
                    response.sendError(400, String.format("You are blocked. %d miliseconds until unblocking.", waitingTime));
                    return;
                }
            }

            infoIP.put(ipAddress, currentTime);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
