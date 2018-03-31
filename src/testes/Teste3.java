package testes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Teste3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Electrodes(args[0],args[1], args[2] );
		
	}

	public static void Electrodes(String fileX, String fileY, String FileName) throws IOException{
		HashMap<Integer, HashMap<String, Double>> map = new HashMap<>();
		String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\"+fileX;
		System.out.println(nomeFicheiro);
		BufferedReader br = null;
		String line = "";
		ArrayList<Integer> Ys = ValoresY(fileY);
		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			String info = br.readLine();
			int position = 0;
			PrintWriter pw = new PrintWriter(new File("C:\\Users\\Eu\\Desktop\\"+FileName));
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				String[] infos = info.split(",");
				if (position >= 1801){
					StringBuilder sb = new StringBuilder();
					sb.append(linha[0]+","+linha[1]+","+linha[2]+","+linha[3]+","+linha[4]+
							","+linha[5]+","+linha[6]+","+linha[7]+","+Ys.get(position)+"\n");
					pw.write(sb.toString());
					System.out.println(position);
				}
				//				HashMap<String, Double> inside = new HashMap<>();
				//				inside.put(infos[0], new Double(linha[0]));
				//				inside.put(infos[1], new Double(linha[1]));
				//				inside.put(infos[2], new Double(linha[2]));
				//				inside.put(infos[3], new Double(linha[3]));
				//				inside.put(infos[4], new Double(linha[4]));
				//				inside.put(infos[5], new Double(linha[5]));
				//				inside.put(infos[6], new Double(linha[6]));
				//				inside.put(infos[7], new Double(linha[7]));
				//				map.put(position, inside);

				position++;
				
			}
			System.out.println(position);
			pw.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static ArrayList<Integer> ValoresY(String fileY) throws IOException{
		ArrayList<Integer> arrayList = new ArrayList<>();
		String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\"+fileY;
		BufferedReader br = null;
		String line = "";

		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			br.readLine();
			int numb = 1;
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				arrayList.add(new Integer(linha[0]));
				numb++;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return arrayList;
	}

}
