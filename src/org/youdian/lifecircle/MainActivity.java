package org.youdian.lifecircle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements OnItemClickListener {
    private GridView gridView;
    private GridAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView=(GridView)findViewById(R.id.gridView);
        adapter=new GridAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		    Intent intent=new Intent();
		    
		    switch(position){
		    
		    case 0:
		         intent.setClass(this, ShopListActivity.class);
		         intent.putExtra("catagory", 1);
		         intent.putExtra("title", "餐馆");
		         startActivity(intent);
		         break;
		    case 1:
		    	intent.setClass(this, ShopListActivity.class);
		    	intent.putExtra("catagory", 2);
		    	intent.putExtra("title", "休闲娱乐");
		        startActivity(intent);
		        break;
		    case 2:
		    	intent.setClass(this, ShopListActivity.class);
		    	intent.putExtra("catagory", 3);
		    	intent.putExtra("title", "生活服务");
		        startActivity(intent);
		        break;
		    case 3:
		    	intent.setClass(this, SecondHandListActivity.class);
		    	intent.putExtra("catagory", 5);
		    	intent.putExtra("title", "二手市场");
		        startActivity(intent);
		    	break;
		    case 4:
		    	intent.setClass(this, ShopListActivity.class);
		    	intent.putExtra("catagory", 4);
		    	intent.putExtra("title", "购物");
		        startActivity(intent);
		    	break;
		    
		    case 5:
		    	intent.setClass(this, TakeAwayListActivity.class);
		    	startActivity(intent);
		    	break;
		    case 6:
		    	intent.setClass(this, PhoneListActivity.class);
		    	startActivity(intent);
		    	break;
		    case 7:
		    	intent.setClass(this, MyFavorActivity.class);
		    	startActivity(intent);
		    	break;
		    
		    
		    
		    
		
	        }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==1){
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	

    
}
