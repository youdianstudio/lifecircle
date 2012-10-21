package org.youdian.lifecircle;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import com.google.gson.stream.JsonReader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TakeAwayActivity extends Activity{
    Button back,call;
    ListView listview;
    TextView name,phone;
    TextView loadmore_tv;
    ProgressBar moreProgressBar;
    RelativeLayout loadmore;
    LinearLayout takeaway_layout;
    SimpleAdapter adapter;
    ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    LayoutInflater inflater;
    ProgressBar progressBar;
    public static int load_source;
    public static int FIRST_LOAD=0;
    public static int MORE_LOAD=1;
    public static int shop_id=0;
    String shop_phone;
    String shop_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_takeaway);
		Intent intent=getIntent();
		shop_name=intent.getStringExtra("name");
		shop_phone=intent.getStringExtra("phone");
		progressBar=(ProgressBar)findViewById(R.id.takeaway_firstprogressbar);
		back=(Button)findViewById(R.id.takeaway_back);
		back.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TakeAwayActivity.this.finish();
			}
		  
		});
		call=(Button)findViewById(R.id.takeaway_call);
		call.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+shop_phone));
				startActivity(intent);
			}
			
		});
		listview=(ListView)findViewById(R.id.takeaway_list);
		name=(TextView)findViewById(R.id.takeaway_name);
		name.setText(shop_name);
		phone=(TextView)findViewById(R.id.takeaway_phone);
		phone.setText("电话: "+shop_phone);
		takeaway_layout=(LinearLayout)findViewById(R.id.takeaway_layout);
		
		inflater=LayoutInflater.from(this);
		loadmore=(RelativeLayout)inflater.inflate(R.layout.footerview, null);
		loadmore_tv=(TextView)loadmore.findViewById(R.id.loadmore_tv);
		moreProgressBar=(ProgressBar)loadmore.findViewById(R.id.loadmore_progressbar);
		loadmore.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(NetState.isNetworkConnected(TakeAwayActivity.this)){
					load_source=MORE_LOAD;
					LoadTask task=new LoadTask();
					task.execute(String.valueOf(shop_id),String.valueOf(list.size()));
				}else{
					Toast.makeText(TakeAwayActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
		listview.addFooterView(loadmore);
		adapter=new SimpleAdapter(this, list, R.layout.listview_item_takeaway, new String[]{"name","price"}, 
				new int[]{R.id.takeaway_item_dishname,R.id.takeaway_item_dishprice});
		listview.setAdapter(adapter);
		back.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				TakeAwayActivity.this.finish();
			}
			
		});
		load_source=FIRST_LOAD;
		LoadTask task=new LoadTask();
		task.execute(String.valueOf(shop_id),String.valueOf(list.size()));
		
	}
	
	class LoadTask extends AsyncTask<String,Integer,String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url="http://lifecircle.sinaapp.com/dish?shop_id="+params[0]+"&limit="+params[1];
			String result=NetOperation.getData(url);
			if(result==null)result="error";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(load_source==MORE_LOAD){
			  	loadmore_tv.setText(R.string.loadmore);
			  	moreProgressBar.setVisibility(View.GONE);
			}else if(load_source==FIRST_LOAD){
				progressBar.setVisibility(View.GONE);
			}
			
			if(result.startsWith("[")&&result.length()>10){
				if(load_source==FIRST_LOAD)
				takeaway_layout.setVisibility(View.VISIBLE);
				parseJson(result);
				adapter.notifyDataSetChanged();
				
			}else if(result.startsWith("[")){
				Toast.makeText(TakeAwayActivity.this, R.string.nomoredata, Toast.LENGTH_SHORT).show();
				loadmore_tv.setText(R.string.nomoredata);
			}else{
				Toast.makeText(TakeAwayActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(load_source==MORE_LOAD){
			  	loadmore_tv.setText(R.string.loading);
			  	moreProgressBar.setVisibility(View.VISIBLE);
			}
		}
		
		
		public void parseJson(String jsonData){
	    	  String stringvalue;
	    	  int intvalue;
	    	  try{
	    	  JsonReader reader=new JsonReader(new StringReader(jsonData));
	    	  reader.beginArray();
	    	  while(reader.hasNext()){
	    		  reader.beginObject();
	    		  Map<String ,Object> map=new HashMap<String,Object>();
	    		  while(reader.hasNext()){
	    			  String tagName=reader.nextName();
	    			  
	    			  if(tagName.equals("id")){
	    				  intvalue=reader.nextInt();
	    				  map.put("id", intvalue);
	    			  }else if(tagName.equals("name")){
	    				  stringvalue=reader.nextString();
	    				  map.put("name", stringvalue);
	    			  }else if(tagName.equals("price")){
	    				  intvalue=reader.nextInt();
	    				  map.put("price", intvalue);
	    			  }
	    			  
	                 
	    			  
	    		  }
	    		  list.add(map);
	    		  reader.endObject();
	    	  }
	    	  reader.endArray();
	    	  reader.close();
	    	  
	    	  
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }
	      }
		
	}

}
