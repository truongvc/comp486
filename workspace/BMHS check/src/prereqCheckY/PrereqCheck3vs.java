package prereqCheckY;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;

public class PrereqCheck3vs {

	/**
	 * Searches the array A for the integer N.
	 * Precondition:  A must be sorted into increasing order.
	 * Postcondition: If N is in the array, then the return value, i,
	 *    satisfies A[i] == N.  If N is not in the array, then the
	 *    return value is -1.
	 */
	static int binarySearch(List<String[]> values, int N) {
	      
	    int lowestPossibleLoc = 0;
	    int highestPossibleLoc = values.size() - 1;
	    
	    while (highestPossibleLoc >= lowestPossibleLoc) {
	       int middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
	       if (Integer.parseInt(values.get(middle)[0]) == N) {
	                 // N has been found at this index!
	          return middle;
	       }
	       else if (Integer.parseInt(values.get(middle)[0]) > N) {
	                 // eliminate locations >= middle
	          highestPossibleLoc = middle - 1;
	       }
	       else {
	                 // eliminate locations <= middle
	          lowestPossibleLoc = middle + 1;   
	       }
	    }
	    
	    // At this point, highestPossibleLoc < LowestPossibleLoc,
	    // which means that N is known to be not in the array.  Return
	    // a -1 to indicate that N could not be found in the array.
	 
	    return -1;
	  
	}

	
	public static void main(String[] args) {
	
		try {
			CSVReader reader = new CSVReader(new FileReader("17-20.csv"));
			List<String[]> values = reader.readAll();
			Scanner in = new Scanner(System.in);
			int asn = 0, count = 0, temp; // vars to validate ASN
			while (count != 9) {
				System.out.print("Enter ASN (-1 to exit): ");
				asn = in.nextInt();
				in.nextLine();
				temp = asn;
				while (temp != 0) {
					temp = temp / 10;
					++count;
				}
				if(count !=9) {
					System.out.println("Invalid ASN "+ count);
					count = 0;
				}
			}//end while loop for asn validation
	     
	     int runNum = 0;
	     String code ="";
	     while(asn != -1) {
	    	 boolean inter1 = false;
	    	 boolean inter2 = false;
	    	 boolean inter3 = false;
	    	 boolean adv1 = false;
	    	 boolean adv2 = false;
	    	 boolean adv3 = false;
	    	 boolean adv4 = false;
	    	 boolean adv5 = false;
	    	 int introCr = 0;
	    	 int interCr = 0;
	    	 int advCr = 0;
	    	 int totalCr = 0;
	    	 ArrayList<String> ctCreds = new ArrayList<String>();
	    	 boolean courseCodeDebug = true;
	    	 //debug mode and switch course code
	    	 if(courseCodeDebug == false || runNum == 0||asn ==0) {
	    		 if(asn == 0) {
	    			 System.out.print("****Switching course ****\nEnter ASN (-1 to exit): ");
	    	     asn = in.nextInt(); 
	    	     System.out.print("Enter NEW Course Code (ie CSE): ");	     
		    		 code = in.next().toUpperCase();
	    		 }else {
	    		 System.out.print("Enter Course Code (ie CSE): ");	     
	    		 code = in.next().toUpperCase();
	    		 }
	    		 runNum++;
	    	 }
	    
	    	 System.out.println();
	    	 
	    	 int record = binarySearch(values,asn);//search for record num to cut looping
//	    	 System.out.println("INDEX: " + binarySearch(values,asn)); //***debug***
//	     asn = 111115127;
//			 asn = 125738500;
	    	 if (record < 500) { //temp fix - found record num is too far forward 
	    		 //so in the next for loop start 500 before found record num to be safe
	    		 //if record num is < 500 bump it up to 501 or else it will throw index out of bounds
	    		 record = 501;
//	    		 System.out.println(record);//***debug***
	    	 }
//	    	   System.out.println(record);//***debug***
	    	 for(int i = record-500; i < values.size(); i++) {//search through records
				 if(Integer.parseInt(values.get(i)[0]) > asn) {//stop search after current asn 
//					 System.out.println((values.get(i)[0]) + " BREAK");//***debug***
					 break;
				 }
				 //print out matching records
				 if(!values.get(i)[11].equals("") && 
						 values.get(i)[0].equals(Integer.toString(asn)) && 
						 values.get(i)[2].contains(code)) {//print out if asn and course code match
					 String format = "|%1$-15s|%2$-20s|%3$-10s|%4$-30s|%5$-10s|\n";//string formatting

					 System.out.format(format,("ASN "+values.get(i)[0]), 
							 values.get(i)[1], 
							 values.get(i)[2],
							 values.get(i)[3],
							 ("Grade: "+values.get(i)[11] ));
					 System.out.println("CHAR AT: " + (values.get(i)[2].charAt(3)));
					 if(values.get(i)[2].charAt(3) == '1') {
						 introCr++;
						 totalCr++;
					 }else if(values.get(i)[2].charAt(3) == '2') {
						 interCr++;
						 totalCr++;
					 }else if(values.get(i)[2].charAt(3) == '3') {
						 advCr++;
						 totalCr++;
					 }
					 
					 if(code.equals("CSE") ) {//specific check for com sci
					 //check prereq for intermediate
					 if(values.get(i)[2].contains("CSE1110")) 
						 inter1 = true;
					 if(values.get(i)[2].contains("CSE1120")) 
						 inter2 = true;
					 if(values.get(i)[2].contains("CSE1910")) 
						 inter3 = true;
					 //check for prereq for advanced
					 if(values.get(i)[2].contains("CSE2110")) 
						 adv1 = true;
					 if(values.get(i)[2].contains("CSE2120")) 
						 adv2 = true;
					 if(values.get(i)[2].contains("CSE2910")) 
						 adv3 = true;
					 if(values.get(i)[2].contains("CSE2140")) 
						 adv4 = true;
					 if(values.get(i)[2].contains("CSE3120")) 
						 adv5 = true;
					 }
					 
					 if(code == "COM"){//specific check for comm tech
  					 
  					 ctCreds.add(values.get(i)[2]);
					 }//end check for comm tech  
  					 

  				 System.out.println();
  			
				 }//end if to check for matching Credits
			 }//end loop through records
	    System.out.println("\nTotal Credits: " + totalCr +
	    		"\nIntro Credits: " + introCr +
	    		"\nInter Credits: " + interCr +
	    		"\nADV Credits: " + advCr + "\n");
	    
	    //basic prereq check **only counts credits by level**
	    if(introCr >= 3)
	    	System.out.println("APPROVED for INTERMEDIATE!");
	    else
	    	System.out.println("*** REMOVE FROM INTERMEDIATE! ***");
	    if(interCr >= 3 || totalCr >= 8)
	    	System.out.println("APPROVED for ADVANCED!");
	    else
	    	System.out.println("*** REMOVE FROM ADVANCED! ***");
	    
	    //report for intermediate
	  if (code.equals("CSE")) {
	    if(inter1 && inter2 && inter3)
	    	System.out.println("APPROVED for INTERMEDIATE");
	    else {
	    	System.out.println("*** REMOVE FROM INTERMEDIATE ***");
	    	if(!inter1)
	    		System.out.println("\tMissing CSE1110");
	    	if(!inter2)
	    		System.out.println("\tMissing CSE1120");
	    	if(!inter3)
	    		System.out.println("\tMissing CSE1910");
	    }
	  
	    //report for advanced
	    if(adv1 && adv2 && adv3 && adv4 && adv5)
	    	System.out.println("APPROVED for ADVANCED");
	    else {
	    	System.out.println("*** REMOVE FROM ADVANCED ***");
	    	if(!adv1)
	    		System.out.println("\tMissing CSE2110");
	    	if(!adv2)
	    		System.out.println("\tMissing CSE2120");
	    	if(!adv3)
	    		System.out.println("\tMissing CSE2910");
	    	if(!adv4)
	    		System.out.println("\tMissing CSE2140");
	    	if(!adv5)
	    		System.out.println("\tMissing CSE3120");
	    }
	  }//end if code == CSE
	  
	  //comm tech check
	  if(code.equals("COM")) {
	  	if(!ctCreds.contains("COM1005")) {
			 	 System.out.println("DID NOT GET COM1005 CREDIT - REMOVE FROM ALL");
			 }
			 if(ctCreds.contains("COM1005") && ctCreds.contains("COM1205")) {
				 System.out.println("Approved for PHOTO");
			 }
			 if(ctCreds.contains("COM1005") && ctCreds.contains("COM1035")) {
				 System.out.println("Approved for Intermediate GRAPHICS");
			 }
			 if(ctCreds.contains("COM1005") && ctCreds.contains("COM1105")) {
				 System.out.println("Approved for Intermediate A/V");
			 }
			 if(ctCreds.contains("COM1005") && values.contains("COM1145")) {
				 System.out.println("Approved for Intermediate ANIMATION");
			 }
			 if(ctCreds.contains("COM1005") && ctCreds.contains("COM2035")) {
				 System.out.println("Approved for ADVANCED RASTER GRAPHICS");
			 }
			 if(ctCreds.contains("COM1005") && ctCreds.contains("COM2045")) {
				 System.out.println("Approved for ADVANCED VECTOR GRAPHICS");
			 }
	  }//end com tech check
	    
	     count = 0; //reset count for asn validation
	     System.out.print("Enter ASN (-1 to exit): ");
				asn = in.nextInt();
			 if(asn != -1 || asn != 0) { //validate asn or quit	
	     while (count != 9 &&(asn != -1 && asn != 0)) {
					
					in.nextLine();
					temp = asn;
					while (temp != 0) {
						temp = temp / 10;
						++count;
					}
					if(count !=9) {
						System.out.println("Invalid ASN "+ count);
						count = 0;
						System.out.print("Enter ASN (-1 to exit): ");
						asn = in.nextInt();
					}//end if count != 9
				}//end while loop for asn validation
			 }//end check for new asn or exit code
			 
	     }//end while loop that runs program
	     
	     System.out.println("BYE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
