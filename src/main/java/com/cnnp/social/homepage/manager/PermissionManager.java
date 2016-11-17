/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.homepage.manager;

import com.cnnp.social.homepage.exception.NoAuthenticationException;
import com.cnnp.social.homepage.repository.dao.PermissionManagerDao;
import com.cnnp.social.homepage.repository.entity.TPortalRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * apiPortal
 * Created by Damon on 2016/11/7.
 */
@EnableTransactionManagement
@Component
public class PermissionManager {
    @Autowired
    private PermissionManagerDao securityValidateDao;

    private static String SITE_PORTAL_ADMIN_GROUP="SitePortalAdmins";
    private static String PORTAL_ADMIN_GROUP="PortalAdmins";
    private static String SITE_FLAG_HQ="HQ";//总部站点标识
    /**
     * 判断用户员工名是否为指定站点的门户管理员
     * @param sn 员工号
     * @param site 站点
     * @return boolean
     */
    public boolean isSitePortalAdmin(String sn,String site){
        TPortalRole role = securityValidateDao.checkSitePermission("%"+sn+"%",site, PermissionManager.SITE_PORTAL_ADMIN_GROUP);
        if(role == null){
            throw new NoAuthenticationException(111,sn,site);
        }
        return true;
    }

    /**
     * 判断用户员工名是否为指定部门的门户管理员
     * @param sn 员工号
     * @return boolean
     */
    public boolean isPortalAdmin(String sn){
        TPortalRole role = securityValidateDao.checkPermission("%"+sn+"%", PermissionManager.PORTAL_ADMIN_GROUP);
        if(role == null){
            throw new NoAuthenticationException(111,sn,SITE_FLAG_HQ);
        }
        return true;
    }
}
