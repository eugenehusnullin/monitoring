package monitoring.terminal.munic.domain;

import java.io.Serializable;

public class MunicDataMdi implements Serializable {
	private static final long serialVersionUID = -7504880117857146427L;
	private MunicData municData;

	private Boolean mdiExtBattLow;
	private Integer mdiExtBattVoltage;
	private String mdiAreaList;
	private String mdiCrashDetected;
	
	private String mdiVehicleState;

	// DTC
	private Boolean mdiDtcMil;
	private Integer mdiDtcNumber;
	private String mdiDtcList;

	// RpmStatistic
	private Integer mdiRpmMax;
	private Integer mdiRpmMin;
	private Integer mdiRpmAverage;
	private Boolean mdiRpmOver;

	// CarDiagnostic
	private Integer mdiObdSpeed;
	private Integer mdiObdRpm;
	private Integer mdiObdFuel;
	private String mdiObdVin;
	private Integer mdiObdMileage;

	// Easyconnect
	private Integer mdiJourneyTime;
	private Integer mdiIdleJourney;
	private Integer mdiDrivingJorney;
	private Integer mdiOverSpeedCounter;
	private Boolean mdiTowAway;
	private Integer mdiOdoJourney;
	private Boolean mdiOverSpeed;
	private Integer mdiMaxSpeedJourney;
	private Boolean mdiJourneyState;

	private String mdiRecordReason;
	private String mdiBootReason;
	private String mdiShutdownReason;

	private Boolean mdiPanicState;
	private String mdiPanicMessage;

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

	public String getMdiAreaList() {
		return mdiAreaList;
	}

	public void setMdiAreaList(String mdiAreaList) {
		this.mdiAreaList = mdiAreaList;
	}

	public String getMdiCrashDetected() {
		return mdiCrashDetected;
	}

	public void setMdiCrashDetected(String mdiCrashDetected) {
		this.mdiCrashDetected = mdiCrashDetected;
	}

	public Integer getMdiJourneyTime() {
		return mdiJourneyTime;
	}

	public void setMdiJourneyTime(Integer mdiJourneyTime) {
		this.mdiJourneyTime = mdiJourneyTime;
	}

	public Integer getMdiIdleJourney() {
		return mdiIdleJourney;
	}

	public void setMdiIdleJourney(Integer mdiIdleJourney) {
		this.mdiIdleJourney = mdiIdleJourney;
	}

	public Integer getMdiDrivingJorney() {
		return mdiDrivingJorney;
	}

	public void setMdiDrivingJorney(Integer mdiDrivingJorney) {
		this.mdiDrivingJorney = mdiDrivingJorney;
	}

	public Boolean getMdiTowAway() {
		return mdiTowAway;
	}

	public void setMdiTowAway(Boolean mdiTowAway) {
		this.mdiTowAway = mdiTowAway;
	}

	public Integer getMdiMaxSpeedJourney() {
		return mdiMaxSpeedJourney;
	}

	public void setMdiMaxSpeedJourney(Integer mdiMaxSpeedJourney) {
		this.mdiMaxSpeedJourney = mdiMaxSpeedJourney;
	}

	public Boolean getMdiJourneyState() {
		return mdiJourneyState;
	}

	public void setMdiJourneyState(Boolean mdiJourneyState) {
		this.mdiJourneyState = mdiJourneyState;
	}

	public String getMdiBootReason() {
		return mdiBootReason;
	}

	public void setMdiBootReason(String mdiBootReason) {
		this.mdiBootReason = mdiBootReason;
	}

	public String getMdiShutdownReason() {
		return mdiShutdownReason;
	}

	public void setMdiShutdownReason(String mdiShutdownReason) {
		this.mdiShutdownReason = mdiShutdownReason;
	}

	public Boolean getMdiPanicState() {
		return mdiPanicState;
	}

	public void setMdiPanicState(Boolean mdiPanicState) {
		this.mdiPanicState = mdiPanicState;
	}

	public String getMdiPanicMessage() {
		return mdiPanicMessage;
	}

	public void setMdiPanicMessage(String mdiPanicMessage) {
		this.mdiPanicMessage = mdiPanicMessage;
	}
}
