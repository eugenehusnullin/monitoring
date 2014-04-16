package ru.gm.munic.domain;

import java.io.Serializable;

public class MunicDataMdi implements Serializable {
	private static final long serialVersionUID = -7504880117857146427L;

	private MunicData municData;

	private Boolean mdiExtBattLow;
	private Integer mdiExtBattVoltage;
	private Boolean mdiDtcMil;
	private Integer mdiDtcNumber;
	private String mdiDtcList;

	private Integer mdiRpmMax;
	private Integer mdiRpmMin;
	private Integer mdiRpmAverage;
	private Boolean mdiRpmOver;

	private Integer mdiObdSpeed;
	private Integer mdiObdRpm;
	private Integer mdiObdFuel;
	private String mdiObdVin;
	private Integer mdiObdMileage;

	private Boolean mdiOverSpeed;
	private Integer mdiOverSpeedCounter;
	private Integer mdiOdoJourney;
	private String mdiRecordReason;
	private String mdiVehicleState;

	public MunicData getMunicData() {
		return municData;
	}

	public void setMunicData(MunicData municData) {
		this.municData = municData;
	}

	public Boolean getMdiExtBattLow() {
		return mdiExtBattLow;
	}

	public void setMdiExtBattLow(Boolean mdiExtBattLow) {
		this.mdiExtBattLow = mdiExtBattLow;
	}

	public Integer getMdiExtBattVoltage() {
		return mdiExtBattVoltage;
	}

	public void setMdiExtBattVoltage(Integer mdiExtBattVoltage) {
		this.mdiExtBattVoltage = mdiExtBattVoltage;
	}

	public Boolean getMdiDtcMil() {
		return mdiDtcMil;
	}

	public void setMdiDtcMil(Boolean mdiDtcMil) {
		this.mdiDtcMil = mdiDtcMil;
	}

	public Integer getMdiDtcNumber() {
		return mdiDtcNumber;
	}

	public void setMdiDtcNumber(Integer mdiDtcNumber) {
		this.mdiDtcNumber = mdiDtcNumber;
	}

	public String getMdiDtcList() {
		return mdiDtcList;
	}

	public void setMdiDtcList(String mdiDtcList) {
		this.mdiDtcList = mdiDtcList;
	}

	public Integer getMdiRpmMax() {
		return mdiRpmMax;
	}

	public void setMdiRpmMax(Integer mdiRpmMax) {
		this.mdiRpmMax = mdiRpmMax;
	}

	public Integer getMdiRpmMin() {
		return mdiRpmMin;
	}

	public void setMdiRpmMin(Integer mdiRpmMin) {
		this.mdiRpmMin = mdiRpmMin;
	}

	public Integer getMdiRpmAverage() {
		return mdiRpmAverage;
	}

	public void setMdiRpmAverage(Integer mdiRpmAverage) {
		this.mdiRpmAverage = mdiRpmAverage;
	}

	public Boolean getMdiRpmOver() {
		return mdiRpmOver;
	}

	public void setMdiRpmOver(Boolean mdiRpmOver) {
		this.mdiRpmOver = mdiRpmOver;
	}

	public Integer getMdiObdSpeed() {
		return mdiObdSpeed;
	}

	public void setMdiObdSpeed(Integer mdiObdSpeed) {
		this.mdiObdSpeed = mdiObdSpeed;
	}

	public Integer getMdiObdRpm() {
		return mdiObdRpm;
	}

	public void setMdiObdRpm(Integer mdiObdRpm) {
		this.mdiObdRpm = mdiObdRpm;
	}

	public Integer getMdiObdFuel() {
		return mdiObdFuel;
	}

	public void setMdiObdFuel(Integer mdiObdFuel) {
		this.mdiObdFuel = mdiObdFuel;
	}

	public String getMdiObdVin() {
		return mdiObdVin;
	}

	public void setMdiObdVin(String mdiObdVin) {
		this.mdiObdVin = mdiObdVin;
	}

	public Integer getMdiObdMileage() {
		return mdiObdMileage;
	}

	public void setMdiObdMileage(Integer mdiObdMileage) {
		this.mdiObdMileage = mdiObdMileage;
	}

	public Integer getMdiOverSpeedCounter() {
		return mdiOverSpeedCounter;
	}

	public void setMdiOverSpeedCounter(Integer mdiOverSpeedCounter) {
		this.mdiOverSpeedCounter = mdiOverSpeedCounter;
	}

	public Integer getMdiOdoJourney() {
		return mdiOdoJourney;
	}

	public void setMdiOdoJourney(Integer mdiOdoJourney) {
		this.mdiOdoJourney = mdiOdoJourney;
	}

	public String getMdiRecordReason() {
		return mdiRecordReason;
	}

	public void setMdiRecordReason(String mdiRecordReason) {
		this.mdiRecordReason = mdiRecordReason;
	}

	public String getMdiVehicleState() {
		return mdiVehicleState;
	}

	public void setMdiVehicleState(String mdiVehicleState) {
		this.mdiVehicleState = mdiVehicleState;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getMdiOverSpeed() {
		return mdiOverSpeed;
	}

	public void setMdiOverSpeed(Boolean mdiOverSpeed) {
		this.mdiOverSpeed = mdiOverSpeed;
	}
}
