package prereqCheckY;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;

public class PrereqCheckMap {

	public static void main(String[] args) {
	
		try {
	     
			 Map<String, String> values = new CSVReaderHeaderAware(new FileReader("17-18.csv")).readMap();	
			 System.out.println(values.size());

			 for (String key : values.keySet()) {
					System.out.println(key);
				}
						
  				 System.out.println();

				
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
