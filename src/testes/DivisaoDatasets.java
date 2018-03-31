package testes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class DivisaoDatasets {


	private static Integer numberStart = 0;
	private final static Integer MS_250 = 64;
	private final static Integer MS_1000 = 256;
	private final static ArrayList<String> ListOfElectrodes = new ArrayList<>(Arrays.asList("Fz","Cz","Pz","Oz","P3","P4","PO7","PO8")); 
	private final static ArrayList<String> ListOfElectrodesAS = new ArrayList<>(Arrays.asList("Fz", "FCz", "Cz", "CPz", "Pz", "Oz", "F3", "F4", "C3", "C4", "CP3", "CP4", "P3", "P4", "PO7", "PO8")); 

	private static ArrayList<Integer> classification = new ArrayList<>();
	private final static ArrayList<Integer> listOfTrials = new ArrayList<>(Arrays.asList(1801, 11529, 21257, 30985, 40713,
			51473, 61201, 70929, 80657, 90385, 101145, 110873, 120601, 130329, 140057, 150817, 160545, 170273,
			180001, 189729, 200489, 210217, 219945, 229673, 239401, 250161, 259889, 269617, 279345, 289073, 299833,
			309561, 319289, 329017, 338745,347704));

	private final static ArrayList<Integer> listOfTrialsAS = new ArrayList<>(Arrays.asList(1545,9737, 17929, 26121, 34313, 42505,50184));


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File dir = new File(args[0]);
		for ( File FileEntry : dir.listFiles()){
			System.out.println(FileEntry.getName());
			HashMap<Integer, HashMap<String, Double>> electrodes = Electrodes(FileEntry.getAbsolutePath(), Integer.parseInt(args[3]));
			PrintWriter pw = new PrintWriter(new FileWriter(FileEntry.getName().split("-")[0]+"Target.csv", true));
			PrintWriter pw2 = new PrintWriter(new FileWriter(FileEntry.getName().split("-")[0]+"NTarget.csv", true));
			numberStart = 0;
			int trialPos = 0;
			int as = 1;
			while (numberStart < classification.size()){
				if (Integer.parseInt(args[3]) == 0){
					if (classification.get(numberStart) == 2 && trialPos < 36 && numberStart < listOfTrials.get(trialPos)-1){
						System.out.println(numberStart +"--------comecou aqui "+as);
						as++;
						for (int i=0 ; i < MS_1000 ; i++){
							StringBuilder sb = new StringBuilder();
							sb.append(electrodes.get(numberStart+i).get("Fz")+","+electrodes.get(numberStart+i).get("Cz")+","+electrodes.get(numberStart+i).get("Pz")+","+
									electrodes.get(numberStart+i).get("Oz")+","+electrodes.get(numberStart+i).get("P3")+","+electrodes.get(numberStart+i).get("P4")+","+
									electrodes.get(numberStart+i).get("PO7")+","+electrodes.get(numberStart+i).get("PO8")+","+classification.get(numberStart+i)+"\n");
							pw.write(sb.toString());
						}
						//pw.write("Proximos 154"+"\n");
					}else if (classification.get(numberStart) == 1 && trialPos < 36 && numberStart < listOfTrials.get(trialPos)-1){
						for (int i=0 ; i < MS_1000 ; i++){
							StringBuilder sb = new StringBuilder();
							sb.append(electrodes.get(numberStart+i).get("Fz")+","+electrodes.get(numberStart+i).get("Cz")+","+electrodes.get(numberStart+i).get("Pz")+","+
									electrodes.get(numberStart+i).get("Oz")+","+electrodes.get(numberStart+i).get("P3")+","+electrodes.get(numberStart+i).get("P4")+","+
									electrodes.get(numberStart+i).get("PO7")+","+electrodes.get(numberStart+i).get("PO8")+","+classification.get(numberStart+i)+"\n");
							pw2.write(sb.toString());
						}
						//pw.write("Proximos 154"+"\n");
					}
				}else {
					if (classification.get(numberStart) == 2 && trialPos < 7 && numberStart < listOfTrialsAS.get(trialPos)-1){
						//System.out.println(numberStart +"--------comecou aqui "+as+" ----- "+ (listOfTrialsAS.get(trialPos)-1));

						as++;
						for (int i=0 ; i < MS_1000 ; i++){
							StringBuilder sb = new StringBuilder();
							Iterator<String> lista = ListOfElectrodesAS.iterator();
							while( lista.hasNext()){
								sb.append(electrodes.get(numberStart+i).get(lista.next())+",");
							}
							sb.append(classification.get(numberStart+i)+"\n");
							pw.write(sb.toString());

						}
						//pw.write("Proximos 154"+"\n");
					}else if (classification.get(numberStart) == 1 && trialPos < 7 && numberStart < listOfTrialsAS.get(trialPos)-1){
						for (int i=0 ; i < MS_1000 ; i++){
							StringBuilder sb = new StringBuilder();
							Iterator<String> lista = ListOfElectrodesAS.iterator();
							while( lista.hasNext()){
								sb.append(electrodes.get(numberStart+i).get(lista.next())+",");
							}
							sb.append(classification.get(numberStart+i)+"\n");
							pw2.write(sb.toString());
						}
						//pw.write("Proximos 154"+"\n");
					}
				}
				//System.out.println(numberStart);

				numberStart = numberStart + MS_250;
				if (Integer.parseInt(args[3]) == 0 && trialPos < 36 && numberStart > listOfTrials.get(trialPos)){
					numberStart = listOfTrials.get(trialPos)-1;
					System.out.println(listOfTrials.get(trialPos)-1);
					System.out.println(trialPos + " ---- ");
					trialPos++;
				}else if( Integer.parseInt(args[3]) == 1 && trialPos < 7 && numberStart > listOfTrialsAS.get(trialPos)){
					numberStart = listOfTrialsAS.get(trialPos)-1;
					//System.out.println(listOfTrialsAS.get(trialPos)-1);
					//System.out.println(trialPos + " ---------------------" + (listOfTrialsAS.get(trialPos)-1));
					trialPos++;
				}
			}
			pw.close();
			pw2.close();
			System.out.println("done");
			//System.out.println(classification.get(42632));
		}



	}


	public static HashMap<Integer, HashMap<String, Double>> Electrodes(String fileXY, Integer val){
		HashMap<Integer, HashMap<String, Double>> map = new HashMap<>();
		//String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\Tese\\ALS\\dataSets\\"+fileXY;
		String nomeFicheiro = fileXY;
		BufferedReader br = null;
		String line = "";
		classification = new ArrayList<>();
		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			int position = 0;
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				HashMap<String, Double> inside = new HashMap<>();
				if(val == 0){
					inside.put(ListOfElectrodes.get(0), new Double(linha[0]));
					inside.put(ListOfElectrodes.get(1), new Double(linha[1]));
					inside.put(ListOfElectrodes.get(2), new Double(linha[2]));
					inside.put(ListOfElectrodes.get(3), new Double(linha[3]));
					inside.put(ListOfElectrodes.get(4), new Double(linha[4]));
					inside.put(ListOfElectrodes.get(5), new Double(linha[5]));
					inside.put(ListOfElectrodes.get(6), new Double(linha[6]));
					inside.put(ListOfElectrodes.get(7), new Double(linha[7]));
					classification.add(new Integer(linha[8]));

				}else{
					Iterator<String> lista = ListOfElectrodesAS.iterator();
					int coisa = 0;
					while(lista.hasNext()){
						inside.put(lista.next(), new Double(linha[coisa]));
						coisa++;
					}
					classification.add(new Integer(linha[coisa]));

				}

				map.put(position, inside);
				position++;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return map;

	}

}
