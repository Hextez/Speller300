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
	private int numAttributes = 25;
	private ArrayList<Attribute> allAttributes;
	private Instances dataset;

	public NewRecognizer(String classifierName, File trainingFile) throws Exception {
		classifier = new NewClassifying(classifierName, trainingFile);
	}

	public NewRecognizer(String classifierName, List<String> allClasses) throws Exception {
		classifier = new NewClassifying(classifierName);

		ArrayList<String> fvClassVal = new ArrayList<String>();
		for (String cls : allClasses)
			fvClassVal.add(cls);

		allAttributes = new ArrayList<Attribute>(numAttributes);
		allAttributes.add(new Attribute("class", fvClassVal));
		allAttributes.add(new Attribute("rectR1"));
		allAttributes.add(new Attribute("rectR2"));
		allAttributes.add(new Attribute("rectR3"));
		allAttributes.add(new Attribute("eqR1"));
		allAttributes.add(new Attribute("MovY"));
		allAttributes.add(new Attribute("chR2"));
		allAttributes.add(new Attribute("chR3"));
		allAttributes.add(new Attribute("bbchR"));
		allAttributes.add(new Attribute("quad2FillR"));
		allAttributes.add(new Attribute("quad3FillR"));
		allAttributes.add(new Attribute("quad4FillR"));
		allAttributes.add(new Attribute("astchAR"));
		allAttributes.add(new Attribute("astchPR"));
		allAttributes.add(new Attribute("aa1"));
		allAttributes.add(new Attribute("aa2"));
		allAttributes.add(new Attribute("aa3"));
		allAttributes.add(new Attribute("aa4"));
		allAttributes.add(new Attribute("ra1"));
		allAttributes.add(new Attribute("ra2"));
		allAttributes.add(new Attribute("ra3"));
		allAttributes.add(new Attribute("ra4"));
		allAttributes.add(new Attribute("astchAB"));
		allAttributes.add(new Attribute("rms"));
		allAttributes.add(new Attribute("fillingR"));
		
		dataset = new Instances("mCALI", allAttributes, 0);           
		dataset.setClassIndex(0);
	}

	public void addExample(NewGesture g) {
		NewFeatures f = g.getFeatures();

		Instance inst = new DenseInstance(numAttributes);
		inst.setDataset(dataset);
		//inst.setClassValue(g.getName());
		inst.setValue(0, g.getName());
		inst.setValue(1, f.getRectR1());
		inst.setValue(2, f.getRectR2());
		inst.setValue(3, f.getRectR3());
		inst.setValue(4, f.getEqR1());
		inst.setValue(5, f.getMovementY());
		inst.setValue(6, f.getChR2());
		inst.setValue(7, f.getChR3());
		inst.setValue(8, f.getBbchR());
		inst.setValue(9, f.getQuad2FillR());
		inst.setValue(10, f.getQuad3FillR());
		inst.setValue(11, f.getQuad4FillR());
		inst.setValue(12, f.getAstchAR());
		inst.setValue(13, f.getAstchPR());
		inst.setValue(14, f.getAa1());
		inst.setValue(15, f.getAa2());
		inst.setValue(16, f.getAa3());
		inst.setValue(17, f.getAa4());
		inst.setValue(18, f.getRa1());
		inst.setValue(19, f.getRa2());
		inst.setValue(20, f.getRa3());
		inst.setValue(21, f.getRa4());
		inst.setValue(22, f.getAstchAB());
		inst.setValue(23, f.getRMS());
		inst.setValue(24, f.getFillingR());
		
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
