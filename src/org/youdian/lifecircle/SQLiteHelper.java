package org.youdian.lifecircle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper{
    public static final int VERSION=1;
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public SQLiteHelper(Context context,String name,int version){
		super(context,name,null,version);
	}
	public SQLiteHelper(Context context,String name){
		super(context,name,null,VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql="create table if not exists %s(%s int auto increment,%s int ,%s varchar(100),%s varchar(50),%s varchar(50))";
		sql=String.format(sql, "favor","id","favor_id","name","kind","detail");
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
