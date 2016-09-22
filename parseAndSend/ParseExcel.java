package parseAndSend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class ParseExcel {

	public static void main(String[] args) throws FileNotFoundException {
		
		File excel = new File("/Users/btholmes/Downloads/ParseExcelNew.csv"); 
		Scanner in = new Scanner(excel); 
		Map<String, Integer> houses = new HashMap<String, Integer>(); 
		File sendFile = new File("SendMe.txt");
		PrintWriter write = new PrintWriter(sendFile); 
		
		in.nextLine(); 
		while(in.hasNextLine()){
			String line = in.nextLine(); 
			String[] row = line.split(","); 
			if(row[2].length() > 1){
//			System.out.println(row[2]);
			
			    if(houses.get(row[2]) == null){
			    	houses.put(row[2], 1); 
			    }else{
			    	houses.put(row[2], houses.get(row[2]) + 1);
			    }	
			}
		}
		Iterator it = houses.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        write.println(pair.getKey() + " = " + pair.getValue());
		        
		        it.remove(); 
		    }
		    write.flush();
	
		    Thread send = new Thread(new SendMail()); 
		    send.start();
		    
	}
	
}
