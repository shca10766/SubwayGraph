
package com.SubwayGraph.jackson;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

/* MenuDemo.java requires images/middle.gif. */

/*
 * This class is just like MenuLookDemo, except the menu items
 * actually do something, thanks to event listeners.
 */
public class Interface {
	private JPanel contentPane;
	private JPanel contentRoute;
	private JPanel contentForm;
	private JTextField fromInput, toInput;
	
    public void displayInterface() {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    };
    
    public void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Transport in Paris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Interface demo = new Interface();
        frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());

        //Display the window.
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    public JMenuBar createMenuBar() {
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(200, 50));
        
        ImageIcon icon1 = createImageIcon("/pointer.png");        
        JMenuItem item1 = new JMenuItem("Find the best route", icon1);
        item1.getAccessibleContext().setAccessibleDescription("Find the best route");
        
        item1.setOpaque(true);
        item1.setBackground(new Color(128, 0, 64));
        item1.setForeground(Color.white);        
        item1.setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.white));
        
        menuBar.add(item1);     

        return menuBar;
    }

    public Container createContentPane() {
        //Create the content-pane-to-be.
    	contentPane = new JPanel();
    	contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));
    	
    	contentPane.add(createOrder("Find the right route by entering your departure and arrival station"), BorderLayout.WEST);
    	contentPane.add(createForm());
    	contentPane.add(createButton());
    	contentPane.add(createSeparator(), BorderLayout.LINE_START);
    	
    	contentRoute = new JPanel();
    	contentRoute.setPreferredSize(new Dimension(900, 425));
    	contentPane.add(new JScrollPane(contentRoute), BorderLayout.CENTER);
    	
        return contentPane;        
    }
    
    public JComponent createOrder(String order) {
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	
    	JLabel title = new JLabel(order);
    	title.setFont(title.getFont().deriveFont(15f));
    	panel.add(title);
    	panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
    	
    	return panel;
    }
    
    
    public JComponent createForm() {
    	contentForm = new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(1000, 80);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(1000, 80);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(1000, 80);
            }
        };
        contentForm.setLayout(new BoxLayout(contentForm, BoxLayout.LINE_AXIS));
        contentForm.setOpaque(true);

        contentForm.add(createInuput("Departure"));
        contentForm.add(createInuput("Arrival"));
        
        EmptyBorder paddingContent = new EmptyBorder(0, 20, 0, 0);
        contentForm.setBorder(paddingContent);
        
        return contentForm;
    }
    
    
    public JComponent createInuput(String label_input) {
    	JPanel contentInput = new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(500, 60);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(500, 60);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(500, 60);
            }
        };
        contentInput.setLayout(new BoxLayout(contentInput, BoxLayout.LINE_AXIS));
        contentInput.setOpaque(true);
        
        JTextField input = new JTextField() {
            public Dimension getPreferredSize() {
                return new Dimension(380, 30);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(380, 30);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(380, 30);
            }
        };
        
        JLabel label = new JLabel(label_input);
        label.setLabelFor(input);
        EmptyBorder paddinglabel = new EmptyBorder(0, 0, 0, 10);
        label.setBorder(paddinglabel);
        
        contentInput.add(label);
        contentInput.add(input);
        
        if (label_input.equals("Departure")) {
        	this.fromInput = input;
        }
        else {
        	this.toInput = input;
        }
        
        return contentInput;
    }
    
    
    public JComponent createButton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Search");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (!fromInput.getText().equals("") && !toInput.getText().equals("")) {
        			Component[] componentPane = contentPane.getComponents();
            		contentPane.remove(componentPane[componentPane.length - 1]);  
            		
            		contentPane.add(new JScrollPane(displayRoute(fromInput.getText(), toInput.getText())), BorderLayout.CENTER);
            		
            		fromInput.setText("");
            		toInput.setText("");
            		
            		contentPane.revalidate();
            		contentPane.repaint();   
        		}     		
        	}
        });
        
        button.setPreferredSize(new Dimension(150, 30));
        button.setOpaque(true);
        button.setBackground(new Color(128, 0, 64));
        button.setForeground(Color.white);
        panel.add(button);
        
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 33));
        return panel;
    }
    
    
    public JComponent createSeparator() {
    	JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
    	
    	sep.setPreferredSize(new Dimension(1000, 5));
    	sep.setForeground(new Color(128, 0, 64));
        sep.setBackground(new Color(128, 0, 64));
    	
    	return sep;
    }
    
    
    public JComponent displayRoute(String departure, String arrival) {
    	ArrayList<Itinerary> itinerary = new ArrayList<Itinerary>();
    	itinerary.add(new Itinerary("metro", "4", "Gare du Nord", "Montparnasse-Bienvenue", "Mairie de Montrouge"));
    	itinerary.add(new Itinerary("metro", "12", "Montparnasse-Bienvenue", "Corentin Celton", "Mairie d'Issy"));
    	
    	JPanel contentRoute = new JPanel(new BorderLayout());
        contentRoute.setLayout(new BoxLayout(contentRoute,BoxLayout.PAGE_AXIS));
        int heightContent = 0;
        for (int i = 0; i < itinerary.size(); i++) {
        	contentRoute.add(createItinerary(itinerary.get(i)));
        	heightContent += 130; 
        }
        if (heightContent > 425) {
        	contentRoute.setPreferredSize(new Dimension(900, heightContent));
        }
        else {
        	contentRoute.setPreferredSize(new Dimension(900, 425));
        }
    	return contentRoute;
    }
    
    public JComponent createItinerary(Itinerary itinerary) {
    	JPanel contentItinerary = new JPanel();
    	contentItinerary.setLayout(new BoxLayout(contentItinerary,BoxLayout.LINE_AXIS));
    	contentItinerary.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
    	
    	contentItinerary.add(createTypeItinerary(itinerary.getType()));
    	contentItinerary.add(createLineItinerary(itinerary.getLine()));
    	contentItinerary.add(createArretsItinerary(itinerary));
    	
    	return contentItinerary;
    }
    
    
    public JComponent createTypeItinerary(String type) {
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            public Dimension getPreferredSize() {
                return new Dimension(50, 70);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(50, 70);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(50, 70);
            }
        };
        JLabel icon_type;
        if (type.equals("metro")) {
        	icon_type = new JLabel(createImageIcon("/metro.png"));
        }
        else if (type.equals("tram")) {
        	icon_type = new JLabel(createImageIcon("/tramway.png"));
        }
        else {
        	icon_type = new JLabel(createImageIcon("/rer.png"));
        }
    	panel.add(icon_type);
    	
    	return panel;
    }
    
    
    public JComponent createLineItinerary(String label_line) {
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)) {
            public Dimension getPreferredSize() {
                return new Dimension(50, 70);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(50, 70);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(50, 70);
            }
        };
    	
    	JLabel line = new JLabel(label_line);
    	line.setFont(line.getFont().deriveFont(15f));
    	panel.add(line);
    	
    	return panel;
    }
    
    
    public JComponent createArretsItinerary(Itinerary itinerary) {
    	JPanel contentArrets = new JPanel();
    	contentArrets.setLayout(new BoxLayout(contentArrets,BoxLayout.PAGE_AXIS));
    	
    	contentArrets.add(createStationItinerary(itinerary.getStation1()));
    	contentArrets.add(createDirectionItinerary(itinerary.getDirection()));
    	contentArrets.add(createStationItinerary(itinerary.getStation2()));
    	
    	return contentArrets;
    }
    
    
    public JComponent createStationItinerary(String label_station) {
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            public Dimension getPreferredSize() {
                return new Dimension(500, 20);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(500, 20);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(500, 20);
            }
        };
    	
    	JLabel station = new JLabel(label_station);
    	station.setFont(station.getFont().deriveFont(15f));
    	panel.add(station);
    	
    	return panel;
    }
    
    
    public JComponent createDirectionItinerary(String label_direction) {
    	JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0)) {
            public Dimension getPreferredSize() {
                return new Dimension(500, 30);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(500, 30);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(500, 30);
            }
        };
    	
    	JLabel direction = new JLabel("dir. " + label_direction);
    	panel.add(direction);
    	
    	return panel;
    }
    
    
    public ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Interface.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    } 
}