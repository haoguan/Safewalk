package com.haowu.safewalk;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.RenderPriority;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SafewalkMap extends Activity {
	
	WebView wv;
	MapJS mapJS;
	LinearLayout llRoutes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//set action bar stuff
		ActionBar ab = getActionBar(); 
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		ab.setCustomView(R.layout.actionbar);
		
		//receive info
		Intent intent = getIntent();
		String fromAddress = intent.getStringExtra(SafewalkApplication.EXTRA_FROM);
		String toAddress = intent.getStringExtra(SafewalkApplication.EXTRA_TO);
		
		setContentView(R.layout.safewalk_map);
		
		//init javascript interface class
		mapJS = new MapJS(getApplication().getApplicationContext());
		mapJS.setFromAddress(fromAddress);
		mapJS.setToAddress(toAddress);
		
		//link android elements
		wv = (WebView) findViewById(R.id.wv_map);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebChromeClient(new WebChromeClient());
		wv.addJavascriptInterface(mapJS, "Safewalk");
		wv.getSettings().setRenderPriority(RenderPriority.HIGH);
		wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wv.getSettings().setAllowFileAccess(true);
		wv.setBackgroundColor(0x00000000); //must set background to zero for background to show through.
		wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // solves a flickering issue of webview content.
		wv.loadUrl("file:///android_asset/index.html");
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		//get relative layout
		llRoutes = (LinearLayout) findViewById(R.id.ll_routes);
		
		
//		TextView testFrom = (TextView) findViewById(R.id.test_from);
//		TextView testTo = (TextView) findViewById(R.id.test_to);
		
//		testFrom.setText(fromAddress);
//		testTo.setText(toAddress);
		
	}
	
	//class to bridge HTML and Android for link passing from featured to app canvas.
	public class MapJS {
		
		Context mContext;
		String fromAddress;
		String toAddress;
		ArrayList<Route> routes = new ArrayList<Route>();
		
		/** Instantiate the interface and set the context */
		public MapJS(Context c) {
			mContext = c;
		}
		
		public void setFromAddress(String from) {
			fromAddress = from;
		}
		
		public void setToAddress(String to) {
			toAddress = to;
		}
		
		@JavascriptInterface
		public String getStartAddress() {
			return fromAddress;
		}
		
		@JavascriptInterface
		public String getEndAddress() {
			return toAddress;
		}
		
		@JavascriptInterface
		public void retrieveRouteInfo(String routeNum, String via, String crimeTotal, String mins) {
			int routeNumber = Integer.parseInt(routeNum);
			if (routeNumber == 0) {
				routes.clear();
			}
			Route route = new Route(routeNumber, via, crimeTotal, mins);
			routes.add(route);
			
			RouteRunnable r = new RouteRunnable(route);
			runOnUiThread(r);
			
		}
		
		
		public class RouteRunnable implements Runnable {
			
			Route route;
			
			public RouteRunnable(Route r) {
				route = r;
			}
			
			@Override
			public void run() {
				
				RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout rl = new RelativeLayout(mContext);
				
				rl.setPadding(0, 15, 0, 15);
				
				RelativeLayout.LayoutParams rlpVia = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams rlpCrimes = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				RelativeLayout.LayoutParams rlpMins = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				
				TextView tvVia = new TextView(mContext);
				tvVia.setId(1000);
				tvVia.setText(route.getVia());
				tvVia.setTextColor(Color.parseColor("#15449B"));
				
				// add rules for rlpVia
				TextView tvRouteTitle = (TextView) findViewById(R.id.tv_route_title);
				rlpVia.addRule(RelativeLayout.ALIGN_LEFT, tvRouteTitle.getId());
				rlpVia.addRule(RelativeLayout.BELOW, tvRouteTitle.getId());
				
				tvVia.setLayoutParams(rlpVia);
				rl.addView(tvVia);
				
				
				TextView tvCrimes = new TextView(mContext);
				tvCrimes.setId(2000);
				tvCrimes.setText(route.getCrimeTotal());
				tvCrimes.setTextColor(Color.parseColor("#15449B"));
				
				//add rules for rlpCrimes
				rlpCrimes.addRule(RelativeLayout.ALIGN_BOTTOM, tvVia.getId());
				rlpCrimes.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//				rlpCrimes.addRule(RelativeLayout.BELOW, vEmpty.getId());
//				rlpCrimes.addRule(RelativeLayout.ALIGN_LEFT, vEmpty.getId());
				
				tvCrimes.setLayoutParams(rlpCrimes);
				rl.addView(tvCrimes);
				
				TextView tvMins = new TextView(mContext);
				tvMins.setId(3000);
				tvMins.setText(route.getMins());
				tvMins.setTextColor(Color.parseColor("#15449B"));
				
				//add rules for rlpMins
				rlpMins.addRule(RelativeLayout.BELOW, tvCrimes.getId());
//				rlpMins.addRule(RelativeLayout.ALIGN_LEFT, tvCrimes.getId());
				rlpMins.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//				rlpMins.addRule(RelativeLayout.BELOW, vEmpty.getId());
				
				tvMins.setLayoutParams(rlpMins);
				rl.addView(tvMins);
				
				rl.setId(route.getRouteNum()); //normalize id to start at 0
				
				if (routes.size() == 1) {
					RelativeLayout rlTitle = (RelativeLayout) findViewById(R.id.rl_routes_title);
					rlparams.addRule(RelativeLayout.BELOW, rlTitle.getId());
				}
				if (routes.size() != 1) {
					rlparams.addRule(RelativeLayout.BELOW, routes.size() - 1);
				}
				rlparams.setMargins(0, 5, 30, 0);			
				
				
				//set onclick listeners
				rl.setOnClickListener(new MyClickListener(rl.getId()));
				
				llRoutes.addView(rl, rlparams);
			}
		}
		
		public class MyClickListener implements OnClickListener {
			
			int id;
			
			public MyClickListener(int id) {
				this.id = id;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("-----------------------------------" + id);
				wv.loadUrl("javascript:chooseRoute(" + id + ")");
			}
			
		}
		
		public class Route {
			
			int routeNum;

			String via;
			String crimeTotal;
			String mins;
			
			public Route() {}
			
			public Route(int routeNum, String via, String crimeTotal, String mins) {
				this.routeNum = routeNum;
				this.via = via;
				this.crimeTotal = crimeTotal;
				this.mins = mins;
			}
			
			public int getRouteNum() {
				return routeNum;
			}

			public void setRouteNum(int routeNum) {
				this.routeNum = routeNum;
			}

			public String getVia() {
				return via;
			}

			public void setVia(String via) {
				this.via = via;
			}

			public String getCrimeTotal() {
				return crimeTotal;
			}

			public void setCrimeTotal(String crimeTotal) {
				this.crimeTotal = crimeTotal;
			}

			public String getMins() {
				return mins;
			}

			public void setMins(String mins) {
				this.mins = mins;
			}
		}
		
	}

}
