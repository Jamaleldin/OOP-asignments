package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Database;

public class Controller {
	Database db;

	public Controller(Database db) {
		this.db = db;
	}

	public void orderExcuter(String query) {
		CheckSyntax check = new CheckSyntax();
		int checker = check.checkSyntax(query);
		if (checker == 2 || checker == 3 || checker == 5) {
			try {
				int count = db.executeUpdateQuery(query);
				System.out.println(count + ((count == 1) ? " row" : " rows")
						+ " had been changed");
			} catch (SQLException | NullPointerException e) {
				System.out.println("Error column not exist or wrong dataType or table not exist!");
			}
		} else if (checker == 0 || checker == 1 || checker == 4 || checker == 7) {
			try {
				System.out.println(db.executeStructureQuery(query));
			} catch (SQLException | NullPointerException e) {
				System.out.println("Error path not exist!");
			}
		} else if (checker == 6) {
			try {
				Object[][] selected = db.executeQuery(query);
				for (int i = 0; i < selected.length; i++) {
					for (int j = 0; j < selected[0].length; j++) {
						if (selected[i][j] instanceof Integer) {
							System.out.printf("%-30d", selected[i][j]);
						} else {
							System.out.printf("%-30s", selected[i][j]);
						}
					}
					System.out.println();
				}
			} catch (SQLException | NullPointerException e) {
				System.out.println("Error column not exised or table not existed!");
			}
		}else {
			System.out.println("Error please check your query!");
		}
	}

}
