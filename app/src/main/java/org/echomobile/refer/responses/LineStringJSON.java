package org.echomobile.refer.responses;



public class LineStringJSON {
	public String type;
	public Point[] coordinates;
	
	LineStringJSON() {
	}
	
	public class Point {
		public String type;
		public String[] coordinates;
	}
	
	
}
