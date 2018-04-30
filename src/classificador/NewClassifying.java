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
import libsvm.svm_parameter;
import weka.classifiers.lazy.IBk;
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


		if (classifier.equals("Random")){
			setCurrentClassifier(new RandomForest());

		}else if (classifier.equals("SVM")){
			LibSVM svm = new LibSVM();
			svm.setKernelType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,LibSVM.TAGS_KERNELTYPE));
			svm.setCoef0(0);
			svm.setGamma(0);
			svm.setDegree(3);
			setCurrentClassifier(svm);

		}else {
			setCurrentClassifier(v);
		}
		createTrainingInstances();
		trainClassifier(training);
	}

	public NewClassifying(String classifier) throws Exception {
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
		}else if (classifier.equalsIgnoreCase("vote3")){
			v = new Vote();
			v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
			LibSVM svm = new LibSVM();
			svm.setKernelType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,LibSVM.TAGS_KERNELTYPE));
			svm.setCoef0(0);
			svm.setGamma(0);
			svm.setDegree(3);
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			Classifier classifierBagging = AbstractClassifier.forName("weka.classifiers.meta.Bagging", new String[]{"-P", "63", "-I", "24", "-S", "1", "-W", "weka.classifiers.rules.JRip", "--", "-N", "3.868189441050318", "-P", "-O", "5"});
			v.setClassifiers(new Classifier[]{lb, svm, classifierBagging});

			
		}else if (classifier.equalsIgnoreCase("vote4")){
			v = new Vote();
			v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			Classifier classifierAdaBoost = AbstractClassifier.forName("weka.classifiers.meta.AdaBoostM1", new String[]{"-P", "80", "-I", "56", "-S", "1", "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "35", "-K", "0", "-depth", "19"});
			Classifier classifierBagging = AbstractClassifier.forName("weka.classifiers.meta.Bagging", new String[]{"-P", "63", "-I", "24", "-S", "1", "-W", "weka.classifiers.rules.JRip", "--", "-N", "3.868189441050318", "-P", "-O", "5"});

			v.setClassifiers(new Classifier[]{lb, classifierAdaBoost, classifierBagging});
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

		}else if (classifier.equalsIgnoreCase("vote6")){
			v = new Vote();
			v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
			LibSVM svm = new LibSVM();
			svm.setKernelType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,LibSVM.TAGS_KERNELTYPE));
			svm.setCoef0(0);
			svm.setGamma(0);
			svm.setDegree(3);
			Classifier classifierAdaBoost = AbstractClassifier.forName("weka.classifiers.meta.AdaBoostM1", new String[]{"-P", "80", "-I", "56", "-S", "1", "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "35", "-K", "0", "-depth", "19"});
			Classifier classifierBagging = AbstractClassifier.forName("weka.classifiers.meta.Bagging", new String[]{"-P", "63", "-I", "24", "-S", "1", "-W", "weka.classifiers.rules.JRip", "--", "-N", "3.868189441050318", "-P", "-O", "5"});
			v.setClassifiers(new Classifier[]{classifierAdaBoost, svm, classifierBagging});

		}else if (classifier.equalsIgnoreCase("vote7")){
			v = new Vote();
			v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
			LibSVM svm = new LibSVM();
			svm.setKernelType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,LibSVM.TAGS_KERNELTYPE));
			svm.setCoef0(0);
			svm.setGamma(0);
			svm.setDegree(3);
			Classifier classifierBagging = AbstractClassifier.forName("weka.classifiers.meta.Bagging", new String[]{"-P", "63", "-I", "24", "-S", "1", "-W", "weka.classifiers.rules.JRip", "--", "-N", "3.868189441050318", "-P", "-O", "5"});
			v.setClassifiers(new Classifier[]{new NaiveBayes(), svm, classifierBagging});

		}else if (classifier.equalsIgnoreCase("vote8")){
			v = new Vote();
			v.setCombinationRule(new SelectedTag(Vote.MAJORITY_VOTING_RULE,Vote.TAGS_RULES));
			LibSVM svm = new LibSVM();
			svm.setKernelType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,LibSVM.TAGS_KERNELTYPE));
			svm.setCoef0(0);
			svm.setGamma(0);
			svm.setDegree(3);
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			Classifier classifierBagging = AbstractClassifier.forName("weka.classifiers.meta.Bagging", new String[]{"-P", "63", "-I", "24", "-S", "1", "-W", "weka.classifiers.rules.JRip", "--", "-N", "3.868189441050318", "-P", "-O", "5"});
			v.setClassifiers(new Classifier[]{new NaiveBayes(), svm, classifierBagging,lb, new IBk() });

		}

		if (classifier.equalsIgnoreCase("Random")){
			setCurrentClassifier(AbstractClassifier.forName("weka.classifiers.trees.RandomForest", new String[]{"-I", "159", "-K", "26", "-depth", "0"}));
		}else if (classifier.equalsIgnoreCase("SVM")){
			LibSVM svm = new LibSVM();
			svm.setKernelType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,LibSVM.TAGS_KERNELTYPE));
			svm.setCoef0(0);
			svm.setGamma(0);
			svm.setDegree(3);
			
			setCurrentClassifier(svm);
		}else if (classifier.equalsIgnoreCase("Bagging")){
			Classifier classifierBagging = AbstractClassifier.forName("weka.classifiers.meta.Bagging", new String[]{"-P", "63", "-I", "24", "-S", "1", "-W", "weka.classifiers.rules.JRip", "--", "-N", "3.868189441050318", "-P", "-O", "5"});
			setCurrentClassifier(classifierBagging);
		}else if (classifier.equalsIgnoreCase("AdaBoost")){
			Classifier classifierAdaBoost = AbstractClassifier.forName("weka.classifiers.meta.AdaBoostM1", new String[]{"-P", "80", "-I", "56", "-S", "1", "-W", "weka.classifiers.trees.RandomForest", "--", "-I", "35", "-K", "0", "-depth", "19"});
			setCurrentClassifier(classifierAdaBoost);
		}else if(classifier.equalsIgnoreCase("BayesNet")){
			setCurrentClassifier(new BayesNet());
		}else if (classifier.equalsIgnoreCase("LogitBoost")){
			LogitBoost lb = new LogitBoost();
			lb.setClassifier(new RandomForest());
			setCurrentClassifier(lb);
		}else if (classifier.equalsIgnoreCase("NaiveBayes")){
			setCurrentClassifier(new NaiveBayes());
		}else if (classifier.equalsIgnoreCase("SubRandom")){
			rs = new RandomSubSpace();
			rs.setClassifier(new RandomForest());
			setCurrentClassifier(rs);
		}else {
			setCurrentClassifier(v);
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

	public String classify(NewFeatures f, int numFeatures) {
		// Create empty instance 
		Instance test = new DenseInstance(training.numAttributes());

		// Setting attributes values
		// Index 0 is the class which is not necessary for testing
		if (numFeatures == 15){
			test.setValue(1, f.getCircleR());
			test.setValue(2, f.getRectR1());
			test.setValue(3, f.getRectR2());
			test.setValue(4, f.getRectR3());
			test.setValue(5, f.getRectR4());
			test.setValue(6, f.getTriR3());
			test.setValue(7, f.getAspectR());
			test.setValue(8, f.getEqR1());
			test.setValue(9, f.getMovementY());
			test.setValue(10, f.getChR2());
			test.setValue(11, f.getBbchR());
			test.setValue(12, f.getAstchAR());
			test.setValue(13, f.getAstchPR());
			test.setValue(14, f.getRMS());
		}else if (numFeatures == 13){
			test.setValue(1, f.getCircleR());
			test.setValue(2, f.getRectR2());
			test.setValue(3, f.getRectR3());
			test.setValue(4, f.getAspectR());
			test.setValue(5, f.getEqR1());
			test.setValue(6, f.getMovementY());
			test.setValue(7, f.getChR2());
			test.setValue(8, f.getChR3());
			test.setValue(9, f.getBbchR());
			test.setValue(10, f.getAstchAR());
			test.setValue(11, f.getAstchPR());
			test.setValue(12, f.getRMS());
		}else if (numFeatures == 11){
			test.setValue(1, f.getRectR2());
			test.setValue(2, f.getAspectR());
			test.setValue(3, f.getEqR1());
			test.setValue(4, f.getMovementY());
			test.setValue(5, f.getChR2());
			test.setValue(6, f.getChR3());
			test.setValue(7, f.getBbchR());
			test.setValue(8, f.getAstchAR());
			test.setValue(9, f.getAstchPR());
			test.setValue(10, f.getRMS());
		}else if (numFeatures == 8){
			test.setValue(1, f.getRectR2());
			test.setValue(2, f.getEqR1());
			test.setValue(3, f.getChR2());
			test.setValue(4, f.getBbchR());
			test.setValue(5, f.getAstchAR());
			test.setValue(6, f.getAstchPR());
			test.setValue(7, f.getRMS());
		}
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
