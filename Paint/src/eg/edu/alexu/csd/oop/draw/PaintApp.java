package eg.edu.alexu.csd.oop.draw;

import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class PaintApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Shape shape;
	private Shape selectedShape;
	private Shape clonedShape;
	private Point click = new Point();
	private Color strokeColor = Color.lightGray;
	private Color fillColor = Color.BLACK;
	private JTextField arcWidth;
	private JTextField arcHight;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PaintApp frame = new PaintApp(new Engine(), new Controller());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PaintApp(DrawingEngine engine, Controller controller) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 737, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox<String> otherShapes = new JComboBox<>();
		otherShapes.setBounds(500, 409, 105, 22);
		java.util.List<Class<? extends Shape>> supportedShapes = engine.getSupportedShapes();
		for (int i = 0; i < supportedShapes.size(); i++) {
			if (!supportedShapes.get(i).getName().substring(supportedShapes.get(i).getName().lastIndexOf(".") + 1)
					.equals("Rectangle") && !supportedShapes.get(i).getName().contains("Square")
					&& !supportedShapes.get(i).getName().contains("Triangle")
					&& !supportedShapes.get(i).getName().contains("Line")
					&& !supportedShapes.get(i).getName().contains("Circle")
					&& !supportedShapes.get(i).getName().contains("Ellipse")) {
				otherShapes.addItem(supportedShapes.get(i).getName()
						.substring(supportedShapes.get(i).getName().lastIndexOf(".") + 1));
			}
		}
		otherShapes.setSelectedItem(null);
		contentPane.add(otherShapes);

		arcWidth = new JTextField();
		arcWidth.setBounds(267, 409, 139, 22);
		contentPane.add(arcWidth);
		arcWidth.setColumns(10);

		arcHight = new JTextField();
		arcHight.setBounds(267, 432, 139, 20);
		contentPane.add(arcHight);
		arcHight.setColumns(10);

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(611, 11, 100, 258);
		contentPane.add(verticalBox);

		JRadioButton rdbtnLine = new JRadioButton("Line");
		rdbtnLine.setSelected(true);
		verticalBox.add(rdbtnLine);
		rdbtnLine.setActionCommand("Line");

		JRadioButton rdbtnRectangle = new JRadioButton("Rectangle");
		verticalBox.add(rdbtnRectangle);
		rdbtnRectangle.setActionCommand("Rectangle");

		JRadioButton rdbtnSquare = new JRadioButton("Square");
		verticalBox.add(rdbtnSquare);
		rdbtnSquare.setActionCommand("Square");

		JRadioButton rdbtnTrinagle = new JRadioButton("Trinagle");
		verticalBox.add(rdbtnTrinagle);
		rdbtnTrinagle.setActionCommand("Triangle");

		JRadioButton rdbtnCircle = new JRadioButton("Circle");
		verticalBox.add(rdbtnCircle);
		rdbtnCircle.setActionCommand("Circle");

		JRadioButton rdbtnEllippse = new JRadioButton("Ellippse");
		verticalBox.add(rdbtnEllippse);
		rdbtnEllippse.setActionCommand("Ellipse");

		JRadioButton rdbtnRelocate = new JRadioButton("Relocate");
		rdbtnRelocate.setActionCommand("Relocate");
		verticalBox.add(rdbtnRelocate);

		JRadioButton rdbtnResize = new JRadioButton("Resize");
		rdbtnResize.setActionCommand("Resize");
		verticalBox.add(rdbtnResize);

		JRadioButton rdbtnUpdatecolor = new JRadioButton("ReColor");
		rdbtnUpdatecolor.setActionCommand("ReColor");
		verticalBox.add(rdbtnUpdatecolor);

		JRadioButton rdbtnRemove = new JRadioButton("Remove");
		rdbtnRemove.setActionCommand("Remove");
		verticalBox.add(rdbtnRemove);

		JRadioButton rdbtnOther = new JRadioButton("Other");
		rdbtnOther.setActionCommand("Other");
		verticalBox.add(rdbtnOther);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnLine);
		group.add(rdbtnRectangle);
		group.add(rdbtnSquare);
		group.add(rdbtnTrinagle);
		group.add(rdbtnCircle);
		group.add(rdbtnEllippse);
		group.add(rdbtnRelocate);
		group.add(rdbtnResize);
		group.add(rdbtnUpdatecolor);
		group.add(rdbtnRemove);
		group.add(rdbtnOther);

		Canvas canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				HashMap<String, Double> pro = new HashMap<String, Double>();
				Graphics g = canvas.getGraphics();
				if (group.getSelection().getActionCommand().equals("Relocate")) {
					try {
						clonedShape = controller.getShapeUpdated(e, clonedShape, click);
						g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(g);
						clonedShape.draw(g);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(canvas, "please select shape to update");
					}
				} else if (group.getSelection().getActionCommand().equals("Resize")) {
					try {
						clonedShape = controller.getShapeResized(e, clonedShape,Double.valueOf(arcWidth.getText()),Double.valueOf(arcHight.getText()));
						g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(g);
						clonedShape.draw(g);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(canvas, "please select shape to redraw");
					}
				} else if (!group.getSelection().getActionCommand().equals("Remove")
						&& !group.getSelection().getActionCommand().equals("Resize")
						&& !group.getSelection().getActionCommand().equals("ReColor")) {
					g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					Point position = (Point) shape.getPosition();
					switch (group.getSelection().getActionCommand()) {
					case "Line":
						pro.put("x", (double) e.getX());
						pro.put("y", (double) e.getY());
						pro.put("x1", (double) e.getX());
						pro.put("y1", (double) e.getY());
						break;
					case "Rectangle":
						pro.put("topLeftMinX", (double) Math.min(position.x, e.getX()));
						pro.put("topLeftMinY", (double) Math.min(position.y, e.getY()));
						pro.put("width", (double) Math.abs(position.x - e.getX()));
						pro.put("hight", (double) Math.abs(position.y - e.getY()));
						break;
					case "Square":
						pro = controller.getLength(shape, e);
						break;
					case "Ellipse":
						pro.put("topLeftMinX", (double) Math.min(position.x, e.getX()));
						pro.put("topLeftMinY", (double) Math.min(position.y, e.getY()));
						pro.put("width", (double) Math.abs(position.x - e.getX()));
						pro.put("hight", (double) Math.abs(position.y - e.getY()));
						break;
					case "Circle":
						pro = controller.getLength(shape, e);
						break;
					case "Triangle":
						pro.put("x", (double) e.getX());
						pro.put("y", (double) e.getY());
						pro.put("x1", (double) position.x - (e.getX() - position.x));
						pro.put("y1", (double) e.getY());
						break;
					case "Other":
						if(otherShapes.getSelectedItem().equals("RoundRectangle")) {
							pro.put("Length", (double) Math.abs(position.y - e.getY()));
							pro.put("Width", (double) Math.abs(position.x - e.getX()));
							pro.put("ArcWidth",Double.parseDouble(arcWidth.getText()));
							pro.put("ArcLength",Double.parseDouble(arcHight.getText()));
						}
						break;
					default:
						break;
					}

					shape.setProperties(new HashMap<String, Double>(pro));
					shape.setColor(strokeColor);
					shape.setFillColor(fillColor);
					engine.refresh(g);
					shape.draw(g);
				}
			}
		});
		canvas.setBounds(10, 10, 595, 393);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (group.getSelection().getActionCommand().equals("Relocate")) {
					click.x = e.getX();
					click.y = e.getY();
					try {
						selectedShape = controller.getSelectedShape(click, engine, canvas.getGraphics());
						clonedShape = (Shape) selectedShape.clone();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(canvas, "please select shape to update");
					}
				} else if (group.getSelection().getActionCommand().equals("Resize")) {
					click.x = e.getX();
					click.y = e.getY();
					try {
						selectedShape = controller.getSelectedShape(click, engine, canvas.getGraphics());
						clonedShape = (Shape) selectedShape.clone();
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(canvas, "please select shape to redraw");
					}
				} else if (!group.getSelection().getActionCommand().equals("Remove")
						&& !group.getSelection().getActionCommand().equals("Resize")
						&& !group.getSelection().getActionCommand().equals("ReColor")) {
					try {
						shape = controller.createShape(group.getSelection().getActionCommand(),engine,otherShapes);
						Point position = new Point(e.getX(), e.getY());
						HashMap<String, Double> pro = new HashMap<String, Double>();
						shape.setPosition(position);
						shape.setProperties(pro);
						shape.setColor(strokeColor);
						shape.setFillColor(fillColor);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(canvas, "Invalid Shape");
					}
					
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (group.getSelection().getActionCommand().equals("Relocate")
						|| group.getSelection().getActionCommand().equals("Resize")) {
					try {
						engine.updateShape(selectedShape, clonedShape);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
						selectedShape = null;
						clonedShape = null;
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(canvas, "please select shape to update");
					}
				} else if (!group.getSelection().getActionCommand().equals("Remove")
						&& !group.getSelection().getActionCommand().equals("ReColor")
						&& !group.getSelection().getActionCommand().equals("Resize")
						&& !group.getSelection().getActionCommand().equals("Relocate")) {
					engine.addShape(shape);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (group.getSelection().getActionCommand().equals("Remove")) {
					click.x = e.getX();
					click.y = e.getY();
					try {
						selectedShape = controller.getSelectedShape(click, engine, canvas.getGraphics());
						engine.removeShape(selectedShape);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
						selectedShape = null;
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(canvas, "please select shape to Recolor");
					}
				} else if (group.getSelection().getActionCommand().equals("ReColor")) {
					click.x = e.getX();
					click.y = e.getY();
					try {
						selectedShape = controller.getSelectedShape(click, engine, canvas.getGraphics());
						clonedShape = (Shape) selectedShape.clone();
						clonedShape.setColor(strokeColor);
						clonedShape.setFillColor(fillColor);
						engine.updateShape(selectedShape, clonedShape);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
						selectedShape = null;
						clonedShape = null;
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(canvas, "please select shape to Recolor");
					}
				}
			}
		});

		contentPane.add(canvas);

		JButton btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Graphics g = canvas.getGraphics();
				engine.undo();
				g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				engine.refresh(canvas.getGraphics());
			}
		});
		btnUndo.setBounds(611, 280, 105, 23);
		contentPane.add(btnUndo);

		JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graphics g = canvas.getGraphics();
				engine.redo();
				g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				engine.refresh(canvas.getGraphics());
			}
		});
		btnRedo.setBounds(611, 310, 105, 23);
		contentPane.add(btnRedo);

		JButton btnSelectColor = new JButton("FillColor");
		btnSelectColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color currentFill = fillColor;
				Color selectedFill = JColorChooser.showDialog(null, "JColorChooser Sample", currentFill);
				if (selectedFill != null) {
					fillColor = selectedFill;
				}
			}
		});
		btnSelectColor.setBounds(611, 340, 105, 23);
		contentPane.add(btnSelectColor);

		JButton btnNewButton = new JButton("StrokeColor");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color currentStroke = strokeColor;
				Color selectedStroke = JColorChooser.showDialog(null, "JColorChooser Sample", currentStroke);
				if (selectedStroke != null) {
					strokeColor = selectedStroke;
				}
			}
		});
		btnNewButton.setBounds(611, 370, 105, 23);
		contentPane.add(btnNewButton);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Graphics g = canvas.getGraphics();
				g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				engine.refresh(g);
			}
		});
		btnRefresh.setBounds(611, 400, 105, 23);
		contentPane.add(btnRefresh);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.setDialogTitle("Choose a save location");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.XmL", "XmL", "XML", "xml");
				FileNameExtensionFilter filter2 = new FileNameExtensionFilter("*.JsOn", "JsOn", "JSON", "json");
				chooser.setFileFilter(filter);
				chooser.setFileFilter(filter2);
				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File folder = chooser.getSelectedFile();
					if (!chooser.getFileFilter().getDescription().equals("All Files")) {
						String path = folder.getAbsolutePath() + chooser.getFileFilter().getDescription().substring(1);
						try {
							engine.save(path);
							JOptionPane.showMessageDialog(canvas, "file save correctly");
						} catch (Exception e) {
							JOptionPane.showMessageDialog(canvas, "Error no shapes found");
						}
					} else {
						JOptionPane.showMessageDialog(canvas, "choose Extension");
					}

				}
			}
		});
		btnSave.setBounds(10, 409, 70, 23);
		contentPane.add(btnSave);

		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.setDialogTitle("Choose the file location");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.XmL", "XmL", "XML", "xml");
				FileNameExtensionFilter filter2 = new FileNameExtensionFilter("*.JsOn", "JsOn", "JSON", "json");
				chooser.setFileFilter(filter);
				chooser.setFileFilter(filter2);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String path = file.getAbsolutePath();
					try {
						engine.load(path);
						Graphics g = canvas.getGraphics();
						g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(g);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(canvas, "Error Empty File");
					}

				}
			}
		});
		btnLoad.setBounds(90, 409, 70, 23);
		contentPane.add(btnLoad);

		JLabel lblArcwidth = new JLabel("ArcWidth:");
		lblArcwidth.setBounds(196, 409, 61, 23);
		contentPane.add(lblArcwidth);

		JLabel lblArchight = new JLabel("ArcHight:");
		lblArchight.setBounds(196, 432, 56, 17);
		contentPane.add(lblArchight);

		JLabel lblOtherShapes = new JLabel("Other Shapes:");
		lblOtherShapes.setBounds(416, 409, 74, 23);
		contentPane.add(lblOtherShapes);
	}
}
