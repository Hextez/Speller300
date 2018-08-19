package classificador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ddf.EscherColorRef.SysIndexSource;

import mcali.Point;

public class Speller {

	private static HashMap<Integer, ArrayList<Double>> listOfBoxs = new HashMap<>();	
	private static HashMap<String, ArrayList<Double>> listOfBoxsToTest = new HashMap<>();

	private static HashMap<Integer, Integer> ListOfSignalsInBoxs = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> ListOfTargetAndNTarget = new HashMap<Integer, Integer>();
	private static HashMap<String, String> listOfBoxResults = new HashMap<>();
	private static LettersCatalog letterCatalog;
	private static ArrayList<Integer> listOfRowAndCollum = new ArrayList<Integer>();
	private static ArrayList<Integer> listOfTargetsNTargets;
	private static int multipler = 150;
	private static int epochSize;
	private static boolean readyToTest = true;
	private static StringBuilder sb = new StringBuilder();;


	public static void main(String[] args) throws Exception {

		int epochDuration = Integer.parseInt(args[0]);
		int ISIDuration = Integer.parseInt(args[1]);
		int stimulesDuration = Integer.parseInt(args[2]);
		int frequency = Integer.parseInt(args[3]);

		epochSize = (frequency * epochDuration) / 1000;
		int stimulesSize = (frequency * stimulesDuration) / 1000;
		int ISISize = (frequency * ISIDuration) / 1000;
		int SOAJump = stimulesSize + ISISize;

		letterCatalog = new LettersCatalog();

		ArrayList<File> ls = new ArrayList<>();
		ls.add(new File("C:\\Users\\Eu\\Desktop\\Tese\\ALS\\dataSets"));
		ls.add(new File("C:\\Users\\Eu\\Desktop\\Tese\\ERP-based_BCI\\DataSets_CSV_S"));
		ls.add(new File("C:\\Users\\Eu\\Desktop\\Tese\\ERP-based_BCI\\DataSets_CSV_G"));
		NewRecognizer mcali = giveRec(ls);
		System.out.println("treinado");
		PrintWriter pw = new PrintWriter("fichei.txt");
		 clearListOfBox();
		for (File file : new File(args[5]).listFiles() ) {
			NewWriter nWriter = new NewWriter("Teste"+file.getName());
			sb.append("ficheiro "+ file.getName() + "\n");
			listOfTargetsNTargets = new ArrayList<>();
			listOfRowAndCollum = new ArrayList<>();

			ArrayList<Double> valores = getMediaElect(file,0);
			boolean posFound = false;
			int position = 0;
			for (int i = 0; i < listOfRowAndCollum.size() && ! posFound; i++){
				if (listOfRowAndCollum.get(i) != 0) {
					position = i;
					posFound = true;
				}
			}
			while (position < listOfRowAndCollum.size()) {
				if (listOfRowAndCollum.get(position ) != 0) {
					if (ListOfSignalsInBoxs.get(listOfRowAndCollum.get(position)) == 0){ // primeiro a entrar
						ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
						for (int b = 0; b < epochSize; b++){
							ntargetMediaTemp.add(valores.get(b+position));

						}
						letterCatalog.addSignal(ntargetMediaTemp, true, listOfRowAndCollum.get(position), epochSize);
						ListOfSignalsInBoxs.put(listOfRowAndCollum.get(position), ListOfSignalsInBoxs.get(listOfRowAndCollum.get(position)) + 1); //incrementa quantos ja fez
						ListOfTargetAndNTarget.put(listOfRowAndCollum.get(position), listOfTargetsNTargets.get(position)); //saber se é target ou nao

					}else{ // quando é mais do que 1
						ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
						for (int b = 0; b < epochSize; b++){
							ntargetMediaTemp.add(valores.get(b+position));
						}
						letterCatalog.addSignal(ntargetMediaTemp, false, listOfRowAndCollum.get(position), epochSize);
						ListOfSignalsInBoxs.put(listOfRowAndCollum.get(position), ListOfSignalsInBoxs.get(listOfRowAndCollum.get(position)) + 1);
					}
					int readyOrNot = checkIfBoxsAreReady();
					
					if (readyOrNot >= 5 && readyOrNot <= 10){
						int cl = 0;
						for (String val : listOfBoxsToTest.keySet()) {
							NewGesture g = new NewGesture();
							
							for (int b = 0; b < epochSize; b++){
								Point p = new Point((int) (b*2),(int) (listOfBoxsToTest.get(val).get(b).doubleValue()*multipler));
								g.addPoint(p);
							}
							g.finalizeStroke();
							
							
							nWriter.addGesture(g);
							String result = mcali.classify(g);
							listOfBoxResults.put(val, result);
						}
						sb.append("Check\n");

						checkResults();
						if (readyOrNot == 10) {
							
							clearListOfBox();
							int r = 0;
							int c = 0;
							for (int test = 1; test < 13; test++) {
								if (ListOfTargetAndNTarget.get(test) == 2 && r == 0) {
									r = test;
								}else if (ListOfTargetAndNTarget.get(test) == 2 && r != 0 && c == 0) {
									c = test;
								}
							}
							sb.append("Correcto\n");
							sb.append("letra Correcta "+ letterCatalog.getLetter(c, r) + "\n");
							sb.append("---------------------NEXT--------------------\n");
							//mcali.trainClassifier();



						}
					}
					position = position + SOAJump;
				}else {
					position++;
				}

			}
			nWriter.close();

		}
		pw.print(sb.toString());
		pw.flush();
		pw.close();
		/*

		 */
	}

	public static void clearListOfBox() {
		for (int i = 1; i < 13; i++) {
			ListOfSignalsInBoxs.put(i, 0);
		}
		for (int i = 7; i < 13; i++) {
			for (int a = 1; a < 7; a++) {
				listOfBoxsToTest.put(String.valueOf(i)+":"+String.valueOf(a) , new ArrayList<Double>());
				listOfBoxResults.put(String.valueOf(i)+":"+String.valueOf(a), "None");
			}
		}
	}

	public static double getMaxAbsolute(ArrayList<Double> list) {
		List<Double> x = new ArrayList<Double>(list);
		for( int i = 0; i < x.size(); i++ ){
			x.set( i, Math.abs(x.get(i)) );
		}
		return Collections.max( x );
	}

	public static ArrayList<Double> getMediaElect(File file, int co) throws IOException{
		ArrayList<Double> valores = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while( (line = br.readLine())!= null){
			String[] linha = line.split(",");
			double total = Double.parseDouble(linha[0]) + Double.parseDouble(linha[1]) +
					Double.parseDouble(linha[2]) + Double.parseDouble(linha[3]) +
					Double.parseDouble(linha[4]) + Double.parseDouble(linha[5]) +
					Double.parseDouble(linha[6]) + Double.parseDouble(linha[7]);
			valores.add(total/8.0);
			listOfTargetsNTargets.add(Integer.parseInt(linha[8]));
			if (co == 0) {
				listOfRowAndCollum.add(Integer.parseInt(linha[9]));
			}
		}	
		return valores;

	}
	public static ArrayList<Double> getMediaElectERP(File file, int co) throws IOException{
		ArrayList<Double> valores = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while( (line = br.readLine())!= null){
			String[] linha = line.split(",");
			double total = Double.parseDouble(linha[0]) + Double.parseDouble(linha[2]) +
					Double.parseDouble(linha[4]) + Double.parseDouble(linha[5]) +
					Double.parseDouble(linha[12]) + Double.parseDouble(linha[13]) +
					Double.parseDouble(linha[14]) + Double.parseDouble(linha[15]);
			valores.add(total/8.0);
			listOfTargetsNTargets.add(Integer.parseInt(linha[16]));
			if (co == 0) {
				listOfRowAndCollum.add(Integer.parseInt(linha[17]));
			}
		}	
		return valores;
	}

	public static int checkIfBoxsAreReady() {
		int current = ListOfSignalsInBoxs.get(1); //primeiro da lista
		for (Integer integer : ListOfSignalsInBoxs.values()) {
			if (integer < 5) {
				return -1;
			}
			if (current != integer) {
				return -1;
			}
		}
		//System.out.println(current);
		for (int i = 7; i < 13; i++) {
			for (int a = 1; a < 7; a++) {
				ArrayList<Double> m = letterCatalog.getArray(i, a);
				ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
				for ( int b = 0; b < epochSize; b++){
					ntargetMediaTemp.add(m.get(b)/(current*2));
				}
				double maxAbsolute = getMaxAbsolute(ntargetMediaTemp);
				ArrayList<Double> ntargetMediaTempMax = new ArrayList<>();
				for ( int b = 0; b < epochSize; b++){
					ntargetMediaTempMax.add(ntargetMediaTemp.get(b)/maxAbsolute);
				}
				listOfBoxsToTest.put(String.valueOf(i)+":"+String.valueOf(a), ntargetMediaTempMax);
			}
		}

		return current;
	}

	public static int checkResults() {
		int possible = 0;
		//System.out.println(listOfBoxResults.get(5) + " -- " + listOfBoxResults.get(12) );
		for (int b = 7; b < 13; b++) {
			for(int i = 1; i < 7; i++) {
				//System.out.println(listOfBoxResults.get(i).equals("Target") && listOfBoxResults.get(b).equals("Target"));
				if (listOfBoxResults.get(String.valueOf(b)+":"+String.valueOf(i)).equals("Target")) {
					possible++;
					sb.append("letra possivel "+String.valueOf(b)+":"+String.valueOf(i) + " --- "+ letterCatalog.getLetter(b, i) + "\n");
				}
			}
			//System.out.println(coisa + "\n");
		}
		return possible;
	}


	public static NewRecognizer giveRec(ArrayList<File> list) throws Exception {
		List<String> s = new ArrayList<String>();
		s.add("Target");
		s.add("NTarget");
		NewRecognizer mcali = new NewRecognizer("SVMRBF1", s);
		NewWriter w = new NewWriter("TesteFInal");
		for (int x = 0; x < 3; x++) {
			int totalToread = 9;

			if (x == 0) {
				totalToread = totalToread * 3;
			}
			for (File ficheiroLeitura : list.get(x).listFiles()) {
				listOfTargetsNTargets = new ArrayList<>();
				ArrayList<Double> valores;
				if (x == 0) {
					valores = getMediaElect(ficheiroLeitura,1);

				}else{
					valores = getMediaElectERP(ficheiroLeitura,1);
				}
				ArrayList<Double> targetMedia = new ArrayList<>();
				ArrayList<Double> ntargetMedia = new ArrayList<>();

				int ntargetDiv = 0;
				int targetDiv = 0;
				int totalTar = 0;
				int totalNTar = 0;

				for (int i = 0; i < listOfTargetsNTargets.size(); i++){

					if (listOfTargetsNTargets.get(i) == 1 && totalNTar < totalToread ){

						if (ntargetDiv < 10){
							if (ntargetDiv == 0){ // primeiro a entrar
								ntargetMedia = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									ntargetMedia.add(valores.get(b+i));

								}
								ntargetDiv++;
							}else{ // quando é mais do que 1
								ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									ntargetMediaTemp.add(valores.get(b+i) + ntargetMedia.get(b));

								}
								ntargetMedia = ntargetMediaTemp;
								ntargetDiv++;
							}
						}
						if (ntargetDiv == 10 ){ //quando o valor e chegado
							NewGesture g = new NewGesture("NTarget");

							ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
							for ( int b = 0; b < 256; b++){

								ntargetMediaTemp.add(ntargetMedia.get(b)/10);

							}
							ntargetMedia = ntargetMediaTemp;

							double maxAbsolute = getMaxAbsolute(ntargetMedia);
							ntargetMediaTemp = new ArrayList<>();
							for ( int b = 0; b < 256; b++){

								ntargetMediaTemp.add(ntargetMedia.get(b)/maxAbsolute);

							}
							ntargetMedia = ntargetMediaTemp;
							for (int b = 0; b < 256; b++){
								Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
								g.addPoint(p);
							}
							try{
								g.finalizeStroke();
								g.calcFeatures();
								w.addGesture(g);
								mcali.addExample(g);
								totalNTar++;
							}catch (Exception e) {
								// TODO: handle exception
							}
							ntargetDiv = 0;
							ntargetMedia = new ArrayList<>();

						}
						i= i+32;

					}else if (listOfTargetsNTargets.get(i) == 2 && totalTar < totalToread){
						if (targetDiv < 10){
							if (targetDiv == 0){ // primeiro a entrar

								targetMedia = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									targetMedia.add(valores.get(b+i));

								}
								targetDiv++;
							}else{ // quando é mais do que 1
								ArrayList<Double> targetMediaTemp = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									targetMediaTemp.add(valores.get(b+i) + targetMedia.get(b));

								}
								targetMedia = targetMediaTemp;
								targetDiv++;
							}
						}
						if (targetDiv == 10){ //quando o valor e chegado

							NewGesture g = new NewGesture("Target");
							ArrayList<Double> targetMediaTemp = new ArrayList<>();
							for ( int b = 0; b < 256; b++){

								targetMediaTemp.add(targetMedia.get(b)/10);

							}
							targetMedia = targetMediaTemp;

							double maxAbsolute = getMaxAbsolute(targetMedia);
							targetMediaTemp = new ArrayList<>();
							for ( int b = 0; b < 256; b++){

								targetMediaTemp.add(targetMedia.get(b)/maxAbsolute);

							}
							targetMedia = targetMediaTemp;
							for (int b = 0; b < 256; b++){
								Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
								g.addPoint(p);
							}

							g.finalizeStroke();
							g.calcFeatures();
							w.addGesture(g);
							mcali.addExample(g);
							totalTar++;


							targetDiv = 0;
							targetMedia = new ArrayList<>();
						}
						i= i+32;

					}
				}
				System.out.println("um user");

			}
		}
		w.close();
		mcali.trainClassifier();
		return mcali;
	}

}

