package ru.itx.conduit;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Account {
	@Attribute
	private int _id;
	@Element(required=false)
	private String subject;
	@Element
	private long date;
	@Element
	private int student_id;
	@Element
	private int teach_group_id;
	@Element(required=false)
	private String value;
	
	public int get_id() {
		return _id;
	}
	//public Account(int _id, String subject, long date, int student_id, int teach_group_id,  String value) {
	public Account(@Attribute(name="_id") int _id, @Element(name="subject") String subject,
			@Element(name="date") long date, @Element(name ="student_id") int student_id,
			@Element(name="teach_group_id") int teach_group_id, @Element(name="value") String value) {
		super();
		this._id = _id;
		this.subject = subject;
		this.date = date;
		this.student_id = student_id;
		this.teach_group_id = teach_group_id;
		this.value = value;
	}
	
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}
	public int getTeach_group_id() {
		return teach_group_id;
	}
	public void setTeach_group_id(int teach_group_id) {
		this.teach_group_id = teach_group_id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
 }