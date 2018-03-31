package testes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Teste4 extends ApplicationFrame{

	private Integer numberStart = 0;
	private final static Integer MS_250 = 64;
	private final static Integer MS_600 = 154;
	private final static Integer FristTrial = 1801;
	private final static ArrayList<String> ListOfElectrodes = new ArrayList<>(Arrays.asList("Fz","Cz","Pz","Oz","P3","P4","PO7","PO8")); 
	private static ArrayList<Integer> classification = new ArrayList<>();
	private final static ArrayList<Integer> listOfTrials = new ArrayList<>(Arrays.asList(1801, 11529, 21257, 30985, 40713,
			51473, 61201, 70929, 80657, 90385, 101145, 110873, 120601, 130329, 140057, 150817, 160545, 170273,
			180001, 189729, 200489, 210217, 219945, 229673, 239401, 250161, 259889, 269617, 279345, 289073, 299833,
			309561, 319289, 329017, 338745));


	public Teste4(String applicationTitle , String chartTitle ) throws IOException {
		super(applicationTitle);
		JFreeChart xylineChart = ChartFactory.createXYLineChart(
				chartTitle ,
				"Category" ,
				"Score" ,
				createDataset() ,
				PlotOrientation.VERTICAL ,
				true , true , false);

		ChartPanel chartPanel = new ChartPanel( xylineChart );
		chartPanel.setPreferredSize( new java.awt.Dimension( 1200 , 500 ) );
		final XYPlot plot = xylineChart.getXYPlot( );

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
		renderer.setSeriesPaint( 0 , Color.RED );
		renderer.setSeriesPaint( 1 , Color.GREEN );
		renderer.setSeriesPaint( 2 , Color.BLACK );
		renderer.setSeriesPaint( 3 , Color.BLUE );
		renderer.setSeriesPaint( 4 , Color.CYAN );
		renderer.setSeriesPaint( 5 , Color.DARK_GRAY );
		renderer.setSeriesPaint( 6 , Color.MAGENTA );
		renderer.setSeriesPaint( 7 , Color.PINK );

		renderer.setSeriesStroke( 0 , new BasicStroke( 0.3f ) );
		renderer.setSeriesStroke( 1 , new BasicStroke( 0.4f ) );
		renderer.setSeriesStroke( 2 , new BasicStroke( 0.3f ) );
		renderer.setSeriesStroke( 3 , new BasicStroke( 0.4f ) );
		renderer.setSeriesStroke( 4 , new BasicStroke( 0.3f ) );
		renderer.setSeriesStroke( 5 , new BasicStroke( 0.4f ) );
		renderer.setSeriesStroke( 6 , new BasicStroke( 0.3f ) );
		renderer.setSeriesStroke( 7 , new BasicStroke( 0.4f ) );
		plot.setRenderer( renderer ); 
		setContentPane( chartPanel );
	}


	private XYSeriesCollection createDataset( ) throws IOException {
		final XYSeriesCollection dataset = new XYSeriesCollection( );
		HashMap<Integer, HashMap<String, Double>> values = Electrodes();
		int index = numberStart ;
		int lastPosition = index + MS_600;
		final XYSeries electrode1 = new XYSeries( ListOfElectrodes.get(0) );     
		final XYSeries electrode2 = new XYSeries( ListOfElectrodes.get(1) );     
		final XYSeries electrode3 = new XYSeries( ListOfElectrodes.get(2) );     
		final XYSeries electrode4 = new XYSeries( ListOfElectrodes.get(3) );     
		final XYSeries electrode5 = new XYSeries( ListOfElectrodes.get(4) );     
		final XYSeries electrode6 = new XYSeries( ListOfElectrodes.get(5) );     
		final XYSeries electrode7 = new XYSeries( ListOfElectrodes.get(6) );    
		final XYSeries electrode8 = new XYSeries( ListOfElectrodes.get(7) );     

		Iterator<Integer> pointInfo = classification.iterator();
		while ( pointInfo.hasNext() && index < lastPosition )	{

			Integer info = pointInfo.next();

			if (info == 1 || info == 0){ //target
				electrode1.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(0)));
				electrode2.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(1)));
				electrode3.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(2)));
				electrode4.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(3)));
				electrode5.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(4)));
				electrode6.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(5)));
				electrode7.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(6)));
				electrode8.add(index+FristTrial, values.get(index).get(ListOfElectrodes.get(7)));

			}
			index++;
		}

//		final XYSeries tst2 = new XYSeries( "Target" );          
//		while ( s.hasNext() && index < lastPosition + MS_600)	{
//
//			Integer ss = s.next();
//			if (ss == 2 || ss == 0){
//				tst2.add(index, electodes.get(index+1).get("Fz"));
//			}
//			index++;
//		}
		dataset.addSeries(electrode1);
		dataset.addSeries(electrode2);
		dataset.addSeries(electrode3);
		dataset.addSeries(electrode4);
		dataset.addSeries(electrode5);
		dataset.addSeries(electrode6);
		dataset.addSeries(electrode7);
		dataset.addSeries(electrode8);
		numberStart = numberStart + MS_250;

		return dataset;
	}


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Teste4 chart = new Teste4(
		         "P300" ,
		         "P300 electrodes");

		      chart.pack( );
		      RefineryUtilities.centerFrameOnScreen( chart );
		      chart.setVisible( true );
	}

	public static HashMap<Integer, HashMap<String, Double>> Electrodes(){
		HashMap<Integer, HashMap<String, Double>> map = new HashMap<>();
		String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\DadosComPontosEstimulos.csv";
		BufferedReader br = null;
		String line = "";

		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			int position = 0;
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				HashMap<String, Double> inside = new HashMap<>();
				inside.put(ListOfElectrodes.get(0), new Double(linha[0]));
				inside.put(ListOfElectrodes.get(1), new Double(linha[1]));
				inside.put(ListOfElectrodes.get(2), new Double(linha[2]));
				inside.put(ListOfElectrodes.get(3), new Double(linha[3]));
				inside.put(ListOfElectrodes.get(4), new Double(linha[4]));
				inside.put(ListOfElectrodes.get(5), new Double(linha[5]));
				inside.put(ListOfElectrodes.get(6), new Double(linha[6]));
				inside.put(ListOfElectrodes.get(7), new Double(linha[7]));
				classification.add(new Integer(linha[8]));
				map.put(position, inside);
				position++;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return map;

	}

}
