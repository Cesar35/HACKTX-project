/*
ebayDemo - Demo of how to call the eBay API and display the results on an Android device
Copyright (C) 2010 Hugues Johnson

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software 
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package com.huguesjohnson.ebayDemo;

import java.util.ArrayList;

import com.estumble.backend.Product;

import android.R.color;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ebayDemoActivity extends ListActivity{
	private final static String TAG="ebayDemoActivity";
	private static ebaylistings ebayInvoke;
	private static EbayParser ebayParser;
	private static ProgressDialog progressDialog;
	private String searchTerm="phantasy+star+3"; //intial value for demo
	private String categoryTerm;
	private SearchResult listings;
	private ListingArrayAdapter adapter;
	private Listing selectedListing;
	private int selectedPosition;
	//listing detail dialog
	private AlertDialog listingDetailDialog;
	private ImageView imageViewImage;
	private TextView textViewStartTime;
	private TextView textViewEndTime;
	private TextView textViewListingType;
	private TextView textViewPrice;
	private TextView textViewShipping;
	private TextView textViewLocation;
	private TextView textViewLink;
	private Button likeButton;
	private Button hateButton;
	private View theLayout;
	//filter dialog
	private AlertDialog keywordDialog;
	private EditText keywordTextbox;
	//menu constants
	private final static int MENU_CATEGORY=0;
	private final static int MENU_QUIT=1;

	//basic activity stuff

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		try{
			ListView listView=this.getListView();
			listView.setTextFilterEnabled(true);
			listView.setOnItemClickListener(selectItemListener);
		}catch(Exception x){
			Log.e(TAG,"onCreate",x);
			this.showErrorDialog(x);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		try{
			menu.add(0,MENU_CATEGORY,0,"Select Category");
			menu.add(0,MENU_QUIT,1,"Quit");
			return(true);
		}catch(Exception x){
			Log.e(TAG,"onCreateOptionsMenu",x);
			return(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		try{
			switch(item.getItemId()){
			case MENU_CATEGORY:{this.showCategoryList();return(true);}  //Show app categories!
			case MENU_QUIT:{this.finish();return(true);}
			default:{return(false);}
			}
		}catch(Exception x){
			Log.e(TAG,"onOptionsItemSelected",x);
			return(false);
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if((this.listingDetailDialog!=null)&&(this.listingDetailDialog.isShowing())){
			return;
		}
		this.refreshListings();
	}

	//dialog handlers
	
	OnClickListener onKeywordDialogCancelListener=new OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog,int which){/*not implemented*/}
	};

	private void showCategoryList(){
		try{
			//create the dialog
			if(this.keywordDialog==null){
				LayoutInflater inflater=(LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout=inflater.inflate(R.layout.searchdialog,(ViewGroup)findViewById(R.id.searchdialog_root));
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setView(layout);
				builder.setTitle("Search Keyword");
				builder.setPositiveButton("OK",onKeywordDialogPositiveListener);
				builder.setNegativeButton("Cancel",onKeywordDialogCancelListener);
				this.keywordTextbox=(EditText)layout.findViewById(R.id.searchdialog_keyword);
				this.keywordDialog=builder.create();
			}
			//show the dialog
			this.keywordDialog.show();
		}catch(Exception x){
			Log.e(TAG,"showFilterDialog",x);
		}
	}

	
	OnClickListener onListingDetailDialogCloseListener=new OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog,int which){
			LayoutInflater inflater=(LayoutInflater)ebayDemoActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout=theLayout;
			Button q = (Button)layout.findViewById(R.id.listingdetail_like);
			q.getBackground().setColorFilter(null);
			q = (Button)layout.findViewById(R.id.listingdetail_dislike);
			q.getBackground().setColorFilter(null);
		} 
	};

	
	OnClickListener onKeywordDialogPositiveListener=new OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog,int which){
			String newSearchTerm=keywordTextbox.getText().toString().replace(" ","+");
			if(!newSearchTerm.equals(searchTerm)){
				searchTerm=newSearchTerm;
				refreshListings();
			}
		}
	};

	private void showListingDetailDialog(){
		try{
			//I don't think this can actually happen so this is just a sanity check
			if(this.selectedListing==null){return;}
			//create the listing detail dialog
			if(this.listingDetailDialog==null){
				LayoutInflater inflater=(LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				theLayout=inflater.inflate(R.layout.listingdetail,(ViewGroup)findViewById(R.id.listingdetaildialog_root));
				View layout = theLayout;
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setView(layout);
				builder.setTitle(this.selectedListing.getTitle());
				builder.setPositiveButton("Close",onListingDetailDialogCloseListener);
				this.imageViewImage=(ImageView)layout.findViewById(R.id.listingdetail_image);
				this.textViewStartTime=(TextView)layout.findViewById(R.id.listingdetail_starttime);
				this.textViewEndTime=(TextView)layout.findViewById(R.id.listingdetail_endtime);
				this.textViewListingType=(TextView)layout.findViewById(R.id.listingdetail_listingtype);
				this.textViewPrice=(TextView)layout.findViewById(R.id.listingdetail_price);
				this.textViewShipping=(TextView)layout.findViewById(R.id.listingdetail_shipping);
				this.textViewLocation=(TextView)layout.findViewById(R.id.listingdetail_location);
				this.textViewLink=(TextView)layout.findViewById(R.id.listingdetail_link);
				this.listingDetailDialog=builder.create();
			}
			//set the values
			this.textViewStartTime.setText(Html.fromHtml("<b>Start Time:</b>&nbsp;&nbsp;"+this.selectedListing.getStartTime().toLocaleString()));
			this.textViewEndTime.setText(Html.fromHtml("<b>End Time:</b>&nbsp;&nbsp;"+this.selectedListing.getEndTime().toLocaleString()));
			this.textViewPrice.setText(Html.fromHtml("<b>Price:</b>&nbsp;&nbsp;"+this.selectedListing.getCurrentPrice()));
			this.textViewShipping.setText(Html.fromHtml("<b>Shipping Cost:</b>&nbsp;&nbsp;"+this.selectedListing.getShippingCost()));
			this.textViewLocation.setText(Html.fromHtml("<b>Location</b>&nbsp;&nbsp;"+this.selectedListing.getLocation()));
			String listingType=new String("<b>Listing Type:</b>&nbsp;&nbsp;");
			if(this.selectedListing.isAuction()){
				listingType=listingType+"Auction";
				if(this.selectedListing.isBuyItNow()){
					listingType=listingType+", "+"Buy it now";
				}
			}else if(this.selectedListing.isBuyItNow()){
				listingType=listingType+"Buy it now";
			}else{
				listingType=listingType+"Not specified";
			}
			//url field
			this.textViewListingType.setText(Html.fromHtml(listingType));
			StringBuffer html=new StringBuffer("<a href='");
			html.append(this.selectedListing.getListingUrl());
			html.append("'>");
			html.append("View original listing on ");
			html.append(this.selectedListing.getAuctionSource());
			html.append("</a>");
			this.textViewLink.setText(Html.fromHtml(html.toString()));
			this.textViewLink.setOnClickListener(urlClickedListener);
			//set the image
			this.imageViewImage.setImageDrawable(this.adapter.getImage(this.selectedPosition));
			//show the dialog
			this.listingDetailDialog.setTitle(this.selectedListing.getTitle());
			this.listingDetailDialog.show();
		}catch(Exception x){
			if((this.listingDetailDialog!=null)&&(this.listingDetailDialog.isShowing())){
				this.listingDetailDialog.dismiss();
			}
			Log.e(TAG,"showListingDetailDialog",x);
		}
	}
	
	private void showErrorDialog(Exception x){
		try{
	        new AlertDialog.Builder(this)
	   		.setTitle(R.string.app_name)
	   		.setMessage(x.getMessage())
	   		.setPositiveButton("Close", null)
	   		.show();	
		}catch(Exception reallyBadTimes){
			Log.e(TAG,"showErrorDialog",reallyBadTimes);
		} 
	}  
	
	//handle user clicks on listitems and URLs
	
	OnItemClickListener selectItemListener=new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> parent,View view,int position,long id){
			try{
				selectedPosition=position;
				selectedListing=(Listing)adapter.getItem(position);
				showListingDetailDialog();
			}catch(Exception x){
				Log.e(TAG,"selectItemListener.onItemClick",x);
			}
		}
	};
	private boolean liked; 
	
	
	public void loveClick(View v){
		theLayout.findViewById(R.id.listingdetail_dislike).getBackground().clearColorFilter();
		v.getBackground().setColorFilter(Color.GREEN, Mode.MULTIPLY);
		Log.d("ID:",""+v.getId());
	}
	
	public void hateClick(View v){
		theLayout.findViewById(R.id.listingdetail_like).getBackground().clearColorFilter();
		v.getBackground().setColorFilter(Color.RED, Mode.MULTIPLY);
	}
	
	View.OnClickListener urlClickedListener=new View.OnClickListener(){
		@Override
		public void onClick(View v){
			launchBrowser();
		}
	};
	
	private void launchBrowser(){
		try{
			Intent browserIntent=new Intent(Intent.ACTION_VIEW,Uri.parse(this.selectedListing.getListingUrl()));
			this.startActivity(browserIntent);
		}catch(Exception x){
			Log.e(TAG,"launchBrowser",x);
			this.showErrorDialog(x);
		}
	}
	
	//execute the search and display results

    private final Handler loadListHandler=new Handler(){
    	public void handleMessage(Message message){
    		loadListAdapter();
    	}
	};
	
	private void loadListAdapter(){
		this.adapter=new ListingArrayAdapter(this,R.layout.listviewitem,listings);
		this.setListAdapter(this.adapter);
		if(progressDialog!=null){
			progressDialog.cancel();
		}
	}
	
	private class LoadListThread extends Thread{
        private Handler handler;
        private Context context;
        
        public LoadListThread(Handler handler,Context context){
            this.handler=handler;
            this.context=context;
        }
       
        public void run(){
        	String searchResponse="";
        	try{
        		ebaycategories temp = new ebaycategories(this.context);
        		String temp2 = temp.getCategories();
        		Log.d("hello", temp2);
        		
        		if(ebayInvoke==null){
        			ebayInvoke=new ebaylistings(this.context);//new EbayInvoke(this.context);
        		}
        		if(ebayParser==null){
        			ebayParser=new EbayParser(this.context);
        		}
        		categoryTerm = getIntent().getStringExtra("id");
           		searchResponse=ebayInvoke.search(categoryTerm,"1");
           		//searchResponse=ebayInvoke.search(searchTerm);
        		//searchResponse = ebayInvoke.getListings("1",categoryTerm);
        		//String s = ebayDemoActivity.this;
           		if(listings==null){
           			listings=new SearchResult();
           		} 
           		Log.d("response", searchResponse);
           		ArrayList<Product> pp = ebayParser.parseProduct(searchResponse);
           		ArrayList<Listing> q = new ArrayList<Listing>();
           		q.addAll(pp);
        		listings.setListings(q);
            	this.handler.sendEmptyMessage(RESULT_OK);
        	}catch(Exception x){
    			Log.e(TAG,"LoadListThread.run(): searchResponse="+searchResponse,x);
    			listings.setError(x);
    			if((progressDialog!=null)&&(progressDialog.isShowing())){
    				progressDialog.dismiss();
    			}
    			showErrorDialog(x);        		
        	}
        }
    }
	
	private void refreshListings(){
		try{
			if(progressDialog==null){
				progressDialog=new ProgressDialog(this);
			}
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("Searching for auctions...");
			progressDialog.setCancelable(false);
			progressDialog.show();
			LoadListThread loadListThread=new LoadListThread(this.loadListHandler,this.getApplicationContext());
			loadListThread.start();
		}catch(Exception x){
			Log.e(TAG,"onResume",x);
			if((progressDialog!=null)&&(progressDialog.isShowing())){
				progressDialog.dismiss();
			}
			this.showErrorDialog(x);
		}	
	}
}