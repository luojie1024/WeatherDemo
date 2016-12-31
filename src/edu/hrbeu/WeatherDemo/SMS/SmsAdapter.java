package edu.hrbeu.WeatherDemo.SMS;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.hrbeu.WeatherDemo.DB.DBAdapter;
import edu.hrbeu.WeatherDemo.R;


public class SmsAdapter extends BaseAdapter{
	  private LayoutInflater mInflater;	
	  private static DBAdapter dbAdapter ;
	  private static SimpleSms[] smsList ; 
		       		          
	  public SmsAdapter(Context context)
	  {

	    mInflater = LayoutInflater.from(context);
	    dbAdapter = new DBAdapter(context);
	    dbAdapter.open(); 
	    smsList = dbAdapter.GetAllSms();    
	  }
	  
	  public static void RefreshData(){
		  smsList = dbAdapter.GetAllSms();   
	  }

	@Override
	public int getCount() {
		if (smsList == null)
			return 0;
		else
			return smsList.length;
	}

	@Override
	public Object getItem(int position) {
		if (smsList == null)
			return 0;
		else
			return smsList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
	    
	    if(convertView == null){
	      convertView = mInflater.inflate(R.layout.data_row, null);
	      holder = new ViewHolder();
	      holder.textRow01 = (TextView) convertView.findViewById(R.id.data_row_01);
	      holder.textRow02 = (TextView) convertView.findViewById(R.id.data_row_02);
	      
	      convertView.setTag(holder);
	    }
	    else{
	      holder = (ViewHolder) convertView.getTag();
	    }

		
		if (smsList != null){
			String row01Msg ="("+position+")" +" 发送者"+ smsList[position].Sender+"，"+smsList[position].ReceiveTime;
			holder.textRow01.setText(row01Msg);
			holder.textRow02.setText(smsList[position].ReturnResult);
		}
	    return convertView;
	}

	private class ViewHolder{
	    TextView textRow01;
	    TextView textRow02;
	  }
}
