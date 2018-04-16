package classificador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class NewRecognizer {
	private NewClassifying classifier;
	private int numAttributes = 0;
	private ArrayList<Attribute> allAttributes;
	private Instances dataset;

	public NewRecognizer(String classifierName, File trainingFile, int classTypes) {
		this.numAttributes = classTypes;
		classifier = new NewClassifying(classifierName, trainingFile);
	}

	public NewRecognizer(String classifierName, List<String> allClasses, int classTypes) {
		this.numAttributes = classTypes;
		classifier = new NewClassifying(classifierName);

		ArrayList<String> fvClassVal = new ArrayList<String>();
		for (String cls : allClasses)
			fvClassVal.add(cls);

		allAttributes = new ArrayList<Attribute>(numAttributes);
		if (numAttributes == 12){
			allAttributes.add(new Attribute("class", fvClassVal));
			allAttributes.add(new Attribute("circleR"));
			allAttributes.add(new Attribute("rectR1"));
			allAttributes.add(new Attribute("rectR2"));
			allAttributes.add(new Attribute("rectR3"));
			allAttributes.add(new Attribute("rectR4"));
			allAttributes.add(new Attribute("triR3"));
			allAttributes.add(new Attribute("aspectR"));
			allAttributes.add(new Attribute("eqR"));
			allAttributes.add(new Attribute("MovY"));
			allAttributes.add(new Attribute("chR2"));
			allAttributes.add(new Attribute("bbchR"));
			//allAttributes.add(new Attribute("rms"));
		}else if (numAttributes == 11){
			allAttributes.add(new Attribute("class", fvClassVal));
			allAttributes.add(new Attribute("circleR"));
			allAttributes.add(new Attribute("rectR2"));
			allAttributes.add(new Attribute("rectR3"));
			allAttributes.add(new Attribute("triR3"));
			allAttributes.add(new Attribute("aspectR"));
			allAttributes.add(new Attribute("eqR"));
			allAttributes.add(new Attribute("MovY"));
			allAttributes.add(new Attribute("chR2"));
			allAttributes.add(new Attribute("chR3"));
			allAttributes.add(new Attribute("bbchR"));
			//allAttributes.add(new Attribute("rms"));
		} else if (numAttributes == 8){
			allAttributes.add(new Attribute("class", fvClassVal));
			allAttributes.add(new Attribute("rectR2"));
			allAttributes.add(new Attribute("aspectR"));
			allAttributes.add(new Attribute("eqR"));
			allAttributes.add(new Attribute("MovY"));
			allAttributes.add(new Attribute("chR2"));
			allAttributes.add(new Attribute("chR3"));
			allAttributes.add(new Attribute("bbchR"));
			//allAttributes.add(new Attribute("rms"));
		}
		dataset = new Instances("mCALI", allAttributes, 0);           
		dataset.setClassIndex(0);
	}

	public void addExample(NewGesture g) {
		NewFeatures f = g.getFeatures();

		Instance inst = new DenseInstance(numAttributes);
		inst.setDataset(dataset);
		//inst.setClassValue(g.getName());
		if (numAttributes == 12){

			inst.setValue(0, g.getName());
			inst.setValue(1, f.getCircleR());
			inst.setValue(2, f.getRectR1());
			inst.setValue(3, f.getRectR2());
			inst.setValue(4, f.getRectR3());
			inst.setValue(5, f.getRectR4());
			inst.setValue(6, f.getTriR3());
			inst.setValue(7, f.getAspectR());
			inst.setValue(8, f.getEqR1());
			inst.setValue(9, f.getMovementY());
			inst.setValue(10, f.getChR2());
			inst.setValue(11, f.getBbchR());
			//inst.setValue(12, f.getRMS());
			
		}else if (numAttributes == 11){
			inst.setValue(0, g.getName());
			inst.setValue(1, f.getCircleR());
			inst.setValue(2, f.getRectR2());
			inst.setValue(3, f.getRectR3());
			inst.setValue(4, f.getTriR3());
			inst.setValue(5, f.getAspectR());
			inst.setValue(6, f.getEqR1());
			inst.setValue(7, f.getMovementY());
			inst.setValue(8, f.getChR2());
			inst.setValue(9, f.getChR3());
			inst.setValue(10, f.getBbchR());
			//inst.setValue(11, f.getRMS());
			
		}else if (numAttributes == 8){
			inst.setValue(0, g.getName());
			inst.setValue(1, f.getRectR2());
			inst.setValue(2, f.getAspectR());
			inst.setValue(3, f.getEqR1());
			inst.setValue(4, f.getMovementY());
			inst.setValue(5, f.getChR2());
			inst.setValue(6, f.getChR3());
			inst.setValue(7, f.getBbchR());
			//inst.setValue(8, f.getRMS());
		}
		dataset.add(inst);
	}

	public void trainClassifier() {
		/*
		try {
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataset);
			saver.setFile(new File("C:\\Users\\Joao\\Desktop\\test.arff"));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 */
		classifier.trainClassifier(dataset);
	}

	public String classify(NewGesture g) {
		g.calcFeatures();
		return classifier.classify(g.getFeatures(), numAttributes);
	}

	public String classify(Instance test) {
		return classifier.classify(test);
	}
}
