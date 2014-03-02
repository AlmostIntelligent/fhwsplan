package de.almostintelligent.fhwsplan.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LectureSQLiteOpenHelper extends SQLiteOpenHelper
{
	public static final String	DATABASE_NAME		= "lectures.sqlite";
	public static final int		DATABASE_VERSION	= 1;

	public LectureSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// TODO Auto-generated method stub
		Log.e("LectureSQLiteOpenHelper.onCreate.db.readonly",
				String.valueOf(db.isReadOnly()));
		createDays(db);
		createEmployees(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

	private void createTable(StringBuilder builder, SQLiteDatabase db)
	{
		db.execSQL(builder.toString());
	}

	private void createDays(SQLiteDatabase db)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE days (");
		builder.append("_id        INTEGER PRIMARY KEY NOT NULL,");
		builder.append("xmlid      INTEGER             NOT NULL,");
		builder.append("short_name TEXT                NOT NULL,");
		builder.append("long_name  TEXT                NOT NULL");
		builder.append(");");

		createTable(builder, db);
	}

	private void createFaculties(SQLiteDatabase db)
	{
		{
			StringBuilder builder = new StringBuilder();
			builder.append("CREATE TABLE faculties (");
			builder.append("_id        INTEGER PRIMARY KEY NOT NULL,");
			builder.append("xmlid      INTEGER             NOT NULL,");
			builder.append("short_name TEXT                NOT NULL,");
			builder.append("long_name  TEXT                NOT NULL");
			builder.append(");");

			createTable(builder, db);
		}

		{
			StringBuilder builder = new StringBuilder();
			builder.append("CREATE TABLE faculties_has_semester (");
			builder.append("_id        INTEGER PRIMARY KEY NOT NULL,");
			builder.append("facultyid  INTEGER             NOT NULL,");
			builder.append("semester   INTEGER             NOT NULL");
			builder.append(");");
			
			createTable(builder, db);
		}
	}

	private void createEmployees(SQLiteDatabase db)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE employees (");
		builder.append("_id        INTEGER PRIMARY KEY NOT NULL,");
		builder.append("xmlid      INTEGER             NOT NULL,");
		builder.append("prename    TEXT                NOT NULL,");
		builder.append("surname    TEXT                NOT NULL");
		builder.append("token      TEXT                NOT NULL");
		builder.append(");");

		createTable(builder, db);
	}

}
