package classificador;

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

import mcali.Gesture;
import mcali.Point;
import mcali.Recognizer;
import mcali.Writer;

public class RunnerForERPUserIndependent {
	private static ArrayList<Integer> listOfTargetsNTargets = new ArrayList<>();

	private static int avgTarget = 5;
	private static int avgNTarget = 5;
	private static int multipler = 150;
	private final static int fromTrial = 35;
	private final static int mc = 50;

	private static String[] classifs = {"SVMNor14"};



	public static void main(String[] args) throws Exception {


		avgTarget = avgNTarget = 5;
		while (avgTarget <= 10){
			int totalToread = 0;
			if ( (96/avgTarget) % 1 == 0){
				totalToread = 96/avgTarget;
			}else {
				totalToread = (96/avgTarget) -1;
			}
			PrintWriter pw;
			PrintWriter pw1;

			pw = new PrintWriter(new File("FinalTests\\TestSampleERP\\UserIndependentNormalizedERPSample_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
			pw1 = new PrintWriter(new File("FinalTests\\Media\\UserIndependentERPNormalizedMediaRandomForest_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
			PrintWriter pw2 = new PrintWriter(new File("FinalTests\\UserIndependentPPERP\\UserIndependentERPNormalizedPP_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));

			StringBuilder sb1 = new StringBuilder();
			sb1.append("UserTested,Target Percent,NTarget Percent\n");
			StringBuilder sb2 = new StringBuilder();
			sb2.append("UserTested,Target Percent,NTarget Percent\n");
			StringBuilder sb = new StringBuilder();
			sb.append("UserTested,Target Percent,NTarget Percent\n");
			File[] ficheiros = new File(args[0]).listFiles();

			NewWriter w;
			NewWriter w2;
			NewRecognizer mcali;
			List<String> s = new ArrayList<String>();
			s.add("Target");
			s.add("NTarget");

			for (int userID = 1; userID < 31; userID = userID+3) {
				int currentUser = userID;
				mcali = new NewRecognizer("SVMRBF1", s);

				ArrayList<Double> listOFNTargetsPP = new ArrayList<>();
				ArrayList<Double> listOFTargetsPP = new ArrayList<>();
				w = new NewWriter("FinalTests\\TestSampleERP\\UserIndependentNormalizedERPSample_"+userID+"_T_"+avgTarget+"_NT_"+avgNTarget);
				w2 = new NewWriter("FinalTests\\Media\\UserIndependentERPNormalizedMediaRandomForest"+userID+"_T_"+avgTarget+"_NT_"+avgNTarget,1);
				for (int userTest = 1; userTest < 31; userTest = userTest+3){
					//System.out.println(((userTest/3)+1));
					//if (userTest/3 < userID-1 || userTest/3 >= userID) {
					if (userTest != userID) {
						//System.out.println("inside");
						for (int userFile = 0; userFile < 3; userFile++) {
							listOfTargetsNTargets = new ArrayList<>();
							ArrayList<Double> valores = getMediaElect(ficheiros[userTest+userFile-1]);
							System.out.println(ficheiros[userTest+userFile-1].getName());
							ArrayList<Double> targetMedia = new ArrayList<>();
							ArrayList<Double> ntargetMedia = new ArrayList<>();

							int ntargetDiv = 0;
							int targetDiv = 0;
							int totalTar = 0;
							int totalNTar = 0;
							for (int i = 0; i < listOfTargetsNTargets.size(); i++){

								if (listOfTargetsNTargets.get(i) == 1 && totalNTar < totalToread){
									/*
									if (ntargetDiv < avgNTarget){
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
									if (ntargetDiv == avgNTarget ){ //quando o valor e chegado
										NewGesture g = new NewGesture("NTarget");

										ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											ntargetMediaTemp.add(ntargetMedia.get(b)/avgNTarget);

										}
										ntargetMedia = ntargetMediaTemp;

										double maxAbsolute = getMaxAbsolute(ntargetMedia);
										ntargetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											ntargetMediaTemp.add(ntargetMedia.get(b)/maxAbsolute);

										}
										ntargetMedia = ntargetMediaTemp;
										Funcoes f = new Funcoes();
										w2.addAverages("NTarget", f.getVector(ntargetMedia));

										for (int b = 0; b < 256; b++){
											Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
											g.addPoint(p);	
										}
										g.finalizeStroke();
										try{
											g.calcFeatures();
									//		mcali.addExample(g);
											w.addGesture(g);
											totalNTar++;
										}catch(Exception e){

										}
										ntargetDiv = 0;
										ntargetMedia = new ArrayList<>();

									}
									i= i+32;
									*/
								}else if (listOfTargetsNTargets.get(i) == 2 && totalTar < totalToread){
									if (targetDiv < avgTarget){
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
									if (targetDiv == avgTarget){ //quando o valor e chegado

										NewGesture g = new NewGesture("Target");
										ArrayList<Double> targetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											targetMediaTemp.add(targetMedia.get(b)/avgTarget);

										}
										targetMedia = targetMediaTemp;
										double lower = new Funcoes().getLowe(targetMedia);
										double higher = new Funcoes().getHight(targetMedia);
										listOFTargetsPP.add(higher - lower);
										/*
										double maxAbsolute = getMaxAbsolute(targetMedia);
										targetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											targetMediaTemp.add(targetMedia.get(b)/maxAbsolute);

										}
										targetMedia = targetMediaTemp;
										Funcoes f = new Funcoes();
										w2.addAverages("Target", f.getVector(targetMedia));
										
										
										
										for (int b = 0; b < 256; b++){
											Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
											g.addPoint(p);
										}
										try{
											g.finalizeStroke();
											g.calcFeatures();
										//	mcali.addExample(g);
										//	w.addGesture(g);
										}catch (Exception e){

										}*/
											totalTar++;

										targetDiv = 0;
										targetMedia = new ArrayList<>();
										

									}
									i= i+32;

								}
							}
						}
					}
				}
				w.close();
				String path = w2.getARFFPath();
				w2.closeArff();

			//	mcali.trainClassifier();
			//	NewRecognizer mcaliMedia = new NewRecognizer("Random", new File(path));
				double[] targetl = new double[listOFTargetsPP.size()];
				for (int i = 0; i < targetl.length; i++) {
					targetl[i] = listOFTargetsPP.get(i).doubleValue();
				}
				Arrays.sort(targetl);

				int pqTarget = (targetl.length+1)/4;

				double target = targetl[pqTarget];

				HashMap<String, Integer> targetResults = new HashMap<>();
				targetResults.put("Target", 0);
				targetResults.put("NTarget", 0);
				HashMap<String, Integer> ntargetResults = new HashMap<>();
				ntargetResults.put("Target", 0);
				ntargetResults.put("NTarget", 0);

				HashMap<String, Integer> targetResultsMedia = new HashMap<>();
				targetResultsMedia.put("Target", 0);
				targetResultsMedia.put("NTarget", 0);
				HashMap<String, Integer> ntargetResultsMedia = new HashMap<>();
				ntargetResultsMedia.put("Target", 0);
				ntargetResultsMedia.put("NTarget", 0);
				
				HashMap<String, Integer> targetResultsPP = new HashMap<>();
				targetResultsPP.put("Target", 0);
				targetResultsPP.put("NTarget", 0);
				HashMap<String, Integer> ntargetResultsPP = new HashMap<>();
				ntargetResultsPP.put("Target", 0);
				ntargetResultsPP.put("NTarget", 0);

				String nameFile = null;

				for (int userTest = 1; userTest < 31; userTest = userTest +3){
					if (((userTest/3)+1) == ((userID/3)+1)) {
						for (int userFile = 0; userFile < 3; userFile++) {
							listOfTargetsNTargets = new ArrayList<>();
							ArrayList<Double> valores = getMediaElect(new File(args[0]).listFiles()[userTest+userFile-1]);
							nameFile = new File(args[0]).listFiles()[userTest+userFile-1].getName();
							System.out.println(nameFile);
							ArrayList<Double> targetMedia = new ArrayList<>();
							ArrayList<Double> ntargetMedia = new ArrayList<>();

							int ntargetDiv = 0;
							int targetDiv = 0;

							int totalTar = 0;
							int totalNTar = 0;
							for (int i = 0; i < listOfTargetsNTargets.size(); i++){

								if (listOfTargetsNTargets.get(i) == 1 && totalNTar < totalToread ){
									if (ntargetDiv < avgNTarget){
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
									if (ntargetDiv == avgNTarget){
										NewGesture g = new NewGesture();
										ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											ntargetMediaTemp.add(ntargetMedia.get(b)/avgNTarget);

										}
										ntargetMedia = ntargetMediaTemp;
										double lower = new Funcoes().getLowe(ntargetMedia);
										double higher = new Funcoes().getHight(ntargetMedia);

										if (higher - lower < target) {
											ntargetResultsPP.put("NTarget", ntargetResultsPP.get("NTarget") + 1);
										}else{
											ntargetResultsPP.put("Target", ntargetResultsPP.get("Target") + 1);
										}
										double maxAbsolute = getMaxAbsolute(ntargetMedia);
										ntargetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											ntargetMediaTemp.add(ntargetMedia.get(b)/maxAbsolute);

										}
										ntargetMedia = ntargetMediaTemp;
										Funcoes f = new Funcoes();
								//		String resultMedia = mcaliMedia.classify(f.getVector(ntargetMedia));
								//		ntargetResultsMedia.put(resultMedia, ntargetResultsMedia.get(resultMedia)+1);
										
										for (int b = 0; b < 256; b++){
											Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
											g.addPoint(p);
										}

										try{
											g.finalizeStroke();
										//	String result = mcali.classify(g);
										//	ntargetResults.put(result, ntargetResults.get(result) +1 );
											totalNTar++;
										}catch(Exception e){

										}

										ntargetDiv = 0;
										ntargetMedia = new ArrayList<>();

									}
									i= i+32;

								}else if (listOfTargetsNTargets.get(i) == 2  && totalTar < totalToread){
									if (targetDiv < avgTarget){
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
									if (targetDiv == avgTarget){ //quando o valor e chegado

										NewGesture g = new NewGesture();
										ArrayList<Double> targetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											targetMediaTemp.add(targetMedia.get(b)/avgTarget);

										}
										targetMedia = targetMediaTemp;
										double lower = new Funcoes().getLowe(targetMedia);
										double higher = new Funcoes().getHight(targetMedia);

										if (higher - lower < target) {
											targetResultsPP.put("NTarget", targetResultsPP.get("NTarget") + 1);
										}else{
											targetResultsPP.put("Target", targetResultsPP.get("Target") + 1);
										}
										double maxAbsolute = getMaxAbsolute(targetMedia);
										targetMediaTemp = new ArrayList<>();
										for ( int b = 0; b < 256; b++){

											targetMediaTemp.add(targetMedia.get(b)/maxAbsolute);

										}
										targetMedia = targetMediaTemp;
										Funcoes f = new Funcoes();
									//	String resultMedia = mcaliMedia.classify(f.getVector(targetMedia));
									//	targetResultsMedia.put(resultMedia, targetResultsMedia.get(resultMedia)+1);
										
										for (int b = 0; b < 256; b++){
											Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
											g.addPoint(p);
										}
										try{
											g.finalizeStroke();
									//		String result = mcali.classify(g);
									//		targetResults.put(result, targetResults.get(result) +1 );
											totalTar++;
										}catch(Exception e){

										}
										targetDiv = 0;
										targetMedia = new ArrayList<>();


									}
									i= i+32;

								}
							}
						}
					}
				}

				double tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
				double ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
				int targetPercentage = (int) (tCacl * 100);
				int ntargetPercentage = (int)(ntCacl * 100);
				double tCaclMedia = (double)targetResultsMedia.get("Target")/ (double)(targetResultsMedia.get("Target") + targetResultsMedia.get("NTarget"));
				double ntCaclMedia = (double)ntargetResultsMedia.get("NTarget")/(double)(ntargetResultsMedia.get("Target") + ntargetResultsMedia.get("NTarget"));
				int targetPercentageMedia = (int) (tCaclMedia * 100);
				int ntargetPercentageMedia = (int)(ntCaclMedia * 100);
				//System.out.println( targetPercentage + "----" + ntargetPercentage);
				sb.append("User "+nameFile+","+targetPercentage+","+ntargetPercentage+"\n");
				sb1.append("User "+nameFile+","+targetPercentageMedia+","+ntargetPercentageMedia+"\n");
				//System.out.println("Foi unm");
				//System.out.println(totalTar);
				//System.out.println(totalNTar);
				
				tCacl = (double)targetResultsPP.get("Target")/ (double)(targetResultsPP.get("Target") + targetResultsPP.get("NTarget"));
				ntCacl = (double)ntargetResultsPP.get("NTarget")/(double)(ntargetResultsPP.get("Target") + ntargetResultsPP.get("NTarget"));
				targetPercentage = (int) (tCacl * 100);
				ntargetPercentage = (int)(ntCacl * 100);
				//System.out.println( targetPercentage + "----" + ntargetPercentage);
				sb2.append("User "+nameFile+","+targetPercentage+","+ntargetPercentage+"\n");

			}

			pw.write(sb.toString());
			pw.flush();
			pw.close();
			pw1.write(sb1.toString());
			pw1.flush();
			pw1.close();
			pw2.write(sb2.toString());
			pw2.flush();
			pw2.close();
			avgTarget++;
			avgNTarget = avgTarget;
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
			double total = Double.parseDouble(linha[0]) + Double.parseDouble(linha[2]) +
					Double.parseDouble(linha[4]) + Double.parseDouble(linha[5]) +
					Double.parseDouble(linha[12]) + Double.parseDouble(linha[13]) +
					Double.parseDouble(linha[14]) + Double.parseDouble(linha[15]);
			valores.add(total/8.0);
			listOfTargetsNTargets.add(Integer.parseInt(linha[16]));
		}	
		return valores;
	}
}
