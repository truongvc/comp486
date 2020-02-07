package prereqCheckY;

import java.io.FileReader;
import java.util.List;
import com.opencsv.CSVReader;

public class PrereqCheck {

	public static void main(String[] args) {
	
		try {
			CSVReader reader = new CSVReader(new FileReader("17-20.csv"));
	     List<String[]> values = reader.readAll();
//	    reader = new CSVReader(new FileReader("18-19.csv"));
//	     List<String[]> values2 = reader.readAll();
//	    reader = new CSVReader(new FileReader("19-20.csv"));
//	     List<String[]> values3 = reader.readAll();
//	     values.addAll(values2);
//	     values.addAll(values3);
	     
//			 Map<String, String> values = new CSVReaderHeaderAware(new FileReader("17-18.csv")).readMap();	
			 System.out.println(values.size());
//			 
	     
			 for(int i = 0; i < values.size(); i++) {
				 //System.out.println(values.get(i).length);
				 if(//values.get(i)[11].equals("") && 
						 values.get(i)[0].equals("1074-5768-1") && 
						 values.get(i)[2].contains("CSE")) {

  				 for(int j = 0; j < values.get(i).length; j++) {
  					 if(j < 4 || j == 11)
  					 System.out.print(j+"*"+ values.get(i)[j]+"\t");
  				 }//end for loop to print record
  				 System.out.println();

				 }//end if to check for CON module outside if to check if grade exists
			 }//end loop through records
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
