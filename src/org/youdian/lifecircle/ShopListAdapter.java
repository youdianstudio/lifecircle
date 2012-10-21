package org.youdian.lifecircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ShopListAdapter extends BaseAdapter{
    Context context;
    ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    Map<String,Integer> catagory_map;
    public static int COMMON=0;
    public static int DISCOUNT=1;
    public static int RECOMMEND=2;
    public static int BOTH=3;
    public static int typecount=4;
	LayoutInflater inflater;
	public ShopListAdapter(Context context){
		this.context=context;
		initCatagoryInfo();
		
	}
	public void initCatagoryInfo(){
		catagory_map=new HashMap<String,Integer>();
		catagory_map.put("江浙菜", 11);
		catagory_map.put("川菜", 12);
		catagory_map.put("粤菜", 13);
		catagory_map.put("湘菜",14);
		catagory_map.put("东北菜", 15);
		catagory_map.put("新疆/清真", 16);
		catagory_map.put("火锅", 17);
		catagory_map.put("自助餐", 18);
		catagory_map.put("小吃快餐", 19);
		catagory_map.put("西餐", 20);
		catagory_map.put("饮料", 21);
		catagory_map.put("面食", 22);
		catagory_map.put("酒吧", 23);
		catagory_map.put("茶馆", 24);
		catagory_map.put("KTV", 25);
		catagory_map.put("电影院", 26);
		catagory_map.put("足疗按摩", 27);
		catagory_map.put("咖啡厅", 28);
		catagory_map.put("洗浴", 29);
		catagory_map.put("桌球馆", 30);
		catagory_map.put("游乐游艺", 31);
		catagory_map.put("桌面游戏", 32);
		catagory_map.put("搬家", 33);
		catagory_map.put("家政", 34);
		catagory_map.put("快照/冲印", 35);
		catagory_map.put("复印/打印", 36);
		catagory_map.put("培训", 37);
		catagory_map.put("银行", 38);
		catagory_map.put("医院", 39);
		catagory_map.put("快递", 40);
		catagory_map.put("美容/SPA", 41);
		catagory_map.put("美发", 42);
		catagory_map.put("超市/便利店", 43);
		catagory_map.put("药店", 44);
		catagory_map.put("服饰鞋包", 45);
		catagory_map.put("书店", 46);
		catagory_map.put("眼镜店", 47);
		catagory_map.put("烟酒", 48);
		catagory_map.put("家居建材", 49);
		catagory_map.put("数码产品", 50);
		catagory_map.put("电脑/手机", 51);
		catagory_map.put("家电", 52);
		catagory_map.put("服饰鞋包", 53);
		catagory_map.put("家具", 54);
		catagory_map.put("其他二手", 55);
		
		
		
		
		
	}
	public void addItem(Map map){
		list.add(map);
		//notifyDataSetChanged();
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Map<String,Object> getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView=(LinearLayout)inflater.inflate(R.layout.listview_item_shoplist, null, false);
			holder=new ViewHolder();
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.name=(TextView)convertView.findViewById(R.id.shoplist_name);
		holder.address=(TextView)convertView.findViewById(R.id.shoplist_address);
		holder.favor=(TextView)convertView.findViewById(R.id.shoplist_favor);
		holder.discount=(ImageView)convertView.findViewById(R.id.shoplist_discount);
		holder.recommend=(ImageView)convertView.findViewById(R.id.shoplist_recommend);
		holder.catagory=(Button)convertView.findViewById(R.id.shoplist_catagory);
		
		Map<String,Object> map=getItem(position);
		String name=(String)map.get("name");
		String address=(String)map.get("address");
		String catagory=(String)map.get("catagory");
		int favor=(Integer)map.get("favor");
		holder.name.setText(name);
		holder.address.setText(address);
		holder.favor.setText(favor+" 收藏");
		holder.catagory.setText(catagory);
		holder.address.setTextSize(12);
		holder.favor.setTextSize(12);
		holder.catagory.setTextSize(12);
		//holder.catagory.setId(position);
		final String catagory_name=holder.catagory.getText().toString();
		holder.catagory.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				int catagory_id=(Integer)catagory_map.get(catagory_name);
				Intent intent=new Intent();
				intent.setClass(context, ShopListActivity.class);
				intent.putExtra("catagory",catagory_id);
				intent.putExtra("title",catagory_name);
				context.startActivity(intent);
			}
			
		});
		int type=getItemViewType(position);
		switch(type){
		case 0:
			
			break;
		case 1:
			holder.discount.setVisibility(View.VISIBLE);
			break;
		case 2:
			holder.recommend.setVisibility(View.VISIBLE);
			break;
		case 3:
			holder.discount.setVisibility(View.VISIBLE);
			holder.recommend.setVisibility(View.VISIBLE);
			break;
		}
		return convertView;
	}
	
	public static class ViewHolder{
		TextView name,address,favor;
		Button catagory;
		ImageView discount,recommend;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		HashMap<String,Object> map;
		int type=0;
		map=(HashMap<String,Object>)list.get(position);
		int discount=(Integer)map.get("discount");
		int recommend=(Integer)map.get("recommend");
		if(discount==0&&recommend==0){
			type=COMMON;
		}else if(discount==0&&recommend==1){
			type=RECOMMEND;
		}else if(discount==1&&recommend==0){
			type=DISCOUNT;
		}else if(discount==1&&recommend==1){
			type=BOTH;
		}
		return type;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return typecount;
	}
	
	

	
	

}
