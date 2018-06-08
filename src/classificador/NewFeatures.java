package classificador;

public class NewFeatures {

		//MCALI
		private double rectR1, rectR2, rectR3;
		// Extreme Quad
		private double eqR1;
		// Movements
		private double movementY;
		// Intersections
		private double chR2, chR3;
		// Bounding box
		private double bbchR;
		//Quads
		private double quad2FillR, quad3FillR, quad4FillR;
		//Alpha shapes
		private double astchAR, astchPR, astchAB;
		//FillingR
		private double fillingR;
		// Absolute angle histogram
		private double aa1, aa2, aa3, aa4;
		// Relative angle histogram
		private double ra1, ra2, ra3, ra4;
		
		//Nova feature RMS 
		private double rms;

		public NewFeatures() {
			rectR1 = rectR2 = rectR3 = 0;
			eqR1 = 0;
			movementY = 0;
			chR2 = 0;
			bbchR = fillingR = 0;
			astchAR = astchPR = astchAB = 0;
			rms = 0;
			quad2FillR = quad3FillR = quad4FillR = 0;
			aa1 = aa2 = aa3 = aa4 = ra1 = ra2 = ra3 = ra4 = 0;

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
		
		public double getFillingR() {
			return fillingR;
		}

		public void setFillingR(double fillingR) {
			this.fillingR = fillingR;
		}
		
		public void setBbchR(double bbchR) {
			this.bbchR = bbchR;
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
		
		public double getAa1() {
			return aa1;
		}

		public void setAa1(double aa1) {
			this.aa1 = aa1;
		}

		public double getAa2() {
			return aa2;
		}

		public void setAa2(double aa2) {
			this.aa2 = aa2;
		}

		public double getAa3() {
			return aa3;
		}

		public void setAa3(double aa3) {
			this.aa3 = aa3;
		}

		public double getAa4() {
			return aa4;
		}

		public void setAa4(double aa4) {
			this.aa4 = aa4;
		}

		public double getRa1() {
			return ra1;
		}

		public void setRa1(double ra1) {
			this.ra1 = ra1;
		}

		public double getRa2() {
			return ra2;
		}

		public void setRa2(double ra2) {
			this.ra2 = ra2;
		}

		public double getRa3() {
			return ra3;
		}

		public void setRa3(double ra3) {
			this.ra3 = ra3;
		}

		public double getRa4() {
			return ra4;
		}

		public void setRa4(double ra4) {
			this.ra4 = ra4;
		}
		
		//Nova feature adicionada
		public double getRMS(){
			return rms;
		}
		
		public void setRMS(double rms){
			this.rms = rms;
		}
		
		public double getAstchAB(){
			return astchAB;
		}

		public void setAstchAB(double astchAB){
			this.astchAB = astchAB;
		}
		
		
}
