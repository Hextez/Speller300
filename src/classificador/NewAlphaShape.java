package classificador;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import mcali.Function;
import mcali.Gesture;
import mcali.Stroke;
import signalprocesser.voronoi.VPoint;
import signalprocesser.voronoi.VoronoiAlgorithm;
import signalprocesser.voronoi.representation.AbstractRepresentation;
import signalprocesser.voronoi.representation.RepresentationFactory;
import signalprocesser.voronoi.representation.RepresentationInterface;
import signalprocesser.voronoi.representation.triangulation.TriangulationRepresentation;
import signalprocesser.voronoi.statusstructure.VLinkedNode;
import tests.AlphaShape;
import tests.AlphaShape.TestRepresentationWrapper;

public class NewAlphaShape {
	
	public NewAlphaShape() {}

    private void nonconvex() {
        ArrayList<VPoint> points = new ArrayList<VPoint>();
        AbstractRepresentation representation = RepresentationFactory.createTriangulationRepresentation();
        TestRepresentationWrapper representationwrapper = new TestRepresentationWrapper();
        BufferedImage img = new BufferedImage(800,400,BufferedImage.TYPE_INT_RGB);

        NewReader NewReader = new NewReader();
        NewReader.readDollarPN(new File("datasets/A.xml"), null);
        NewGesture g = NewReader.getGesture();
        List<Stroke> strokes = g.getStrokes();
        for (Stroke s : strokes) {
            for (mcali.Point p : s.getPoints()) {
                img.setRGB((int)p.x,(int)p.y,Color.WHITE.getRGB());
                points.add(representation.createPoint((int)p.x,(int)p.y));
            }
        }

        TriangulationRepresentation triangularrep = (TriangulationRepresentation) representation;
        TriangulationRepresentation.CalcCutOff calccutoff = new TriangulationRepresentation.CalcCutOff() {
            public int calculateCutOff(TriangulationRepresentation rep) {
//                return rep.getMaxLengthOfSmallestTriangleEdge();
                return rep.getMaxLengthOfMinimumSpanningTree();
            }
        };
        triangularrep.setCalcCutOff(calccutoff);
        representationwrapper.innerrepresentation = representation;
        VoronoiAlgorithm.generateVoronoi(representationwrapper, points);

        Graphics2D g2d = img.createGraphics();
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.RED);
        ArrayList<VPoint> boundaryPts = triangularrep.getPointsFormingOutterBoundary();
        int numPts = boundaryPts.size();
        for (int i = 1; i < numPts; i++) {
            VPoint p0 = boundaryPts.get(i-1);
            VPoint p1 = boundaryPts.get(i);
            g2d.drawLine(p0.x,p0.y,p1.x,p1.y);
        }

        try {
            ImageIO.write(img, "png", new File("bla.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<VPoint> getAlphaShape(NewGesture newGesture, final String type, final int normalisedValue) {
        ArrayList<VPoint> points = new ArrayList<VPoint>();
        AbstractRepresentation representation = RepresentationFactory.createTriangulationRepresentation();
        TestRepresentationWrapper representationwrapper = new TestRepresentationWrapper();

        for (Stroke s : newGesture.getStrokes()) {
            for (mcali.Point p : s.getPoints())
                points.add(representation.createPoint((int)p.x,(int)p.y));
        }

        TriangulationRepresentation triangularrep = (TriangulationRepresentation) representation;
        TriangulationRepresentation.CalcCutOff calccutoff = new TriangulationRepresentation.CalcCutOff() {
            public int calculateCutOff(TriangulationRepresentation rep) {
                if (type.equals("Normalised")) {
                    double percentage = (double)normalisedValue / 100.0;
                    double min = rep.getMinLength();
                    double max = rep.getMaxLength();
                    // Calculate normalised length based off percentage
                    return (int) (percentage * (max-min) + min);
                }
                if (type.equals("MST")) {
                    return rep.getMaxLengthOfMinimumSpanningTree();
                }
                else
                    return rep.getMaxLengthOfSmallestTriangleEdge();
            }
        };
        triangularrep.setCalcCutOff(calccutoff);
        representationwrapper.innerrepresentation = representation;
        VoronoiAlgorithm.generateVoronoi(representationwrapper, points);

        return triangularrep.getPointsFormingOutterBoundary();
    }

    public ArrayList<VPoint> getAlphaShapeTest(NewGesture g, final String type, final int normalisedValue) {
        ArrayList<VPoint> points = new ArrayList<VPoint>();
        AbstractRepresentation representation = RepresentationFactory.createTriangulationRepresentation();
        TestRepresentationWrapper representationwrapper = new TestRepresentationWrapper();

        for (Stroke s : createImages(g)) {
            for (mcali.Point p : s.getPoints()) {
                int px = (int) p.x;
                int py = (int) p.y;
//                for (int x = px-1; x < px+2; x++) {
//                    for (int y = py-1; y < py+2; y++) {
//                        points.add(representation.createPoint(px,py));
//                    }
//                }
                points.add(representation.createPoint(px-2,py));
                points.add(representation.createPoint(px,py));
                points.add(representation.createPoint(px+2,py));
                points.add(representation.createPoint(px-2,py+2));
                points.add(representation.createPoint(px,py+2));
                points.add(representation.createPoint(px+2,py+2));
                points.add(representation.createPoint(px-2,py-2));
                points.add(representation.createPoint(px+2,py-2));
                points.add(representation.createPoint(px,py-2));
            }
        }

        TriangulationRepresentation triangularrep = (TriangulationRepresentation) representation;
        TriangulationRepresentation.CalcCutOff calccutoff = new TriangulationRepresentation.CalcCutOff() {
            public int calculateCutOff(TriangulationRepresentation rep) {
                if (type.equals("Normalised")) {
                    double percentage = (double)normalisedValue / 100.0;
                    double min = rep.getMinLength();
                    double max = rep.getMaxLength();
                    // Calculate normalised length based off percentage
                    return (int) (percentage * (max-min) + min);
                }
                if (type.equals("MST")) {
                    return rep.getMaxLengthOfMinimumSpanningTree();
                }
                else
                    return rep.getMaxLengthOfSmallestTriangleEdge();
            }
        };
        triangularrep.setCalcCutOff(calccutoff);
        representationwrapper.innerrepresentation = representation;
        VoronoiAlgorithm.generateVoronoi(representationwrapper, points);

        return triangularrep.getPointsFormingOutterBoundary();
    }

    public List<Stroke> createImages(NewGesture g) {
//        BufferedImage orig = new BufferedImage(800,800,BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2d = orig.createGraphics();
//        g2d.setStroke(new BasicStroke(1));
//        g2d.setColor(Color.WHITE);
//        for (Stroke s : g.getStrokes()) {
//            List<Point> pts = s.getPoints();
//            for (int i = 1; i < pts.size(); i++) {
//                Point p0 = pts.get(i-1);
//                Point p1 = pts.get(i);
//                g2d.drawLine((int)p0.x,(int)p0.y,(int)p1.x,(int)p1.y);
//            }
//        }
//        for (Stroke s : g.getStrokes()) {
//            for (Point p : s.getPoints())
//                orig.setRGB((int)p.x,(int)p.y,Color.RED.getRGB());
//        }

        List<Stroke> resampledStrokes = new ArrayList<Stroke>();
        Function.resampleDistPts(5, g.getStrokes(), resampledStrokes);

//        BufferedImage resampled = new BufferedImage(800,800,BufferedImage.TYPE_INT_RGB);
//        g2d = resampled.createGraphics();
//        g2d.setStroke(new BasicStroke(1));
//        g2d.setColor(Color.WHITE);
//        for (Stroke s : resampledStrokes) {
//            List<Point> pts = s.getPoints();
//            for (int i = 1; i < pts.size(); i++) {
//                Point p0 = pts.get(i-1);
//                Point p1 = pts.get(i);
//                g2d.drawLine((int)p0.x,(int)p0.y,(int)p1.x,(int)p1.y);
//            }
//        }
//        for (Stroke s : resampledStrokes) {
//            for (Point p : s.getPoints()) {
//                int px = (int) p.x;
//                int py = (int) p.y;
////                for (int x = px-1; x < px+2; x++) {
////                    for (int y = py-1; y < py+2; y++) {
////                        resampled.setRGB(x,y,Color.RED.getRGB());
////                    }
////                }
//                resampled.setRGB(px-2,py,Color.RED.getRGB());
//                resampled.setRGB(px,py,Color.RED.getRGB());
//                resampled.setRGB(px+2,py,Color.RED.getRGB());
//                resampled.setRGB(px-2,py-2,Color.RED.getRGB());
//                resampled.setRGB(px,py-2,Color.RED.getRGB());
//                resampled.setRGB(px+2,py-2,Color.RED.getRGB());
//                resampled.setRGB(px-2,py+2,Color.RED.getRGB());
//                resampled.setRGB(px,py+2,Color.RED.getRGB());
//                resampled.setRGB(px+2,py+2,Color.RED.getRGB());
//            }
//        }
//
//        try {
//            ImageIO.write(orig, "png", new File("orig.png"));
//            ImageIO.write(resampled, "png", new File("resampled.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return resampledStrokes;
    }

    public class TestRepresentationWrapper implements RepresentationInterface {

        /* ***************************************************** */
        // Variables

        private final ArrayList<VPoint> circleevents = new ArrayList<VPoint>();

        private RepresentationInterface innerrepresentation = null;

        /* ***************************************************** */
        // Data/Representation Interface Method

        // Executed before the algorithm begins to process (can be used to
        //   initialise any data structures required)
        public void beginAlgorithm(Collection<VPoint> points) {
            // Reset the triangle array list
            circleevents.clear();

            // Call the inner representation
            if ( innerrepresentation!=null ) {
                innerrepresentation.beginAlgorithm(points);
            }
        }

        // Called to record that a vertex has been found
        public void siteEvent( VLinkedNode n1 , VLinkedNode n2 , VLinkedNode n3 ) {
            // Call the inner representation
            if ( innerrepresentation!=null ) {
                innerrepresentation.siteEvent(n1, n2, n3);
            }
        }
        public void circleEvent( VLinkedNode n1 , VLinkedNode n2 , VLinkedNode n3 , int circle_x , int circle_y ) {
            // Add the circle event
            circleevents.add( new VPoint(circle_x, circle_y) );

            // Call the inner representation
            if ( innerrepresentation!=null ) {
                innerrepresentation.circleEvent(n1, n2, n3, circle_x, circle_y);
            }
        }

        // Called when the algorithm has finished processing
        public void endAlgorithm(Collection<VPoint> points, int lastsweeplineposition, VLinkedNode headnode) {
            // Call the inner representation
            if ( innerrepresentation!=null ) {
                innerrepresentation.endAlgorithm(points, lastsweeplineposition, headnode);
            }
        }

        /* ***************************************************** */
    }

    public static void main(String[] args) {
        new NewAlphaShape().nonconvex();
    }
}
