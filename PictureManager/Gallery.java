import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.imgscalr.Scalr;


public class Gallery extends JFrame implements ActionListener{

	private ArrayList<String> picPaths;
	private ArrayList<CustomPanel> pictureList;
	
	private JFrame pbarContainer;
	private JPanel pbarPanel;
	private JProgressBar pbar;
	private JTextField pField;
	private JTextField search;
	private JPanel listPanel;
	private CustomPanel previewPanel;
	private CustomPanel cp;
	
	private BufferedImage icon = null;
	private BufferedImage img = null;
	private Dimension boundary;
	private Dimension resizedDimension;
	private Dimension previewPanelDimension;
	
	private Connection conn;
	private Statement stmt;
	private QueryDB qdb;
	
	private DecimalFormat f = new DecimalFormat("##.0");
	private static final int pbar_MIN = 0;
	private int pbar_MAX = 0;
	private int id;
	private String path;
	private String name;
	private String oldName;
	private boolean startup = false;
	
	
	public Gallery(){
		
		// GUI preferences
		this.setTitle("Personal Picture Management System");
		this.setSize(1280,720);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Search bar for searching through the pictures
		search = new JTextField();
		search.setPreferredSize(new Dimension(this.getWidth(), 50));
		Font searchFont = new Font("Arial", Font.PLAIN, 30);
		search.setFont(searchFont);
		
		// Panel for containing all the pictures
		listPanel = new JPanel(new WrapLayout(WrapLayout.LEFT));
		listPanel.setBackground(Color.BLACK);
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		JScrollPane scrollPane = new JScrollPane(listPanel, v, h);
		listPanel.setSize(new Dimension(1280, 1));

		this.add(search, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		
		pictureList = new ArrayList<CustomPanel>();
		picPaths = new ArrayList<String>();
	
		// Creates the drag&drop listener
	    MyDragDropListener myDragDropListener = new MyDragDropListener();

	    //Connects the GUI with a drag and drop listener
	    new DropTarget(this, myDragDropListener);
		
		// DocumentListener for search bar
	    search.getDocument().addDocumentListener(new DocumentListener() {
	        @Override
	        public void insertUpdate(DocumentEvent e) { search(); }
	        @Override
	        public void removeUpdate(DocumentEvent e) { search(); }
	        @Override
	        public void changedUpdate(DocumentEvent e) {}
	    });
	    // Query through the db at startup, showing a progressbar before GUI is set visible.
	    Query();
		this.setVisible(true);
	}
	
	public void Query(){
		try {
			qdb = new QueryDB();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Progressbar during loading, only shows at startup
		if(!startup){
			pbarContainer = new JFrame();
			pbarPanel = new JPanel(new BorderLayout());
			pbar = new JProgressBar();
			pField = new JTextField("Loading pictures: 0%");
			pField.setEditable(false);
			pbar_MAX = qdb.picModel.size();
			pbar.setMinimum(pbar_MIN);
		    pbar.setMaximum(pbar_MAX);
			pbarPanel.add(pbar, BorderLayout.NORTH);
			pbarPanel.add(pField, BorderLayout.SOUTH);
		    pbarContainer.setLocationRelativeTo(null);
			pbarContainer.setContentPane(pbarPanel);
			pbarContainer.setUndecorated(true);
			pbarContainer.pack();
			pbarContainer.setVisible(true);
		}
	    for(int i = 0; i < qdb.picModel.size(); i++){
	    	
	    	picPaths.add(qdb.picModel.get(i).getPath().trim()+"\\"+qdb.picModel.get(i).getName());
	    	id = qdb.picModel.get(i).getId();
	    	path = picPaths.get(i);
	    	name = qdb.picModel.get(i).getName();
	    	previewPanel = new CustomPanel(id, path, name);
	    	previewPanelDimension = new Dimension(200,200);

	    	BufferedImage image = null;
			try {
				image = ImageIO.read(new File(picPaths.get(i)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	Dimension origDim = new Dimension(image.getWidth(),image.getHeight());
	    	Dimension picDim = resize(origDim, previewPanelDimension);
	    	
	    	img = Scalr.resize(image, Scalr.Method.SPEED, picDim.width,picDim.height, Scalr.OP_ANTIALIAS);
	    	ImageIcon newIcon = new ImageIcon(img);
	    	JLabel label = new JLabel();
	    	label.setSize(newIcon.getIconWidth(), previewPanelDimension.height);
	    	previewPanel.add(label, BorderLayout.CENTER);
	    	label.setIcon(newIcon);
	    	JTextField field = new JTextField(name);
	    	field.addActionListener(this);
	    	field.addMouseListener(new MouseAdapter(){
	    		public void mousePressed(MouseEvent e) {
	    			JTextField oldField = (JTextField)e.getSource();
			   		oldName = oldField.getText();
	    		}
			});
	    	previewPanel.add(field, BorderLayout.SOUTH);
	    	previewPanel.setBackground(Color.BLACK);
	    	listPanel.add(previewPanel);
	    	pictureList.add(previewPanel);
	    	if(!startup){
	    		updateBar((int)Math.round(1.0*(i+1/pbar_MAX*100)));
	    		pField.setText("Loading pictures: "+f.format((float)((i+1)*100)/pbar_MAX)+" %");
	    	}
	    }
	    if(!startup){
	    	startup = true;
	    	pbarContainer.dispose();
	    }
	}
	 
	  public void updateBar(int newValue) {
	    pbar.setValue(newValue);
	  }
	
	public void search(){
		// First clear the whole listPanel
		listPanel.removeAll();
		
		// Then load all with name containing the search string
		for(int i = 0; i < pictureList.size(); i++){
			CustomPanel p = pictureList.get(i);
			if(p.thisName.contains(search.getText())){
				listPanel.add(p);
			}
		}
		listPanel.invalidate();
		listPanel.validate();
		listPanel.repaint();
	}
	
	public BufferedImage previewPicture(){
		
		Dimension imgSize = new Dimension(icon.getWidth(),icon.getHeight());
		boundary = previewPanelDimension;
		resizedDimension = resize(imgSize,boundary);
		img = icon;
		
		return img;
	}
	
	public static Dimension resize(Dimension imgSize, Dimension boundary) {

	    int original_width = imgSize.width;
	    int original_height = imgSize.height;
	    int bound_height = boundary.height;
	    int new_width = original_width;
	    int new_height = original_height;

	    if (new_height > bound_height) {
	        // Scale height to fit
	        new_height = bound_height;

	        // Scale width to maintain aspect ratio
	        new_width = (new_height * original_width) / original_height;
	    }

	    return new Dimension(new_width, new_height);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// Gets the source textfield and updates the DB with the new name
		JTextField updateField = (JTextField)arg0.getSource();
		updateDB(oldName, updateField.getText());
		String oldPath = "";
		String newPath = "";
		for(int i = 0; i < picPaths.size(); i++){
			if(picPaths.get(i).contains(oldName)){
				oldPath = picPaths.get(i);
				newPath = qdb.picModel.get(i).getPath().trim();
				picPaths.set(i, newPath+"\\"+updateField.getText());
			}
		}
		File oldFile = new File(oldPath);
		File newFile = new File(newPath+"\\"+updateField.getText());
		if(oldFile.renameTo(newFile)){
			System.out.println("File has been renamed and updated");
		}else{
			System.out.println("There has occured an error renaming the file");
		}
	}
	
	// Function for updating a picture name
	public void updateDB(String oldPicName, String newName){
		try {
			// Connection to the DB, put in your own connection parameters
			conn = DriverManager.getConnection(
			 "jdbc:mysql://localhost/yourDB",
			 "username",
			 "password" );
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			try {
				stmt = conn.createStatement();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		try {
	        // SQL update-statement
	        stmt.executeUpdate("UPDATE pictures"
					+ " SET pic_name = '"+newName+"' "
					+ " WHERE pic_name = '"+oldPicName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try { stmt.close(); } catch (Throwable ignore) {}
		}
		} finally {
		    //It's important to close the connection when you are done with it
		    try { conn.close(); } catch (Throwable ignore) { /* Propagate the original exception
		instead of this one that you may want just logged */ }
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

	                        String fileName = new File(filePath).getName();
	                        String path = new File(filePath).getAbsolutePath();
	                        if(path.contains(fileName)){
	                        	path = path.replace("\\", "\\\\");
	                        	path = path.replace("\\"+fileName, " ");
	                        }
	                        try {
	                			conn = DriverManager.getConnection(
	                			 "jdbc:mysql://localhost/yourDB",
	                			 "username",
	                			 "password" );
	                		} catch (SQLException e3) {
	                			// TODO Auto-generated catch block
	                			e3.printStackTrace();
	                		}
	                		try {
	                			try {
	                				stmt = conn.createStatement();
	                			} catch (SQLException e2) {
	                				// TODO Auto-generated catch block
	                				e2.printStackTrace();
	                			}
	                		try {
	                			System.out.println("Filename is: "+fileName+", Path is: "+path);
	                	        stmt.executeUpdate( "INSERT INTO pictures VALUES (null,'"+fileName+"','"+path+"')");
	                		} finally {
	                		    try { stmt.close(); } catch (Throwable ignore) {}
	                		}
	                		} finally {
	                		    try { conn.close(); } catch (Throwable ignore) {}
	                		}
	                    }
	                }

	            } catch (Exception e) {
	                // Print out the error stack
	                e.printStackTrace();
	            }
	        }

	        // Inform that the drop is complete
	        event.dropComplete(true);
	       
	        listPanel.removeAll();
	        picPaths.clear();
	        pictureList.clear();
	        
	        // Query through the db again
	        Query();
	        
	        listPanel.invalidate();
	        listPanel.validate();
	        listPanel.repaint();
	    }
	    @Override public void dragEnter(DropTargetDragEvent event) {}
	    @Override public void dragExit(DropTargetEvent event) {}
	    @Override public void dragOver(DropTargetDragEvent event) {}
	    @Override public void dropActionChanged(DropTargetDragEvent event) {}
	}

	class CustomPanel extends JPanel
    {
		Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
        MouseAdapter myMA = new MouseAdapterMod();
        BorderLayout b;
        int thisId;
        String thisPath;
        String thisName;
        
        CustomPanel(int id, String picPath, String picName)
        {
        	b = new BorderLayout();
        	setLayout(b);
            addMouseListener(myMA);
            setBorder(blackBorder);
            setFocusable(true);
            thisId = id;
            thisPath = picPath;
            thisName = picName;
        }
    }
	
	class MouseAdapterMod extends MouseAdapter {
		
		Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
		Border redBorder = BorderFactory.createLineBorder(Color.CYAN,2);
		
	   	@Override public void mouseClicked(MouseEvent e){}
	   	@Override public void mousePressed(MouseEvent e){}
        @Override public void mouseReleased(MouseEvent e){}

        @Override
        public void mouseEntered(MouseEvent e)
        {
        	cp = (CustomPanel)e.getSource();
            cp.setBorder(redBorder);
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            cp.setBorder(blackBorder);
        }
   }
}
