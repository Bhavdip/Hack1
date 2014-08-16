package com.takeuse.hack1;

import android.content.Context;

public interface IServicePresenter {

	public void onResume(Context context);
	
	public void onPause(Context context);
	
	public void onDestory();
	
	public void startService();
	
	public void bindService();
	
	public void unBindService();
	
	public void stopService();
	
	public void sendMessage(String message);
}
