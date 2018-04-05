package classificador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.meta.RandomSubSpace;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

public class NewClassifying implements Serializable{

	private static final long serialVersionUID = -3348566270290979887L;

	private File datasetFile;
	private Classifier currentClassifier;
	private Instances training;

	public NewClassifying(String classifier, File loadedFile) {
		datasetFile = loadedFile;

		Vote v = new Vote();
		v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
		RandomSubSpace rs = new RandomSubSpace();
		rs.setClassifier(new RandomForest());

		if (classifier.equals("Vote 1")) {
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			v.setClassifiers(new Classifier[]{new IBk(), rs, lb});
		}
		else {
			v.setClassifiers(new Classifier[]{new IBk(), rs, new NaiveBayes()});
		}

		setCurrentClassifier(v);
		createTrainingInstances();
		trainClassifier(training);
	}

	public NewClassifying(String classifier) {
		Vote v = new Vote();
		v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
		RandomSubSpace rs = new RandomSubSpace();
		rs.setClassifier(new RandomForest());

		if (classifier.equalsIgnoreCase("Vote 1")) {
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			v.setClassifiers(new Classifier[]{new IBk(), rs, lb});
		}
		else if (classifier.equalsIgnoreCase("vote 2")) {
			v.setClassifiers(new Classifier[]{new IBk(), rs, new NaiveBayes()});
		}

		setCurrentClassifier(v);
	}

	public void createTrainingInstances() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(datasetFile), 8192);
			training = new Instances(reader);
			// Setting class attribute index
			training.setClassIndex(0);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void trainClassifier(Instances train) {
		training = train;
		try {
			currentClassifier.buildClassifier(train);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCurrentClassifier(Classifier classifier) {
		currentClassifier = classifier;
	}

	public String classify(NewFeatures f) {
		// Create empty instance 
		Instance test = new DenseInstance(training.numAttributes());

		// Setting attributes values
		// Index 0 is the class which is not necessary for testing
		test.setValue(1, f.getCircleR());
		test.setValue(2, f.getRectR1());
		test.setValue(3, f.getRectR2());
		test.setValue(4, f.getRectR3());
		test.setValue(5, f.getTriR3());
		test.setValue(6, f.getAspectR());
		test.setValue(7, f.getFillingR());
		test.setValue(8, f.getEqR1());
		test.setValue(9, f.getMovementY());
		test.setValue(10, f.getChR2());
		test.setValue(11, f.getBbchR());
		test.setValue(12, f.getQuad1FillR());
		test.setValue(13, f.getQuad2FillR());
		test.setValue(14, f.getQuad3FillR());
		test.setValue(15, f.getQuad4FillR());
		test.setValue(16, f.getRMS());

		// Setting the header of test to be the same as the training set
		test.setDataset(training);

		try {
			// Distribution Result
			//			double[] results = currentClassifier.distributionForInstance(test);
			//			System.out.println("-------------");
			//			for (int i = 0; i < results.length; i++)
			//				System.out.println(training.classAttribute().value((int) i) + ": " + results[i]);

			// Single Result
			
			double prediction = currentClassifier.classifyInstance(test);
			return training.classAttribute().value((int) prediction);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public String classify(Instance test) {
		try {
			// Single Result
			double prediction = currentClassifier.classifyInstance(test);
			return training.classAttribute().value((int) prediction);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
