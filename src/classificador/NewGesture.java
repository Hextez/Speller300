package classificador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcali.Function;
import mcali.Point;
import mcali.Point2D;
import mcali.Polygon;
import mcali.Stroke;
import mcali.Vector;

import java.util.*;

public class NewGesture {
		
		private List<Stroke> strokes;
		private Stroke currentStroke;
		private String name;
		private String user;
		private int gestID;
		private Polygon convexHull, largestTriangle, 
			largestQuad, enclosingRect, extremeQuad,
			intersectCH1, intersectCH2, boundingBox,
			alphaShape, alphaShapeTest;
		private NewFeatures features;
		private Point minX, maxX, minY, maxY;
		// Test
		private Polygon quad1, quad2, quad3, quad4;
		public Polygon quad1CH, quad2CH, quad3CH, quad4CH;

		public int getGestID() {
			return gestID;
		}

		public void setGestID(int gestID) {
			this.gestID = gestID;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public NewGesture() {
			init();
			strokes = new ArrayList<Stroke>();
			currentStroke = new Stroke();
			name = "";
			user = "";
		}
		
		public NewGesture(NewGesture g) {
			init();
			
			strokes = new ArrayList<Stroke>();
			currentStroke = new Stroke();
			name = g.getName();
			user = g.getUser();
			gestID = g.getGestID();

			for (Stroke s : g.getStrokes()) {
				for (Point p : s.getPoints()) {
					addPoint(p.x, p.y);
				}
				finalizeStroke();
			}
		}
		
		public NewGesture(String name) {
			init();
			strokes = new ArrayList<Stroke>();
			currentStroke = new Stroke();
			this.name = name;
			user = "";
		}

		public void init() {
			convexHull = null;
			extremeQuad = null;
			largestTriangle = null;
			largestQuad = null;
			extremeQuad = null;
			intersectCH1 = null;
			intersectCH2 = null;
			boundingBox = null;
			quad1 = null;
			quad2 = null;
			quad3 = null;
			quad4 = null;
			quad1CH = null;
			quad2CH = null;
			quad3CH = null;
			quad4CH = null;
			features = new NewFeatures();
			minX = new Point(Integer.MAX_VALUE,0);
			maxX = new Point(0,0);
			minY = new Point(0,Integer.MAX_VALUE);
			maxY = new Point(0,0);
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void addPoint(Point p) {
			currentStroke.add(p);
			updateExtremes(p);
		}
		
		public void addPoint(double x, double y) {
			Point p = new Point((int)x, (int)y);
			currentStroke.add(p);
			updateExtremes(p);
		}
		
		public void addStroke(Stroke s) {
			strokes.add(s);
		}
		
		public List<Stroke> getStrokes() {
			return strokes;
		}
		
		public void finalizeStroke() {
			strokes.add(currentStroke);
			currentStroke = new Stroke();
		}

		public int getNumPoints() {
			int numPts = 0;
			for (Stroke s : strokes)
				numPts += s.getPoints().size();

			return numPts;
		}
		
		public int getNumStrokes() {
			return strokes.size();
		}
		
		public double getLength() {
			double length = 0;

			for (Stroke s : strokes) {
				List<Point> pts = s.getPoints();
				int numPts = pts.size();
				for (int i = 1; i < numPts; ++i) {
					length += pts.get(i).distanceFrom(pts.get(i-1));
				}
			}
			return length;
		}
		
		public Point2D anotherCenter() {
			return new Point2D((maxX.x + minX.x) / 2, (maxY.y + minY.y) / 2);
		}
		
		public void translatePoints(int x, int y) {
			Point2D center = anotherCenter();
			int signalX, signalY;

			if (center.x > x)
				signalX = -1;
			else
				signalX = 1;
			
			if (center.y > y)
				signalY = -1;
			else
				signalY = 1;
			
			int difX = (int) Math.abs(center.x - x);
			int difY = (int) Math.abs(center.y - y);
			
			for (Stroke s : this.getStrokes()) {
				List<Point> pts = s.getPoints();
				int numPts = pts.size();
				for (int i = 0; i < numPts; i++) {
					Point p = pts.get(i);
					pts.set(i, p.translate(signalX * difX, signalY * difY));
				}
			}
		}
		
		public Polygon getConvexHull() {
			return convexHull;
		}

		public Polygon getAlphaShape() {
			return alphaShape;
		}

		public Polygon getAlphaShapeTest() {
			return alphaShapeTest;
		}

		public Polygon getLargestTriangle() {
			return largestTriangle;
		}

		public Polygon getLargestQuad() {
			return largestQuad;
		}

		public Polygon getEnclosingRect() {
			return enclosingRect;
		}
		
		public Polygon getExtremeQuad() {
			return extremeQuad;
		}
		
		public Polygon getIntersectCH1() {
			return intersectCH1;
		}

		public Polygon getIntersectCH2() {
			return intersectCH2;
		}
		
		public Polygon getBoundingBox() {
			return boundingBox;
		}
		
		public Polygon getQuad1() {
			return quad1;
		}

		public Polygon getQuad2() {
			return quad2;
		}

		public Polygon getQuad3() {
			return quad3;
		}

		public Polygon getQuad4() {
			return quad4;
		}
		
		public NewFeatures getFeatures() {
			return features;
		}
		
		public static double divide(double numerator, double denominator, boolean normalized) {
			if (numerator == 0)
				return 0;
			if (denominator == 0) {
				if (normalized)
					return 1;
				else
					return Float.MAX_VALUE;
			} else {
				double division = numerator / denominator;
				if (normalized && division > 1)
					return 1;
				if (normalized && division < 0)
					return 0;
				else
					return division;
			}
		}
		
		public void updateExtremes(Point p) {
			if (p.x < minX.x)
				minX = p;
			if (p.y < minY.y)
				minY = p;
			if (p.x > maxX.x)
				maxX = p;
			if (p.y > maxY.y)
				maxY = p;
		}
		
		public void calcFeatures() {
			convexHull = calcConvexHullByStrokes(strokes);  /// ---- Correlation weka
			largestTriangle = calcLargestTriangle(); /// ---- Correlation weka
			largestQuad = calcLargestQuad(); /// ---- Correlation weka
			enclosingRect = calcEnclosingRect(); /// ---- Correlation weka
			extremeQuad = calcExtremeQuad();
			boundingBox = calcBoundingBox();
//			alphaShape = calcAlphaShape();
			//alphaShapeTest = calcAlphaShapeTest("STE", -1);
			//calcAngleHistograms();
			
			calcRMS();
			calcMovement();
			calcIntersections();
			calcRatios();
//			calcCorrelation();
		}
		
		public void calcRatios() {
			double chPerim = convexHull.perimeter();
			double chArea = convexHull.area();
			//double ltPerim = largestTriangle.perimeter();
			double ltArea = largestTriangle.area();
			//double lqPerim = largestQuad.perimeter();
			double lqArea = largestQuad.area();
			double erPerim = enclosingRect.perimeter();
			double erArea = enclosingRect.area();
			double eqPerim = extremeQuad.perimeter();
			double eqArea = extremeQuad.area();
			double bbArea = boundingBox.area();
			
			//features.setCircleR(divide(chPerim * chPerim, chArea, false));
			features.setCircleR(divide(chArea, chPerim * chPerim, true));  /// ---- Correlation weka
			features.setRectR1(divide(chArea, erArea, true)); /// ---- Correlation weka
			features.setRectR2(divide(chPerim, erPerim, true));/// ---- Correlation weka
			features.setRectR3(divide(lqArea, erArea, true));
			/*
			  features.setRectR4(divide(lqArea, chArea, true));		 
			features.setRectR5(divide(lqPerim, chPerim, true));
			features.setTriR1(divide(ltArea, chArea, true));
			features.setTriR2(divide(ltPerim, chPerim, true));
			*/
			features.setTriR3(divide(ltArea, lqArea, true)); /// ---- Correlation weka
			
			List<Point2D> erPoints = enclosingRect.getPoints();
			if (erPoints.size() != 0) {
				Point2D p1 = erPoints.get(0);
				Point2D p2 = erPoints.get(1);
				Point2D p3 = erPoints.get(2);
				
				double width = p1.distanceTo(p2);
				double height = p1.distanceTo(p3);
				
				if (width < height)   /// ---- Correlation weka
					features.setAspectR(divide(width, height, true));
				else
					features.setAspectR(divide(height, width, true));
			}
			
			features.setFillingR(divide(getLength(), chPerim, false));  /// ---- Correlation weka
			//features.setEqR1(divide(eqPerim * eqPerim, eqArea, false));
			features.setEqR1(divide(eqArea, eqPerim * eqPerim, true));  /// ---- Correlation weka
			//features.setEqR2(divide(eqArea, chArea, true));
			//features.setEqR3(divide(eqArea, lqArea, true));
			
			//features.setChR1(divide(intersectCH1.area(), chArea, true));
			features.setChR2(divide(intersectCH2.area(), chArea, true)); /// ---- Correlation weka
			//features.setChR3(divide(intersectCH2.area(), intersectCH1.area(), false));
			
			features.setBbchR(divide(chArea, bbArea, true));  /// ---- Correlation weka
			
			calcQuadrantRatios();
		}
		
		public void calcMovement() {
			double totalX = 0f, totalY = 0f;
			for (Stroke s : strokes) {
				int numPoints = s.getPoints().size() - 1;
				List<Point> pts = s.getPoints();
				for (int i = 0; i < numPoints; i++) {
					totalX += Math.abs(pts.get(i).x - pts.get(i+1).x);
					totalY += Math.abs(pts.get(i).y - pts.get(i+1).y);
				}
			}

			//features.setMovementX(divide(maxX.x - minX.x, totalX, true));
			features.setMovementY(divide(maxY.y - minY.y, totalY, true));
		}
		
		public void calcIntersections() {
			if (convexHull.getNumPoints() == 0) {
				intersectCH1 = new Polygon();
				intersectCH2 = new Polygon();
				return;
			}
			Point2D center = convexHull.getCentroid();
			//Point2D center = largestTriangle.getCentroid();
			
			Polygon sub1 = convexHull.getSubPolygon(center, 0.75f);
			Polygon sub2 = convexHull.getSubPolygon(center, 0.5f);
			
			ArrayList<Point2D> subPoints1 = (ArrayList<Point2D>) sub1.getPoints();
			ArrayList<Point2D> subPoints2 = (ArrayList<Point2D>) sub2.getPoints();
			
			List<Point2D> intersecPoints1 = new ArrayList<Point2D>();
			List<Point2D> intersecPoints2 = new ArrayList<Point2D>();
			
			int N2 = subPoints1.size();
			int N3 = subPoints2.size();
			
			for (Stroke s : strokes) {
				ArrayList<Point> strokePoints = (ArrayList<Point>) s.getPoints();
				int N1 = strokePoints.size();
				for (int i = 1; i < N1; i++) {
					Point p1 = strokePoints.get(i-1);
					Point p2 = strokePoints.get(i);
					for (int j = 1; j < N2; j++) {
						Point2D p3 = subPoints1.get(j-1);
						Point2D p4 = subPoints1.get(j);
						
						Point2D p5 = Function.intersection(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
						
						if (p5 != null)
							intersecPoints1.add(p5);
					}
					for (int j = 1; j < N3; j++) {
						Point2D p3 = subPoints2.get(j-1);
						Point2D p4 = subPoints2.get(j);
						
						Point2D p5 = Function.intersection(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y);
						
						if (p5 != null)
							intersecPoints2.add(p5);
					}
				}
			}
			
			intersectCH1 = calcConvexHull2D(intersecPoints1);
			intersectCH2 = calcConvexHull2D(intersecPoints2);
		}
		
		public void calcQuadrantRatios() {
			// List of points of the gesture that belong in the quadrant
			List<Point> gestQuad1Pts = new ArrayList<Point>();
			List<Point> gestQuad2Pts = new ArrayList<Point>();
			List<Point> gestQuad3Pts = new ArrayList<Point>();
			List<Point> gestQuad4Pts = new ArrayList<Point>();
			
			double gestQuad1TotalLength, gestQuad2TotalLength, gestQuad3TotalLength, gestQuad4TotalLength;
			gestQuad1TotalLength = gestQuad2TotalLength = gestQuad3TotalLength = gestQuad4TotalLength = 0;
			
			for (Stroke s : strokes) {
				ArrayList<Point> strokePoints = (ArrayList<Point>) s.getPoints();
				int N = strokePoints.size();
				for (int i = 1; i < N; i++) {
					Point p1 = strokePoints.get(i-1);
					Point p2 = strokePoints.get(i);
					
					if (isPointInsidePoly(p1, quad1)) {
						gestQuad1Pts.add(p1);
						if (isPointInsidePoly(p2, quad1)) {
							gestQuad1Pts.add(p2);
							gestQuad1TotalLength += p1.distanceFrom(p2);
						}
					}
					else if (isPointInsidePoly(p1, quad2)) {
						gestQuad2Pts.add(p1);
						if (isPointInsidePoly(p2, quad2)) {
							gestQuad2Pts.add(p2);
							gestQuad2TotalLength += p1.distanceFrom(p2);
						}
					}
					else if (isPointInsidePoly(p1, quad3)) {
						gestQuad3Pts.add(p1);
						if (isPointInsidePoly(p2, quad3)) {
							gestQuad3Pts.add(p2);
							gestQuad3TotalLength += p1.distanceFrom(p2);
						}
					}
					else if (isPointInsidePoly(p1, quad4)) {
						gestQuad4Pts.add(p1);
						if (isPointInsidePoly(p2, quad4)) {
							gestQuad4Pts.add(p2);
							gestQuad4TotalLength += p1.distanceFrom(p2);
						}
					}
					
				}
			}

			double gestTotalLength = getLength();

			features.setQuad1FillR(divide(gestQuad1TotalLength, gestTotalLength, true));  /// ---- Correlation weka
			features.setQuad2FillR(divide(gestQuad2TotalLength, gestTotalLength, true)); /// ---- Correlation weka
			features.setQuad3FillR(divide(gestQuad3TotalLength, gestTotalLength, true));/// ---- Correlation weka
			features.setQuad4FillR(divide(gestQuad4TotalLength, gestTotalLength, true));/// ---- Correlation weka
			
//			quad1CH = calcConvexHull(gestQuad1Pts);
//			quad2CH = calcConvexHull(gestQuad2Pts);
//			quad3CH = calcConvexHull(gestQuad3Pts);
//			quad4CH = calcConvexHull(gestQuad4Pts);
	//
//			features.setQuad1FillR(divide(gestQuad1TotalLength, quad1CH.perimeter(), false));
//			features.setQuad2FillR(divide(gestQuad2TotalLength, quad2CH.perimeter(), false));
//			features.setQuad3FillR(divide(gestQuad3TotalLength, quad3CH.perimeter(), false));
//			features.setQuad4FillR(divide(gestQuad4TotalLength, quad4CH.perimeter(), false));
		}
		
		public boolean isPointInsidePoly(Point p, Polygon poly) {
			double px = p.x;
			double py = p.y;
			
			double minX = poly.getMinX();
			double maxX = poly.getMaxX();
			double minY = poly.getMinY();
			double maxY = poly.getMaxY();
			
			if (px >= minX && px <= maxX && py >= minY && py <= maxY)
				return true;
			else
				return false;
		}
		
//		public void calcCorrelation() {
//			int numPoints = this.points.size();
	//
//			if (numPoints != 0 && convexHull.getNumPoints() != 0) {
//				double[][] dpts = new double[numPoints][2];
	//
//				for (int i = 0; i < numPoints; i++) {
//					Point p = points.get(i);
//					dpts[i][0] = p.x;
//					dpts[i][1] = p.y;
//				}
	//
//				PearsonsCorrelation pc = new PearsonsCorrelation(dpts);
	//
//				double correlation = pc.getCorrelationMatrix().getEntry(0,1);
	//
//				if (Double.isNaN(correlation))
//					features.setCorrelation(0);
//				else
//					features.setCorrelation(Math.abs(correlation));
//			}
//		}

		public Polygon calcConvexHullByStrokes(List<Stroke> strokes) {
			if (getNumPoints() == 0)
				return new Polygon();

			int numPts = 0;
			for (Stroke s : strokes)
				numPts += s.getPoints().size();

			Point2D[] pts = new Point2D[numPts];
			int i = 0;
			for (Stroke s : strokes) {
				for (Point p : s.getPoints()) {
					pts[i] = new Point2D(p.x, p.y);
					i++;
				}
			}

			return calcConvexHullMethod(pts);
		}

		public Polygon calcConvexHull(List<Point> points) {
			if (points.size() == 0)
				return new Polygon();

			int N = points.size();
	        Point2D[] pts = new Point2D[N];
	        for (int i = 0; i < N; i++)
	        	pts[i] = new Point2D(points.get(i).x, points.get(i).y);

	        return calcConvexHullMethod(pts);
		}
		
		public Polygon calcConvexHull2D(List<Point2D> points) {
			if (points.size() == 0)
				return new Polygon();
			
			int N = points.size();
	        Point2D[] pts = new Point2D[N];
	        for (int i = 0; i < N; i++)
	        	pts[i] = new Point2D(points.get(i).x(), points.get(i).y());
			
	        return calcConvexHullMethod(pts);
		}
		
		private Polygon calcConvexHullMethod(Point2D[] pts) {

			int N = pts.length;
			Stack<Point2D> hull = new Stack<Point2D>();
			// preprocess so that points[0] has lowest y-coordinate; break ties by x-coordinate
	        // points[0] is an extreme point of the convex hull
	        // (alternatively, could do easily in linear time)
	        Arrays.sort(pts);
	        
	        Arrays.sort(pts, 1, N, pts[0].POLAR_ORDER);

	        hull.push(pts[0]);       // p[0] is first extreme point

	        // find index k1 of first point not equal to points[0]
	        int k1;
	        for (k1 = 1; k1 < N; k1++)
	            if (!pts[0].equals(pts[k1]))
	            	break;
	        if (k1 == N)
	        	return new Polygon();        // all points equal

	        // find index k2 of first point not collinear with points[0] and points[k1]
	        int k2;
	        for (k2 = k1 + 1; k2 < N; k2++)
	            if (Point2D.ccw(pts[0], pts[k1], pts[k2]) != 0)
	            	break;
	        hull.push(pts[k2-1]);    // points[k2-1] is second extreme point

	        // Graham scan; note that points[N-1] is extreme point different from points[0]
	        for (int i = k2; i < N; i++) {
	            Point2D top = hull.pop();
	            while (Point2D.ccw(hull.peek(), top, pts[i]) <= 0) {
	                top = hull.pop();
	                if (hull.empty())
	                	return new Polygon();
	            }
	            hull.push(top);
	            hull.push(pts[i]);
	        }
	        
	        //assert isConvex();
	        
	        Stack<Point2D> s = new Stack<Point2D>();
	        for (Point2D p : hull) { 
	        	s.push(p);
	        }
	        
	        Polygon poly = new Polygon();
	        
	        for (Point2D p : s) {
	        	poly.addPoint(p);
	        }
	        poly.addPoint(s.firstElement());
	        
	        return poly;
		}

		public Polygon calcLargestTriangle() {
			
			Polygon lt = new Polygon();
			
			if (convexHull.getNumPoints() != 0) {
				int ia, ib, ic, i;
				int ripa = 0, ripb = 0, ripc = 0; // indexes of rooted triangle
				double area, triArea;
				
				List<Point2D> pts = convexHull.getPoints();
				int numPts = pts.size();
				
				if (numPts <= 3) {
					largestTriangle = new Polygon();
					for (i = 0; i < numPts; i++) {
						largestTriangle.addPoint(pts.get(i));
					}
					for (i = numPts; i < 4; i++) {
						largestTriangle.addPoint(pts.get(0));
					}
				}
				
				// computes one rooted triangle with root in the first point of the convex hull
				ia = 0;
				area = 0;
				triArea = 0;
				for(ib = 1; ib <= numPts - 2; ib++) {
					if (ib >= 2) {
						ic = ib + 1;
					}
					else {
						ic = 2;
					}
		            Object[] res =  Function.compRootedTri(pts, ia, ib, ic, numPts);
		            area = (Double) res[0];
		            ic = (Integer) res[1];
					if (area > triArea) {
						triArea = area;
						ripa = ia;
						ripb = ib;
						ripc = ic;
					}
				} // ripa, ripb and ripc are the indexes of the points of the rooted triangle
				
				// computes other triangles and choose the largest one
				double finalArea = triArea;
				int pf0, pf1, pf2;   // indexes of the final points
				int fipa=0, fipb=0, fipc=0; 
				int ib0;
				pf0 = ripa;
				pf1 = ripb;
				pf2 = ripc;
				
				for(ia = ripa+1; ia <= ripb; ia++) {
					triArea = 0;
					if (ia == ripb) {
						ib0 = ripb + 1;
					}
					else {
						ib0 = ripb;
					}
					area = 0;
					for(ib = ib0; ib <= ripc; ib++) {
						if (ib == ripc) {
							ic = ripc + 1;
						}
						else {
							ic = ripc;
						}
		                Object[] res = Function.compRootedTri(pts, ia, ib, ic, numPts);
		                area = (Double) res[0];
		                ic=(Integer) res[1];
						if (area > triArea) {
							triArea = area;
							fipa = ia;
							fipb = ib;
							fipc = ic;
						}
					}
					if(triArea > finalArea) {
						finalArea = triArea;
						pf0 = fipa;
						pf1 = fipb;
						pf2 = fipc;
					}
				}
				
				// Tranfer the points to a polygon
				lt.addPoint(pts.get(pf0));
				lt.addPoint(pts.get(pf1));
				lt.addPoint(pts.get(pf2));
				lt.addPoint(pts.get(pf0));
			}
			
			return lt;
		}

		public Polygon calcLargestQuad() {
			
			Polygon lq = new Polygon();
			
			if (convexHull.getNumPoints() != 0) {
				int i, ia, ib, ic, ic0;
				int ripa=0, ripb=0, ripc=0; // indexes for rooted triangle
				double area, triArea;
				List<Point2D> pts = convexHull.getPoints();
				int numPts = pts.size();
				
				if (numPts <= 4) {
					largestQuad = new Polygon();
					for (i=0; i < numPts; i++)
						largestQuad.addPoint(pts.get(i));
					for (i= numPts; i < 5; i++)
						largestQuad.addPoint(pts.get(0));
				}
				
				// computes one rooted triangle        
				ia = 0;
				area = 0;
				triArea = 0;
				for(ib = 1; ib <= numPts - 2; ib++) {
					if (ib >= 2) {
						ic = ib + 1;
					}
					else {
						ic = 2;
					}
		            Object[] res = Function.compRootedTri(pts, ia, ib, ic, numPts);
		            area = (Double) res[0];
		            ic = (Integer) res[1];
					
					if (area > triArea) {
						triArea = area;
						ripa = ia;
						ripb = ib;
						ripc = ic;
					}
				}
				
				// computes the rooted quadrilateral based on a rooted triangle
				int fipa = 0, fipb = 0, fipc = 0, fipd = 0; // indexes for final values
				int id, ib0;
				double quadArea;
				
				quadArea = 0;
				for(ib = ripa + 1; ib <= ripb; ib++) {
					if (ib == ripb) {
						ic0 = ripb + 1;
					}
					else {
						ic0 = ripb;
					}
					
					for(ic = ic0; ic <= ripc; ic++) {
						if (ic == ripc) {
							id = ripc + 1;
						}
						else {
							id = ripc;
						}
						
		                Object[] res = Function.compRootedQuad(pts, ia, ib, ic, id, numPts);
		                area = (Double) res[0];
		                id = (Integer) res[1];
						if (area > quadArea) {
							quadArea = area;
							fipa = ia;
							fipb = ib;
							fipc = ic;
							fipd = id;
						}
					}
				}
				
				// computes other quadrilaterals and choose the largest one
				int pf0, pf1, pf2, pf3;
				double finalArea = quadArea;
				pf0 = fipa;
				pf1 = fipb;
				pf2 = fipc;
				pf3 = fipd;
				ripa = fipa;
				ripb = fipb;
				ripc = fipc;
				int ripd = fipd;
				
				for(ia = ripa + 1; ia <= ripb; ia++) {
					if (ia == ripb) {
						ib0 = ripb + 1;
					}
					else {
						ib0 = ripb;
					}
					
					quadArea = 0;
					area = 0;
					for(ib = ib0; ib <= ripc; ib++) {
						if (ib == ripc) {
							ic0 = ripc + 1;
						}
						else {
							ic0 = ripc;
						}
						
						for (ic = ic0; ic <= ripd; ic++) {
							if (ic == ripd) {
								id = ripd + 1;
							}
							else {
								id = ripd;
							}
							
		                    Object[] res = Function.compRootedQuad(pts, ia, ib, ic, id, numPts);
		                    area = (Double) res[0];
		                    id = (Integer) res[1];
							if (area > quadArea) {
								quadArea = area;
								fipa = ia;
								fipb = ib;
								fipc = ic;
								fipd = id;
							}
						}
					}
					
					if(quadArea > finalArea) {
						finalArea = quadArea;
						pf0 = fipa;
						pf1 = fipb;
						pf2 = fipc;
						pf3 = fipd;
					}
				}
				
				// Tranfer the points to a polygon
				lq.addPoint(pts.get(pf0));
				lq.addPoint(pts.get(pf1));
				lq.addPoint(pts.get(pf2));
				lq.addPoint(pts.get(pf3));
				lq.addPoint(pts.get(pf0));
			}
			
			return lq;
		}
		
		public Polygon calcEnclosingRect() {
			
			Polygon er = new Polygon();
			
			if (convexHull.getNumPoints() != 0) {
				List<Point2D> pts = convexHull.getPoints();
				int numPts = pts.size();
				double minx = 0, miny = 0, maxx = 0, maxy = 0;
				int minxp = 0, maxxp = 0, maxyp = 0;
				double ang,dis;
				double xx,yy;
				double area;
				double min_area = 0.0;
				double p1x = 0, p1y = 0, p2x = 0, p2y = 0, p3x = 0, p3y = 0, p4x = 0, p4y = 0;
				
				if (numPts < 2) {  // is just a point
					enclosingRect = new Polygon();
					enclosingRect.addPoint(pts.get(0));
					enclosingRect.addPoint(pts.get(0));
					enclosingRect.addPoint(pts.get(0));
					enclosingRect.addPoint(pts.get(0));
					enclosingRect.addPoint(pts.get(0));
				}
				else if (numPts < 3) {  // is a line with just two points
					enclosingRect = new Polygon();
					enclosingRect.addPoint(pts.get(0));
					enclosingRect.addPoint(pts.get(1));
					enclosingRect.addPoint(pts.get(1));
					enclosingRect.addPoint(pts.get(0));
					enclosingRect.addPoint(pts.get(0));
				}
				else {  // ok it's normal :-)
					for(int i = 0; i < numPts - 1; i++) {
						for(int a = 0; a < numPts; a++) {
							
							Vector v1 = new Vector(pts.get(i), pts.get(i+1));
							Vector v2 = new Vector(pts.get(i), pts.get(a));
							ang = Function.angle(v1, v2);
							
							dis = v2.length();
							xx = dis * Math.cos(ang);
							yy = dis * Math.sin(ang);
							
							if(a == 0) {
								minx = maxx = xx;
								miny = maxy = yy;
								minxp = maxxp = maxyp = 0;
							}
							if(xx < minx) {
								minxp = a;
								minx = xx;
							}
							if(xx > maxx) {
								maxxp = a;
								maxx = xx;
							}
							if(yy < miny) {
								miny = yy;
							}
							if(yy > maxy) {
								maxyp = a;
								maxy = yy;
							}
							//delete v1;
							//delete v2;        
						}   
						Point2D p1 = Function.closest(pts.get(i), pts.get(i+1), pts.get(minxp));
						Point2D p2 = Function.closest(pts.get(i), pts.get(i+1), pts.get(maxxp));
						
						Point2D paux = new Point2D((pts.get(i)).x + 100, (pts.get(i)).y);
						Vector v3 = new Vector(pts.get(i), paux);
						Vector v4 = new Vector(pts.get(i), pts.get(i+1));
						ang = Function.angle(v3, v4);
						
						double f1x = p1.x + 100 * Math.cos(ang + Function.M_PI_2);
						double f1y = p1.y + 100 * Math.sin(ang + Function.M_PI_2);
						double f2x = p2.x + 100 * Math.cos(ang + Function.M_PI_2);
						double f2y = p2.y + 100 * Math.sin(ang + Function.M_PI_2);
						
						Point2D paux1 = new Point2D(f1x, f1y);
						Point2D paux2 = new Point2D(f2x, f2y);
						
						Point2D p3 = Function.closest(p2,paux2, pts.get(maxyp));
						Point2D p4 = Function.closest(p1,paux1, pts.get(maxyp));
						
						area = Function.quadArea(p1,p2,p3,p4);
						
						if ((i==0) || (area < min_area))
						{
							min_area = area;
							p1x = p1.x;
							p1y = p1.y;
							p2x = p2.x;
							p2y = p2.y;
							p3x = p3.x;
							p3y = p3.y;
							p4x = p4.x;
							p4y = p4.y;
						}
						
					}
					er.addPoint(new Point2D(p1x, p1y));
					er.addPoint(new Point2D(p2x, p2y));
					er.addPoint(new Point2D(p3x, p3y));
					er.addPoint(new Point2D(p4x, p4y));
					er.addPoint(new Point2D(p1x, p1y));
				}
			}
			
			return er;
		}
		
		public Polygon calcExtremeQuad() {
			
			Polygon eq = new Polygon();
			
			if (convexHull.getNumPoints() != 0) {
				eq.addPoint(new Point2D(minX.x, minX.y));
				eq.addPoint(new Point2D(minY.x, minY.y));
				eq.addPoint(new Point2D(maxX.x, maxX.y));
				eq.addPoint(new Point2D(maxY.x, maxY.y));
				eq.addPoint(new Point2D(minX.x, minX.y));
			}
			
			return eq;
		}
		
		public Polygon calcBoundingBox() {
			Polygon bb = new Polygon();
			
			quad1 = new Polygon();
			quad2 = new Polygon();
			quad3 = new Polygon();
			quad4 = new Polygon();
			
			if (convexHull.getNumPoints() != 0) {
				Point2D p1 = new Point2D(minX.x, minY.y);
				Point2D p2 = new Point2D(maxX.x, minY.y);
				Point2D p3 = new Point2D(maxX.x, maxY.y);
				Point2D p4 = new Point2D(minX.x, maxY.y);
				
				bb.addPoint(p1);
				bb.addPoint(p2);
				bb.addPoint(p3);
				bb.addPoint(p4);
				bb.addPoint(p1);
				
				Point2D c = bb.anotherCenter();
				Point2D p5 = new Point2D(c.x, minY.y);
				Point2D p6 = new Point2D(maxX.x, c.y);
				Point2D p7 = new Point2D(c.x, maxY.y);
				Point2D p8 = new Point2D(minX.x, c.y);
				
				quad1.addPoint(p1);
				quad1.addPoint(p5);
				quad1.addPoint(c);
				quad1.addPoint(p8);
				quad1.addPoint(p1);
				
				quad2.addPoint(p5);
				quad2.addPoint(p2);
				quad2.addPoint(p6);
				quad2.addPoint(c);
				quad2.addPoint(p5);
				
				quad3.addPoint(c);
				quad3.addPoint(p6);
				quad3.addPoint(p3);
				quad3.addPoint(p7);
				quad3.addPoint(c);
				
				quad4.addPoint(p8);
				quad4.addPoint(c);
				quad4.addPoint(p7);
				quad4.addPoint(p4);
				quad4.addPoint(p8);
			}
			
			return bb;
		}
		/*
		public Polygon calcAlphaShape(String type, int normalisedValue) {
			Polygon as = new Polygon();
			ArrayList<VPoint> boundary = new AlphaShape().getAlphaShape(this, type, normalisedValue);
//			System.out.println("AS: " + VoronoiShared.calculateAreaOfShape(boundary));
			if (boundary != null) {
				for (VPoint v : boundary)
					as.addPoint(new Point2D(v.x,v.y));
			}
//			System.out.println("AS Poly area: " + as.area());
			return as;
		}

		public Polygon calcAlphaShapeTest(String type, int normalisedValue) {
			Polygon as = new Polygon();
			ArrayList<VPoint> boundary = new AlphaShape().getAlphaShapeTest(this, type, normalisedValue);

			if (boundary != null) {
				for (VPoint v : boundary)
					as.addPoint(new Point2D(v.x,v.y));
			}
		
//			double areaAS = as.area();
//			double areaCH = convexHull.area();
//			double ratio = Gesture.divide(areaAS, areaCH, true);
//			if (ratio > 1.1) {
//				System.out.println("----------------");
//				System.out.println("AS Test area: " + areaAS);
//				System.out.println("CH area: " + areaCH);
//				System.out.println("ratio: " + ratio);
//				System.out.println("Voronoi area: " + VoronoiShared.calculateAreaOfShape(boundary));
//			}
//			System.out.println("AS Test Poly area: " + as.area());
			return as;
		}
		 	*/
		int numBins = 8;
		double inv_deltaphi = numBins / (2 * Math.PI);
		
		/*
		public void calcAngleHistograms() {
			List<Stroke> resampledStrokes = new ArrayList<Stroke>();
			Function.resampleDistPts(5, strokes, resampledStrokes);
			List<Double> bins = new ArrayList<Double>();
			for (int i = 0; i < numBins; i++)
				bins.add(0.0);

			List<Point> binsVectors = new ArrayList<Point>();
			binsVectors.add(new Point(-1,0));
			binsVectors.add(new Point(-1,-1));
			binsVectors.add(new Point(0,-1));
			binsVectors.add(new Point(1,-1));
			binsVectors.add(new Point(1,0));
			binsVectors.add(new Point(1,1));
			binsVectors.add(new Point(0,1));
			binsVectors.add(new Point(-1,1));

			// Absolute angle histogram
			for (Stroke s : resampledStrokes) {
				List<Point> pts = s.getPoints();
				int numPts = pts.size();
				for (int i = 1; i < numPts; i++) {
					Point p0 = pts.get(i-1);
					Point p1 = pts.get(i);

					// Angle between the vector formed by p0 and p1, and the X axis
					double angle = Function.angle(p0, p1);
					int index = intervalIndex(angle, 0);
					// Angle between the vector formed by p0 and p1, and the closest bin vector
					double angleWithBinVector = Function.unsignedAngleFromVectors(new Point(p1.x - p0.x, p1.y - p0.y), binsVectors.get(index));
					double w1 = 1 - (angleWithBinVector / (Math.PI / 4));
					if (w1 < 0)
						w1 = 0;

					double w2 = 1 - w1;
					bins.set(index, bins.get(index) + w1);
					bins.set((index + 1) % numBins, bins.get((index + 1) % numBins) + w2);

//					System.out.println("---------");
//					System.out.println("angle: " + Math.toDegrees(angle));
//					System.out.println("index: " + index);
//					System.out.println("angle with bin vector: " + Math.toDegrees(angleWithBinVector));
//					System.out.println("w1: " + w1);
//					System.out.println("w2: " + w2);
				}
			}

			int numSegments = 0;
			for (Stroke s : resampledStrokes)
				numSegments += s.getPoints().size() - 1;

			features.setAa1(divide((bins.get(0) + bins.get(4)), numSegments, true));
			features.setAa2(divide((bins.get(1) + bins.get(5)), numSegments, true));
			features.setAa3(divide((bins.get(2) + bins.get(6)), numSegments, true));
			features.setAa4(divide((bins.get(3) + bins.get(7)), numSegments, true));

//			if (Double.isNaN(f1) || Double.isInfinite(f1)) {
//				System.out.println(name + " - " + gestID);
//			}
	//
//			System.out.println("Num segments: " + numSegments);
//			for (int i = 0; i < 4; i++) {
//				System.out.println("f" + (i+1) + ": " + bins.get(i) + " + " + bins.get(i+4));
//			}
	//
//			BufferedImage resampled = new BufferedImage(800,800, BufferedImage.TYPE_INT_RGB);
//	        Graphics2D g2d = resampled.createGraphics();
//	        g2d.setStroke(new BasicStroke(1));
//	        g2d.setColor(Color.WHITE);
//	        for (Stroke s : resampledStrokes) {
//	            List<Point> pts = s.getPoints();
//	            for (int i = 1; i < pts.size(); i++) {
//	                Point p0 = pts.get(i-1);
//	                Point p1 = pts.get(i);
//	                g2d.drawLine((int)p0.x,(int)p0.y,(int)p1.x,(int)p1.y);
//	            }
//	        }
//	        for (Stroke s : resampledStrokes) {
//	            for (Point p : s.getPoints()) {
//	                int px = (int) p.x;
//	                int py = (int) p.y;
//					resampled.setRGB(px,py,Color.RED.getRGB());
//	            }
//	        }
	//
//			try {
//	            ImageIO.write(resampled, "png", new File("datasets/resampled.png"));
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }

			// Relative angle histogram
			bins.clear();
			for (int i = 0; i < numBins; i++)
				bins.add(0.0);

			for (Stroke s : resampledStrokes) {
				List<Point> pts = s.getPoints();
				int numPts = pts.size() - 2;
				for (int i = 2; i < numPts; i++) {
					Point prev = pts.get(i-1);
					Point p = pts.get(i);
					Point next = pts.get(i+1);

//					System.out.println("prev: " + prev.x + " " + prev.y);
//					System.out.println("p: " + p.x + " " + p.y);
//					System.out.println("next: " + next.x + " " + next.y);

					Point prevToP = new Point(p.x - prev.x, p.y - prev.y);
					Point pToNext = new Point(next.x - p.x, next.y - p.y);

					double num = prevToP.x * pToNext.x + prevToP.y * pToNext.y;
					double denom = Math.sqrt(prevToP.x * prevToP.x + prevToP.y * prevToP.y)
								   * Math.sqrt(pToNext.x * pToNext.x + pToNext.y * pToNext.y);
					double value = num / denom;
//					System.out.println("prevToP: " + prevToP.x + " " + prevToP.y);
//					System.out.println("pToNext: " + pToNext.x + " " + pToNext.y);
//					System.out.println("num: " + num);
//					System.out.println("denom: " + denom);
//					System.out.println("value: " + value);
					// This is needed to avoid NaN values. Double precision creates numbers like 1.000000002
					if (value > 1.0)
						value = 1.0;
					else if (value < -1.0)
						value = -1.0;

					double firstAngle = Math.acos(value);

					prev = pts.get(i-2);
					next = pts.get(i+2);

					prevToP = new Point(p.x - prev.x, p.y - prev.y);
					pToNext = new Point(next.x - p.x, next.y - p.y);

					num = prevToP.x * pToNext.x + prevToP.y * pToNext.y;
					denom = Math.sqrt(prevToP.x * prevToP.x + prevToP.y * prevToP.y)
							* Math.sqrt(pToNext.x * pToNext.x + pToNext.y * pToNext.y);
					value = num / denom;
					// This is needed to avoid NaN values. Double precision creates numbers like 1.000000002
					if (value > 1.0)
						value = 1.0;
					else if (value < -1.0)
						value = -1.0;

					double secondAngle = Math.acos(value);

					double finalAngle = 0.25 * firstAngle + 0.75 * secondAngle;
					Point v = new Point(Math.cos(finalAngle), Math.sin(finalAngle));

					int index = intervalIndex(finalAngle, 4);
					double angleWithBinVector = Function.unsignedAngleFromVectors(v, binsVectors.get(index));
					double w1 = 1 - (angleWithBinVector / (Math.PI / 4));
					if (w1 < 0)
						w1 = 0;

					double w2 = 1 - w1;
					bins.set(index, bins.get(index) + w1);
					bins.set((index + 1) % numBins, bins.get((index + 1) % numBins) + w2);

//					System.out.println("---------");
//					System.out.println("first angle: " + Math.toDegrees(firstAngle));
//					System.out.println("second angle: " + Math.toDegrees(secondAngle));
//					System.out.println("final angle: " + Math.toDegrees(finalAngle));
//					System.out.println("index: " + index);
//					System.out.println("angle with bin vector: " + Math.toDegrees(angleWithBinVector));
//					System.out.println("w1: " + w1);
//					System.out.println("w2: " + w2);
				}
			}

			numSegments = 0;
			int numSegmentsInStroke;
			for (Stroke s : resampledStrokes) {
				numSegmentsInStroke = s.getPoints().size() - 1 - 3;
				if (numSegmentsInStroke > 0)
					numSegments += numSegmentsInStroke;
			}

			features.setRa1(divide(bins.get(4), numSegments, true));
			features.setRa2(divide(bins.get(5), numSegments, true));
			features.setRa3(divide(bins.get(6), numSegments, true));
			features.setRa4(divide(bins.get(7), numSegments, true));

//			System.out.println("----Absolute----");
//			System.out.println("f1: " + f1);
//			System.out.println("f2: " + f2);
//			System.out.println("f3: " + f3);
//			System.out.println("f4: " + f4);
//			System.out.println("----Relative----");
//			System.out.println("f5: " + f5);
//			System.out.println("f6: " + f6);
//			System.out.println("f7: " + f7);
//			System.out.println("f8: " + f8);

//			System.out.println("Num segments: " + numSegments);
//			for (int i = 4; i < 8; i++) {
//				System.out.println("f" + i + ": " + bins.get(i));
//			}
		}
			
		*/
		public int intervalIndex(double angle, int firstSlot) {
			int angleIndex = (int) ((angle + Math.PI) * inv_deltaphi);
			if (angleIndex == numBins)
				angleIndex = firstSlot;

			return angleIndex;
		}
		
		
		//Nova feature RMS
		public void calcRMS(){
			double sum = 0;
			for (Stroke s : strokes) {
				int numPoints = s.getPoints().size() - 1;
				List<Point> pts = s.getPoints();
				for (int i = 0; i < numPoints; i++) {
					sum += (pts.get(i).y * pts.get(i).y);
				}
				double rms = sum/numPoints;
				features.setRMS(Math.abs(rms));
				return;
			}
			
		}
	
}
