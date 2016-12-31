package edu.hrbeu.WeatherDemo.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import edu.hrbeu.WeatherDemo.DB.Config;
import edu.hrbeu.WeatherDemo.SMS.SimpleSms;


public class SmsReceiver extends BroadcastReceiver{	

	private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	  
  @Override
  public void onReceive(Context context, Intent intent){    
    if (intent.getAction().equals(SMS_ACTION)){
      Bundle bundle = intent.getExtras();
      
      if (bundle != null){
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];
        for (int i = 0; i<objs.length; i++){ 
          messages[i] = 
          SmsMessage.createFromPdu((byte[]) objs[i]);
        }
      
        String smsBody = messages[0].getDisplayMessageBody();
        String smsSender =  messages[0].getDisplayOriginatingAddress();

        if (smsBody.contains(Config.KeyWord)&&Config.ProvideSmsService.equals("true")){
        	SimpleSms simpleSms = new SimpleSms(smsSender, smsBody);
        	WeatherService.RequerSMSService(simpleSms);
        	Toast.makeText(context, "接收短信", Toast.LENGTH_SHORT).show();
        }
      }  
    }
  }
}