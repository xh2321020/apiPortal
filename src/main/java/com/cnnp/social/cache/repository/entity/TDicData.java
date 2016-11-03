package com.cnnp.social.cache.repository.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "T_DIC_DATA")
public class TDicData implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String diccode;
	private String dicname;
	private String dictype;
	private String status;
	private long parentid;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDiccode() {
		return diccode;
	}
	public void setDiccode(String diccode) {
		this.diccode = diccode;
	}
	public String getDicname() {
		return dicname;
	}
	public void setDicname(String dicname) {
		this.dicname = dicname;
	}
	public String getDictype() {
		return dictype;
	}
	public void setDictype(String dictype) {
		this.dictype = dictype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getParentid() {
		return parentid;
	}
	public void setParentid(long parentid) {
		this.parentid = parentid;
	}
	
	
}
