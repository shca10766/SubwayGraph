
package com.SubwayGraph.graph;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.SubwayGraph.jackson.Station;
import com.SubwayGraph.jackson.Subway;

public class Interface {
	private JPanel contentPane;
	private JPanel contentRoute;
	private JPanel contentForm;
	private JTextField fromInput, toInput;
	private MapBuilder map;
	private Subway subway;
	
	public Interface(MapBuilder m, Subway s) {
		this.map = m;
		this.subway = s;
	}
	
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
        Interface demo = new Interface(this.map, this.subway);
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
    	contentPane.add(contentRoute, BorderLayout.CENTER);
    	
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
        
        String[] stationString = getStationStrings();
        JSpinner input = new JSpinner(new SpinnerListModel(stationString)) {
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
        	this.fromInput = getTextField((JSpinner)input);
        }
        else {
        	this.toInput = getTextField((JSpinner)input);
        }
        
        return contentInput;
    }
    
    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                               + spinner.getEditor().getClass()
                               + " isn't a descendant of DefaultEditor");
            return null;
        }
    }
    
    public String[] getStationStrings() {
    	ArrayList<Station> stations = subway.getStations();
    	String[] stationsString = new String[stations.size()];
    	for (int i = 0 ; i < stations.size(); i++) {
    		stationsString[i] = stations.get(i).getNom();
    	}
        return stationsString;
    }
    
    public JComponent createButton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Search");
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (!fromInput.getText().equals("") && !toInput.getText().equals("") && !fromInput.getText().equals(toInput.getText())) {
        			Component[] componentRoute = contentRoute.getComponents();
        			for (int i = 0; i < componentRoute.length; i++) {
        				contentRoute.remove(componentRoute[i]);
        			}
            		
            		contentRoute.setLayout(new BoxLayout(contentRoute,BoxLayout.LINE_AXIS));
            		
            		BFSShortestPath b = new BFSShortestPath(map, idStationWithName(fromInput.getText()), idStationWithName(toInput.getText()), subway);
                	ArrayList<Itinerary> itineraryBFS = b.getItinerary();
                	contentRoute.add(new JScrollPane(displayRoute("BFS", itineraryBFS)), BorderLayout.CENTER);
                	
                	DijkstraShortestPath d = new DijkstraShortestPath(map, idStationWithName(fromInput.getText()), idStationWithName(toInput.getText()), subway);
                	ArrayList<Itinerary> itinerary_Dijkstra = d.getItinerary();
                	contentRoute.add(new JScrollPane(displayRoute("Dijkstra", itinerary_Dijkstra)), BorderLayout.CENTER);
                	
                	contentPane.add(contentRoute, BorderLayout.CENTER);
            		
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
    
    
    public JComponent displayRoute(String algo, ArrayList<Itinerary> itineraries) {    	
    	JPanel contentItinerary = new JPanel();
    	contentItinerary.setLayout(new BoxLayout(contentItinerary,BoxLayout.PAGE_AXIS));
        int heightContent = 0;
        
        JLabel label = new JLabel(algo + " shortest path");
        label.setFont(label.getFont().deriveFont(18f));
        contentItinerary.add(label);
        
        for (int i = 0; i < itineraries.size(); i++) {
        	contentItinerary.add(createItinerary(itineraries.get(i)));
        	heightContent += 130; 
        }
        if (heightContent > 425) {
        	contentItinerary.setPreferredSize(new Dimension(400, heightContent));
        }
        else {
        	contentItinerary.setPreferredSize(new Dimension(400, 425));
        }
        
    	return contentItinerary;
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
    
    public String idStationWithName(String name) {
    	ArrayList<Station> stations = subway.getStations();
    	for (int i = 0; i < stations.size(); i++) {
    		if (stations.get(i).getNom().equals(name)) {
    			return stations.get(i).getNum();
    		}
    	}
    	return null;
    }
}