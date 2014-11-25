package ru.gm.munic.domain;

import java.util.Date;

/*
 * Table: top_block_alert Columns:
 * 
 * alertId int(11)
 * autoId int(11)
 * alertType int(2)
 * addDate datetime
 * status int(1)
 * alertJSON varchar(5000)
 */

public class TopBlockAlert {
	private Integer id;
	private TopAuto topAuto;
	private Integer alertType;
	private Date addDate;
	private Integer status;
	private String message;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TopAuto getTopAuto() {
		return topAuto;
	}

	public void setTopAuto(TopAuto topAuto) {
		this.topAuto = topAuto;
	}

	public Integer getAlertType() {
		return alertType;
	}

	public void setAlertType(Integer alertType) {
		this.alertType = alertType;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
