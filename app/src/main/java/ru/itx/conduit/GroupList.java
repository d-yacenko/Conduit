package ru.itx.conduit;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class GroupList {
	@Attribute
	private int _id;
	@Element(required=false)
	private int teach_group_id;
	@Element(required=false)
	private int student_id;
	public GroupList(@Attribute(name="_id") int _id,@Element(name="teach_group_id") int teach_group_id,
			@Element(name="student_id") int student_id) {
		super();
		this._id = _id;
		this.teach_group_id = teach_group_id;
		this.student_id = student_id;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getTeach_group_id() {
		return teach_group_id;
	}
	public void setTeach_group_id(int teach_group_id) {
		this.teach_group_id = teach_group_id;
	}
	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

}
