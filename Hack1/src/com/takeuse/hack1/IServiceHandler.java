package com.takeuse.hack1;

public interface IServiceHandler {
	
	public void sendMessage(String message);
	
	public void addServiceListener(HandleServiceListener mListener);
	
	public void connectService();
	
	public void disConnectService();
	
	public void bindService();
	
	public void unBindService();
	
	
}
