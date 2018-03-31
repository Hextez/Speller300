package testes;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.*;
public class Teste1 extends ApplicationFrame{

	public Teste1(String applicationTitle , String chartTitle ) throws IOException {
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
	      
	      renderer.setSeriesStroke( 0 , new BasicStroke( 0.1f ) );
	      renderer.setSeriesStroke( 1 , new BasicStroke( 0.4f ) );

	      plot.setRenderer( renderer ); 
	      setContentPane( chartPanel );
	   }

	private XYSeriesCollection createDataset( ) throws IOException {
		final XYSeriesCollection dataset = new XYSeriesCollection( );  
		HashMap<Integer, HashMap<String, Double>> electodes = Electrodes();
		  ArrayList<Integer> Ys = ValoresY();
		  ArrayList<Integer> YsStim = ValoresY_Stim();
		  Iterator<Integer> s = Ys.iterator();
		  int index = 1801;
		  final XYSeries tst = new XYSeries( "Non-Target" );          

		  while ( s.hasNext() && index < 11529)	{

			  Integer ss = s.next();

			  if (ss == 1 || ss == 0){
			      tst.add(index-1, electodes.get(index).get("Fz"));
			  }
			  index++;
		  }
		  
		  final XYSeries tst2 = new XYSeries( "Target" );          
		  index = 1801;
		  s = Ys.iterator();
		  while ( s.hasNext() && index < 11529)	{
			  
			  Integer ss = s.next();
			  if (ss == 2 || ss == 0){
			      tst2.add(index-1, electodes.get(index).get("Fz"));
			  }
			  index++;
		  }
		  dataset.addSeries(tst);
		  dataset.addSeries(tst2);

	      return dataset;
	   }
	   

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Teste1 chart = new Teste1(
		         "P300" ,
		         "P300 do electrode Fz");

		      chart.pack( );
		      RefineryUtilities.centerFrameOnScreen( chart );
		      chart.setVisible( true );
	}
	
	
	
	public static ArrayList<Integer> ValoresY() throws IOException{
		ArrayList<Integer> arrayList = new ArrayList<>();
		String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\test2.csv";
		BufferedReader br = null;
		String line = "";
		
		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			br.readLine();
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				arrayList.add(new Integer(linha[0]));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return arrayList;
	}
	
	public static ArrayList<Integer> ValoresY_Stim() throws IOException{
		ArrayList<Integer> arrayList = new ArrayList<>();
		String nomeFicheiro = "C:\\Users\\Eu\\Desktop\\test2.csv";
		BufferedReader br = null;
		String line = "";
		
		try{
			br = new BufferedReader(new FileReader(nomeFicheiro));
			br.readLine();
			while( (line = br.readLine())!= null){
				String[] linha = line.split(",");
				arrayList.add(new Integer(linha[1]));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return arrayList;
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
	
	
}
