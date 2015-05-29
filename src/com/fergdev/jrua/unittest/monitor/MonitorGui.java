package nz.ac.massey.buto.unittest.monitor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MonitorGui extends JFrame{

	private JTextArea textArea;
	
	public MonitorGui(String testName){
		setTitle("Buto Monitor - " + testName);
		setSize(500, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(textArea),BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	public void appendText(String message){
		textArea.append( message + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
}
