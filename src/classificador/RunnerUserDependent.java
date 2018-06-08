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
	private final static int fromTrial = 35;
	private final static int mc = 50;
	private final static int mcTest = 20;
	private static int numFeatures = 11;

	public static void main(String[] args) throws Exception {

		avgTarget = avgNTarget = 5;
		while (avgTarget <= 10){
			PrintWriter pw;
			pw = new PrintWriter(new File("FinalTests\\UserDependentNormalizedERP\\UserDependent_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));

			StringBuilder sb = new StringBuilder();
			sb.append("UserTested,Target Percent,NTarget Percent\n");
			if (Integer.parseInt(args[1]) == 0) {

				for (int userTest = 1; userTest < 9; userTest++){
					NewRecognizer mcali;
					NewWriter newWriter = new NewWriter("FinalTests\\UserDependentNormalizedALS\\UserDependent"+userTest+"_T_"+avgTarget+"_NT_"+avgNTarget);
					List<String> s = new ArrayList<String>();
					s.add("Target");
					s.add("NTarget");

					mcali = new NewRecognizer("SVMRBF1", s);

					listOfTargetsNTargets = new ArrayList<>();
					ArrayList<Double> valores = getMediaElect(new File(args[0]).listFiles()[userTest-1]);

					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();

					int ntargetDiv = 0;
					int targetDiv = 0;
					int totalTar = 0;
					int totalNTar = 0;
					int totalTarTest = 0;
					int totalNTarTest = 0;

					HashMap<String, Integer> targetResults = new HashMap<>();
					targetResults.put("Target", 0);
					targetResults.put("NTarget", 0);
					HashMap<String, Integer> ntargetResults = new HashMap<>();
					ntargetResults.put("Target", 0);
					ntargetResults.put("NTarget", 0);

					boolean readyTo = true;

					for (int i = 0; i < listOfTargetsNTargets.size(); i++){

						if (totalNTar < mc || totalTar < mc) {
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
						}else { // quando estiver com 40 targets e 40 nao targets
							if (readyTo == true) {
								mcali.trainClassifier();
								newWriter.close();
								ntargetDiv = 0;
								targetDiv = 0;
								readyTo = false;
								targetMedia = new ArrayList<>();
								ntargetMedia = new ArrayList<>();

							}

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
					sb.append("User "+userTest+","+targetPercentage+","+ntargetPercentage+"\n");
					//System.out.println("Foi unm");
					//System.out.println(totalTar);
					//System.out.println(totalNTar);

				}
			}

			pw.write(sb.toString());
			pw.flush();
			pw.close();

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
