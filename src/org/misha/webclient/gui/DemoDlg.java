package org.misha.webclient.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class DemoDlg extends JFrame {

	public DemoDlg() throws HeadlessException {
		super("Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        Component label = new JLabel("Метка");
        Component field = new JTextField(15);

        contentPane.add(label);
        contentPane.add(field);
        layout.putConstraint(SpringLayout.WEST , label, 10, 
                             SpringLayout.WEST , contentPane);
        layout.putConstraint(SpringLayout.NORTH, label, 25, 
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, field, 25, 
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST , field, 20, 
                             SpringLayout.EAST , label      );

        setSize(300, 110);
	}

	

}
