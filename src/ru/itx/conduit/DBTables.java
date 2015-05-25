package ru.itx.conduit;

public class DBTables {
	public static final int DB_VERSION = 25;

	public static final String T0_TABLE_NAME = "asecurity";
	public static final String T0_COLUMN_ID = "_id";
	public static final String T0_COLUMN_PASSWORD = "PASSWORD"; 		// криптохеш пароля на вход в программу. может быть несколько для нескольких паролей
	public static final String T0_COLUMN_DB_PASSWORD = "DB_PASSWORD";	// зашифрованный входным паролем пароль (изначально случайное число) на расшифровку критичных данных DB
	public static final String T0_COLUMN_REPLICA_ID = "REPLICA_ID"; 		// ID по котрому извлекается реплика с сервера GAE
	

	public static final String T1_TABLE_NAME = "student";
	public static final String T1_COLUMN_ID = "_id";
	public static final String T1_COLUMN_NAME = "NAME";
	public static final String T1_COLUMN_LASTNAME = "LASTNAME";
	public static final String T1_COLUMN_SURNAME = "SURNAME";
	public static final String T1_COLUMN_EMAIL = "EMAIL";
	public static final String T1_COLUMN_PHONE = "PHONE";
	public static final String T1_COLUMN_PARENTPHONE = "PARENTPHONE";
	public static final String T1_COLUMN_BIRTHDAY = "BIRTHDAY";
	public static final String T1_COLUMN_NUMCLASS = "NUMCLASS";
	public static final String T1_COLUMN_HIDDEN = "HIDDEN";
	public static final String T1_COLUMN_PHOTO = "PHOTO";

	public static final String T2_TABLE_NAME = "teach_group";
	public static final String T2_COLUMN_ID = "_id";
	public static final String T2_COLUMN_NAME = "NAME";
	public static final String T2_COLUMN_CITY = "CITY";
	public static final String T2_COLUMN_TEACHER = "TEACHER";
	public static final String T2_COLUMN_ADDITIONAL = "ADDITIONAL";
	public static final String T2_COLUMN_TIMETABLE = "TIMETABLE";

	public static final String T3_TABLE_NAME = "teach_group_list";
	public static final String T3_COLUMN_ID = "_id";
	public static final String T3_COLUMN_TEACH_GROUP_ID = "TEACH_GROUP_ID";
	public static final String T3_COLUMN_STUDENT_ID = "STUDENT_ID";

	public static final String T4_TABLE_NAME = "account";
	public static final String T4_COLUMN_ID = "_id";
	public static final String T4_COLUMN_SUBJECT = "SUBJECT";
	public static final String T4_COLUMN_DATE = "DATE";
	public static final String T4_COLUMN_STUDENT_ID = "STUDENT_ID";
	public static final String T4_COLUMN_GROUP_ID = "TEACH_GROUP_ID";
	public static final String T4_COLUMN_VALUE = "VALUE";
	
}
