package classificador;

public class NewFeatures {

		//MCALI
		private double circleR, rectR1, rectR2, rectR3, triR3, aspectR, fillingR;
		// Extreme Quad
		private double eqR1;
		// Movements
		private double movementY;
		// Intersections
		private double chR2;
		// Bounding box
		private double bbchR;
		// Quadrant
		private double quad1FillR, quad2FillR, quad3FillR, quad4FillR;

		//Nova feature RMS 
		private double rms;
		
		public NewFeatures() {
			circleR = rectR1 = rectR2 = rectR3 = triR3 = aspectR = fillingR = 0;
			eqR1 = 0;
			movementY = 0;
			chR2 = 0;
			bbchR = 0; 
			quad1FillR = quad2FillR = quad3FillR = quad4FillR = 0;
			rms = 0;
		}
		
		public double getCircleR() {
			return circleR;
		}

		public void setCircleR(double circleR) {
			this.circleR = circleR;
		}

		public double getRectR1() {
			return rectR1;
		}

		public void setRectR1(double rectR1) {
			this.rectR1 = rectR1;
		}

		public double getRectR2() {
			return rectR2;
		}

		public void setRectR2(double rectR2) {
			this.rectR2 = rectR2;
		}

		public double getRectR3() {
			return rectR3;
		}

		public void setRectR3(double rectR3) {
			this.rectR3 = rectR3;
		}

		public double getTriR3() {
			return triR3;
		}

		public void setTriR3(double triR3) {
			this.triR3 = triR3;
		}

		public double getAspectR() {
			return aspectR;
		}

		public void setAspectR(double aspectR) {
			this.aspectR = aspectR;
		}

		public double getFillingR() {
			return fillingR;
		}

		public void setFillingR(double fillingR) {
			this.fillingR = fillingR;
		}

		public double getEqR1() {
			return eqR1;
		}

		public void setEqR1(double eqR1) {
			this.eqR1 = eqR1;
		}

		public double getMovementY() {
			return movementY;
		}

		public void setMovementY(double movementY) {
			this.movementY = movementY;
		}
		
		public double getChR2() {
			return chR2;
		}

		public void setChR2(double chR2) {
			this.chR2 = chR2;
		}
		
		public double getBbchR() {
			return bbchR;
		}

		public void setBbchR(double bbchR) {
			this.bbchR = bbchR;
		}
		
		public double getQuad1FillR() {
			return quad1FillR;
		}

		public void setQuad1FillR(double quad1FillR) {
			this.quad1FillR = quad1FillR;
		}

		public double getQuad2FillR() {
			return quad2FillR;
		}

		public void setQuad2FillR(double quad2FillR) {
			this.quad2FillR = quad2FillR;
		}

		public double getQuad3FillR() {
			return quad3FillR;
		}

		public void setQuad3FillR(double quad3FillR) {
			this.quad3FillR = quad3FillR;
		}

		public double getQuad4FillR() {
			return quad4FillR;
		}

		public void setQuad4FillR(double quad4FillR) {
			this.quad4FillR = quad4FillR;
		}
		
		//Nova feature adicionada
		public double getRMS(){
			return rms;
		}
		
		public void setRMS(double rms){
			this.rms = rms;
		}
}
