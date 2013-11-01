package com.example.i35_2013;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("JavascriptInterface")
public class MainActivity extends Activity implements LocationListener {
	private static final String TAG = "i35_2013";
	private WebView webview;
	private long exitTime = 0;
	private double lng=0,lat=0;
	private LocationManager lms;
	private String choiceProvider;
	private boolean getService = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		SetView();
		lms = (LocationManager) getSystemService(LOCATION_SERVICE);	//���o�t�Ωw��A��
		
	}
	
	private void SetView() {
		webview = (WebView)findViewById(R.id.webview);
		WebViewSet();
	}

	private void WebViewSet() {
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true); //�ҥ�JavaScript����\��
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.addJavascriptInterface(new webobj(),"inwcall");
		webview.loadUrl("file:///android_asset/index.html");
	}
	
	class webobj {
		//exit app
		public void ExitApp() {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		//�o��g��
		public double GetLocationLng() {
			return lng;
		}
		//�o��n��
		public double GetLocationLat() {
			return lat;
		}
	}
	
	//�B�zwebview back�Papp �����D
	public boolean onKeyDown(int KeyCode,KeyEvent event){
		if(KeyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if(webview.canGoBack()){
				webview.goBack();
			} else {
				if((System.currentTimeMillis()-exitTime) > 2000) {
			        Toast.makeText(getApplicationContext(), "�A���@���h�X�{��", Toast.LENGTH_SHORT).show();
			        exitTime = System.currentTimeMillis();  
			    } else {
			        finish();  
			        System.exit(0);  
			    }
			}
			return true;
		}
		return super.onKeyDown(KeyCode,event);
	}
	
	private void locationServiceInitial() {
		if(lms.isProviderEnabled(LocationManager.GPS_PROVIDER))//��GPS�N�]�w�Hgps�өw��
			choiceProvider=LocationManager.GPS_PROVIDER;
		else if(lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))//�p�G�Sgps�������N�H�����өw��
			choiceProvider=LocationManager.NETWORK_PROVIDER;
		Location lo = lms.getLastKnownLocation(choiceProvider);
		getLocation(lo);
	}
	
	private void getLocation(Location location) {	//�N�w���T��ܦb�e����
		if(location != null) {
			lat= 23.945154695027593;
			lng= 120.98384857177734;
			//lng = location.getLongitude();	//���o�g��
			//lat = location.getLatitude();	//���o�n��
			Log.i(TAG,"�g��:"+lng+"�n��:"+lat);
		} else {
			Toast.makeText(this, "�L�k�w��y��", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) || lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
  			//�p�GGPS�κ����w��}�ҡA�I�slocationServiceInitial()��s��m
  			getService = true;	//�T�{�}�ҩw��A��
  			locationServiceInitial();
  		} else {
  			Toast.makeText(MainActivity.this, "�ж}�ҩw��A��", Toast.LENGTH_LONG).show();
  			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
  		    builder.setTitle("ĵ�i�T��");
  			builder.setMessage("�z�|���}�ҩw��A�ȡA�n�e���]�w�ܡH");
  			builder.setPositiveButton("�O", new DialogInterface.OnClickListener() {
  		        @Override
  		        public void onClick(DialogInterface dialog, int which) {
  		        	startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));	//�}�ҳ]�w����
  		        }
  		    });
  		    builder.setNegativeButton("�_", new DialogInterface.OnClickListener() {
  		        @Override
  		        public void onClick(DialogInterface dialog, int which) {
  		           Toast.makeText(getApplicationContext(),"�w��A�ȩ|���}��...", Toast.LENGTH_SHORT).show();
  		        }
  		    });
  		    AlertDialog alert = builder.create();
  		    alert.show();
  			
  		}
		
		if(getService) {
			//�A�ȴ��Ѫ̡B��s�W�v60000�@��=1�����B�̵u�Z���B�a�I���ܮɩI�s����
			lms.requestLocationUpdates(choiceProvider, 1000, 1, this);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		lms.removeUpdates(MainActivity.this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		getLocation(location);
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
