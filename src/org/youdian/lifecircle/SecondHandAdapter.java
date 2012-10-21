package org.youdian.lifecircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SecondHandAdapter extends BaseAdapter{
    ArrayList<Map<String,Object>> arrayList=new ArrayList<Map<String,Object>>();
    Map<String,Integer> catagory_map;
    LayoutInflater inflater;
    Context context;
    public SecondHandAdapter(Context context){
    	this.context=context;
    	initCatagoryInfo();
    }
    public void initCatagoryInfo(){
		catagory_map=new HashMap<String,Integer>();
		
		catagory_map.put("数码产品", 50);
		catagory_map.put("电脑/手机", 51);
		catagory_map.put("家电", 52);
		catagory_map.put("服饰鞋包", 53);
		catagory_map.put("家具", 54);
		catagory_map.put("其他二手", 55);
		
		
		
		
		
	}
    public void addItem(Map map){
    	arrayList.add(map);
    }
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	public Map<String,Object> getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.listview_item_secondhand, null, false);
			holder=new ViewHolder();
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		Map<String,Object> map=getItem(position);
		holder.name=(TextView)convertView.findViewById(R.id.secondhand_name);
		holder.catagory=(Button)convertView.findViewById(R.id.secondhand_catagory);
		holder.time=(TextView)convertView.findViewById(R.id.secondhand_time);
		holder.price=(TextView)convertView.findViewById(R.id.secondhand_price);
		
		holder.name.setText((String)map.get("name"));
		holder.catagory.setText((String)map.get("catagory"));
		String time=(String)map.get("time");
		String show_time=time.substring(5, 10);
		holder.time.setText(show_time);
		int price=(Integer)map.get("price");
		holder.price.setText(price+"元");
		final String catagory_name=holder.catagory.getText().toString();
		holder.catagory.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int catagory_id=(Integer)catagory_map.get(catagory_name);
				Intent intent=new Intent();
				intent.setClass(context, SecondHandListActivity.class);
				intent.putExtra("catagory",catagory_id);
				intent.putExtra("title",catagory_name);
				context.startActivity(intent);
			}
			
		});
		return convertView;
	}
	
	public static class ViewHolder{
		TextView name,time,price;
		Button catagory;
	}

}
