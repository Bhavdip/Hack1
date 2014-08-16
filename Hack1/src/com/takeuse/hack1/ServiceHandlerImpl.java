package com.takeuse.hack1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;


/**
 * It will handle the service Connection logic such like start stop bind and
 * unBind the service with activity, send and receive the message communication
 * 
 * Implement to the ISeviceHandler that handle by the Service Presenter class
 * 
 * @author Bhavdip
 *
 */
public class ServiceHandlerImpl implements IServiceHandler{
	
	private Context mContext;
	
	private HandleServiceListener mHandleServiceListener;
	
	private ServiceConnection mServiceConnection;
	
	final Messenger mMessenger = new Messenger(new IncomingHandler(Looper.getMainLooper()));
	
	private Messenger mService = null;
	
	private boolean mIsBound;
	
	public ServiceHandlerImpl(Context activityContext){
		
		this.mContext = activityContext;
		
		this.mServiceConnection = new BackgroundServiceConnection();
	}
	
	private Context getContext(){
		return mContext;
	}
	
	@Override
	public void connectService() {
		startService();
	}

	@Override
	public void disConnectService() {
		stopService();
	}

	@Override
	public void bindService() {
		// The third parameter is a flag indicating options for the binding. It
		// should usually be BIND_AUTO_CREATE in order to create the service if
		// its not already alive. Other possible values are BIND_DEBUG_UNBIND
		// and BIND_NOT_FOREGROUND, or 0 for none.
		if(mServiceConnection != null){
			getContext().bindService(new Intent(getContext(),BackgroundService.class), mServiceConnection,Context.BIND_AUTO_CREATE);	
		}
		
	}

	@Override
	public void unBindService() {
		if(mServiceConnection != null){
			getContext().unbindService(mServiceConnection);	
			mIsBound = false;
			mService = null;
		}
	}

	@Override
	public void addServiceListener(HandleServiceListener mListener) {
		this.mHandleServiceListener = mListener;
	}
	
	private boolean hasListenerAvailable(){
		
		if(mHandleServiceListener!= null){
			return true;
		}else{
			
			try {
				throw new Exception("HandleServiceListener No register ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Override
	public void sendMessage(String message) {
		sendMessageToService(message);
	}
	
	private void startService(){
		getContext().startService(new Intent(getContext(),BackgroundService.class));
	}
	
	private void stopService(){
		getContext().stopService(new Intent(getContext(), BackgroundService.class));
	}
	
	
	class BackgroundServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
			mService = new Messenger(service);
			
			if(hasListenerAvailable())
			mHandleServiceListener.onMessageHandler("Service Bind..");
			
			try{
				
				Message requestMessage = Message.obtain();
				
				requestMessage.what = BackgroundService.REQUEST_ADDED;
				
				// reply to the background service and give it to register Messagner
				requestMessage.replyTo = mMessenger;
						
				mService.send(requestMessage);
				
			}catch(Exception e){}
			
			mIsBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIsBound = false;
			mService = null;
		}
	}
	
	/**
	 * Use to send the message to the background service 
	 * 
	 * @param value
	 */
	private void sendMessageToService(String value) {
		
		if(mIsBound){
			
			try
			{
				if(hasListenerAvailable())
					mHandleServiceListener.onMessageHandler(String.format("Client : %2s", value));
				
				Message message = Message.obtain();
				
				message.what = BackgroundService.REQUEST_MESSAGE;
				
				message.obj = value;
				
				mService.send(message);
				
			}catch(Exception e){
				
			}
			
		}else{
			
			// service not bound please start the service 
			mHandleServiceListener.onMessageHandler("Failed Servie Not bounded");
		}
	}
	
	/**
	 * Handler Message incoming from background service to activity 
	 * 
	 * @author Bhavdip
	 *
	 */
	class IncomingHandler extends Handler {
		
		
		public IncomingHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BackgroundService.REQUEST_MESSAGE: {
				mHandleServiceListener.onMessageHandler((String)msg.obj);
				break;				
			}

			default:
				break;
			}
		}
	}
}
