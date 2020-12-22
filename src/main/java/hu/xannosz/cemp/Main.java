package hu.xannosz.cemp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main implements ActionListener {

    public static final String MICRO_TOOLS_VERSION = "0.5.2";
    public static final String LOMBOK_VERSION = "1.12.2";

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
        for (String project : Objects.requireNonNull(dir.list())) {
            boolean empty = true;
            try {
                for (String name : Objects.requireNonNull(new File("../" + project).list())) {
                    if (name.equals("pom.xml")) {
                        empty = false;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (empty) {
                projects.add(project);
            }
        }

        System.out.println(projects);

        artifactId = new JTextArea();
        name = new JTextArea();
        comboBox = new JComboBox<>(projects.toArray(new String[0]));
        JButton create = new JButton("Create");
        JButton update = new JButton("Update");
        create.addActionListener(new Main());
        update.addActionListener(new Main());
        frame.setLayout(new GridLayout(4, 2));
        frame.add(projectFolder);
        frame.add(comboBox);
        frame.add(packageName);
        frame.add(artifactId);
        frame.add(projectName);
        frame.add(name);
        frame.add(create);
        frame.add(update);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        if (a.getActionCommand().equals("Update")) {
            artifactId.setText(camelToSnake((String) Objects.requireNonNull(comboBox.getSelectedItem())));
            name.setText(camelToSpace((String) Objects.requireNonNull(comboBox.getSelectedItem())));
        }
        if (a.getActionCommand().equals("Create")) {
            new File("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/src").mkdir();

            new File("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/src/main").mkdir();
            new File("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/src/test").mkdir();

            new File("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/src/main/java/"
                    + artifactId.getText().trim().replace(".", "/")).mkdirs();
            new File("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/src/main/resources/").mkdirs();
            new File("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/src/test/java/"
                    + artifactId.getText().trim().replace(".", "/")).mkdirs();
            new File("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/src/test/resources/").mkdirs();

            try (PrintWriter writer = new PrintWriter("../" + Objects.requireNonNull(comboBox.getSelectedItem()).toString() + "/pom.xml", "UTF-8")) {
                for (String line : Files.readAllLines(new File("template.xml").toPath())) {
                    writer.println(replaceTemplate(line));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Created");
        }
    }

    public static String camelToSnake(String str) {
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(str.charAt(0)));

        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('.');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    public static String camelToSpace(String str) {
        StringBuilder result = new StringBuilder();
        result.append(str.charAt(0));

        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append(' ');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    private String replaceTemplate(String line) {
        String result = line;
        result = result.replaceAll("##name##", name.getText().trim());
        result = result.replaceAll("##artifactId##", artifactId.getText().trim());
        result = result.replaceAll("##lombokVersion##", LOMBOK_VERSION);
        result = result.replaceAll("##microtoolsVersion##", MICRO_TOOLS_VERSION);
        return result;
    }
}