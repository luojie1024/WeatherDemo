package edu.hrbeu.WeatherDemo.DB;

public class Config {

	public static String CityName;
	public static String RefreshSpeed;
	public static String ProvideSmsService;
	public static String SaveSmsInfo;
	public static String KeyWord;
	
	public static void LoadDefaultConfig(){
		CityName = "changde";
		RefreshSpeed = "60";
		ProvideSmsService = "true";
		SaveSmsInfo = "true";
		KeyWord = "changde";
	}
	
}
