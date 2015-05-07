import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class LekeXMLEditor extends JFrame implements Serializable,ActionListener
{
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private FlowLayout switchPanelLayout;
	private JMenuBar menuBar=new JMenuBar(); //remember we add  Menu to MenuBar and MenuItem
	private JPanel menuBarPanel=new JPanel();
	private JMenu fileMenu = new JMenu("File");
	private JMenu editMenu=new JMenu("Edit");
	private JMenu helpMenu=new JMenu("Help");
	private JMenuItem newMenuItem= new JMenuItem("New");
	private JMenuItem openMenuItem= new JMenuItem("Open");
	private JMenuItem saveMenuItem=new JMenuItem("Save");
	private JMenuItem saveAsMenuItem= new JMenuItem("Save As");
	private JMenuItem quitMenuItem= new JMenuItem("Quit");
	private JMenuItem undoMenuItem=new JMenuItem("Undo");
	private JMenuItem redoMenuItem=new JMenuItem("Redo");
	private JMenuItem cutMenuItem=new JMenuItem("Cut");
	private JMenuItem copyMenuItem=new JMenuItem("Copy");
	private JMenuItem pasteMenuItem=new JMenuItem("Paste");
	private JMenu addMenu=new JMenu("Add");
	private JMenuItem attributeMenuItem=new JMenuItem("Attribute");
	private JMenuItem elementMenuItem= new JMenuItem("Element");
	private JMenuItem textMenuItem=new JMenuItem("Text");
	private JMenuItem aboutMenuItem= new JMenuItem("About");
	private JToolBar jToolBar=new JToolBar("XMLEditor ToolBar");
	private JButton newButton= new JButton("New");
	private JButton cutButton=new JButton("Cut");
	private JButton copyButton=new JButton("Copy");
	private JButton pasteButton= new JButton("Paste");
	private JButton attributeButton= new JButton("Attribute");
	private JButton elementButton= new JButton("Element");
	private JButton textButton= new JButton("Text");
	private JButton quitButton= new JButton("Quit");
	private JButton openButton= new JButton("Open");
	private JButton saveButton= new JButton("Save");
	private JButton undoButton= new JButton("Undo");
	private JButton redoButton = new JButton("Redo");
	private JTextArea xmlTextArea=new JTextArea(150,150); 
	private JScrollPane scrollPane= new JScrollPane(xmlTextArea);
	private JScrollPane treeScrollPane;
	private JPanel textAreaPanel=new JPanel();
	private JPanel switchPanel=new JPanel();
	private JPanel jTreePanel=new JPanel();
	private JLabel switchToLabel=new JLabel("Switch To:");
	private JButton switchButton=new JButton("JTree");
	private JTree tree;
	private JTabbedPane tabbedPanel=new JTabbedPane();
	private TreePath path;
	private TreePath[] paths;
	private static JFileChooser fileChooser= new JFileChooser(new File(""));
	private DefaultMutableTreeNode root;
	private String treeRootString;
	private TreeModel treeModel;
	private DefaultMutableTreeNode copiedNode=new DefaultMutableTreeNode();
	private Document document=null;
	private SAXBuilder saxBuilder=new SAXBuilder();
	private DefaultTreeModel model;
	private DefaultTreeModel undoModel;
	private DefaultTreeModel redoModel;
	private Element rootElement=null;
	private String fileName = null;
	private File file = null;
	private Object rootObject;
	private DefaultTreeModel openModel;
	private boolean isJTreeInPanel=false;
	private String fileNameExists;
	
	
	
	
	//private Action quitAction;
	

	private LekeXMLEditor()
	{
		super("GUI XML Editor");
		menuBarPanel.setLayout(new BorderLayout());
		
		//position the menu Bar and jToolBar to sit in the North and Center of the Panel
		menuBarPanel.add(menuBar, BorderLayout.NORTH);
		menuBarPanel.add(jToolBar,BorderLayout.CENTER);
		
		//add the menu components to the MenuBar
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		
		//add menu items to the MenuBar
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(quitMenuItem);
		
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(cutMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);
		editMenu.add(addMenu);
		
		addMenu.add(attributeMenuItem);
		addMenu.add(elementMenuItem);
		addMenu.add(textMenuItem);
		
		helpMenu.add(aboutMenuItem);
			
		//add the buttons to the JToolBar
		jToolBar.add(newButton);
		jToolBar.addSeparator();
		jToolBar.add(openButton);
		jToolBar.addSeparator();
		jToolBar.add(saveButton);
		jToolBar.addSeparator();
		jToolBar.add(undoButton);
		jToolBar.add(redoButton);
		jToolBar.addSeparator();
		jToolBar.add(cutButton);
		jToolBar.add(copyButton);
		jToolBar.add(pasteButton);
		jToolBar.addSeparator();
		jToolBar.add(attributeButton);
		jToolBar.add(elementButton);
		jToolBar.add(textButton);
		jToolBar.addSeparator();
		jToolBar.add(quitButton);
		
		//add the JTree to the panel
		/**treeRootString="root";
		//root=new DefaultMutableTreeNode(treeRootString);
		jTreePanel.setLayout(new BorderLayout(1,1));
		jTreePanel.add(new JScrollPane(tree));
		**/
		jTreePanel.setLayout(new BorderLayout(1,1));
		
		//tree.setVisibleRowCount(3);
		
		//work on the textArea
		xmlTextArea.setEditable(false);
		xmlTextArea.setWrapStyleWord(true);
		
		//design the attributes of the textArea and JTree Panels
		textAreaPanel.setLayout(new BorderLayout(1,1));
		textAreaPanel.add(scrollPane);
		
		//tabbed Panel Layout
		tabbedPanel.add(jTreePanel,"JTree");
		tabbedPanel.add(textAreaPanel,"Text Area");
		tabbedPanel.setToolTipTextAt(0,"Displays's JTree");
		tabbedPanel.setToolTipTextAt(1,"Text Area");
		
		//add the JLaabel and the ProcessButton to the processButtonPanel
		switchPanelLayout= new FlowLayout(2);
		switchPanel.setLayout(switchPanelLayout);
		switchPanel.add(switchToLabel);
		switchPanel.add(switchButton);
		
		//disable all buttons
		enableButtons(false);
		
		//add the File Menu Mnemonics
		fileMenu.setMnemonic('F');
		editMenu.setMnemonic('E');
		helpMenu.setMnemonic('A');
		
		//set Keyboard Accelerators
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,ActionEvent.CTRL_MASK));
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,ActionEvent.CTRL_MASK));
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK));
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK));
		elementMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,ActionEvent.CTRL_MASK));
		attributeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,ActionEvent.CTRL_MASK));
		textMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,ActionEvent.CTRL_MASK));
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,ActionEvent.CTRL_MASK));
		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,ActionEvent.CTRL_MASK));
		
		
		
		
		//set the MenuItem TooltipTexts
		saveButton.setToolTipText("Save");
		openButton.setToolTipText("Open");
		openMenuItem.setToolTipText("Open");
		newButton.setToolTipText("Create New XML File");
		pasteButton.setToolTipText("Paste");
		elementButton.setToolTipText("Add Element");
		quitButton.setToolTipText("Quit");
		attributeButton.setToolTipText("Add Attribute");
		textButton.setToolTipText("Add Text");
		cutButton.setToolTipText("Cut");
		undoButton.setToolTipText("Undo");
		redoButton.setToolTipText("Redo");
		copyButton.setToolTipText("Copy");
		
		
		
		
		//add the panels to the JFrame
		add(menuBarPanel,BorderLayout.NORTH);
		add(tabbedPanel,BorderLayout.CENTER);
		add(switchPanel,BorderLayout.SOUTH);
		
		//change the look and feel to nimbus
				try{
					for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels())
					{
						if("Nimbus".equals(info.getName()))
						
						{
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				}
				catch(Exception e)
				{
					//if Nimbus is not available then use the default Look and feel
				}
		
		//start adding the Listeners
		openMenuItem.addActionListener(this);
		openButton.addActionListener(this);
		saveMenuItem.addActionListener(this);
		saveButton.addActionListener(this);
		saveAsMenuItem.addActionListener(this);
		quitMenuItem.addActionListener(this);
		quitButton.addActionListener(this);	
		switchButton.addActionListener(this);
		elementButton.addActionListener(this);
		elementMenuItem.addActionListener(this);
		cutMenuItem.addActionListener(this);
		cutButton.addActionListener(this);
		newMenuItem.addActionListener(this);
		newButton.addActionListener(this);
		copyButton.addActionListener(this);
		copyMenuItem.addActionListener(this);
		pasteButton.addActionListener(this);
		pasteMenuItem.addActionListener(this);
		textMenuItem.addActionListener(this);
		textButton.addActionListener(this);
		attributeMenuItem.addActionListener(this);
		attributeButton.addActionListener(this);
		aboutMenuItem.addActionListener(this);
		undoButton.addActionListener(this);
		undoMenuItem.addActionListener(this);
		redoButton.addActionListener(this);
		redoMenuItem.addActionListener(this);
		
	}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==newMenuItem||e.getSource()==newButton)
			{
				 try{
						newMenuAction();
					}
				catch(Exception exception)
				{}
			}
			//create the open button listener
			
			if(e.getSource()==undoButton||e.getSource()==undoMenuItem)
			{
				try
				{
					if(tree.getModel()!=null)
					{
						redoModel=(DefaultTreeModel) tree.getModel();
					}
					//

					if(undoModel!=null)					
					{
						tree.setModel(undoModel);
						expandTreeNodes();	
						tree.updateUI();
					}
					//undoModel=new DefaultTreeModel(getTree(rootElement));
					//path=tree.getSelectionPath();
					//expandTreeNodes();	
					//tree.updateUI();
				}
				catch(Exception exception)
				{}				
			}
			if(e.getSource()==redoButton||e.getSource()==redoMenuItem)
			{
				try
				{
					if(redoModel!=null)					
					{
						tree.setModel(redoModel);
						expandTreeNodes();	
						tree.updateUI();
					}
					//undoModel=new DefaultTreeModel(getTree(rootElement));
					//tree.setModel(redoModel);
					//redoModel=new DefaultTreeModel(getTree(rootElement));
					//path=tree.getSelectionPath();
					//expandTreeNodes();	
					//tree.updateUI();
				}
				catch(Exception exception)
				{}
			}
			if(e.getSource()==openMenuItem || e.getSource()==openButton)
			{
				
				try
				{
					int value=JOptionPane.showOptionDialog(null,"Select an Option", "Open an XML File", JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Local","Open Link","Cancel"},"Local");
					//Local was chosen
					if(value==0){
						FileNameExtensionFilter filter= new FileNameExtensionFilter("XML Files", "xml");
						fileChooser.setFileFilter(filter);		
						fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);


						@SuppressWarnings("unused")
						int returnVal=fileChooser.showOpenDialog(null);
						File sampleXML=new File(fileChooser.getSelectedFile().getAbsolutePath());
						document=saxBuilder.build(sampleXML);
						XMLOutputter xmlOutput=new XMLOutputter(Format.getPrettyFormat());
						

						if(returnVal==fileChooser.APPROVE_OPTION)
						{
							enableButtons(true);
							xmlTextArea.setText("");
							xmlTextArea.append(xmlOutput.outputString(document));//append the text to the Text Area.

							//if the file name is not empty then set the title at the top.
							if(fileChooser.getSelectedFile().getName() != null)
							{
								frame.setTitle("GUI XML Editor- " + fileChooser.getSelectedFile().getName());
								fileNameExists=fileChooser.getSelectedFile().getName();

							}
							rootElement=document.getRootElement();
							DefaultTreeModel treeModel=new DefaultTreeModel(getTree(rootElement));
							tree=new JTree(treeModel);
							
							if(isJTreeInPanel==false)
							{
								jTreePanel.add(new JScrollPane(tree));
								tree.setBackground(Color.LIGHT_GRAY);
							}
							else 
							{
								jTreePanel.removeAll();
								remove(tree);
								tree.setModel(treeModel);
								jTreePanel.add(new JScrollPane(tree));
								tree.setBackground(Color.LIGHT_GRAY);
							}
							//	tree.setModel(treeModel);
							revalidate();
							expandTreeNodes();	
							tree.updateUI();
							isJTreeInPanel=true; //the open button has been pressed
							//buildTree(getTree());
						}
						else if(returnVal==fileChooser.CANCEL_OPTION){}
					}//end if

					else if(value==1) //web Link was chosen
					{
						try
						{
							String webLink=JOptionPane.showInputDialog(null, "Please Enter a URL"); 
							if(webLink!=null)
							{
								URL url=new URL(webLink);
								document=saxBuilder.build(url);
								XMLOutputter xmlOutput=new XMLOutputter(Format.getPrettyFormat());
								xmlTextArea.setText("");
								xmlTextArea.append(xmlOutput.outputString(document));
								rootElement=document.getRootElement();
								DefaultTreeModel treeModel=new DefaultTreeModel(getTree(rootElement));
								tree=new JTree(treeModel);
								enableButtons(true);
								
								if(isJTreeInPanel==false)
								{
									jTreePanel.add(new JScrollPane(tree));
									tree.setBackground(Color.LIGHT_GRAY);
								}
								else 
								{
									jTreePanel.removeAll();
									remove(tree);
									tree.setModel(treeModel);
									jTreePanel.add(new JScrollPane(tree));
									tree.setBackground(Color.LIGHT_GRAY);
								}
								//	tree.setModel(treeModel);
								revalidate();
								expandTreeNodes();	
								tree.updateUI();
								isJTreeInPanel=true; //the open button has been pressed
							}
						}
						catch(JDOMException jdomException)
						{
							JOptionPane.showMessageDialog(frame, "There was an Issue with parsing the URL:Check URL or Internet Connection");
						}
					}
			    }//end try
				catch(FileNotFoundException fileNotFound)
				{
					
				}
				catch(IOException ioException)
				{
					
				}
				catch(Exception exception)
				{
					//JOptionPane.showMessageDialog(null,"Choose a Valid XML File");
				}//end catch block
			}
			
			//create listeners for the save button
			if(e.getSource()==saveButton||e.getSource()==saveMenuItem)
			{
				if(fileNameExists!=null)
				{
					//saveAsMethodAction();
					//TreeModel model=tree.getModel();
					try
					{
						fileName=fileChooser.getSelectedFile().getAbsolutePath();
						file=new File(fileName);
						file.delete();
						File tempFile=new File(fileName);
						
						if(!tempFile.exists()){
						tempFile.createNewFile();
						}
						//tempFile.
						//System.out.println("I am pressed");
						FileWriter filewriter=new FileWriter(tempFile,false);
						BufferedWriter bufferWriter=new BufferedWriter(filewriter);
						XMLOutputter xmloutput=new XMLOutputter();
						xmloutput.setFormat(Format.getPrettyFormat());
						xmloutput.output(rootElement,bufferWriter);
						//output.close();
						tempFile.renameTo(new File(fileName));
						bufferWriter.close();
						
						JOptionPane.showMessageDialog(frame, fileChooser.getSelectedFile().getName()+" has been Saved");
					}
					catch(Exception ex){}
				}
				else
					saveAsMethodAction();
					//JOptionPane.showMessageDialog(null, "File Saved");
			}
			
			if(e.getSource()==saveAsMenuItem)
			{
				saveAsMethodAction();
			}
			//create the quit button listener
			if(e.getSource()==quitMenuItem || e.getSource()==quitButton)
			{
				performQuitAction();
			}
			if(e.getSource()==switchButton)
			{
				switchButtonAction();
			}//end if
			
			//add event for the Element Buttons
			if(e.getSource()==elementMenuItem||e.getSource()==elementButton)
			{
				elementMethodAction();
			}//end listener for element Button
			if(e.getSource()==attributeMenuItem||e.getSource()==attributeButton)
			{
				addAttributeMethodAction();
			}
				
			if(e.getSource()==textMenuItem||e.getSource()==textButton)
			{
				try{
					path=tree.getSelectionPath();
					if(!isJTreePanel())//if the panel being displayed is not the JTreePanel the show a message dialog prompting to switch to JTreePanel
					{
						JOptionPane.showMessageDialog(frame, "Switch to JTree to add New Text");
					}

					if(path==null && isJTreePanel()) //if node/element is selected while the Jtree Panel is visible
					{
						JOptionPane.showMessageDialog(null, "You must select a Node before You can Add an Text");
					}

					else if(path!=null)
					{
						//prompt user to input values for Text
						undoModel=new DefaultTreeModel(getTree(rootElement));
						String input=JOptionPane.showInputDialog(null, "Please Enter in an Attribute to Add");

						if(input!=null)
						{
							DefaultMutableTreeNode parent=(DefaultMutableTreeNode) path.getLastPathComponent();
							DefaultMutableTreeNode childText=new DefaultMutableTreeNode(new Text(input));
							parent.add(childText);
							tree.updateUI();
							tree.expandPath(path);
						}

					}
					redoModel=new DefaultTreeModel(getTree(rootElement));
				}
				catch(Exception exception)
				{
					
				}
			}
			
			if(e.getSource()==cutMenuItem||e.getSource()==cutButton)
			{
				cutMenuAction();
			}
				
			if(e.getSource()==copyMenuItem||e.getSource()==copyButton)
			{
				try
				{
					copyMenuAction();
				}
				catch(Exception exception){}
			}
			
			if(e.getSource()==pasteButton||e.getSource()==pasteMenuItem)
			{
				
				//DefaultTreeModel model=(DefaultTreeModel) tree.getModel();
				if(!isJTreePanel())
				{
					JOptionPane.showMessageDialog(frame, "Please switch to JTree");
				}
				else{
					try{
						path=tree.getSelectionPath();
						if(path!=null)
						{
							undoModel=new DefaultTreeModel(getTree(rootElement));
							DefaultMutableTreeNode parent=(DefaultMutableTreeNode) path.getLastPathComponent();
							//pasteNode.setUserObject();
							parent.add(copiedNode);
							tree.expandPath(path);
							tree.updateUI();
						}
						else
						{}
					}
					catch(NullPointerException nullPointer)
					{

					}
					catch(Exception exception){}
				}
			}
			if(e.getSource()==aboutMenuItem)
			{
				JOptionPane.showMessageDialog(frame, "XML To JTree Converter and Processor \n\nCreated by OLUWALEKE AINA-31250699 \nDepartment of Computer Science, New Jersey Institute of Tech.");
			}
		}//end ActionPerformed Event Method
		public void addAttributeMethodAction() {
			//get the selected component
			try{
			path=tree.getSelectionPath();
			
			if(!isJTreePanel())//if the panel being displayed is not the JTreePanel the show a message dialog prompting to switch to JTreePanel
			{
				JOptionPane.showMessageDialog(frame, "Switch to JTree to add a New Attribute");
			}
			
			if(path==null && isJTreePanel()) //if node/element is selected while the Jtree Panel is visible
			{
				JOptionPane.showMessageDialog(null, "You must select an Node Element before You can Add an Attribute");
			}
			//if a node is selected while in the JTree Panel (path!=null)
			if(path!=null)
			{
					undoModel=new DefaultTreeModel(getTree(rootElement));
					DefaultMutableTreeNode parent=(DefaultMutableTreeNode) path.getLastPathComponent();
					if(parent.getUserObject() instanceof Element)
					{
						JTextField name = new JTextField();
						JTextField value = new JTextField();
						Object[] fields = {"Name", name, "Value", value};
						int choice = JOptionPane.showConfirmDialog(null, fields, "Please Enter Attribute Name and Value", JOptionPane.OK_CANCEL_OPTION);
						
						if (choice == JOptionPane.OK_OPTION)
						{
							String nameString = name.getText();
							String valueString = value.getText();
							
							if(nameString!=null&&valueString!=null)
							{
								if(nameString.contains(" ")||valueString.contains(" ")||nameString.contains(",")||valueString.contains(","))
								{
									JOptionPane.showMessageDialog(null, "You cannot enter invalid Characters: ,.!");
									addAttributeMethodAction();
								}
								
								else
								{
									Attribute attribute=new Attribute(nameString, valueString);
									DefaultMutableTreeNode child=new DefaultMutableTreeNode(attribute);
									parent.add(child);
									tree.expandPath(path);
									tree.updateUI();
									revalidate();
									tree.repaint();
								}
							}//end if
						}
					}
					else
						JOptionPane.showMessageDialog(frame, "You can only add An attribute to an Element.Select an Element");		
			 }
			}
			catch(Exception exception){}
		}
		
		private DefaultMutableTreeNode deepClone(DefaultMutableTreeNode selectedNode) {
			DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) selectedNode
					.clone();

			for (Enumeration selectedNodeEnumeration = selectedNode.children(); selectedNodeEnumeration.hasMoreElements();) {
				newNode.add(deepClone((DefaultMutableTreeNode) selectedNodeEnumeration.nextElement()));
			}
			return newNode;
		}	
		
		public void newMenuAction() throws HeadlessException {
			String input=JOptionPane.showInputDialog(null, "Please Enter a Root Node");
			if(input!=null)
			{
				if(input.contains(" "))
				{
					JOptionPane.showMessageDialog(null, "Element cannot contain spaces");
				}
				else
				{
				Element element=new Element(input);
				//element.setName(input);
				DefaultMutableTreeNode rootNode=new DefaultMutableTreeNode(element);
				//buildTree(child);
				DefaultTreeModel treeModel=new DefaultTreeModel(rootNode);
				tree=new JTree(treeModel);
				rootElement=element;
			
				if(isJTreeInPanel==false)
				{
					jTreePanel.add(new JScrollPane(tree));
					tree.setBackground(Color.LIGHT_GRAY);
					enableButtons(true);
				}
				else 
				{
					jTreePanel.removeAll();
					remove(tree);
					tree.setModel(treeModel);
					jTreePanel.add(new JScrollPane(tree));
					//enableButtons(true);
					tree.setBackground(Color.LIGHT_GRAY);
				}
				//	tree.setModel(treeModel);
				revalidate();
				expandTreeNodes();	
				tree.updateUI();
				frame.setTitle("GUI XML Editor");
				fileNameExists=null;
				isJTreeInPanel=true; //the open button has been pressed
				}
			}
		}

		public void copyMenuAction() throws HeadlessException {
			path=tree.getSelectionPath();
			if(!isJTreePanel())
			{
				JOptionPane.showMessageDialog(frame, "Please switch to JTree");
			}
			if(path!=null){
				undoModel=new DefaultTreeModel(getTree(rootElement));
				DefaultTreeModel model=(DefaultTreeModel) tree.getModel();
				DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			    copiedNode=deepClone(selectedNode);
			    //JOptionPane.showMessageDialog(frame, copiedNode.toString() +"was copied");
			}
			else{}
		}
		

		public void cutMenuAction() {
			try{
				path=tree.getSelectionPath();
				if(isJTreePanel())
				{
					if(path!=null)
					{
						model=(DefaultTreeModel) tree.getModel();
						//store tree data inside the undoModel
						undoModel=new DefaultTreeModel(getTree(rootElement));
						DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
						if(selectedNode.isRoot())
						{
							//bring up a JOptionPane box
							JOptionPane.showMessageDialog(frame, "You cannot delete the Root node");
						}
						else
						{
							copiedNode=deepClone(selectedNode);
							model.removeNodeFromParent(selectedNode);
							//redoModel=new DefaultTreeModel(getTree(rootElement));
							redoModel=(DefaultTreeModel) tree.getModel();
						}
					}
					else if(path==null){
						JOptionPane.showMessageDialog(frame, "You must select a Node");
					}
					
				}
			}
			catch(NullPointerException nullPointer){
			}
			catch(Exception exception){	
			}
		}

		public void buildTree(DefaultMutableTreeNode parent) {
			DefaultTreeModel treeModel=new DefaultTreeModel(parent);
			tree=new JTree(treeModel);
			jTreePanel.add(new JScrollPane(tree));
			//treeModel.reload();
		}

		public void saveAsMethodAction() throws HeadlessException {
			try{
				FileNameExtensionFilter filter= new FileNameExtensionFilter("XML Files", "xml");
				fileChooser.setFileFilter(filter);		
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				@SuppressWarnings("unused")

				int returnVal=fileChooser.showSaveDialog(null);
				TreeModel model=tree.getModel();

				if(returnVal==fileChooser.APPROVE_OPTION)
				{
					fileName=fileChooser.getSelectedFile().getAbsolutePath();
					file=new File(fileName);
					if(!file.exists())
					{
						file.createNewFile();
					}
					//FileWriter output=;
					BufferedWriter bufferWriter=new BufferedWriter(new FileWriter(file));
					XMLOutputter xmloutput=new XMLOutputter();
					xmloutput.setFormat(Format.getPrettyFormat());
					xmloutput.output(rootElement,bufferWriter );
					//output.close();
					bufferWriter.close();
					JOptionPane.showMessageDialog(frame, fileChooser.getSelectedFile().getName()+" has been Saved");
				}
				//XMLOutputter xmlOutput=new XMLOutputter(Format.getPrettyFormat());
			}
			catch(IOException exception)
			{}
			
			catch(Exception exception)
			{}
		}
	    public void switchButtonAction() {
			if(switchButton.getText()=="JTree")
			{
				//switch to Text Area
				tabbedPanel.setSelectedIndex(1);
				switchButton.setText("Text Area");		 
			}
			else if(switchButton.getText()=="Text Area")
			{
				tabbedPanel.setSelectedIndex(0); //switch to TextArea 
				switchButton.setText("JTree");
			}
		}
		public boolean isJTreePanel()
		{
			if(tabbedPanel.getSelectedIndex()==0){ //this is Text Area
				return true;
			}
			else 
				return false;
		}

		public void elementMethodAction() throws HeadlessException {
			try{
			
				path=tree.getSelectionPath(); //whichever element is highlited at that point, assign it to the path variable
				

				if(!isJTreePanel()) //if the Text Area is currently active 
				{
					JOptionPane.showMessageDialog(frame, "Switch to JTree to add a New Element");
				}

				if(path==null && isJTreePanel()) //if node/element is selected while the Jtree Panel 
				{
					JOptionPane.showMessageDialog(null, "You must select a Node before You can Add an Element");
				}
				//if a node is selected while in the JTree Panel (path!=null)
				else if(path!=null)
				{
					//bring up a JOptionPane for the user to enter a child variable
					undoModel=new DefaultTreeModel(getTree(rootElement));
					String input=JOptionPane.showInputDialog(null, "Please Enter in an Element to Add");

					if(input!=null)
					{
						Element element=new Element("Text");
						element.setName(input);
						DefaultMutableTreeNode child=new DefaultMutableTreeNode(element);
						DefaultMutableTreeNode parent=(DefaultMutableTreeNode) path.getLastPathComponent();
						parent.add(child);
						revalidate();
						tree.updateUI();
						tree.expandPath(path);
						
						
					}//end if
					//redoModel=new DefaultTreeModel(getTree(rootElement));

				}//end else if
			}
			catch(IllegalArgumentException exception)
			{
				//catch the IllegalArgument Exception
			}
			catch(Exception exception)
			{
			//catch every other exception
			}
		}

		public void enableButtons(boolean isbuttonEnabled) {
			elementButton.setEnabled(isbuttonEnabled);
			elementMenuItem.setEnabled(isbuttonEnabled);
			attributeButton.setEnabled(isbuttonEnabled);
			attributeMenuItem.setEnabled(isbuttonEnabled);
			undoMenuItem.setEnabled(isbuttonEnabled);
			undoButton.setEnabled(isbuttonEnabled);
			redoMenuItem.setEnabled(isbuttonEnabled);
			redoButton.setEnabled(isbuttonEnabled);
			saveButton.setEnabled(isbuttonEnabled);
			saveMenuItem.setEnabled(isbuttonEnabled);
			saveAsMenuItem.setEnabled(isbuttonEnabled);
			textButton.setEnabled(isbuttonEnabled);
			textMenuItem.setEnabled(isbuttonEnabled);
			cutButton.setEnabled(isbuttonEnabled);
			cutMenuItem.setEnabled(isbuttonEnabled);
			copyButton.setEnabled(isbuttonEnabled);
			copyMenuItem.setEnabled(isbuttonEnabled);
			pasteButton.setEnabled(isbuttonEnabled);
			pasteMenuItem.setEnabled(isbuttonEnabled);
		}
		
		
		public DefaultMutableTreeNode getTree(Element root) {
			DefaultMutableTreeNode rootnode = new DefaultMutableTreeNode(root);
			getTreeChildren(root, rootnode);
			return rootnode;
		}
		private void getTreeChildren(Element element, DefaultMutableTreeNode rootNode) {
			List<Element> elementList = element.getChildren();
			for (Element e : elementList) {
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(e);
				rootNode.add(dmtn);
				List<Attribute> attributeList = e.getAttributes();
				for (Attribute a : attributeList) {
					dmtn.add(new DefaultMutableTreeNode(new Attribute(a.getName(), a.getValue())));
				}
				String t = e.getTextTrim();
				if (!t.equals("")) {
					dmtn.add(new DefaultMutableTreeNode(new Text(t)));
				}
				int numChildren = e.getChildren().size();
				if (numChildren>0) getTreeChildren(e, dmtn);
			}
		}
		/**
		private void getTreeChildren(Element e, DefaultMutableTreeNode root) {
			List<Element> c = e.getChildren();
			for (Element subRootElements : c) {
				DefaultMutableTreeNode subRootNodes = new DefaultMutableTreeNode(subRootElements);
				root.add(subRootNodes);
				List<Attribute> lat = subRootElements.getAttributes();
				for (Attribute a : lat) {
					String textValues=subRootElements.getText();
					DefaultMutableTreeNode attributeNode=new DefaultMutableTreeNode(new Attribute(a.getName(),a.getValue()));
					subRootNodes.add(attributeNode);
					//attributeNode.add(new DefaultMutableTreeNode(textValues));
					//getTreeChildren(ch, attributeNode);
				}
				if(!subRootElements.hasAttributes())
				{
					String textValues=subRootElements.getText();
					subRootNodes.add(new DefaultMutableTreeNode(textValues));
				}
				int numChildren = subRootElements.getChildren().size();
					//get number of children of element directly under the parent root and if its not empty, traverse through it and add them also
					if (numChildren>0) getTreeChildren(subRootElements, subRootNodes);
			}
		} **/
	private void expandTreeNodes() {
		for (int i = 0; i < tree.getRowCount(); i++) {
		     tree.expandRow(i);
		}
	}
	
	//create the quitAction method
	private static void performQuitAction()
	{
		try
		{
			int choice=JOptionPane.showConfirmDialog(null, "Are you sure?", "Quit?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(choice==JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}
		}
		catch(Exception exception){}
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() 
			{
				frame= new LekeXMLEditor();		
				//frame.setTitle("GUI XML Editor");
				frame.setVisible(true);
				frame.addWindowListener(new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
					{
						performQuitAction();
					}
				});
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.setSize(850, 600);
				frame.setLocationRelativeTo(null);
			}
		});//end of SwingUtilities call
	}//end of main method
}
