package ru.itx.conduit;

import java.util.Date;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.graphics.Matrix;
@Root
public class Group implements Comparable<Group> {
	@Attribute
	private int _id;
	@Element(required=false)
	private String name, city, teacher, additional, timetable;
	
	@Override
	public boolean equals(Object o) {
		return this._id==((Group)o).get_id();
	}

	public Group(@Attribute(name="_id") int _id, @Element(name="name") String name, @Element(name="city") String city, 
			@Element(name="teacher") String teacher, @Element(name="additional") String additional,
			@Element(name="timetable") String timetable) {
		this(name, city, teacher, additional, timetable);
		this._id = _id;

	}
	
	public static int _getNumberGroup(){
		return DataHelper.getDH().selectAll_group().size();
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public String getTimetable() {
		return timetable;
	}

	public void setTimetable(String timetable) {
		this.timetable = timetable;
	}

	public Group(String name, String city, String teacher, String additional,String timetable) {
		this.name = name;
		this.city = city;
		this.teacher = teacher;
		this.additional = additional;
		this.timetable = timetable;
	}

	public Group() {
	}

	@Override
	public int compareTo(Group another) {
		return name.compareTo(another.getName());
	}

}
