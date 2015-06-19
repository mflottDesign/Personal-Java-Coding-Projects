import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GUI extends JFrame implements ActionListener, ItemListener, MouseListener{
	
	public JFrame cWf;
	public JPanel playPanel;
	private JLabel picLabel;
	private GridBagConstraints previewConstraints;
	private GridBagConstraints pPanelConstraints;
	private JPanel pPanel;
	private JLayeredPane picContainer;
	private JPanel picPanel;
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
	private GraphicsEnvironment ge;
	private GraphicsDevice[] gs;
	private GraphicsDevice screen;
	
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
		picLabel = new JLabel(playlist.get(playlistCount));
		picLabel.setPreferredSize(new Dimension(userWidth,userHeight));
		playPanel.add(picLabel, BorderLayout.CENTER);
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
		
		cWf.addMouseListener(this);
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
		pPanelConstraints = new GridBagConstraints();
		
		pPanel = new JPanel();
		pPanel.setLayout(new GridBagLayout());
		main.setBackground(new Color(25,25,25));
		pPanel.setBackground(new Color(25,25,25));
		previewConstraints = new GridBagConstraints();
		scrollPane = new JScrollPane(pPanel);
		
		// Creates the drag&drop listener
	    MyDragDropListener myDragDropListener = new MyDragDropListener();

	    //Connects pPanel with a drag and drop listener
	    new DropTarget(pPanel, myDragDropListener);
	    
	    pPanelConstraints.gridx = 0;
	    pPanelConstraints.gridy = 1;
	    pPanelConstraints.weightx = 1;
	    pPanelConstraints.weighty = 1;
	    pPanelConstraints.fill = GridBagConstraints.BOTH;
	    pPanelConstraints.anchor = GridBagConstraints.WEST;
	    main.getContentPane().add(scrollPane, pPanelConstraints);
	    
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
		
		JLabel previewLabel;
		for(int i = 0; i < playlist.size(); i++){
			previewLabel = new JLabel(playlist.get(i));
			previewLabel.setSize(playlist.get(i).getIconWidth(), playlist.get(i).getIconHeight());
			picPanel = new JPanel();
			picPanel.add(previewLabel);
			picPanel.setName(playlist.get(i).toString());
			picPanel.setBackground(new Color(60,60,60));
			picPanel.setSize(previewLabel.getSize());
			
			previewConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
			previewConstraints.gridx = 0;
			previewConstraints.gridy = i;
			previewConstraints.weightx = 1;
			previewConstraints.weighty = 1;
			
			//LayeredPane for adding things later on top of picPanel
			picContainer = new JLayeredPane();
			picContainer.setPreferredSize(new Dimension(picPanel.getSize()));
			picContainer.add(picPanel, new Integer(0));
			picContainer.addMouseListener(this);
			pPanel.add(picContainer, previewConstraints);
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
	
	public void removeImage(String imageName){
		for(int i = 0; i < icons.size(); i++){
			if(icons.get(i).equals(imageName)){
				icons.remove(i);
			}
		}
		for(int i = 0; i < URLicons.size(); i++){
			if(URLicons.get(i).toString().equals(imageName)){
				URLicons.remove(i);
			}
		}
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
	class MyDragDropListener implements DropTargetListener {

	    @Override
	    public void drop(DropTargetDropEvent event) {

	        // Accept copy drops
	        event.acceptDrop(DnDConstants.ACTION_COPY);

	        // Get the transfer which can provide the dropped item data
	        Transferable transferable = event.getTransferable();

	        // Get the data formats of the dropped item
	        DataFlavor[] flavors = transferable.getTransferDataFlavors();

	        // Loop through the flavors
	        for (DataFlavor flavor : flavors) {

	            try {

	                // If the drop items are files
	                if (flavor.isFlavorJavaFileListType()) {

	                    // Get all of the dropped files
	                    List files = (List) transferable.getTransferData(flavor);
	                    ArrayList<String> arrayFiles = new ArrayList<String>();
	                    for(int i = 0; i < files.size(); i++){
	                    	arrayFiles.add(files.get(i).toString());
	                    }

	                    // Loop them through
	                    for (String filePath : arrayFiles) {

	                        // Print out the file path
	                        System.out.println("File path is '" + filePath + "'.");
	                        icons.add(filePath);
	                        previewImages();
	                    }
	                }

	            } catch (Exception e) {

	                // Print out the error stack
	                e.printStackTrace();

	            }
	        }

	        // Inform that the drop is complete
	        event.dropComplete(true);

	    }

	    @Override
	    public void dragEnter(DropTargetDragEvent event) {
	    }

	    @Override
	    public void dragExit(DropTargetEvent event) {
	    }

	    @Override
	    public void dragOver(DropTargetDragEvent event) {
	    }

	    @Override
	    public void dropActionChanged(DropTargetDragEvent event) {
	    }

	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getSource().equals(cWf)) {
			// TODO Auto-generated method stub
			if (isAutoplay) {
				t.cancel();
				previousImage();
				addTimer();
			} else {
				nextImage();
			}
		}
		JLayeredPane source = (JLayeredPane) arg0.getSource();
		
		if (source != null) {
			int reply = JOptionPane.showConfirmDialog(null, "Do you want to delete this image?", "Delete image", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				removeImage(source.getComponentAt(0,0).getName());
				previewImages();
			}
		} else {
			System.out.println("There is something wrong");
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLayeredPane source = (JLayeredPane) arg0.getSource();
		
		if (source != null) {
			ImageIcon i = new ImageIcon(getClass().getResource("/images/icon50x50.png"));
			JLabel l = new JLabel(i);
			l.setBounds(0, 10, 50, 50);
			source.add(l, new Integer(1));
			
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLayeredPane source = (JLayeredPane) arg0.getSource();
		
		if (source != null) {
			source.remove(source.getComponentAt(0, 10));
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}