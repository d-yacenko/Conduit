package ru.itx.conduit;

public class RowGroupListModel {
	private Student student;
	private boolean included;
	public RowGroupListModel(Student student,boolean included) {
		this.student=student;
		this.included=included;
	}
	public RowGroupListModel(Student student) {
		this.student=student;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public boolean isIncluded() {
		return included;
	}
	public void setIncluded(boolean included) {
		this.included = included;
	}
	
	
}
