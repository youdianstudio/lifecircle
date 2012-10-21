package org.youdian.lifecircle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TakeAwayListAdapter extends BaseAdapter{

	  public  int type;
	    public static int PHONE_NUMBER=0;
	    public static int PHONE_OPTION=1;
	    public static final int typeCount=2;
	    public static final int BIG_INT=10000;
	    public  int SELECTED_ITEM=BIG_INT;
	    public int first_position=-1;
	    public int second_position=-1;
	    Context context;
	    LayoutInflater inflater;
	    ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	    public TakeAwayListAdapter(Context context){
	    	this.context=context;
	    }
	    
	    public void addItem(Map map){
	    	list.add(map);
	    }
	    public void showOption(int position){
	    	if(position>SELECTED_ITEM){
	    		SELECTED_ITEM=position-1;
	    	}else{
	    		SELECTED_ITEM=position;
	    	}
	    	
	    	notifyDataSetChanged();
	    }
	    public void cancelOption(){
	    	SELECTED_ITEM=BIG_INT;
	    	notifyDataSetChanged();
	    }
	    
	    public int getItemCount(){
	    	return list.size();
	    }
		public int getCount() {
			// TODO Auto-generated method stub
			return SELECTED_ITEM==BIG_INT?list.size():list.size()+1;
		}

		public Map<String,Object> getItem(int position) {
			// TODO Auto-generated method stub
			
			Map<String,Object> map=null;
			if(position<SELECTED_ITEM+1){
				map=list.get(position);
			}else if(position>SELECTED_ITEM+1){
				map=list.get(position-1);
			}
			return map;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	    

		
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder=null;
			int type=getItemViewType(position);
			Map<String,Object> map=getItem(position);
			if(convertView==null){
				inflater=LayoutInflater.from(context);
				viewHolder=new ViewHolder();
				
				if(type==PHONE_NUMBER){
					convertView=inflater.inflate(R.layout.listview_item_takeawaylist, null,false);
					viewHolder.name=(TextView)convertView.findViewById(R.id.takeawaylist_item_name);
					viewHolder.number=(TextView)convertView.findViewById(R.id.takeawaylist_item_phonenum);
					viewHolder.status=(TextView)convertView.findViewById(R.id.takeawaylist_item_status);
					viewHolder.option=(Button)convertView.findViewById(R.id.takeawaylist_item_option);
					viewHolder.detail=(TextView)convertView.findViewById(R.id.takeawaylist_item_detail);
					
				}else{
					convertView=inflater.inflate(R.layout.listview_item_takeawaylist_option, null,false);
					viewHolder.call=(Button)convertView.findViewById(R.id.takeawaylist_item_call);
					viewHolder.save=(Button)convertView.findViewById(R.id.takeawaylist_item_save);
					viewHolder.view=(Button)convertView.findViewById(R.id.takeawaylist_item_view);
				}
				convertView.setTag(viewHolder);
			}else{
				viewHolder=(ViewHolder)convertView.getTag();
			}
			
			if(type==PHONE_NUMBER){
				// setID() should be invoked here
				viewHolder.option.setId(position);
				viewHolder.option.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						// TODO Auto-generated method stub
						second_position=v.getId();
						if(second_position!=first_position){
							first_position=second_position;
							
							showOption(second_position);
							second_position=-1;
						}else{
							first_position=-1;
							cancelOption();
						}
					}
					
				});
				viewHolder.name.setText((String)map.get("name"));
				viewHolder.number.setText("电话： "+String.valueOf((Integer)map.get("phone")));
				viewHolder.detail.setText((String)map.get("detail"));
				SimpleDateFormat sformat=new SimpleDateFormat("HHmm");
				String current_time=sformat.format(new Date());
				int currenttime=Integer.parseInt(current_time);
				int starttime=(Integer)map.get("starttime");
				int endtime=(Integer)map.get("endtime");
				if(currenttime<starttime||currenttime>endtime){
					viewHolder.status.setText(R.string.closed);
				}else{
					viewHolder.status.setText(R.string.opening);
				}
			}else{
				viewHolder.call.setId(position);
				viewHolder.save.setId(position);
				viewHolder.view.setId(position);
				viewHolder.call.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						// TODO Auto-generated method stub
						int position=v.getId();
						Map<String,Object> map=getItem(position-1);
						int phone=(Integer)map.get("phone");
						Intent intent=new Intent("android.intent.action.DIAL",Uri.parse("tel:"+phone));
						//Intent intent=new Intent("android.intent.action.CALL_BUTTON");
						context.startActivity(intent);
						Toast.makeText(context, "you call"+phone, Toast.LENGTH_SHORT).show();
					}
					
				});
				viewHolder.save.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						// TODO Auto-generated method stub
						int position=v.getId();
						Map<String,Object> map=getItem(position-1);
						int phone=(Integer)map.get("phone");
						SQLiteHelper helper=new SQLiteHelper(context,"favor");
						SQLiteDatabase db=helper.getWritableDatabase();
						ContentValues cv=new ContentValues();
						cv.put("favor_id", (Integer)map.get("id"));
						cv.put("name", (String)map.get("name"));
						cv.put("detail", String.valueOf(phone));
						cv.put("kind", "外卖");
						db.insert("favor", null, cv);
						db.close();
						Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
						
					}
					
				});
				viewHolder.view.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						// TODO Auto-generated method stub
						int position=v.getId();
						
						if(NetState.isNetworkConnected(context)){
							Intent intent=new Intent();
							intent.setClass(context, TakeAwayActivity.class);
							Map<String,Object> map=getItem(position-1);
							int shop_id=(Integer)map.get("id");
							String shop_name=(String)map.get("name");
							String shop_phone=((Integer)map.get("phone")).toString();
							intent.putExtra("id", shop_id);
							intent.putExtra("name", shop_name);
							intent.putExtra("phone", shop_phone);
							context.startActivity(intent);
						}else{
							Toast.makeText(context, R.string.nonetwork, Toast.LENGTH_SHORT).show();
						}
					}
					
				});
			}
			
			return convertView;
		}
		
		public  class ViewHolder{
			TextView name,number,detail;
			TextView status;
			Button call,save,option,view;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			int type=PHONE_NUMBER;
			if(position==SELECTED_ITEM+1)type=PHONE_OPTION;
			return type;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return typeCount;
		}


}
