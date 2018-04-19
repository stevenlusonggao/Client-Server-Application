import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class client {
    boolean connected = false;
 	int port_n;
    String ip_add;
    String fromuser;
    Socket kkSocket;
    PrintWriter out;
    BufferedReader in;
    String command = "";
	public static void main(String[] args) throws IOException {
	        client client = new client();
	        gui g = new gui();
	        
	        //button to process initial connection
	        g.connectb.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e)  {
			    	if (client.connected == true) {
			    		return;
			    	}
			    	if (g.iptext.getText().matches("(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))") != true) {
			    		g.outtext.setText("Invalid IP address format");
			    		return;
			    	} else {
			    		client.ip_add = g.iptext.getText();
			    	}
			    	if (Integer.parseInt(g.porttext.getText()) <= 1024 || Integer.parseInt(g.porttext.getText()) >= 49151) {
			    		g.outtext.setText("Invalid port number format");
			    		return;
			    	} else {
			    		client.port_n = Integer.parseInt(g.porttext.getText());
			    	}
			    	
			    	//connect to server socket 
			        try {
			        	client.kkSocket = new Socket(client.ip_add, client.port_n);
			        	client.out = new PrintWriter(client.kkSocket.getOutputStream(), true);
			        	client.in = new BufferedReader(new InputStreamReader(client.kkSocket.getInputStream()));
			        } catch (UnknownHostException er) {
			            System.err.println("Connection Error: unknown host");
			            g.outtext.setText("Connection Error: unknown host");
			            System.exit(1);
			        } catch (IOException er) {
			            System.err.println("Connection Error: couldn't get I/O for the connection");
			            g.outtext.setText("Connection Error: couldn't get I/O for the connection");
			            System.exit(1);
			        } 
			    	client.connected = true;
			    	try {
						String succ = client.in.readLine();
						g.outtext.setText(succ + "\n");
						g.outtext.append("TYPE IN A COMMAND (GET, GETALL, GETBIBTEX, SUBMIT, UPDATE, REMOVE), THEN THEIR ASSOCIATED FIELDS" + "\n");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    }
			});
	        
	        //button to process requests
	        g.sendb.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent ew) {
			    	if (client.connected == false) 
			    		return;
			    	//input error checking
			    	if (g.comtext.getText().toLowerCase().matches("submit|get|remove|update|getall|getbibtex") == false) {
			    		 g.outtext.append("Input Error: Invalid Request" + "\n");
			    		 g.outtext.append("TYPE IN A COMMAND (GET, GETALL, GETBIBTEX, SUBMIT, UPDATE, REMOVE), THEN THEIR ASSOCIATED FIELDS" + "\n");
			    		 return;
			    	//putting together getall command
			    	} else if (g.comtext.getText().toLowerCase().equals("getall")) {
			    		client.command = "getall///";
			    	//putting together getbibtex command
			    	} else if (g.comtext.getText().toLowerCase().equals("getbibtex")) {
			    		client.command = "getbibtex///";
			    	//putting together submit command
			    	} else if (g.comtext.getText().toLowerCase().equals("submit")) {
			    		if(g.isbntext.getText().isEmpty() || g.authtext.getText().isEmpty() || g.titletext.getText().isEmpty() || g.yeartext.getText().isEmpty() || g.pubtext.getText().isEmpty()) {
			    			g.outtext.append("Input Error: Entry Fields cannot be empty" + "\n");
			    			return;
			    		}
			    		if(g.isbntext.getText().matches("\\d+") != true || g.isbntext.getText().length() != 13) {
			    			g.outtext.append("Input Error: ISBN must be a 13 digit number" + "\n");
			    			return;
			    		}
			    		if(g.yeartext.getText().matches("\\d+") != true) {
			    			g.outtext.append("Input Error: Year must be a number" + "\n");
			    			return;
			    		}
			    		client.command = "submit" + "///" + g.isbntext.getText() + "///" + g.titletext.getText() + "///" + g.authtext.getText() + "///" + g.pubtext.getText() + "///" + g.yeartext.getText();
			    	
			    	} else if ((g.comtext.getText().toLowerCase().equals("get"))) {
			    		if(g.isbntext.getText().isEmpty() && g.authtext.getText().isEmpty() && g.titletext.getText().isEmpty() && g.yeartext.getText().isEmpty() && g.pubtext.getText().isEmpty()) {
			    			client.command = "getall///";
			    		} else {
			    			client.command = "get///";
			    			if (g.isbntext.getText().isEmpty() != true) {
			    				if(g.isbntext.getText().matches("\\d+") != true || g.isbntext.getText().length() != 13) {
					    			g.outtext.append("Input Error: ISBN must be a 13 digit number" + "\n");
					    			return;
					    		} else {
					    			client.command = client.command + "isbn" + "///" + g.isbntext.getText() + "///";
					    		}
			    			}
			    			if (g.titletext.getText().isEmpty() != true) {
			    				client.command = client.command + "title" + "///" + g.titletext.getText() + "///";
			    			}
			    			if (g.authtext.getText().isEmpty() != true) {
			    				client.command = client.command + "author" + "///" + g.authtext.getText() + "///";
			    			}
			    			if (g.pubtext.getText().isEmpty() != true) {
			    				client.command = client.command + "publisher" + "///" + g.pubtext.getText() + "///";
			    			}
			    			if (g.yeartext.getText().isEmpty() != true) {
			    				if(g.yeartext.getText().matches("\\d+") != true) {
					    			g.outtext.append("Input Error: Year must be a number" + "\n");
					    			return;
					    		} else {
					    			client.command = client.command + "year" + "///" + g.yeartext.getText() + "///";
					    		}
			    			}
			    		}
			    		//putting update update command	
			    	} else if ((g.comtext.getText().toLowerCase().equals("update"))) {
			    		if(g.isbntext.getText().isEmpty()) {
			    			g.outtext.append("Input Error: ISBN cannot be empty" + "\n");
			    			return;
			    		}else {
			    			if(g.isbntext.getText().matches("\\d+") != true || g.isbntext.getText().length() != 13) {
				    			g.outtext.append("Input Error: ISBN must be a 13 digit number" + "\n");
				    			return;
			    			}
			    			
			    			int counter = 0;
			    			String upfield = "";
			    			String upfieldt = "";
			    			if (g.authtext.getText().isEmpty() != true) {
			    				counter = counter + 1;
			    				upfieldt = g.authtext.getText();
			    				upfield = "author";
			    			}
			    			if (g.titletext.getText().isEmpty() != true) {
			    				counter = counter + 1;
			    				upfieldt = g.titletext.getText();
			    				upfield = "title";
			    			}
			    			if (g.yeartext.getText().isEmpty() != true) {
			    				counter = counter + 1;
			    				upfieldt = g.yeartext.getText();
			    				upfield = "year";
			    			}
			    			if (g.pubtext.getText().isEmpty() != true) {
			    				counter = counter + 1;
			    				upfieldt = g.pubtext.getText();
			    				upfield = "publisher";
			    			}
			    			if (counter != 1) {
			    				g.outtext.append("Input Error: Invalid number of fields entered. You can update one field at a time" + "\n");
			    				return;
			    			}
			    			if (upfield.equals("year")) {
			    				if(g.yeartext.getText().matches("\\d+") != true) {
					    			g.outtext.append("Input Error: Year must be a number" + "\n");
					    			return;
			    				}
			    			}
			    			client.command = "update" + "///" + g.isbntext.getText() + "///" + upfield + "///" + upfieldt + "///";
			    		}
			    		//putting together remove command
			    	} else if ((g.comtext.getText().toLowerCase().equals("remove"))) {
			    		if (g.isbntext.getText().isEmpty() && g.authtext.getText().isEmpty() && g.titletext.getText().isEmpty() && g.yeartext.getText().isEmpty() && g.pubtext.getText().isEmpty()) {
			    			g.outtext.append("Input Error: You must input atleast one field for removal of entries" + "\n");
			    			return;
			    		} else {
			    			client.command = "remove///";
			    			if (g.isbntext.getText().isEmpty() != true) {
			    				if(g.isbntext.getText().matches("\\d+") != true || g.isbntext.getText().length() != 13) {
					    			g.outtext.append("Input Error: ISBN must be a 13 digit number" + "\n");
					    			return;
					    		} else {
					    			client.command = client.command + "isbn" + "///" + g.isbntext.getText() + "///";
					    		}
			    			}
			    			if (g.titletext.getText().isEmpty() != true) {
			    				client.command = client.command + "title" + "///" + g.titletext.getText() + "///";
			    			}
			    			if (g.authtext.getText().isEmpty() != true) {
			    				client.command = client.command + "author" + "///" + g.authtext.getText() + "///";
			    			}
			    			if (g.pubtext.getText().isEmpty() != true) {
			    				client.command = client.command + "publisher" + "///" + g.pubtext.getText() + "///";
			    			}
			    			if (g.yeartext.getText().isEmpty() != true) {
			    				if(g.yeartext.getText().matches("\\d+") != true) {
					    			g.outtext.append("Input Error: Year must be a number" + "\n");
					    			return;
					    		} else {
					    			client.command = client.command + "year" + "///" + g.yeartext.getText() + "///";
					    		}
			    			}
			    		}
			    	}
			    	g.comtext.setText("");
			    	g.isbntext.setText("");
			    	g.titletext.setText("");
			    	g.authtext.setText("");
			    	g.pubtext.setText("");
			    	g.yeartext.setText("");
			    	//send command to server
			    	client.out.println(client.command);
			    	
			    	//server response
			    	String fromserver;
					try {
						fromserver = client.in.readLine();
						if (fromserver.substring(0, Math.min(fromserver.length(), 4)).equals("$get")) {
			        		String[] inputs = fromserver.split("/");
			        		for(int i=1; i < inputs.length; i++) {
			        			
			        			g.outtext.append(inputs[i] + "\n");
			        		}
			        	} else if (fromserver.substring(0, Math.min(fromserver.length(), 7)).equals("$bibgen")){
			        		String[] inputs = fromserver.split("%");
			        		for(int i=1; i < inputs.length; i++) {
			        			
			        			g.outtext.append(inputs[i] + "\n");
			        		}
			        	} else {
			        		
			        		g.outtext.append(fromserver + "\n");
			        	}
						g.outtext.append("TYPE IN A COMMAND (GET, GETALL, GETBIBTEX, SUBMIT, UPDATE, REMOVE), THEN THEIR ASSOCIATED FIELDS" + "\n");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			    }
	        });
	        
	        //exit button
	        g.exitb.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent ex)  {
			    	if (client.connected == false) 
			    		return;
			    	
			    	try {
			    	client.command = "exit///";
        			client.out.println(client.command);
        			client.out.close();
        	        client.in.close();
        	        client.kkSocket.close();
        	        client.connected = false;
        	        System.exit(0);
			    	} catch (Exception e2) {
			    		e2.printStackTrace();
			    	}
			    }
	        });
	    } 
}


