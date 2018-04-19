import java.net.*;
import java.io.*;
import java.util.*;

public class canvas {
	public static void main(String[] args) {
		//String a = "fwfwafwafwafwa";
		//System.out.println(a.substring(0, Math.min(a.length(), 4)));
		ArrayList<Integer> aa =
			    new ArrayList<Integer>(Arrays.asList(3,3,3,6,7,34,4,2,45,6,3,2,4,5,6,4,3,3,3,3,3,3,3,3));
		int b = 0;
		while (b < aa.size()) {
			if (aa.get(b) == 3) {
				aa.remove(b);
			}else
				b++;
		}
		System.out.println(aa.size());
		System.out.println(aa);
		
		String fromuser;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("IP address: ");
		try {
        fromuser = stdIn.readLine();
        while(fromuser.toLowerCase().matches("(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))") != true) {
        	System.out.println("Input Error: Invalid IP address format");
        	fromuser = stdIn.readLine();
        }
		}
        catch (IOException e) {
        	
        }
	}
}
