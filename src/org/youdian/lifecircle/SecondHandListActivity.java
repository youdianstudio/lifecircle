package org.youdian.lifecircle;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.youdian.lifecircle.ShopListActivity.LoadTask;

import com.google.gson.stream.JsonReader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SecondHandListActivity extends Activity{

	 ProgressBar firstProgressBar,moreProgressBar;
	    ListView listview;
	    RelativeLayout loadmore;
	    TextView loadmore_tv;
	    Button home;
	    TextView title;
	    String title_name;
	    LayoutInflater inflater;
	    ArrayList<Map<String,Object>> arrayList=new ArrayList<Map<String,Object>>();
	    SecondHandAdapter adapter;
	    public static int load_source;
	    public static int FIRST_LOAD=0;
	    public static int MORE_LOAD=1;
	    public  int catagory_id;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_secondhandlist);
			Intent intent=getIntent();
			catagory_id=intent.getIntExtra("catagory", 1);
			title_name=intent.getStringExtra("title");
			title=(TextView)findViewById(R.id.secondhand_title);
			title.setText(title_name);
			firstProgressBar=(ProgressBar)findViewById(R.id.secondhand_firstprogressbar);
			
			listview=(ListView)findViewById(R.id.secondhand_list);
			inflater=this.getLayoutInflater();
			loadmore=(RelativeLayout)inflater.inflate(R.layout.footerview, null,false);
			listview.addFooterView(loadmore);
			adapter=new SecondHandAdapter(this);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					// TODO Auto-generated method stub
					if(NetState.isNetworkConnected(SecondHandListActivity.this)){
						Map<String,Object> map=(Map<String,Object>)listview.getItemAtPosition(position);
						int product_id=(Integer)map.get("id");
						Intent intent=new Intent();
						intent.setClass(SecondHandListActivity.this, SecondHandActivity.class);
						intent.putExtra("id", product_id);
						startActivity(intent);
					}else{
						Toast.makeText(SecondHandListActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
					}
					
					
				}
				
			});
			loadmore_tv=(TextView)loadmore.findViewById(R.id.loadmore_tv);
			moreProgressBar=(ProgressBar)loadmore.findViewById(R.id.loadmore_progressbar);
			home=(Button)findViewById(R.id.secondhand_back_to_home);
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
					if(NetState.isNetworkConnected(SecondHandListActivity.this)){
						load_source=MORE_LOAD;
						LoadTask task=new LoadTask();
						task.execute(String.valueOf(catagory_id),String.valueOf(adapter.getCount()));
					}else{
						Toast.makeText(SecondHandListActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			
			home.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SecondHandListActivity.this.finish();
				}
				
			});
		}
		
		public  void updateList(int catagory_id){
			Toast.makeText(SecondHandListActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
		}
		
		class LoadTask extends AsyncTask<String,Integer,String>{
	        String url="http://lifecircle.sinaapp.com/secondhand";
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
					Toast.makeText(SecondHandListActivity.this, R.string.nomoredata, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(SecondHandListActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
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
		                  else if(tagName.equals("time")){
		                	  stringvalue=reader.nextString();
		    				  map.put("time", stringvalue);
		    			  }
		                  
		                  else if(tagName.equals("whichcatagory")){
		    				  stringvalue=reader.nextString();
		    				  map.put("catagory", stringvalue);
		    			  }else if(tagName.equals("price")){
		    				  intvalue=reader.nextInt();
		    				  map.put("price", intvalue);
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
