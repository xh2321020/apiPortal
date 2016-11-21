/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.homepage.service;

import com.cnnp.social.homepage.manager.PermissionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * apiPortal
 * Created by Damon on 2016/11/7.
 */
@RestController
@RequestMapping("/api/v1.0/portal")
public class PermissionManagerController {

    @Autowired
    private PermissionManager permissionManager;

    @RequestMapping(value = "/permission/editable", method = RequestMethod.GET)
    public boolean checkPortalEditablePermission(@RequestParam String sn, @RequestParam String site){
        return permissionManager.isSitePortalAdmin(sn,site);
    }

    @RequestMapping(value = "/permission/activable", method = RequestMethod.GET)
    public boolean checkPortalEditablePermission(@RequestParam String sn){
        return permissionManager.isPortalAdmin(sn);
    }
}
