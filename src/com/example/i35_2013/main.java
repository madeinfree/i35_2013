package com.example.i35_2013;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class main extends Activity implements Button.OnClickListener{
	
	private Button butterflyBtn,frogBtn,mothBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findView();
        setListener();
    }
    
    private void findView() {
    	butterflyBtn=(Button)findViewById(R.id.butterflyBtn);
    	frogBtn=(Button)findViewById(R.id.frogBtn);
    	mothBtn=(Button)findViewById(R.id.mothBtn);
    }

    private void setListener() {
    	butterflyBtn.setOnClickListener(this);
    	frogBtn.setOnClickListener(this);
    	mothBtn.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		//new一個intent物件，並指定Activity切換的class
        Intent intent = new Intent();
        intent.setClass(main.this, MainActivity.class);

        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();

		if(v==butterflyBtn) {
			bundle.putInt("species", 1);
		} else if(v==frogBtn) {
			bundle.putInt("species", 2);
		} else if(v==mothBtn) {
			bundle.putInt("species", 3);
		}
		
        //將Bundle物件assign給intent
        intent.putExtras(bundle);
        
        //切換Activity
        startActivity(intent);
	}

}



