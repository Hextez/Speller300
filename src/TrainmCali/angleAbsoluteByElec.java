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

public class angleAbsoluteByElec {

	public static HashMap<Integer, ArrayList<Double>> file;
	public static ArrayList<MyGesture> allGesturesTarget = new ArrayList<>();
	public static ArrayList<MyGesture> allGesturesNTarget = new ArrayList<>();
	public static int total = 179200;
	public static PrintWriter pw ;
	public static StringBuilder sb;

	public static void main(String[] args) throws IOException, OpenXML4JException, SAXException {


		File location = new File(args[0]);
		for (File string : location.listFiles()) {
			file = new HashMap<>();
			file.put(0, new ArrayList<>());
			file.put(1, new ArrayList<>());
			file.put(2, new ArrayList<>());
			file.put(3, new ArrayList<>());
			file.put(4, new ArrayList<>());
			file.put(5, new ArrayList<>());
			file.put(6, new ArrayList<>());
			file.put(7, new ArrayList<>());

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

			for (int b = 0 ; b < 8; b++){
				//System.out.println(b);
				for (int i = 0; i < file.get(b).size(); i = i + 256){
					MyGesture myG = new MyGesture("Target", b);
					for (int a = 0; a < 256; a++){
						myG.addXpoints(a);
						double sd = Math.atan(file.get(b).get(i+a)/  normalise(a, 0, 256) );
						myG.addYpoints((int)(sd * 500));

					}
					allGesturesTarget.add(myG);
				}
			}
			//System.out.println(file.get(0).get(0) + " --" +file.get(1).get(0) + " -- " +file.get(2).get(0));

			//System.out.println(tD.getpart3(0).get(0) + " --" +tD.getpart3(1).get(0));
		}
		System.out.println("leitura de targets feita");
		File location2 = new File(args[1]);
		for (File string : location2.listFiles()) {
			try{
				file = new HashMap<>();
				file.put(0, new ArrayList<>());
				file.put(1, new ArrayList<>());
				file.put(2, new ArrayList<>());
				file.put(3, new ArrayList<>());
				file.put(4, new ArrayList<>());
				file.put(5, new ArrayList<>());
				file.put(6, new ArrayList<>());
				file.put(7, new ArrayList<>());

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
				for (int b = 0 ; b < 8; b++){
					for (int i = 0; i < file.get(b).size(); i = i + 256){
						MyGesture myG = new MyGesture("NTarget", b);
						for (int a = 0; a < 256; a++){
							//tD.addPart3(b, file.get(b).get(i+a));
							myG.addXpoints(a);
							double sd = Math.atan(file.get(b).get(i+a)/  normalise(a, 0, 256) );
							myG.addYpoints((int)(sd * 500));

						}
						allGesturesNTarget.add(myG);
					}
				}
			}catch (Exception e){

			}
		}
		System.out.println("leitura de Ntargets feita");

		HashMap<Integer, HashMap<String,ArrayList<MyGesture>>> listaFinal = orderIt();
		int inicio = 0;
		int Inifinal = 700;
		pw = new PrintWriter(new File("ResultadoPoAnglo.csv"));
		sb = new StringBuilder();
		sb.append("User, temp, result\n");

		for (int i = 0; i < 8; i ++){

			sb.append("User"+(i+1)+",0-1000,");
			mCaliClassifier(listaFinal , 0, 256, inicio, Inifinal);
			sb.append(",0-600,");
			mCaliClassifier(listaFinal,  0, 156, inicio, Inifinal);
			sb.append(",250-600,");
			mCaliClassifier(listaFinal,  64, 156, inicio, Inifinal);

			inicio += 700;
			Inifinal += 700;
			System.out.println("user "+ (i+1) + " done");
		}
		pw.write(sb.toString());
		pw.close();


	}

	public static HashMap<Integer, HashMap<String,ArrayList<MyGesture>>> orderIt(){
		HashMap<Integer, HashMap<String,ArrayList<MyGesture>>> ret = new HashMap<>();
		for (MyGesture myGesture : allGesturesTarget) {
			if (!ret.containsKey(myGesture.getElec())){
				ret.put(myGesture.getElec(), new HashMap<>());
			}
			if (!ret.get(myGesture.getElec()).containsKey(myGesture.getTypename())){
				ret.get(myGesture.getElec()).put(myGesture.getTypename(), new ArrayList<>());

			}
			ret.get(myGesture.getElec()).get(myGesture.getTypename()).add(myGesture);

		}
		for (MyGesture myGesture : allGesturesNTarget) {
			if (!ret.containsKey(myGesture.getElec())){
				ret.put(myGesture.getElec(), new HashMap<>());
			}
			if (!ret.get(myGesture.getElec()).containsKey(myGesture.getTypename())){
				ret.get(myGesture.getElec()).put(myGesture.getTypename(), new ArrayList<>());

			}
			ret.get(myGesture.getElec()).get(myGesture.getTypename()).add(myGesture);

		}
		return ret;
	}


	public static void mCaliClassifier(HashMap<Integer, HashMap<String,ArrayList<MyGesture>>> listaFinal, int from, int to, int pos, int posfin){
		List<String> s = new ArrayList<String>();
		s.add("Target");
		s.add("NTarget");
		Recognizer mcali = new Recognizer("Vote 1", s);
		for ( int elec = 0; elec < 8 ; elec ++){
			ArrayList<MyGesture> target = listaFinal.get(elec).get("Target");
			for (int i = pos; i < pos+500; i ++){
				Gesture g = new Gesture(target.get(i).getTypename());
				ArrayList<Integer> valY = target.get(i).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);
				}
				g.finalizeStroke();
				g.calcFeatures();
				mcali.addExample(g);
			}
			ArrayList<MyGesture> ntarget = listaFinal.get(elec).get("NTarget");

			for (int i = pos; i < pos+500 ; i ++){
				Gesture g = new Gesture(ntarget.get(i).getTypename());
				ArrayList<Integer> valY = ntarget.get(i).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);			
				}
				g.finalizeStroke();
				g.calcFeatures();
				mcali.addExample(g);
			}
		}

		mcali.trainClassifier();

		HashMap<String, Integer> resultsTarget = new HashMap<>();
		resultsTarget.put("Target", 0);
		resultsTarget.put("NTarget", 0);
		HashMap<String, Integer> resultsNTarget = new HashMap<>();
		resultsNTarget.put("Target", 0);
		resultsNTarget.put("NTarget", 0);
		for ( int elec = 0; elec < 8 ; elec ++){
			ArrayList<MyGesture> target = listaFinal.get(elec).get("Target");
			for (int i = pos+500; i < posfin; i ++){
				Gesture g = new Gesture();
				ArrayList<Integer> valY = target.get(i).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);
				}
				g.finalizeStroke();
				g.calcFeatures();
				String result = mcali.classify(g);
				resultsTarget.put(result, resultsTarget.get(result) + 1);
			}

			ArrayList<MyGesture> ntarget = listaFinal.get(elec).get("NTarget");

			for (int i = pos+500; i < posfin; i ++){
				Gesture g = new Gesture();
				ArrayList<Integer> valY = ntarget.get(i).getYpoints();
				for (int a = from; a < to; a++) {
					Point p = new Point((int)(normalise(a, from, to)*500),valY.get(a));
					g.addPoint(p);
				}
				g.finalizeStroke();
				g.calcFeatures();
				String result = mcali.classify(g);
				resultsNTarget.put(result, resultsNTarget.get(result) + 1);
			}
		}

		double tCacl = (double)resultsTarget.get("Target")/ (double)(resultsTarget.get("Target") + resultsTarget.get("NTarget"));
		double ntCacl = (double)resultsNTarget.get("NTarget")/(double)(resultsNTarget.get("Target") + resultsNTarget.get("NTarget"));
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
						file.get(countCell).add(Double.valueOf(lastContents));
						countCell++;
						//System.out.println(countCell + " .. " +lastContents);
						if (countCell == 8){
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
