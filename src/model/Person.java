package model;

public class Person implements Resource {
	private int id; 		// force yourself to use the method. 
	public String name; // public is faster than private with a getter.

	public int id() {
		return id;
	}

}