package com.haowu.safewalk;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SafewalkApplication extends Activity {
	
	//message setting
	public final static String EXTRA_FROM = "com.haowu.safewalk.FROM";
	public final static String EXTRA_TO = "com.haowu.safewalk.TO";
	
	private static SafewalkApplication instance;
	EditText etFrom;
	EditText etTo;
	Button bSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//set action bar stuff
		ActionBar ab = getActionBar(); 
		ab.setDisplayShowTitleEnabled(false); 
		ab.setDisplayShowHomeEnabled(false);
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C1D4F1")));
		
		
		setContentView(R.layout.safewalk_home);
		
		//initialize all vars
		etFrom = (EditText) findViewById(R.id.et_from);
		etTo = (EditText) findViewById(R.id.et_to);
		bSubmit = (Button) findViewById(R.id.b_submit);
		//set edittexts to end
		etFrom.setSelection(etFrom.getText().length());
		etTo.setSelection(etTo.getText().length());
		
		//onclick listeners
		bSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String from = etFrom.getText().toString();
				String to = etTo.getText().toString();
				Intent intent = new Intent(instance, SafewalkMap.class);
				intent.putExtra(EXTRA_FROM, from);
				intent.putExtra(EXTRA_TO, to);
				startActivity(intent);
			}
			
		});
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
