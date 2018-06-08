package classificador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.functions.supportVector.RegSMO;
import weka.classifiers.functions.supportVector.RegSMOImproved;
import libsvm.svm_parameter;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.LogitBoost;
import weka.classifiers.meta.RandomSubSpace;
import weka.classifiers.meta.Stacking;
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

	public NewClassifying(String classifier, File loadedFile) throws Exception {
		datasetFile = loadedFile;

		Vote v = new Vote();
		v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
		RandomSubSpace rs = new RandomSubSpace();
		rs.setClassifier(new RandomForest());

		if (classifier.equals("Vote 1")) {
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			v.setClassifiers(new Classifier[]{new IBk(), rs, lb});
			setCurrentClassifier(v);

		}else if (classifier.equalsIgnoreCase("vote5")){
			v = new Vote();
			v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
			LibSVM svm = new LibSVM();
			svm.setKernelType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,LibSVM.TAGS_KERNELTYPE));
			svm.setCoef0(0);
			svm.setGamma(0);
			svm.setDegree(3);
			Classifier classifierBagging = AbstractClassifier.forName("weka.classifiers.meta.Bagging", new String[]{"-P", "63", "-I", "24", "-S", "1", "-W", "weka.classifiers.rules.JRip", "--", "-N", "3.868189441050318", "-P", "-O", "5"});
			v.setClassifiers(new Classifier[]{AbstractClassifier.forName("weka.classifiers.trees.RandomForest", new String[]{"-I", "159", "-K", "26", "-depth", "0"}), svm, classifierBagging});
			setCurrentClassifier(v);

		}else if(classifier.equalsIgnoreCase("vote2")) {
			v.setClassifiers(new Classifier[]{new IBk(), rs, new NaiveBayes()});
			setCurrentClassifier(v);

		}else if (classifier.equalsIgnoreCase("SVMRBF09")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(0.9);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVMNor13")) {
			new RBFKernel();
			SMO sm = new SMO();
			NormalizedPolyKernel npk = new NormalizedPolyKernel();
			npk.setExponent(13);
			sm.setKernel(npk);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVMNor18")) {
			new RBFKernel();
			SMO sm = new SMO();
			NormalizedPolyKernel npk = new NormalizedPolyKernel();
			npk.setExponent(18);
			sm.setKernel(npk);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVMNor14")) {
			new RBFKernel();
			SMO sm = new SMO();
			
			NormalizedPolyKernel npk = new NormalizedPolyKernel();
			npk.setExponent(14);
			sm.setKernel(npk);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVMRBF1")) {
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(1);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}
		
		
		createTrainingInstances();
		trainClassifier(training);
	}

	public NewClassifying(String classifier) throws Exception {
		Vote v = new Vote();
		v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
		

		if (classifier.equalsIgnoreCase("Vote 1")) {
			RandomSubSpace rs = new RandomSubSpace();
			rs.setClassifier(new RandomForest());
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			v.setClassifiers(new Classifier[]{new IBk(), rs, lb});
			setCurrentClassifier(v);

		}
		else if (classifier.equalsIgnoreCase("vote 2")) {
			RandomSubSpace rs = new RandomSubSpace();
			rs.setClassifier(new RandomForest());
			v.setClassifiers(new Classifier[]{new IBk(), rs, new NaiveBayes()});
			setCurrentClassifier(v);
		}else if (classifier.equalsIgnoreCase("vote3")) {
			v.setClassifiers(new Classifier[] {new RandomForest(), new SMO(), new Bagging()});
			setCurrentClassifier(v);
		}else if (classifier.equalsIgnoreCase("vote4")) {
			v.setClassifiers(new Classifier[] {new AdaBoostM1(), new SMO(), new Bagging()});
			setCurrentClassifier(v);
		}else if (classifier.equalsIgnoreCase("vote5")) {
			v.setClassifiers(new Classifier[] {new RandomForest(), new SMO(), new AdaBoostM1()});
			setCurrentClassifier(v);
		}else if (classifier.equalsIgnoreCase("vote6")) {
			v.setClassifiers(new Classifier[] {new RandomForest(), new AdaBoostM1(), new Bagging()});
			setCurrentClassifier(v);
		}else if (classifier.equalsIgnoreCase("vote7")) {
			v.setClassifiers(new Classifier[] {new RandomForest(), new SMO(), new LogitBoost()});
			setCurrentClassifier(v);
		}else if (classifier.equalsIgnoreCase("Random")) {
			setCurrentClassifier(new RandomForest());
		}else if (classifier.equalsIgnoreCase("NaiveBayes")) {
			setCurrentClassifier(new NaiveBayes());
		}else if (classifier.equalsIgnoreCase("BayesNet")) {
			setCurrentClassifier(new BayesNet());
		}else if (classifier.equalsIgnoreCase("LogitBoost")) {
			setCurrentClassifier(new LogitBoost());
		}else if (classifier.equalsIgnoreCase("AdaBoost")) {
			setCurrentClassifier(new AdaBoostM1());
		}else if (classifier.equalsIgnoreCase("Bagging")) {
			setCurrentClassifier(new Bagging());
		}else if (classifier.equalsIgnoreCase("SVM1")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(1);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVM5")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(5);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVM10")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(10);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVM20")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(20);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVM30")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(30);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVM40")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(40);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVM50")) {
			new RBFKernel();
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(50);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVM1")) {
			new RBFKernel();
			SMO sm = new SMO();
			NormalizedPolyKernel npk = new NormalizedPolyKernel();
			npk.setExponent(2);
			sm.setKernel(npk);
			setCurrentClassifier(sm);
		}else if (classifier.equalsIgnoreCase("SVMRBF1")) {
			SMO sm = new SMO();
			RBFKernel ker = new RBFKernel();
			ker.setGamma(1);
			sm.setKernel(ker);
			setCurrentClassifier(sm);
		}

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
		test.setValue(1, f.getRectR1());
		test.setValue(2, f.getRectR2());
		test.setValue(3, f.getRectR3());
		test.setValue(4, f.getEqR1());
		test.setValue(5, f.getMovementY());
		test.setValue(6, f.getChR2());
		test.setValue(7, f.getChR3());
		test.setValue(8, f.getBbchR());
		test.setValue(9, f.getQuad2FillR());
		test.setValue(10, f.getQuad3FillR());
		test.setValue(11, f.getQuad4FillR());
		test.setValue(12, f.getAstchAR());
		test.setValue(13, f.getAstchPR());
		test.setValue(14, f.getAa1());
		test.setValue(15, f.getAa2());
		test.setValue(16, f.getAa3());
		test.setValue(17, f.getAa4());
		test.setValue(18, f.getRa1());
		test.setValue(19, f.getRa2());
		test.setValue(20, f.getRa3());
		test.setValue(21, f.getRa4());
		test.setValue(22, f.getAstchAB());
		test.setValue(23, f.getRMS());
		test.setValue(24, f.getFillingR());
		
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
