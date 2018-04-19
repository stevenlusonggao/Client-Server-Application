import javax.swing.*;
import java.awt.*;
import java.io.*;

public class gui {
	public JFrame window;
	public JPanel panel;
	public JTextField iptext, porttext, comtext, isbntext, authtext, titletext, pubtext, yeartext;
	public JTextArea outtext;
	public JLabel portdis, ipdis,comdis, isbndis, authdis, titledis, pubdis, yeardis;
	public JButton connectb, exitb, sendb;

	public gui() {
		window = new JFrame("Bibliography Client");
		
		JPanel panelm = new JPanel();
		panelm.setLayout(new BoxLayout(panelm, BoxLayout.PAGE_AXIS));
		JPanel panel1 = new JPanel(new FlowLayout());
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		
		ipdis = new JLabel("IP Address: ");
		panel1.add(ipdis);
		iptext = new JTextField(16);
		panel1.add(iptext);
		portdis = new JLabel("Port Number: ");
		panel1.add(portdis);
		porttext = new JTextField(4);
		panel1.add(porttext);
		
		connectb = new JButton("Connect");
		exitb = new JButton("Disconnect");
		panel1.add(connectb);
		panel1.add(exitb);
		
		outtext = new JTextArea(25,75);
		outtext.setEnabled(false);
		outtext.setText("TYPE IN A COMMAND (GET, GETALL, GETBIBTEX, SUBMIT, UPDATE, REMOVE, THEN THEIR ASSOCIATED FIELDS)" + "\n");
		JScrollPane scroll = new JScrollPane (outtext, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		Font font = new Font("Verdana", Font.PLAIN, 12);
	    outtext.setFont(font);
	    scroll.getViewport().getView().setBackground(Color.BLACK);
		panel4.add(scroll, BorderLayout.NORTH);
		
		comdis = new JLabel("Command: ");
		panel2.add(comdis);
		comtext = new JTextField(10);
		panel2.add(comtext);
		
		isbndis = new JLabel("ISBN: ");
		panel2.add(isbndis);
		isbntext = new JTextField(10);
		panel2.add(isbntext);
		
		titledis = new JLabel("Title: ");
		panel2.add(titledis);
		titletext = new JTextField(10);
		panel2.add(titletext);
		
		authdis = new JLabel("Author: ");
		panel2.add(authdis);
		authtext = new JTextField(10);
		panel2.add(authtext);
		
		pubdis = new JLabel("Publisher: ");
		panel2.add(pubdis);
		pubtext = new JTextField(10);
		panel2.add(pubtext);
		
		yeardis = new JLabel("Year: ");
		panel2.add(yeardis);
		yeartext = new JTextField(10);
		panel2.add(yeartext);
		
		sendb = new JButton("SEND COMMAND");
		panel3.add(sendb);
		
		panelm.add(panel1);
		panelm.add(panel2);
		panelm.add(panel3);
		panelm.add(panel4);
        window.getContentPane().add(panelm);
		window.pack();
		window.setVisible(true);		
		
	}
	public static void main(String[] args) {
		
	}
}

