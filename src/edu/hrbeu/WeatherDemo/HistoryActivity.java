package edu.hrbeu.WeatherDemo;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import edu.hrbeu.WeatherDemo.DB.DBAdapter;
import edu.hrbeu.WeatherDemo.SMS.*;

public class HistoryActivity extends ListActivity{
	final static int MENU_REFRESH = Menu.FIRST;
	final static int MENU_DELETE = Menu.FIRST+1;
	final static int MENU_QUIT = Menu.FIRST+2;
	
	private DBAdapter dbAdapter ;
	private SmsAdapter dataAdapter;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tab_history);
	        
	        dbAdapter = new DBAdapter(this);
		    dbAdapter.open();   
		    
		    dataAdapter = new SmsAdapter(this);
	        setListAdapter(dataAdapter);
	 }
	 
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu){
		 menu.add(0,MENU_REFRESH ,0,"refresh");
 		 menu.add(0,MENU_DELETE,1,"delete");
 		 menu.add(0,MENU_QUIT,1,"setting");
 		 return true;
	 }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item){
		 switch(item.getItemId()){
		 	case MENU_REFRESH:
		 		SmsAdapter.RefreshData();
				setListAdapter(dataAdapter);
	    		return true;
	    	case MENU_DELETE:
	    		dbAdapter.DeleteAllSms();
	    		return true;  
	    	case MENU_QUIT:
	    		finish();
	    		break;
	    	}	
	    	return false;
	  } 
}
