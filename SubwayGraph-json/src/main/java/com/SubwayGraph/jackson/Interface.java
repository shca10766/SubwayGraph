
package com.SubwayGraph.jackson;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/* MenuDemo.java requires images/middle.gif. */

/*
 * This class is just like MenuLookDemo, except the menu items
 * actually do something, thanks to event listeners.
 */
public class Interface {
    JTextArea output;
    JScrollPane scrollPane;
    String newline = "\n";
    
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
        frame.setSize(1000, 500);
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

        //Build the first menu.
        ImageIcon icon2 = createImageIcon("/info.png"); 
        JMenuItem item2 = new JMenuItem("Line information", icon2);
        item2.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        
        item2.setOpaque(true);
        item2.setBackground(new Color(128, 0, 64));
        item2.setForeground(Color.white);
        
        menuBar.add(item2);       

        return menuBar;
    }

    public Container createContentPane() {
        //Create the content-pane-to-be.
    	JPanel contentPane = new JPanel();
    	contentPane.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));
    	
    	contentPane.add(createForm());
    	contentPane.add(createButtons());
    	contentPane.add(displayRoute());

        return contentPane;        
    }
    
    public JComponent createForm() {
    	JPanel contentForm = new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(1000, 100);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(1000, 100);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(1000, 100);
            }
        };
        contentForm.setLayout(new BoxLayout(contentForm, BoxLayout.LINE_AXIS));
        contentForm.setOpaque(true);

        contentForm.add(createInuput("From"));
        contentForm.add(createInuput("To"));
        
        return contentForm;
    }
    
    public JComponent createInuput(String label_input) {
    	JPanel contentInput = new JPanel() {
            public Dimension getPreferredSize() {
                return new Dimension(500, 50);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(500, 50);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(500, 50);
            }
        };
        
        contentInput.setLayout(new BoxLayout(contentInput, BoxLayout.PAGE_AXIS));
        contentInput.setOpaque(true);
        
        JTextField input = new JTextField() {
            public Dimension getPreferredSize() {
                return new Dimension(400, 30);
            }
            public Dimension getMinimumSize() {
            	return new Dimension(400, 30);
            }
            public Dimension getMaximumSize() {
            	return new Dimension(400, 30);
            }
        };
        JLabel label = new JLabel(label_input);
        label.setLabelFor(input);
        
        contentInput.add(label);
        contentInput.add(input);
        
        return contentInput;
    }
    
    public JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Set address");
        panel.add(button);

        button = new JButton("Clear address");
        panel.add(button);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10-5, 10-5));
        return panel;
    }
    
    public JComponent displayRoute() {
    	JPanel contentPane = new JPanel(new BorderLayout());
    	contentPane.setPreferredSize(new Dimension(200, 325));
    	return contentPane;
    }
    

    /** Returns an ImageIcon, or null if the path was invalid. */
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