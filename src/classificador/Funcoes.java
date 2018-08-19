package classificador;

import java.util.ArrayList;

public class Funcoes {


	public double getLowe(ArrayList<Double> s) {
		double d = s.get(0);
		for (int i = 0; i < 64; i++) {
			if (d > s.get(i)) {
				d = s.get(i);
			}
		}
		return d;
	}
	public double getHight(ArrayList<Double> s) {
		double d = s.get(64);
		for (int i = 64; i < 154; i++) {
			if (d < s.get(i)) {
				d = s.get(i);
			}
		}
		return d;
	}

	public double getAverage(ArrayList<Double> s) {
		double d = 0;
		for (int i = 0; i < s.size(); i++) {
			d += s.get(i);
		}
		return d / s.size();
	}

	public ArrayList<Double> sampleReduction(ArrayList<Double> s){
		ArrayList<Double> rs = new ArrayList<>();
		for (int i = 0; i < s.size(); i = i + 16) {
			double l = 0;
			for (int a = i; a < i + 16; a++) {
				l+=s.get(a);
			}
			rs.add(l/16);
		}
		return rs; 
	}

	public double getAverageSD(ArrayList<Double> p, int start, int stop) {
		double a = 0;
		for ( int i = start; i< stop; i++) {
			a+=p.get(i);
		}
		return a/(stop-start);
	}
	public double getAverageDesvio(ArrayList<Double> p, int start, int stop) {
		double a = 0;
		double average = getAverageSD(p, start, stop);
		for ( int i = start; i< stop; i++) {
			double desvio = p.get(i) - average;
			a+=(desvio * desvio);
		}
		double m = a/(stop-start-1);
		return Math.pow(m, 2);
	}


	public ArrayList<Double> getVector(ArrayList<Double> p){
		ArrayList<Double> e = new ArrayList<>();
		e.add(getAverageSD(p, 37, 67));
		e.add(getAverageSD(p, 50, 80));
		e.add(getAverageSD(p, 75, 105));
		e.add(getAverageSD(p, 100, 130));
		e.add(getAverageSD(p, 125, 155));
		e.add(getAverageSD(p, 150, 180));
		e.add(getAverageSD(p, 62, 155));
		e.add(getAverageSD(p, 100, 155));
		e.add(getAverageDesvio(p, 37, 67));
		e.add(getAverageDesvio(p, 50, 80));
		e.add(getAverageDesvio(p, 75, 105));
		e.add(getAverageDesvio(p, 100, 130));
		e.add(getAverageDesvio(p, 125, 155));
		e.add(getAverageDesvio(p, 150, 180));
		e.add(getAverageDesvio(p, 62, 155));
		e.add(getAverageDesvio(p, 100, 155));
		return e;
		
		
		
		
		
		
		
		
		
	}

}
