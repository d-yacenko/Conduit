package ru.itx.conduit;

import java.util.Date;
import java.util.Locale;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.graphics.Matrix;

@Root
public class Student implements Comparable<Student> {
    @Attribute
    private int _id;
    @Element(required = false)
    private String name, lastName, surName, email, phone, parentPhone, numClass;
    @Element(required = false)
    private byte[] photo;
    @Element(required = false)
    private Date birthDate;
    private boolean hidden;
    @Element(required = false, name = "hidden")
    public String s_hidden;

    @Override
    public boolean equals(Object o) {
        return this._id == ((Student) o).get_id();
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Student(int _id, String name, String lastName, String surName, String email, String phone,
                   String parentPhone, Date birthDate, String numClass, boolean hidden, byte[] photo) {
        this(name, lastName, surName, email, phone, parentPhone, birthDate,
                numClass, hidden, photo);
        this._id = _id;
    }

    public Student(@Attribute(name = "_id") int _id, @Element(name = "name") String name, @Element(name = "lastName") String lastName,
                   @Element(name = "surName") String surName, @Element(name = "email") String email, @Element(name = "phone") String phone,
                   @Element(name = "parentPhone") String parentPhone, @Element(name = "birthDate") Date birthDate,
                   @Element(name = "numClass") String numClass, @Element(name = "hidden") String s_hidden, @Element(name = "photo") byte[] photo) {
        this(name, lastName, surName, email, phone, parentPhone, birthDate,
                numClass, false, photo);
        this._id = _id;
        this.s_hidden = s_hidden;

    }

    public Student(String name, String lastName, String surName, String email,
                   String phone, String parentPhone, Date birthDate, String numClass, boolean hidden, byte[] photo) {
        this.name = name;
        this.lastName = lastName;
        this.surName = surName;
        this.email = email;
        this.phone = phone;
        this.parentPhone = parentPhone;
        this.birthDate = birthDate;
        this.numClass = numClass;
        this.hidden = hidden;
        this.photo = photo;
    }


    public static int _getNumberStudent() {
        return DataHelper.getDH().selectAll_student().size();
    }

    public Student() {
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getNumClass() {
        return numClass;
    }

    public void setNumClass(String numClass) {
        this.numClass = numClass;
    }

    @Override
    public int compareTo(Student another) {
        if (!Locale.getDefault().getCountry().equals("RU"))
            return name.compareTo(another.getName());
        else return surName.compareTo(another.getSurName());
    }

    public String getFullName() {
        if (!Locale.getDefault().getCountry().equals("RU"))
            return name + " " + (lastName == null ? "" : lastName) + " "
                    + surName;
        else return surName + " " + name + " " + (lastName == null ? "" : lastName);
    }
}
