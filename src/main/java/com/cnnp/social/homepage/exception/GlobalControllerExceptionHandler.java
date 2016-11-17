/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.homepage.exception;

import com.cnnp.social.base.ExceptionMessage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * apiPortal
 * Created by Damon on 2016/11/3.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(NoAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public @ResponseBody ExceptionMessage handleError(
            HttpServletRequest req,
            NoAuthenticationException ex) {
        return getExceptionMessage(req, ex,HttpStatus.UNAUTHORIZED);
    }

    private ExceptionMessage getExceptionMessage(HttpServletRequest req, Exception ex,HttpStatus status) {
        ExceptionMessage msg = new ExceptionMessage();
        msg.setMessage(ex.getMessage());
        //get request url contain request params
        msg.setRequesturl(StringUtils.isBlank(req.getQueryString()) ? req.getRequestURI() :
                req.getRequestURI() + "?" + req.getQueryString());
        msg.setStatus(status.value());
        msg.setTimestamp(new Date().getTime());
        //get request body
        try {
            if (req.getInputStream() != null) {
                msg.setPayload(IOUtils.toString(req.getInputStream(), "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
