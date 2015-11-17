package ru.itx.conduit;

import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ReplicaModel {
	@Attribute
	private Date save_date;
	@ElementList
	private List<ASecurity> list_asecurity;
	@ElementList
	private List<Student> list_student;
	@ElementList
	private List<Group> list_group;
	@ElementList
	private List<GroupList> list_group_list;
	public List<GroupList> getList_group_list() {
		return list_group_list;
	}
	public void setList_group_list(List<GroupList> list_group_list) {
		this.list_group_list = list_group_list;
	}
	@ElementList
	private List<Account> list_account;
	public Date getSave_date() {
		return save_date;
	}
	public void setSave_date(Date save_date) {
		this.save_date = save_date;
	}
	public List<ASecurity> getList_asecurity() {
		return list_asecurity;
	}
	public void setList_asecurity(List<ASecurity> list_asecurity) {
		this.list_asecurity = list_asecurity;
	}
	public List<Student> getList_student() {
		return list_student;
	}
	public void setList_student(List<Student> list_student) {
		this.list_student = list_student;
	}
	public List<Group> getList_group() {
		return list_group;
	}
	public void setList_group(List<Group> list_group) {
		this.list_group = list_group;
	}
	public List<Account> getList_account() {
		return list_account;
	}
	public void setList_account(List<Account> list_account) {
		this.list_account = list_account;
	}
}
