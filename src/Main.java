import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import index.Index;
import search.SearchIndex;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class Main {

	private JFrame frame;
	
	private JTextField[] tfs;
	private JTextArea tarea;
	
	private final String[] attrs ={
		"marca",
		"modelo",
		"versao",
		"ano",
		"cilindradas"
	};
	
	Index index;
	SearchIndex search;
	
	/**
	 * Launch the application.
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main(new File("documents/processed/"));
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public Main(File docs) throws IOException {
		index = Index.createIndexFromDocuments(docs);
		search = new SearchIndex(index); 
		
		index.saveIndex(new File("full/"), false);
		index.saveIndex(new File("compress/"), true);
		
		initialize();
	}

	public JTextField createLabelAndField(String name, int desloc){
		JLabel label = new JLabel(name);
		label.setBounds(65, desloc, 100, 14);
		frame.getContentPane().add(label);
		
		JTextField tf = new JTextField();
		tf.setBounds(128, desloc, 500, 20);
		frame.getContentPane().add(tf);
		tf.setColumns(30);
		
		return tf;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		tfs = new JTextField[attrs.length];
		
		for(int x=0;x<attrs.length;x++){
			tfs[x] = createLabelAndField(attrs[x],50 + x*20);
		}
		
		JButton buscar = new JButton("busca sem tfidf");
		buscar.setBounds(65, 50 + attrs.length*20, 130, 23);
		frame.getContentPane().add(buscar);
		
		JButton rankear = new JButton("busca com tfidf");
		rankear.setBounds(200, 50 + attrs.length*20, 130, 23);
		frame.getContentPane().add(rankear);
		
		tarea= new JTextArea();
		
		JScrollPane scroll = new JScrollPane (tarea, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		scroll.setBounds(65, 50 + (attrs.length+1)*20 + 20, 600, 400);
		
		frame.getContentPane().add(scroll);
		
		buscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				List<String> query = new ArrayList<String>();
				
				String[] input;
				
				for(int x=0;x<attrs.length;x++){
					input = tfs[x].getText().split(" ");
					
					for(int y=0;y<input.length;y++){
						query.add(index.createLabel(attrs[x], input[y]));
					}
				}
				
				String[] res = search.search(query);
				
				String output = "";
				
				for(String doc : res){
					File file = new File(doc);
					
					Scanner in;
					try {
						in = new Scanner(file);
						output += "Doc: " + doc+"\n";
						while(in.hasNextLine()){
							output+=in.nextLine() + "\n";
						}
						output+="\n";
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				tarea.setText(output);
				
			}
		});
		
		
		rankear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				List<String> query = new ArrayList<String>();
				
				String[] input;
				
				for(int x=0;x<attrs.length;x++){
					input = tfs[x].getText().split(" ");
					
					for(int y=0;y<input.length;y++){
						query.add(index.createLabel(attrs[x], input[y]));
					}
				}
				
				String[] res = search.rankedSearch(query);
				
				String output = "";
				
				for(String doc : res){
					File file = new File(doc);
					
					Scanner in;
					try {
						in = new Scanner(file);
						output += "Doc: " + doc+"\n";
						while(in.hasNextLine()){
							System.out.println(output);
							output+=in.nextLine() + "\n";
						}
						output+="\n";
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				tarea.setText(output);
				
			}
		});
		
		
	}
}
