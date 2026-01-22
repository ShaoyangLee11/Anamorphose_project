package pluginUtils;

public class Transformation {
	
	private Quadrilatere depart,arrivee; 
	private double denom1,denom2; 
	private double denomInv1,denomInv2;
	private int transfoNumber; 
	private static int number=0; 
	private double affineMatrix[]={0,0,0,0,0,0,0,0,1}; 
	private double den1,den2; 
	private double dab,dac,dbd,dcd; 
	/**
	 * @param depart
	 * @param arrivee
	 */
	public Transformation(Quadrilatere depart, Quadrilatere arrivee) {
		this.transfoNumber=number++;
		this.depart = depart;
		this.arrivee = arrivee;
		double yc=depart.getLl().getZ(); 
		double ya=depart.getUl().getZ(); 
		this.denom1=yc-ya; 
		double xb=depart.getUr().getY(); 
		double xa=depart.getUl().getY();
		this.denom2=xb-xa;
		
		double yAd=arrivee.getLl().getZ(); 
		double yAa=arrivee.getUl().getZ(); 
		this.denomInv1=yAd-yAa; 
		double xAb=arrivee.getUr().getY(); 
		double xAa=arrivee.getUl().getY();
		this.denomInv2=xAb-xAa;
		// On prend A,B,C et on calcule la transformation 
		// A
		double x11=depart.getLl().getY();
		double x12=depart.getLl().getZ(); 
		//B
		double x21=depart.getUr().getY();
		double x22=depart.getUr().getZ();
		// C
		double x31=depart.getLr().getY();
		double x32=depart.getLr().getZ();
		// Image de A
		double y11=arrivee.getLl().getY();
		double y12=arrivee.getLl().getZ(); 
		//Image de B
		double y21=arrivee.getUr().getY();
		double y22=arrivee.getUr().getZ();
		// Umage de C
		double y31=arrivee.getLr().getY();
		double y32=arrivee.getLr().getZ();
		
		this.den1=(x11-x21)*(x12-x32)-(x11-x31)*(x12-x22);
		this.den2=(x12-x22)*(x11-x31)-(x12-x32)*(x11-x21); 
		affineMatrix[0]=((y11-y21)*(x12-x32)-(y11-y31)*(x12-x22))/den1; 
		affineMatrix[1]=((y11-y21)*(x11-x31)-(y11-y31)*(x11-x21))/den2;
		affineMatrix[2]=y11-affineMatrix[0]*x11-affineMatrix[1]*x12; 
		affineMatrix[3]=((y12-y22)*(x12-x32)-(y12-y32)*(x12-x22))/den1;
		affineMatrix[4]=((y12-y22)*(x11-x31)-(y12-y32)*(x11-x21))/den2; 
		affineMatrix[5]=y12-affineMatrix[3]*x11-affineMatrix[4]*x12; 
			
		// longueur des cotes
		dab=Punkt.distance(arrivee.getUl(),arrivee.getUr()); 
		dac=Punkt.distance(arrivee.getUl(),arrivee.getLl()); 
		dbd=Punkt.distance(arrivee.getUr(),arrivee.getLr());
		dcd=Punkt.distance(arrivee.getLl(),arrivee.getLr()); 
	}

	public Punkt transformeR2(Punkt d){
		double Nx=d.getY()*affineMatrix[0]+affineMatrix[1]*d.getZ()+affineMatrix[2]; 
		double Ny=d.getY()*affineMatrix[3]+affineMatrix[4]*d.getZ()+affineMatrix[5]; 
		return new Punkt(0,0,Nx,Ny); 
	}
	
	// TODO : probleme : l'image des deux points du milieu sont interverties
	/*
	public Punkt transformeReserve(Punkt d){
		double alpha=(d.getZ()-depart.getUl().getZ())/this.denom1;
		double beta=(d.getY()-depart.getUl().getY())/this.denom2;
	
		
		//double Apx=(1-beta)*arrivee.getUl().getZ()+beta*arrivee.getLl().getZ();
		//double Apy=(1-beta)*arrivee.getUl().getY()+beta*arrivee.getLl().getY();
		//double Bpx=(1-beta)*arrivee.getUr().getZ()+beta*arrivee.getLr().getZ();
		//double Bpy=(1-beta)*arrivee.getUr().getY()+beta*arrivee.getLr().getY();
		
		double Apx=(1-beta)*arrivee.getUl().getZ()+beta*arrivee.getUr().getZ();
		double Apy=(1-beta)*arrivee.getUl().getY()+beta*arrivee.getUr().getY();
		double Bpx=(1-beta)*arrivee.getLl().getZ()+beta*arrivee.getLr().getZ();
		double Bpy=(1-beta)*arrivee.getLl().getY()+beta*arrivee.getLr().getY();
		
		double Nx=alpha*Bpx+(1-alpha)*Apx; 
		double Ny=alpha*Bpy+(1-alpha)*Apy;
		return new Punkt(0,0,Ny,Nx);
		
	}
*/
public Punkt transforme(Punkt d){
	double alpha=(d.getZ()-depart.getUl().getZ())/this.denom1;
	double beta=(d.getY()-depart.getUl().getY())/this.denom2;
	
	

	double Apx=(1-beta)*arrivee.getUl().getZ()+beta*arrivee.getLl().getZ();
	double Apy=(1-beta)*arrivee.getUl().getY()+beta*arrivee.getLl().getY();
	double Bpx=(1-beta)*arrivee.getUr().getZ()+beta*arrivee.getLr().getZ();
	double Bpy=(1-beta)*arrivee.getUr().getY()+beta*arrivee.getLr().getY();
	
	double Nz=alpha*Bpx+(1-alpha)*Apx; 
	double Ny=alpha*Bpy+(1-alpha)*Apy;
	Punkt image=new Punkt(0,0,Ny,Nz); 
	
	return new Punkt(0,0,Ny,Nz);
}



public Punkt transformeInverse(Punkt d){
	double alpha=(d.getZ()-arrivee.getUl().getZ())/this.denomInv1;
	double beta=(d.getY()-arrivee.getUl().getY())/this.denomInv2; 
	double Apx=(1-beta)*depart.getUl().getZ()+beta*depart.getLl().getZ();
	double Apy=(1-beta)*depart.getUl().getY()+beta*depart.getLl().getY();
	double Bpx=(1-beta)*depart.getUr().getZ()+beta*depart.getLr().getZ();
	double Bpy=(1-beta)*depart.getUr().getY()+beta*depart.getLr().getY();
	double Nx=alpha*Bpx+(1-alpha)*Apx; 
	double Ny=alpha*Bpy+(1-alpha)*Apy; 
	return new Punkt(0,0,Ny,Nx);
}
/**
 * @return the transfoNumber
 */
public int getTransfoNumber() {
	return transfoNumber;
}

public double getcoefInv1(){return this.denomInv1; }
public double getcoefInv2(){return this.denomInv2;}



public String toString(){
	String s="Transformation "+getTransfoNumber()+"\n"+"Coins du carre de depart : \n";
	s+=depart+"\n"; 
	s+="Coins du carre d'arrivee\n"+arrivee+"\n"; 
	s+="Coefficients "+den1+" "+den2+"\n";
	for(int i=0;i<6;i++)
		s+="matrix["+i+"]:"+affineMatrix[i]+"\n"; 
	return s; 
	
}
/**
 * verifier que les coins ont bien leur bonne image
 */
public String verif(){
	Punkt midBC=Punkt.mid(depart.getUr(),depart.getLl());
	Punkt midBCprim=Punkt.mid(arrivee.getUr(),arrivee.getLl());
	Punkt entreBC=Punkt.entre(depart.getUr(),depart.getLl(),0.13); 
	Punkt entreBCprim=Punkt.entre(arrivee.getUr(),arrivee.getLl(),0.13); 
	String s=this.toString()+"\n";
	s+=this.transforme(depart.getUl())+" * "+arrivee.getUl()+"\n"; 
	s+=this.transforme(depart.getUr())+" * "+arrivee.getUr()+"\n"; 
	s+=this.transforme(depart.getLr())+" * "+arrivee.getLr()+"\n"; 
	s+=this.transforme(depart.getLl())+" * "+arrivee.getLl()+"\n";
	s+="Milieu de B,C\n";
	s+=this.transforme(midBC)+" * "+midBCprim+"\n";
	s+=this.transforme(entreBC)+" * "+entreBCprim; 
	return s; 
}

}



