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

public class RunnerWithNewData {
	
	private static ArrayList<Integer> listOfTargetsNTargets = new ArrayList<>();

	private static int avgTarget = 5;
	private static int avgNTarget = 5;
	private final static int multipler = 150;
	private final static int fromTrial = 35;
	private final static int mc = 50;



	public static void main(String[] args) throws Exception {

		for (int classif = 0; classif < 1; classif++){

			avgTarget = avgNTarget = 5;

			while (avgTarget <= 10){
				PrintWriter pw;				
				NewRecognizer mcali;
				NewWriter nw;

				//pw = new PrintWriter(new File("FinalTests\\Intensifications\\ProbIntensificationsERP_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
				pw = new PrintWriter(new File("FinalTests\\CrossResults\\CrossValitadtionALSwithERPReduced_T"+avgTarget+"_NT_"+avgNTarget+".csv"));
				mcali = new NewRecognizer("SVMRBF1",new File(args[0]).listFiles()[avgTarget-5]);
				//mcali = new NewRecognizer("SVMRBF1", new File("C:\\Users\\Eu\\workspace\\Tese\\FinalTests\\AllUsers\\Novos\\ERP\\mCaliTested_AllUsersERPNormalized_T_10_NT_10.arff"));
				//mcali = new NewRecognizer("SVMRBF1", new File("C:\\Users\\Eu\\workspace\\Tese\\FinalTests\\AllUsers\\Novos\\ALS\\mCaliTested_AllUsersALSNormalized_T_10_NT_10.arff"));
				
				StringBuilder sb = new StringBuilder();
				sb.append("UserTested,Target Percent,NTarget Percent\n");


				//mcali.trainClassifier();

				for (File ficheiroLeitura : new File(args[1]).listFiles()) {
					HashMap<String, Integer> targetResults = new HashMap<>();
					targetResults.put("Target", 0);
					targetResults.put("NTarget", 0);
					HashMap<String, Integer> ntargetResults = new HashMap<>();
					ntargetResults.put("Target", 0);
					ntargetResults.put("NTarget", 0); 

					listOfTargetsNTargets = new ArrayList<>();
					ArrayList<Double> valores = getMediaElectERP(ficheiroLeitura);

					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();

					int ntargetDiv = 0;
					int targetDiv = 0;
					int totalTar = 0;
					int totalNTar = 0;
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
									totalNTar++;

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
									Point p = new Point((int) (b*2),(int) (targetMedia.get(b).doubleValue() * multipler));
									g.addPoint(p);
								}
								try{
									g.finalizeStroke();
									String result = mcali.classify(g);
									targetResults.put(result, targetResults.get(result) +1 );
									totalNTar++;
								}catch (Exception e){

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
					sb.append("User"+ficheiroLeitura.getName()+","+targetPercentage+","+ntargetPercentage+"\n");
					//System.out.println("lido - "+ficheiroLeitura.getName());

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

	public static double getMaxAbsolute(ArrayList<Double> list) {
		List<Double> x = new ArrayList<Double>(list);
		for( int i = 0; i < x.size(); i++ ){
			x.set( i, Math.abs(x.get(i)) );
		}
		return Collections.max( x );
	}
}
