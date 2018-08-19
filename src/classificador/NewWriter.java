package classificador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mcali.Point;
import mcali.Stroke;

public class NewWriter {

	private File arff, xml;
	private BufferedWriter writerARFF;
	private BufferedWriter writerXML;
	private ArrayList<String> classes;
	private String line;
	private int gestID;
	private int numFeatures;

	public NewWriter(String path) {
		arff = new File(path + ".arff");
		xml = new File(path + ".xml");
		classes = new ArrayList<String>();
		gestID = 0;

		try {
			writerARFF = new BufferedWriter(new FileWriter(arff));
			writerXML = new BufferedWriter(new FileWriter(xml));

			line = "% mCALI dataset\n";

			//fileContent += "%\n% Joao Vieira\n";
			line += "@RELATION mCALI\n\n";
			line += "@ATTRIBUTE class {}\n";
			line += "@ATTRIBUTE rectR1 NUMERIC\n";
			line += "@ATTRIBUTE rectR2 NUMERIC\n";
			line += "@ATTRIBUTE rectR3 NUMERIC\n";
			line += "@ATTRIBUTE eqR NUMERIC\n";
			line += "@ATTRIBUTE MovY NUMERIC\n";
			line += "@ATTRIBUTE chR2 NUMERIC\n";
			line += "@ATTRIBUTE chR3 NUMERIC\n";
			line += "@ATTRIBUTE bbchR NUMERIC\n";
			line += "@ATTRIBUTE quad2FillR NUMERIC\n";
			line += "@ATTRIBUTE quad3FillR NUMERIC\n";
			line += "@ATTRIBUTE quad4FillR NUMERIC\n";
			line += "@ATTRIBUTE astchAR NUMERIC\n";
			line += "@ATTRIBUTE astchPR NUMERIC\n";
			line += "@ATTRIBUTE f1 NUMERIC\n";
			line += "@ATTRIBUTE f2 NUMERIC\n";
			line += "@ATTRIBUTE f3 NUMERIC\n";
			line += "@ATTRIBUTE f4 NUMERIC\n";
			line += "@ATTRIBUTE f5 NUMERIC\n";
			line += "@ATTRIBUTE f6 NUMERIC\n";
			line += "@ATTRIBUTE f7 NUMERIC\n";
			line += "@ATTRIBUTE f8 NUMERIC\n";
			line += "@ATTRIBUTE astchAB NUMERIC\n";
			line += "@ATTRIBUTE rms NUMERIC\n";
			line += "@ATTRIBUTE fillingR NUMERIC\n";
			line += "\n@DATA\n";

			writerARFF.write(line);
			writerARFF.flush();

			line = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n<MCALI>\n";
			writerXML.write(line);
			writerXML.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public NewWriter(String path, int s) {
		arff = new File(path + ".arff");
		//xml = new File(path + ".xml");
		classes = new ArrayList<String>();
		gestID = 0;

		try {
			writerARFF = new BufferedWriter(new FileWriter(arff));
			//writerXML = new BufferedWriter(new FileWriter(xml));

			line = "% mCALI dataset\n";

			//fileContent += "%\n% Joao Vieira\n";
			line += "@RELATION mCALI\n\n";
			line += "@ATTRIBUTE class {}\n";
			line += "@ATTRIBUTE Average150250 NUMERIC\n";
			line += "@ATTRIBUTE Average200300 NUMERIC\n";
			line += "@ATTRIBUTE Average300400 NUMERIC\n";
			line += "@ATTRIBUTE Average400500 NUMERIC\n";
			line += "@ATTRIBUTE Average500600 NUMERIC\n";
			line += "@ATTRIBUTE Average600700 NUMERIC\n";
			line += "@ATTRIBUTE Average250600 NUMERIC\n";
			line += "@ATTRIBUTE Average400600 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes150250 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes200300 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes300400 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes400500 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes500600 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes600700 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes250600 NUMERIC\n";
			line += "@ATTRIBUTE AverageDes400600 NUMERIC\n";
			line += "\n@DATA\n";

			writerARFF.write(line);
			writerARFF.flush();

			//line = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n<MCALI>\n";
			//writerXML.write(line);
			//writerXML.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addGesture(NewGesture g) {
		g.calcFeatures();
		addGestureToXML(g);
		addGestureToARFF(g);
		gestID++;
	}

	public void addAverages(String name, ArrayList<Double> averages ) {
		if (!classes.contains(name))
			classes.add(name);

		try {
			String fileContent = name;
			for (int i = 0; i < averages.size(); i++) {
				fileContent+= ","+averages.get(i);
			}
			
			writerARFF.write(fileContent+"\n");
			writerARFF.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeArff() {
		try {
			writerARFF.close();
			addClasses();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String getARFFPath() {
		return arff.getAbsolutePath();
	}
	public void addGestureToARFF(NewGesture g) {
		if (!classes.contains(g.getName()))
			classes.add(g.getName());

		try {
			String fileContent = "";
			NewFeatures f = g.getFeatures();
			fileContent += g.getName() + ",";
			fileContent += f.getRectR1() + ",";
			fileContent += f.getRectR2() + ",";
			fileContent += f.getRectR3() + ",";
			fileContent += f.getEqR1() + ",";
			fileContent += f.getMovementY() + ",";
			fileContent += f.getChR2() + ",";
			fileContent += f.getChR3() + ",";
			fileContent += f.getBbchR() + ",";
			fileContent += f.getQuad2FillR() + ",";
			fileContent += f.getQuad3FillR() + ",";
			fileContent += f.getQuad4FillR() + ",";
			fileContent += f.getAstchAR() + ",";
			fileContent += f.getAstchPR()+ ",";
			fileContent += f.getAa1() + ",";
			fileContent += f.getAa2() + ",";
			fileContent += f.getAa3() + ",";
			fileContent += f.getAa4() + ",";
			fileContent += f.getRa1() + ",";
			fileContent += f.getRa2() + ",";
			fileContent += f.getRa3() + ",";
			fileContent += f.getRa4() + ",";
			fileContent += f.getAstchAB() + ",";
			fileContent += f.getRMS() + ",";
			fileContent += f.getFillingR() + "\n";

			writerARFF.write(fileContent);
			writerARFF.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addGestureToXML(NewGesture g) {
		try {
			line = "\t<Gesture Name=\"" + g.getName() + "\" NumStrokes=\"" + g.getNumStrokes() + "\" NumPts=\"" + g.getNumPoints() + "\" User=\"" + g.getUser() + "\" GestID=\"" + gestID + "\">\n";
			writerXML.write(line);

			List<Stroke> strokes = g.getStrokes();
			int i = 1;

			for (Stroke s : strokes) {
				line = "\t\t<Stroke ID=\"" + i + "\">\n";
				writerXML.write(line);

				for (Point p : s.getPoints()) {
					line = "\t\t\t<Point X=\"" + p.x + "\" Y=\"" + p.y + "\" MS=\"" + p.timestamp + "\"/>\n";
					writerXML.write(line);
				}

				line = "\t\t</Stroke>\n";
				writerXML.write(line);
				i++;
			}

			line = "\t</Gesture>\n";
			writerXML.write(line);
			writerXML.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writerARFF.close();
			addClasses();

			line = "</MCALI>";
			writerXML.write(line);
			writerXML.flush();
			writerXML.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addClasses() {

		try {
			File newARFF = new File("new-dataset.arff");
			newARFF.createNewFile();

			BufferedReader reader = new BufferedReader(new FileReader(arff), 8192);
			BufferedWriter writer = new BufferedWriter(new FileWriter(newARFF));

			String currentLine;

			String newClasses = "";

			for (int i = 0; i < 3; i++) {
				currentLine = reader.readLine();
				writer.write(currentLine + "\n");
			}

			if (classes.size() != 0) {
				Collections.sort(classes);
				currentLine = reader.readLine();
				String[] splitByBrackets = currentLine.split("[{.*?}]");
				String[] splitByCommas;

				// There is no class between the brackets
				if (splitByBrackets.length == 1) {
					for (String c : classes) {
						newClasses += "," + c;
					}
					writer.write("@ATTRIBUTE class {" + newClasses.substring(1) + "}\n");
				}
				// There is at least one class between the brackets
				else {
					splitByCommas = splitByBrackets[1].split(",");
					boolean isPresent = false;
					for (String c : classes) {
						for (int i = 0; i < splitByCommas.length; i++) {
							if (splitByCommas[i].equals(c)) {
								isPresent = true;
								break;
							}
						}
						if (!isPresent) {
							newClasses += "," + c;
						}
						isPresent = false;
					}

					writer.write("@ATTRIBUTE class {" + splitByBrackets[1] + newClasses + "}\n");
				}
			}

			while((currentLine = reader.readLine()) != null) {
				writer.write(currentLine + "\n");
			}

			reader.close();
			writer.close();

			arff.delete();
			newARFF.renameTo(arff);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
