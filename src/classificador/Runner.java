package classificador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
	private final static int multipler = 20;
	private final static int fromTrial = 35;
	private final static int mc = 50;
	private static int numFeatures = 11;

	private final static ArrayList<Integer> listOfTrials = new ArrayList<>(Arrays.asList(1801, 11529, 21257, 30985, 40713,
			51473, 61201, 70929, 80657, 90385, 101145, 110873, 120601, 130329, 140057, 150817, 160545, 170273,
			180001, 189729, 200489, 210217, 219945, 229673, 239401, 250161, 259889, 269617, 279345, 289073, 299833,
			309561, 319289, 329017, 338745,347704));


	public static void main(String[] args) throws Exception {

		for (int classif = 3; classif < 5; classif++){

			avgTarget = avgNTarget = 5;
			while (avgTarget <= 10){
				PrintWriter pw;
				if ( classif == 1){
					pw = new PrintWriter(new File("OfflineTest\\CSV\\UserIndependentNewClassifierNormal8FeaturesVote6_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				}else if ( classif == 2){
					pw = new PrintWriter(new File("OfflineTest\\CSV\\UserIndependentNewClassifierNormal8FeaturesVote7_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				}else if ( classif == 3){
					pw = new PrintWriter(new File("OfflineTest\\CSV\\UserIndependentNewClassifierNormal8FeaturesVote8_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));

				}else {
					pw = new PrintWriter(new File("OfflineTest\\CSV\\UserIndependentNewClassifierNormal8FeaturesSubRandom_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));

				}

				StringBuilder sb = new StringBuilder();
				sb.append("UserTested,Target Percent,NTarget Percent\n");

				for (int userTest = 1; userTest < 9; userTest++){
					NewWriter w;
					NewRecognizer mcali;
					List<String> s = new ArrayList<String>();
					s.add("Target");
					s.add("NTarget");

					if ( classif == 0){

						w = new NewWriter("OfflineTest\\UserIndependentNormalNew8Featuresvote5\\mCaliTested_UsingUser"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget,8);

						mcali = new NewRecognizer("vote5", s,8);

					}else if ( classif == 1){

						w = new NewWriter("OfflineTest\\UserIndependentNormalNew8Featuresvote6\\mCaliTested_UsingUser"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget,8);

						mcali = new NewRecognizer("vote6", s,8);

					}else if ( classif == 2){
						w = new NewWriter("OfflineTest\\UserIndependentNormalNew8Featuresvote7\\mCaliTested_UsingUser"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget,8);

						mcali = new NewRecognizer("vote7", s,8);


					}else if ( classif == 3) {
						w = new NewWriter("OfflineTest\\UserIndependentNormalNew8Featuresvote8\\mCaliTested_UsingUser"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget,15);

						mcali = new NewRecognizer("vote8", s,8);


					}else{
						w = new NewWriter("OfflineTest\\UserIndependentNormalNew8FeaturesSubRandom\\mCaliTested_UsingUser"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget,15);

						mcali = new NewRecognizer("SubRandom", s,8);


					}

					int user = 1;




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
							for (int i = listOfTrials.get(0)-1; i < listOfTargetsNTargets.size(); i++){

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


					HashMap<String, Integer> targetResults = new HashMap<>();
					targetResults.put("Target", 0);
					targetResults.put("NTarget", 0);
					HashMap<String, Integer> ntargetResults = new HashMap<>();
					ntargetResults.put("Target", 0);
					ntargetResults.put("NTarget", 0);


					listOfTargetsNTargets = new ArrayList<>();
					ArrayList<Double> valores = getMediaElect(new File(args[0]).listFiles()[userTest-1]);

					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();

					int ntargetDiv = 0;
					int targetDiv = 0;

					int totalTar = 0;
					int totalNTar = 0;
					for (int i = listOfTrials.get(1)-1; i < listOfTargetsNTargets.size(); i++){

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
								ArrayList<Double> targetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < 256; b++){

									targetMediaTemp.add(targetMedia.get(b)/avgTarget);

								}
								targetMedia = targetMediaTemp;

								for (int b = 0; b < 256; b++){
									Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
									g.addPoint(p);
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
			}
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
