package ru.itx.conduit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {
	private final static String query0 = "CREATE TABLE " + DBTables.T0_TABLE_NAME + " ("
			+ DBTables.T0_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DBTables.T0_COLUMN_PASSWORD + " TEXT NOT NULL, " 
			+ DBTables.T0_COLUMN_DB_PASSWORD + " TEXT NOT NULL, " 
			+ DBTables.T0_COLUMN_REPLICA_ID + " TEXT " 
			+"); ";
	private final static String query1 = "CREATE TABLE " + DBTables.T1_TABLE_NAME + " ("
			+ DBTables.T1_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DBTables.T1_COLUMN_NAME + " TEXT NOT NULL, " 
			+ DBTables.T1_COLUMN_LASTNAME	+ " TEXT, " 
			+ DBTables.T1_COLUMN_SURNAME + " TEXT NOT NULL, "
			+ DBTables.T1_COLUMN_EMAIL + " TEXT, "
			+ DBTables.T1_COLUMN_PHONE + " TEXT, "
			+ DBTables.T1_COLUMN_PARENTPHONE + " TEXT, "
			+ DBTables.T1_COLUMN_BIRTHDAY + " INTEGER, "
			+ DBTables.T1_COLUMN_NUMCLASS	 + " TEXT, "
			+ DBTables.T1_COLUMN_HIDDEN + " TEXT, "
			+ DBTables.T1_COLUMN_PHOTO + " BLOB "
			+"); ";
	private final static String query2 = "CREATE TABLE " + DBTables.T2_TABLE_NAME + " ("
			+ DBTables.T2_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DBTables.T2_COLUMN_NAME + " TEXT NOT NULL, " 
			+ DBTables.T2_COLUMN_CITY + " TEXT, " 
			+ DBTables.T2_COLUMN_TEACHER + " TEXT, " 
			+ DBTables.T2_COLUMN_ADDITIONAL + " TEXT, " 
			+ DBTables.T2_COLUMN_TIMETABLE + " TEXT " 
			+"); ";
	private final static String query3 = "CREATE TABLE " + DBTables.T3_TABLE_NAME + " ("
			+ DBTables.T3_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DBTables.T3_COLUMN_TEACH_GROUP_ID + " INTEGER, "
			+ DBTables.T3_COLUMN_STUDENT_ID + " INTEGER, "
			+ " FOREIGN KEY("+ DBTables.T3_COLUMN_TEACH_GROUP_ID+") REFERENCES teach_group(_id), " 
			+ " FOREIGN KEY("+ DBTables.T3_COLUMN_STUDENT_ID+") REFERENCES student(_id) " 
			+"); ";
	private final static String query4 = "CREATE TABLE " + DBTables.T4_TABLE_NAME + " ("
			+ DBTables.T4_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DBTables.T4_COLUMN_SUBJECT + " TEXT, " 
			+ DBTables.T4_COLUMN_DATE + " INTEGER NOT NULL, " 
			+ DBTables.T4_COLUMN_STUDENT_ID + " INTEGER, "
			+ DBTables.T4_COLUMN_GROUP_ID + " INTEGER, "
			+ DBTables.T4_COLUMN_VALUE + " TEXT, " 
			+ " FOREIGN KEY("+ DBTables.T4_COLUMN_STUDENT_ID + ") REFERENCES student(_id), " 
			+ " FOREIGN KEY("+ DBTables.T4_COLUMN_GROUP_ID + ") REFERENCES teach_group(_id) " 
			+"); ";
	
	public OpenHelper(Context context, String name, int version) {
		super(context, name, null, version);

	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(query0);
		db.execSQL(query1);
		db.execSQL(query2);
		db.execSQL(query3);
		db.execSQL(query4);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("PRAGMA foreign_keys=OFF;");
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T0_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T1_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T2_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T3_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T4_TABLE_NAME);
		onCreate(db);
	}

	public static void clearAll(SQLiteDatabase db){
		db.execSQL("PRAGMA foreign_keys=OFF;");
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T0_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T1_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T2_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T3_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTables.T4_TABLE_NAME);
		db.execSQL(query0);
		db.execSQL(query1);
		db.execSQL(query2);
		db.execSQL(query3);
		db.execSQL(query4);
		db.execSQL("PRAGMA foreign_keys=ON;");
		
	}
	
}
