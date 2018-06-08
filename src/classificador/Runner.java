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

public class Runner {

	private static ArrayList<Integer> listOfTargetsNTargets = new ArrayList<>();

	private static int avgTarget = 5;
	private static int avgNTarget = 5;
	private static int multipler = 150;
	private final static int fromTrial = 35;
	private final static int mc = 50;
	private static int numFeatures = 11;

	private final static ArrayList<Integer> listOfTrials = new ArrayList<>(Arrays.asList(1801, 11529, 21257, 30985, 40713,
			51473, 61201, 70929, 80657, 90385, 101145, 110873, 120601, 130329, 140057, 150817, 160545, 170273,
			180001, 189729, 200489, 210217, 219945, 229673, 239401, 250161, 259889, 269617, 279345, 289073, 299833,
			309561, 319289, 329017, 338745,347704));

	private static String[] classifs = {"SVMNor14"};

	public static void main(String[] args) throws Exception {
		multipler = Integer.parseInt(args[2]);
		for (int classif = 0; classif < 1; classif++){
			int d = 0;
			avgTarget = avgNTarget = 5;
			while (avgTarget <= 10){
				PrintWriter pw;
				//if (classif == 0 && Integer.parseInt(args[1]) == 1) {}
				/*if (classif == 0) {
					pw = new PrintWriter(new File("FinalTests\\CSV\\UserIndependentNormalized24FeaturesSMOT_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				}else if (classif == 1) {
					pw = new PrintWriter(new File("FinalTests\\CSV\\UserIndependentNormalized24FeaturesVote4_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				}else if (classif == 2) {
					pw = new PrintWriter(new File("FinalTests\\CSV\\UserIndependentNormalized24FeaturesVote5_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				}else if (classif == 3) {
					pw = new PrintWriter(new File("FinalTests\\CSV\\UserIndependentNormalized24FeaturesVote6_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				}else{
					pw = new PrintWriter(new File("FinalTests\\CSV\\UserIndependentNormalized24FeaturesVote7_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				}*/
				pw = new PrintWriter(new File("FinalTests\\CSV\\UserIndependentNormalized24Features"+classifs[classif]+"_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));

				
				StringBuilder sb = new StringBuilder();
				sb.append("UserTested,Target Percent,NTarget Percent\n");
				
				for (int userTest = 1; userTest < 49; userTest = userTest + 6){
					NewWriter w;
					//NewWriter w2;
					NewRecognizer mcali;
					List<String> s = new ArrayList<String>();
					s.add("Target");
					s.add("NTarget");
					mcali = new NewRecognizer(classifs[classif], new File(args[3]).listFiles()[userTest-1+d]);
					System.out.println(new File(args[3]).listFiles()[userTest-1+d].getName());
					//if (classif == 0 && Integer.parseInt(args[1]) == 1) {}
					/*
					if (classif == 0) {
						w = new NewWriter("FinalTests\\UserIndependentNormalized24FeaturesSMOT\\UserIndependentNormalizedERP24FeaturesSMOT_"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
						mcali = new NewRecognizer("SVM", s);
					}else if (classif == 1) {
						w = new NewWriter("FinalTests\\UserIndependentNormalized24FeaturesVote4\\UserIndependentNormalizedERP24FeaturesVote4_"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
						mcali = new NewRecognizer("vote4", s);
					}else if (classif == 2) {
						w = new NewWriter("FinalTests\\UserIndependentNormalized24FeaturesVote5\\UserIndependentNormalizedERP24FeaturesVote5_"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
						mcali = new NewRecognizer("vote5", s);
					}else if (classif == 3) {
						w = new NewWriter("FinalTests\\UserIndependentNormalized24FeaturesVote6\\UserIndependentNormalizedERP24FeaturesVote6_"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
						mcali = new NewRecognizer("vote6", s);
					}else{
						w = new NewWriter("FinalTests\\UserIndependentNormalized24FeaturesVote7\\UserIndependentNormalizedERP24FeaturesVote7_"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
						mcali = new NewRecognizer("vote7", s);
					}*/
					
					int user = 1;
					/*
					for (File ficheiroLeitura : new File(args[0]).listFiles()) {
						if ( user != userTest){
							listOfTargetsNTargets = new ArrayList<>();
							ArrayList<Double> valores = getMediaElect(ficheiroLeitura);

							ArrayList<Double> targetMedia = new ArrayList<>();
							ArrayList<Double> ntargetMedia = new ArrayList<>();

							int ntargetDiv = 0;
							int targetDiv = 0;
							int totalTar = 0;
							int totalNTar = 0;
							for (int i = 0; i < listOfTargetsNTargets.size(); i++){

								if (listOfTargetsNTargets.get(i) == 1 && totalNTar < mc){

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
										if (Integer.parseInt(args[1]) == 1) {
											double maxAbsolute = getMaxAbsolute(ntargetMedia);
											ntargetMediaTemp = new ArrayList<>();
											for ( int b = 0; b < 256; b++){

												ntargetMediaTemp.add(ntargetMedia.get(b)/maxAbsolute);

											}
											ntargetMedia = ntargetMediaTemp;
										}
										for (int b = 0; b < 256; b++){
											Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
											g.addPoint(p);	
										}
										g.finalizeStroke();
										try{
											g.calcFeatures();
											mcali.addExample(g);
											w.addGesture(g);
											totalNTar++;

										}catch(Exception e){

										}
										ntargetDiv = 0;
										ntargetMedia = new ArrayList<>();

									}
									i= i+32;

								}else if (listOfTargetsNTargets.get(i) == 2 && totalTar < mc){
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
										if (Integer.parseInt(args[1]) == 1) {

											double maxAbsolute = getMaxAbsolute(targetMedia);
											targetMediaTemp = new ArrayList<>();
											for ( int b = 0; b < 256; b++){

												targetMediaTemp.add(targetMedia.get(b)/maxAbsolute);

											}
											targetMedia = targetMediaTemp;
										}
										for (int b = 0; b < 256; b++){
											Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
											g.addPoint(p);
										}
										try{
											g.finalizeStroke();
											g.calcFeatures();
											mcali.addExample(g);
											w.addGesture(g);
											totalTar++;
										}catch (Exception e){

										}

										targetDiv = 0;
										targetMedia = new ArrayList<>();


									}
									i= i+32;

								}
							}
						}
						user++;

					}
					w.close();

					mcali.trainClassifier();

					*/
				
					HashMap<String, Integer> targetResults = new HashMap<>();
					targetResults.put("Target", 0);
					targetResults.put("NTarget", 0);
					HashMap<String, Integer> ntargetResults = new HashMap<>();
					ntargetResults.put("Target", 0);
					ntargetResults.put("NTarget", 0);


					listOfTargetsNTargets = new ArrayList<>();
					ArrayList<Double> valores = getMediaElect(new File(args[0]).listFiles()[(userTest/6)]);
					System.out.println(new File(args[0]).listFiles()[(userTest/6)].getName());

					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();

					int ntargetDiv = 0;
					int targetDiv = 0;

					int totalTar = 0;
					int totalNTar = 0;
					for (int i = 0; i < listOfTargetsNTargets.size(); i++){

						if (listOfTargetsNTargets.get(i) == 1 && totalNTar < mc ){
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
								NewGesture g2 = new NewGesture("NTarget");
								ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < 256; b++){

									ntargetMediaTemp.add(ntargetMedia.get(b)/avgNTarget);

								}
								ntargetMedia = ntargetMediaTemp;
								if (Integer.parseInt(args[1]) == 1) {

									double maxAbsolute = getMaxAbsolute(ntargetMedia);
									ntargetMediaTemp = new ArrayList<>();
									for ( int b = 0; b < 256; b++){

										ntargetMediaTemp.add(ntargetMedia.get(b)/maxAbsolute);

									}
									ntargetMedia = ntargetMediaTemp;
								}
								for (int b = 0; b < 256; b++){
									Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
									g.addPoint(p);
									g2.addPoint(p);
								}

								try{
									g.finalizeStroke();
									String result = mcali.classify(g);
									ntargetResults.put(result, ntargetResults.get(result) +1 );
									totalNTar++;
								}catch(Exception e){

								}

								ntargetDiv = 0;
								ntargetMedia = new ArrayList<>();

							}
							i= i+32;

						}else if (listOfTargetsNTargets.get(i) == 2  && totalTar < mc){
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
								NewGesture g2 = new NewGesture("Target");
								ArrayList<Double> targetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < 256; b++){

									targetMediaTemp.add(targetMedia.get(b)/avgTarget);

								}
								targetMedia = targetMediaTemp;
								if (Integer.parseInt(args[1]) == 1) {

									double maxAbsolute = getMaxAbsolute(targetMedia);
									targetMediaTemp = new ArrayList<>();
									for ( int b = 0; b < 256; b++){

										targetMediaTemp.add(targetMedia.get(b)/maxAbsolute);

									}
									targetMedia = targetMediaTemp;
								}
								for (int b = 0; b < 256; b++){
									Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
									g.addPoint(p);
									g2.addPoint(p);
								}
								try{
									g.finalizeStroke();	
									String result = mcali.classify(g);
									targetResults.put(result, targetResults.get(result) +1 );
									totalTar++;
								}catch(Exception e){

								}
								targetDiv = 0;
								targetMedia = new ArrayList<>();


							}
							i= i+32;

						}
					}

					double tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
					double ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
					int targetPercentage = (int) (tCacl * 100);
					int ntargetPercentage = (int)(ntCacl * 100);

					//System.out.println( targetPercentage + "----" + ntargetPercentage);
					sb.append("User "+userTest+","+targetPercentage+","+ntargetPercentage+"\n");
					//System.out.println("Foi unm");
					//System.out.println(totalTar);
					//System.out.println(totalNTar);

				}

				pw.write(sb.toString());
				pw.flush();
				pw.close();

				avgTarget++;
				avgNTarget = avgTarget;
				d++;
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
