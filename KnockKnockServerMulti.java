import java.net.*;
import java.io.*;
import java.util.*;

public class KnockKnockServerMulti {
    public static void main(String[] args) throws IOException {
    	/*
    	int portn;
    	System.out.println("Enter server port:");
    	if (args.length < 1) {
    		System.err.println("Invalid port number");
    
    	}
    	else {
    		portn = Integer.parseInt(args[0]);
    	}
    	*/

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
        	serverSocket = new ServerSocket(8888);
            while (true) {
            	clientSocket = serverSocket.accept();
            	RequestHandlert request = new RequestHandlert(clientSocket);
            	Thread thread = new Thread (request);
            	thread.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        } 
    }
}

final class RequestHandlert implements Runnable {
	private final Socket client;
	
	public RequestHandlert(Socket client) {
		this.client = client;
	}
	
	public void run() {
		try {
			processrequest();
		} catch (Exception e) {
			
		}
	}
	
	private void processrequest() throws Exception {
		try {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(
					new InputStreamReader(
					client.getInputStream()));
	        String inputLine, outputLine;
	        KnockKnockProtocol kkp = new KnockKnockProtocol();

	        outputLine = kkp.processInput(null);
	        out.println(outputLine);

	        while ((inputLine = in.readLine()) != null) {
	             outputLine = kkp.processInput(inputLine);
	             out.println(outputLine);
	             if (outputLine.equals("Bye."))
	                break;
	        }
	        out.close();
	        in.close();
		} catch (IOException e)  {
			System.err.println("requesthander error.");
			
		}	
	}
}
