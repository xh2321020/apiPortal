package com.cnnp.social.work.repository.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the W_ADMIN_LINK database table.
 * 
 */
@Entity
@Table(name = "W_ADMIN_LINK")
public class TAdminLink{
	
	@Id
	private long id; //ID

	private String link;//日程标题

	private String icoa;//日程类型

	private String description;//备注
	private String shortname;//简称

	
	public TAdminLink() {
	}



	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcoa() {
		return this.icoa;
	}

	public void setIcoa(String icoa) {
		this.icoa = icoa;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
}