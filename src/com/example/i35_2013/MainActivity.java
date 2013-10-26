package com.example.i35_2013;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("JavascriptInterface")
public class MainActivity extends Activity implements LocationListener {
	private static final String TAG = "i35_2013";
	private WebView webview;
	private String[][] resultrow;
	private StringBuilder theText = new StringBuilder();
	private String path;
	private long exitTime = 0;
	private double lng=0,lat=0;
	private LocationManager lms;
	private String choiceProvider;
	private boolean getService = false;
	private Thread BackgroundThread;
	private boolean onThreadStatus = false;
	private boolean onSearch = false;
	private int species;
	/*********************************************************************
	
	//Handler switch flag
	protected static final int Result_Data = 0x1;
	//Handler 接收Thread訊息
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case Result_Data:
					String result = null;
					if(msg.obj instanceof String) {
						result = (String) msg.obj;
					}
					if(result != null) {
						int start,row;
						//先將xml最上方的註解移除
						start=result.indexOf("<rows>");
						result=result.substring(start);
						//共有幾筆資料
						row = NumberOfKeywords(result,"<row>");
						Log.i(TAG,"row:"+String.valueOf(row));
						String[] strkey = {
								"near",
								"rid",
								"collcet_time",
								"lon",
								"lat",
								"Collector_id",
								"Collector",
								"ez_id",
								"ez_name",
								"fam",
								"genus_infra"
								};
						resultrow = new String[row][strkey.length];
						String strtmp=""; //暫存每筆資料字串
						int row_start = 0,row_end = 0;
						//theText.delete(0, theText.length());
						theText.append("---------START----------\n");
						for(int i=0;i<row;i++){
							if(i==0){
								row_start = result.indexOf("<row>");
								row_end = result.indexOf("</row>");
							} else {
								row_start+=5; row_end+=6;
								row_start = result.indexOf("<row>",row_start);
								row_end = result.indexOf("</row>",row_end);
							}
							strtmp = result.substring(row_start,row_end+6);
							int p_start,p_end;
							for(int j=0;j<strkey.length;j++){
								p_start = strtmp.indexOf("<"+strkey[j]+">");
								p_end = strtmp.indexOf("</"+strkey[j]+">");
								resultrow[i][j] = strtmp.substring(p_start+strkey[j].length()+2,p_end);
							}
							AddResult(i);
						}
						theText.append("\n---------END----------");
						tv.setText(theText);
						onThreadStatus = false;
					}
				break;
			}
		}
	};
	
	//取得String中某個字串出現的次數
	public static int NumberOfKeywords(String strKeywords, String strkey){
		int index1,index2; //最前面最後面
		index1=strKeywords.indexOf(strkey);
		index2=strKeywords.lastIndexOf(strkey);
		int spcount=0; //次數
		if(index1 >= 0){
			while(index1 <= index2){
				index1 += strkey.length();
				index1 = strKeywords.indexOf(strkey,index1);
				spcount++;
				if(index1 == -1){
					break;
				}
			}
		}
		return spcount;
	}
	
	private void AddResult(int theRow) {
		theText.append("公尺距離: "+resultrow[theRow][0]+"\n");
		theText.append("採集編號: "+resultrow[theRow][1]+"\n");
		theText.append("採集時間: "+resultrow[theRow][2]+"\n");
		theText.append("經度: "+resultrow[theRow][3]+"\n");
		theText.append("緯度: "+resultrow[theRow][4]+"\n");
		theText.append("採集人編號: "+resultrow[theRow][5]+"\n");
		theText.append("採集人名: "+resultrow[theRow][6]+"\n");
		theText.append("簡易分類編號: "+resultrow[theRow][7]+"\n");
		theText.append("簡易分類名稱: "+resultrow[theRow][8]+"\n");
		theText.append("中文科名: "+resultrow[theRow][9]+"\n");
		theText.append("中文學名: "+resultrow[theRow][10]+"\n\n");
	}
	
	
	// Get方式請求  
	public void requestByGet() throws Throwable {
		try {
		    // 新建一個URL對象
			//path = "http://www.i35.club.tw/xml/near.php?lon="+lng+"&lat="+lat+"&l=119.167800&r=122.047400&u=25.336300&d=21.866000&ez=15&lm=800&cnt=50&page=1";
			path = "http://www.i35.club.tw/xml/near.php?lon=120.80148&lat=23.828677&l=119.167800&r=122.047400&u=25.336300&d=21.866000&ez=15&lm=800&cnt=50&page=1";
		    URL url = new URL(path);
		    Log.i(TAG,path);
		    
		    // 打開一個HttpURLConnection連接
		    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
		    
		    //
		    int xxx = httpConn.getResponseCode();
		    Log.i(MainActivity.TAG, String.valueOf(xxx));
		    
		    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		    	InputStream  input = new BufferedInputStream(httpConn.getInputStream());
		    	handler.obtainMessage(Result_Data,readStream(input)).sendToTarget();
		    	input.close();
		    	//disconnect
				httpConn.disconnect();
            }
		    
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private String readStream(InputStream is) {
	    try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i = is.read();
			while(i != -1) {
				bo.write(i);
				i = is.read();
			}
			return bo.toString();
	    } catch (IOException e) {
	    	return "";
	    }
	}
	
	*********************************************************************/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		
		Bundle bundle = this.getIntent().getExtras();
		species = bundle.getInt("species");
		Log.i(TAG,"species"+species);
		 
		SetView();
		lms = (LocationManager) getSystemService(LOCATION_SERVICE);	//取得系統定位服務
		
		/***********************************************
		BackgroundThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					requestByGet();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		************************************************/
	}
	
	private void SetView() {
		webview = (WebView)findViewById(R.id.webview);
		WebViewSet();
	}

	private void WebViewSet() {
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true); //啟用JavaScript執行功能
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.addJavascriptInterface(new webobj(),"inwcall");
		webview.loadUrl("file:///android_asset/butterfly.html");
	}
	
	class webobj {
		//exit app
		public void ExitApp() {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
	//處理webview back與app 的問題
	public boolean onKeyDown(int KeyCode,KeyEvent event){
		if(KeyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if(webview.canGoBack()){
				webview.goBack();
			} else {
				if((System.currentTimeMillis()-exitTime) > 2000) {
			        Toast.makeText(getApplicationContext(), "再按一次退出程式", Toast.LENGTH_SHORT).show();
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
		if(lms.isProviderEnabled(LocationManager.GPS_PROVIDER))//有GPS就設定以gps來定位
			choiceProvider=LocationManager.GPS_PROVIDER;
		else if(lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))//如果沒gps有網路就以網路來定位
			choiceProvider=LocationManager.NETWORK_PROVIDER;
		Location lo = lms.getLastKnownLocation(choiceProvider);
		getLocation(lo);
	}
	
	private void getLocation(Location location) {	//將定位資訊顯示在畫面中
		if(location != null) {
			lng = location.getLongitude();	//取得經度
			lat = location.getLatitude();	//取得緯度
			Log.i(TAG,"經度:"+lng+"緯度:"+lat);
			webview.loadUrl("javascript:Android_to_JS_Latlng('23.945154695027593','120.98384857177734',"+species+")");
			
			/*******************************
			if(!onThreadStatus){
				onThreadStatus = true;
				BackgroundThread.start();
			}
			*******************************/
		} else {
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER) || lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
  			//如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
  			getService = true;	//確認開啟定位服務
  			locationServiceInitial();
  		} else {
  			Toast.makeText(MainActivity.this, "請開啟定位服務", Toast.LENGTH_LONG).show();
  			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
  		    builder.setTitle("警告訊息");
  			builder.setMessage("您尚未開啟定位服務，要前往設定嗎？");
  			builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
  		        @Override
  		        public void onClick(DialogInterface dialog, int which) {
  		        	startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));	//開啟設定頁面
  		        }
  		    });
  		    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
  		        @Override
  		        public void onClick(DialogInterface dialog, int which) {
  		           Toast.makeText(getApplicationContext(),"定位服務尚未開啟...", Toast.LENGTH_SHORT).show();
  		        }
  		    });
  		    AlertDialog alert = builder.create();
  		    alert.show();
  			
  		}
		
		if(getService) {
			//服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
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
