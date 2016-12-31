package edu.hrbeu.WeatherDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import edu.hrbeu.WeatherDemo.DB.Config;
import edu.hrbeu.WeatherDemo.DB.DBAdapter;

public class SetupActivity extends Activity{
	final static int MENU_RESTORE = Menu.FIRST;
	final static int MENU_QUIT = Menu.FIRST+1;
	
	private EditText cityNameView;
	private EditText refreshSpeedView;
	private CheckBox smsServiceView;
	private CheckBox saveSmsInfoView;
	private EditText keyWorkView;
	
	public static DBAdapter dbAdapter ;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tab_setup);
	        
			cityNameView = (EditText)findViewById(R.id.tab_setup_city_name);
			refreshSpeedView = (EditText)findViewById(R.id.tab_setup_refresh_speed);
			smsServiceView = (CheckBox)findViewById(R.id.tab_setup_sms_service);
			saveSmsInfoView = (CheckBox)findViewById(R.id.tab_setup_save_sms_info);
			keyWorkView = (EditText)findViewById(R.id.tab_setup_key_work);			 

			dbAdapter = new DBAdapter(this);
		    dbAdapter.open();    
		    UpdateUI();
		    
	        Button applyBtn = (Button)findViewById(R.id.tab_setup_apply);
	        applyBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SaveConfig();			
				}
			});
	        Button cancelBtn = (Button)findViewById(R.id.tab_setup_cancel);
	        cancelBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dbAdapter.LoadConfig();
					UpdateUI();
				}       	
	        }
	        );
	        
	       
	 }
	 
	   @Override
	 public boolean onCreateOptionsMenu(Menu menu){
		 menu.add(0,MENU_RESTORE ,0,"ª÷∏¥≥ı º…Ë÷√");
 		 menu.add(0,MENU_QUIT,1,"quit");
 		 return true;
	 }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item){
		 switch(item.getItemId()){
		 	case MENU_RESTORE:
		 		RestoreDefaultSetup();
	    		return true;	 
	    	case MENU_QUIT:
	    		finish();
	    		break;
	    	}	
	    	return false;
	  } 
	 
	 private void RestoreDefaultSetup(){
		 Config.LoadDefaultConfig();
		 UpdateUI();
		 dbAdapter.SaveConfig();
	 }
	 
	 private void UpdateUI(){
		 cityNameView.setText(Config.CityName);
		 refreshSpeedView.setText(Config.RefreshSpeed);		 
		 smsServiceView.setChecked(Config.ProvideSmsService.equals("true")?true:false);
		 saveSmsInfoView.setChecked(Config.SaveSmsInfo.equals("true")?true:false);
		 keyWorkView.setText(Config.KeyWord);
	 }
	 
	 private void SaveConfig(){
		Config.CityName = cityNameView.getText().toString().trim();
		Config.RefreshSpeed = refreshSpeedView.getText().toString();
		if (smsServiceView.isChecked()){
			Config.ProvideSmsService = "true";
		}
		else{
			Config.ProvideSmsService = "false";
		}
		if (saveSmsInfoView.isChecked()){
			Config.SaveSmsInfo = "true";
		}
		else{
			Config.SaveSmsInfo = "false";
		}
		Config.KeyWord = keyWorkView.getText().toString().trim();

		dbAdapter.SaveConfig();	
	 }
}
