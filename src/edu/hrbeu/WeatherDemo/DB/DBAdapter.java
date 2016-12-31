package edu.hrbeu.WeatherDemo.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;
import edu.hrbeu.WeatherDemo.SMS.*;

public class DBAdapter {

	private static final String DB_NAME = "weather_app.db";
	private static final String DB_TABLE_CONFIG = "setup_config";
	private static final String DB_CONFIG_ID = "1";
	private static final int DB_VERSION = 1;

	public static final String KEY_ID = "_id";
	public static final String KEY_CITY_NAME = "city_name";
	public static final String KEY_REFRESH_SPEED = "refresh_speed";
	public static final String KEY_SMS_SERVICE = "sms_service";
	public static final String KEY_SMS_INFO = "sms_info";
	public static final String KEY_KEY_WORD = "key_word";

	private static final String DB_TABLE_SMS = "sms_data";
	public static final String KEY_SENDER = "sms_sender";
	public static final String KEY_BODY = "sms_body";
	public static final String KEY_RECEIVE_TIME = "sms_receive_time";
	public static final String KEY_RETURN_RESULT = "return_result";


	public  SQLiteDatabase db;
	private final Context context;
	private DBOpenHelper dbOpenHelper;

	public DBAdapter(Context _context) {
		context = _context;
	}

	/** Close the database */
	public void close() {
		if (db != null){
			db.close();
			db = null;
		}
	}

	/** Open the database */
	public void open() throws SQLiteException {
		dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		}
		catch (SQLiteException ex) {
			db = dbOpenHelper.getReadableDatabase();
		}
	}





	// ************** 用于 SMS **************
	public void SaveOneSms(SimpleSms sms){
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_SENDER, sms.Sender);
		newValues.put(KEY_BODY, sms.Body);
		newValues.put(KEY_RECEIVE_TIME, sms.ReceiveTime);
		newValues.put(KEY_RETURN_RESULT, sms.ReturnResult);

		db.insert(DB_TABLE_SMS, null, newValues);

	}

	public long DeleteAllSms() {
		return db.delete(DB_TABLE_SMS, null, null);
	}

	public SimpleSms[] GetAllSms() {
		Cursor results =  db.query(DB_TABLE_SMS, new String[] { KEY_ID, KEY_SENDER,
		KEY_BODY, KEY_RECEIVE_TIME, KEY_RETURN_RESULT},
		null, null, null, null, null);
		return ToSimpleSms(results);
	}


	private SimpleSms[] ToSimpleSms(Cursor cursor){
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()){
			return null;
		}
		SimpleSms[] sms = new SimpleSms[resultCounts];
		for (int i = 0 ; i<resultCounts; i++){
			sms[i] = new SimpleSms();
			sms[i].Sender = cursor.getString(cursor.getColumnIndex(KEY_SENDER));
			sms[i].Body = cursor.getString(cursor.getColumnIndex(KEY_BODY));
			sms[i].ReceiveTime = cursor.getString(cursor.getColumnIndex(KEY_RECEIVE_TIME));
			sms[i].ReturnResult = cursor.getString(cursor.getColumnIndex(KEY_RETURN_RESULT));

			cursor.moveToNext();
		}
		return sms;
	}



	// ************** 用于Config **************
	public void SaveConfig(){
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_CITY_NAME, Config.CityName);
		updateValues.put(KEY_REFRESH_SPEED, Config.RefreshSpeed);
		updateValues.put(KEY_SMS_SERVICE, Config.ProvideSmsService);
		updateValues.put(KEY_SMS_INFO, Config.SaveSmsInfo);
		updateValues.put(KEY_KEY_WORD, Config.KeyWord);

		db.update(DB_TABLE_CONFIG, updateValues,  KEY_ID + "=" + DB_CONFIG_ID, null);

		Toast.makeText(context, "系统设置保存成功", Toast.LENGTH_SHORT).show();

	}

	public void LoadConfig() {
		Cursor result =  db.query(DB_TABLE_CONFIG, new String[] { KEY_ID, KEY_CITY_NAME, KEY_REFRESH_SPEED,
		KEY_SMS_SERVICE, KEY_SMS_INFO, KEY_KEY_WORD},
		KEY_ID + "=" + DB_CONFIG_ID, null, null, null, null);
		if (result.getCount() == 0 || !result.moveToFirst()){
			return;
		}
		Config.CityName = result.getString(result.getColumnIndex(KEY_CITY_NAME));
		Config.RefreshSpeed =  result.getString(result.getColumnIndex(KEY_REFRESH_SPEED));
		Config.ProvideSmsService = result.getString(result.getColumnIndex(KEY_SMS_SERVICE));
		Config.SaveSmsInfo =  result.getString(result.getColumnIndex(KEY_SMS_INFO));
		Config.KeyWord = result.getString(result.getColumnIndex(KEY_KEY_WORD));

		Toast.makeText(context, "系统设置读取成功", Toast.LENGTH_SHORT).show();
	}


	/** 静态Helper类，用于建立、更新和打开数据库*/
	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		private static final String DB_CREATE_CONFIG = "create table " +
		DB_TABLE_CONFIG + " (" + KEY_ID + " integer primary key autoincrement, " +
		KEY_CITY_NAME+ " text not null, " + KEY_REFRESH_SPEED+ " text," +
		KEY_SMS_SERVICE +" text, " + KEY_SMS_INFO +  " text, " +
		KEY_KEY_WORD + " text);";

		private static final String DB_CREATE_SMS = "create table " +
		DB_TABLE_SMS + " (" + KEY_ID + " integer primary key autoincrement, " +
		KEY_SENDER+ " text not null, " + KEY_BODY+ " text, " +
		KEY_RECEIVE_TIME +" text, " +   KEY_RETURN_RESULT + " text);";

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DB_CREATE_CONFIG);
			_db.execSQL(DB_CREATE_SMS);

			//初始化系统配置的数据表
			Config.LoadDefaultConfig();
			ContentValues newValues = new ContentValues();
			newValues.put(KEY_CITY_NAME, Config.CityName);
			newValues.put(KEY_REFRESH_SPEED, Config.RefreshSpeed);
			newValues.put(KEY_SMS_SERVICE, Config.ProvideSmsService);
			newValues.put(KEY_SMS_INFO, Config.SaveSmsInfo);
			newValues.put(KEY_KEY_WORD, Config.KeyWord);
			_db.insert(DB_TABLE_CONFIG, null, newValues);

		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			_db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CONFIG);
			_db.execSQL("DROP TABLE IF EXISTS " + DB_CREATE_SMS);
			onCreate(_db);
		}
	}



}