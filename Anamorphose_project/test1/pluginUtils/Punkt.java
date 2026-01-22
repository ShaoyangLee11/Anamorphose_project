package pluginUtils;





//Punkt contient les coordonnees d'un point 
public class Punkt{
	private int y,z; 
	private double cx,cy; 
	public Punkt(int a,int b,double c,double d){
		y=a;
		z=b; 
		cx=c;
		cy=d;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Punkt other = (Punkt) obj;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	/**
	 * @return the y
	 */
	public double getY() {
		return cx;
	}
	/**
	 * @return the z
	 */
	public double getZ() {
		return cy;
	}
	
	public String toString(){
		return "("+this.cx+" "+this.cy+")"; 
	}
	
	public static Punkt mid(Punkt a, Punkt b){
		return new Punkt(0,0,(a.getY()+b.getY())/2,(a.getZ()+b.getZ())/2); 
	}
	public static Punkt entre(Punkt a, Punkt b,double alpha){
		return new Punkt(0,0,alpha*a.getY()+(1-alpha)*b.getY(),alpha*a.getZ()+(1-alpha)*b.getZ()); 
	}
	
	public static double distance(Punkt p1,Punkt p2){
		double s=(p1.cx-p2.cx)*(p1.cx-p2.cx); 
		s+=(p1.cy-p2.cy)*(p1.cy-p2.cy);
		return Math.sqrt(s);
		
	}
	/**
	 * @param y the cy to set
	 */
	public void setY(double y) {
		this.cx = y;
	}
	/**
	 * @param z the cz to set
	 */
	public void setZ(double z) {
		this.cy = z;
	}
	
}


