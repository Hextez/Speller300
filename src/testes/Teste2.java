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

public class Teste2 extends ApplicationFrame{

	
	private static Integer trial = 0;
	private static Integer currentPosition = 0;
	private final static Integer MS_250 = 64;
	private final static Integer MS_600 = 154;
	
	private final static ArrayList<Integer> listOfTrials = new ArrayList<>(Arrays.asList(1801, 11529, 21257, 30985, 40713,
			51473, 61201, 70929, 80657, 90385, 101145, 110873, 120601, 130329, 140057, 150817, 160545, 170273,
			180001, 189729, 200489, 210217, 219945, 229673, 239401, 250161, 259889, 269617, 279345, 289073, 299833,
			309561, 319289, 329017, 338745));
	
	public Teste2(String applicationTitle , String chartTitle ) throws IOException {
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
	      
	      renderer.setSeriesStroke( 0 , new BasicStroke( 0.3f ) );
	      renderer.setSeriesStroke( 1 , new BasicStroke( 1.0f ) );

	      plot.setRenderer( renderer ); 
	      setContentPane( chartPanel );
	   }

	private XYSeriesCollection createDataset( ) throws IOException {
		final XYSeriesCollection dataset = new XYSeriesCollection( );  
		HashMap<Integer, HashMap<String, Double>> electodes = Electrodes();
		  ArrayList<Integer> Ys = ValoresY();
		  Iterator<Integer> s = Ys.iterator();
		  //int index = listOfTrials.get(trial) ;
		  int index = 2121;
		  int lastPosition = index;
		  final XYSeries tst = new XYSeries( "Non-Target" );          

		  while ( s.hasNext() && index < lastPosition + MS_600 )	{

			  Integer ss = s.next();

			  if (ss == 1 || ss == 0){
			      tst.add(index, electodes.get(index+1).get("Fz"));
			  }
			  index++;
		  }
		  
		  final XYSeries tst2 = new XYSeries( "Target" );          
		  index = 1801;
		  s = Ys.iterator();
		  while ( s.hasNext() && index < lastPosition + MS_600)	{
			  
			  Integer ss = s.next();
			  if (ss == 2 || ss == 0){
			      tst2.add(index, electodes.get(index+1).get("Fz"));
			  }
			  index++;
		  }
		  dataset.addSeries(tst);
		  dataset.addSeries(tst2);
		  trial++;

	      return dataset;
	   }
	   
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Teste2 chart = new Teste2(
		         "P300" ,
		         "P300 do electrode Fz");

		      chart.pack( );
		      RefineryUtilities.centerFrameOnScreen( chart );
		      chart.setVisible( true );
	}
	
	public static HashMap<Integer, HashMap<String, Double>> Electrodes(){
		HashMap<Integer, HashMap<String, Double>> map = new HashMap<>();
		String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\test.csv";
		BufferedReader br = null;
		String line = "";

		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			String info = br.readLine();
			int position = 1;
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				String[] infos = info.split(",");
				HashMap<String, Double> inside = new HashMap<>();
				inside.put(infos[0], new Double(linha[0]));
				inside.put(infos[1], new Double(linha[1]));
				inside.put(infos[2], new Double(linha[2]));
				inside.put(infos[3], new Double(linha[3]));
				inside.put(infos[4], new Double(linha[4]));
				inside.put(infos[5], new Double(linha[5]));
				inside.put(infos[6], new Double(linha[6]));
				inside.put(infos[7], new Double(linha[7]));
				map.put(position, inside);
				position++;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return map;
		
	}
	
	public static ArrayList<Integer> ValoresY() throws IOException{
		ArrayList<Integer> arrayList = new ArrayList<>();
		String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\test2.csv";
		BufferedReader br = null;
		String line = "";
		
		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			br.readLine();
			int numb = 1;
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				if (numb > 2121){
					arrayList.add(new Integer(linha[0]));
				}
				numb++;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return arrayList;
	}

}
