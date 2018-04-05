package TrainmCali;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;

public class WekaFeatureSelection {

	public static String[] listaFeatures = new String []{"circleR","rectR1","rectR2", "rectR3", "rectR4", "rectR5",
			"triR1", "triR2", "triR3","aspectR", "fillingR", "eqR", "MovX", "MovY", "chR1", 
			"chR2", "chR3", "Correlation", "bbchR", "quad1FillR", "quad2FillR", "quad3FillR", "quad4FillR"};



	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HashMap<Double, Integer> manyTimes = new HashMap<>();
		File direc = new File(args[0]);
		PrintWriter pw = new PrintWriter(new File("OfflineTest//UserIndependentNormal//ToWeka//T_10_NT_10//Res//CorrelationNormalizado.txt"));
		//PrintWriter pw = new PrintWriter(new File("OfflineTest//UserIndependentNormal//ToWeka//T_10_NT_10//Res//GainInfoNormalizado.txt"));

		StringBuilder sb = new StringBuilder();
		for (File fich : direc.listFiles()) {
			if (!fich.isDirectory()){
				DataSource source = new DataSource(fich.getAbsolutePath());

				Instances data = source.getDataSet();

				data.setClassIndex(0);

				AttributeSelection attsel = new AttributeSelection();  // package weka.attributeSelection!
				CorrelationAttributeEval eval = new CorrelationAttributeEval();
				//InfoGainAttributeEval eval = new InfoGainAttributeEval();

				Ranker search = new Ranker();

				attsel.setEvaluator(eval);
				attsel.setSearch(search);
				attsel.SelectAttributes(data);
				// obtain the attribute indices that were selected
				int[] indices = attsel.selectedAttributes();
				double[][] bo = attsel.rankedAttributes();
				for (double[] ds : bo) {
					for (int i = 1; i < ds.length; i = i + 2){
						if (ds[i] > 0.1){
							if (manyTimes.containsKey(ds[i-1])){
								manyTimes.put(ds[i-1], manyTimes.get(ds[i-1])+1);
							}else{
								manyTimes.put(ds[i-1], 1);

							}
						}else{
							System.out.println("um foi de saco");
						}

					}

				}

			} 
		}
		for (Entry<Double, Integer> val : manyTimes.entrySet()) {
			sb.append(listaFeatures[((int) val.getKey().doubleValue())-1] +":"+val.getValue()+"\n");
		}
		pw.write(sb.toString());
		pw.close();


	}

}
