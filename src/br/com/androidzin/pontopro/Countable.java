package br.com.androidzin.pontopro;

public interface Countable {

	public void onTick(long milisElapsedTime, long millisUntilFinished);
	
	public void onFinish();
}
