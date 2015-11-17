package ru.itx.conduit;

public class RowAccountRecordModel {
	private Student student;
	private String value;
	private Group group;
	
	public RowAccountRecordModel(Student student,Group group) {
		this(student,group,"");
	}
	public RowAccountRecordModel(Student student,Group group,  String value) {
		this.student=student;
		this.group=group;
		this.value=value;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
