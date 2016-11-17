/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.homepage.repository.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * apiPortal
 * Created by Damon on 2016/11/4.
 */
@Entity
@Table(name = "T_PORTAL_ROLE")
public class TPortalRole {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ID", length = 64)
    private String id;
    @Column(name = "ROLENAME", length = 64)
    private String roleName;
    @Column(name = "MEMBERS", length = 255)
    private String members;
    @Column(name = "SITE", length = 4)
    private String site;
    @Column(name = "STATUS")
    private int status;
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
