package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionListener;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import Controller.Controller_sv;
import DAO.sv_dao;
import Model.SinhVien;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class Giaodien_Student{

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton btnNewButton ;
	private JLabel lblNewLabel_1;
	private  JTable table;
	private JTree tree;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Giaodien_Student window = new Giaodien_Student();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Giaodien_Student() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 900, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Sinh vien");
		lblNewLabel.setBounds(41, 11, 64, 22);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(59, 44, 96, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		 lblNewLabel_1 = new JLabel("Name");
		lblNewLabel_1.setBounds(0, 47, 49, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Age");
		lblNewLabel_1_1.setBounds(0, 88, 49, 14);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(59, 85, 96, 20);
		frame.getContentPane().add(textField_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Score\r\n");
		lblNewLabel_1_1_1.setBounds(0, 126, 49, 14);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(59, 123, 96, 20);
		frame.getContentPane().add(textField_2);
        String[] columnNames = {"Name", "Age", "Score"};
        table = new JTable();
        table.setModel(new DefaultTableModel(
    			new Object[][] {
    				{null, null, null},
    			},
    			columnNames
    		));
		table.getColumnModel().getColumn(0).setMinWidth(20);
        showdata();
        
	        
	        JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setSize(230, 200);
	        scrollPane.setLocation(200, 30);
	        
	        frame.getContentPane().add(scrollPane);
	       
	        ActionListener ac=new Controller_sv(this);
	        
	        btnNewButton = new JButton("Save");
	        btnNewButton.setBounds(33, 174, 89, 23);
	        btnNewButton.addActionListener(ac);
	        frame.getContentPane().add(btnNewButton);
	        
	        JButton choose=new JButton("Choose Directory");
	        choose.setBounds(476, 39, 150, 30);
	        choose.addActionListener(ac);
	        
	         tree=new JTree();
	        tree.setBounds(450, 100, 200, 100);
	        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
	        DefaultTreeModel treeModel = new DefaultTreeModel(root);
	        tree.setModel(treeModel);
	        
	        TreeSelectionListener select=new Controller_sv(this);
	        tree.addTreeSelectionListener(select);
	        
	        frame.getContentPane().add(choose);
	        frame.getContentPane().add(tree);
	        
	        textArea = new JTextArea();
	        textArea.setBounds(670, 30, 206, 222);
	        JScrollPane scroll=new JScrollPane(textArea);
	        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        scroll.setBounds(670,30,206,222);
	        frame.getContentPane().add(scroll);
	        
	        JLabel lblNewLabel_2 = new JLabel("Content of the file");
	        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
	        lblNewLabel_2.setBounds(704, 11, 172, 15);
	        frame.getContentPane().add(lblNewLabel_2);
            try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	public JTextArea getTextarea() {
		return textArea;
	}
	public JTree getTree() {
		return tree;
	}
	public JTextField getNameTextField() {
        return textField;
    }

    public JTextField getAgeTextField() {
        return textField_1;
    }

    public JTextField getScoreTextField() {
        return textField_2;
    }
    public void reset() {
    	textField.setText(null);
    	textField_1.setText(null);
    	textField_2.setText(null);
    }
    public void showdata() {
    	try {
            String[] columnNames = {"Name", "Age", "Score"};
			DefaultTableModel model = new DefaultTableModel(columnNames, 0);
			Connection connection = ConnectMysql.Connectmysql.getConnection();
			String query = "SELECT * FROM sv";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			List<Vector<Object>> rows = new Vector<>();

	        while (rs.next()) {
	            String name = rs.getString("name");
	            int age = rs.getInt("age");
	            float score = rs.getFloat("score");

	            Vector<Object> row = new Vector<>();
	            row.add(name);
	            row.add(age);
	            row.add(score);
	            
	            rows.add(row);
	        }
	        rows.sort(Comparator.comparing(row -> (Float) row.get(2)));
	        List<Vector<Object>> distinctRows = rows.stream()
	                                                .distinct()
	                                                .collect(Collectors.toList());

	        for (Vector<Object> row : distinctRows) {
	             model.addRow(row);
	        }
		        ConnectMysql.Connectmysql.close(connection);
				table.setModel(model);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
    	
    }
}
