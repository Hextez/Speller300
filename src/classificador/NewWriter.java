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

	public NewWriter(String path, int numFeatures) {
		this.numFeatures = numFeatures;
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
			if (this.numFeatures == 13){
				line += "@ATTRIBUTE circleR NUMERIC\n";
				line += "@ATTRIBUTE rectR1 NUMERIC\n";
				line += "@ATTRIBUTE rectR2 NUMERIC\n";
				line += "@ATTRIBUTE rectR3 NUMERIC\n";
				line += "@ATTRIBUTE rectR4 NUMERIC\n";
				line += "@ATTRIBUTE triR3 NUMERIC\n";
				line += "@ATTRIBUTE aspectR NUMERIC\n";
				line += "@ATTRIBUTE eqR NUMERIC\n";
				line += "@ATTRIBUTE MovY NUMERIC\n";
				line += "@ATTRIBUTE chR2 NUMERIC\n";
				line += "@ATTRIBUTE bbchR NUMERIC\n";
				//line += "@ATTRIBUTE rms NUMERIC\n";
			}else if (this.numFeatures == 12){
				line += "@ATTRIBUTE circleR NUMERIC\n";
				line += "@ATTRIBUTE rectR2 NUMERIC\n";
				line += "@ATTRIBUTE rectR3 NUMERIC\n";
				line += "@ATTRIBUTE triR3 NUMERIC\n";
				line += "@ATTRIBUTE aspectR NUMERIC\n";
				line += "@ATTRIBUTE eqR NUMERIC\n";
				line += "@ATTRIBUTE MovY NUMERIC\n";
				line += "@ATTRIBUTE chR2 NUMERIC\n";
				line += "@ATTRIBUTE chR3 NUMERIC\n";
				line += "@ATTRIBUTE bbchR NUMERIC\n";
				//line += "@ATTRIBUTE rms NUMERIC\n";
			}else if (this.numFeatures == 9){
				line += "@ATTRIBUTE rectR2 NUMERIC\n";
				line += "@ATTRIBUTE aspectR NUMERIC\n";
				line += "@ATTRIBUTE eqR NUMERIC\n";
				line += "@ATTRIBUTE MovY NUMERIC\n";
				line += "@ATTRIBUTE chR2 NUMERIC\n";
				line += "@ATTRIBUTE chR3 NUMERIC\n";
				line += "@ATTRIBUTE bbchR NUMERIC\n";
				//line += "@ATTRIBUTE rms NUMERIC\n";
			}
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

	public void addGesture(NewGesture g) {
		g.calcFeatures();
		addGestureToXML(g);
		addGestureToARFF(g);
		gestID++;
	}

	public void addGestureToARFF(NewGesture g) {
		if (!classes.contains(g.getName()))
			classes.add(g.getName());

		try {
			String fileContent = "";
			NewFeatures f = g.getFeatures();
			fileContent += g.getName() + ",";
			if (this.numFeatures == 13){
				fileContent += f.getCircleR() + ",";
				fileContent += f.getRectR1() + ",";
				fileContent += f.getRectR2() + ",";
				fileContent += f.getRectR3() + ",";
				fileContent += f.getRectR4() + ",";
				fileContent += f.getTriR3() + ",";
				fileContent += f.getAspectR() + ",";
				fileContent += f.getEqR1() + ",";
				fileContent += f.getMovementY() + ",";
				fileContent += f.getChR2() + ",";
				fileContent += f.getBbchR() + "\n";
				//fileContent += f.getRMS()+ "\n";
			}else if (this.numFeatures == 12){
				fileContent += f.getCircleR() + ",";
				fileContent += f.getRectR2() + ",";
				fileContent += f.getRectR3() + ",";
				fileContent += f.getTriR3() + ",";
				fileContent += f.getAspectR() + ",";
				fileContent += f.getEqR1() + ",";
				fileContent += f.getMovementY() + ",";
				fileContent += f.getChR2() + ",";
				fileContent += f.getChR3() + ",";
				fileContent += f.getBbchR() + "\n";
				//fileContent += f.getRMS()+ "\n";
			}else if (this.numFeatures == 9){
				fileContent += f.getRectR2() + ",";
				fileContent += f.getAspectR() + ",";
				fileContent += f.getEqR1() + ",";
				fileContent += f.getMovementY() + ",";
				fileContent += f.getChR2() + ",";
				fileContent += f.getChR3() + ",";
				fileContent += f.getBbchR() + "\n";
				//fileContent += f.getRMS()+ "\n";
			}

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
