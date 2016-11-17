/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.homepage.repository.dao;

import com.cnnp.social.homepage.repository.entity.TPortalRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * apiPortal
 * Created by Damon on 2016/11/7.
 */
public interface PermissionManagerDao extends CrudRepository<TPortalRole, String>,
        JpaSpecificationExecutor<TPortalRole> {
    @Query("select role from TPortalRole role where role.members like ?1 and (role.site='HQ' or role.site=?2) and role.roleName=?3")
    TPortalRole checkSitePermission(String sn,String site,String rolename);

    @Query("select role from TPortalRole role where role.members=?1 and role.site='HQ' and role.roleName=?2")
    TPortalRole checkPermission(String sn,String rolename);

}
