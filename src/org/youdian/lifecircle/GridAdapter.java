package org.youdian.lifecircle;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter{

    private String[] s;
    private ArrayList<String> arrayList=new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;
    public GridAdapter(Context context){
    	this.context=context;
    	inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initData();
    }
    
    public void initData(){
    	s=context.getResources().getStringArray(R.array.catagory);
       for(int i=0;i<s.length;i++){
    	   arrayList.add(s[i]);
       }
       
    }

	public int getCount() {
		// TODO Auto-generated method stub
		
		return arrayList.size();
	}

	public String getItem(int position) {
		
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
		int type=getItemViewType(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.gridview_item, null);			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
			
		}
        holder.textView=(TextView)convertView.findViewById(R.id.text);
        holder.textView.setText(arrayList.get(position));
		return convertView;
	}


	
	public static class ViewHolder{
		public TextView textView;

	}
	

}
