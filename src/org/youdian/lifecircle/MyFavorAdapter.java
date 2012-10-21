package org.youdian.lifecircle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyFavorAdapter extends CursorAdapter{
    Context context;
    LayoutInflater inflater;
    SQLiteHelper helper;
	public MyFavorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
	}
	public MyFavorAdapter(Context context, Cursor c) {
		super(context, c, true);
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	public void getHelper(){
		
	}
	public void setHelper(SQLiteHelper helper){
		this.helper=helper;
	}
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView name=(TextView)view.findViewById(R.id.myfavor_item_name);
		TextView kind=(TextView)view.findViewById(R.id.myfavor_item_kind);
		TextView detail=(TextView)view.findViewById(R.id.myfavor_item_detail);
		Button cancel=(Button)view.findViewById(R.id.myfavor_item_cancel);
		name.setText(cursor.getString(2));
		kind.setText(cursor.getString(3));
		detail.setText(cursor.getString(4));
		final int favor_id=cursor.getInt(1);
		final Cursor c=cursor;
		cancel.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
				SQLiteDatabase db=helper.getWritableDatabase();
				db.delete("favor", "favor_id="+favor_id, null);
				
				c.requery();
				System.out.println("you deleted:"+favor_id);
			}
			
		});
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		inflater=LayoutInflater.from(context);
		return inflater.inflate(R.layout.listview_item_myfavor, null,false);
	}

}
