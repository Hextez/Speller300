package TrainmCali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mcali.Gesture;
import mcali.Point;
import mcali.Recognizer;
import mcali.Writer;

public class OfflineTest {

	private static ArrayList<Integer> listOfTargetsNTargets = new ArrayList<>();

	private  static int avgTarget = 4;
	private  static int avgNTarget = 4;
	private final static int multipler = 100;
	private final static int fromTrial = 18;
	private final static int mc = 50;
	private final static int start = 0;

	private final static ArrayList<Integer> listOfTrials = new ArrayList<>(Arrays.asList(1801, 11529, 21257, 30985, 40713,
			51473, 61201, 70929, 80657, 90385, 101145, 110873, 120601, 130329, 140057, 150817, 160545, 170273,
			180001, 189729, 200489, 210217, 219945, 229673, 239401, 250161, 259889, 269617, 279345, 289073, 299833,
			309561, 319289, 329017, 338745,347704));

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		while (avgTarget <= 6){
			while (avgNTarget <= 10){				
				StringBuilder sb = new StringBuilder();

				PrintWriter pw = null;
				if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==2){
					if (Integer.parseInt(args[1])==0){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\AnguloAbsolutoOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));
					}else if (Integer.parseInt(args[1])==1){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\NormalOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					}else if (Integer.parseInt(args[1])==2){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\NormalizadoOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					}else if (Integer.parseInt(args[1])==3){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\AnguloRelativoOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					} else if (Integer.parseInt(args[1])==4){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\AbsolutoComNormalizadoOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					}else if(Integer.parseInt(args[1])==5){
						if (start == 0){
							pw = new PrintWriter(new File("OfflineTest\\CSV\\0-600NormalOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));
						}else {
							pw = new PrintWriter(new File("OfflineTest\\CSV\\250-600NormalOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));
						}
					}else {
						pw = new PrintWriter(new File("OfflineTest\\CSV\\12SampleOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					}
					sb.append("User,Target Percent,NTarget Percent\n");

				}else if (Integer.parseInt(args[2])==1){
					if (Integer.parseInt(args[1])==0){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\NovaFeatureAreaAnguloRelativoNNormalizadoOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));
					}else if (Integer.parseInt(args[1])==1){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\NovaFeatureAreaAnguloAbsolutoNNormalizadoOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					}else if (Integer.parseInt(args[1])==2){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\NovaFeatureRMSOrignalOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					}else if (Integer.parseInt(args[1])==3){
						pw = new PrintWriter(new File("OfflineTest\\CSV\\NovaFeatureRMSNormalizadoOfflineT_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					} 
					sb.append("User,Target Percent,,,,NTarget Percent\n");

				}
				/*
		PrintWriter pwTest = new PrintWriter(new File("OfflineTest\\valoresToTest.txt"));
		PrintWriter pwTestPontos = new PrintWriter(new File("OfflineTest\\valoresToTestPontos.txt"));
		StringBuilder sbTestPontos = new StringBuilder();
		StringBuilder sbTest = new StringBuilder();
				 */
				int user = 1;
				for (File ficheiroLeitura : new File(args[0]).listFiles()) {

					listOfTargetsNTargets = new ArrayList<>();
					ArrayList<Double> valores = getMediaElect(ficheiroLeitura);
					Writer w = null;
					Recognizer mcali = null;
					if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==2){
						if (Integer.parseInt(args[1])==0){
							w = new Writer("OfflineTest\\XML&&ARFF\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
						}else if (Integer.parseInt(args[1])==1){
							w = new Writer("OfflineTest\\UserDependentNormal\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
						}else if (Integer.parseInt(args[1])==2){
							w = new Writer("OfflineTest\\UserDependentNormalizado\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
						}else if (Integer.parseInt(args[1])==3){
							w = new Writer("OfflineTest\\UserDependentRelativo\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
						}else if (Integer.parseInt(args[1])==4){
							w = new Writer("OfflineTest\\UserDependentNormalizadoAngulo\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
						}else if(Integer.parseInt(args[1])==5){
							if (start == 0){
								w = new Writer("OfflineTest\\UserDependentNormal0\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
							}else {
								w = new Writer("OfflineTest\\UserDependentNormal64\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
							}
						}else {
							w = new Writer("OfflineTest\\UserDependent12Samples\\mCaliTested_User_"+user+"_T_"+avgTarget+"_NT_"+avgNTarget);
						}
						List<String> s = new ArrayList<String>();
						s.add("Target");
						s.add("NTarget");
						mcali = new Recognizer("Vote 1", s);
					}
					//Writer w = new Writer("OfflineTest\\XML&&ARFF\\testar");


					//Valores das medias
					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();

					//array para as novas features
					ArrayList<Double> areasERMSTarget = new ArrayList<>();
					ArrayList<Double> areasERMSNTarget = new ArrayList<>();

					int ntargetDiv = 0;
					int targetDiv = 0;
					int totalTar = 0;
					int totalNTar = 0;

					for (int i = listOfTrials.get(0)-1; i < listOfTrials.get(fromTrial)-1; i++){


						if (listOfTargetsNTargets.get(i) == 1 && totalNTar < mc){

							if (ntargetDiv < avgNTarget){
								if (ntargetDiv == 0){ // primeiro a entrar

									ntargetMedia = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){
										for (int b = 0; b < 256; b++){
											ntargetMedia.add(valores.get(b+i));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if ( count == 12){
												ntargetMedia.add(media12/12);
												count = 0;
												media12 = 0;
											}
										}
									}
									ntargetDiv++;
								}else{ // quando é mais do que 1
									ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){

										for (int b = 0; b < 256; b++){
											ntargetMediaTemp.add(valores.get(b+i) + ntargetMedia.get(b));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										int pos = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if ( count == 12){
												ntargetMediaTemp.add((media12/12) + ntargetMedia.get(pos));
												count = 0;
												pos++;
												media12=0;
											}
										}
									}
									ntargetMedia = ntargetMediaTemp;
									ntargetDiv++;
								}
							}
							if (ntargetDiv == avgNTarget ){ //quando o valor e chegado
								Gesture g = new Gesture("NTarget");

								ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < ntargetMedia.size(); b++){
									ntargetMediaTemp.add(ntargetMedia.get(b)/avgNTarget);
								}

								ntargetMedia = ntargetMediaTemp;

								int minIndex = ntargetMedia.indexOf(Collections.min(ntargetMedia));
								int maxIndex = ntargetMedia.indexOf(Collections.max(ntargetMedia));

								//valor area Relativo
								double calculoDasAreas = 0;


								for (int b = 0; b < ntargetMedia.size(); b++){
									if (Integer.parseInt(args[2]) == 0){
										if (Integer.parseInt(args[1]) == 0){ // valor normalizado e angulo
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (b*2),(int) (sd*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 1){ //valor normal
											Point p = new Point((int) (b*2), (int) (ntargetMedia.get(b).doubleValue()*20));
											g.addPoint(p);
										} else if (Integer.parseInt(args[1]) == 2){ // valor normalizado
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											Point p = new Point((int) (b*2),(int) (valY*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 3){ // valor nao normalizado e relativo
											if (b == 0){
												Point p = new Point(1,0);
												g.addPoint(p);
											}else{
												double valY = ntargetMedia.get(b) - ntargetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												Point p = new Point((int) ((b+1)*2),(int) (sd*multipler));
												g.addPoint(p);
											}
										}else if (Integer.parseInt(args[1]) == 4){ // valor normalizado e angulo
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (valY*multipler),(int) (sd*multipler));
											g.addPoint(p);
										}

									}else if (Integer.parseInt(args[2]) == 1){
										if (Integer.parseInt(args[1]) == 0){//area relativo
											if (b != 0){
												double valY = ntargetMedia.get(b) - ntargetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												if (sd > 0){
													calculoDasAreas+= sd;
												}
											}

										}else if (Integer.parseInt(args[1]) == 1){ //area absoluta
											double sd = Math.atan(ntargetMedia.get(b)/ b);
											if (sd > 0){
												calculoDasAreas+= sd;
											}
										}else if (Integer.parseInt(args[1]) == 2){ //RMS original
											calculoDasAreas += (ntargetMedia.get(b) * ntargetMedia.get(b));

										}else if (Integer.parseInt(args[1]) == 3){ //RMS normalizado
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											calculoDasAreas += (valY * valY);
										}
									}else if (Integer.parseInt(args[2]) == 2){
										Point p = new Point((int) (b*10), (int) (ntargetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 && Integer.parseInt(args[1]) == 5){
									for (int b = start; b < 154; b++ ){
										Point p = new Point((int) (b*2), (int) (ntargetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 || Integer.parseInt(args[2]) == 2){
									g.finalizeStroke();
									try {
										g.calcFeatures();
										mcali.addExample(g);
										w.addGesture(g);
									}catch (Exception e){
										totalNTar--;
									}

								}else if (Integer.parseInt(args[2]) == 1){
									if (Integer.parseInt(args[1])==0){
										areasERMSNTarget.add(calculoDasAreas);
									}else if (Integer.parseInt(args[1])==1){
										areasERMSNTarget.add(calculoDasAreas);
									}else if (Integer.parseInt(args[1])==2){
										double valor = calculoDasAreas/256;
										areasERMSNTarget.add(Math.abs(valor));
									}else if (Integer.parseInt(args[1])==3){
										double valor = calculoDasAreas/256;
										areasERMSNTarget.add(Math.abs(valor));
									} 

								}
								ntargetDiv = 0;
								ntargetMedia = new ArrayList<>();
								totalNTar++;
							}
							i= i+32;

						}else if (listOfTargetsNTargets.get(i) == 2 && totalTar < mc){
							if (targetDiv < avgTarget){
								if (targetDiv == 0){ // primeiro a entrar
									targetMedia = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){
										for (int b = 0; b < 256; b++){
											targetMedia.add(valores.get(b+i));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if ( count == 12){
												targetMedia.add(media12/12);
												count = 0;
												media12 = 0;
											}
										}
									}
									targetDiv++;
								}else{ // quando é mais do que 1
									ArrayList<Double> targetMediaTemp = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){

										for (int b = 0; b < 256; b++){
											targetMediaTemp.add(valores.get(b+i) + targetMedia.get(b));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										int pos = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if ( count == 12){
												targetMediaTemp.add((media12/12) + targetMedia.get(pos));
												count = 0;
												pos++;
												media12=0;
											}
										}
									}
									targetMedia = targetMediaTemp;
									targetDiv++;
								}
							}
							if (targetDiv == avgTarget){ //quando o valor e chegado

								Gesture g = new Gesture("Target");
								ArrayList<Double> targetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < targetMedia.size(); b++){

									targetMediaTemp.add(targetMedia.get(b)/avgTarget);

								}
								targetMedia = targetMediaTemp;

								int minIndex = targetMedia.indexOf(Collections.min(targetMedia));
								int maxIndex = targetMedia.indexOf(Collections.max(targetMedia));

								//valor area Relativo
								double calculoDasAreas = 0;


								for (int b = 0; b < targetMedia.size(); b++){
									if (Integer.parseInt(args[2]) == 0){ // valor normalizado e angulo

										if (Integer.parseInt(args[1]) == 0){ // valor normalizado e angulo
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (b*2),(int) (sd*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 1){ //valor normal
											Point p = new Point((int) (b*2), (int) (targetMedia.get(b).doubleValue()*20));
											g.addPoint(p);
										} else if (Integer.parseInt(args[1]) == 2){ // valor normalizado
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											//double valX = normalise((double)b, (double)0, (double)256);
											Point p = new Point((int) (b*2),(int) (valY*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 3){ // valor nao normalizado e relativo

											if (b == 0){
												Point p = new Point(1,0);
												g.addPoint(p);
											}else{
												double valY = targetMedia.get(b) - targetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												Point p = new Point((int) ((b+1)*2),(int) (sd*multipler));
												g.addPoint(p);
											}
										}else if (Integer.parseInt(args[1]) == 4){ // valor normalizado e angulo
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (valY*multipler),(int) (sd*multipler));
											g.addPoint(p);
										}
									}if (Integer.parseInt(args[2]) == 1){
										if (Integer.parseInt(args[1]) == 0){//area relativo
											if (b != 0){
												double valY = targetMedia.get(b) - targetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												if (sd > 0){
													calculoDasAreas+= sd;
												}
											}

										}else if (Integer.parseInt(args[1]) == 1){ //area absoluta
											double sd = Math.atan(targetMedia.get(b)/ b);
											if (sd > 0){
												calculoDasAreas+= sd;
											}
										}else if (Integer.parseInt(args[1]) == 2){ //RMS original
											calculoDasAreas += (targetMedia.get(b) * targetMedia.get(b));

										}else if (Integer.parseInt(args[1]) == 3){ //RMS normalizado
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											calculoDasAreas += (valY * valY);
										}
									}else if (Integer.parseInt(args[2]) == 2){ //valor normal
										Point p = new Point((int) (b*10), (int) (targetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 && Integer.parseInt(args[1]) == 5){
									for (int b = start; b < 154; b++ ){
										Point p = new Point((int) (b*2), (int) (targetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 || Integer.parseInt(args[2]) == 2){
									g.finalizeStroke();
									try {
										g.calcFeatures();
										mcali.addExample(g);										
										w.addGesture(g);
									}catch (Exception e){
										totalTar--;
									}

								}else if (Integer.parseInt(args[2]) == 1){
									if (Integer.parseInt(args[1])==0){
										areasERMSTarget.add(calculoDasAreas);
									}else if (Integer.parseInt(args[1])==1){
										areasERMSTarget.add(calculoDasAreas);
									}else if (Integer.parseInt(args[1])==2){
										double valor = calculoDasAreas/256;
										areasERMSTarget.add(Math.abs(valor));
									}else if (Integer.parseInt(args[1])==3){
										double valor = calculoDasAreas/256;
										areasERMSTarget.add(Math.abs(valor));
									} 

								}
								targetDiv = 0;
								targetMedia = new ArrayList<>();
								totalTar++;	
							}
							i= i+32;

						}
					}
					if (Integer.parseInt(args[2]) == 0 || Integer.parseInt(args[2]) == 2 ){
						w.close();
						mcali.trainClassifier();
					}

					double lowerQT = 0;
					double upperQT = 0;
					double lowerQNT = 0;
					double upperQNT = 0;

					if (Integer.parseInt(args[2]) == 1){ 
						Collections.sort(areasERMSNTarget);
						Collections.sort(areasERMSTarget);

						if ((areasERMSTarget.size()*0.1) % 1 != 0 ){
							lowerQT = areasERMSTarget.get((int) (areasERMSTarget.size()*0.1));
							upperQT =areasERMSTarget.get((int) (areasERMSTarget.size()*0.9));
						}else{
							lowerQT = (areasERMSTarget.get((int) (areasERMSTarget.size()*0.1))+
									areasERMSTarget.get((int) (areasERMSTarget.size()*0.1)+1))/2;
							upperQT = (areasERMSTarget.get((int) (areasERMSTarget.size()*0.9))+
									areasERMSTarget.get((int) (areasERMSTarget.size()*0.9)+1))/2;
						}

						if ((areasERMSNTarget.size()*0.1) % 1 != 0 ){
							lowerQNT = areasERMSNTarget.get((int) (areasERMSNTarget.size()*0.1));
							upperQNT =areasERMSNTarget.get((int) (areasERMSNTarget.size()*0.9));
						}else{
							lowerQNT = (areasERMSNTarget.get((int) (areasERMSNTarget.size()*0.1))+
									areasERMSNTarget.get((int) (areasERMSNTarget.size()*0.1)+1))/2;
							upperQNT = (areasERMSNTarget.get((int) (areasERMSNTarget.size()*0.9))+
									areasERMSNTarget.get((int) (areasERMSNTarget.size()*0.9)+1))/2;
						}


					}


					HashMap<String, Integer> targetResults = new HashMap<>();
					targetResults.put("Target", 0);
					targetResults.put("NTarget", 0);
					targetResults.put("Dois", 0);
					targetResults.put("Nenhum", 0);

					HashMap<String, Integer> ntargetResults = new HashMap<>();
					ntargetResults.put("Target", 0);
					ntargetResults.put("NTarget", 0);
					ntargetResults.put("Dois", 0);
					ntargetResults.put("Nenhum", 0);


					targetMedia = new ArrayList<>();
					ntargetMedia = new ArrayList<>();


					ntargetDiv = 0;
					targetDiv = 0;
					/*
					System.out.println("Testes-- LowerQT:"+lowerQT);

					System.out.println("UpperQT:"+upperQT);

					System.out.println("LowerQNT:"+lowerQNT);

					System.out.println("UpperQNT:"+upperQNT);

					System.out.println("");
					 */

					for (int i = listOfTrials.get(fromTrial)-1; i < listOfTargetsNTargets.size(); i++){

						if (listOfTargetsNTargets.get(i) == 1 ){
							if (ntargetDiv < avgNTarget){

								if (ntargetDiv == 0){ // primeiro a entrar

									ntargetMedia = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){
										for (int b = 0; b < 256; b++){
											ntargetMedia.add(valores.get(b+i));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if ( count == 12){
												ntargetMedia.add(media12/12);
												count = 0;
												media12=0;
											}
										}
									}
									ntargetDiv++;
								}else{ // quando é mais do que 1
									ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){

										for (int b = 0; b < 256; b++){
											ntargetMediaTemp.add(valores.get(b+i) + ntargetMedia.get(b));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										int pos = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if (count == 12){
												ntargetMediaTemp.add((media12/12) + ntargetMedia.get(pos));
												count = 0;
												pos++;
												media12=0;
											}
										}
									}
									ntargetMedia = ntargetMediaTemp;
									ntargetDiv++;
								}
							}
							if (ntargetDiv == avgNTarget){
								Gesture g = new Gesture();

								ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < ntargetMedia.size(); b++){

									ntargetMediaTemp.add(ntargetMedia.get(b)/avgNTarget);

								}
								ntargetMedia = ntargetMediaTemp;

								int minIndex = ntargetMedia.indexOf(Collections.min(ntargetMedia));
								int maxIndex = ntargetMedia.indexOf(Collections.max(ntargetMedia));

								//valor area Relativo
								double calculoDasAreas = 0;

								for (int b = 0; b < ntargetMedia.size(); b++){
									if (Integer.parseInt(args[2]) == 0){
										if (Integer.parseInt(args[1]) == 0){ // valor normalizado e angulo
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (b*2),(int) (sd*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 1){ //valor normal
											Point p = new Point((int) (b*2), (int) (ntargetMedia.get(b).doubleValue()*20));
											g.addPoint(p);
										} else if (Integer.parseInt(args[1]) == 2){ // valor normalizado
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											Point p = new Point((int) (b*2),(int) (valY*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 3){ // valor nao normalizado e relativo
											if (b == 0){
												Point p = new Point(1,0);
												g.addPoint(p);
											}else{
												double valY = ntargetMedia.get(b) - ntargetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												Point p = new Point((int) ((b+1)*2),(int) (sd*multipler));
												g.addPoint(p);
											}
										}else if (Integer.parseInt(args[1]) == 4){ // valor normalizado e angulo
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (valY*multipler),(int) (sd*multipler));
											g.addPoint(p);
										}
									}else if (Integer.parseInt(args[2]) == 1){
										if (Integer.parseInt(args[1]) == 0){//area relativo
											if (b != 0){
												double valY = ntargetMedia.get(b) - ntargetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												if (sd > 0){
													calculoDasAreas+= sd;
												}
											}

										}else if (Integer.parseInt(args[1]) == 1){ //area absoluta
											double sd = Math.atan(ntargetMedia.get(b)/ b);
											if (sd > 0){
												calculoDasAreas+= sd;
											}
										}else if (Integer.parseInt(args[1]) == 2){ //RMS original
											calculoDasAreas += (ntargetMedia.get(b) * ntargetMedia.get(b));

										}else if (Integer.parseInt(args[1]) == 3){ //RMS normalizado
											double valY = normalise(ntargetMedia.get(b), ntargetMedia.get(minIndex), ntargetMedia.get(maxIndex));
											calculoDasAreas += (valY * valY);
										}
									}else if (Integer.parseInt(args[2]) == 2){
										Point p = new Point((int) (b*10), (int) (ntargetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 && Integer.parseInt(args[1]) == 5){
									for (int b = start; b < 154; b++ ){
										Point p = new Point((int) (b*2), (int) (ntargetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 || Integer.parseInt(args[2]) == 2){
									g.finalizeStroke();
									try {
										g.calcFeatures();
										String result = mcali.classify(g);
										ntargetResults.put(result, ntargetResults.get(result) +1 );
									}catch (Exception e){

									}

								}else if (Integer.parseInt(args[2]) == 1){
									double valToTest = 0;
									if (Integer.parseInt(args[1])==0){
										valToTest = calculoDasAreas;
									}else if (Integer.parseInt(args[1])==1){
										valToTest = calculoDasAreas;
									}else if (Integer.parseInt(args[1])==2){
										double valor = calculoDasAreas/256;
										valToTest = Math.abs(valor);
									}else if (Integer.parseInt(args[1])==3){
										double valor = calculoDasAreas/256;
										valToTest = Math.abs(valor);
									} 

									boolean resultT = false;
									boolean resultNT = false;
									if (valToTest >= lowerQT && valToTest <= upperQT){
										resultT = true;
									}
									if (valToTest >= lowerQNT && valToTest <= upperQNT){
										resultNT = true;
									}

									if (resultT == true && resultNT == true){
										ntargetResults.put("Dois", ntargetResults.get("Dois") +1 );
									}else if (resultT == false && resultNT == true){
										ntargetResults.put("NTarget", ntargetResults.get("NTarget") +1 );

									}else if (resultT == true && resultNT == false){
										ntargetResults.put("Target", ntargetResults.get("Target") +1 );

									}else{
										ntargetResults.put("Nenhum", ntargetResults.get("Nenhum") +1 );

									}

								}
								ntargetDiv = 0;
								ntargetMedia = new ArrayList<>();

							}
							i= i+32;

						}else if (listOfTargetsNTargets.get(i) == 2){
							if (targetDiv < avgTarget){
								if (targetDiv == 0){ // primeiro a entrar
									targetMedia = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){
										for (int b = 0; b < 256; b++){
											targetMedia.add(valores.get(b+i));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if ( count == 12){
												targetMedia.add(media12/12);
												count = 0;
												media12=0;
											}
										}
									}
									targetDiv++;
								}else{ // quando é mais do que 1
									ArrayList<Double> targetMediaTemp = new ArrayList<>();
									if (Integer.parseInt(args[2])==0 || Integer.parseInt(args[2])==1){

										for (int b = 0; b < 256; b++){
											targetMediaTemp.add(valores.get(b+i) + targetMedia.get(b));

										}
									}else if (Integer.parseInt(args[2])==2){
										double media12 = 0;
										int count = 0;
										int pos = 0;
										for (int b = 0; b < 252; b++){
											media12 += valores.get(b+i);
											count++;
											if (count == 12){
												targetMediaTemp.add((media12/12) + targetMedia.get(pos));
												count = 0;
												pos++;
												media12=0;
											}
										}
									}
									targetMedia = targetMediaTemp;
									targetDiv++;
								}
							}
							if (targetDiv == avgTarget){ //quando o valor e chegado

								Gesture g = new Gesture();
								ArrayList<Double> targetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < targetMedia.size(); b++){

									targetMediaTemp.add(targetMedia.get(b)/avgTarget);

								}
								targetMedia = targetMediaTemp;

								int minIndex = targetMedia.indexOf(Collections.min(targetMedia));
								int maxIndex = targetMedia.indexOf(Collections.max(targetMedia));

								//valor area Relativo
								double calculoDasAreas = 0;


								for (int b = 0; b < targetMedia.size(); b++){
									if (Integer.parseInt(args[2]) == 0){ // valor normalizado e angulo

										if (Integer.parseInt(args[1]) == 0){ // valor normalizado e angulo
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (b*2),(int) (sd*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 1){ //valor normal

											Point p = new Point((int) (b*2), (int) (targetMedia.get(b).doubleValue()*20));
											g.addPoint(p);
										} else if (Integer.parseInt(args[1]) == 2){ // valor normalizado
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											//double valX = normalise((double)b, (double)0, (double)256);
											Point p = new Point((int) (b*2),(int) (valY*multipler));
											g.addPoint(p);
										}else if (Integer.parseInt(args[1]) == 3){ // valor nao normalizado e relativo

											if (b == 0){
												Point p = new Point(1,0);
												g.addPoint(p);
											}else{
												double valY = targetMedia.get(b) - targetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												Point p = new Point((int) ((b+1)*2),(int) (sd*multipler));
												g.addPoint(p);
											}
										}else if (Integer.parseInt(args[1]) == 4){ // valor normalizado e angulo
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											double valX = normalise((double)b, (double)0, (double)256);
											double sd = Math.atan(valY/ valX );
											Point p = new Point((int) (valY*multipler),(int) (sd*multipler));
											g.addPoint(p);
										}
									}else if (Integer.parseInt(args[2]) == 1){
										if (Integer.parseInt(args[1]) == 0){//area relativo
											if (b != 0){
												double valY = targetMedia.get(b) - targetMedia.get(b-1);
												double valX = (b+1)-(b);
												double sd = Math.atan(valY/ valX );
												if (sd > 0){
													calculoDasAreas+= sd;
												}
											}

										}else if (Integer.parseInt(args[1]) == 1){ //area absoluta
											double sd = Math.atan(targetMedia.get(b)/ b);
											if (sd > 0){
												calculoDasAreas+= sd;
											}
										}else if (Integer.parseInt(args[1]) == 2){ //RMS original
											calculoDasAreas += (targetMedia.get(b) * targetMedia.get(b));

										}else if (Integer.parseInt(args[1]) == 3){ //RMS normalizado
											double valY = normalise(targetMedia.get(b), targetMedia.get(minIndex), targetMedia.get(maxIndex));
											calculoDasAreas += (valY * valY);
										}
									}else if (Integer.parseInt(args[2]) == 2){
										Point p = new Point((int) (b*10), (int) (targetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 && Integer.parseInt(args[1]) == 5){
									for (int b = start; b < 154; b++ ){
										Point p = new Point((int) (b*2), (int) (targetMedia.get(b).doubleValue()*20));
										g.addPoint(p);
									}
								}
								if (Integer.parseInt(args[2]) == 0 || Integer.parseInt(args[2]) == 2){
									g.finalizeStroke();
									try {
										g.calcFeatures();
										String result = mcali.classify(g);
										targetResults.put(result, targetResults.get(result) +1 );
									}catch (Exception e){
									}


								}else if (Integer.parseInt(args[2]) == 1){
									double valToTest = 0;
									if (Integer.parseInt(args[1])==0){
										valToTest = calculoDasAreas;
									}else if (Integer.parseInt(args[1])==1){
										valToTest = calculoDasAreas;
									}else if (Integer.parseInt(args[1])==2){
										double valor = calculoDasAreas/256;
										valToTest = Math.abs(valor);
									}else if (Integer.parseInt(args[1])==3){
										double valor = calculoDasAreas/256;
										valToTest = Math.abs(valor);
									} 

									boolean resultT = false;
									boolean resultNT = false;
									if (valToTest >= lowerQT && valToTest <= upperQT){
										resultT = true;
									}
									if (valToTest >= lowerQNT && valToTest <= upperQNT){
										resultNT = true;
									}

									if (resultT == true && resultNT == true){
										targetResults.put("Dois", targetResults.get("Dois") +1 );
									}else if (resultT == false && resultNT == true){
										targetResults.put("NTarget", targetResults.get("NTarget") +1 );

									}else if (resultT == true && resultNT == false){
										targetResults.put("Target", targetResults.get("Target") +1 );

									}else{
										targetResults.put("Nenhum", targetResults.get("Nenhum") +1 );

									}

								}
								targetDiv = 0;
								targetMedia = new ArrayList<>();
							}
							i= i+32;
						}

					}
					if (Integer.parseInt(args[2]) == 0 || Integer.parseInt(args[2]) == 2){
						double tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
						double ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
						int targetPercentage = (int) (tCacl * 100);
						int ntargetPercentage = (int)(ntCacl * 100);
						sb.append("User "+user+","+targetPercentage+","+ntargetPercentage+"\n");

					}else if (Integer.parseInt(args[2]) == 1){
						double tCaclT = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget")+targetResults.get("Dois")+targetResults.get("Nenhum"));
						double tCaclNT = (double)targetResults.get("NTarget")/ (double)(targetResults.get("Target") + targetResults.get("NTarget")+targetResults.get("Dois")+targetResults.get("Nenhum"));
						double tCaclDois = (double)targetResults.get("Dois")/ (double)(targetResults.get("Target") + targetResults.get("NTarget")+targetResults.get("Dois")+targetResults.get("Nenhum"));
						double tCaclNenhum = (double)targetResults.get("Nenhum")/ (double)(targetResults.get("Target") + targetResults.get("NTarget")+targetResults.get("Dois")+targetResults.get("Nenhum"));

						double ntCaclT = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget")+ntargetResults.get("Dois")+ntargetResults.get("Nenhum"));
						double ntCaclNT = (double)ntargetResults.get("Target")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget")+ntargetResults.get("Dois")+ntargetResults.get("Nenhum"));
						double ntCaclDois = (double)ntargetResults.get("Dois")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget")+ntargetResults.get("Dois")+ntargetResults.get("Nenhum"));
						double ntCaclNenhum = (double)ntargetResults.get("Nenhum")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget")+ntargetResults.get("Dois")+ntargetResults.get("Nenhum"));

						sb.append("User "+user+","+(int) (tCaclT * 100)+","+(int) (tCaclNT * 100)+","+(int) (tCaclDois * 100)+","+(int) (tCaclNenhum * 100)
								+","+(int) (ntCaclT * 100)+","+(int) (ntCaclNT * 100)+","+(int) (ntCaclDois * 100)+","+(int) (ntCaclNenhum * 100)+"\n");
					}


					//System.out.println( targetPercentage + "----" + ntargetPercentage);

					//System.out.println(totalTar);
					//System.out.println(totalNTar);
					user++;
				}
				pw.write(sb.toString());
				pw.flush();
				pw.close();
				avgNTarget++;
			}
			avgTarget++;
			avgNTarget = avgTarget;
		}
		/*pwTest.write(sbTest.toString());
		pwTest.flush();
		pwTest.close();

		pwTestPontos.write(sbTestPontos.toString());
		pwTestPontos.flush();
		pwTestPontos.close();*/


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
