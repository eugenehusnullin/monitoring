package monitoring.domain;

import java.util.Date;

public class AlarmVinChanged {
	private Long id;
	private Long carId;
	private Long imei;
	private String vinNew;
	private String vinOld;

	private Date offDate;
	private Double offLat;
	private Double offLon;
	private Long offMileage;

	private Date onDate;
	private Double onLat;
	private Double onLon;
	private Long onMileage;

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

	public Date getOffDate() {
		return offDate;
	}

	public void setOffDate(Date offDate) {
		this.offDate = offDate;
	}

	public Double getOffLat() {
		return offLat;
	}

	public void setOffLat(Double offLat) {
		this.offLat = offLat;
	}

	public Double getOffLon() {
		return offLon;
	}

	public void setOffLon(Double offLon) {
		this.offLon = offLon;
	}

	public Long getOffMileage() {
		return offMileage;
	}

	public void setOffMileage(Long offMileage) {
		this.offMileage = offMileage;
	}

	public Date getOnDate() {
		return onDate;
	}

	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}

	public Double getOnLat() {
		return onLat;
	}

	public void setOnLat(Double onLat) {
		this.onLat = onLat;
	}

	public Double getOnLon() {
		return onLon;
	}

	public void setOnLon(Double onLon) {
		this.onLon = onLon;
	}

	public Long getOnMileage() {
		return onMileage;
	}

	public void setOnMileage(Long onMileage) {
		this.onMileage = onMileage;
	}

}
