package org.youdian.lifecircle;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShopActivity extends Activity implements OnClickListener{
    ProgressBar progressBar;
    Button back,save;
    TextView name,detail,address,discount;
    LinearLayout shop_layout;
    int shop_id;
    Map<String ,Object> map;
    SQLiteHelper helper=new SQLiteHelper(this,"favor");
    SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopdetail);
		progressBar=(ProgressBar)findViewById(R.id.shop_firstprogressbar);
		initViews();
		Intent intent=getIntent();
		shop_id=intent.getIntExtra("id", 0);
		if(shop_id!=0){
			LoadTask task=new LoadTask();
			task.execute(String.valueOf(shop_id));
		}else{
			progressBar.setVisibility(View.GONE);
			Toast.makeText(ShopActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	class LoadTask extends AsyncTask<String,Integer,String>{
         
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url="http://lifecircle.sinaapp.com/shop?shop_id="+params[0];
			
			String result=NetOperation.getData(url);
			if(result==null)result="error";
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			progressBar.setVisibility(View.GONE);
						
			if(result.startsWith("[")&&result.length()>10){
				parseJson(result);
				name.setText((String)map.get("name"));
				detail.setText((String)map.get("detail"));
				address.setText((String)map.get("address"));
				discount.setText((String)map.get("discount"));
				shop_layout.setVisibility(View.VISIBLE);
				save.setOnClickListener(ShopActivity.this);
				
			}else if(result.startsWith("[")){
				Toast.makeText(ShopActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(ShopActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
			}
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}
		
		public void parseJson(String jsonData){
	    	  String stringvalue;
	    	  int intvalue;
	    	  try{
	    	  JsonReader reader=new JsonReader(new StringReader(jsonData));
	    	  reader.beginArray();
	    	  while(reader.hasNext()){
	    		  reader.beginObject();
	    		  map=new HashMap<String,Object>();
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
	                	  stringvalue=reader.nextString();
	    				  map.put("discount", stringvalue);
	    			  }
	                  
	                  else if(tagName.equals("address")){
	    				  stringvalue=reader.nextString();
	    				  map.put("address", stringvalue);
	    			  }
	                  else if(tagName.equals("detail")){
	    				  stringvalue=reader.nextString();
	    				  map.put("detail", stringvalue);
	    			  }
	                 
	    			  
	    		  }
	    		 
	    		  reader.endObject();
	    	  }
	    	  reader.endArray();
	    	  reader.close();
	    	  
	    	  
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }
	      }
		
	}
	
	private void initViews(){
		back=(Button)findViewById(R.id.shopdetail_back);
		save=(Button)findViewById(R.id.shop_save);
		name=(TextView)findViewById(R.id.shop_name);
	    detail=(TextView)findViewById(R.id.shop_detail);
	    address=(TextView)findViewById(R.id.shop_address);
	    discount=(TextView)findViewById(R.id.shop_discount);
	    shop_layout=(LinearLayout)findViewById(R.id.shop_layout);
	    back.setOnClickListener(this);
	    
	    
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==back){
			ShopActivity.this.finish();
		}else if(view==save){
			
			if(map.size()!=0){
								
				db=helper.getWritableDatabase();
				ContentValues cv=new ContentValues();
				cv.put("favor_id", (Integer)map.get("id"));
				cv.put("name", (String)map.get("name"));
				cv.put("detail", " ");
				cv.put("kind", "店铺");
				db.insert("favor", null, cv);
				db.close();
				Toast.makeText(ShopActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	

}
