package testes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import demo.PriceVolumeDemo1;

public class DivisaoDatasetExpecifico {

	public static void main(String[] args) throws IOException{

		String line = "";
		BufferedReader br = null;
		ArrayList<String> linhas = new ArrayList<>();
		br = new BufferedReader(new FileReader("C:\\Users\\Eu\\Desktop\\Tese\\ALS\\User1-Target.csv"));
		while( (line = br.readLine())!= null){
			linhas.add(line);
		}
		System.out.println("feito sisis");
		PrintWriter pw = new PrintWriter(new File("Media.csv"));
		int totalLinhas = linhas.size();
		System.out.println(totalLinhas/256);
		for ( int b = 0; b < 6; b++){

			for (int i = 0; i<256 ; i++){
				StringBuilder sb = new StringBuilder();
				
				double totalPos = 0;
				for (int a = 0; a < totalLinhas; a=a+256){
					totalPos += Double.parseDouble(linhas.get(i+a).split(",")[b]);
					System.out.println(i+a);

				}
				//System.out.println(totalPos);
				sb.append(totalPos/(totalLinhas/256)+"\n");
				pw.write(sb.toString());
				pw.flush();

			}
		}
		pw.close();

	}

}
