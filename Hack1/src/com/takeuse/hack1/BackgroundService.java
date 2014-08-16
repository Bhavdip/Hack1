package com.takeuse.hack1;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

/**
 * Background service handle the incoming request from client and send the
 * message back to client
 * 
 * @author Bhavdip
 *
 */
public class BackgroundService extends Service{

	ArrayList<Messenger> mClientRequestList = new ArrayList<Messenger>();
	
	public final static int REQUEST_MESSAGE = 1;
	
	public final static int REQUEST_ADDED = 2; 
	
	public final static int REQUEST_REMOVE = 3;
	
	final Messenger mServiceManager = new Messenger(new ClientHandler(Looper.getMainLooper()));
			
	
	@Override
	public IBinder onBind(Intent intent) {
		return mServiceManager.getBinder();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	class ClientHandler extends Handler{
		
		public ClientHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case REQUEST_MESSAGE:{
				sendMessageToClient(new String("Server :"+ msg.obj));
				break;
			}
			
			case REQUEST_ADDED:{
				// we handle the client incoming messenger here
				if(mClientRequestList != null){
					mClientRequestList.add(msg.replyTo);	
				}
				break;
			}
			
			case REQUEST_REMOVE:{
				// when client destroy it call remove we have to remove the client messenger
				if(mClientRequestList != null && mClientRequestList.size() > 0){
					mClientRequestList.remove(msg.replyTo);	
				}
				break;
			}
			
			default:
				break;
			}
		}
	}
	
	
	 private void sendMessageToClient(String message) {
		 
		if (mClientRequestList!= null && mClientRequestList.size() > 0) {
			
			for (int i = mClientRequestList.size() - 1; i >= 0; i--) {
				
				try
				{
					 Message mesg = Message.obtain();
					 mesg.what = BackgroundService.REQUEST_MESSAGE;
					 mesg.obj = message;
					 mClientRequestList.get(i).send(mesg);
					
				}catch(Exception e){
					
					mClientRequestList.remove(i);
					e.printStackTrace();
				}
				
			 }
		 }
	 }
}
