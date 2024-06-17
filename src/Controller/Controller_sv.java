package Controller;

import java.awt.Color;
import java.awt.Font;
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

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import DAO.sv_dao;
import Model.SinhVien;
import View.Giaodien_Student;
public class Controller_sv implements ActionListener, TreeSelectionListener {
    private Giaodien_Student view;
    private File selectedDirectory = null;
    String filePath1="C:/Users/DELL/Downloads/file.txt";
    
    

    public File getSelectedDirectory() {
		return selectedDirectory;
	}

	public void setSelectedDirectory(File selectedDirectory) {
		this.selectedDirectory = selectedDirectory;
	}

	public Controller_sv(Giaodien_Student view) {
        this.view = view;
    }

    public void saveInformation() {
        String name = view.getNameTextField().getText();
        int age = Integer.parseInt(view.getAgeTextField().getText());
        float score = Float.parseFloat(view.getScoreTextField().getText());

        if (!isNameValid(name)) {
            JOptionPane.showMessageDialog(null, "Tên không hợp lệ", "Nhập lại", JOptionPane.PLAIN_MESSAGE);
        } else if (!isAgeValid(age)) {
            JOptionPane.showMessageDialog(null, "Độ tuổi không hợp lệ", "Nhập lại", JOptionPane.PLAIN_MESSAGE);
        } else if (!isScoreValid(score)) {
            JOptionPane.showMessageDialog(null, "Điểm không hợp lệ", "Nhập lại", JOptionPane.PLAIN_MESSAGE);
        } else {
            SinhVien sv = new SinhVien(name, score, age);
            sv_dao dao = new sv_dao();
            boolean exists = dao.check(name);
            if (!exists) {
                int check = dao.add(sv);
                if (check == 1) {
                    JOptionPane.showMessageDialog(null, "Lưu thông tin thành công", "Thành công", JOptionPane.PLAIN_MESSAGE);
                    view.showdata();
                    view.reset();
                    saveToFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Lưu thông tin thất bại", "Thất bại", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Tên đã tồn tại", "Thất bại", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    private boolean isNameValid(String name) {
        return name.length() > 4;
    }

    private boolean isAgeValid(int age) {
        return age > 17 && age < 26;
    }

    private boolean isScoreValid(float score) {
        return score >= 0 && score <= 10;
    }

    private void saveToFile() {
        if (selectedDirectory == null) {
            JOptionPane.showMessageDialog(null, "Chưa chọn thư mục để lưu", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        sv_dao svList = new sv_dao();
        java.util.List<SinhVien> ds = svList.selectAll();
        File file = new File(selectedDirectory, "SinhVien.txt");
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file))) {
            for (SinhVien sinhVien : ds) {
                writer.write("Name: " + sinhVien.getName() + "\n");
                writer.write("Age: " + sinhVien.getAge() + "\n");
                writer.write("Score: " + sinhVien.getDiem() + "\n\n");
            }
            JOptionPane.showMessageDialog(null, "Lưu thông tin thành công vào " + file.getAbsolutePath(), "Thành công", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi lưu thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("Save".equals(command)) {
            if (view.getNameTextField().getText().isEmpty() || view.getAgeTextField().getText().isEmpty()
                    || view.getScoreTextField().getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ các trường", "Lỗi",
                        JOptionPane.PLAIN_MESSAGE);
            } else {
                saveInformation();
            }
        } else if ("Choose Directory".equals(command)) {
        	JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
            	setSelectedDirectory(chooser.getSelectedFile());
                selectedDirectory = chooser.getSelectedFile();
            	//setSelectedDirectory(new File("C:\\Users\\DELL\\Downloads\\test"));
                saveToFile(selectedDirectory.toString(),filePath1);
                populateTree(selectedDirectory); 
            } else {
                JOptionPane.showMessageDialog(null, "Chưa chọn thư mục", "Lỗi", JOptionPane.PLAIN_MESSAGE);
            }
        } else if ("Lưu vào file trong thư mục".equals(command)) {
            if (selectedDirectory != null) {
                saveToFile();
                expandAllNodes(view.getTree());
            } else {
                JOptionPane.showMessageDialog(null, "Chưa chọn thư mục để lưu", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else if("Tăng size".equals(command)) {
        	
                    increaseFontSize();
              
        }
        else if("Giảm size".equals(command)) {
        	
                    decreaseFontSize();
               
        }
        else if("Thay đổi màu chữ".equals(command)) {
        	
                    changeFontColor();
        }
    }
    private void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
    private void populateTree(File directory) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(directory.getName());
        view.getTree().setModel(new javax.swing.tree.DefaultTreeModel(rootNode));
        addFilesToTree(rootNode, directory);
    }

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
    	 String escapedPath = readFromFile(filePath1).replace("\\", "\\\\");    	
    	setSelectedDirectory(new File(escapedPath));
        DefaultMutableTreeNode selectedNode = null;
        TreePath treePath = e.getPath(); 
        
       if (getSelectedDirectory() != null && treePath != null) {
            selectedNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

            String directoryPath = getSelectedDirectory().getAbsolutePath();
            StringBuilder filePathBuilder = new StringBuilder(directoryPath);
            Object[] nodes = treePath.getPath();
            for (int i = 1; i < nodes.length; i++) {
                filePathBuilder.append(File.separator).append(nodes[i].toString());
            }
            String filePath = filePathBuilder.toString().replaceAll("[\r\n]", "");
            try {
                if (selectedNode.toString().endsWith(".txt")) {
                    String content = readTextFile(filePath);
                    view.getTextarea().setText(content);
                } else if (selectedNode.toString().endsWith(".pdf")) {
                    String content = readPDFFile(filePath);
                    view.getTextarea().setText(content);
                } else if(selectedNode.toString().endsWith(".xml")) {
                	String content = readXML(filePath);
                    view.getTextarea().setText(content);
                } 
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "File not found: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error occurred while reading the file", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } else {
            JOptionPane.showMessageDialog(null, "Chưa chọn thư mục", "Lỗi", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public static String readXML(String path) {
        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            StringBuilder content = new StringBuilder();
            readNodeContent(document.getDocumentElement(), content, 0);

            return content.toString();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
            return "Error occurred while reading XML: " + ex.getMessage();
        }
    }

    private static void readNodeContent(Node node, StringBuilder content, int depth) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            for (int i = 0; i < depth; i++) {
                content.append("  ");
            }

            content.append("<").append(node.getNodeName()).append(">");

            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                content.append(" ").append(attribute.getNodeName()).append("=\"").append(attribute.getNodeValue()).append("\"");
            }

            if (node.hasChildNodes() && node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                content.append(": ").append(node.getFirstChild().getNodeValue().trim());
            }

            content.append("\n");

            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                readNodeContent(childNodes.item(i), content, depth + 1);
            }
        }
    }
    private String readTextFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private String readPDFFile(String filePath) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(filePath));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
   
    public static void saveToFile(String content, String filePath) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);        
            } catch (IOException e) {
        }
    }
    public static String readFromFile(String filePath) {
        StringBuilder content = new StringBuilder();
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc nội dung từ file: " + e.getMessage());
        }

        return content.toString();
    }
    private void increaseFontSize() {
        Font currentFont = view.getTextarea().getFont();
        int newSize = currentFont.getSize() + 2;
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), newSize);
        view.getTextarea().setFont(newFont);
    }

    private void decreaseFontSize() {
        Font currentFont = view.getTextarea().getFont();
        int newSize = Math.max(currentFont.getSize() - 2, 10); 
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), newSize);
        view.getTextarea().setFont(newFont);
    }

    private void changeFontColor() {
        Color newColor = JColorChooser.showDialog(null, "Choose Font Color", view.getTextarea().getForeground());
        if (newColor != null) {
        	view.getTextarea().setForeground(newColor);
        }
    }
}
