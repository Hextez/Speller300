package Stuff;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class runner {

	public static HashMap<Integer, HashMap<Integer,Double>> file;


	public static void main(String[] args) throws IOException, OpenXML4JException, SAXException {
		// TODO Auto-generated method stub
		file = new HashMap<>();
		file.put(0, new HashMap<>());
		file.put(1, new HashMap<>());
		file.put(2, new HashMap<>());
		file.put(3, new HashMap<>());
		file.put(4, new HashMap<>());
		file.put(5, new HashMap<>());
		file.put(6, new HashMap<>());
		file.put(7, new HashMap<>());

		ArrayList<Double> toPass = new ArrayList<>();
		//File file = new File(args[0]);
		OPCPackage pkg = OPCPackage.open(args[0]);
		XSSFReader r = new XSSFReader( pkg );
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		// To look up the Sheet Name / Sheet Order / rID,
		//  you need to process the core Workbook stream.
		// Normally it's of the form rId# or rSheet#
		InputStream sheet2 = r.getSheet("rId10");
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
		int x = 0;
		int total = file.get(0).size();
		System.out.println(total);
		int totalN = 0;
		int totalU = 0;
		for ( int a = 0; a < file.get(0).size() / 256; a++){

			HashMap<Integer, Double> rs = new HashMap<>();
			int i = x;
			while (i < x + 256){
				rs.put(i-x, file.get(0).get(i));
				i++;
			}
			x = x + 256;
			
			HashMap<Integer, Double> teste1 = new Inflexion(rs).getNpoint();
			HashMap<Integer, Double> teste2 = new Inflexion(rs).getUpoint();
			totalN = teste1.size();
			totalU = teste2.size();
			//System.out.println(totalN);
			//System.out.println(totalU);
			
			CentralMoments cM = new CentralMoments(rs);
			System.out.println("Mean "+ cM.pointsMean());
			System.out.println("Standard "+cM.standardDeviation());
			System.out.println("Skewness "+cM.skewness());
			System.out.println(cM.skewness() / cM.standardDeviation());
			System.out.println("------");
		}
		//System.out.println(totalN);
		//System.out.println(totalN+totalU);
		//System.out.println(new Float( totalN / (totalN + totalU)));

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
				if (countCell < 8){
					file.get(countCell).put(position, Double.valueOf(lastContents));
					countCell++;
				}else{
					countCell =0;
					position++;
					System.out.println(position);
				}
				//System.out.println(lastContents);
			}
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			lastContents += new String(ch, start, length);
		}
	}

}
