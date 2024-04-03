package Model;

public class SinhVien {
 public String name;
 public float diem;
 public int age;
 
public SinhVien(String name, float diem, int age) {
	super();
	this.name = name;
	this.diem = diem;
	this.age = age;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public float getDiem() {
	return diem;
}
public void setDiem(float diem) {
	this.diem = diem;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
 
}
