package edu.hrbeu.WeatherDemo.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import edu.hrbeu.WeatherDemo.DB.Config;
import edu.hrbeu.WeatherDemo.DB.DBAdapter;
import edu.hrbeu.WeatherDemo.SMS.SimpleSms;
import edu.hrbeu.WeatherDemo.Weather.Weather;


public class WeatherService extends Service{
	
	private DBAdapter dbAdapter ;
	private Thread workThread;
	private static ArrayList<SimpleSms> smsList = new ArrayList<SimpleSms>();
	private static int timeCounter = 1;
	
	public static void RequerSMSService(SimpleSms sms){
		if (Config.ProvideSmsService.equals("true")){
			smsList.add(sms);
		}
	}
	private void SaveSmsData(SimpleSms sms){
		if (Config.SaveSmsInfo.equals("true")){
			dbAdapter.SaveOneSms(sms);
		}
	}
	
	@Override
	public void onCreate() {
	    super.onCreate();
	         
        dbAdapter = new DBAdapter(this);
	    dbAdapter.open();   
	      
	    Toast.makeText(this, "start service", Toast.LENGTH_LONG).show();
	    workThread = new Thread(null,backgroudWork,"WorkThread");
	    
	    
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
	      super.onStart(intent, startId);

	      if (!workThread.isAlive()){
	    	  workThread.start();
	      }
	}
	
	@Override
	public void onDestroy() {
	     super.onDestroy();
	     Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
	     workThread.interrupt();
	}
	 
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private Runnable backgroudWork = new Runnable(){
		@Override
		public void run() {
			try {
		
				while(!Thread.interrupted()){				

					ProcessSmsList();

					GetGoogleWeatherData();
					
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
	
	private void ProcessSmsList(){
		if (smsList.size()==0){
			return;
		}
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent mPi = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
		while(smsList.size()>0){
			SimpleSms sms = smsList.get(0);
			smsList.remove(0);
			smsManager.sendTextMessage(sms.Sender, null, Weather.GetSmsMsg(), mPi, null);
			sms.ReturnResult = Weather.GetSmsMsg();
			SaveSmsData(sms);
			
		}
	}

	private void GetGoogleWeatherData(){
		Log.i("TIMER",String.valueOf(timeCounter));
		if (timeCounter-- < 0){
			timeCounter = Integer.parseInt(Config.RefreshSpeed);
			Log.i("TIMER","NOW");
			try {
				WeatherAdapter.GetWeatherData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

}
