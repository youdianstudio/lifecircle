package org.youdian.lifecircle;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopListActivity extends Activity{
    ProgressBar firstProgressBar,moreProgressBar;
    ListView listview;
    RelativeLayout loadmore;
    TextView loadmore_tv;
    Button home;
    TextView title;
    String title_name;
    LayoutInflater inflater;
    ArrayList<Map<String,Object>> arrayList=new ArrayList<Map<String,Object>>();
    ShopListAdapter adapter;
    public static int load_source;
    public static int FIRST_LOAD=0;
    public static int MORE_LOAD=1;
    public  int catagory_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoplist);
		Intent intent=getIntent();
		catagory_id=intent.getIntExtra("catagory", 1);
		title_name=intent.getStringExtra("title");
		title=(TextView)findViewById(R.id.shoplist_title);
		title.setText(title_name);
		firstProgressBar=(ProgressBar)findViewById(R.id.shoplist_firstprogressbar);
		
		listview=(ListView)findViewById(R.id.shoplist_list);
		inflater=this.getLayoutInflater();
		loadmore=(RelativeLayout)inflater.inflate(R.layout.footerview, null,false);
		listview.addFooterView(loadmore);
		adapter=new ShopListAdapter(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if(NetState.isNetworkConnected(ShopListActivity.this)){
					Intent intent=new Intent();
					intent.setClass(ShopListActivity.this, ShopActivity.class);
					Map<String,Object> map=(Map<String,Object>)listview.getItemAtPosition(position);
					int shop_id=(Integer)map.get("id");
					intent.putExtra("id", shop_id);
					startActivity(intent);
				}else{
					Toast.makeText(ShopListActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
		loadmore_tv=(TextView)loadmore.findViewById(R.id.loadmore_tv);
		moreProgressBar=(ProgressBar)loadmore.findViewById(R.id.loadmore_progressbar);
		home=(Button)findViewById(R.id.shoplist_back_to_home);
		if(NetState.isNetworkConnected(this)){
			load_source=FIRST_LOAD;
			LoadTask task=new LoadTask();
			
			task.execute(String.valueOf(catagory_id),String.valueOf(adapter.getCount()));
		}else{
			Toast.makeText(this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
		}
		
		loadmore.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(NetState.isNetworkConnected(ShopListActivity.this)){
					load_source=MORE_LOAD;
					LoadTask task=new LoadTask();
					task.execute(String.valueOf(catagory_id),String.valueOf(adapter.getCount()));
				}else{
					Toast.makeText(ShopListActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		home.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShopListActivity.this.finish();
			}
			
		});
	}
	
	public  void updateList(int catagory_id){
		Toast.makeText(ShopListActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
	}
	
	class LoadTask extends AsyncTask<String,Integer,String>{
        String url="http://lifecircle.sinaapp.com/shoplist";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Map<String,String> map=new HashMap<String,String>();
			map.put("catagory", params[0]);
			map.put("limit", params[1]);
			String result=NetOperation.postData(map, url);
			if(result==null)result="error";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println(result);
			if(load_source==FIRST_LOAD){
				firstProgressBar.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
			}else if(load_source==MORE_LOAD){
				loadmore_tv.setText(R.string.loadmore);
				moreProgressBar.setVisibility(View.GONE);
			}
			
			if(result.startsWith("[")&&result.length()>10){
				parseJson(result);
				adapter.notifyDataSetChanged();
				
			}else if(result.startsWith("[")){
				Toast.makeText(ShopListActivity.this, R.string.nomoredata, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(ShopListActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(load_source==FIRST_LOAD){
				firstProgressBar.setVisibility(View.VISIBLE);
			}else if(load_source==MORE_LOAD){
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
	    			  }
	    			  else if(tagName.equals("name")){
	    				  stringvalue=reader.nextString();
	    				  map.put("name", stringvalue);
	    			  }
	                  else if(tagName.equals("discount")){
	                	  intvalue=reader.nextInt();
	    				  map.put("discount", intvalue);
	    			  }
	                  else if(tagName.equals("recommend")){
	                	  intvalue=reader.nextInt();
	    				  map.put("recommend", intvalue);
	    			  }
	                  else if(tagName.equals("favor")){
	                	  intvalue=reader.nextInt();
	    				  map.put("favor", intvalue);
	    			  }
	                  else if(tagName.equals("address")){
	    				  stringvalue=reader.nextString();
	    				  map.put("address", stringvalue);
	    			  }
	                  else if(tagName.equals("whichcatagory")){
	    				  stringvalue=reader.nextString();
	    				  map.put("catagory", stringvalue);
	    			  }
	                 
	    			  
	    		  }
	    		  adapter.addItem(map);
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
