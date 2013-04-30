package br.com.androidzin.pontopro.model;

public class Checkin {

	private String timeStamp;
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

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
