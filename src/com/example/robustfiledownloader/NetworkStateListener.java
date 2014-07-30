package com.example.robustfiledownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkStateListener extends BroadcastReceiver{
	
	public ConnectivityManager cm;
	public NetworkInfo n;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent dls = new Intent();
		dls.setClass(context, DownloadService.class);
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
	    NetworkInfo n= cm.getActiveNetworkInfo();
	    if(n!=null && n.isConnected()){
	    	
	    	Toast.makeText(context,"Type"+n.getTypeName()+"Sub:"+n.getSubtypeName(), Toast.LENGTH_SHORT).show();
	    	if(MainActivity.sched==true || MainActivity.pause==true)
	    		{
	    			MainActivity.downloading=true;
	    			context.startService(dls);
	    			
	    		}
	    	}
	    else{
	    	if(MainActivity.downloading && !DownloadService.complete)
	    	{	
	    		MainActivity.pause=true;
	    		Toast.makeText(context,"Download Paused", Toast.LENGTH_SHORT).show();
	    	}
	    	else if(n!=null && !n.isConnected()){
	    		Toast.makeText(context,"No network", Toast.LENGTH_SHORT).show();
	    	}	
	
	    }
	}
	
}
