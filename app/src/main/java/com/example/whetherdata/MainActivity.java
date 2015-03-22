package com.example.whetherdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.whetherdata.GPSService;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract.Root;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	double lat;
	JSONObject weatherobj;
	double lon;
	ImageButton imgbtn;
	String[] result = new String[6];
	String logstr;
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;
	TextView tv5;
	  RelativeLayout root;
	  Animation fadein;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        
        imgbtn = (ImageButton) findViewById(R.id.imgbtn);
        root = (RelativeLayout)findViewById(R.id.root);
        tv1= (TextView) findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        
        imgbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				imgbtn.startAnimation(fadein);
				
				String address = "";
				GPSService mGPSService = new GPSService(getApplicationContext());
				mGPSService.getLocation();

				if (mGPSService.isLocationAvailable == false) {

					// Here you can ask the user to try again, using return; for that
					Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
					return;

					// Or you can continue without getting the location, remove the return; above and uncomment the line given below
					// address = "Location not available";
				} else {

					// Getting location co-ordinates
					lat = mGPSService.getLatitude();
					lon = mGPSService.getLongitude();
					//Toast.makeText(getApplicationContext(), "Latitude:" + lat + " | Longitude: " + lon, Toast.LENGTH_LONG).show();

					address = mGPSService.getLocationAddress();

					//tvLocation.setText("Latitude: " + latitude + " \nLongitude: " + longitude);
					//tvAddress.setText("Address: " + address);
				}
				
				
				//Toast.makeText(getApplicationContext(), "Your address is: " + address, Toast.LENGTH_SHORT).show();
				
				// make sure you close the gps after using it. Save user's battery power
				mGPSService.closeGPS();
				
				
				// Post().execute();
				
				
				// lon lat
				// TODO Auto-generated method stub
				
			
			
			new Post().execute();
			}
			
		});
        
    }
    
    public class Post extends AsyncTask<String, String[], String[]>
    {
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    	}
    	@Override
    	protected String[] doInBackground(String... arg0) {
    		// TODO Auto-generated method stub
    		String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon;
    		JSONParser json = new JSONParser();
    		JSONObject jsonobj = json.getJSONFromUrl(url);
    			Log.e("json", url);
    			JSONObject coordobj;
    			JSONObject mainobj;
				try {
					coordobj = jsonobj.getJSONObject("coord");
					Log.e("coord", coordobj.toString());
					logstr = coordobj.getString("lon");
					String lonstr = coordobj.getString("lat");
					String name = jsonobj.getString("name");
					mainobj = jsonobj.getJSONObject("main");
					
					String temp = mainobj.getString("temp");
					String pressure = mainobj.getString("pressure");
					Log.e("main",mainobj.toString());
					String humidity = mainobj.getString("humidity");
					
					JSONArray weather = jsonobj.getJSONArray("weather");
					for(int i=0;i<weather.length();i++){
					weatherobj = weather.getJSONObject(i);
					}
					String main = weatherobj.getString("main");
				    result[0]=logstr;
					result[1]=main;
					result[2]=name;
					result[3]=temp;
					result[4]=pressure;
					result[5]=humidity;
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		return result;
    	}
    	@Override
    	protected void onPostExecute(String[] result) {
    		// TODO Auto-generated method stub
    		tv1.setText("Your Location : " +result[2]);
    		tv1.setTextColor(Color.RED);;
    	String s = result[1].toString();
    		//if("Clear".equals(tv2.getText().toString()))
    			 if("Clear".equals(s))
    		{
    			
    			root.setBackgroundResource(R.drawable.clear);
    		}
    		
    		else if("Rain".equals(s))
    		{
    			root.setBackgroundResource(R.drawable.rain);
    		}
    		
    		else if("Clouds".equals(s))
    		{
    			root.setBackgroundResource(R.drawable.cloudy);
    		}
    		
    		else if("Mist".equals(s))
    		{
    			root.setBackgroundResource(R.drawable.mist);
    		}
    		else if("Haze".equals(s))
    		{
    			root.setBackgroundResource(R.drawable.haze1);
    		}
    		
    		else{
    			root.setBackgroundResource(R.drawable.logo);
    		}
    		//}
    		tv2.setText("Weather type :  "+result[1]);
    	
    		tv3.setText("Temperature :  "+result[3]);
    		tv4.setText("Pressure :  "+result[4]);
    		tv5.setText("Humidity :  "+result[5]);
    		
    		super.onPostExecute(result);
    	
    	}
    	
    }
    
    }
    

    
    
    
  
    


