package pluginUtils;

public class Quadrilatere {
	// les coordonnees dans le quadrillage du coin superieur gauche
	private double a; 
	private double b; 
	private double minx,maxx,miny,maxy;  

	
	// upper-left, upper-right,lower-left, lower-right
	private Punkt ul,ur,ll,lr; 
	
	/*
	 * 
	 * 
	 *        A--------B
	 *        |        |
	 *        |        |
	 *        C--------D
	 * 
	 * 
	 */
	
	public Quadrilatere(Punkt A,Punkt B,Punkt C,Punkt D){
		ul=A; 
		ur=B; 
		ll=C; 
		lr=D;
		a=A.getY();
		b=A.getZ();
		this.fixBounds(); 
	}
	
	private void fixBounds(){
		minx=ul.getY(); 
		maxx=ul.getY(); 
		miny=ul.getZ(); 
		maxy=ul.getZ();
		if(ur.getY()<minx) minx=ur.getY();
		if(ur.getY()>maxx) maxx=ur.getY();
		if(ll.getY()<minx) minx=ll.getY();
		if(ll.getY()>maxx) maxx=ll.getY();
		if(lr.getY()<minx) minx=lr.getY();
		if(lr.getY()>maxx) maxx=lr.getY();
		if(ur.getZ()<miny) miny=ur.getZ();
		if(ur.getZ()>maxy) maxy=ur.getZ();
		if(ll.getZ()<miny) miny=ll.getZ();
		if(ll.getZ()>maxy) maxy=ll.getZ();
		if(lr.getZ()<miny) miny=lr.getZ();
		if(lr.getZ()>maxy) maxy=lr.getZ();	
	}

	public double getMinx() {
		return minx;
	}

	public double getMaxx() {
		return maxx;
	}

	public double getMiny() {
		return miny;
	}

	public double getMaxy() {
		return maxy;
	}

	/**
	 * @return the ul
	 */
	public Punkt getUl() {
		return ul;
	}

	/**
	 * @return the ur
	 */
	public Punkt getUr() {
		return ur;
	}

	/**
	 * @return the ll
	 */
	public Punkt getLl() {
		return ll;
	}

	/**
	 * @return the lr
	 */
	public Punkt getLr() {
		return lr;
	}
	public String toString(){
		String s="UL : "+ul+"\n"; 
		s+="UR : "+ur+"\n";
		s+="LR : "+lr+"\n";
		s+="LL : "+ll; 
		return s; 
	}
	
}
