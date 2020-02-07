package gui;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.TextArea;
import com.opencsv.CSVReader;
import java.io.FileReader;

public class PreReqGUI2 {

  public static final int MODE_OPEN = 1;
  public static final int MODE_SAVE = 2;
	private JFrame vicsAwesomePrefrm = new JFrame();
	private JTextField marksFileTxt = new JTextField();
	JLabel marksFilelbl = new JLabel("Marks File: ");
	
	JLabel asnFilelbl = new JLabel("ASN File: ");
	JLabel courseCodeLbl = new JLabel("Course Code: ");
  private JTextField asnFileTxt = new JTextField();
  private JTextField courseCodeTxt = new JTextField();
  private JTextField asnNumTxt = new JTextField();
  JButton singleCtsSearchBtn = new JButton("CTS Search");
  JButton runSearchBtn = new JButton("Search");
  JButton marksFileBrowseBtn = new JButton("Browse");
	JButton asnFileBrowseBtn = new JButton("Browse");
  JRadioButton singleCtsSearchOption = new JRadioButton("Single CTS Search");
  JRadioButton singleFullSearchOption = new JRadioButton("Single Full Search");
  JRadioButton fullCtsSearchOption = new JRadioButton("Full CTS Search");
  ButtonGroup group = new ButtonGroup();
  
  TextArea resultsTxtArea = new TextArea();
  int searchMode = 0;
  private JButton singleFullSearchBtn = new JButton("Full Search");
  private JTextField scheduleFileTxt;
  private final JButton loadASNBtn = new JButton("Load ASN");
  
  ArrayList<String[]> ASNList = new ArrayList<>();
  private JTextField courseNumTxt;
  
  static int binarySearch(List<String[]> values, int N) {
    
    int lowestPossibleLoc = 1;
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
   
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreReqGUI2 window = new PreReqGUI2();
					window.vicsAwesomePrefrm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PreReqGUI2() {
		initialize();
	}

	public void search() {
		try {
			CSVReader reader = new CSVReader(new FileReader(marksFileTxt.getText()));
			List<String[]> values = reader.readAll();
			
			String code ="";
			int asn = 0, count = 0, temp; // vars to validate ASN
			int runNum = 1;
	if(searchMode == 1) {//serach single ASN for specific cts credits
		int introCr = 0;
 	 int interCr = 0;
 	 int advCr = 0;
 	 int totalCr = 0;
		resultsTxtArea.setText(null);
		int singleAsn = Integer.parseInt(asnNumTxt.getText());
			resultsTxtArea.append("Single search\n");
			
			 code = courseCodeTxt.getText().toUpperCase();
			 int record = binarySearch(values,singleAsn);//search for record num to cut looping
			 if (record == -1) {
				 resultsTxtArea.append("\n***ASN NOT FOUND***\n");
			 }
//			 resultsTxtArea.append("Record: " + Integer.toString(record) + (values.get(record)[0]));
			 
			 if (record < 500) { //temp fix - found record num is too far forward 
    		 //so in the next for loop start 500 before found record num to be safe
    		 //if record num is < 500 bump it up to 501 or else it will throw index out of bounds
    		 record = 501;
//    		 System.out.println(record);//***debug***
    	 }
//    	   System.out.println(record);//***debug***
    	 for(int i = record-500; i < values.size(); i++) {//search through records
			 if(Integer.parseInt(values.get(i)[0]) > singleAsn) {//stop search after current asn 
//				 System.out.println(i+" - "+(values.get(i)[0]) + " BREAK");//***debug***
				 break;
			 }
			
			 if(//!values.get(i)[6].equals("") && 
					 values.get(i)[0].equals(Integer.toString(singleAsn)) && 
					 values.get(i)[3].contains(code)) {//print out if asn and course code match
				 String format = "|%1$-15s|%2$-20s|%3$-10s|%4$-40s|%5$-10s|\n";//string formatting
				 String s = String.format(format,("ASN "+values.get(i)[0]), 
						 values.get(i)[1], 
						 values.get(i)[3],
						 values.get(i)[4],
						 ("Grade: "+values.get(i)[6] ));
				 
				 //tally credits
				 if(values.get(i)[3].charAt(3) == '1' && 
						 Integer.parseInt(values.get(i)[7]) > 0) {
					 introCr++;
					 totalCr++;
				 }else if(values.get(i)[3].charAt(3) == '2' && 
						 Integer.parseInt(values.get(i)[7]) > 0) {
					 interCr++;
					 totalCr++;
				 }else if(values.get(i)[3].charAt(3) == '3' && 
						 Integer.parseInt(values.get(i)[7]) > 0) {
					 advCr++;
					 totalCr++;
				 }
				 
				 resultsTxtArea.append(s);
			 }//end if to print records
    	 }//end for loop to look for asn
    	 resultsTxtArea.append("\nTotal Credits: " + totalCr +
 	    		"\nIntro Credits: " + introCr +
 	    		"\nInter Credits: " + interCr +
 	    		"\nADV Credits: " + advCr + "\n");
    	//basic prereq check **only counts credits by level**
				if (introCr >= 3 && !code.equalsIgnoreCase("CKA"))
					resultsTxtArea.append("APPROVED for INTERMEDIATE!\n");
				else if(introCr < 3 && !code.equalsIgnoreCase("CKA"))
					resultsTxtArea.append("*** REMOVE FROM INTERMEDIATE! ***1\n");
				
				if (advCr >= 3 && code.equalsIgnoreCase("CKA") && advCr < 5) {
					resultsTxtArea.append("APPROVED for INTERMEDIATE!");
				} else if(code.equalsIgnoreCase("CKA") && advCr < 3)
					resultsTxtArea.append("*** REMOVE FROM INTERMEDIATE! ***2\n");

				if (interCr >= 3 || totalCr >= 8)
					resultsTxtArea.append("APPROVED for ADVANCED!\n");
				else
					resultsTxtArea.append("*** REMOVE FROM ADVANCED! ***\n");
 	    
			}//end single search
	
//search for all credits for one ASN			
	if(searchMode == 2) {
		resultsTxtArea.setText(null);
		int singleAsn = Integer.parseInt(asnNumTxt.getText());
		resultsTxtArea.append("\nSingle full search\n");
		
		List<String[]> allCrList = new ArrayList<>();
		
		int record = binarySearch(values,singleAsn);//search for record num to cut looping
		if (record == -1) {
			 resultsTxtArea.append("\n***ASN NOT FOUND***\n");
		 }
		 resultsTxtArea.append("Record: " + Integer.toString(record) + (values.get(record)[0])+"\n");
		 
		 if (record < 500) { //temp fix - found record num is too far forward 
  		 //so in the next for loop start 500 before found record num to be safe
  		 //if record num is < 500 bump it up to 501 or else it will throw index out of bounds
  		 record = 501;
//  		 System.out.println(record);//***debug***
  	 }
//  	   System.out.println(record);//***debug***
		 int introCr = 0;
  	 int interCr = 0;
  	 int advCr = 0;
  	 int totalCr = 0;
  	 int ctsCr = 0;
  	 int fullCr = 0;
  	 int otherCr = 0;
  	 
  	 for(int i = record-500; i < values.size(); i++) {//search through records
		 if(Integer.parseInt(values.get(i)[0]) > singleAsn) {//stop search after current asn 
//			 System.out.println(i+" - "+(values.get(i)[0]) + " BREAK");//***debug***
			 break;
		 }
		 
		 if(values.get(i)[0].equals(Integer.toString(singleAsn))) {//print out if asn matches
			 String format = "|%1$-15s|%2$-20s|%3$-10s|%4$-40s|%5$-10s|%6$-10s|\n";//string formatting
			 String s = String.format(format,("ASN "+values.get(i)[0]), 
					 values.get(i)[1], 
					 values.get(i)[3],
					 values.get(i)[4],
					 ("Grade: "+values.get(i)[6]),
					 ("Credits: "+values.get(i)[7]));
			 
			 allCrList.add(values.get(i));
			 
			 //simple credit tally
			 if(values.get(i)[3].charAt(3) == '1' && 
					 Integer.parseInt(values.get(i)[7]) > 0) {
				 introCr += Integer.parseInt(values.get(i)[7]);
				 totalCr += Integer.parseInt(values.get(i)[7]);
			 }else if(values.get(i)[3].charAt(3) == '2' && 
					 Integer.parseInt(values.get(i)[7]) > 0) {
				 interCr += Integer.parseInt(values.get(i)[7]);
				 totalCr += Integer.parseInt(values.get(i)[7]);
			 }else if(values.get(i)[3].charAt(3) == '3' && 
					 Integer.parseInt(values.get(i)[7]) > 0) {
				 advCr += Integer.parseInt(values.get(i)[7]);
				 totalCr += Integer.parseInt(values.get(i)[7]);
			 }
			 //tally single 3 and 5 credit courses
			 if( Integer.parseInt(values.get(i)[7]) == 1)
				 ctsCr += Integer.parseInt(values.get(i)[7]);
			 if( Integer.parseInt(values.get(i)[7]) == 3)
				 otherCr += Integer.parseInt(values.get(i)[7]);
			 if( Integer.parseInt(values.get(i)[7]) == 5)
				 fullCr += Integer.parseInt(values.get(i)[7]);
			 
			 resultsTxtArea.append(s);
		 }//end if to print records
  	 }//end for loop to look for asn
  	 resultsTxtArea.append("\nTotal Credits: " + totalCr +
	    		"\nIntro Credits: " + introCr +
	    		"\nInter Credits: " + interCr +
	    		"\nAdv Credits: " + advCr + 
	    		"\nCTS Credits: " + ctsCr +
	    		"\nOTHER Credits: " + otherCr +
	    		"\nFULL Credits: " + fullCr );
  	 
		}//end single FULL search
	
//search for all ASN in a file for specific CTS code	     
			 else if(searchMode == 0) {
				 //next 3 lines old code
//				 reader = new CSVReader(new FileReader(asnFileTxt.getText()));
//					//read in student list
//							List<String[]> students = reader.readAll();
							
	     System.out.print("Enter Course Code (ie CSE): ");	     
  		 code = courseCodeTxt.getText().toUpperCase();
  		 resultsTxtArea.setText(null);

//  		 while(runNum < student.size()) {
  		while(runNum < ASNList.size()) {
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
	    	 		  	    	 
//	    	 asn = Integer.parseInt(students.get(runNum)[2]);//old code
	    	 asn = Integer.parseInt(ASNList.get(runNum)[5]);
	    	 runNum--;
	    	 runNum++;
	    	 System.out.println();
	    	 resultsTxtArea.append("");
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
//					 System.out.println((values.get(i)[0]) + " search done - BREAK");//***debug***
					 break;
				 }
				 //print out matching records
//				 System.out.println("module: " + (values.get(i)[3])); //debug - print all codes for matching record
				 if(//!values.get(i)[6].equals("") && 
						 values.get(i)[0].equals(Integer.toString(asn)) && 
						 values.get(i)[3].contains(code)) {//print out if asn and course code match
					 String format = "|%1$-15s|%2$-20s|%3$-10s|%4$-40s|%5$-10s|\n";//string formatting
					 String s = String.format(format,("ASN "+values.get(i)[0]), 
							 values.get(i)[1], 
							 values.get(i)[3],
							 values.get(i)[4],
							 ("Grade: "+values.get(i)[6] ));
					 
					 resultsTxtArea.append(s);
					 System.out.format(format,("ASN "+values.get(i)[0]), 
							 values.get(i)[1], 
							 values.get(i)[3],
							 values.get(i)[4],
							 ("Grade: "+values.get(i)[6] ));
//					 System.out.println("CHAR AT: " + (values.get(i)[2].charAt(3)));//**debug to check module level
//					 System.out.println("CREDITS AWARDED: " + values.get(i)[7]);//**debug
					 if(values.get(i)[3].charAt(3) == '1' && 
							 Integer.parseInt(values.get(i)[7]) > 0) {
						 introCr++;
						 totalCr++;
					 }else if(values.get(i)[3].charAt(3) == '2' && 
							 Integer.parseInt(values.get(i)[7]) > 0) {
						 interCr++;
						 totalCr++;
					 }else if(values.get(i)[3].charAt(3) == '3' && 
							 Integer.parseInt(values.get(i)[7]) > 0) {
						 advCr++;
						 totalCr++;
					 }
					 
					 if(code.equals("CSE") ) {
					 //check prereq for intermediate
					 if(values.get(i)[3].contains("CSE1110")) 
						 inter1 = true;
					 if(values.get(i)[3].contains("CSE1120")) 
						 inter2 = true;
					 if(values.get(i)[3].contains("CSE1910")) 
						 inter3 = true;
					 //check for prereq for advanced
					 if(values.get(i)[3].contains("CSE2110")) 
						 adv1 = true;
					 if(values.get(i)[3].contains("CSE2120")) 
						 adv2 = true;
					 if(values.get(i)[3].contains("CSE2910")) 
						 adv3 = true;
					 if(values.get(i)[3].contains("CSE2140")) 
						 adv4 = true;
					 if(values.get(i)[3].contains("CSE3120")) 
						 adv5 = true;
					 }
					 
//  				 System.out.println();
  			
				 }//end if to check for CON module outside if to check if grade exists
			 }//end loop through records
	    resultsTxtArea.append("\nTotal Credits: " + totalCr +
	    		"\nIntro Credits: " + introCr +
	    		"\nInter Credits: " + interCr +
	    		"\nADV Credits: " + advCr + "\n");
	    
				 //report for intermediate
	  if (code.equals("CSE")) {
	  	if(totalCr >= 3 && adv1 && adv2 && adv3 && adv4 && adv5)
	    	resultsTxtArea.append("APPROVED for ADVANCED\n");
	    else if(totalCr > 3 && totalCr < 8){
	    	resultsTxtArea.append("*** REMOVE FROM ADVANCED ***\n");
	    	if(!adv1)
	    		resultsTxtArea.append("\tMissing CSE2110");
	    	if(!adv2)
	    		resultsTxtArea.append("\tMissing CSE2120");
	    	if(!adv3)
	    		resultsTxtArea.append("\tMissing CSE2910");
	    	if(!adv4)
	    		resultsTxtArea.append("\tMissing CSE2140");
	    	if(!adv5)
	    		resultsTxtArea.append("\tMissing CSE3120");
	    }
	    else if(inter1 && inter2 && inter3)    //report for advanced
	    	resultsTxtArea.append("APPROVED for INTERMEDIATE\n");
	    else {
	    	resultsTxtArea.append("*** REMOVE FROM INTERMEDIATE ***\n");
	    	if(!inter1)
	    		resultsTxtArea.append("\tMissing CSE1110");
	    	if(!inter2)
	    		resultsTxtArea.append("\tMissing CSE1120");
	    	if(!inter3)
	    		resultsTxtArea.append("\tMissing CSE1910");
	    }
	  }//end if code == CSE
	  resultsTxtArea.append("\n");
	  if(!code.equals("CSE")) {
	    //basic prereq check **only counts credits by level**
				if (introCr >= 3 && !code.equalsIgnoreCase("CKA"))
					resultsTxtArea.append("APPROVED for INTERMEDIATE!\n");
				else if(introCr < 3 && !code.equalsIgnoreCase("CKA"))
					resultsTxtArea.append("*** REMOVE FROM INTERMEDIATE! ***1\n");
				
				if (advCr >= 3 && code.equalsIgnoreCase("CKA") && advCr < 5) {
					resultsTxtArea.append("APPROVED for INTERMEDIATE!");
				} else if(code.equalsIgnoreCase("CKA") && advCr < 3)
					resultsTxtArea.append("*** REMOVE FROM INTERMEDIATE! ***2\n");

				if (interCr >= 3 || totalCr >= 8)
					resultsTxtArea.append("APPROVED for ADVANCED!\n");
				else
					resultsTxtArea.append("*** REMOVE FROM ADVANCED! ***\n");
	    }

			 resultsTxtArea.append("***************************************************************\n");
	     }//end while loop that runs program
	     
	     resultsTxtArea.append("BYE");
	     reader.close();
		}//end else for full search
		}catch (Exception e1) {
			e1.printStackTrace();
		}//end catch
			
	}//end search()
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		vicsAwesomePrefrm.setTitle("Vic's Awesome Pre Req Checker");
		vicsAwesomePrefrm.setBounds(100, 100, 700, 502);
		vicsAwesomePrefrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vicsAwesomePrefrm.getContentPane().setLayout(null);
		
		marksFilelbl.setBounds(10, 11, 93, 14);
		vicsAwesomePrefrm.getContentPane().add(marksFilelbl);
		
		marksFileTxt.setBounds(113, 7, 206, 20);
		vicsAwesomePrefrm.getContentPane().add(marksFileTxt);
		marksFileTxt.setColumns(10);
		
		marksFileBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					marksFileTxt.setText(selectedFile.getAbsolutePath());
				}		
			}
		});
		
		marksFileBrowseBtn.setBounds(335, 7, 99, 23);
		vicsAwesomePrefrm.getContentPane().add(marksFileBrowseBtn);
		asnFileBrowseBtn.setEnabled(false);
		
		asnFileBrowseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					asnFileTxt.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		asnFileBrowseBtn.setBounds(335, 36, 99, 23);
		vicsAwesomePrefrm.getContentPane().add(asnFileBrowseBtn);
		asnFileTxt.setEnabled(false);
		
		asnFileTxt.setColumns(10);
		asnFileTxt.setBounds(113, 36, 206, 20);
		vicsAwesomePrefrm.getContentPane().add(asnFileTxt);
		
		asnFilelbl.setBounds(10, 40, 93, 14);
		vicsAwesomePrefrm.getContentPane().add(asnFilelbl);
		runSearchBtn.setEnabled(false);
		
		runSearchBtn.setBounds(335, 69, 99, 23);
		vicsAwesomePrefrm.getContentPane().add(runSearchBtn);
		courseCodeTxt.setEnabled(false);
		
		courseCodeTxt.setColumns(10);
		courseCodeTxt.setBounds(113, 69, 206, 20);
		vicsAwesomePrefrm.getContentPane().add(courseCodeTxt);
				
		courseCodeLbl.setBounds(10, 74, 93, 14);
		vicsAwesomePrefrm.getContentPane().add(courseCodeLbl);
		singleCtsSearchBtn.setEnabled(false);
		
		singleCtsSearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchMode = 1;
				if(!asnNumTxt.getText().isEmpty())
					search();
				else
					resultsTxtArea.append("Invalid ASN\n");
			}
		});
		
		
		singleCtsSearchBtn.setBounds(335, 433, 121, 23);
		vicsAwesomePrefrm.getContentPane().add(singleCtsSearchBtn);
		asnNumTxt.setEnabled(false);
		
		asnNumTxt.setColumns(10);
		asnNumTxt.setBounds(113, 433, 206, 20);
		vicsAwesomePrefrm.getContentPane().add(asnNumTxt);
		
		JLabel ansNumLbl = new JLabel("Single ASN  ");
		ansNumLbl.setBounds(10, 436, 93, 14);
		vicsAwesomePrefrm.getContentPane().add(ansNumLbl);
		
		resultsTxtArea.setBounds(10, 134, 650, 292);
		vicsAwesomePrefrm.getContentPane().add(resultsTxtArea);
		singleFullSearchBtn.setEnabled(false);
		singleFullSearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchMode = 2;
				if(!asnNumTxt.getText().isEmpty())
					search();
				else
					resultsTxtArea.append("Invalid ASN - no full search possible\n");
			}
			
		});
		singleFullSearchBtn.setBounds(466, 433, 113, 23);
		
		vicsAwesomePrefrm.getContentPane().add(singleFullSearchBtn);

		runSearchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchMode = 0;
				
				if(!scheduleFileTxt.getText().isEmpty())
					search();
				else
					resultsTxtArea.append("Invalid ASN File Location\n");
			}//end runSearchBtn action
		});//end runSearchBtn action listener
		group.add(singleCtsSearchOption);
    group.add(singleFullSearchOption);
    group.add(fullCtsSearchOption);
//    asnFileTxt = new JTextField();
//    private JTextField courseCodeTxt = new JTextField();
//    private JTextField asnNumTxt = new JTextField();
    
    singleCtsSearchOption.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		singleCtsSearchBtn.setEnabled(true);
    		runSearchBtn.setEnabled(false);
    		asnFileBrowseBtn.setEnabled(false);
    		singleFullSearchBtn.setEnabled(false);
    		asnFileTxt.setText("");
    		courseCodeTxt.setText("");
    		courseCodeTxt.setEnabled(true);
    		asnNumTxt.setText("");
    		asnFileTxt.setEnabled(false);
    		asnNumTxt.setEnabled(true);
    	}
    });
    singleCtsSearchOption.setSize(150, 23);
    singleCtsSearchOption.setLocation(461, 30);
    vicsAwesomePrefrm.getContentPane().add(singleCtsSearchOption);
    
    singleFullSearchOption.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		singleCtsSearchBtn.setEnabled(false);
    		runSearchBtn.setEnabled(false);
    		asnFileBrowseBtn.setEnabled(false);
    		singleFullSearchBtn.setEnabled(true);
    		asnFileTxt.setText("");
    		courseCodeTxt.setText("");
    		asnNumTxt.setText("");
    		asnFileTxt.setEnabled(false);
    		courseCodeTxt.setEnabled(false);
    		asnNumTxt.setEnabled(true);
    	}
    });
    singleFullSearchOption.setSize(150, 23);
    singleFullSearchOption.setLocation(461, 70);
    vicsAwesomePrefrm.getContentPane().add(singleFullSearchOption);
    
    fullCtsSearchOption.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		singleCtsSearchBtn.setEnabled(false);
    		runSearchBtn.setEnabled(true);
    		asnFileBrowseBtn.setEnabled(true);
    		singleFullSearchBtn.setEnabled(false);
    		asnFileTxt.setText("");
    		courseCodeTxt.setText("");
    		asnNumTxt.setText("");
    		asnFileTxt.setEnabled(true);
    		courseCodeTxt.setEnabled(true);
    		asnNumTxt.setEnabled(false);
    	}
    });
    fullCtsSearchOption.setSize(150, 23);
    fullCtsSearchOption.setLocation(461, 50);
    vicsAwesomePrefrm.getContentPane().add(fullCtsSearchOption);
    
    JLabel schedLbl = new JLabel("Schedule File");
    schedLbl.setBounds(10, 105, 93, 14);
    vicsAwesomePrefrm.getContentPane().add(schedLbl);
    
    scheduleFileTxt = new JTextField();
    scheduleFileTxt.setEnabled(false);
    scheduleFileTxt.setColumns(10);
    scheduleFileTxt.setBounds(113, 100, 206, 20);
    vicsAwesomePrefrm.getContentPane().add(scheduleFileTxt);
    
    JButton loadbtn = new JButton("Browse");
    loadbtn.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent arg0) {
    		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					scheduleFileTxt.setText(selectedFile.getAbsolutePath());
				}
    	}
    });
    loadbtn.setBounds(335, 100, 99, 23);
    vicsAwesomePrefrm.getContentPane().add(loadbtn);
    
    loadASNBtn.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		ASNList.clear();
    		resultsTxtArea.setText("");
				try {
					CSVReader reader = new CSVReader(new FileReader(scheduleFileTxt.getText()));
					List<String[]> scheduleList = reader.readAll();
					//search scheduleList for matching course code
					for(int i = 0; i < scheduleList.size(); i++) {
						if(scheduleList.get(i)[13].toLowerCase().contains(courseNumTxt.getText().toLowerCase())) {
							ASNList.add(scheduleList.get(i));
							resultsTxtArea.append(scheduleList.get(i)[3]+"\t"+ scheduleList.get(i)[5]+"\n");
						}
					}//end search
	  			
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
  			
    		
    	}
    });
    loadASNBtn.setBounds(466, 100, 113, 23);
    
    vicsAwesomePrefrm.getContentPane().add(loadASNBtn);
    
    courseNumTxt = new JTextField();
    courseNumTxt.setBounds(563, 8, 86, 20);
    vicsAwesomePrefrm.getContentPane().add(courseNumTxt);
    courseNumTxt.setColumns(10);
    
    JLabel courseNumlbl = new JLabel("Course Number:");
    courseNumlbl.setBounds(466, 11, 87, 14);
    vicsAwesomePrefrm.getContentPane().add(courseNumlbl);

	}//end initialize
}//end class
