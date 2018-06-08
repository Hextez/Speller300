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

import org.apache.poi.util.ArrayUtil;
import org.jfree.util.ArrayUtilities;

import mcali.Point;

public class CombineDatasets {

	private static int avgTarget = 5;
	private static int avgNTarget = 5;
	private static int mc = 5;
	private static int multipler = 150;
	private static ArrayList<ArrayList<Integer>> possibiALS;
	private static ArrayList<ArrayList<Integer>> possibiERP;
	private static ArrayList<Integer> listOfTargetsNTargetsALS = new ArrayList<>();
	private static ArrayList<Integer> listOfTargetsNTargetsERP = new ArrayList<>();


	/* arr[] ---> Input Array
	data[] ---> Temporary array to store current combination
	start & end ---> Staring and Ending indexes in arr[]
	index ---> Current index in data[]
	r ---> Size of a combination to be printed */
	static void combinationUtil(int arr[], int data[], int start,
			int end, int index, int r)
	{
		// Current combination is ready to be printed, print it
		if (index == r)
		{
			ArrayList<Integer> lst = new ArrayList<>();
			for (int j=0; j<r; j++) {
				lst.add(data[j]);
			}
			if (r == 5) {
				possibiERP.add(lst);
			}else if (r == 4) {
				possibiALS.add(lst);
			}
			return;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i=start; i<=end && end-i+1 >= r-index; i++)
		{
			data[index] = arr[i];
			combinationUtil(arr, data, i+1, end, index+1, r);
		}
	}

	// The main function that prints all combinations of size r
	// in arr[] of size n. This function mainly uses combinationUtil()
	static void printCombination(int arr[], int n, int r)
	{
		// A temporary array to store all combination one by one
		int data[]=new int[r];

		// Print all combination using temprary array 'data[]'
		combinationUtil(arr, data, 0, n-1, 0, r);
	}

	/*Driver function to check for above function*/
	public static void main (String[] args) throws Exception {

		File[] ficheirosALS = new File(args[0]).listFiles();
		File[] ficheirosERP = new File(args[1]).listFiles();
		File[] ficheirosGEO = new File(args[2]).listFiles();


		avgTarget = avgNTarget = 5;
		while (avgTarget <= 10) {
			int totalToread = 0;
			if ( (96/avgTarget) % 1 == 0){
				totalToread = 96/avgTarget;
			}else {
				totalToread = (96/avgTarget) -1;
			}
			//totalToread = 1;
			PrintWriter pw = new PrintWriter(new File("FinalTests\\CrossResults\\CrossValidationALSTesssWith_T_"+avgTarget+"_NT_"+avgNTarget+".csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("UserTested,Target Percent,NTarget Percent\n");
			

			for (int user = 1; user < 19; user ++) {
				List<String> s = new ArrayList<String>();
				s.add("Target");
				s.add("NTarget");
				NewRecognizer mcali = new NewRecognizer("SVMRBF1", s);
				for (int file = 1; file < 9; file++) {
					if (user != file) {
						int totalOfFile;
						int files;
						if (file > 8) {
							if ( file > 18) {
							//	System.out.println(file-1-18);
								files = (file-1-18) * 3;

							}else {
							//	System.out.println(file-1-8);
								files = (file-1-8) * 3;

							}
							totalOfFile = 3;
						}else {
							totalOfFile = 1;
							files = file;
							//System.out.println(files);

						}
						for (int nub = 0; nub < totalOfFile; nub++) {
							listOfTargetsNTargetsALS = new ArrayList<>();
							ArrayList<Double> valores = new ArrayList<>();
							if (file > 8) {
								if (file > 18) {
									valores = getMediaElectERP(ficheirosGEO[files+nub]);
								}else {
									valores = getMediaElectERP(ficheirosERP[files+nub]);
								}
								mc = totalToread;
							}else {
								valores = getMediaElectALS(ficheirosALS[files-1+nub]);
								mc = totalToread * 5;
							}
							ArrayList<Double> targetMedia = new ArrayList<>();
							ArrayList<Double> ntargetMedia = new ArrayList<>();

							int ntargetDiv = 0;
							int targetDiv = 0;
							int totalTar = 0;
							int totalNTar = 0;

							for (int a = 0; a < listOfTargetsNTargetsALS.size() ; a++) {
								if (listOfTargetsNTargetsALS.get(a) == 1 && totalNTar < mc){

									if (ntargetDiv < avgNTarget){
										if (ntargetDiv == 0){ // primeiro a entrar

											ntargetMedia = new ArrayList<>();
											for (int b = 0; b < 256; b++){
												ntargetMedia.add(valores.get(b+a));

											}
											ntargetDiv++;
										}else{ // quando é mais do que 1
											ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
											for (int b = 0; b < 256; b++){
												ntargetMediaTemp.add(valores.get(b+a) + ntargetMedia.get(b));

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
											mcali.addExample(g);
											//w.addGesture(g);
											totalNTar++;

										}catch(Exception e){

										}
										ntargetDiv = 0;
										ntargetMedia = new ArrayList<>();

									}
									a= a+32;

								}else if (listOfTargetsNTargetsALS.get(a) == 2 && totalTar < mc){
									if (targetDiv < avgTarget){
										if (targetDiv == 0){ // primeiro a entrar

											targetMedia = new ArrayList<>();
											for (int b = 0; b < 256; b++){
												targetMedia.add(valores.get(b+a));

											}
											targetDiv++;
										}else{ // quando é mais do que 1
											ArrayList<Double> targetMediaTemp = new ArrayList<>();
											for (int b = 0; b < 256; b++){
												targetMediaTemp.add(valores.get(b+a) + targetMedia.get(b));

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
											mcali.addExample(g);
											//w.addGesture(g);
											totalTar++;
										}catch (Exception e){

										}
										targetDiv = 0;
										targetMedia = new ArrayList<>();
									}
									a= a+32;
								}
							}
						}
					}
				}
				mcali.trainClassifier();



				HashMap<String, Integer> targetResults = new HashMap<>();
				targetResults.put("Target", 0);
				targetResults.put("NTarget", 0);
				HashMap<String, Integer> ntargetResults = new HashMap<>();
				ntargetResults.put("Target", 0);
				ntargetResults.put("NTarget", 0);

				int totalOfFile;
				int files;
				if (user > 8) {
					totalOfFile = 3;
					if ( user > 18) {
						//	System.out.println(file-1-18);
							files = (user-1-18) * 3;

						}else {
						//	System.out.println(file-1-8);
							files = (user-1-8) * 3;

						}
				}else {
					totalOfFile = 1;
					files = user;
				}
				for (int nub = 0; nub < totalOfFile; nub++) {
					listOfTargetsNTargetsALS = new ArrayList<>();
					ArrayList<Double> valores = new ArrayList<>();
					listOfTargetsNTargetsALS = new ArrayList<>();

					if (user > 8) {
						if (user > 18) {
							valores = getMediaElectERP(ficheirosGEO[files+nub]);
							//System.out.println(ficheirosGEO[files+nub].getName());
						}else {
							valores = getMediaElectERP(ficheirosERP[files+nub]);
							//System.out.println(ficheirosERP[files+nub].getName());

						}
						mc = totalToread;
					}else {
						valores = getMediaElectALS(ficheirosALS[files-1-nub]);
						//System.out.println(ficheirosALS[files-1-nub].getName());

						mc = totalToread * 7 ;

					}
					ArrayList<Double> targetMedia = new ArrayList<>();
					ArrayList<Double> ntargetMedia = new ArrayList<>();

					int ntargetDiv = 0;
					int targetDiv = 0;
					int totalTar = 0;
					int totalNTar = 0;

					for (int a = 0; a < listOfTargetsNTargetsALS.size() ; a++) {
						if (listOfTargetsNTargetsALS.get(a) == 1 && totalNTar < mc){

							if (ntargetDiv < avgNTarget){
								if (ntargetDiv == 0){ // primeiro a entrar

									ntargetMedia = new ArrayList<>();
									for (int b = 0; b < 256; b++){
										ntargetMedia.add(valores.get(b+a));

									}
									ntargetDiv++;
								}else{ // quando é mais do que 1
									ArrayList<Double> ntargetMediaTemp = new ArrayList<>();
									for (int b = 0; b < 256; b++){
										ntargetMediaTemp.add(valores.get(b+a) + ntargetMedia.get(b));

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
								g.finalizeStroke();
								try{
									String result = mcali.classify(g);
									ntargetResults.put(result, ntargetResults.get(result) +1 );
									totalNTar++;

								}catch(Exception e){

								}
								ntargetDiv = 0;
								ntargetMedia = new ArrayList<>();

							}
							a= a+32;

						}else if (listOfTargetsNTargetsALS.get(a) == 2 && totalTar < mc){
							if (targetDiv < avgTarget){
								if (targetDiv == 0){ // primeiro a entrar

									targetMedia = new ArrayList<>();
									for (int b = 0; b < 256; b++){
										targetMedia.add(valores.get(b+a));

									}
									targetDiv++;
								}else{ // quando é mais do que 1
									ArrayList<Double> targetMediaTemp = new ArrayList<>();
									for (int b = 0; b < 256; b++){
										targetMediaTemp.add(valores.get(b+a) + targetMedia.get(b));

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
									//w.addGesture(g);
									totalTar++;
								}catch (Exception e){

								}
								targetDiv = 0;
								targetMedia = new ArrayList<>();
							}
							a= a+32;
						}
					}
				}
				double tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
				double ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
				int targetPercentage = (int) (tCacl * 100);
				int ntargetPercentage = (int)(ntCacl * 100);

				//System.out.println( targetPercentage + "----" + ntargetPercentage);
				sb.append("User "+user+","+targetPercentage+","+ntargetPercentage+"\n");
				System.out.println("user "+user);
			}
			pw.write(sb.toString());
			pw.flush();
			pw.close();

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

	public static ArrayList<Double> getMediaElectALS(File file) throws IOException{
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
			listOfTargetsNTargetsALS.add(Integer.parseInt(linha[8]));
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
			listOfTargetsNTargetsALS.add(Integer.parseInt(linha[16]));
		}	
		return valores;
	}


}
