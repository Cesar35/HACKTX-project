package com.estumble.ui;

import java.util.ArrayList;

import com.huguesjohnson.ebayDemo.Category;
import com.huguesjohnson.ebayDemo.EbayParser;
import com.huguesjohnson.ebayDemo.R;
import com.huguesjohnson.ebayDemo.ebaycategories;
import com.huguesjohnson.ebayDemo.R.layout;
import com.huguesjohnson.ebayDemo.ebayDemoActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Categorylist extends ListActivity {
	
	private ListView view;
	private ArrayAdapter<String> adapter;
	private ArrayList<Category> categories;
	private ArrayList<String> catnames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ebaycategories temp = new ebaycategories(this.getApplicationContext());
		try {
			EbayParser parser = new EbayParser(this.getApplicationContext());
			//Log.d("JJJJJJJ", temp.getCategories());
			categories = parser.parseCategories(temp.getCategories());
		} catch (Exception e) {
			Log.d("HELLL", "djkfjlsdkfjslkdjf");
			e.printStackTrace();
		}
		
		
		view = getListView();
		setAdapter();
		
		view.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	Intent i = new Intent(Categorylist.this, ebayDemoActivity.class);
            	startActivity(i);
              
            }
        });
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.categorylist, menu);
//		return true;
//	}
	
	private void setAdapter() {
		catnames = new ArrayList<String>();
		for(int i = 1; i < categories.size(); ++i){
			String tttt = categories.get(i).CategoryName;
			catnames.add(tttt);
		}
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, catnames);
        setListAdapter(adapter);
    }
	
}
