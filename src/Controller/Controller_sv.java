package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import DAO.sv_dao;
import Model.SinhVien;
import View.Giaodien_Student;

public class Controller_sv implements ActionListener,TreeSelectionListener{
 private SinhVien sv;
 private Giaodien_Student view;
 public Controller_sv(Giaodien_Student view) {
	 this.view=view;	 
 }
 
 public void Savein4(){
	 String name=view.getNameTextField().getText();
	 int age=Integer.parseInt(view.getAgeTextField().getText());
	 float score=Float.parseFloat(view.getScoreTextField().getText());
	 
	 if(name.length()<=4) {
		 JOptionPane.showMessageDialog(null, "Unfomat Name", "Enter again", JOptionPane.PLAIN_MESSAGE);
	 }
	 else if(age<=17||age>=26) {
		 JOptionPane.showMessageDialog(null, "Do tuoi danh cho sinh vien ko hop le", "Enter again", JOptionPane.PLAIN_MESSAGE);
	 }
	 else if(score<0||score>10) {
		 JOptionPane.showMessageDialog(null, "So diem nhap ko hop le", "Enter again", JOptionPane.PLAIN_MESSAGE);
	 }
	 else {
		 SinhVien sv=new SinhVien(name,score,age);
		 sv_dao dao=new sv_dao();
		 boolean c=dao.check(name);
		 if(c==false) {
			 int check=dao.add(sv);
			 if(check==1) {
				 JOptionPane.showMessageDialog(null, "Success save information", "Successs", JOptionPane.PLAIN_MESSAGE);
				 view.showdata();
			     view.reset();
		    sv_dao svList=new sv_dao();
			 List<SinhVien> ds=svList.selectAll();
			 String folder="D://Folder//Directory";
			 File fol=new File(folder);
			 if(!fol.exists()) {
				 boolean cre=fol.mkdirs();
			 }
		     File file = new File("D://Folder//Directory/SinhVien.txt");
		     if(file.exists()) {
		    	 file.delete();
		    	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		             for (SinhVien sinhVien : ds) {
		                 writer.write("Name: " + sinhVien.getName() + "\n");
		                 writer.write("Age: " + sinhVien.getAge() + "\n");
		                 writer.write("Score: " + sinhVien.getDiem() + "\n\n");
		             }
		         } catch (IOException e1) {
		             e1.printStackTrace();
		             JOptionPane.showMessageDialog(null, "Error occurred while saving information", "Error", JOptionPane.ERROR_MESSAGE);
		         }
		     }
		     else { 
		    	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		             for (SinhVien sinhVien : ds) {
		                 writer.write("Name: " + sinhVien.getName() + "\n");
		                 writer.write("Age: " + sinhVien.getAge() + "\n");
		                 writer.write("Score: " + sinhVien.getDiem() + "\n\n");
		             }
		         } catch (IOException e1) {
		             e1.printStackTrace();
		             JOptionPane.showMessageDialog(null, "Error occurred while saving information", "Error", JOptionPane.ERROR_MESSAGE);
		         }
		     }
			 }
			 else {
				 JOptionPane.showMessageDialog(null, "Fail save information", "Fail", JOptionPane.PLAIN_MESSAGE);
				 view.showdata();
			 }
		 }
		 else {
			 JOptionPane.showMessageDialog(null, "Ten da ton tai", "Fail", JOptionPane.PLAIN_MESSAGE);
		 } 
	 }
 }

@Override
public void actionPerformed(ActionEvent e) {
	if(e.getActionCommand().equals("Save")) {
		if(view.getNameTextField().getText().equals("")||view.getAgeTextField().getText().equals("")||view.getScoreTextField().getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Vui long nhap cac truong", "Fail", JOptionPane.PLAIN_MESSAGE);
		}
		else {
			Savein4();
		}
	}
	else if(e.getActionCommand().equals("Choose Directory")) {
		JFileChooser choose=new JFileChooser();
		//FileNameExtensionFilter filter=new FileNameExtensionFilter("Tệp văn bản (*.txt)","txt");
		choose.setFileSelectionMode(choose.DIRECTORIES_ONLY);
		int result = choose.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = choose.getSelectedFile();
            populateTree(selectedDirectory);
        }
	}
	
}

 private void populateTree(File directory) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(directory.getName());
        view.getTree().setModel(new DefaultTreeModel(rootNode));
        addFilesToTree(rootNode, directory);
    }
//de quy
 private void addFilesToTree(DefaultMutableTreeNode parentNode, File directory) {
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
                parentNode.add(node);
                if (file.isDirectory()) {
                    addFilesToTree(node, file);
                }
            }
        }
    }

 @Override
 public void valueChanged(TreeSelectionEvent e) {
     DefaultMutableTreeNode selectedNode =null;
     TreePath tree=view.getTree().getSelectionPath();
     if(tree!=null) {
    	 selectedNode= (DefaultMutableTreeNode) view.getTree().getLastSelectedPathComponent();
    	 if(selectedNode.toString().equals("SinhVien.txt")) {
        	 String filePath = "D://Folder//Directory/SinhVien.txt";

    	        try (FileReader fileReader = new FileReader(filePath);
    	             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
    	        	StringBuilder content = new StringBuilder();
    	            String line;
    	            while ((line = bufferedReader.readLine()) != null) {
    	                content.append(line).append("\n"); 
    	            }
    	            view.getTextarea().setText(content.toString());

    	        } catch (IOException e2) {
    	            e2.printStackTrace();
    	        }
         }
     }
     }
};



