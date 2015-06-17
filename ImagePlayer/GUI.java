import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUI extends JFrame implements ActionListener, ItemListener{
	
	public JFrame cWf;
	public JPanel playPanel;
	private JLabel label;
	private FlowLayout f;
	private GridBagConstraints c;
	private GridBagConstraints c2;
	private JPanel pPanel;
	private JMenuItem file;
	private JMenuItem urlFile;
	private JMenuItem play;
	private JCheckBoxMenuItem shufflePlaylist;
	private JCheckBoxMenuItem autoPlay;
	private JMenuItem savePlaylist;
	private JMenuItem loadPlaylist;
	private ArrayList<String> icons = new ArrayList<String>();
	private ArrayList<URL> URLicons = new ArrayList<URL>();
	private ArrayList<ImageIcon> playlist = new ArrayList<ImageIcon>();
	private int playlistCount = 0;
	public Timer t;
	public int changeInterval = 0;
	public Toolkit toolkit;
	public boolean firstInterval = true;
	private JFrame main;
	private JScrollPane scrollPane = new JScrollPane(pPanel);
	private Boolean shuffle = false;
	public Boolean isAutoplay = false;
	private int userWidth;
	private int userHeight;
	GraphicsEnvironment ge;
	GraphicsDevice[] gs;
	GraphicsDevice screen;
	
	public GUI() throws IOException{
		startWindow();
	}
	
	public String askImage(){
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setCurrentDirectory(new java.io.File("."));
		chooseFile.setDialogTitle("Select an image");
		chooseFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooseFile.setAcceptAllFileFilterUsed(false);
		chooseFile.showOpenDialog(null);
		return chooseFile.getSelectedFile().toString();
	}
	
	public void createWindow(){
		cWf = new JFrame();
		playPanel = new JPanel();
		
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gs = ge.getScreenDevices();
	
		cWf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		cWf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		cWf.setUndecorated(true);
		cWf.pack();
		cWf.setOpacity(1f);
		cWf.setFocusable(true);
		cWf.setFocusableWindowState(true);
		cWf.getContentPane().add(playPanel);
		
		BorderLayout b = new BorderLayout();
		playPanel.setLayout(b);
		
		//Optional shuffle
		if(shuffle){
			shufflePlaylist();
		}
		
		//First from playlist
		label = new JLabel(playlist.get(playlistCount));
		label.setPreferredSize(new Dimension(userWidth,userHeight));
		playPanel.add(label, BorderLayout.CENTER);
		if(isAutoplay){	
			addTimer();
		}
			
		cWf.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
				int key = arg0.getKeyCode();
				
				if(key == KeyEvent.VK_LEFT){
					if(isAutoplay){
						t.cancel();
						previousImage();
						addTimer();
					}else{
						previousImage();
					}
					
				}
				if(key == KeyEvent.VK_RIGHT){
					if(isAutoplay){
						t.cancel();
						previousImage();
						addTimer();
					}else{
						nextImage();
					}
				}
				if(key == arg0.VK_1){
					changeScreen(gs[0]);
				}else if(key == arg0.VK_2){
					if(!(gs.length < 2)){
						changeScreen(gs[1]);
					}else{
						System.out.println("Screen 2 doesn't exist!");
					}
				}else if(key == arg0.VK_3){
					if(!(gs.length < 3)){
						changeScreen(gs[2]);
					}else{
						System.out.println("Screen 3 doesn't exist!");
					}
				}else if(key == arg0.VK_4){
					if(!(gs.length < 4)){
						changeScreen(gs[3]);
					}else{
						System.out.println("Screen 4 doesn't exist!");
					}
				}else if(key == arg0.VK_5){
					if(!(gs.length < 5)){
						changeScreen(gs[4]);
					}else{
						System.out.println("Screen 5 doesn't exist!");
					}
				}else if(key == arg0.VK_6){
					if(!(gs.length < 6)){
						changeScreen(gs[5]);
					}else{
						System.out.println("Screen 6 doesn't exist!");
					}
				}else if(key == arg0.VK_7){
					if(!(gs.length < 7)){
						changeScreen(gs[6]);
					}else{
						System.out.println("Screen 7 doesn't exist!");
					}
				}else if(key == arg0.VK_8){
					if(!(gs.length < 8)){
						changeScreen(gs[7]);
					}else{
						System.out.println("Screen 8 doesn't exist!");
					}
				}else if(key == arg0.VK_9){
					if(!(gs.length < 9)){
						changeScreen(gs[8]);
					}else{
						System.out.println("Screen 9 doesn't exist!");
					}
				}else if(key == arg0.VK_ESCAPE){
					cWf.dispose();
					startWindow();
					if(shuffle){
						shufflePlaylist.setSelected(true);
					}
					if(isAutoplay){
						t.cancel();
						autoPlay.setSelected(true);
					}
					previewImages();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		cWf.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(isAutoplay){
					t.cancel();
					previousImage();
					addTimer();
				}else{
					nextImage();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub	
			}
		});
		cWf.setVisible(true);
	}
	
	public void shufflePlaylist(){
		//Optional shuffle
		Collections.shuffle(playlist);
	}
	
	public void startWindow(){
		main = new JFrame();
		main.setSize(500,500);
		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		main.setLocationRelativeTo(null);
		JMenu menu = new JMenu("File");
		JMenu playback = new JMenu("Playback");
		JMenuBar menubar = new JMenuBar();
		file = new JMenuItem("Add file");
		urlFile = new JMenuItem("Add from URL");
		play = new JMenuItem("Play all");
		shufflePlaylist = new JCheckBoxMenuItem("Shuffle");
		autoPlay = new JCheckBoxMenuItem("Autoplay");
		savePlaylist = new JMenuItem("Save playlist");
		loadPlaylist = new JMenuItem("Load playlist");
		main.setJMenuBar(menubar);
		menubar.add(menu);
		menubar.add(playback);
		menu.add(file);
		menu.add(urlFile);
		playback.add(play);
		playback.add(shufflePlaylist);
		playback.add(autoPlay);
		playback.add(savePlaylist);
		playback.add(loadPlaylist);
		
		//ActionListeners for menuitems
		file.addActionListener(this);
		urlFile.addActionListener(this);
		play.addActionListener(this);
		shufflePlaylist.addItemListener(this);
		autoPlay.addItemListener(this);
		savePlaylist.addActionListener(this);
		loadPlaylist.addActionListener(this);
		
		GridBagLayout g = new GridBagLayout();
		main.setLayout(g);
		c2 = new GridBagConstraints();
		
		pPanel = new JPanel();
		pPanel.setLayout(new GridBagLayout());
		pPanel.setBackground(Color.WHITE);
		c = new GridBagConstraints();
		scrollPane = new JScrollPane(pPanel);
	    
	    c2.gridx = 0;
	    c2.gridy = 1;
	    c2.weightx = 1;
	    c2.weighty = 1;
	    c2.fill = GridBagConstraints.BOTH;
	    c2.anchor = GridBagConstraints.WEST;
	    main.getContentPane().add(scrollPane, c2);
	    
	    //Sets the window visible
		main.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		URL url = null;
		if(arg0.getSource().equals(file)){
			icons.add(askImage());
			
		}else if(arg0.getSource().equals(urlFile)){
			String urlTarg = JOptionPane.showInputDialog(this,"Paste image URL");
			if(urlTarg != null){
				try {
					url = new URL(urlTarg);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				URLicons.add(url);
			}else{
				System.out.println("Something went wrong.");
			}
			
		}else if(arg0.getSource().equals(play)){
			if(playlist.size() != 0){
				main.dispose();
				if(isAutoplay){
					changeInterval = Integer.parseInt(JOptionPane.showInputDialog("Insert a change interval in seconds:"));
				}
				createWindow();
			}else{
				System.out.println("You havn't loaded any images!");
			}
			
		}else if(arg0.getSource().equals(savePlaylist)){
			try {
				savePlaylist(icons, URLicons);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(arg0.getSource().equals(loadPlaylist)){
			JFileChooser loadFile = new JFileChooser();
			loadFile.setCurrentDirectory(new java.io.File("."));
			loadFile.setDialogTitle("Select a playlist");
			loadFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
			loadFile.setAcceptAllFileFilterUsed(false);
			if (loadFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				if(loadFile.getSelectedFile() != null){
					try {
						loadPlaylist(loadFile.getSelectedFile());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				} else {
				  System.out.println("No Selection ");
				}
		}
		previewImages();
	}
	
	public void changeScreen(GraphicsDevice graphicsDevice){
		screen = graphicsDevice;
		GraphicsConfiguration[] gc = screen.getConfigurations();
		System.out.println(gc[0].getBounds());
		cWf.setLocation(gc[0].getBounds().x,gc[0].getBounds().y);
		cWf.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	public void previewImages(){
		playlist.clear();
		pPanel.removeAll();
		
		if(icons != null){
			for(int i = 0;i < icons.size(); i++){
				ImageIcon icon = new ImageIcon(icons.get(i));
				playlist.add(icon);
			}
		}
		if(URLicons != null){
			for(int i = 0; i < URLicons.size(); i++){
				ImageIcon icon = new ImageIcon(URLicons.get(i));
				playlist.add(icon);
			}
		}
		
		JPanel p;
		for(int i = 0; i < playlist.size(); i++){
			JLabel label = new JLabel(playlist.get(i));
			p = new JPanel();
			p.add(label);
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.gridx = 0;
			c.gridy = i;
			c.weightx = 1;
			c.weighty = 1;
			pPanel.add(p, c);
		}
		
		pPanel.invalidate();
		main.invalidate();
		pPanel.validate();
		main.validate();
		pPanel.repaint();
		main.repaint();
	}
	
	public void savePlaylist(ArrayList<String> icons, ArrayList<URL> URLicons) throws FileNotFoundException{
		
		JFileChooser saveFile = new JFileChooser();
		saveFile.setCurrentDirectory(new java.io.File("."));
		saveFile.setDialogTitle("Select a playlist");
		saveFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
		saveFile.setAcceptAllFileFilterUsed(false);
		saveFile.addChoosableFileFilter(new TextFileFilter());
		
		if (saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String fileName;
			if(saveFile.getSelectedFile().getName().contains(".txt")){
				fileName = saveFile.getSelectedFile().getName();
			}else{
				fileName = saveFile.getSelectedFile().getName()+".txt";
			}
			PrintWriter pwIcons = new PrintWriter(new FileOutputStream(fileName));
			for(int i = 0; i < icons.size(); i++){
				pwIcons.println(icons.get(i).toString());
			}
			for(int i = 0; i < URLicons.size(); i++){
				pwIcons.println("<URL>"+URLicons.get(i).toString());
			}
			System.out.println("Icons have been saved");
			pwIcons.close();
		}
	}
	public void loadPlaylist(File iconlist) throws FileNotFoundException{
		String line;
		icons.clear();
		URLicons.clear();
		try(
			FileInputStream fin = new FileInputStream(iconlist);
			InputStreamReader isr = new InputStreamReader(fin, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
		){
			while((line = br.readLine()) != null){
				if(!line.contains("<URL>")){
					icons.add(line);
				}else{
					line = line.replace("<URL>", "");
					URL lineURL = new URL(line);
					URLicons.add(lineURL);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Iconlist loaded");
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		AbstractButton button = (AbstractButton) arg0.getItem();
	    if (button.getText().equals(shufflePlaylist.getText())) {
			if (button.isSelected()) {
				shuffle = true;
				System.out.println("Shuffle is now TRUE");
			} else {
				shuffle = false;
				System.out.println("Shuffle is now FALSE");
			}
		}else if(button.getText().equals(autoPlay.getText())){
			if(button.isSelected()){
				isAutoplay = true;
				System.out.println("Autoplay is now TRUE");
			}else{
				isAutoplay = false;
				System.out.println("Autoplay is now FALSE");
			}
		}
	}
	
	public void previousImage(){
		playlistCount--;
		playPanel.removeAll();
		if(!(playlistCount < 0)){
			JLabel l = new JLabel(playlist.get(playlistCount));
			l.setPreferredSize(new Dimension(userWidth,userHeight));
			playPanel.add(l, BorderLayout.CENTER);
		}else{
			playlistCount = playlist.size()-1;
			JLabel l = new JLabel(playlist.get(playlistCount));
			l.setPreferredSize(new Dimension(userWidth,userHeight));
			playPanel.add(l, BorderLayout.CENTER);
		}
		playPanel.invalidate();
		playPanel.validate();
		playPanel.repaint();
	}
	
	public void nextImage(){
		playlistCount++;
		playPanel.removeAll();
		if(!(playlistCount == (playlist.size()))){
			JLabel l = new JLabel(playlist.get(playlistCount));
			l.setPreferredSize(new Dimension(userWidth,userHeight));
			playPanel.add(l, BorderLayout.CENTER);
		}else{
			playlistCount = 0;
			if(shuffle){
				shufflePlaylist();
			}
			JLabel l = new JLabel(playlist.get(playlistCount));
			l.setPreferredSize(new Dimension(userWidth,userHeight));
			playPanel.add(l, BorderLayout.CENTER);
		}
		playPanel.invalidate();
		playPanel.validate();
		playPanel.repaint();
	}
	
	public void addTimer(){
		toolkit = Toolkit.getDefaultToolkit();
		t = new Timer();
		t.scheduleAtFixedRate(new IntervalChanger(), 0, changeInterval * 1000);
	}
	
	class IntervalChanger extends TimerTask{

		public void run() {
			// TODO Auto-generated method stub
			
			if(!firstInterval){
				nextImage();
			}else{
				firstInterval = false;
			}
		}
	}
}