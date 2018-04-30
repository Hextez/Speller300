package classificador;

public class NewFeatures {

		//MCALI
		private double circleR, rectR1, rectR2, rectR3, rectR4, triR3, aspectR;
		// Extreme Quad
		private double eqR1;
		// Movements
		private double movementY;
		// Intersections
		private double chR2, chR3;
		// Bounding box
		private double bbchR;
		
		private double astchAR, astchPR;

		//Nova feature RMS 
		private double rms;
		
		public NewFeatures() {
			circleR = rectR1 = rectR2 = rectR3 = rectR4 = triR3 = aspectR = 0;
			eqR1 = 0;
			movementY = 0;
			chR2 = 0;
			bbchR = 0;
			astchAR = astchPR = 0;
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
		
		public double getRectR4() {
			return rectR4;
		}

		public void setRectR4(double rectR4) {
			this.rectR4 = rectR4;
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
		
		public double getChR3() {
			return chR3;
		}

		public void setChR3(double chR3) {
			this.chR3 = chR3;
		}
		
		public double getBbchR() {
			return bbchR;
		}

		public void setBbchR(double bbchR) {
			this.bbchR = bbchR;
		}
		
		public double getAstchAR(){
			return astchAR;
		}
		
		public void setAstchAR(double astchAR){
			this.astchAR = astchAR;
		}
		
		public double getAstchPR(){
			return astchPR;
		}
		
		public void setAstchPR(double astchPR){
			this.astchPR = astchPR;
		}
		
		//Nova feature adicionada
		public double getRMS(){
			return rms;
		}
		
		public void setRMS(double rms){
			this.rms = rms;
		}
}
