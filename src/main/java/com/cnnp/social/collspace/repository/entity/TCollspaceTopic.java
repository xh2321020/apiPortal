package com.cnnp.social.collspace.repository.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TOPICINFO database table.
 * 
 */
@Entity
@Table(name = "TOPICINFO")
public class TCollspaceTopic{
	
	@Id
	private long topicid;//主题ID
	private long collspaceid; //空间ID

	private long createuserid;//创建人ID

	private String createusername;//创建人

	private Date createtime;//创建时间

	private Date updatetime;//更新时间

	private String description;//备注信息

	private String remarkname;//主题名

	public TCollspaceTopic() {
	}

	public long getCollspaceid() {
		return this.collspaceid;
	}

	public void setCollspaceid(long collspaceid) {
		this.collspaceid = collspaceid;
	}

	public long getTopicid() {
		return this.topicid;
	}

	public void setTopicid(long topicid) {
		this.topicid = topicid;
	}

	public long getCreateuserid() {
		return this.createuserid;
	}

	public void setCreateuserid(long createuserid) {
		this.createuserid = createuserid;
	}

	public String getCreateusername() {
		return this.createusername;
	}

	public void setCreateusername(String createusername) {
		this.createusername = createusername;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarkname() {
		return this.remarkname;
	}

	public void setRemarkname(String remarkname) {
		this.remarkname = remarkname;
	}


	
}