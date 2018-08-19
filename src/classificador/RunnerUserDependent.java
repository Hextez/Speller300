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

import mcali.Point;

public class RunnerUserDependent {

	private static ArrayList<Integer> listOfTargetsNTargets = new ArrayList<>();

	private static int avgTarget = 5;
	private static int avgNTarget = 5;
	private final static int multipler = 150;
	private final static int mc = 50;
	private final static int mcTest = 20;

	public static void main(String[] args) throws Exception {

		avgTarget = avgNTarget = 10;
		while (avgTarget <= 10){
			PrintWriter pw;
			PrintWriter pw1;

			if (Integer.parseInt(args[1]) == 0) {

				for (int userTest = 1; userTest < 9; userTest++){
					pw = new PrintWriter(new File("FinalTests\\UserDependentALSCross\\UserDependent_Geometrico"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
					pw1 = new PrintWriter(new File("FinalTests\\UserDependentALSCross\\UserDependentPeakPicking"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));

					StringBuilder sb = new StringBuilder();
					StringBuilder sb1 = new StringBuilder();

					sb.append("UserTested,Target Percent,NTarget Percent\n");
					sb1.append("UserTested,Target Percent,NTarget Percent\n");

					NewWriter newWriter = new NewWriter("FinalTests\\UserDependentALSCross\\UserDependentSample"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
					List<String> s = new ArrayList<String>();
					s.add("Target");
					s.add("NTarget");


					listOfTargetsNTargets = new ArrayList<>();
					ArrayList<Double> valores = getMediaElect(new File(args[0]).listFiles()[userTest-1]);

					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();
					ArrayList<NewGesture> listOFNTargets = new ArrayList<>();
					ArrayList<NewGesture> listOFTargets = new ArrayList<>();

					ArrayList<Double> listOFNTargetsPP = new ArrayList<>();
					ArrayList<Double> listOFTargetsPP = new ArrayList<>();

					ArrayList<ArrayList<Double>> listOFAveragesNTarget = new ArrayList<>(); 
					ArrayList<ArrayList<Double>> listOFAveragesTarget = new ArrayList<>(); 

					int ntargetDiv = 0;
					int targetDiv = 0;

					for (int i = 0; i < listOfTargetsNTargets.size(); i++){

						if (listOfTargetsNTargets.get(i) == 1){

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

								//Peak Picking
								double lower = new Funcoes().getLowe(ntargetMedia);
								double higher = new Funcoes().getHight(ntargetMedia);

								listOFNTargetsPP.add(higher - lower);

								double maxAbsolute = getMaxAbsolute(ntargetMedia);
								ntargetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < 256; b++){

									ntargetMediaTemp.add(ntargetMedia.get(b)/maxAbsolute);

								}
								ntargetMedia = ntargetMediaTemp;
								listOFAveragesNTarget.add(ntargetMedia);

								for (int b = 0; b < 256; b++){
									Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
									g.addPoint(p);
								}
								g.finalizeStroke();
								try{
									g.calcFeatures();
									newWriter.addGesture(g);
									listOFNTargets.add(g);

								}catch(Exception e){

								}
								ntargetDiv = 0;
								ntargetMedia = new ArrayList<>();

							}
							i= i+32;

						}else if (listOfTargetsNTargets.get(i) == 2){
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

								double maxAbsolute = getMaxAbsolute(targetMedia);
								targetMediaTemp = new ArrayList<>();
								for ( int b = 0; b < 256; b++){

									targetMediaTemp.add(targetMedia.get(b)/maxAbsolute);

								}
								targetMedia = targetMediaTemp;

								listOFAveragesTarget.add(targetMedia);
								for (int b = 0; b < 256; b++){
									Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
									g.addPoint(p);
								}
								try{
									g.finalizeStroke();
									g.calcFeatures();
									newWriter.addGesture(g);
									listOFTargets.add(g);

								}catch (Exception e){

								}

								targetDiv = 0;
								targetMedia = new ArrayList<>();


							}
							i= i+32;

						}
					}
					HashMap<Integer, ArrayList<NewGesture>> model = new HashMap<>();
					HashMap<Integer, ArrayList<Double>> modelPP = new HashMap<>();
					HashMap<Integer, ArrayList<ArrayList<Double>>> modelAverage = new HashMap<>();
					for (int pos = 0; pos < 7; pos++) {

						ArrayList<NewGesture> list = new ArrayList<>();
						ArrayList<Double> ppList = new ArrayList<>();
						ArrayList<ArrayList<Double>> AverageList = new ArrayList<>();

						for (int posL = pos*10; posL < (pos*10) + 10; posL++) {
							list.add(listOFNTargets.get(posL));
							list.add(listOFTargets.get(posL));
							ppList.add(listOFNTargetsPP.get(posL));
							ppList.add(listOFTargetsPP.get(posL));
							AverageList.add(listOFAveragesNTarget.get(posL));
							AverageList.add(listOFAveragesTarget.get(posL));
						}
						model.put(pos+1, list);
						modelPP.put(pos+1, ppList);
						modelAverage.put(pos+1, AverageList);
					}

					NewRecognizer mcali;
					NewWriter averageArff;
					PrintWriter pwAverage = new PrintWriter(new File("FinalTests\\Media\\UserDependentALS_SVMRBF_"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
					StringBuilder sbAverage = new StringBuilder();
					sbAverage.append("UserTested,Target Percent,NTarget Percent\n");


					for (int test = 1; test < 8; test++) {
						mcali = new NewRecognizer("SVMRBF1", s);
						ArrayList<Double> pptargets = new ArrayList<>();
						averageArff = new NewWriter("testeALS", 1);
						String path = averageArff.getARFFPath();
						for (int testL = 1; testL< 8; testL++) {
							if (test != testL) {
								for (int l = 0; l < model.get(testL).size(); l++) {
									mcali.addExample(model.get(testL).get(l));
									Funcoes f = new Funcoes();
									if (l%2 == 0) {
										averageArff.addAverages("NTarget", f.getVector(modelAverage.get(testL).get(l)));
									}else {
										averageArff.addAverages("Target", f.getVector(modelAverage.get(testL).get(l)));
									}
								}
								for (int l = 0; l < modelPP.get(testL).size(); l++) {
									pptargets.add(modelPP.get(testL).get(l));
								}
							}
						}
						averageArff.closeArff();
						mcali.trainClassifier();
						NewRecognizer mcaliAverage = new NewRecognizer("SVMRBF1", new File(path));

						double[] targetl = new double[pptargets.size()];
						for (int i = 0; i < targetl.length; i++) {
							targetl[i] = pptargets.get(i).doubleValue();
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


						for(int i = 0; i < 20; i++) {
							if (i % 2 == 0) {
								if (modelPP.get(test).get(i) < target) {
									ntargetResults.put("NTarget", ntargetResults.get("NTarget") + 1);
								}else{
									ntargetResults.put("Target", ntargetResults.get("Target") + 1);
								}
							}else {
								if (modelPP.get(test).get(i) < target) {
									targetResults.put("NTarget", targetResults.get("NTarget") + 1);
								}else{
									targetResults.put("Target", targetResults.get("Target") + 1);
								}
							}
						}

						double tCacl = (double)targetResults.get("Target")/ 10;
						double ntCacl = (double)ntargetResults.get("NTarget")/ 10;
						int targetPercentage = (int) (tCacl * 100);
						int ntargetPercentage = (int)(ntCacl * 100);

						sb1.append("User "+userTest+","+targetPercentage+","+ntargetPercentage+"\n");

						targetResults = new HashMap<>();
						targetResults.put("Target", 0);
						targetResults.put("NTarget", 0);
						ntargetResults = new HashMap<>();
						ntargetResults.put("Target", 0);
						ntargetResults.put("NTarget", 0);

						for (int l = 0; l < model.get(test).size(); l++) {
							if (model.get(test).get(l).getName().equals("Target")) {
								String result = mcali.classify((model.get(test).get(l)));
								targetResults.put(result, targetResults.get(result)+1);
							}else {
								String result = mcali.classify((model.get(test).get(l)));
								ntargetResults.put(result, ntargetResults.get(result)+1);
							}
						}


						tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
						ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
						targetPercentage = (int) (tCacl * 100);
						ntargetPercentage = (int)(ntCacl * 100);

						//System.out.println( targetPercentage + "----" + ntargetPercentage);
						sb.append("User "+userTest+","+targetPercentage+","+ntargetPercentage+"\n");
						//System.out.println("Foi unm");
						//System.out.println(totalTar);
						//System.out.println(totalNTar);



						targetResults = new HashMap<>();
						targetResults.put("Target", 0);
						targetResults.put("NTarget", 0);
						ntargetResults = new HashMap<>();
						ntargetResults.put("Target", 0);
						ntargetResults.put("NTarget", 0);
						Funcoes f = new Funcoes();
						for (int l = 0; l < modelAverage.get(test).size(); l++) {
							if (l % 2 == 0) {
								String result = mcaliAverage.classify(f.getVector(modelAverage.get(test).get(l)));
								ntargetResults.put(result, ntargetResults.get(result)+1);
							}else {
								String result = mcaliAverage.classify(f.getVector(modelAverage.get(test).get(l)));
								targetResults.put(result, targetResults.get(result)+1);
							}
						}


						tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
						ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
						targetPercentage = (int) (tCacl * 100);
						ntargetPercentage = (int)(ntCacl * 100);

						//System.out.println( targetPercentage + "----" + ntargetPercentage);
						sbAverage.append("User "+test+","+targetPercentage+","+ntargetPercentage+"\n");


					}
					pw.write(sb.toString());
					pw.flush();
					pw.close();
					pw1.write(sb1.toString());
					pw1.flush();
					pw1.close();
					pwAverage.write(sbAverage.toString());
					pwAverage.flush();
					pwAverage.close();
				}
			}else if (Integer.parseInt(args[1]) == 1) {
				for (int userTest = 1; userTest < 31; userTest=userTest+3){
					pw = new PrintWriter(new File("FinalTests\\UserDependentGEOCross\\UserDependent_Geometrico"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
					pw1 = new PrintWriter(new File("FinalTests\\UserDependentGEOCross\\UserDependent_PeakPicking"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
					StringBuilder sb = new StringBuilder();
					sb.append("UserTested,Target Percent,NTarget Percent\n");

					StringBuilder sb1 = new StringBuilder();
					sb1.append("UserTested,Target Percent,NTarget Percent\n");

					NewWriter newWriter = new NewWriter("FinalTests\\UserDependentGEOCross\\UserDependent"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
					List<String> s = new ArrayList<String>();
					s.add("Target");
					s.add("NTarget");

					ArrayList<NewGesture> listOFNTargets = new ArrayList<>();
					ArrayList<NewGesture> listOFTargets = new ArrayList<>();

					ArrayList<ArrayList<Double>> listOFAveragesNTarget = new ArrayList<>(); 
					ArrayList<ArrayList<Double>> listOFAveragesTarget = new ArrayList<>(); 

					ArrayList<Double> listOFNTargetsPP = new ArrayList<>();
					ArrayList<Double> listOFTargetsPP = new ArrayList<>();

					for (int files = 0; files < 3; files++) {
						listOfTargetsNTargets = new ArrayList<>();
						ArrayList<Double> valores = getMediaElectERP(new File(args[0]).listFiles()[userTest+files-1]);

						ArrayList<Double> targetMedia = new ArrayList<>();
						ArrayList<Double> ntargetMedia = new ArrayList<>();

						int ntargetDiv = 0;
						int targetDiv = 0;

						for (int i = 0; i < listOfTargetsNTargets.size(); i++){

							if (listOfTargetsNTargets.get(i) == 1){

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

									//Peak Picking
									double lower = new Funcoes().getLowe(ntargetMedia);
									double higher = new Funcoes().getHight(ntargetMedia);

									listOFNTargetsPP.add(higher - lower);

									double maxAbsolute = getMaxAbsolute(ntargetMedia);
									ntargetMediaTemp = new ArrayList<>();
									for ( int b = 0; b < 256; b++){

										ntargetMediaTemp.add(ntargetMedia.get(b)/maxAbsolute);

									}
									ntargetMedia = ntargetMediaTemp;
									listOFAveragesNTarget.add(ntargetMedia);


									//listOFNTargetsPP.add(higher);
									for (int b = 0; b < 256; b++){
										Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
										g.addPoint(p);
									}
									g.finalizeStroke();
									try{
										g.calcFeatures();
										newWriter.addGesture(g);
										listOFNTargets.add(g);

									}catch(Exception e){

									}
									ntargetDiv = 0;
									ntargetMedia = new ArrayList<>();

								}
								i= i+32;

							}else if (listOfTargetsNTargets.get(i) == 2){
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
									//Peak
									double lower = new Funcoes().getLowe(targetMedia);
									double higher = new Funcoes().getHight(targetMedia);
									//System.out.println(higher +"-----!!!"+ lower);

									listOFTargetsPP.add(higher - lower);
									double maxAbsolute = getMaxAbsolute(targetMedia);
									targetMediaTemp = new ArrayList<>();
									for ( int b = 0; b < 256; b++){

										targetMediaTemp.add(targetMedia.get(b)/maxAbsolute);

									}
									targetMedia = targetMediaTemp;

									listOFAveragesTarget.add(targetMedia);



									for (int b = 0; b < 256; b++){
										Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue()*multipler));
										g.addPoint(p);
									}
									try{
										g.finalizeStroke();
										g.calcFeatures();
										newWriter.addGesture(g);
										listOFTargets.add(g);

									}catch (Exception e){

									}

									targetDiv = 0;
									targetMedia = new ArrayList<>();


								}
								i= i+32;

							}
						}
						//System.out.println(targetDiv);
					}
					HashMap<Integer, ArrayList<NewGesture>> model = new HashMap<>();
					HashMap<Integer, ArrayList<Double>> modelPP = new HashMap<>();
					HashMap<Integer, ArrayList<ArrayList<Double>>> modelAverage = new HashMap<>();

					for (int pos = 0; pos < 7; pos++) {

						ArrayList<NewGesture> list = new ArrayList<>();
						ArrayList<Double> ppList = new ArrayList<>();
						ArrayList<ArrayList<Double>> AverageList = new ArrayList<>();

						for (int posL = pos*3; posL < (pos*3) + 3; posL++) {
							list.add(listOFNTargets.get(posL));
							list.add(listOFTargets.get(posL));
							ppList.add(listOFNTargetsPP.get(posL));
							ppList.add(listOFTargetsPP.get(posL));
							AverageList.add(listOFAveragesNTarget.get(posL));
							AverageList.add(listOFAveragesTarget.get(posL));
						}
						model.put(pos+1, list);
						modelPP.put(pos+1, ppList);
						modelAverage.put(pos+1, AverageList);
					}
					NewRecognizer mcali;
					NewWriter averageArff;
					PrintWriter pwAverage = new PrintWriter(new File("FinalTests\\Media\\UserDependentGEO_SVMRBF_"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
					StringBuilder sbAverage = new StringBuilder();
					sbAverage.append("UserTested,Target Percent,NTarget Percent\n");

					for (int test = 1; test < 8; test++) {
						mcali = new NewRecognizer("SVMRBF1", s);
						ArrayList<Double> pptargets = new ArrayList<>();
						averageArff = new NewWriter("testeERP", 1);
						String path = averageArff.getARFFPath();
						for (int testL = 1; testL< 8; testL++) {
							if (test != testL) {
								for (int l = 0; l < model.get(testL).size(); l++) {
									mcali.addExample(model.get(testL).get(l));
									Funcoes f = new Funcoes();
									if (l%2 == 0) {
										averageArff.addAverages("NTarget", f.getVector(modelAverage.get(testL).get(l)));
									}else {
										averageArff.addAverages("Target", f.getVector(modelAverage.get(testL).get(l)));
									}
								}
								for ( int l = 0; l < modelPP.get(testL).size(); l++) {
									pptargets.add(modelPP.get(testL).get(l));

								}
							}
						}
						averageArff.closeArff();
						mcali.trainClassifier();
						NewRecognizer mcaliAverage = new NewRecognizer("SVMRBF1", new File(path));
						//PP order

						double[] targetl = new double[pptargets.size()];
						for (int i = 0; i < targetl.length; i++) {
							targetl[i] = pptargets.get(i).doubleValue();
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


						for(int i = 0; i < 6; i++) {

							if (i % 2 == 0) {
								if (modelPP.get(test).get(i) < target) {
									ntargetResults.put("NTarget", ntargetResults.get("NTarget") + 1);
								}else{
									ntargetResults.put("Target", ntargetResults.get("Target") + 1);
								}
							}else {
								if (modelPP.get(test).get(i) < target) {
									targetResults.put("NTarget", targetResults.get("NTarget") + 1);
								}else{
									targetResults.put("Target", targetResults.get("Target") + 1);
								}
							}
						}


						double tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
						double ntCacl = (double)ntargetResults.get("NTarget")/ (double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
						int targetPercentage = (int) (tCacl * 100);
						int ntargetPercentage = (int)(ntCacl * 100);

						sb1.append("User "+userTest+","+targetPercentage+","+ntargetPercentage+"\n");

						targetResults = new HashMap<>();
						targetResults.put("Target", 0);
						targetResults.put("NTarget", 0);
						ntargetResults = new HashMap<>();
						ntargetResults.put("Target", 0);
						ntargetResults.put("NTarget", 0);

						for (int l = 0; l < model.get(test).size(); l++) {
							if (model.get(test).get(l).getName().equals("Target")) {
								String result = mcali.classify((model.get(test).get(l)));
								targetResults.put(result, targetResults.get(result)+1);
							}else {
								String result = mcali.classify((model.get(test).get(l)));
								ntargetResults.put(result, ntargetResults.get(result)+1);
							}
						}


						tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
						ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
						targetPercentage = (int) (tCacl * 100);
						ntargetPercentage = (int)(ntCacl * 100);

						//System.out.println( targetPercentage + "----" + ntargetPercentage);
						sb.append("User "+userTest+","+targetPercentage+","+ntargetPercentage+"\n");
						//System.out.println("Foi unm");
						//System.out.println(totalTar);
						//System.out.println(totalNTar);



						targetResults = new HashMap<>();
						targetResults.put("Target", 0);
						targetResults.put("NTarget", 0);
						ntargetResults = new HashMap<>();
						ntargetResults.put("Target", 0);
						ntargetResults.put("NTarget", 0);

						Funcoes f = new Funcoes();
						for (int l = 0; l < modelAverage.get(test).size(); l++) {
							if (l % 2 == 0) {
								String result = mcaliAverage.classify(f.getVector(modelAverage.get(test).get(l)));
								ntargetResults.put(result, ntargetResults.get(result)+1);
							}else {
								String result = mcaliAverage.classify(f.getVector(modelAverage.get(test).get(l)));
								targetResults.put(result, targetResults.get(result)+1);
							}
						}


						tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
						ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
						targetPercentage = (int) (tCacl * 100);
						ntargetPercentage = (int)(ntCacl * 100);

						//System.out.println( targetPercentage + "----" + ntargetPercentage);
						sbAverage.append("User "+test+","+targetPercentage+","+ntargetPercentage+"\n");


					}
					pw.write(sb.toString());
					pw.flush();
					pw.close();
					pw1.write(sb1.toString());
					pw1.flush();
					pw1.close();
					pwAverage.write(sbAverage.toString());
					pwAverage.flush();
					pwAverage.close();
				}
			}else {
				for (int userTest = 1; userTest < 31; userTest=userTest+3){
					NewRecognizer mcali;
					NewWriter newWriter = new NewWriter("FinalTests\\UserDependentNormalizedALS\\UserDependent"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
					List<String> s = new ArrayList<String>();
					s.add("Target");
					s.add("NTarget");

					mcali = new NewRecognizer("SVMRBF1", s);


					HashMap<String, Integer> targetResults = new HashMap<>();
					targetResults.put("Target", 0);
					targetResults.put("NTarget", 0);
					HashMap<String, Integer> ntargetResults = new HashMap<>();
					ntargetResults.put("Target", 0);
					ntargetResults.put("NTarget", 0);

					int totalToread = 0;
					if ( (96/avgTarget) % 1 == 0){
						totalToread = 96/avgTarget;
					}else {
						totalToread = (96/avgTarget) -1;
					}

					for (int files = 0; files < 2; files++) {
						listOfTargetsNTargets = new ArrayList<>();
						ArrayList<Double> valores = getMediaElectERP(new File(args[0]).listFiles()[userTest+files-1]);
						System.out.println(new File(args[0]).listFiles()[userTest+files-1].getName() + "Treino");
						ArrayList<Double> targetMedia = new ArrayList<>();
						ArrayList<Double> ntargetMedia = new ArrayList<>();

						int ntargetDiv = 0;
						int targetDiv = 0;
						int totalTar = 0;
						int totalNTar = 0;
						int totalTarTest = 0;
						int totalNTarTest = 0;

						for (int i = 0; i < listOfTargetsNTargets.size(); i++){

							if (listOfTargetsNTargets.get(i) == 1 && totalNTar < totalToread){

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

									for (int b = 0; b < 256; b++){
										Point p = new Point((int) (b*2),(int) (ntargetMedia.get(b).doubleValue()*multipler));
										g.addPoint(p);	
									}
									g.finalizeStroke();
									try{
										g.calcFeatures();
										newWriter.addGesture(g);
										mcali.addExample(g);
										totalNTar++;

									}catch(Exception e){

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
									try{
										g.finalizeStroke();
										g.calcFeatures();
										newWriter.addGesture(g);
										mcali.addExample(g);
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

					mcali.trainClassifier();
					newWriter.close();

					listOfTargetsNTargets = new ArrayList<>();
					ArrayList<Double> valores = getMediaElectERP(new File(args[0]).listFiles()[userTest+2-1]);
					System.out.println(new File(args[0]).listFiles()[userTest+2-1].getName() + " Teste");
					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();

					int ntargetDiv = 0;
					int targetDiv = 0;
					int totalTarTest = 0;
					int totalNTarTest = 0;

					for (int i = 0; i < listOfTargetsNTargets.size(); i++){
						if (listOfTargetsNTargets.get(i) == 1 && totalNTarTest < mcTest ){
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
									String result = mcali.classify(g);
									ntargetResults.put(result, ntargetResults.get(result) +1 );
									totalNTarTest++;
								}catch(Exception e){

								}

								ntargetDiv = 0;
								ntargetMedia = new ArrayList<>();

							}
							i= i+32;

						}else if (listOfTargetsNTargets.get(i) == 2  && totalTarTest < mcTest){
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
								try{
									g.finalizeStroke();
									String result = mcali.classify(g);
									targetResults.put(result, targetResults.get(result) +1 );
									totalTarTest++;
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
					//sb.append("User "+userTest+","+targetPercentage+","+ntargetPercentage+"\n");
					//System.out.println("Foi unm");
					//System.out.println(totalTar);
					//System.out.println(totalNTar);

				}
			}



			avgTarget++;
			avgNTarget = avgTarget;
		}

	}
	//}

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


	public static double getMaxAbsolute(ArrayList<Double> list) {
		List<Double> x = new ArrayList<Double>(list);
		for( int i = 0; i < x.size(); i++ ){
			x.set( i, Math.abs(x.get(i)) );
		}
		return Collections.max( x );
	}

	public static ArrayList<Double> getMediaElectERP(File file) throws IOException{
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
