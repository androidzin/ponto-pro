package br.com.androidzin.pontopro.model;

public class Checkin {

	private long timeStamp;
	private long checkinID;
	private long workdayID;

	public long getCheckinID() {
		return checkinID;
	}

	public void setCheckinID(long checkinID) {
		this.checkinID = checkinID;
	}

	public long getWorkdayID() {
		return workdayID;
	}

	public void setWorkdayID(long workdayID) {
		this.workdayID = workdayID;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
