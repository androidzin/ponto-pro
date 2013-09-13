package br.com.androidzin.pontopro.model;

import java.util.ArrayList;
import java.util.List;

public class Workday {

	protected long workdayID;
	protected List<Checkin> mCheckinList;
	private String timeStamp;
	protected boolean hasOpenCheckin;
	protected long workedTime; // minutes
	protected long dailyMark; // minutes
	protected long initialTime;

	public long getInitialTime() {
		return initialTime;
	}

	public void setInitialTime(long initialTime) {
		this.initialTime = initialTime;
	}

	public Workday() {
		workedTime = 0;
		dailyMark = 0;
		workdayID = 0;
		hasOpenCheckin = false;
		mCheckinList = new ArrayList<Checkin>();
	}
	
	public boolean wasStarted(){
		if ( workdayID != 0){
			return true;
		} else {
			return false;
		}
	}

	public List<Checkin> getCheckinList() {
		return mCheckinList;
	}

	public void setCheckinList(List<Checkin> checkinList) {
		this.mCheckinList = checkinList;
	}
	
	public boolean hasWorked(){
		if ( workedTime > dailyMark ) {
			return true;
		}
		return false;
	}

	public boolean hasOpenCheckin() {
		return hasOpenCheckin;
	}

	public void setHasOpenCheckin(boolean hasOpenCheckin) {
		this.hasOpenCheckin = hasOpenCheckin;
	}

	public long getWorkedTime() {
		return workedTime;
	}

	public void setWorkedTime(int workedTime) {
		this.workedTime = workedTime;
	}

	public long getDailyMark() {
		return dailyMark;
	}

	public void setDailyMark(int dailyMark) {
		this.dailyMark = dailyMark;
	}

	public long getWorkdayID() {
		return workdayID;
	}

	public void setWorkdayID(long workdayID) {
		this.workdayID = workdayID;
	}

	public void updateWorkdayStatus() {
		hasOpenCheckin = !hasOpenCheckin;
	}

	public void addCheckin(Checkin checkin) {
		mCheckinList.add(checkin);
	}

	public Checkin getLastCheckin() {
		return mCheckinList.get(mCheckinList.size() - 1);
	}
	
	public boolean haveCheckedIn(){
		return mCheckinList.size() > 0;
	}
	
	@Override
	public boolean equals(Object theOther) {
		if ( this == theOther) {
			return true;
		}
		if ( !(this instanceof Workday) ) {
			return false;
		}
		Workday other = (Workday) theOther;
		return other.getWorkdayID() == other.getWorkdayID();
	}

	public void setTimeStamp(String string) {
		timeStamp = string;
	}
	
	public String getStringDate(){
		return timeStamp;
	}

	public void setWorkedHours(int worked) {
		workedTime = worked;
	}

	public void setClosed(int open) {
		hasOpenCheckin = Boolean.FALSE;
	}

	public void setDailyMak(int mark) {
		dailyMark = mark;
	}
	
	public boolean isClosed(){
		return !hasOpenCheckin();
	}
}
