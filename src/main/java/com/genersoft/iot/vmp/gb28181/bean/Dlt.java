package com.genersoft.iot.vmp.gb28181.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lin
 */
public class Dlt {

	private BigDecimal epimp;

	private BigDecimal ua;

	private BigDecimal ia;

	private Date time;

	public BigDecimal getEpimp() {
		return epimp;
	}

	public void setEpimp(BigDecimal epimp) {
		this.epimp = epimp;
	}

	public BigDecimal getUa() {
		return ua;
	}

	public void setUa(BigDecimal ua) {
		this.ua = ua;
	}

	public BigDecimal getIa() {
		return ia;
	}

	public void setIa(BigDecimal ia) {
		this.ia = ia;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
