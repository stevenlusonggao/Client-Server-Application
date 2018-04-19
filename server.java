import java.net.*;
import java.io.*;
import java.util.*;

public class server {
	public static ArrayList<bibliography> bib = new ArrayList<bibliography>();
    public static void main(String[] args) throws IOException {
    	
    	//check if port number argument is correct
    	int portn;
    	try{ 
    		Integer.parseInt(args[0]);
    		if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151 ) {
        		System.err.println("Submit a valid port number (1024 to 49151)");
        		System.exit(0);
        	}  		  
    		}catch(NumberFormatException e){
    		  System.out.println("Connection Error: Invalid port");
    		  System.exit(0);
    		}
    	
    	portn = Integer.parseInt(args[0]);
		System.out.println("server setup complete. ");
		
		//setup connection with clients
    	ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
        	serverSocket = new ServerSocket(portn);
            while (true) {
            	clientSocket = serverSocket.accept();
            	RequestHandler request = new RequestHandler(clientSocket);
            	Thread thread = new Thread (request);
            	thread.start();
            }
        } catch (IOException e) {
            System.err.println("Connection Error: server could not listen on port");
            System.exit(1);
        } 
    }
}

//class to handle client interactions in different threads
class RequestHandler implements Runnable {
	private final Socket client;
	
	public RequestHandler(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        String inputline; 
	        String outputline;
	        boolean dupe;
	        out.println("Server Connection Accepted");
	        while ((inputline = in.readLine()) != null) {
	        	String[] inputs = inputline.split("///"); //split the message from clients to process them
	        	//get request processing
	        	if(inputs[0].equals("get")) {
	        		ArrayList<bibliography> results = new ArrayList<bibliography>();
	        		results.addAll(server.bib);
	        		int a = 1;
	        		while(a < inputs.length) {
	        			int b = 0;
	        			while (b < results.size()) {
	        				if(inputs[a].equals("isbn")) {
	        					if(results.get(b).isbn != Long.parseLong(inputs[a+1])) {
	        						results.remove(b);
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("title")) {
	        					if(results.get(b).title.equals(inputs[a+1]) != true) {
	        						results.remove(b);
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("author")) {
	        					if(results.get(b).author.equals(inputs[a+1]) != true) {
	        						results.remove(b);
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("publisher")) {
	        					if(results.get(b).publisher.equals(inputs[a+1]) != true) {
	        						results.remove(b);
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("year")) {
	        					if(results.get(b).year != Integer.parseInt(inputs[a+1])) {
	        						results.remove(b);
	        					}else {
	        						b++;
	        					}
	        				}
	        			}
	        			a = a + 2;
	        		}
	        		if(results.size() == 0) {
	        			out.println("Get returned nothing");
	        		} else {
	        		outputline = "$get/";
	        		for(int i=0; i < results.size(); i++) {
	        			outputline = outputline + "ISBN: " + results.get(i).isbn + "/" 
	        									+ "TITLE: " + results.get(i).title + "/" 
	        									+ "AUTHOR: " + results.get(i).author + "/"
	        									+ "PUBLISHER: " + results.get(i).publisher + "/"
	        									+ "YEAR: " + results.get(i).year + "/" 
	        									+ "----------------------------------" + "/";
	        		}
	        		out.println(outputline);
	        		}
	 	        }
	        	//getall request processing
	        	else if(inputs[0].equals("getall")) {
	        		if(server.bib.size() == 0) {
	        			out.println("Getall returned nothing");
	        		} else {
	        		outputline = "$get/";
	        		for(int i=0; i < server.bib.size(); i++) {
	        			outputline = outputline + "ISBN: " + server.bib.get(i).isbn + "/" 
	        									+ "TITLE: " + server.bib.get(i).title + "/" 
	        									+ "AUTHOR: " + server.bib.get(i).author + "/"
	        									+ "PUBLISHER: " + server.bib.get(i).publisher + "/"
	        									+ "YEAR: " + server.bib.get(i).year + "/" 
	        									+ "----------------------------------" + "/";
	        		}
	        		out.println(outputline);
	        		}
	        	}
	        	//getbibtex request processing
	        	else if(inputs[0].equals("getbibtex")) {
	        		if(server.bib.size() == 0) {
	        			out.println("Getbibtex returned nothing");
	        		} else {
	        			outputline = "$bibgen%";
		        		for(int i=0; i < server.bib.size(); i++) {
		        			outputline = outputline + "@Book{" + server.bib.get(i).isbn + "," + "%"
		        									+ "title = " + "{" + server.bib.get(i).title + "}" + "," + "%"
		        									+ "author = " + "{" + server.bib.get(i).author + "}" + "," + "%"
		        									+ "publisher = " + "{" + server.bib.get(i).publisher + "}" + "," + "%"
		        									+ "year = " + "{" + server.bib.get(i).year + "}" + "," + "%"
		        									+ "}" + "%";
		        		}
		        	out.println(outputline);
	        		}
	        	}
	        	//submit request processing
	        	else if(inputs[0].equals("submit")) {
	        		dupe = false;
	        		for(int i=0; i < server.bib.size(); i++) {
		        		if(Long.parseLong(inputs[1]) == server.bib.get(i).isbn) 
		        			dupe = true;
		        	}
	        		if (dupe == true) {
	        			out.println("Request Error: Cannot add multiple entries with the same ISBN");
	        		}else {
	        			bibliography newentry = new bibliography(Long.parseLong(inputs[1]),inputs[2],inputs[3],inputs[4],Integer.parseInt(inputs[5]));
	        			server.bib.add(newentry);
	        			out.println("New entry added. New bibliography size: " + server.bib.size());
	        		} 
	        	 }
	        	//remove request processing
	        	else if(inputs[0].equals("remove")) {
	        		boolean found = false;
	        		int a = 1;
	        		while(a < inputs.length) {
	        			int b = 0;
	        			while (b < server.bib.size()) {
	        				if(inputs[a].equals("isbn")) {
	        					if(server.bib.get(b).isbn == Long.parseLong(inputs[a+1])) {
	        						server.bib.remove(b);
	        						found = true;
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("title")) {
	        					if(server.bib.get(b).title.equals(inputs[a+1]) == true) {
	        						server.bib.remove(b);
	        						found = true;
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("author")) {
	        					if(server.bib.get(b).author.equals(inputs[a+1]) == true) {
	        						server.bib.remove(b);
	        						found = true;
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("publisher")) {
	        					if(server.bib.get(b).publisher.equals(inputs[a+1]) == true) {
	        						server.bib.remove(b);
	        						found = true;
	        					}else {
	        						b++;
	        					}
	        				}
	        				else if(inputs[a].equals("year")) {
	        					if(server.bib.get(b).year == Integer.parseInt(inputs[a+1])) {
	        						server.bib.remove(b);
	        						found = true;
	        					}else {
	        						b++;
	        					}
	        				}
	        			}
	        			a = a + 2;
	        		}
	        		if (found == true) {
	        			out.println("Entry(s) removed. New bibliography size: " + server.bib.size());
	        		} else {
	        			out.println("Request Error: Could not find any entries with specificed fields");
	        		}
	        	 }
	        	//update request processing
	        	else if(inputs[0].equals("update")) {
	        		dupe = true;
	        		for(int i=0; i<server.bib.size(); i++) {
	        			if(Long.parseLong(inputs[1]) == server.bib.get(i).isbn) {
	        				if(inputs[2].equals("title")) {
	        					server.bib.get(i).title = inputs[3];
	        				}
	        				else if(inputs[2].equals("author")) {
	        					server.bib.get(i).author = inputs[3];
	        				}
	        				else if(inputs[2].equals("publisher")) {
	        					server.bib.get(i).publisher = inputs[3];
	        				}
	        				else if(inputs[2].equals("year")) {
	        					server.bib.get(i).year = Integer.parseInt(inputs[3]);
	        				}
		        			dupe = false;
		        			break;
	        			}
	        		}
	        		if (dupe == false) {
	        			out.println("Entry updated. ");
	        		} else {
	        			out.println("Request Error: Cannot find entry to be updated");
	        		}
	        	 }
	        	else if(inputs[0].equals("falseexit")) {
	 	        	out.println(" ");
	        	 }  
	        	else if(inputs[0].equals("exit")) {
	 	        	break;
	        	 }           
	        }
	        out.close();
	        in.close();
		} catch (IOException e)  {
			System.err.println("Connection Error:" + e);
		}
	}
}
