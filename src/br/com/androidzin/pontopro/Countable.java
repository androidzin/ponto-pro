package br.com.androidzin.pontopro;

public interface Countable {

	public void onTick(long millisElapsedTime, long millisUntilFinished);
	
	public void onFinish();
}
