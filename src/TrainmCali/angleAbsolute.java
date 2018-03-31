package TrainmCali;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import mcali.Gesture;
import mcali.Point;
import mcali.Recognizer;
import mcali.Writer;

public class angleAbsolute {

	public static ArrayList<Double> file;
	public static ArrayList<MyGesture> allGesturesTarget = new ArrayList<>();
	public static ArrayList<MyGesture> allGesturesNTarget = new ArrayList<>();
	public static int total = 179200;
	public static int totalTarget = 4900;
	public static PrintWriter pw ;
	public static StringBuilder sb;

	public static void main(String[] args) throws IOException, OpenXML4JException, SAXException {


		File location = new File(args[0]);
		for (File string : location.listFiles()) {
			file = new ArrayList<>();

			//File file = new File(args[0]);
			OPCPackage pkg = OPCPackage.open(string);
			XSSFReader r = new XSSFReader( pkg );
			SharedStringsTable sst = r.getSharedStringsTable();

			XMLReader parser = fetchSheetParser(sst);

			// To look up the Sheet Name / Sheet Order / rID,
			//  you need to process the core Workbook stream.
			// Normally it's of the form rId# or rSheet#
			InputStream sheet2 = r.getSheet("rId1");
			InputSource sheetSource = new InputSource(sheet2);
			parser.parse(sheetSource);
			sheet2.close();

			for (int i = 0; i < file.size(); i = i + 256){
				MyGesture myG = new MyGesture("Target", -1);
				for (int a = 0; a < 256; a++){
					double sd = Math.atan(file.get(i+a)/  normalise(a, 0, 256) );
					myG.addYpoints((int)(sd * 500));

				}
				allGesturesTarget.add(myG);
			}

			//System.out.println(file.get(0).get(0) + " --" +file.get(1).get(0) + " -- " +file.get(2).get(0));

			//System.out.println(tD.getpart3(0).get(0) + " --" +tD.getpart3(1).get(0));
		}
		System.out.println("leitura de targets feita");
		File location2 = new File(args[1]);
		for (File string : location2.listFiles()) {
			try{
				file = new ArrayList<>();

				OPCPackage pkg1 = OPCPackage.open(string);
				XSSFReader r1 = new XSSFReader( pkg1 );
				SharedStringsTable sst1 = r1.getSharedStringsTable();

				XMLReader parser1 = fetchSheetParser(sst1);

				// To look up the Sheet Name / Sheet Order / rID,
				//  you need to process the core Workbook stream.
				// Normally it's of the form rId# or rSheet#
				InputStream sheet21 = r1.getSheet("rId1");
				InputSource sheetSource1 = new InputSource(sheet21);
				parser1.parse(sheetSource1);
				sheet21.close();
				for (int i = 0; i < file.size(); i = i + 256){
					MyGesture myG = new MyGesture("NTarget", -1);
					for (int a = 0; a < 256; a++){
						//tD.addPart3(b, file.get(b).get(i+a));
						double sd = Math.atan(file.get(i+a)/  normalise(a, 0, 256) );
						myG.addYpoints((int)(sd * 500));

					}
					allGesturesNTarget.add(myG);
				}

			}catch (Exception e){

			}
		}
		System.out.println("leitura de Ntargets feita");
		HashMap<String,ArrayList<MyGesture>> allGOrder = new HashMap<>();
		allGOrder.put("Target", allGesturesTarget);
		allGOrder.put("NTarget", allGesturesNTarget);
		pw = new PrintWriter(new File("AngClassiUnicoPorTempo.csv"));
		sb = new StringBuilder();
		sb.append("Temp--Elec, result\n");
		sb.append("0--1000,");
		mCaliClassifier(allGOrder, 0, 256);
		//sb.append("0--600,");
		//mCaliClassifier(allGOrder, 0, 156);
		//sb.append("250--600,");
		//mCaliClassifier(allGOrder, 64, 156);
		pw.write(sb.toString());
		pw.close();

	}




	public static void mCaliClassifier(HashMap<String,ArrayList<MyGesture>> stuff, int from, int to){
		List<String> s = new ArrayList<String>();
		s.add("Target");
		s.add("NTarget");
		Recognizer mcali = new Recognizer("Vote 1", s);
		ArrayList<MyGesture> t = stuff.get("Target");
		Writer w = new Writer("coisamaluca");
		for (int i = 0; i < t.size(); i = i + 700){
			for (int b = 0; b < 500; b++){
				Gesture g = new Gesture(t.get(i+b).getTypename());
				ArrayList<Integer> valY = t.get(i+b).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);
				}
				g.finalizeStroke();
				g.calcFeatures();
				w.addGesture(g);
				mcali.addExample(g);
			}
			//System.out.println("Cliente ");

		}
		ArrayList<MyGesture> nT =  stuff.get("NTarget");
		for (int i = 0; i < nT.size() ; i = i + 700){
			for (int b = 0; b < 500; b++){

				Gesture g = new Gesture(nT.get(i+b).getTypename());
				ArrayList<Integer> valY = nT.get(i+b).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);			
				}
				g.finalizeStroke();
				g.calcFeatures();
				w.addGesture(g);
				mcali.addExample(g);
			}

		}
		mcali.trainClassifier();
		System.out.println("Classificador feito");
		w.close();
		HashMap<String, Integer> targetResults = new HashMap<>();
		targetResults.put("Target", 0);
		targetResults.put("NTarget", 0);
		HashMap<String, Integer> ntargetResults = new HashMap<>();
		ntargetResults.put("Target", 0);
		ntargetResults.put("NTarget", 0);

		for (int i = 0; i < t.size(); i = i +700){
			for ( int b = 500 ; b < 700; b++){
				Gesture g = new Gesture();
				ArrayList<Integer> valY = t.get(i+b).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);
				}
				g.finalizeStroke();
				g.calcFeatures();
				String result = mcali.classify(g);
				targetResults.put(result, targetResults.get(result) +1 );
			}
		}

		for (int i = 0; i < nT.size(); i=i +700){
			for (int b = 500; b < 700; b++){
				Gesture g = new Gesture();
				ArrayList<Integer> valY = nT.get(i+b).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);
				}
				g.finalizeStroke();
				g.calcFeatures();
				String result = mcali.classify(g);

				ntargetResults.put(result, ntargetResults.get(result) +1 );
			}
		}
		
		double tCacl = (double)targetResults.get("Target")/ (double)(targetResults.get("Target") + targetResults.get("NTarget"));
		double ntCacl = (double)ntargetResults.get("NTarget")/(double)(ntargetResults.get("Target") + ntargetResults.get("NTarget"));
		int targetPercentage = (int) (tCacl * 100);
		int ntargetPercentage = (int)(ntCacl * 100);

		sb.append(((targetPercentage + ntargetPercentage)/2)+"\n");

	}



	public static double normalise(float inValue, float min, float max) {
		return (inValue - min)/(max - min);
	}

	public static XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser =
				XMLReaderFactory.createXMLReader(
						"org.apache.xerces.parsers.SAXParser"
						);
		ContentHandler handler = new SheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}

	/** 
	 * See org.xml.sax.helpers.DefaultHandler javadocs 
	 */
	private static class SheetHandler extends DefaultHandler {
		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;
		private Integer position = 0;
		private Integer countCell = 0;
		private Integer totalLines = 0;
		private Double sumElect = 0.0;

		private SheetHandler(SharedStringsTable sst) {
			this.sst = sst;
		}

		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			// c => cell
			if(name.equals("c")) {
				// Print the cell reference
				//System.out.print( attributes.getValue("r") + " - ");
				// Figure out if the value is an index in the SST
				String cellType = attributes.getValue("t");
				if(cellType != null && cellType.equals("s")) {
					nextIsString = true;
				} else {
					nextIsString = false;
				}
			}
			// Clear contents cache
			lastContents = "";
		}

		public void endElement(String uri, String localName, String name)
				throws SAXException {
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if(nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}

			// v => contents of a cell
			// Output after we've seen the string contents
			if(name.equals("v")) {

				try{
					if (totalLines < total){
						sumElect += Double.valueOf(lastContents);
						countCell++;
						//System.out.println(countCell + " .. " +lastContents);
						if (countCell == 8){
							file.add(sumElect/8.0);
							countCell=0;
							position++;
							totalLines++;
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			lastContents += new String(ch, start, length);
		}
	}

}
