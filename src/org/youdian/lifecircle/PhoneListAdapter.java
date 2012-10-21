package org.youdian.lifecircle;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneListAdapter extends BaseAdapter {
    public int type;
    public static int PHONE_NUMBER=0;
    public static int PHONE_OPTION=1;
    public static final int typeCount=2;
    public static final int BIG_INT=10000;
    public static int SELECTED_ITEM=BIG_INT;
    public static int first_position=-1;
    public static int second_position=-1;
    public int option_status=-1;
    public static final int OPTION_INVISIBLE=1;
    public static final int OPTION_VISIBLE=1;
    Context context;
    LayoutInflater inflater;
    ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
    public PhoneListAdapter(Context context){
    	this.context=context;
    }
    
    public void addItem(Map map){
    	list.add(map);
    }
    public void showOption(int position){
    	
    	SELECTED_ITEM=position;
    	
    	option_status=OPTION_VISIBLE;
    	notifyDataSetChanged();
    }
    public void cancelOption(){
    	SELECTED_ITEM=BIG_INT;
    	option_status=OPTION_INVISIBLE;
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
				convertView=inflater.inflate(R.layout.listview_item_phonelist, null,false);
				viewHolder.name=(TextView)convertView.findViewById(R.id.phonelist_name);
				viewHolder.number=(TextView)convertView.findViewById(R.id.phonelist_phonenum);
				viewHolder.option=(Button)convertView.findViewById(R.id.phonelist_option);
				
				
			}else{
				convertView=inflater.inflate(R.layout.listview_item_phonelist_option, null,false);
				viewHolder.call=(Button)convertView.findViewById(R.id.phonelist_call);
				viewHolder.save=(Button)convertView.findViewById(R.id.phonelist_save);
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
					if(v.getId()>SELECTED_ITEM){
						second_position=v.getId()-1;
					}else{
						second_position=v.getId();
					}
					
					System.out.println("second position "+second_position);
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
			viewHolder.number.setText(String.valueOf((Integer)map.get("phone")));
		}else{
			viewHolder.call.setId(position);
			viewHolder.save.setId(position);
			viewHolder.call.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position=v.getId();
					Map<String,Object> map=getItem(position-1);
					int phone=(Integer)map.get("phone");
					Intent intent=new Intent("android.intent.action.DIAL",Uri.parse("tel:"+phone));
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
					Toast.makeText(context, "you save"+phone, Toast.LENGTH_SHORT).show();
				}
				
			});
		}
		
		return convertView;
	}
	
	public  static class ViewHolder{
		TextView name,number;
		Button call,save,option;
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
