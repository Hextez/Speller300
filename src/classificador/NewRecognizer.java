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
	private int numAttributes = 17;
	private ArrayList<Attribute> allAttributes;
	private Instances dataset;
	
	public NewRecognizer(String classifierName, File trainingFile) {
		classifier = new NewClassifying(classifierName, trainingFile);
	}
	
	public NewRecognizer(String classifierName, List<String> allClasses) {
		classifier = new NewClassifying(classifierName);
		
		ArrayList<String> fvClassVal = new ArrayList<String>();
		for (String cls : allClasses)
			fvClassVal.add(cls);
		
		allAttributes = new ArrayList<Attribute>(numAttributes);
		allAttributes.add(new Attribute("class", fvClassVal));
		allAttributes.add(new Attribute("circleR"));
		allAttributes.add(new Attribute("rectR1"));
		allAttributes.add(new Attribute("rectR2"));
		allAttributes.add(new Attribute("rectR3"));
		allAttributes.add(new Attribute("triR3"));
		allAttributes.add(new Attribute("aspectR"));
		allAttributes.add(new Attribute("fillingR"));
		allAttributes.add(new Attribute("eqR1"));
		allAttributes.add(new Attribute("MovY"));
		allAttributes.add(new Attribute("chR2"));
		allAttributes.add(new Attribute("bbchR"));
		allAttributes.add(new Attribute("quad1FillR"));
		allAttributes.add(new Attribute("quad2FillR"));
		allAttributes.add(new Attribute("quad3FillR"));
		allAttributes.add(new Attribute("quad4FillR"));
		allAttributes.add(new Attribute("rms"));
		
		dataset = new Instances("mCALI", allAttributes, 0);           
		dataset.setClassIndex(0);
	}
	
	public void addExample(NewGesture g) {
		NewFeatures f = g.getFeatures();
		
		Instance inst = new DenseInstance(numAttributes);
		inst.setDataset(dataset);
		//inst.setClassValue(g.getName());~
		inst.setValue(0, g.getName());
		inst.setValue(1, f.getCircleR());
		inst.setValue(2, f.getRectR1());
		inst.setValue(3, f.getRectR2());
		inst.setValue(4, f.getRectR3());
		inst.setValue(5, f.getTriR3());
		inst.setValue(6, f.getAspectR());
		inst.setValue(7, f.getFillingR());
		inst.setValue(8, f.getEqR1());
		inst.setValue(9, f.getMovementY());
		inst.setValue(10, f.getChR2());
		inst.setValue(11, f.getBbchR());
		inst.setValue(12, f.getQuad1FillR());
		inst.setValue(13, f.getQuad2FillR());
		inst.setValue(14, f.getQuad3FillR());
		inst.setValue(15, f.getQuad4FillR());
		inst.setValue(16, f.getRMS());
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
		return classifier.classify(g.getFeatures());
	}

	public String classify(Instance test) {
		return classifier.classify(test);
	}
}
