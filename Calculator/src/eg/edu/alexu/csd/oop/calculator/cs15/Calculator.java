package eg.edu.alexu.csd.oop.calculator.cs15;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Calculator implements eg.edu.alexu.csd.oop.calculator.Calculator {
	private int indexOperation;
	private ArrayList<String> operations = new ArrayList<String>();
	private int current = 0;

	@Override
	public void input(String s) {
		if (operations.size() < 5) {
			operations.add(s);
		} else {
			operations.remove(0);
			operations.add(s);
		}
		current = operations.size() - 1;
	}

	@Override
	public String getResult() {
		String s = operations.get(current);
		Character operation = null;
		for (int i = 1; i < s.length(); i++) {
			if (checkOperation(s.charAt(i))) {
				operation = s.charAt(i);
				indexOperation = i;
				break;
			}
		}
		Double num1 = Double.parseDouble(s.substring(0, indexOperation));
		Double num2 = Double.parseDouble(s.substring(indexOperation + 1, s.length()));
		Double result = null;
		if (operation == '+') {
			result = num1 + num2;
		} else if (operation == '-') {
			result = num1 - num2;
		} else if (operation == '/') {
			result = num1 / num2;
		} else if (operation == '*') {
			result = num1 * num2;
		}
		return result.toString();
	}

	@Override
	public String current() {
		if (operations.size() == 0) {
			return null;
		}
		return operations.get(current);
	}

	@Override
	public String prev() {
		if (operations.size() == 0) {
			return null;
		}
		current--;
		if (current >= 0) {
			return operations.get(current);
		} else {
			current = 0;
			return null;
		}

	}

	@Override
	public String next() {
		if (operations.size() == 0) {
			return null;
		}
		current++;
		if (current < operations.size()) {
			return operations.get(current);
		} else {
			current = operations.size() - 1;
			return null;
		}
	}

	@Override
	public void save() {
		try {
			FileWriter fw = new FileWriter("operations.txt", false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(String.valueOf(operations.size()));
			bw.newLine();
			for (int i = 0; i < operations.size(); i++) {
				bw.write(operations.get(i));
				bw.newLine();
			}
			bw.write(String.valueOf(current));
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void load() {
		try {
			FileReader fr = new FileReader("operations.txt");
			BufferedReader br = new BufferedReader(fr);
			ArrayList<String> temp = new ArrayList<>();
			int counter = Integer.parseInt(br.readLine());
			for (int i = 0; i < counter; i++) {
				temp.add(br.readLine());
			}
			operations = temp;
			current = Integer.parseInt(br.readLine());
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean checkOperation(char op) {
		if (op == '+' || op == '-' || op == '/' || op == '*') {
			return true;
		} else {
			return false;
		}
	}

}
