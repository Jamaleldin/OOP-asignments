package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.util.Scanner;

public class Main {

	public static void main(String[] args){
		Scanner scn = new Scanner(System.in);
		String s;
		Controller control = new Controller(new CDataBase());
		while (!(s = scn.nextLine()).equalsIgnoreCase("exit")) {
			control.orderExcuter(s);
		}
		scn.close();
	}
}
