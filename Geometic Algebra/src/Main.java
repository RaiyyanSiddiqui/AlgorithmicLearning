import java.util.TreeSet;


public class Main {
	public static void inversionTest() {
		var a = new GeometricVector(3,new double[]{0,1,1,1} );
		var b = new GeometricVector(a);
		b.reverse();
		
		System.out.println("A::" + a + "\nB::" + b);
		System.out.println("PRODUCT: " + GeometricVector.product(a, b));
		System.out.println("A-PRODUCT" + GeometricVector.product(a, a) + "\n");
		
		GeometricVector.debug = true;
		var mag = a.getMag();
		System.out.println(mag + " MAG");
		mag = mag * mag;
		
		GeometricVector.debug = false;
	}
	
	public static void main(String[] args) {
		
		//inversionTest();
		//GeometricVector.debug = false;
		var a = new GeometricVector(3, new double[]{0,0,1,0} );
		var b = new GeometricVector(3, new double[]{0,0,0,1} );
		//GeometricVector.debug = true;
		GeometricVector.concisePrint = false;
		System.out.println(GeometricVector.crossProduct(a,b));
	}

}
