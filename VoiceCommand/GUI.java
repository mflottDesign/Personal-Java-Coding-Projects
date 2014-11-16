package demo.sphinx.helloworld;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class GUI extends JFrame implements ActionListener{

	private SpeechRecognizer speechRecognizer;
	
	private JButton startRecognize;
	private JButton stopRecognize;
	public JTextArea outputPane;
	
	private GUI(){
		
		this.setSize(720,500);
		
		FlowLayout f = new FlowLayout();
		this.setLayout(f);
		
		startRecognize = new JButton("Start recognization");
		stopRecognize = new JButton("Stop recognization");
		outputPane = new JTextArea();
		this.add(outputPane);
		this.add(startRecognize);
		this.add(stopRecognize);
		stopRecognize.setEnabled(false);
		
		//Modify the size of the different objects in the GUI
		startRecognize.setPreferredSize(new Dimension(250,75));
		stopRecognize.setPreferredSize(new Dimension(250,75));
		outputPane.setPreferredSize(new Dimension(500,300));
		
		//Adds actionListeners to the two buttons
		startRecognize.addActionListener(this);
		stopRecognize.addActionListener(this);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				if(speechRecognizer != null){
					speechRecognizer.shutdown();
				}
				System.exit(0);
			}
		});
	}
	
	private static GUI g;
	
	 public void go() {
	        try {

	        	this.setOutput("Loading recognizer...");
	            //outputPane.setText(outputPane.getText() + "Loading recognizer..."+"\n");
	            speechRecognizer = new SpeechRecognizer();

	            this.setOutput("Starting recognizer...");
	            speechRecognizer.startup();

	            this.setOutput("VoiceCommand Version 1.0");
	            //outputPane.setText(outputPane.getText() + "VoiceCommand Version 1.0"+"\n");
	            startRecognize.setEnabled(true);
	        } catch (Throwable e) {
	        	this.setOutput("Error: " + e.getMessage());
	            //outputPane.setText(outputPane.getText() + "Error: " + e.getMessage() + "\n");
	        }
	    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(startRecognize)){
			if(startRecognize.isEnabled()){
				startRecognize.setEnabled(false);
				stopRecognize.setEnabled(true);
				speechRecognizer.microphoneOn();
			}
		}else if(arg0.getSource().equals(stopRecognize)){
			startRecognize.setEnabled(true);
			speechRecognizer.microphoneOff();
			stopRecognize.setEnabled(false);
		}
		
		
	}
	
	/*
	 * Singleton method - ensures that there will only be one instance of GUI
	 * If there already is one GUI instance, it will return the current GUI instance
	 */
	
	public static GUI getSharedGUI(){
		if (g == null){
			g = new GUI();
		}
		return g;
	}
	
	/*
	 * Responsible for putting all output to the outputPane field
	 */
	
	public void setOutput(String output){
		outputPane.setText(outputPane.getText() + output + "\n");
	}
	
}


