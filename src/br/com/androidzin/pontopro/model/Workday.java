package br.com.androidzin.pontopro.model;

import java.util.ArrayList;

public class Workday {

	private long workdayID;
	private ArrayList<Checkin> mCheckinList;
	private boolean hasOpenCheckin;
	private int workedTime; // hours .. minutes ?
	private int dailyMark; // hours .. minutes ?
	
	public ArrayList<Checkin> getCheckinList() {
		return mCheckinList;
	}
	public void setCheckinList(ArrayList<Checkin> mCheckinList) {
		this.mCheckinList = mCheckinList;
	}
	public boolean hasOpenCheckin() {
		return hasOpenCheckin;
	}
	public void setHasOpenCheckin(boolean hasOpenCheckin) {
		this.hasOpenCheckin = hasOpenCheckin;
	}
	public int getWorkedTime() {
		return workedTime;
	}
	public void setWorkedTime(int workedTime) {
		this.workedTime = workedTime;
	}
	public int getDailyMark() {
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
}


