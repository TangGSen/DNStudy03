package com.dongnao.lsn8_canvas_searchview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	private MySearchView mSearchView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSearchView = (MySearchView)findViewById(R.id.sv);
		mSearchView.setController(new Controller1());
		
	}
	
	public void start(View v){
		mSearchView.startAnimation();
	}
	public void reset(View v){
		mSearchView.resetAnimation();
	}

}
