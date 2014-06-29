package bit.city;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class Start {
	
	private static int threads = 10;
	private static ExecutorService executorService = Executors.newFixedThreadPool(threads);
	private static AutoRegisterTask autoRegisterTask = new AutoRegisterTask();
	private static DefaultTableModel tableModel;
	private static JScrollBar scrollBar;
	private static Vector<Vector<String>> rows = new Vector<Vector<String>>();
	private static boolean autoRegister = false;
	
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception ex){
			Exceptions.handleException(ex);
		}
		
		JFrame frame = new JFrame("BitCity - Register");
		
		try{
			frame.setIconImages(Arrays.asList(new Image[]{ImageIO.read(Start.class.getResource("/icon128.png")), ImageIO.read(Start.class.getResource("/icon16.png"))}));
		}catch(Exception ex){
			Exceptions.handleException(ex);
		}
		
		JPanel buttonPanel = new JPanel();
		final JButton registerButton = new JButton("Register");
		final JToggleButton autoRegisterButton = new JToggleButton("Auto-Register");
		JButton userButton = new JButton("Users");
		
		buttonPanel.add(registerButton);
		buttonPanel.add(autoRegisterButton);
		buttonPanel.add(userButton);
		
		registerButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				registerUser();
				UserHtml.saveHtml();
			}
		});
		
		autoRegisterButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				autoRegister = autoRegisterButton.isSelected();
				
				registerButton.setEnabled(!autoRegister);
				
				if(autoRegister){
					for(int i = 0; i < threads; i++){
						executorService.submit(autoRegisterTask);
					}
				}else{
					UserHtml.saveHtml();
				}
			}
		});
		
		userButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Desktop desktop = Desktop.getDesktop();
				
				try{
					desktop.browse(new URL("http://localhost").toURI());
				}catch(Exception ex){
					Exceptions.handleException(ex);
				}
			}
		});
		
		Vector<String> columns = new Vector<String>();
		
		columns.add("#");
		columns.add("Vorname");
		columns.add("Nachname");
		columns.add("Username");
		columns.add("Email");
		columns.add("Passwort");
		
		tableModel = new DefaultTableModel(rows, columns);
		final JTable userTable = new JTable(tableModel){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		
		userTable.getTableHeader().setReorderingAllowed(false);
		userTable.getColumn("#").setMaxWidth(35);
		userTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e){
				int row = userTable.getSelectedRow();
				
				if(row != -1){
					String username = (String)userTable.getValueAt(row, 3);
					String password = (String)userTable.getValueAt(row, 5);
					String javaScript = "javascript:$(\"input[name='username']\").val(\"" + username + "\");$(\"input[name='password']\").val(\"" + password + "\");$(\"input[type='submit']\").click();";
					StringSelection selection = new StringSelection(javaScript);
					Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
					
					clipBoard.setContents(selection, selection);
				}
			}
		});
		
		JScrollPane scrollPanel = new JScrollPane(userTable);
		scrollBar = scrollPanel.getVerticalScrollBar();
		
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		final JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 101, threads);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		
		labelTable.put(new Integer(1), new JLabel("1"));
		
		for(int i = 11; i <= 101; i += 10){
			labelTable.put(new Integer(i), new JLabel("" + (i - 1)));
		}
		
		slider.setMajorTickSpacing(10);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setLabelTable(labelTable);
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				threads = slider.getValue();
				executorService = Executors.newFixedThreadPool(threads);
			}
		});
		
		frame.add(buttonPanel, BorderLayout.NORTH);
		frame.add(scrollPanel, BorderLayout.CENTER);
		frame.add(slider, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new UserServer();
	}
	
	public static boolean getAutoRegister(){
		return autoRegister;
	}

	public static void registerUser(){
		User user = new User();
		
		try{
			Request.Post("http://bitcity.tinf13b4.de/BitCity/?site=register")
				.bodyForm(Form.form()
					.add("vorname", user.getFirst())
					.add("nachname", user.getLast())
					.add("username", user.getUsername())
					.add("mail", user.getEmail())
					.add("password", user.getPassword())
					.add("password_check", user.getPassword())
				.build())
				.execute()
				.discardContent();
		}catch(Exception ex){
			Exceptions.handleException(ex);
		}
		
		rows.add(user.toVector());
		tableModel.fireTableDataChanged();
		scrollBar.setValue(scrollBar.getMaximum());
	}
	
}
