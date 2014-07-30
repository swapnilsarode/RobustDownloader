package com.example.robustfiledownloader;

import java.io.File;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity{

	private Button b;
	public static boolean sched=false;
	public static boolean downloading=false;
	public static ProgressBar p;
	private File f;
	public static boolean pause=false;
	public static String fileURL="http://www.winlab.rutgers.edu/~janne/chi2011web.pdf"; 
	PackageManager pm;
	ComponentName cm;
	public static Button of;
	public static Handler h;
	private EditText t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b= (Button) findViewById(R.id.button1);
		p = (ProgressBar) findViewById(R.id.progressBar1);
		PackageManager pm = getPackageManager();
		ComponentName cm= new ComponentName(this,NetworkStateListener.class);
		final NetworkStateListener nlist = new NetworkStateListener();
		nlist.cm= (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
		nlist.n=nlist.cm.getActiveNetworkInfo();
		of=(Button) findViewById(R.id.button2);
		final Intent iServ= new Intent(this,DownloadService.class);
		t=(EditText) findViewById(R.id.editText1);
		
		
		h=new Handler(){
			@Override
		      public void handleMessage(Message msg) {
				
				Toast.makeText(getApplicationContext(),"Download Complete; Your file can be found in Storage", Toast.LENGTH_SHORT).show();		      
				of.setVisibility(View.VISIBLE);
				
			}
		};
		
		
		of.setVisibility(View.INVISIBLE);
		
		b.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					
					if(downloading==true);
				else if(nlist.n != null && nlist.n.isConnected())
				{	
					fileURL=t.getText().toString();
					t.setText("");
					startService(iServ);
					downloading=true;
					
				}		
					else{ 
					Toast.makeText(getApplicationContext(),"Scheduled for later", Toast.LENGTH_SHORT).show();
					sched=true;
						}
					
			}
			
		});
	
		
		
		of.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String fpath=Environment.getExternalStorageDirectory().getPath() + File.separator+"paper.pdf";
				f=new File(fpath);
				if(f.exists() && DownloadService.complete)
				{
					Uri filepath = Uri.fromFile(f);
	                Intent intent = new Intent(Intent.ACTION_VIEW);
	                intent.setDataAndType(filepath, "application/pdf");
	                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	                try {
	                    startActivity(intent);
	                } catch (Exception e) {
	                               
	                	Log.e("error", "" + e);
	                }

				}
				else 
					Toast.makeText(getApplicationContext(),"File not downloaded yet", Toast.LENGTH_SHORT).show();
			}
			
		});
				
	}

	public void showStatus(){
		if(DownloadService.complete)
		{Toast.makeText(getApplicationContext(),"Download Complete; Your file can be found in Storage", Toast.LENGTH_SHORT).show();
		try{
			pm.setComponentEnabledSetting(cm,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
		}catch(Exception e){}
		}else if(downloading && pause){
			Toast.makeText(getApplicationContext(),"Download will continue on network availability", Toast.LENGTH_SHORT).show();
			}
	}
		
		@Override
		protected void onResume(){
			super.onResume();
			if(DownloadService.complete)
			{	
				//of.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(),"Download Complete; Your file can be found in Storage", Toast.LENGTH_SHORT).show();
				try{
					pm.setComponentEnabledSetting(cm,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
				}catch(Exception e){}
			}else if(downloading && pause){
				Toast.makeText(getApplicationContext(),"Download will continue on network availability", Toast.LENGTH_SHORT).show();
				}	
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

}
