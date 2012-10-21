package org.youdian.lifecircle;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;



import com.google.gson.stream.JsonReader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SecondHandActivity extends Activity implements OnClickListener{
    int id;
    ProgressBar progressBar;
    Button back;
    Button call,sms;
    TextView name,price,date,person,contact,detail;
    RelativeLayout secondhand_layout;
    Map<String,Object> map=new HashMap<String,Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secondhand);
		progressBar=(ProgressBar)findViewById(R.id.secondhanddetail_firstprogressbar);
		back=(Button)findViewById(R.id.secondhanddetail_back);
		
		call=(Button)findViewById(R.id.secondhanddetail_call);
		sms=(Button)findViewById(R.id.secondhanddetail_sms);
		name=(TextView)findViewById(R.id.secondhanddetail_name);
		price=(TextView)findViewById(R.id.secondhanddetail_price);
		date=(TextView)findViewById(R.id.secondhanddetail_date);
		person=(TextView)findViewById(R.id.secondhanddetail_person);
		contact=(TextView)findViewById(R.id.secondhanddetail_contact);
		detail=(TextView)findViewById(R.id.secondhanddetail_detail);
		secondhand_layout=(RelativeLayout)findViewById(R.id.secondhand_layout);
		back.setOnClickListener(this);
		
		call.setOnClickListener(this);
		sms.setOnClickListener(this);
		Intent intent=getIntent();
		id=intent.getIntExtra("id", 0);
		if(id!=0){
			showDetail();
		}
	}
  public void showDetail(){
	  
		  LoadTask task=new LoadTask();
		  task.execute();
	  
  }
  
  class LoadTask extends AsyncTask<String,Integer,String>{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url="http://lifecircle.sinaapp.com/secondhanddetail";
		Map<String,String> map=new HashMap<String,String>();
		map.put("id", String.valueOf(id));
		String result=NetOperation.postData(map, url);
		if(result==null)result="error";
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressBar.setVisibility(View.INVISIBLE);
		
		if(result.startsWith("[")&&result.length()>10){
			parsonJson(result);
			if(map.size()!=0){
				name.setText((String)map.get("name"));
				price.setText("转让价格: "+(Integer)map.get("price")+" 元");
				date.setText(((String)map.get("date")).substring(5, 10));
				detail.setText((String)map.get("detail"));
				person.setText((String)map.get("person"));
				contact.setText((String)map.get("contact"));
				secondhand_layout.setVisibility(View.VISIBLE);
				
			}
			
		}else {
			Toast.makeText(SecondHandActivity.this, R.string.errordata, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressBar.setVisibility(View.VISIBLE);
	}
	
	private void parsonJson(String jsonData){
		String tagName="";
		String stringvalue;
		int intvalue;
		JsonReader reader=new JsonReader(new StringReader(jsonData));
		
		try {
			reader.beginArray();
			while(reader.hasNext()){
				reader.beginObject();
				
				while(reader.hasNext()){
					tagName=reader.nextName();
					if(tagName.equals("id")){
						intvalue=reader.nextInt();
						map.put("id", intvalue);
					}else if(tagName.equals("name")){
						stringvalue=reader.nextString();
						map.put("name", stringvalue);
					}else if(tagName.equals("person")){
						stringvalue=reader.nextString();
						map.put("person", stringvalue);
					}else if(tagName.equals("contact")){
						stringvalue=reader.nextString();
						map.put("contact", stringvalue);
					}else if(tagName.equals("detail")){
						stringvalue=reader.nextString();
						map.put("detail", stringvalue);
					}else if(tagName.equals("price")){
						intvalue=reader.nextInt();
						map.put("price", intvalue);
					}else if(tagName.equals("date")){
						stringvalue=reader.nextString();
						map.put("date", stringvalue);
					}
				}
				reader.endObject();
			}
			reader.endArray();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	  
  }

public void onClick(View view) {
	// TODO Auto-generated method stub
	if(view==back){
		SecondHandActivity.this.finish();
	}else if(view==call){
		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:"+contact.getText().toString()));
		startActivity(intent);
		
	}else if(view==sms){
		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("smsto:"+contact.getText().toString()));
		startActivity(intent);
	}
}
}
