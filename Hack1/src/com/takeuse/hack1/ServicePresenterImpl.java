package com.takeuse.hack1;

import android.content.Context;

public class ServicePresenterImpl implements IServicePresenter,HandleServiceListener{

	private IMainView mIMainView;
	
	private IServiceHandler mServiceHandler;
	
	public ServicePresenterImpl(IMainView mIMainView){
		
		this.mIMainView = mIMainView;
		
		this.mServiceHandler = new ServiceHandlerImpl(mIMainView.getActivityContext());
		
		this.mServiceHandler.addServiceListener(this);
	}

	@Override
	public void onResume(Context context) {
		
	}

	@Override
	public void onPause(Context context) {
		
	}

	@Override
	public void onDestory() {
		mServiceHandler.unBindService();
	}

	@Override
	public void startService() {
		mServiceHandler.connectService();
	}

	@Override
	public void bindService() {
		mServiceHandler.bindService();
	}

	@Override
	public void unBindService() {
		mServiceHandler.unBindService();
	}

	@Override
	public void stopService() {
		mServiceHandler.disConnectService();
	}

	@Override
	public void sendMessage(String message) {
		mServiceHandler.sendMessage(message);
	}

	@Override
	public void onMessageHandler(String message) {
		mIMainView.receiveMessage(message);
	}
	
	
}
