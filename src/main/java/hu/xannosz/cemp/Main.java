package hu.xannosz.cemp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class Main implements ActionListener {

	public static JTextArea artifactId;
	public static JTextArea name;
	public static JComboBox<String> comboBox;

	public static void main(String[] args) {
		JFrame frame = new JFrame("CEMP");
		JLabel projectFolder = new JLabel("Project folder:");
		JLabel packageName = new JLabel("Package name:");
		JLabel projectName = new JLabel("Project name:");
		List<String> projects = new ArrayList<>();

		File dir = new File("..");
		for (String project : dir.list()) {
			boolean empty = true;
			for (String name : new File("../" + project).list()) {
				if (name.equals("pom.xml")) {
					empty = false;
				}
			}
			if (empty) {
				projects.add(project);
			}
		}

		System.out.println(projects);

		artifactId = new JTextArea();
		name = new JTextArea();
		comboBox = new JComboBox<String>(projects.toArray(new String[0]));
		JButton create = new JButton("Create");
		create.addActionListener(new Main());
		frame.setLayout(new GridLayout(4, 2));
		frame.add(projectFolder);
		frame.add(comboBox);
		frame.add(packageName);
		frame.add(artifactId);
		frame.add(projectName);
		frame.add(name);
		frame.add(create);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(300, 150);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("../" + comboBox.getSelectedItem().toString() + "/pom.xml", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (String line : Files.readAllLines(Paths.get("template.xml"))) {
				writer.println(replaceTemplate(line));
			}
		} catch (IOException e) {
		}

		writer.close();
	}

	private String replaceTemplate(String line) {
		String result = line;

		result = result.replaceAll("##name##", name.getText().trim());
		result = result.replaceAll("##artifactId##", artifactId.getText().trim());

		System.out.println(result);

		return result;
	}
}