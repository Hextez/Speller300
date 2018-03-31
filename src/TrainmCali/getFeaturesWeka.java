package TrainmCali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class getFeaturesWeka {
	
	public static void main(String[] args) throws IOException{
		
		File file = new File(args[0]);
		
		for (File fich : file.listFiles()) {
			HashMap<String, Integer> how = new HashMap<>();
			FileReader fileReader = new FileReader(fich);
			BufferedReader br = new BufferedReader(fileReader);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] stringlist = sCurrentLine.split(",");
				int t = stringlist.length;
				//int t = 10;
 				for (int i = 0; i < t; i++ ) {
					if (!how.containsKey(stringlist[i]) && stringlist[i] != ""){
						how.put(stringlist[i], 1);
					}else if (how.containsKey(stringlist[i])){
						how.put(stringlist[i], how.get(stringlist[i])+1);
					}
				}
			}
			for (String string : how.keySet()) {
				System.out.println(string + " ---- " + how.get(string));
			}
			
			System.out.println("\n\n-----\n\n");
		}		
	}
}
