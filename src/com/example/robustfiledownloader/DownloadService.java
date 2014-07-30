package com.example.robustfiledownloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class DownloadService extends IntentService{
	
	 public static boolean complete=false; 
	public DownloadService() {
		super("DownloadService");
		// TODO Auto-generated constructor stub
	}

	public HttpURLConnection conn;
	File f;
	static int max;
	 static boolean getmax=false;
	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		int count;
		
		try {
			
			URL url = new URL(MainActivity.fileURL);
			conn =(HttpURLConnection)url.openConnection();
		//	conn.connect();
			
			String filepath=Environment.getExternalStorageDirectory().getPath() + File.separator+"paper.pdf"; 
			f = new File(filepath);
			count=(int)f.length();
			if(f.exists()){
				getmax=false;
				conn.setRequestProperty("Range", "bytes="+count +"-");
	        }
		else{
	        conn.setRequestProperty("Range", "bytes=" + count + "-");
	        getmax=true;
	    }
			conn.setDoInput(true);
			conn.setDoOutput(true);
			if(getmax)
				max=conn.getContentLength();
			MainActivity.p.setMax(conn.getContentLength());
			
			BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
			FileOutputStream out;
			BufferedOutputStream bout;
		     out=(count==0)? new FileOutputStream(filepath): new FileOutputStream(filepath ,true);
		     bout = new BufferedOutputStream(out, 1024);
		    byte[] data = new byte[1024];
		    int x = 0;
		    
		    while ((x = in.read(data, 0, 1024)) >= 0) {
		        bout.write(data, 0, x);
		         count += x;
		         MainActivity.p.setProgress(count);
		         
		    }
		   bout.flush();
		    bout.close();
		    if(f.length()==max){
		    	complete=true;
		    	MainActivity.h.sendEmptyMessage(0);
		 } 
		   
		}catch(Exception e) {
           // Log.e("Error: ", e.getMessage());
		
	}
	}	
	
	
}
