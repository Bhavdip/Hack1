package com.takeuse.hack1;

import android.content.Context;


public interface IMainView {
	
	public Context getActivityContext();
	
	public void receiveMessage(String message);
}
