package demo.sphinx.helloworld;


import java.io.IOException;
import java.net.URL;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.recognizer.RecognizerState;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

public class SpeechRecognizer implements Runnable{
	
	//Gets the singleton instance of GUI-class
	GUI g = GUI.getSharedGUI();
	
	private Microphone microphone;
	private Recognizer recognizer;
	private Boolean startRecording = false;
	
	public SpeechRecognizer() throws IOException{
		try  {
            URL url = this.getClass().getResource("helloworld.config.xml");
            g.setOutput("Found 'helloworld.config.xml'");
            if (url == null) {
            	g.setOutput("Couldn't find 'helloworld.config.xml'");
                throw new IOException("Can't find helloworld.config.xml");
            } 
            
            ConfigurationManager cm = new ConfigurationManager(url);
            recognizer = (Recognizer) cm.lookup("recognizer");
            microphone = (Microphone) cm.lookup("microphone");
        } catch (PropertyException e) {
        	g.setOutput("Problem configuring speechRecognizer" + e);
            throw new IOException("Problem configuring speechRecognizer " + e);
        } catch (InstantiationException e) {
        	g.setOutput("Problem creating speechRecognizer" + e);
            throw new IOException("Problem creating speechRecognizer " + e);
        }
	}

	public void microphoneOn() {
	    new Thread(this).start();
	}

	public void microphoneOff() {
        microphone.stopRecording();
    }
	
	public void startup() throws IOException {
		//System.out.println("Allocating recources");
		g.setOutput("Allocating recourses...");
        recognizer.allocate();
    }
	
	public void shutdown() {
        microphoneOff();
        if (recognizer.getState() == RecognizerState.ALLOCATED) {
            recognizer.deallocate();
        }
    }
	
	 public void run() {
		 	
	        microphone.clear();
	        microphone.startRecording();
	        startRecording = true;
	        while(startRecording){
		        //System.out.println("Command me master!");
	        	g.setOutput("Please give me a command");
		        Result result = recognizer.recognize();
		        //microphone.stopRecording();
		        if (result != null) {
		            String resultText = result.getBestFinalResultNoFiller();
		            //System.out.println("You said: "+ resultText + "\n");
		            g.setOutput("You said: " + resultText);
		            
		            if(resultText.equalsIgnoreCase(("Pause"))){
		            	startRecording = false;
		            }
		            //Browser-commands
		            else if(resultText.equalsIgnoreCase("Open Browser")){
		            	g.setOutput("Opening browser...");
		            	Process p;
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start chrome.exe");
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	g.setOutput("Redirecting to www.google.no");
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start chrome www.google.no");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }else if(resultText.equalsIgnoreCase("Site v g")){
		            	g.setOutput("Opening new tab with www.vg.no...");
		            	Process p;
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start chrome --new-tab www.vg.no");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }else if(resultText.equalsIgnoreCase("site hot mail")){
		            	g.setOutput("Opening new tab with www.hotmail.com...");
		            	Process p;
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start chrome --new-tab www.hotmail.com");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }else if(resultText.equalsIgnoreCase("site Face book")){
		            	g.setOutput("Opening new tab with www.facebook.com...");
		            	Process p;
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start chrome --new-tab www.facebook.com");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		            //Games-commands
		            else if(resultText.equalsIgnoreCase("Start Step Mania")){
		            	g.setOutput("Starting game StepMania.exe...");
		            	Process p;
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start F:\\Games\\StepMania\\StepMania\\Program\\StepMania.exe");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }else if(resultText.equalsIgnoreCase("Stop Step Mania")){
		            	g.setOutput("Closing game StepMania.exe...");
		            	Process p;
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start taskkill /im F:\\Games\\StepMania\\StepMania\\Program\\StepMania.exe /f");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		            else if(resultText.equalsIgnoreCase("start Photo shop")){
		            	g.setOutput("Starting photoshop.exe...");
		            	Process p;
		            	try {
							p = Runtime.getRuntime().exec("cmd /c start photoshop.exe");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }
		        }else{
		        	g.setOutput("I didn't get what you were saying...");
		        	//System.out.println("I can't hear what you're saying!");
		        }
	        }
	    }
}
