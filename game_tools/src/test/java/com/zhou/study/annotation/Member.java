package com.zhou.study.annotation;
@DBTable(name = "member")
public class Member {
	@SQLString(30) String firstName;
	@SQLString(50) String lastName;
	@SQLInteger() int age;
	@SQLString(value=30,constraints = @Constraints(primaryKey=true)) String handle;
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public int getAge() {
		return age;
	}
	public String getHandle() {
		return handle;
	}
	
}
