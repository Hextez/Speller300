package TrainmCali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import classificador.NewGesture;
import classificador.NewWriter;
import mcali.Gesture;
import mcali.Point;
import mcali.Recognizer;
import mcali.Writer;

public class OfflineTest7User1User {

	private static ArrayList<Integer> listOfTargetsNTargets = new ArrayList<>();

	private static int avgTarget = 9 ;
	private static int avgNTarget = 9;
	private final static int multipler = 20;
	private final static int fromTrial = 15;
	private final static int mc = 50;

	private final static ArrayList<Integer> listOfTrials = new ArrayList<>(Arrays.asList(1801, 11529, 21257, 30985, 40713,
			51473, 61201, 70929, 80657, 90385, 101145, 110873, 120601, 130329, 140057, 150817, 160545, 170273,
			180001, 189729, 200489, 210217, 219945, 229673, 239401, 250161, 259889, 269617, 279345, 289073, 299833,
			309561, 319289, 329017, 338745,347704));


	public static void main(String[] args) throws IOException {

		while (avgTarget <= 10){
			Writer w = new Writer("OfflineTest\\AllUsers\\mCaliTested_AllUsersFeatureALLFeatures_T_"+avgTarget+"_NT_"+avgNTarget);
			int totalToread = 0;
			if ( (700/avgTarget) % 1 == 0){
				totalToread = 700/avgTarget;
				System.out.println(totalToread);
			}else {
				totalToread = (700/avgTarget) -1;
				System.out.println(totalToread);
			}
			for (File ficheiroLeitura : new File(args[0]).listFiles()) {
				listOfTargetsNTargets = new ArrayList<>();
				ArrayList<Double> valores = getMediaElect(ficheiroLeitura);
				ArrayList<Double> targetMedia = new ArrayList<>();
				ArrayList<Double> ntargetMedia = new ArrayList<>();

				int ntargetDiv = 0;
				int targetDiv = 0;
				int totalTar = 0;
				int totalNTar = 0;
				for (int i = listOfTrials.get(0)-1; i < 347704-1; i++){

					if (listOfTargetsNTargets.get(i) == 1 && totalNTar < totalToread ){

						if (ntargetDiv < avgNTarget){
							if (ntargetDiv == 0){ // primeiro a entrar
								ntargetMedia = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									ntargetMedia.add(valores.get(b+i));

								}
								ntargetDiv++;
							}else{ // quando � mais do que 1
								ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									ntargetMediaTemp.add(valores.get(b+i) + ntargetMedia.get(b));

								}
								ntargetMedia = ntargetMediaTemp;
								ntargetDiv++;
							}
						}
						if (ntargetDiv == avgNTarget ){ //quando o valor e chegado
							Gesture g = new Gesture("NTarget");

							ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
							for ( int b = 0; b < 256; b++){

								ntargetMediaTemp.add(ntargetMedia.get(b)/avgNTarget);

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
							totalNTar++;
							}catch (Exception e) {
								// TODO: handle exception
							}
							ntargetDiv = 0;
							ntargetMedia = new ArrayList<>();

						}
						i= i+32;

					}else if (listOfTargetsNTargets.get(i) == 2 && totalTar < totalToread){
						if (targetDiv < avgTarget){
							if (targetDiv == 0){ // primeiro a entrar

								targetMedia = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									targetMedia.add(valores.get(b+i));

								}
								targetDiv++;
							}else{ // quando � mais do que 1
								ArrayList<Double> targetMediaTemp = new ArrayList<>();
								for (int b = 0; b < 256; b++){
									targetMediaTemp.add(valores.get(b+i) + targetMedia.get(b));

								}
								targetMedia = targetMediaTemp;
								targetDiv++;
							}
						}
						if (targetDiv == avgTarget){ //quando o valor e chegado

							Gesture g = new Gesture("Target");
							ArrayList<Double> targetMediaTemp = new ArrayList<>();
							for ( int b = 0; b < 256; b++){

								targetMediaTemp.add(targetMedia.get(b)/avgTarget);

							}
							targetMedia = targetMediaTemp;


							for (int b = 0; b < 256; b++){
								Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
								g.addPoint(p);
							}

							g.finalizeStroke();
							g.calcFeatures();
							w.addGesture(g);
							totalTar++;


							targetDiv = 0;
							targetMedia = new ArrayList<>();
						}
						i= i+32;

					}
				}
			}
			w.close();

			avgTarget++;
			avgNTarget = avgTarget;
		}


	}

	public static double normalise(double double1, Double double3, Double double2) {
		return (double1 - double3)/(double2 - double3);
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
			listOfTargetsNTargets.add(Integer.parseInt(linha[8]));
		}	
		return valores;

	}
}
