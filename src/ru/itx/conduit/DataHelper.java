package ru.itx.conduit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import ru.itx.conduit.exceptions.DBConstraintStudentFieldException;
import ru.itx.conduit.ui.MainActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataHelper {
	public static final String DB_NAME = "conduit.db";

	private static DataHelper DH;

	private SecretKey DBP;

	public static DataHelper getDH() {
		return DH;
	}

	public void setDBP(SecretKey DBP) {
		this.DBP = DBP;
	}

	private String code(String s) {
		return ASecurity._code(DBP, s);
	}

	private String decode(String s) {
		return ASecurity._decode(DBP, s);
	}

	private byte[] code(byte[] s) {
		return ASecurity._code(DBP, s);
	}

	private byte[] decode(byte[] s) {
		return ASecurity._decode(DBP, s);
	}

	public static final String INSERT_T0 = "INSERT INTO "
			+ DBTables.T0_TABLE_NAME + " (" + DBTables.T0_COLUMN_PASSWORD + ","
			+ DBTables.T0_COLUMN_DB_PASSWORD + ","
			+ DBTables.T0_COLUMN_REPLICA_ID + ") values (?,?,?); ";
	public static final String INSERT_T0_1 = "INSERT INTO "
			+ DBTables.T0_TABLE_NAME + " (" + DBTables.T0_COLUMN_ID + ","
			+ DBTables.T0_COLUMN_PASSWORD + ","
			+ DBTables.T0_COLUMN_DB_PASSWORD + ","
			+ DBTables.T0_COLUMN_REPLICA_ID + ") values (?,?,?,?); ";

	public static final String INSERT_T1 = "INSERT INTO "
			+ DBTables.T1_TABLE_NAME + " (" + DBTables.T1_COLUMN_NAME + ","
			+ DBTables.T1_COLUMN_LASTNAME + "," + DBTables.T1_COLUMN_SURNAME
			+ "," + DBTables.T1_COLUMN_EMAIL + "," + DBTables.T1_COLUMN_PHONE
			+ "," + DBTables.T1_COLUMN_PARENTPHONE + ","
			+ DBTables.T1_COLUMN_BIRTHDAY + "," + DBTables.T1_COLUMN_NUMCLASS
			+ "," + DBTables.T1_COLUMN_HIDDEN + "," + DBTables.T1_COLUMN_PHOTO
			+ ") values (?,?,?,?,?,?,?,?,?,?); ";
	public static final String INSERT_T1_1 = "INSERT INTO "
			+ DBTables.T1_TABLE_NAME + " (" + DBTables.T1_COLUMN_ID + ","
			+ DBTables.T1_COLUMN_NAME + "," + DBTables.T1_COLUMN_LASTNAME + ","
			+ DBTables.T1_COLUMN_SURNAME + "," + DBTables.T1_COLUMN_EMAIL + ","
			+ DBTables.T1_COLUMN_PHONE + "," + DBTables.T1_COLUMN_PARENTPHONE
			+ "," + DBTables.T1_COLUMN_BIRTHDAY + ","
			+ DBTables.T1_COLUMN_NUMCLASS + "," + DBTables.T1_COLUMN_HIDDEN
			+ "," + DBTables.T1_COLUMN_PHOTO
			+ ") values (?,?,?,?,?,?,?,?,?,?,?); ";

	public static final String INSERT_T2 = "INSERT INTO "
			+ DBTables.T2_TABLE_NAME + " (" + DBTables.T2_COLUMN_NAME + ","
			+ DBTables.T2_COLUMN_CITY + "," + DBTables.T2_COLUMN_TEACHER + ","
			+ DBTables.T2_COLUMN_ADDITIONAL + ","
			+ DBTables.T2_COLUMN_TIMETABLE + ") values (?,?,?,?,?); ";
	public static final String INSERT_T2_1 = "INSERT INTO "
			+ DBTables.T2_TABLE_NAME + " (" + DBTables.T2_COLUMN_ID + ","
			+ DBTables.T2_COLUMN_NAME + "," + DBTables.T2_COLUMN_CITY + ","
			+ DBTables.T2_COLUMN_TEACHER + "," + DBTables.T2_COLUMN_ADDITIONAL
			+ "," + DBTables.T2_COLUMN_TIMETABLE + ") values (?,?,?,?,?,?); ";

	public static final String INSERT_T3 = "INSERT INTO "
			+ DBTables.T3_TABLE_NAME + " (" + DBTables.T3_COLUMN_TEACH_GROUP_ID
			+ "," + DBTables.T3_COLUMN_STUDENT_ID + ") values (?,?); ";
	public static final String INSERT_T3_1 = "INSERT INTO "
			+ DBTables.T3_TABLE_NAME + " (" + DBTables.T3_COLUMN_ID + ","
			+ DBTables.T3_COLUMN_TEACH_GROUP_ID + ","
			+ DBTables.T3_COLUMN_STUDENT_ID + ") values (?,?,?); ";

	public static final String INSERT_T4 = "INSERT INTO "
			+ DBTables.T4_TABLE_NAME + " (" + DBTables.T4_COLUMN_SUBJECT + ","
			+ DBTables.T4_COLUMN_DATE + "," + DBTables.T4_COLUMN_STUDENT_ID
			+ "," + DBTables.T4_COLUMN_GROUP_ID + ","
			+ DBTables.T4_COLUMN_VALUE + ") values (?,?,?,?,?); ";
	public static final String INSERT_T4_1 = "INSERT INTO "
			+ DBTables.T4_TABLE_NAME + " (" + DBTables.T4_COLUMN_ID + ","
			+ DBTables.T4_COLUMN_SUBJECT + "," + DBTables.T4_COLUMN_DATE + ","
			+ DBTables.T4_COLUMN_STUDENT_ID + "," + DBTables.T4_COLUMN_GROUP_ID
			+ "," + DBTables.T4_COLUMN_VALUE + ") values (?,?,?,?,?,?); ";

	private SQLiteDatabase db;
	private Context context;
	private SQLiteStatement stmt;
	private ContentValues cv;

	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context, DB_NAME,
				DBTables.DB_VERSION);
		this.db = openHelper.getWritableDatabase();
		DH = this;
	}

	private void bind(int num, Object obj) {
		bind(num, obj, true);
	}

	private void bind(int num, Object obj, boolean encoded) {
		if (obj == null) {
			stmt.bindNull(num);
			return;
		}
		String objClass = obj.getClass().getSimpleName();
		switch (objClass) {
		case "String":
			String str = ((String) obj).trim();
			if (str.equals(""))
				stmt.bindNull(num);
			else
				stmt.bindString(num, encoded ? code(str) : str);
			break;
		case "Date":
			stmt.bindLong(num, ((Date) obj).getTime());
			break;
		case "byte[]":
			stmt.bindBlob(num, encoded ? code((byte[]) obj) : (byte[]) obj);
			break;
		case "Integer":
			stmt.bindLong(num, (Integer) obj);
			break;
		case "Long":
			stmt.bindLong(num, (Long) obj);
			break;
		case "Boolean":
			str = String.valueOf((Boolean) obj);
			stmt.bindString(num, encoded ? code(str) : str);
			break;
		default:
			break;
		}
	}

	private void bind(String field, Object obj) {
		if (obj == null)
			return;
		String objClass = obj.getClass().getSimpleName();
		switch (objClass) {
		case "String":
			String str = ((String) obj).trim();
			if (str.equals(""))
				return;
			cv.put(field, code(str));
			break;
		case "Date":
			cv.put(field, ((Date) obj).getTime());
			break;
		case "byte[]":
			cv.put(field, code((byte[]) obj));
			break;
		case "Integer":
			cv.put(field, (Integer) obj);
			break;
		case "Long":
			cv.put(field, (Long) obj);
			break;
		case "Boolean":
			cv.put(field, code(String.valueOf((Boolean) obj)));
			break;
		default:
			break;
		}
	}

	class _Student extends Student {
		public _Student(Cursor cursor) {
			super(cursor.getInt(0), decode(cursor.getString(1)), decode(cursor
					.getString(2)), decode(cursor.getString(3)), decode(cursor
					.getString(4)), decode(cursor.getString(5)), decode(cursor
					.getString(6)), cursor.getLong(7) == 0 ? null : new Date(
					cursor.getLong(7)), decode(cursor.getString(8)), Boolean
					.valueOf(decode(cursor.getString(9))), decode(cursor
					.getBlob(10)));
		}
	}

	class _Group extends Group {
		public _Group(Cursor cursor) {
			super(cursor.getInt(0), decode(cursor.getString(1)), decode(cursor
					.getString(2)), decode(cursor.getString(3)), decode(cursor
					.getString(4)), decode(cursor.getString(5)));
		}
	}

	// ==============================================================
	//
	// Student
	//
	// ==============================================================
	// List==========================================================
	public List<Student> selectAll_student() {
		List<Student> list = new ArrayList<Student>();
		Cursor cursor = db.query(DBTables.T1_TABLE_NAME, new String[] {
				DBTables.T1_COLUMN_ID, DBTables.T1_COLUMN_NAME,
				DBTables.T1_COLUMN_LASTNAME, DBTables.T1_COLUMN_SURNAME,
				DBTables.T1_COLUMN_EMAIL, DBTables.T1_COLUMN_PHONE,
				DBTables.T1_COLUMN_PARENTPHONE, DBTables.T1_COLUMN_BIRTHDAY,
				DBTables.T1_COLUMN_NUMCLASS, DBTables.T1_COLUMN_HIDDEN,
				DBTables.T1_COLUMN_PHOTO }, null, null, null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new _Student(cursor));
			} while (cursor.moveToNext());
		return list;
	}

	public List<Student> replicaStudent() {
		List<Student> list = new ArrayList<Student>();
		Cursor cursor = db.query(DBTables.T1_TABLE_NAME, new String[] {
				DBTables.T1_COLUMN_ID, DBTables.T1_COLUMN_NAME,
				DBTables.T1_COLUMN_LASTNAME, DBTables.T1_COLUMN_SURNAME,
				DBTables.T1_COLUMN_EMAIL, DBTables.T1_COLUMN_PHONE,
				DBTables.T1_COLUMN_PARENTPHONE, DBTables.T1_COLUMN_BIRTHDAY,
				DBTables.T1_COLUMN_NUMCLASS, DBTables.T1_COLUMN_HIDDEN,
				DBTables.T1_COLUMN_PHOTO }, null, null, null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new Student(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3), cursor
								.getString(4), cursor.getString(5), cursor
								.getString(6), cursor.getLong(7) == 0 ? null
								: new Date(cursor.getLong(7)), cursor
								.getString(8), cursor.getString(9), cursor
								.getBlob(10)));
			} while (cursor.moveToNext());
		return list;
	}

	// Insert========================================================
	public long insert(Student s) throws DBConstraintStudentFieldException {
		stmt = db.compileStatement(INSERT_T1);
		bind(1, s.getName());
		bind(2, s.getLastName());
		bind(3, s.getSurName());
		bind(4, s.getEmail());
		bind(5, s.getPhone());
		bind(6, s.getParentPhone());
		bind(7, s.getBirthDate());
		bind(8, s.getNumClass());
		bind(9, s.isHidden());
		bind(10, s.getPhoto());
		int rez=-1;
		try {
			stmt.executeInsert();
		} catch(SQLiteConstraintException e){
			throw new DBConstraintStudentFieldException(MainActivity.CONTEXT);
			//throw new DBConstraintStudentFieldException();
		}

		return rez;
	}

	public void replicaStudent(List<Student> list) {
		db.execSQL("PRAGMA foreign_keys=OFF;");
		for (Student s : list) {
			stmt = db.compileStatement(INSERT_T1_1);
			bind(1, s.get_id(), false);
			bind(2, s.getName(), false);
			bind(3, s.getLastName(), false);
			bind(4, s.getSurName(), false);
			bind(5, s.getEmail(), false);
			bind(6, s.getPhone(), false);
			bind(7, s.getParentPhone(), false);
			bind(8, s.getBirthDate(), false);
			bind(9, s.getNumClass(), false);
			bind(10, s.s_hidden, false);
			bind(11, s.getPhoto(), false);
			stmt.executeInsert();
		}
		db.execSQL("PRAGMA foreign_keys=ON;");
	}

	// Delete========================================================
	public long delete(Student s) {
		return db.delete(DBTables.T1_TABLE_NAME, "_id=" + s.get_id(), null);
	}

	// Update========================================================
	public long update(Student s) {
		cv = new ContentValues();
		bind(DBTables.T1_COLUMN_NAME, s.getName());
		bind(DBTables.T1_COLUMN_LASTNAME, s.getLastName());
		bind(DBTables.T1_COLUMN_SURNAME, s.getSurName());
		bind(DBTables.T1_COLUMN_EMAIL, s.getEmail());
		bind(DBTables.T1_COLUMN_PHONE, s.getPhone());
		bind(DBTables.T1_COLUMN_PARENTPHONE, s.getParentPhone());
		bind(DBTables.T1_COLUMN_BIRTHDAY, s.getBirthDate());
		bind(DBTables.T1_COLUMN_NUMCLASS, s.getNumClass());
		bind(DBTables.T1_COLUMN_HIDDEN, s.isHidden());
		bind(DBTables.T1_COLUMN_PHOTO, s.getPhoto());
		return db.update(DBTables.T1_TABLE_NAME, cv, "_id=" + s.get_id(), null);

	}

	// ==============================================================
	//
	// Group
	//
	// ==============================================================

	// List==========================================================
	public List<Group> selectAll_group() {
		List<Group> list = new ArrayList<Group>();
		Cursor cursor = db.query(DBTables.T2_TABLE_NAME, new String[] {
				DBTables.T2_COLUMN_ID, DBTables.T2_COLUMN_NAME,
				DBTables.T2_COLUMN_CITY, DBTables.T2_COLUMN_TEACHER,
				DBTables.T2_COLUMN_ADDITIONAL, DBTables.T2_COLUMN_TIMETABLE },
				null, null, null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new _Group(cursor));
			} while (cursor.moveToNext());
		return list;
	}

	public List<Group> replicaGroup() {
		List<Group> list = new ArrayList<Group>();
		Cursor cursor = db.query(DBTables.T2_TABLE_NAME, new String[] {
				DBTables.T2_COLUMN_ID, DBTables.T2_COLUMN_NAME,
				DBTables.T2_COLUMN_CITY, DBTables.T2_COLUMN_TEACHER,
				DBTables.T2_COLUMN_ADDITIONAL, DBTables.T2_COLUMN_TIMETABLE },
				null, null, null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new Group(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3), cursor
								.getString(4), cursor.getString(5)));
			} while (cursor.moveToNext());
		return list;
	}

	// Insert========================================================
	public long insert(Group g) {
		stmt = db.compileStatement(INSERT_T2);
		bind(1, g.getName());
		bind(2, g.getCity());
		bind(3, g.getTeacher());
		bind(4, g.getAdditional());
		bind(5, g.getTimetable());
		return stmt.executeInsert();
	}

	public void replicaGroup(List<Group> list) {
		db.execSQL("PRAGMA foreign_keys=OFF;");
		for (Group g : list) {
			stmt = db.compileStatement(INSERT_T2_1);
			bind(1, g.get_id(), false);
			bind(2, g.getName(), false);
			bind(3, g.getCity(), false);
			bind(4, g.getTeacher(), false);
			bind(5, g.getAdditional(), false);
			bind(6, g.getTimetable(), false);
			stmt.executeInsert();
		}
		db.execSQL("PRAGMA foreign_keys=ON;");
	}

	// Delete========================================================
	public long delete(Group g) {
		return db.delete(DBTables.T2_TABLE_NAME, "_id=" + g.get_id(), null);
	}

	// Update========================================================
	public long update(Group g) {
		cv = new ContentValues();
		bind(DBTables.T2_COLUMN_NAME, g.getName());
		bind(DBTables.T2_COLUMN_CITY, g.getCity());
		bind(DBTables.T2_COLUMN_TEACHER, g.getTeacher());
		bind(DBTables.T2_COLUMN_ADDITIONAL, g.getAdditional());
		bind(DBTables.T2_COLUMN_TIMETABLE, g.getTimetable());
		return db.update(DBTables.T2_TABLE_NAME, cv, "_id=" + g.get_id(), null);
	}

	// ==============================================================
	//
	// GroupList
	//
	// ==============================================================

	// Delete========================================================
	public long deleteAllGrolupMember(Group group) {
		return this.db.delete(DBTables.T3_TABLE_NAME,
				DBTables.T3_COLUMN_TEACH_GROUP_ID + "=" + group.get_id(), null);
	}

	// Insert========================================================
	public long insertGroupMember(Group group, Student student) {
		stmt = db.compileStatement(INSERT_T3);
		bind(1, group.get_id());
		bind(2, student.get_id());
		return stmt.executeInsert();
	}

	public void replicaGroupList(List<GroupList> list) {
		db.execSQL("PRAGMA foreign_keys=OFF;");
		for (GroupList gl : list) {
			stmt = db.compileStatement(INSERT_T3_1);
			bind(1, gl.get_id(), false);
			bind(2, gl.getTeach_group_id(), false);
			bind(3, gl.getStudent_id(), false);
			stmt.executeInsert();
		}
		db.execSQL("PRAGMA foreign_keys=ON;");
	}

	// List==========================================================
	public List<Student> selectAll_studentForGroup(Group group) {
		List<Student> list = new ArrayList<Student>();
		Cursor cursor = db.query(DBTables.T1_TABLE_NAME + " as ST inner join "
				+ DBTables.T3_TABLE_NAME + " as GR on ST._id = GR.student_id",
				new String[] { "ST." + DBTables.T1_COLUMN_ID,
						"ST." + DBTables.T1_COLUMN_NAME,
						"ST." + DBTables.T1_COLUMN_LASTNAME,
						"ST." + DBTables.T1_COLUMN_SURNAME,
						"ST." + DBTables.T1_COLUMN_EMAIL,
						"ST." + DBTables.T1_COLUMN_PHONE,
						"ST." + DBTables.T1_COLUMN_PARENTPHONE,
						"ST." + DBTables.T1_COLUMN_BIRTHDAY,
						"ST." + DBTables.T1_COLUMN_NUMCLASS,
						"ST." + DBTables.T1_COLUMN_HIDDEN,
						"ST." + DBTables.T1_COLUMN_PHOTO },
				"GR.teach_group_id = ?",
				new String[] { String.valueOf(group.get_id()) }, null, null,
				null);
		if (cursor.moveToFirst())
			do {
				list.add(new _Student(cursor));
			} while (cursor.moveToNext());
		return list;
	}

	public List<GroupList> replicaGroupList() {
		List<GroupList> list = new ArrayList<GroupList>();
		Cursor cursor = db.query(DBTables.T3_TABLE_NAME, new String[] {
				DBTables.T3_COLUMN_ID, DBTables.T3_COLUMN_TEACH_GROUP_ID,
				DBTables.T3_COLUMN_STUDENT_ID }, null, null, null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new GroupList(cursor.getInt(0), cursor.getInt(1),
						cursor.getInt(2)));
			} while (cursor.moveToNext());
		return list;

	}

	// ==============================================================
	//
	// Account
	//
	// ==============================================================
	// Insert========================================================
	public int insertAccountRecords(long date, String subject,
			List<RowAccountRecordModel> list) {
		int rez = 0;
		for (RowAccountRecordModel r : list) {
			stmt = db.compileStatement(INSERT_T4);
			bind(1, subject);
			bind(2, date);
			bind(3, r.getStudent().get_id());
			bind(4, r.getGroup().get_id());
			bind(5, r.getValue());
			stmt.executeInsert();
			rez++;
		}
		return rez;
	}

	public void replicaAccount(List<Account> list) {
		db.execSQL("PRAGMA foreign_keys=OFF;");
		for (Account a : list) {
			stmt = db.compileStatement(INSERT_T4_1);
			bind(1, a.get_id(), false);
			bind(2, a.getSubject(), false);
			bind(3, a.getDate(), false);
			bind(4, a.getStudent_id(), false);
			bind(5, a.getTeach_group_id(), false);
			bind(6, a.getValue(), false);
			stmt.executeInsert();
		}
		db.execSQL("PRAGMA foreign_keys=ON;");
	}

	// List==========================================================
	public int count_AccountLesson() {
		int rez = 0;
		Cursor cursor = this.db.query(DBTables.T4_TABLE_NAME, new String[] {
				DBTables.T4_COLUMN_SUBJECT, DBTables.T4_COLUMN_DATE }, null,
				null, DBTables.T4_COLUMN_DATE + ","
						+ DBTables.T4_COLUMN_SUBJECT, null, null);
		if (cursor.moveToFirst())
			rez = cursor.getCount();
		return rez;
	}

	public List<AccountModel> selectAll_Account(Group group) {
		List<AccountModel> list = null;
		class Lesson {
			long date;
			String subject;

			public Lesson(String subject, long date) {
				this.date = date;
				this.subject = subject;
			}
		}

		List<Lesson> lessons = new ArrayList<Lesson>();
		Cursor cursor = this.db.query(DBTables.T4_TABLE_NAME, new String[] {
				DBTables.T4_COLUMN_SUBJECT, DBTables.T4_COLUMN_DATE },
				DBTables.T4_COLUMN_GROUP_ID + "= ?",
				new String[] { String.valueOf(group.get_id()) },
				DBTables.T4_COLUMN_DATE + "," + DBTables.T4_COLUMN_SUBJECT,
				null, DBTables.T4_COLUMN_DATE + ","
						+ DBTables.T4_COLUMN_SUBJECT);
		if (cursor.moveToFirst())
			do {
				lessons.add(new Lesson(cursor.getString(0), Long.valueOf(cursor
						.getString(1))));
			} while (cursor.moveToNext());
		if (lessons.size() == 0)
			return null;
		list = new ArrayList<AccountModel>();
		for (int i = 0; i < lessons.size(); i++) {
			AccountModel account = new AccountModel(lessons.get(i).subject,
					lessons.get(i).date, new ArrayList<RowAccountRecordModel>());
			if (lessons.get(i).subject != null) {
				cursor = this.db.query(
						DBTables.T4_TABLE_NAME + " as AC inner join "
								+ DBTables.T1_TABLE_NAME
								+ " as ST on ST._id = AC."
								+ DBTables.T4_COLUMN_STUDENT_ID,
						new String[] { "ST." + DBTables.T1_COLUMN_ID,
								"ST." + DBTables.T1_COLUMN_NAME,
								"ST." + DBTables.T1_COLUMN_LASTNAME,
								"ST." + DBTables.T1_COLUMN_SURNAME,
								"ST." + DBTables.T1_COLUMN_EMAIL,
								"ST." + DBTables.T1_COLUMN_PHONE,
								"ST." + DBTables.T1_COLUMN_PARENTPHONE,
								"ST." + DBTables.T1_COLUMN_BIRTHDAY,
								"ST." + DBTables.T1_COLUMN_NUMCLASS,
								"ST." + DBTables.T1_COLUMN_HIDDEN,
								"ST." + DBTables.T1_COLUMN_PHOTO,
								"AC." + DBTables.T4_COLUMN_VALUE },
						"AC." + DBTables.T4_COLUMN_SUBJECT + "=? and AC."
								+ DBTables.T4_COLUMN_DATE + "=? and AC."
								+ DBTables.T4_COLUMN_GROUP_ID + "=? ",
						new String[] { lessons.get(i).subject,
								String.valueOf(lessons.get(i).date),
								String.valueOf(group.get_id()) }, null, null,
						null);
			} else {
				cursor = this.db.query(DBTables.T4_TABLE_NAME
						+ " as AC inner join " + DBTables.T1_TABLE_NAME
						+ " as ST on ST._id = AC."
						+ DBTables.T4_COLUMN_STUDENT_ID, new String[] {
						"ST." + DBTables.T1_COLUMN_ID,
						"ST." + DBTables.T1_COLUMN_NAME,
						"ST." + DBTables.T1_COLUMN_LASTNAME,
						"ST." + DBTables.T1_COLUMN_SURNAME,
						"ST." + DBTables.T1_COLUMN_EMAIL,
						"ST." + DBTables.T1_COLUMN_PHONE,
						"ST." + DBTables.T1_COLUMN_PARENTPHONE,
						"ST." + DBTables.T1_COLUMN_BIRTHDAY,
						"ST." + DBTables.T1_COLUMN_NUMCLASS,
						"ST." + DBTables.T1_COLUMN_HIDDEN,
						"ST." + DBTables.T1_COLUMN_PHOTO,
						"AC." + DBTables.T4_COLUMN_VALUE }, "AC."
						+ DBTables.T4_COLUMN_SUBJECT + " is null and AC."
						+ DBTables.T4_COLUMN_DATE + "=? and AC."
						+ DBTables.T4_COLUMN_GROUP_ID + "=? ",
						new String[] { String.valueOf(lessons.get(i).date),
								String.valueOf(group.get_id()) }, null, null,
						null);
			}
			if (cursor.moveToFirst())
				do {
					account.list.add(new RowAccountRecordModel(new _Student(
							cursor), group, decode(cursor.getString(11))));
				} while (cursor.moveToNext());
			account.subject = decode(account.subject);
			list.add(account);
		}
		return list;
	}

	public List<Account> replicaAccount() {
		List<Account> list = new ArrayList<Account>();
		Cursor cursor = db.query(DBTables.T4_TABLE_NAME, new String[] {
				DBTables.T4_COLUMN_ID, DBTables.T4_COLUMN_SUBJECT,
				DBTables.T4_COLUMN_DATE, DBTables.T4_COLUMN_STUDENT_ID,
				DBTables.T4_COLUMN_GROUP_ID, DBTables.T4_COLUMN_VALUE }, null,
				null, null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new Account(cursor.getInt(0), cursor.getString(1),
						cursor.getLong(2), cursor.getInt(3), cursor.getInt(4),
						cursor.getString(5)));
			} while (cursor.moveToNext());
		return list;
	}

	// Delete========================================================
	public int delete(AccountModel account) {
		int rez = 0;

		for (RowAccountRecordModel r : account.list)
			if (account.subject != null)
				rez += db.delete(
						DBTables.T4_TABLE_NAME,
						DBTables.T4_COLUMN_SUBJECT + " = ? and "
								+ DBTables.T4_COLUMN_DATE + " = ? and "
								+ DBTables.T4_COLUMN_STUDENT_ID + " = ? ",
						new String[] { code(account.subject),
								String.valueOf(account.date),
								String.valueOf(r.getStudent().get_id()) });
			else
				rez += db.delete(
						DBTables.T4_TABLE_NAME,
						DBTables.T4_COLUMN_SUBJECT + " is null and "
								+ DBTables.T4_COLUMN_DATE + " = ? and "
								+ DBTables.T4_COLUMN_STUDENT_ID + " = ? ",
						new String[] { String.valueOf(account.date),
								String.valueOf(r.getStudent().get_id()) });
		return rez;
	}

	// Update========================================================
	public int editAccountRecords(long old_ldate, String old_subj, long ldate,
			String subj, List<RowAccountRecordModel> list) {
		int rez = 0;
		for (RowAccountRecordModel r : list) {
			cv = new ContentValues();
			bind(DBTables.T4_COLUMN_SUBJECT, subj);
			bind(DBTables.T4_COLUMN_DATE, ldate);
			bind(DBTables.T4_COLUMN_VALUE, r.getValue());
			if (old_subj != null) {
				rez += db.update(
						DBTables.T4_TABLE_NAME,
						cv,
						DBTables.T4_COLUMN_SUBJECT + "= ? and "
								+ DBTables.T4_COLUMN_DATE + "= ? and "
								+ DBTables.T4_COLUMN_STUDENT_ID + "=? ",
						new String[] { code(old_subj),
								String.valueOf(old_ldate),
								String.valueOf(r.getStudent().get_id()) });
			} else { 
				rez += db.update(
						DBTables.T4_TABLE_NAME,
						cv,
						DBTables.T4_COLUMN_SUBJECT + " is null and "
								+ DBTables.T4_COLUMN_DATE + "= ? and "
								+ DBTables.T4_COLUMN_STUDENT_ID + "=? ",
						new String[] { String.valueOf(old_ldate),
								String.valueOf(r.getStudent().get_id()) });
			}
		}

		return rez;
	}

	// ==============================================================
	//
	// Security
	//
	// ==============================================================

	// List==========================================================
	public List<ASecurity> selectAll_security() {
		List<ASecurity> list = new ArrayList<ASecurity>();
		Cursor cursor = db.query(DBTables.T0_TABLE_NAME, null, null, null,
				null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new ASecurity(cursor.getLong(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3)));
			} while (cursor.moveToNext());
		return list;
	}

	public List<ASecurity> replicaASecurity() {
		return selectAll_security();
	}

	public ASecurity selectAll_security(String cryptoHash) {
		List<ASecurity> list = new ArrayList<ASecurity>();
		Cursor cursor = this.db.query(DBTables.T0_TABLE_NAME, null,
				DBTables.T0_COLUMN_PASSWORD + "=?",
				new String[] { cryptoHash }, null, null, null);
		if (cursor.moveToFirst())
			do {
				list.add(new ASecurity(cursor.getLong(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3)));
			} while (cursor.moveToNext());
		return list.size() == 0 ? null : list.get(0);
	}

	// Insert========================================================
	public long insert(ASecurity security) {
		stmt = db.compileStatement(INSERT_T0);
		stmt.bindString(1, security.getPassword());
		stmt.bindString(2, security.getDbPassword());
		stmt.bindString(3, security.getReplicaId());
		return stmt.executeInsert();
	}

	public void replicaASecurity(List<ASecurity> list) {
		db.execSQL("PRAGMA foreign_keys=OFF;");
		for (ASecurity as : list) {
			stmt = db.compileStatement(INSERT_T0_1);
			stmt.bindLong(1, as.get_id());
			stmt.bindString(2, as.getPassword());
			stmt.bindString(3, as.getDbPassword());
			if (as.getReplicaId() != null)
				stmt.bindString(4, as.getReplicaId());
			stmt.executeInsert();
		}
		db.execSQL("PRAGMA foreign_keys=ON;");
	}

	// Delete========================================================
	public long delete(ASecurity security) {
		return this.db.delete(DBTables.T0_TABLE_NAME,
				DBTables.T0_COLUMN_PASSWORD + "= ?",
				new String[] { security.getPassword() });
	}

	// Update========================================================
	public long update(long _id, ASecurity security) {
		cv = new ContentValues();
		cv.put(DBTables.T0_COLUMN_PASSWORD, security.getPassword());
		cv.put(DBTables.T0_COLUMN_DB_PASSWORD, security.getDbPassword());
		cv.put(DBTables.T0_COLUMN_REPLICA_ID, security.getReplicaId());
		return db.update(DBTables.T0_TABLE_NAME, cv, "_id=" + _id, null);

	}

	public long setReplica(String replica_id) {
		cv = new ContentValues();
		cv.put(DBTables.T0_COLUMN_REPLICA_ID, replica_id);
		return db.update(DBTables.T0_TABLE_NAME, cv, null, null);

	}

	// ==============================================================
	//
	// Other
	//
	// ==============================================================
	public void clearAll() {
		OpenHelper.clearAll(db);
	}
}
