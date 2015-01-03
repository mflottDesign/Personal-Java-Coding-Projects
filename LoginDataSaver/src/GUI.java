import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame implements ActionListener {

	private ArrayList<UserPass> upList = new ArrayList<UserPass>();
	private DefaultTableModel tableModel = new DefaultTableModel();
	private JTable upTable = new JTable(tableModel);
	private JButton search = new JButton("Search");
	private JButton addNew = new JButton("Add");
	private JButton remove = new JButton("Remove");
	private JTextPane searchPane = new JTextPane();
	private int isSelectedIndex = -1;
	private ListSelectionModel selectionModel;
	private Boolean isDeleting = false;

	public GUI() {
		this.setSize(750, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		BorderLayout b = new BorderLayout();
		this.setLayout(b);

		JPanel topPanel = new JPanel();
		FlowLayout f = new FlowLayout();
		topPanel.setLayout(f);
		this.add(topPanel, BorderLayout.NORTH);

		JPanel bottomPanel = new JPanel();
		FlowLayout f2 = new FlowLayout();
		bottomPanel.setLayout(f2);
		this.add(bottomPanel, BorderLayout.SOUTH);

		searchPane.setPreferredSize(new Dimension(300, 24));
		topPanel.add(searchPane);
		topPanel.add(search);
		upTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(upTable);
		this.add(scrollPane, BorderLayout.CENTER);
		bottomPanel.add(addNew);
		bottomPanel.add(remove);

		// Adds actionListeners to objects
		search.addActionListener(this);
		addNew.addActionListener(this);
		remove.addActionListener(this);

		// Adds the columns to the DefaultTableModel
		tableModel.addColumn("Username");
		tableModel.addColumn("Password");
		tableModel.addColumn("Location");

		upTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionModel = upTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!isDeleting) {
					handleSelectionEvent(e);
				}
			}
		});

		getFile();

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource().equals(search)) {
			search(searchPane.getText());
		} else if (arg0.getSource().equals(addNew)) {
			JTextField uField = new JTextField();
			JTextField pField = new JTextField();
			JTextField lField = new JTextField();
			Object[] message = { "Username:", uField, "Password:", pField,
					"Location:", lField, };
			int option = JOptionPane.showConfirmDialog(this, message,
					"Enter the credentials", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				UserPass p = new UserPass();
				p.setUsername(uField.getText());
				p.setPassword(pField.getText());
				p.setLocation(lField.getText());
				upList.add(p);
				tableModel.addRow(new Object[] { p.getUsername(),
						p.getPassword(), p.getLocation() });
				addToFile(p);
			}
		} else {
			try {
				isDeleting = true;
				if (isSelectedIndex != -1) {
					removeFromFile(isSelectedIndex);
				} else {
					isDeleting = false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void search(String word) {
		// Iterates through the table rows and checks if there is any row
		// containing the search word
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			String tableRowContent = tableModel.getValueAt(i, 0) + " "
					+ tableModel.getValueAt(i, 1) + " "
					+ tableModel.getValueAt(i, 2);
			if (tableRowContent.contains(word)) {
				upTable.setRowSelectionInterval(0, i);
			}
		}
	}

	public void getFile() {
		// Checks if the user is running the program as an admin - admin rights
		// needed to save a file to C:/
		try {
			String command = "reg query \"HKU\\S-1-5-19\"";
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor(); // Wait for for command to finish
			int exitValue = p.exitValue(); // If exit value 0, then admin user.

			if (0 == exitValue) {
				System.out.println("admin user");
			} else {
				System.out.println("non admin user");
				JOptionPane
						.showMessageDialog(null,
								"You are not an admin user. Please run the program with administrator rights.");
				System.exit(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			File file = new File("c:/personalCredentials.txt");
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				String line = null;
				while ((line = br.readLine()) != null) {
					String[] strs = line.split("\\s+");

					UserPass up = new UserPass();
					up.setUsername(strs[0]);
					up.setPassword(strs[1]);
					up.setLocation(strs[2]);
					upList.add(up);
					tableModel.addRow(new Object[] { up.getUsername(),
							up.getPassword(), up.getLocation() });
				}
				br.close();
			} else {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addToFile(UserPass up) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("c:/personalCredentials.txt", true)))) {
			out.println(up.getUsername() + " " + up.getPassword() + " "
					+ up.getLocation());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeFromFile(int selectedUserPass) throws IOException {
		File oldFile = new File("c:/personalCredentials.txt");
		File newFile = new File("c:/tempPersonalCredentials.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(oldFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

			String lineToRemove = upList.get(selectedUserPass).getUsername()
					+ " " + upList.get(selectedUserPass).getPassword() + " "
					+ upList.get(selectedUserPass).getLocation();
			System.out.println(lineToRemove);
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				String trimmedLine = currentLine.trim();
				if (trimmedLine.equals(lineToRemove))
					continue;
				writer.write(currentLine + "\n");
			}
			writer.close();
			reader.close();
			oldFile.delete();
			newFile.renameTo(oldFile);

			tableModel.removeRow(selectedUserPass);
			upList.remove(selectedUserPass);

			isDeleting = false;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleSelectionEvent(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		} else if (!e.getValueIsAdjusting()) {
			String strSource = e.getSource().toString();
			int start = strSource.indexOf("{") + 1;
			int stop = strSource.length() - 1;
			isSelectedIndex = Integer
					.parseInt(strSource.substring(start, stop));
			System.out.println(isSelectedIndex);
		}
	}
}
