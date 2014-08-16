package com.takeuse.hack1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity implements IMainView{
	
	
	private TextView mMessage;
	
	private StringBuilder mMessageBuilder;
	
	private IServicePresenter mPresenterImpl;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mMessage = (TextView)findViewById(R.id.textview_message);
        
        mMessageBuilder = new StringBuilder();
        
        mPresenterImpl = new ServicePresenterImpl(this);
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mPresenterImpl.onResume(getActivityContext());
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mPresenterImpl.onPause(getActivityContext());
    }
    
    public String getTextViewContent(){
    	return mMessage.getText().toString();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	mPresenterImpl.onDestory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
		switch (id) {
		case R.id.action_startservice: {
			mPresenterImpl.startService();
			break;
		}
		case R.id.action_stopservice:{
			mPresenterImpl.stopService();
			break;
		}
		case R.id.action_bind_service:{
			mPresenterImpl.bindService();
			break;
		}
		case R.id.action_unbind_service:{
			mPresenterImpl.unBindService();
			break;
		}
		case R.id.action_send_message:{
			mPresenterImpl.sendMessage(String.format("%s : %3d","Test Message",System.currentTimeMillis()));
			break;
		}
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
    }


	@Override
	public void receiveMessage(String message) {
		mMessageBuilder.append("\n").append(message);
		
		mMessage.setText(mMessageBuilder.toString());
	}


	@Override
	public Context getActivityContext() {
		return MainActivity.this;
	}
	
}
