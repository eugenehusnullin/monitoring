package monitoring.domain;

import java.util.Date;

public class AlarmVinChanged {
	private Long id;
	private Long carId;
	private Long imei;
	private String vinNew;
	private String vinOld;
	private Date alarmDate;

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public String getVinNew() {
		return vinNew;
	}

	public void setVinNew(String vinNew) {
		this.vinNew = vinNew;
	}

	public String getVinOld() {
		return vinOld;
	}

	public void setVinOld(String vinOld) {
		this.vinOld = vinOld;
	}

	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}

	public Long getImei() {
		return imei;
	}

	public void setImei(Long imei) {
		this.imei = imei;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// autoId int(11)
	// VINnew varchar(255)
	// VINold varchar(255)
	// changeDate datetime

}
