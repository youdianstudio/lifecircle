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

public class PhoneListActivity extends Activity{

	    ProgressBar firstProgressBar,moreProgressBar;
	    ListView listview;
	    RelativeLayout loadmore;
	    TextView loadmore_tv;
	    Button home;
	   // TextView title;
	   // String title_name;
	    LayoutInflater inflater;
	    ArrayList<Map<String,Object>> arrayList=new ArrayList<Map<String,Object>>();
	    PhoneListAdapter adapter;
	    public static int load_source;
	    public static int FIRST_LOAD=0;
	    public static int MORE_LOAD=1;
	    //public int first_position=-1;
	    //public int second_position=-1;
	    //public  int catagory_id;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_phonelist);
			
			firstProgressBar=(ProgressBar)findViewById(R.id.phonelist_firstprogressbar);
			
			listview=(ListView)findViewById(R.id.phonelist_list);
			inflater=this.getLayoutInflater();
			loadmore=(RelativeLayout)inflater.inflate(R.layout.footerview, null,false);
			listview.addFooterView(loadmore);
			adapter=new PhoneListAdapter(this);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
					if(position>PhoneListAdapter.SELECTED_ITEM){
						PhoneListAdapter.second_position=position-1;
					}else{
						PhoneListAdapter.second_position=position;
					}
					
					if(PhoneListAdapter.second_position!=PhoneListAdapter.first_position){
						PhoneListAdapter.first_position=PhoneListAdapter.second_position;
						
						adapter.showOption(PhoneListAdapter.second_position);
						PhoneListAdapter.second_position=-1;
					}else{
						PhoneListAdapter.first_position=-1;
						adapter.cancelOption();
					}
					
					
				}
				
			});
			loadmore_tv=(TextView)loadmore.findViewById(R.id.loadmore_tv);
			moreProgressBar=(ProgressBar)loadmore.findViewById(R.id.loadmore_progressbar);
			home=(Button)findViewById(R.id.phonelist_back_to_home);
			if(NetState.isNetworkConnected(this)){
				load_source=FIRST_LOAD;
				LoadTask task=new LoadTask();
				//System.out.println(catagory_id);
				task.execute(String.valueOf(adapter.getItemCount()));
			}else{
				Toast.makeText(this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
			}
			
			loadmore.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(NetState.isNetworkConnected(PhoneListActivity.this)){
						load_source=MORE_LOAD;
						LoadTask task=new LoadTask();
						task.execute(String.valueOf(adapter.getItemCount()));
					}else{
						Toast.makeText(PhoneListActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			
			home.setOnClickListener(new OnClickListener(){

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					PhoneListActivity.this.finish();
				}
				
			});
		}
		
		class LoadTask extends AsyncTask<String,Integer,String>{
	        String url="http://lifecircle.sinaapp.com/phone";
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Map<String,String> map=new HashMap<String,String>();
				//map.put("catagory", params[0]);
				map.put("limit", params[0]);
				String result=NetOperation.postData(map, url);
				if(result==null)result="error";
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
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
					Toast.makeText(PhoneListActivity.this, R.string.nomoredata, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(PhoneListActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
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
		                  else if(tagName.equals("phone")){
		                	  intvalue=reader.nextInt();
		    				  map.put("phone", intvalue);
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
