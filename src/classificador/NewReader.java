package classificador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import mcali.Gesture;
import mcali.Point;
import mcali.Stroke;
import mcali.Writer;

public class NewReader {
private NewGesture gesture;
	
	public NewReader() {
		gesture = new NewGesture();
	}
	
	public NewGesture getGesture() {
		return gesture;
	}

	public TreeMap<String, List<NewGesture>> readXML(final File file) {
		// TreeMap<ClassName, List<GestureWithClassName>>
		final TreeMap<String, List<NewGesture>> gestures = new TreeMap<String, List<NewGesture>>();

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				NewGesture g;
				double x, y;
				long timestamp;
				String name = "";

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						g = new NewGesture();
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("NAME")) {
								name = attributes.getValue(i);
								g.setName(name);
								if (!gestures.containsKey(name)) {
									gestures.put(name, new ArrayList<NewGesture>());
								}
							}
							else if (attributes.getQName(i).equalsIgnoreCase("GESTID")) {
								g.setGestID(Integer.parseInt(attributes.getValue(i)));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("USER")) {
								g.setUser(attributes.getValue(i));
							}
						}
					}
					if (qName.equalsIgnoreCase("POINT")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("X")) {
								x = Double.parseDouble(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("Y")) {
								y = Double.parseDouble(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("MS")) {
								timestamp = Long.parseLong(attributes.getValue(i));
							}
						}
						g.addPoint(x,y);
					}
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("STROKE")) {
						g.finalizeStroke();
					}
					if (qName.equalsIgnoreCase("GESTURE")) {
						gestures.get(name).add(g);
//						g.calcFeatures();
					}
				}
			};

			saxParser.parse(file.getAbsolutePath(), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return gestures;
	}

	public TreeMap<String, List<NewGesture>> readXMLAndReturnByUser(final File file) {
		// TreeMap<User, List<GestureOfUser>>
		final TreeMap<String, List<NewGesture>> gestures = new TreeMap<String, List<NewGesture>>();

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				NewGesture g;
				double x, y;
				long timestamp;
				String user = "";

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						g = new NewGesture();
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("NAME")) {
								g.setName(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("GESTID")) {
								g.setGestID(Integer.parseInt(attributes.getValue(i)));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("USER")) {
								user = attributes.getValue(i);
								g.setUser(user);
								if (!gestures.containsKey(user)) {
									gestures.put(user, new ArrayList<NewGesture>());
								}
							}
						}
					}
					if (qName.equalsIgnoreCase("POINT")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("X")) {
								x = Double.parseDouble(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("Y")) {
								y = Double.parseDouble(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("MS")) {
								timestamp = Long.parseLong(attributes.getValue(i));
							}
						}
						g.addPoint(x,y);
					}
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("STROKE")) {
						g.finalizeStroke();
					}
					if (qName.equalsIgnoreCase("GESTURE")) {
						gestures.get(user).add(g);
//						g.calcFeatures();
					}
				}
			};

			saxParser.parse(file.getAbsolutePath(), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return gestures;
	}

	public void readMCali(final File file, final NewWriter writer) {
		final String filename = file.getName();
		System.out.println(filename);
	    try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {

				String user = filename.split("\\.")[0];
				NewGesture g;
				int x, y;

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("NAME")) {
								g = new NewGesture(attributes.getValue(i));
								g.setUser(user);
							}
						}
					}
					if (qName.equalsIgnoreCase("POINT")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("X")) {
								x = Integer.parseInt(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("Y")) {
								y = Integer.parseInt(attributes.getValue(i));
							}
						}
						g.addPoint(x, y);
					}
				}
			 
				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("STROKE")) {
						g.finalizeStroke();
					}
					if (qName.equalsIgnoreCase("GESTURE")) {
						gesture = g;
						if (writer != null && ((g.getNumPoints() > 5) || (g.getLength() > 20))) {
							writer.addGesture(g);
						}
					}
				}
			};
		 
			saxParser.parse(file.getAbsolutePath(), handler);
	 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	public void readDollarPN(final File file, final NewWriter writer) {
		final String filename = file.getName();
		System.out.println(filename);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				String user = filename.split("-")[0];
				NewGesture g;
				double x, y;
				Point p;
				String name = "";
				String[] splitName;

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("NAME")) {
								name = attributes.getValue(i);
							}
						}
						g = new NewGesture();
						splitName = name.split("~");
						g.setName(splitName[0]);
						g.setUser(user);
					}
					if (qName.equalsIgnoreCase("POINT")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("X")) {
								x = Integer.parseInt(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("Y")) {
								y = Integer.parseInt(attributes.getValue(i));
							}
						}
						p = new Point(x, y);
						g.addPoint(p);
					}
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("STROKE")) {
						g.finalizeStroke();
					}
					if (qName.equalsIgnoreCase("GESTURE")) {
						gesture = g;
						if (writer != null && ((g.getNumPoints() > 5) || (g.getLength() > 20))) {
							writer.addGesture(g);
						}
					}
				}
			};

			saxParser.parse(file.getAbsolutePath(), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readDollar1(final File file, final NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
	    try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {

				String user = file.getParentFile().getParentFile().getName();
				NewGesture g;
				int x, y;
				Point p;
				String name = "";
				
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("NAME")) {
								name = attributes.getValue(i);
							}
						}
						g = new NewGesture();
						name = name.substring(0, name.length() - 2);
						/*
						if (name.startsWith("left_curly") || name.startsWith("right_curly"))
							name = "curly_brace";
						if (name.startsWith("left_sq") || name.startsWith("right_sq"))
							name = "sq_bracket";
						*/
						g.setName(name);
						g.setUser(user);
					}
					if (qName.equalsIgnoreCase("POINT")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("X")) {
								x = Integer.parseInt(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("Y")) {
								y = Integer.parseInt(attributes.getValue(i));
							}
						}
						p = new Point(x, y);
						g.addPoint(p);
					}
				}
			 
				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						g.finalizeStroke();
						
						if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
				        	writer.addGesture(g);
							gesture = g;
						}
					}
				}
			};
		 
			saxParser.parse(file.getAbsolutePath(), handler);
	 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public void readHhreco(final File file, final NewWriter writer) {
		final String filename = file.getName();
		System.out.println(filename);
	    try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {

				String user = filename.split("\\.")[0];
				NewGesture g;
				Point p;
				String name = "";
				
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("TYPE")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("NAME")) {
								name = attributes.getValue(i);
							}
						}
					}
					if (qName.equalsIgnoreCase("EXAMPLE")) {
						g = new NewGesture();
						g.setName(name);
						g.setUser(user);
					}
					if (qName.equalsIgnoreCase("STROKE")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("POINTS")) {
								String[] points = attributes.getValue(i).split(" ");
								for (int j = 0; j < points.length;) {
									p = new Point(Math.round(Float.valueOf(points[j])), Math.round(Float.valueOf(points[j+1])));
									g.addPoint(p);
									j += 3;
								}
							}
						}
						g.finalizeStroke();
					}
				}
			 
				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("EXAMPLE")) {
						if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
				        	writer.addGesture(g);
							gesture = g;
						}
					}
				}
			};
		 
			saxParser.parse(file.getAbsolutePath(), handler);
	 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public void readCali1(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(file), 8192);
			String currentLine;
			NewGesture g;
			Point p;
			String n = filename.split("\\.")[0].split("_")[0];

			int numStrokes, numPoints, line;
			boolean firstTime = true;
			
			while((currentLine = reader.readLine()) != null) {
				
				line = reader.getLineNumber();
				
				if (firstTime) {
					String[] stroke = currentLine.split(" ");
					numStrokes = Integer.valueOf(stroke[2]);
					firstTime = false;
				}
				else
					numStrokes = Integer.valueOf(currentLine);
				
				g = new NewGesture();
				g.setName(n);
				
				for (int i = 0; i < numStrokes; i++) {
					currentLine = reader.readLine();
					numPoints = Integer.valueOf(currentLine);
					
					for (int j = 0; j < numPoints; j++) {
						currentLine = reader.readLine();
						String[] point = currentLine.split(" ");
						p = new Point(Integer.valueOf(point[0]), Integer.valueOf(point[1]));
						g.addPoint(p);
					}
					
					g.finalizeStroke();
				}
				
				if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
		        	writer.addGesture(g);
		        	gesture = g;
				}
			}
			
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readCaliSolid(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(file), 8192);
			String currentLine;
			NewGesture g;
			Point p;
			String n = filename.split("\\.")[0];
			
			String[] test = filename.split("\\.")[0].split("_");
			
			if (test.length == 2) {
				if (test[1].equals("d") || test[1].equals("b")) {
					reader.close();
					return;
				}
			}
			
			int numStrokes, numPoints, line;
			boolean firstTime = true;
			
			while((currentLine = reader.readLine()) != null) {
				
				line = reader.getLineNumber();
				
				if (firstTime) {
					String[] stroke = currentLine.split(" ");
					numStrokes = Integer.valueOf(stroke[2]);
					firstTime = false;
				}
				else
					numStrokes = Integer.valueOf(currentLine);
				
				g = new NewGesture();
				g.setName(n);
				
				for (int i = 0; i < numStrokes; i++) {
					currentLine = reader.readLine();
					numPoints = Integer.valueOf(currentLine);
					
					for (int j = 0; j < numPoints; j++) {
						currentLine = reader.readLine();
						String[] point = currentLine.split(" ");
						p = new Point(Integer.valueOf(point[0]), Integer.valueOf(point[1]));
						g.addPoint(p);
					}
					
					g.finalizeStroke();
				}
				
				if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
		        	writer.addGesture(g);
		        	gesture = g;
				}
			}
			
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readCaliDashed(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(file), 8192);
			String currentLine;
			NewGesture g;
			Point p;
			String n = filename.split("\\.")[0];
			
			String[] test = filename.split("\\.")[0].split("_");
			
			if (test.length != 2 || test[1].equals("s") || test[1].equals("b")) {
				reader.close();
				return;
			}
			
			int numStrokes, numPoints, line;
			boolean firstTime = true;
			
			while((currentLine = reader.readLine()) != null) {
				
				line = reader.getLineNumber();
				
				if (firstTime) {
					String[] stroke = currentLine.split(" ");
					numStrokes = Integer.valueOf(stroke[2]);
					firstTime = false;
				}
				else
					numStrokes = Integer.valueOf(currentLine);
				
				g = new NewGesture();
				g.setName(n);
				
				for (int i = 0; i < numStrokes; i++) {
					currentLine = reader.readLine();
					numPoints = Integer.valueOf(currentLine);
					
					for (int j = 0; j < numPoints; j++) {
						currentLine = reader.readLine();
						String[] point = currentLine.split(" ");
						p = new Point(Integer.valueOf(point[0]), Integer.valueOf(point[1]));
						g.addPoint(p);
					}
					
					g.finalizeStroke();
				}
				
				if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
		        	writer.addGesture(g);
		        	gesture = g;
				}
			}
			
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readCaliBold(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(file), 8192);
			String currentLine;
			NewGesture g;
			Point p;
			String n = filename.split("\\.")[0];
			
			String[] test = filename.split("\\.")[0].split("_");
			
			if (test.length != 2 || test[1].equals("s") || test[1].equals("d")) {
				reader.close();
				return;
			}
			
			int numStrokes, numPoints, line;
			boolean firstTime = true;
			
			while((currentLine = reader.readLine()) != null) {
				
				line = reader.getLineNumber();
				
				if (firstTime) {
					String[] stroke = currentLine.split(" ");
					numStrokes = Integer.valueOf(stroke[2]);
					firstTime = false;
				}
				else
					numStrokes = Integer.valueOf(currentLine);
				
				g = new NewGesture();
				g.setName(n);
				
				for (int i = 0; i < numStrokes; i++) {
					currentLine = reader.readLine();
					numPoints = Integer.valueOf(currentLine);
					
					for (int j = 0; j < numPoints; j++) {
						currentLine = reader.readLine();
						String[] point = currentLine.split(" ");
						p = new Point(Integer.valueOf(point[0]), Integer.valueOf(point[1]));
						g.addPoint(p);
					}
					
					g.finalizeStroke();
				}
				
				if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
		        	writer.addGesture(g);
		        	gesture = g;
				}
			}
			
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readExecDif(final File file, final NewWriter writer) {
		final String filename = file.getName();
		System.out.println(filename);
	    try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {

				String user = file.getParentFile().getName();
				NewGesture g;
				Point p;
				int x, y;
				String n = filename.split("\\.")[0];
				
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						g = new NewGesture();
						g.setName(n);
						g.setUser(user);
					}
					if (qName.equalsIgnoreCase("POINT")) {
						int length = attributes.getLength();
						for (int i = 0; i < length; i++) {
							if (attributes.getQName(i).equalsIgnoreCase("X")) {
								x = Integer.parseInt(attributes.getValue(i));
							}
							else if (attributes.getQName(i).equalsIgnoreCase("Y")) {
								y = Integer.parseInt(attributes.getValue(i));
							}
						}
						p = new Point(x, y);
						g.addPoint(p);
					}
				}
			 
				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("GESTURE")) {
						g.finalizeStroke();
						if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
				        	writer.addGesture(g);
							gesture = g;
						}
					}
				}
			};
		 
			saxParser.parse(file.getAbsolutePath(), handler);
	 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	public void readIMISketch(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currentLine;
			NewGesture g;
			String name;
			int numPts;
			String user = filename.split("\\.")[0];

			while ((currentLine = br.readLine()) != null) {
				String[] firstLineOfGesture = currentLine.split(" ");
				int length = firstLineOfGesture.length;
				if (length == 2) {
					name = firstLineOfGesture[0].substring(1);
					numPts = Integer.valueOf(firstLineOfGesture[1]);
				} else if (length == 3) {
					name = firstLineOfGesture[0].substring(1) + "_" + firstLineOfGesture[1];
					numPts = Integer.valueOf(firstLineOfGesture[2]);
				} else {
					name = firstLineOfGesture[0].substring(1) + "_" + firstLineOfGesture[1] + "_" + firstLineOfGesture[2];
					numPts = Integer.valueOf(firstLineOfGesture[3]);
				}
				g = new NewGesture(name);
				g.setUser(user);

				currentLine = br.readLine();

				for (int i = 0; i < numPts; i++, currentLine = br.readLine()) {
					String[] point = currentLine.split(" ");
					int x = Integer.valueOf(point[0]);
					int y = Integer.valueOf(point[1]);
					g.addPoint(x,y);
					int penUp = Integer.valueOf(point[2]);
					if (penUp == 0)
						g.finalizeStroke();
				}
				if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
					writer.addGesture(g);
					gesture = g;
				}
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readLaViola(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			String gestName = filename.split("\\.")[0];
			if (gestName.equalsIgnoreCase("check") || gestName.equalsIgnoreCase("a_c") || gestName.equalsIgnoreCase("left_bracket"))
				return;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currentLine;
			NewGesture g;

			while (!(currentLine = reader.readLine()).startsWith(".WRITER_ID"));
			String user = currentLine.split(" ")[1];

			int numStroke = 0;
			TreeMap<Integer, Stroke> map = new TreeMap<>();
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.equals(".PEN_DOWN")) {
					Stroke s = new Stroke();
					currentLine = reader.readLine();
					while (!currentLine.equals(".PEN_UP")) {
						String[] pts = 	currentLine.split(" ");
						s.add(new Point(Integer.parseInt(pts[0]), Integer.parseInt(pts[1])));
						currentLine = reader.readLine();
					}
					map.put(numStroke, s);
					numStroke++;
				}
			}
			reader.close();

			String separator = File.separator;
			String keyPath = file.getAbsolutePath().replace(separator + "include" + separator, separator + "data" + separator);
			reader = new BufferedReader(new FileReader(keyPath));
			reader.readLine();
			reader.readLine();

			while ((currentLine = reader.readLine()) != null) {
				g = new NewGesture(gestName);
				g.setUser(user);
				String[] keyLine = currentLine.split(" ");
				String[] numbers = keyLine[2].split("-");
				int left, right;
				if (numbers.length == 2) {
					left = Integer.valueOf(numbers[0]);
					right = Integer.valueOf(numbers[1]) + 1;
				} else {
					left = Integer.valueOf(keyLine[2]);
					right = left + 1;
				}

				for (int i = left; i < right; i++)
					g.addStroke(map.get(i));

				if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
					writer.addGesture(g);
					gesture = g;
				}
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readLaViolaTest(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			String gestName = filename.split("\\.")[0];
			if (gestName.equalsIgnoreCase("check") || gestName.equalsIgnoreCase("a_c") || gestName.equalsIgnoreCase("left_bracket"))
				return;

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currentLine;
			NewGesture g;

			while (!(currentLine = reader.readLine()).startsWith(".WRITER_ID"));
			String user = currentLine.split(" ")[1];

			int numStroke = 0;
			TreeMap<Integer, Stroke> map = new TreeMap<>();
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.equals(".PEN_DOWN")) {
					Stroke s = new Stroke();
					currentLine = reader.readLine();
					while (!currentLine.equals(".PEN_UP")) {
						String[] pts = 	currentLine.split(" ");
						s.add(new Point(Integer.parseInt(pts[0]), Integer.parseInt(pts[1])));
						currentLine = reader.readLine();
					}
					map.put(numStroke, s);
					numStroke++;
				}
			}
			reader.close();

			int numStrokes = map.size();
			int numStrokesPerGesture = numStrokes / 12;

			for (int i = 0; i < numStrokes; i = i + numStrokesPerGesture) {
				g = new NewGesture(gestName);
				g.setUser(user);
				for (int j = 0; j < numStrokesPerGesture; j++) {
					g.addStroke(map.get(i + j));
				}
				if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
					writer.addGesture(g);
					gesture = g;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readSign(final File file, final NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				NewGesture g;
				int x, y;
				String userName;
				boolean annotation, tracegroup, user, label, trace;

				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					if (qName.equalsIgnoreCase("ANNOTATIONXML")) {
						annotation = true;
					}
					if (qName.equalsIgnoreCase("NAME")) {
						if (annotation)
							user = true;
					}
					if (qName.equalsIgnoreCase("TRACEGROUP")) {
						tracegroup = true;
					}
					if (qName.equalsIgnoreCase("LABEL")) {
						if (tracegroup)
							label = true;
					}
					if (qName.equalsIgnoreCase("TRACE")) {
						trace = true;
					}
				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if (annotation && user) {
						userName = new String(ch, start, length);
						annotation = false;
						user = false;
					}

					if (label) {
						g = new NewGesture(new String(ch, start, length));
						g.setUser(userName);
						label = false;
					}

					if (trace) {
						String whole = new String(ch, start, length).trim();
//						System.out.println(whole);
						String[] pts = whole.split(",");
						int numPts = pts.length;
						if (numPts != 0) {
							String[] split = pts[0].split(" ");
							x = Integer.valueOf(split[0]) / 26;
							y = Integer.valueOf(split[1]) / 26;
							g.addPoint(x, y);
						}
						for (int i = 1; i < numPts; i++) {
							String[] split = pts[i].split(" ");
							x = Integer.valueOf(split[1]) / 26;
							y = Integer.valueOf(split[2]) / 26;
							g.addPoint(x, y);
						}
						trace = false;
					}
				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					if (qName.equalsIgnoreCase("TRACE")) {
						g.finalizeStroke();
						if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
							writer.addGesture(g);
							gesture = g;
						}
//						tracegroup = false;
					}
				}
			};

			saxParser.parse(file.getAbsolutePath(), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readCVC(File file, NewWriter writer) {
		String filename = file.getName();
		System.out.println(filename);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currentLine;
			String user = file.getParentFile().getName();
			NewGesture g = new NewGesture();
			g.setUser(user);
			int numPts;

			while ((currentLine = br.readLine()) != null) {
				if (currentLine.startsWith("=L")) {
					String[] nameLine = currentLine.split(" ");
					g.setName(nameLine[6]);
				}
				else if (currentLine.startsWith("=S")) {
					currentLine = br.readLine();
					String[] pts = currentLine.split(" ");
					numPts = pts.length;
					for (int i = 0; i < numPts; i=i+2)
						g.addPoint(Integer.valueOf(pts[i]), Integer.valueOf(pts[i+1]));
					g.finalizeStroke();
				}
			}

			if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
				writer.addGesture(g);
				gesture = g;
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readNicIcon(File file, NewWriter writer) {
		try {
			String folder = file.getParentFile().getName();
			if (folder.equals("WI-eval") || folder.equals("WI-test") || folder.equals("WI-train")) {
				String filename = file.getName();
				System.out.println(filename);
				String user = filename.split("\\.")[0];
				BufferedReader keyReader = new BufferedReader(new FileReader(file));
				BufferedReader strokeReader = new BufferedReader(new FileReader(file));
				String currLineKey, currLineStroke = "";
				NewGesture g;
				int currentStrokeNumber = 0;

				while (!(strokeReader.readLine()).startsWith(".PEN"));

				keyReader.readLine();
				keyReader.readLine();
				keyReader.readLine();
				while ((currLineKey = keyReader.readLine()).startsWith(".SEGMENT")) {
					currLineKey = currLineKey.trim().replaceAll(" +", " ");
					String[] spaceSplit = currLineKey.split(" ");
					if (spaceSplit[3].equals("BAD"))
						continue;

					String gestName = spaceSplit[4].split("-")[4];
					g = new NewGesture(gestName);
					g.setUser(user);
					int x, y;
					String[] intervals = spaceSplit[2].split(",");
					for (int i = 0; i < intervals.length; i++) {
						String strokeNumbers = intervals[i];
//						System.out.println(strokeNumbers);
						String[] numbers = strokeNumbers.split("-");
						int left, right;
						if (numbers.length == 2) {
							left = Integer.valueOf(numbers[0]);
							right = Integer.valueOf(numbers[1]) + 1;
						} else {
							left = Integer.valueOf(strokeNumbers);
							right = left + 1;
						}

						while (currentStrokeNumber < left) {
							currLineStroke = strokeReader.readLine();
							if (currLineStroke.startsWith(".PEN"))
								currentStrokeNumber++;
						}

						while (currentStrokeNumber < right) {
							if (currLineStroke.startsWith(".PEN_DOWN")) {
								while (!(currLineStroke = strokeReader.readLine()).startsWith(".PEN")) {
									String[] pts = currLineStroke.trim().replaceAll(" +", " ").split(" ");
									x = Integer.parseInt(pts[0]) / 50;
									y = Integer.parseInt(pts[1]) / 50;
									g.addPoint(x, y);
								}
								g.finalizeStroke();
								currentStrokeNumber += 2;
							}
							else
								currLineStroke = strokeReader.readLine();
						}
					}
					if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
						writer.addGesture(g);
						gesture = g;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readILG(File file, NewWriter writer) {
		try {
			String filename = file.getParentFile().getName();
			System.out.println(filename);
			String user = filename.split("_")[0];
//			String phase = filename.split("\\.")[0].split("_")[1];
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currentLine;
			NewGesture g;
			int numPts;
			String name;

			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#")) {
					String[] split = currentLine.split(" ");
					name = split[0].substring(1);
					numPts = Integer.valueOf(split[1]);
					g = new NewGesture(name);
					g.setUser(user);
					for (int i = 0; i < numPts; i++) {
						currentLine = reader.readLine();
						String[] pts = currentLine.split(" ");
						g.addPoint(Integer.valueOf(pts[0]), Integer.valueOf(pts[1]));
					}
					g.finalizeStroke();
					if ((g.getNumPoints() > 5) || (g.getLength() > 20)) {
						writer.addGesture(g);
						gesture = g;
					}
				}
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
