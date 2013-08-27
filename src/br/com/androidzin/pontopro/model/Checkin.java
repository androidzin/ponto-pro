package br.com.androidzin.pontopro.model;

public class Checkin {

	public enum CheckinType {
		ENTERED, LUNCH, AFTER_LUNCH, LEAVING, ANY_ENTRANCE, ANY_LEAVING, ANY
	}
	
	public interface CheckinListener {
		public void onCheckinDone(CheckinType checkin, long when, long workedHours);
	}
	
	private String timeStamp;
	private long checkinID;
	private long workdayID;
	private CheckinType type;
	
	public Checkin() {
		// TODO Auto-generated constructor stub
	}
	
	public Checkin(long workdayID){
		this.workdayID = workdayID;
	}
	
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

	public int getCheckinIntType() {
		return type.ordinal();
	}
	
	public void setType(int ordinal){
		if ( ordinal < CheckinType.values().length)
			type = CheckinType.values()[ordinal];
	}
}


