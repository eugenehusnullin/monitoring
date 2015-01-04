package monitoring.terminal.munic.domain;

import java.io.Serializable;

public class MunicDataBehave implements Serializable {
	private static final long serialVersionUID = 4756527148107498257L;

	private MunicData municData;
	private Integer behaveUniqueId;
	private Integer behaveId;
	private Integer behaveElapsed;
	private Integer behaveGpsSpeedBegin;
	private Integer behaveGpsSpeedPeak;
	private Integer behaveGpsSpeedEnd;
	private Integer behaveGpsHeadingBegin;
	private Integer behaveGpsHeadingPeak;
	private Integer behaveGpsHeadingEnd;
	private Integer behaveAccXBegin;
	private Integer behaveAccXPeak;
	private Integer behaveAccXEnd;
	private Integer behaveAccYBegin;
	private Integer behaveAccYPeak;
	private Integer behaveAccYEnd;
	private Integer behaveAccZBegin;
	private Integer behaveAccZPeak;
	private Integer behaveAccZEnd;
	private Integer behaveLong;
	private Integer behaveLat;
	private Integer behaveDayOfYear;
	private Integer behaveTimeOfDay;

	public MunicData getMunicData() {
		return municData;
	}

	public void setMunicData(MunicData municData) {
		this.municData = municData;
	}

	public Integer getBehaveUniqueId() {
		return behaveUniqueId;
	}

	public void setBehaveUniqueId(Integer behaveUniqueId) {
		this.behaveUniqueId = behaveUniqueId;
	}

	public Integer getBehaveId() {
		return behaveId;
	}

	public void setBehaveId(Integer behaveId) {
		this.behaveId = behaveId;
	}

	public Integer getBehaveElapsed() {
		return behaveElapsed;
	}

	public void setBehaveElapsed(Integer behaveElapsed) {
		this.behaveElapsed = behaveElapsed;
	}

	public Integer getBehaveGpsSpeedBegin() {
		return behaveGpsSpeedBegin;
	}

	public void setBehaveGpsSpeedBegin(Integer behaveGpsSpeedBegin) {
		this.behaveGpsSpeedBegin = behaveGpsSpeedBegin;
	}

	public Integer getBehaveGpsSpeedPeak() {
		return behaveGpsSpeedPeak;
	}

	public void setBehaveGpsSpeedPeak(Integer behaveGpsSpeedPeak) {
		this.behaveGpsSpeedPeak = behaveGpsSpeedPeak;
	}

	public Integer getBehaveGpsSpeedEnd() {
		return behaveGpsSpeedEnd;
	}

	public void setBehaveGpsSpeedEnd(Integer behaveGpsSpeedEnd) {
		this.behaveGpsSpeedEnd = behaveGpsSpeedEnd;
	}

	public Integer getBehaveGpsHeadingBegin() {
		return behaveGpsHeadingBegin;
	}

	public void setBehaveGpsHeadingBegin(Integer behaveGpsHeadingBegin) {
		this.behaveGpsHeadingBegin = behaveGpsHeadingBegin;
	}

	public Integer getBehaveGpsHeadingPeak() {
		return behaveGpsHeadingPeak;
	}

	public void setBehaveGpsHeadingPeak(Integer behaveGpsHeadingPeak) {
		this.behaveGpsHeadingPeak = behaveGpsHeadingPeak;
	}

	public Integer getBehaveGpsHeadingEnd() {
		return behaveGpsHeadingEnd;
	}

	public void setBehaveGpsHeadingEnd(Integer behaveGpsHeadingEnd) {
		this.behaveGpsHeadingEnd = behaveGpsHeadingEnd;
	}

	public Integer getBehaveAccXBegin() {
		return behaveAccXBegin;
	}

	public void setBehaveAccXBegin(Integer behaveAccXBegin) {
		this.behaveAccXBegin = behaveAccXBegin;
	}

	public Integer getBehaveAccXPeak() {
		return behaveAccXPeak;
	}

	public void setBehaveAccXPeak(Integer behaveAccXPeak) {
		this.behaveAccXPeak = behaveAccXPeak;
	}

	public Integer getBehaveAccXEnd() {
		return behaveAccXEnd;
	}

	public void setBehaveAccXEnd(Integer behaveAccXEnd) {
		this.behaveAccXEnd = behaveAccXEnd;
	}

	public Integer getBehaveAccYBegin() {
		return behaveAccYBegin;
	}

	public void setBehaveAccYBegin(Integer behaveAccYBegin) {
		this.behaveAccYBegin = behaveAccYBegin;
	}

	public Integer getBehaveAccYPeak() {
		return behaveAccYPeak;
	}

	public void setBehaveAccYPeak(Integer behaveAccYPeak) {
		this.behaveAccYPeak = behaveAccYPeak;
	}

	public Integer getBehaveAccYEnd() {
		return behaveAccYEnd;
	}

	public void setBehaveAccYEnd(Integer behaveAccYEnd) {
		this.behaveAccYEnd = behaveAccYEnd;
	}

	public Integer getBehaveAccZBegin() {
		return behaveAccZBegin;
	}

	public void setBehaveAccZBegin(Integer behaveAccZBegin) {
		this.behaveAccZBegin = behaveAccZBegin;
	}

	public Integer getBehaveAccZPeak() {
		return behaveAccZPeak;
	}

	public void setBehaveAccZPeak(Integer behaveAccZPeak) {
		this.behaveAccZPeak = behaveAccZPeak;
	}

	public Integer getBehaveAccZEnd() {
		return behaveAccZEnd;
	}

	public void setBehaveAccZEnd(Integer behaveAccZEnd) {
		this.behaveAccZEnd = behaveAccZEnd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getBehaveLong() {
		return behaveLong;
	}

	public void setBehaveLong(Integer behaveLong) {
		this.behaveLong = behaveLong;
	}

	public Integer getBehaveLat() {
		return behaveLat;
	}

	public void setBehaveLat(Integer behaveLat) {
		this.behaveLat = behaveLat;
	}

	public Integer getBehaveDayOfYear() {
		return behaveDayOfYear;
	}

	public void setBehaveDayOfYear(Integer behaveDayOfYear) {
		this.behaveDayOfYear = behaveDayOfYear;
	}

	public Integer getBehaveTimeOfDay() {
		return behaveTimeOfDay;
	}

	public void setBehaveTimeOfDay(Integer behaveTimeOfDay) {
		this.behaveTimeOfDay = behaveTimeOfDay;
	}
}
