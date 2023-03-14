import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
interface TwoArgD{
	double apply(double a, double b);
};

class Util{ // basis, sorting, and external conversions
	public static TwoArgD add = (one,two) -> one + two;
	public static TwoArgD sub = (one,two) -> one - two;
	public static TwoArgD div = (one,two) -> one / two;
	public static TwoArgD mul = (one,two) -> one * two;
	public static TwoArgD pow = (one,two) -> Math.pow(one, two);
	public static TwoArgD log = (one,two) -> Math.log10(one) / Math.log10(two);
	
	public static String intToCharStr(int total, int n) {
		int m = n / 26;
		int d = n % 26;
		if(total < 26) {
			return "" +  (char)('a' + d);
		} else {
			return (char)('a' + d) + "" + m + "_";
		}
	}
	/**
	 * InsertionSort with output reporting number of swaps occurred.
	 * @param  a
	 * @return swaps
	 */
	public static int sortArrayList(ArrayList<Integer> a) {
		int swaps = 0;
		for(int i=0; i < a.size(); i++) {
			for(int j = i; j >= 0; j--) {
				if(j == 0) continue;
				//System.out.println("SORT: " + a + " j " + j + " i " + i);
				if(a.get(j - 1) > a.get(j)) {//swap
					var temp = (int)a.get(j - 1);
					a.set(j - 1, a.get(j));
					a.set(j, temp);
					swaps ++;
				} else break;
			}
		}
		return swaps;
	}
	
	/**
	 * Helper function for basis sorting function below. Compares one basis entry (a set) to another.
	 * @param set1
	 * @param set2
	 * @return boolean (set1 is larger than set2)
	 */
	private static boolean basisComp(HashSet<Integer> set1, HashSet<Integer> set2) { 
		// true = set1 > set2. false is set1 < set2, true swaps      
		if(set1.size() != set2.size()) return set1.size() > set2.size();
		if(set1.equals(set2)) return false; // to prevent unnecessary swaps
		//check which first doesn't contain numbers
		for(int i=0; ; i++) {
			boolean s1 = set1.contains(i);
			boolean s2 = set2.contains(i);
			if(s1 == s2) continue; //if both contain or both dont contain
			return s2; // s2 is the lower one (s1 is larger)? 
		}
	}
	private static void sortBasis(ArrayList<HashSet<Integer>>  sets) {
		// assuming at least one element
		if(sets.size() == 0)
			return;
		for(int i = 0; i < sets.size(); i++ ) {
			for(int j = i; j > 0; j--) {
				if( basisComp(sets.get(j - 1), sets.get(j) )) { // swap, shallow copy O.K.
					var temp = sets.get(j);
					sets.set(j, sets.get(j - 1) );
					sets.set(j - 1, temp );
				}
			}
		}
	}
	private static HashSet<HashSet<Integer>> powerSet(Set<Integer> originalSet) {
		HashSet<HashSet<Integer>> sets = new HashSet<HashSet<Integer>>();
	    if (originalSet.isEmpty()) {
	        sets.add(new HashSet<Integer>());
	        return sets;
	    }
	    var list = new ArrayList<Integer>(originalSet);
	    Integer head = list.get(0);
	    HashSet<Integer> rest = new HashSet<Integer>(list.subList(1, list.size())); 
	    for (HashSet<Integer> set : powerSet(rest)) {
	    	HashSet<Integer> newSet = new HashSet<Integer>();
	        newSet.add(head);
	        newSet.addAll(set);
	        sets.add(newSet);
	        sets.add(set);
	    }
	    //sortDSet(sets);
	    return sets;
	}  
	
	public static ArrayList<HashSet<Integer>> generateBasis(int numBasisVectors){
		var out = new ArrayList<HashSet<Integer>>();
		
		var basisVecs = new HashSet<Integer>(); 
		for(int i=1; i <= numBasisVectors; i++) basisVecs.add(i);
		//System.out.println(basisVecs);
		var powerset = Util.powerSet(basisVecs);
		
		for(var e : powerset) out.add(new HashSet<Integer>(e));
		sortBasis(out);
		
		return out;
	}
}



public class GeometricVector {
	/**Prints basis calculation and product table every time product is called. Default false.*/
	public static boolean debug = false; 
	/**Include all elements (even empty) in toString. Default true.*/
	public static boolean concisePrint = true; 
	
	public int dimension;
	public ArrayList<HashSet<Integer>> basis;
	public double elements[];
	
	public GeometricVector(int dim) {
		basis = Util.generateBasis(dim);
		dimension = dim; // basis.size();
		elements = new double[ basis.size() ];
	}
	public GeometricVector(int dim, double arr[]) { // length != dim
		basis = Util.generateBasis(dim);
		dimension = dim;//(int) Math.round(Math.log(arr.length) / Math.log(2)); //should work when rounding
		elements = new double[ basis.size() ];
		for(int i=0; i < elements.length && i < arr.length; i++) elements[i] = arr[i];
	}
	public GeometricVector(GeometricVector a) {
		basis = a.basis; // might not need any fancy cloning
		dimension = a.dimension;
		
		elements = new double[a.elements.length];
		for(int i=0; i < a.elements.length; i++) elements[i] = a.elements[i];
	}
	public GeometricVector(int newDim, GeometricVector a) {
		this(a);
		if(newDim == a.dimension) return;
		
		basis = Util.generateBasis(newDim);
		elements = new double[basis.size()];
		dimension = newDim;
		for(int i=0; i < basis.size(); i++) { // based on output
			int index = a.basis.indexOf(basis.get(i)); // find index of current basis on old
			if(index != -1) {
				elements[i] = a.elements[index];
			}
		}
	}
	
// ----------------------------- CORE FUNCTIONALITY -------------------	
	
	public static GeometricVector product(GeometricVector a, GeometricVector b) {
		ArrayList<String> debugGeneric = new ArrayList<String>();
		if(debug) {
			for (int j = 0; j < Math.max(a.elements.length, b.elements.length); j++) debugGeneric.add("");
			for(int j = 1; j <= 2; j++) {
				int limit = (j == 1)? a.elements.length : b.elements.length;
				var basisAB = (j == 1)? a.basis : b.basis;
				for(int i=0; i < limit; i++) {
					if(i!= 0) {
						System.out.print(" +");
					}
					
					System.out.print(Util.intToCharStr(limit, i) + "" + j);
					if(i != 0) System.out.print("*e");
					for(var e : basisAB.get(i))
						System.out.print(e);
				}
				System.out.println();
			}
		}
		
		int maxDeg = Math.max(a.dimension, b.dimension);
		GeometricVector o = new GeometricVector(maxDeg);
		for (int ai = 0; ai < a.basis.size(); ai++) {
			if(a.elements[ai] == 0 && !debug) continue;
			
			var refabasis = new ArrayList<Integer>(a.basis.get(ai));
			Util.sortArrayList(refabasis);
			for (int bi = 0; bi < b.basis.size(); bi++) {
				if(b.elements[bi] == 0 && !debug) continue;
				
				var negative = false;
				var newBasis = new ArrayList<Integer>();
				var abasis = (ArrayList<Integer>)refabasis.clone();
				var bbasis = new ArrayList<Integer>(b.basis.get(bi));
				Util.sortArrayList(bbasis); // order not guaranteed in hashset				
				//System.out.println("\n" + a.elements[ai] + " :ab: " + b.elements[bi]);
				
				newBasis.addAll(abasis);
				newBasis.addAll(bbasis);
				int swaps = Util.sortArrayList(newBasis);
				negative = swaps % 2 == 1;

				//System.out.println(newBasis + " " + negative);
				//removedupes
				for(int i=0; i < newBasis.size(); ) {
					if(i != 0 && newBasis.get(i - 1) == newBasis.get(i)) {
						newBasis.remove(i - 1);
						newBasis.remove(i - 1); // everything got shifted because of the last one
					} else i++;
				}
				//System.out.println(newBasis);
				//once done
				var hs = new HashSet<Integer>(newBasis);
				int ni = o.basis.indexOf(hs);
				//System.out.println("HASHSET" + hs + " i:" + ni + " a:" + a.elements[ai] + " B:" + b.elements[bi]);
				o.elements[ni] += a.elements[ai] * b.elements[bi] * ((negative)? -1:1 );
				if(debug) {
					String sign = (ai == 0)? " " : ((negative)? " -":" +" );
					debugGeneric.set(ni, debugGeneric.get(ni) + sign + Util.intToCharStr(a.elements.length,ai) + "1*" + Util.intToCharStr(b.elements.length, bi) + "2");
				}
			}
		}
		//might refactor this later
		if(debug) {
			for(int i=0; i < debugGeneric.size(); i++) {
				if(a.basis.get(i).size() != 0) System.out.print('e');
				else System.out.print("e0");
				for(var e : a.basis.get(i))System.out.print(e);
				System.out.println();
				System.out.println(debugGeneric.get(i));
			}
		}
		return o;
	}

	//geometric product
	public GeometricVector product(GeometricVector v) {
		var o = GeometricVector.product(this, v);
		
		this.basis = o.basis;
		this.dimension = o.dimension;
		this.elements = o.elements;
		return this;
	}
	public static GeometricVector crossProduct(GeometricVector a, GeometricVector b) { // Casts a,b as G3. Returns G3
		var a3 = new GeometricVector(3, a);
		var b3 = new GeometricVector(3, b);
		a3.product(b3);
		a3.product(new GeometricVector(3, new double[] {0, 0,0,0, 0,0,0, -1}) ); // could be statically cached 
		return a3;
	}
	public static GeometricVector elementWise(GeometricVector a, GeometricVector b, TwoArgD fun) { // func will be lambda
		GeometricVector out = new GeometricVector(Math.max(a.dimension, b.dimension));
		GeometricVector newA = new GeometricVector(out.dimension, a);
		GeometricVector newB = new GeometricVector(out.dimension, b);
		
		for(int i = 0; i < newA.basis.size(); i++) {
			out.elements[i] = fun.apply(newA.elements[i], newB.elements[i]); // pass into func
		}
		return out;
	}
	public static GeometricVector broadcast(GeometricVector a, double scalar, TwoArgD fun) {
		GeometricVector out = new GeometricVector(a);
		
		for(int i = 0; i < out.basis.size(); i++) {
			out.elements[i] = fun.apply(a.elements[i], scalar); // pass into func
		}
		return out;
	}
// ----------------------------------- HELPER FUNCS ------------------	
	public static int pseudoscalarSign(int n) { // might move to util
		int a = (n / 2) % 2; // generates 0,0,1,1,0,0,...
		return (-2 * a) + 1; // generates 1,1,-1,-1,1,1...
	}
	public boolean isZero() {
		for(var e : elements)
			if(e != 0) return false;
		return true;
	}
	public void reverse() {
		for(int i=0; i < elements.length; i++) {
			elements[i] = elements[i] * pseudoscalarSign(basis.get(i).size() + 1);
		}
	}
	public double getMag() { // depends on context, might disambiguate
		var a = new GeometricVector(this);
		a.product(this);
		return a.elements[0];
	}
	
	public String debugString() { // testing purposes
		String s  = "BASIS: ";
		s += basis.toString() + "\n";
		s += "DIM: " + this.dimension + " ELEMENTS: {";
		for(int i=0; i < elements.length; i++) s += elements[i] + " ";
		s += "}";
		return s;
	}
	public String prettyString(boolean skipEmpty) { // keeps it neat
		String o = "G";
		o += dimension + " ";
		for(int i=0; i < basis.size(); i++) {
			if(skipEmpty && elements[i] == 0) 
				continue;
			
			var be = new ArrayList<Integer>(basis.get(i));
			o += elements[i] + ((be.size() != 0)? "*e" : "");
			for(var e : be) {
				o += e;
			}
			int tab = dimension / 8 + 4;
			for(int s = 0; s < tab - be.size(); s++) {
				o += " ";
			}
		}
		return o;
	}
	public String toString() {
		return prettyString(concisePrint); // might make a separate toggleable boolean
	}
}
