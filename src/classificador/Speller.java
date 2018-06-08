package classificador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import mcali.Point;

public class Speller {

	private static HashMap<Integer, ArrayList<Double>> listOfBoxs = new HashMap<>();	
	private static HashMap<Integer, ArrayList<Double>> listOfBoxsToTest = new HashMap<>();

	private static HashMap<Integer, Integer> ListOfSignalsInBoxs = new HashMap<Integer, Integer>();
	private static HashMap<Integer, String> listOfBoxResults = new HashMap<>();
	private static LettersCatalog letterCatalog;
	private static ArrayList<Integer> listOfRowAndCollum = new ArrayList<Integer>();
	private static int multipler = 150;
	private static int epochSize;


	public static void main(String[] args) throws Exception {

		int epochDuration = Integer.parseInt(args[0]);
		int ISIDuration = Integer.parseInt(args[1]);
		int stimulesDuration = Integer.parseInt(args[2]);
		int frequency = Integer.parseInt(args[3]);


		epochSize = (frequency * epochDuration) / 1000;
		int stimulesSize = (frequency * stimulesDuration) / 1000;
		int ISISize = (frequency * ISIDuration) / 1000;
		int SOAJump = stimulesSize + ISISize;


		clearListOfBox();
		letterCatalog = new LettersCatalog();

		NewRecognizer mcali = new NewRecognizer("SVMRBF1", new File(args[4]));

		//listOfTargetsNTargets = new ArrayList<>();
		listOfRowAndCollum = new ArrayList<>();

		ArrayList<Double> valores = getMediaElect(new File(args[5]));
		boolean posFound = false;
		int position = 0;
		for (int i = 0; i < listOfRowAndCollum.size() && ! posFound; i++){
			if (listOfRowAndCollum.get(i) != 0) {
				position = i;
				posFound = true;
			}
		}

		while (posFound) {
			//System.out.println(position);
			if (listOfRowAndCollum.get(position) != 0) {
				if (ListOfSignalsInBoxs.get(listOfRowAndCollum.get(position)) == 0){ // primeiro a entrar
					ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
					for (int b = 0; b < epochSize; b++){
						ntargetMediaTemp.add(valores.get(b+position));

					}
					listOfBoxs.put(listOfRowAndCollum.get(position),ntargetMediaTemp);
					ListOfSignalsInBoxs.put(listOfRowAndCollum.get(position), ListOfSignalsInBoxs.get(listOfRowAndCollum.get(position)) + 1);

				}else{ // quando é mais do que 1
					ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
					for (int b = 0; b < epochSize; b++){
						ntargetMediaTemp.add(valores.get(b+position) + listOfBoxs.get(listOfRowAndCollum.get(position)).get(b));

					}
					listOfBoxs.put(listOfRowAndCollum.get(position), ntargetMediaTemp);
					ListOfSignalsInBoxs.put(listOfRowAndCollum.get(position), ListOfSignalsInBoxs.get(listOfRowAndCollum.get(position)) + 1);
				}
				if (checkIfBoxsAreReady()){
					for (Integer val : listOfBoxsToTest.keySet()) {
						NewGesture g = new NewGesture();
						for (int b = 0; b < epochSize; b++){
							Point p = new Point((int) (b*2),(int) (listOfBoxsToTest.get(val).get(b).doubleValue()*multipler));
							g.addPoint(p);
						}
						g.finalizeStroke();
						String result = mcali.classify(g);
						listOfBoxResults.put(val, result);
					}
					System.out.println("Check");
					checkResults();
				}
				position = position + SOAJump;

			}else {
				posFound = false;
			}

		}
	}

	public static void clearListOfBox() {
		for (int i = 1; i < 13; i++) {
			listOfBoxs.put(i, new ArrayList<Double>());
			listOfBoxsToTest.put(i, new ArrayList<Double>());
			listOfBoxResults.put(i, "None");
			ListOfSignalsInBoxs.put(i, 0);
		}
	}

	public static double getMaxAbsolute(ArrayList<Double> list) {
		List<Double> x = new ArrayList<Double>(list);
		for( int i = 0; i < x.size(); i++ ){
			x.set( i, Math.abs(x.get(i)) );
		}
		return Collections.max( x );
	}

	public static ArrayList<Double> getMediaElect(File file) throws IOException{
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
			//listOfTargetsNTargets.add(Integer.parseInt(linha[8]));
			listOfRowAndCollum.add(Integer.parseInt(linha[8]));
		}	
		return valores;

	}

	public static boolean checkIfBoxsAreReady() {
		int current = ListOfSignalsInBoxs.get(1);
		for (Integer integer : ListOfSignalsInBoxs.values()) {
			if (integer < 5) {
				return false;
			}
			if (current != integer) {
				return false;
			}
		}

		for (Integer val : listOfBoxs.keySet()) {
			ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
			for ( int b = 0; b < epochSize; b++){
				ntargetMediaTemp.add(listOfBoxs.get(val).get(b)/current);
			}
			double maxAbsolute = getMaxAbsolute(ntargetMediaTemp);
			ArrayList<Double> ntargetMediaTempMax = new ArrayList<>();
			for ( int b = 0; b < epochSize; b++){
				ntargetMediaTempMax.add(ntargetMediaTemp.get(b)/maxAbsolute);
			}
			listOfBoxsToTest.put(val, ntargetMediaTempMax);
		}
		return true;
	}

	public static int checkResults() {
		int possible = 0;
		for (int b = 7; b < 13; b++) {
			String coisa = listOfBoxResults.get(b);
			for(int i = 1; i < 7; i++) {
				coisa+= " "+ listOfBoxResults.get(i);
				//System.out.println(listOfBoxResults.get(i).equals("Target") && listOfBoxResults.get(b).equals("Target"));
				if (listOfBoxResults.get(b).equals("Target") && listOfBoxResults.get(i).equals("Target")) {
					possible++;
					System.out.println("letra possivel "+ letterCatalog.getLetter(b, i));
				}
			}
			//System.out.println(coisa + "\n");
		}
		return possible;
	}

}

