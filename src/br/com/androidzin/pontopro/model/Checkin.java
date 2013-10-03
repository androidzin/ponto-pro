package br.com.androidzin.pontopro.model;


public class Checkin {

	public enum CheckinType {
		ENTERED, ANY_ENTRANCE_BEFORE_LUNCH, ANY_LEAVING_BEFORE_LUNCH, LUNCH,
		AFTER_LUNCH, ANY_ENTRANCE_AFTER_LUNCH, ANY_LEAVING_AFTER_LUNCH, LEAVING
	}
	
	public interface CheckinListener {
		public void onCheckinDone(CheckinType checkin, long checkinTime, long workedHours, long dailyMark);

	}
	
	//private String timeStampString;
	private long checkinID;
	private long workdayID;
	private CheckinType type;
	private long timeStamp;
	
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

	/*public String getTimeStampString() {
		return timeStampString;
	}

	public void setTimeStampSting(String timeStamp) {
		this.timeStampString = timeStamp;
	}*/

	public int getCheckinIntType() {
		return type.ordinal();
	}
	
	public void setType(int ordinal){
		if ( ordinal < CheckinType.values().length)
			type = CheckinType.values()[ordinal];
	}

	public void setTimeStamp(long currentTimeMillis) {
		this.timeStamp = currentTimeMillis;
	}

	public long getTime() {
		return timeStamp;
	}
	
}


