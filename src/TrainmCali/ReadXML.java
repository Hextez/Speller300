package TrainmCali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mcali.Gesture;
import mcali.Point;
import mcali.Recognizer;


public class ReadXML {
	

	
	public static void main(String[] args) throws IOException{
		ArrayList<GestureInfo> rec = getGesturesFrom("C:\\Users\\Eu\\Desktop\\final.xml");
		ArrayList<GestureInfo> val = getGesturesFrom("C:\\Users\\Eu\\Desktop\\final.xml");
		//ArrayList<GestureInfo> val = getGesturesFrom("C:\\Users\\Eu\\Desktop\\valif.xml");
		recog(rec,val);
		
		//rec.addAll(val);
		System.out.println(".....................");
		System.out.println(".....................");
		recogFile("C:\\Users\\Eu\\Desktop\\final.arff", rec);

	}
	
	public static void recogFile(String name, ArrayList<GestureInfo> toVal){ //testado com o ficheiro ARFF 100% correcto
		Recognizer mcali = new Recognizer("TESTE",new File(name));
		
		
		for (GestureInfo gesture : toVal) {
			Gesture g = new Gesture();
			ArrayList<Integer> valX = gesture.getXpoints();
			ArrayList<Integer> valY = gesture.getYpoints();
			for (int i = 0; i < valX.size(); i++) {
				g.addPoint(new Point(valX.get(i),valY.get(i)));
			}
			g.finalizeStroke();
			String result = mcali.classify(g);
			System.out.println(gesture.getTypename() + " " + gesture.getONE() + " result is "+ result);
		}
	}
	
	public static void recog(ArrayList<GestureInfo> toRec, ArrayList<GestureInfo> toVal){ //alguma coisa com a lista de classes ....
		List<String> s = new ArrayList<String>();
		s.add("Square");
		s.add("Tri");
		Recognizer mcali = new Recognizer("Vote 1", s);
		
		for (GestureInfo gesture : toRec) {
			Gesture g = new Gesture(gesture.getTypename());
			ArrayList<Integer> valX = gesture.getXpoints();
			ArrayList<Integer> valY = gesture.getYpoints();
			for (int i = 0; i < valX.size(); i++) {
				g.addPoint(new Point(valX.get(i),valY.get(i)));
			}
			g.finalizeStroke();
			g.calcFeatures();
			mcali.addExample(g);
		}
		
		mcali.trainClassifier();
		
		
		for (GestureInfo gesture : toVal) {
			Gesture g = new Gesture();
			ArrayList<Integer> valX = gesture.getXpoints();
			ArrayList<Integer> valY = gesture.getYpoints();
			for (int i = 0; i < valX.size(); i++) {
				g.addPoint(new Point(valX.get(i),valY.get(i)));
			}
			g.finalizeStroke();
			String result = mcali.classify(g);
			System.out.println(gesture.getTypename()  + " " + gesture.getONE()+ " result is "+ result);
		}
	}
	
	
	public static ArrayList<GestureInfo> getGesturesFrom(String filename) throws NumberFormatException, IOException{
		
		boolean getPoints = false;
		ArrayList<GestureInfo> te = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		
		try {
			StringBuilder sb = new StringBuilder();
			String line;
			GestureInfo ges = null;
			int s = 0;
			while ((line = br.readLine()) != null){
				String[] f1 = line.split("<");
				String[] f = f1[1].split(" ");
				if (f[0].equals("Gesture") && f.length > 3){
					ges = new GestureInfo(f[1].substring(6, f[1].length()-1),s);
					getPoints = true;
					s++;
				}else if (f[0].equals("/Gesture>") && f.length == 1){
					te.add(ges);
					getPoints = false;
				}
				
				if (getPoints == true && f[0].equals("Point")){
					ges.addXpoints(Integer.parseInt(f[1].substring(3, f[1].length()-1)));
					ges.addYpoints(Integer.parseInt(f[2].substring(3, f[2].length()-1)));
				}
			}
		}finally{
			br.close();
		}
		

		return te;
	}

}
