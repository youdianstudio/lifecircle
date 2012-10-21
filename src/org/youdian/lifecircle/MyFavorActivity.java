package org.youdian.lifecircle;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MyFavorActivity extends Activity{
	
    Button back;
    ListView listview;
    MyFavorAdapter adapter;
    SQLiteHelper helper=new SQLiteHelper(this,"favor");
    Cursor cursor;
    SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfavor);
		back=(Button)findViewById(R.id.myfavor_back);
		listview=(ListView)findViewById(R.id.myfavor_list);
		db=helper.getReadableDatabase();
		cursor=db.query("favor", new String[]{"distinct id as _id","favor_id","name","kind","detail"}, null, null, null, null,null);
		adapter=new MyFavorAdapter(this,cursor);
		adapter.setHelper(helper);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if(NetState.isNetworkConnected(MyFavorActivity.this)){
					
				Cursor c=(Cursor)listview.getItemAtPosition(position);
				String kind=c.getString(3);
				Intent intent=new Intent();
				if(kind.equals("店铺")){
					intent.setClass(MyFavorActivity.this, ShopActivity.class);
					int shop_id=c.getInt(1);
					intent.putExtra("id", shop_id);
					startActivity(intent);
				}else{
					intent.setClass(MyFavorActivity.this, TakeAwayActivity.class);
					int shop_id=c.getInt(1);
					String shop_name=c.getString(2);
					String shop_phone=c.getString(4);
					intent.putExtra("id", shop_id);
					intent.putExtra("name", shop_name);
					intent.putExtra("phone", shop_phone);
					startActivity(intent);
					
				}	
										
					
					
				}else{
					Toast.makeText(MyFavorActivity.this, R.string.nonetwork, Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		cursor.close();
		db.close();
	}

}
