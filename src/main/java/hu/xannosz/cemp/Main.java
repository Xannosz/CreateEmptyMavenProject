package hu.xannosz.cemp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class Main implements ActionListener {

	public static JTextArea artifactId;
	public static JTextArea name;

	public static void main(String[] args) {
		JFrame frame = new JFrame("CEMP");
		JLabel packageName = new JLabel("Package name:");
		JLabel projectName = new JLabel("Project name:");
		artifactId = new JTextArea();
		name = new JTextArea();
		JButton create = new JButton("Create");
		create.addActionListener(new Main());
		frame.setLayout(new GridLayout(3, 2));
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
			writer = new PrintWriter("pom.xml", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//@formatter:off
		writer.println("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">"+	
	                     "<modelVersion>4.0.0</modelVersion>"+
	                     "<groupId>hu.xannosz</groupId>"+
	                     "<artifactId>"+artifactId.getText().trim()+"</artifactId>"+
	                     "<version>0.0.1</version>"+
	                     "<name>"+name.getText().trim()+"</name>"+
	                     "<description></description>"+
                         "<organization>"+
		                   "<name>xannosz</name>"+
	                     "</organization>"+
	                     "<build>"+
		                   "<plugins>"+
			"<plugin><groupId>org.apache.maven.plugins</groupId><artifactId>maven-compiler-plugin</artifactId><version>3.3</version><configuration><target>1.8</target><source>1.8</source></configuration></plugin><plugin><groupId>org.apache.maven.plugins</groupId><artifactId>maven-dependency-plugin</artifactId><version>2.10</version><executions><execution><id>copy-dependencies</id><phase>prepare-package</phase><goals><goal>copy-dependencies</goal></goals><configuration><outputDirectory>${project.build.directory}/lib</outputDirectory><overWriteReleases>false</overWriteReleases><overWriteSnapshots>false</overWriteSnapshots><overWriteIfNewer>true</overWriteIfNewer></configuration></execution></executions></plugin><plugin><groupId>org.apache.maven.plugins</groupId><artifactId>maven-jar-plugin</artifactId><version>2.6</version><configuration><archive><manifest><addClasspath>true</addClasspath><classpathPrefix>lib/</classpathPrefix><mainClass>"+
							"hu.xannosz."+artifactId.getText().trim()
							+".app.Main</mainClass><addDefaultImplementationEntries>true</addDefaultImplementationEntries><addDefaultSpecificationEntries>true</addDefaultSpecificationEntries></manifest></archive></configuration></plugin></plugins></build><dependencies><dependency><groupId>org.projectlombok</groupId><artifactId>lombok</artifactId><version>1.16.4</version></dependency><dependency><groupId>hu.xannosz</groupId><artifactId>microtools</artifactId><version>0.1.0</version></dependency></dependencies><repositories><repository><id>xannosz.repo</id><name>Xannosz</name><url>http://www.xannosz.cloud/repositories/</url></repository></repositories><properties><project.build.sourceEncoding>UTF-8</project.build.sourceEncoding></properties></project>");
		//@formatter:on
		writer.close();
	}
}