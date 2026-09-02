package com.dhiraj.weatherforecast;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtility {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtility.class);

    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        if (StringUtils.isEmpty(ip)) {
            ip = request.getRemoteAddr();
        }

        logger.info("Client's IP Address: {}", ip);
        return ip;
    }
}
