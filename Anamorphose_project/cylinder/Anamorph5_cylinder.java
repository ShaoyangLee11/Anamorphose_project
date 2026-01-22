import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;


import java.awt.Color;


import java.awt.Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import pluginUtils.Doublet;
import pluginUtils.Punkt;
import pluginUtils.Quadrilatere;
import pluginUtils.Transformation;


// Read a datafile and a image file, compute the distorted image for anamorphosis


public class Anamorph5 extends ImagePlus implements PlugIn{
	{Locale.setDefault(Locale.US); }
	
	// Controlling the size of the distorted image


	private double scaleb=1.6; //0.0125; 


	// the steps for scanning each square
	private double mstepx=0.005,mstepy=0.005;
	
	private ImageProcessor primeIP; 
	private ImageProcessor inv_ip; 
	private int X=2800,Y=2100; // image size
	
	
	
	private void drawLine(Punkt A,Punkt B){
		drawLine(A.getY(),A.getZ(),B.getY(),B.getZ()); 
	}
	
	private void drawLine(double xd,double yd,double xf,double yf){
		int XD=(int)(xd*acoef+ccoef); 
		int YD=(int)(yd*vcoef+wcoef); 
		int XF=(int)(xf*acoef+ccoef); 
		int YF=(int)(yf*vcoef+wcoef);
		//System.out.println("---->"+XD+" "+YD+" "+XF+" "+YF); 
		primeIP.drawLine(XD,YD,XF,YF);
		
	}
	
	
	private void drawPoint(Punkt p){
		double xCenter=p.getY(); 
		double yCenter=p.getZ(); 
		int intX=(int)(xCenter*acoef+ccoef);
		int intY=(int)(yCenter*vcoef+wcoef);
		primeIP.fillOval(intX-10,intY-10,20,20);
		
	}
	
	// For the scaling of the image
	private double x0=0,x1=1,y0=0,y1=1;
	private double acoef,ccoef,vcoef,wcoef; 
	
	


	public void setCoefs(){
		acoef=X/(x1-x0); 
		vcoef=Y/(y1-y0); 
		ccoef=-acoef*x0; 
		wcoef=-vcoef*y0; 
	}
	 
	
	
	public void setBounds(double xl,double xu,double yl,double yu){
		x0=xl; 
		x1=xu;
		y0=yl;
		y1=yu;
		 
	}
	
	
	
	public void run(String arg) {
		System.out.println("go");

		//ImagePlus imp = IJ.openImage("/tmp/Mona_Lisa.png");
		ImagePlus imp=IJ.openImage("D:/cylinder/cs2.png"); 

		//ImagePlus imp = IJ.openImage("/tmp/monamod.png");
		//ImagePlus imp=IJ.openImage("F:/Povray/anamorphoses/Mona_Lisa.png"); 

		imp.setTitle("PrimeIP");
		
	
		System.out.println(imp); 
		primeIP=imp.getProcessor();
		X=primeIP.getWidth(); 
		Y=primeIP.getHeight(); 
		
        setStack(null, imp.getStack());
        setCalibration(imp.getCalibration()); 
		
        	show();
		
		
		

		// create a new image with the same size and copy the pixels of the original image
		ImagePlus inverted = NewImage.createRGBImage ("Inverted image", primeIP.getWidth(), primeIP.getHeight(), 1, NewImage.FILL_BLACK);
		inv_ip = inverted.getProcessor();
		
		
		for(int i=0;i<primeIP.getWidth();i++)
			for(int j=0;j<primeIP.getHeight();j++)
				inv_ip.putPixel(i,j,1214); 

		
		primeIP.setFont(new Font(null,Font.ITALIC,60));
		primeIP.setColor(Color.RED);
		primeIP.setLineWidth(10); 
	
		setProcessor("Verifying", primeIP);
		
		
		 HashMap<Doublet,Punkt> lesPoints=new HashMap<Doublet,Punkt>();
		 HashMap<Doublet,Quadrilatere>lesCarres =new HashMap<Doublet,Quadrilatere>();
		 HashMap<Doublet,Quadrilatere>QuadrillageDeBase =new HashMap<Doublet,Quadrilatere>();
		 String ligne="rien"; 
		 double hE=0,incr=0; 
		try{
			// Open the data file and read the data
			 //File source = new File("/tmp/quadri.txt");
			File source=new File("D:/cylinder/quadri.txt"); 
			
			 BufferedReader in = new BufferedReader(new FileReader(source));
             ligne = in.readLine();
             Scanner rl=new Scanner(ligne); 
             rl.useLocale(Locale.US);
             hE=rl.nextDouble(); 
             incr=rl.nextDouble(); 
            // System.out.println(hE+" "+incr); 
             ligne=in.readLine();
            
            
             while(true){
            	// System.out.println("1->"+ligne); 
            	  rl=new Scanner(ligne); 
                 rl.useLocale(Locale.US);
                 int a= rl.nextInt(); 
                 int b=rl.nextInt(); 
                 double c=rl.nextDouble(); 
                 double d=rl.nextDouble();
            //     System.out.println("2->"+a+" "+b+" "+c+" "+d); 
                 lesPoints.put(new Doublet(a,b),new Punkt(a,b,c,d)); 
                 ligne=in.readLine(); 
             }
             
		}
		
		
		catch(InputMismatchException e){System.out.println("File read "+ligne);}
		catch(Exception e){System.out.println(e);   } // end of file reading in Windows
	
		double minx=10000; 
		double miny=10000; 
		double maxx=-10000; 
		double maxy=-10000; 
		int mina=10000; 
		int maxa=-10000; 
		int minb=10000; 
		int maxb=-10000; 
		// points are all read, build the quadrilaterals
		
		
		
		for(Doublet d:lesPoints.keySet()){
			int a=d.getA(); 
			int b=d.getB();
			
			
			// If the point is the upper left corner of a elementary square
			if (lesPoints.containsKey(new Doublet(a,b+1))&& // B
					lesPoints.containsKey(new Doublet(a+1,b))&& //C 
					lesPoints.containsKey(new Doublet(a+1,b+1))) //D
			{
				
				if(a<mina) mina=a; 
				if(a>maxa) maxa=a; 
				if(b<minb) minb=b; 
				if(b>maxb) maxb=b; 
			
				Punkt A=lesPoints.get(new Doublet(a,b)); 
				double vx=A.getY(); 
				double vy=A.getZ(); 
				if(vx<minx) minx=vx; 
				if(vy<miny) miny=vy; 
				if(vx>maxx) maxx=vx; 
				if(vy>maxy) maxy=vy;
				A=new Punkt(0,0,vx,vy); 
				Punkt B=lesPoints.get(new Doublet(a,b+1)); 
				vx=B.getY(); 
				vy=B.getZ(); 
				if(vx<minx) minx=vx; 
				if(vy<miny) miny=vy; 
				if(vx>maxx) maxx=vx; 
				if(vy>maxy) maxy=vy;
				B=new Punkt(0,0,vx,vy); 
				Punkt C=lesPoints.get(new Doublet(a+1,b));
				vx=C.getY(); 
				vy=C.getZ(); 
				if(vx<minx) minx=vx; 
				if(vy<miny) miny=vy; 
				if(vx>maxx) maxx=vx; 
				if(vy>maxy) maxy=vy;
				C=new Punkt(0,0,vx,vy); 
				Punkt D=lesPoints.get(new Doublet(a+1,b+1));
				vx=D.getY(); 
				vy=D.getZ(); 
				if(vx<minx) minx=vx; 
				if(vy<miny) miny=vy; 
				if(vx>maxx) maxx=vx; 
				if(vy>maxy) maxy=vy;
				D=new Punkt(0,0,vx,vy); 
				lesCarres.put(d,new Quadrilatere(A,B,C,D));
			}
			
		
			
		}// for Doublet 
		
		
		System.out.println(minx+" "+maxx+" "+miny+" "+maxy);
		System.out.println(mina+" "+maxa+" "+minb+" "+maxb);
		
		
		/* Uncomment the following if you want to see the squares on the original image*/
		/*
		setBounds(minx,maxx,miny,maxy); 
		setCoefs(); 

		for(Doublet d:lesCarres.keySet()){
			Quadrilatere q=lesCarres.get(d);
			drawLine(q.getUl(),q.getUr());
			drawLine(q.getUl(),q.getLl()); 
			drawLine(q.getLl(),q.getLr()); 
			drawLine(q.getLr(),q.getUr());		
			System.out.println(q);
		}
		
		for(Doublet d: lesPoints.keySet())
		{
			Punkt pp=lesPoints.get(d);
			drawPoint(pp); 
			
		}
			
		show(); 
		// freeze
		
		int kp=0; 
		while(kp==0) kp=0;
		*/
		
		
		
		int nbCarrY=(int)(hE/incr);  //maxa-mina;
		int nbCarrX=(int)(hE/incr);  //maxb-minb;
		
		double stepy=primeIP.getWidth()/(nbCarrY+0.0);
		double stepx=primeIP.getHeight()/(nbCarrX+0.0);
		int stepyInt=(int)stepy; //primeIP.getWidth()/nbCarrY;
		int stepxInt=(int) stepx; //primeIP.getHeight()/nbCarrX;
		System.out.println(hE+" "+incr+" "+nbCarrY+" "+stepx+" "+stepy); 


		// size of the visible part of the image (settable parameter : scaleb) 
		double ma=(maxx-minx)/2; 
		double mb=(maxy-miny)/2; 
		//double mc=Math.max(ma,mb); 
	
		setBounds(-scaleb*ma,scaleb*ma,-scaleb*mb,scaleb*mb);
		
		setCoefs(); 
		/*
		double minxx=100; 
		double maxxx=-100; 
		double minyy=100; 
		double maxyy=-100;
		*/ 
		for(int xa=mina; xa<=maxa;xa++)
			for(int xb=minb;xb<=maxb;xb++){
				if(lesCarres.containsKey(new Doublet(xa,xb))){
					// Compute the quadrilateral
					
					Punkt A=new Punkt(xa,xb,xa*stepy,xb*stepx); 
					Punkt B=new Punkt(xa+1,xb,(xa+1)*stepy,(xb)*stepx);
					Punkt C=new Punkt(xa,xb+1,(xa)*stepy,(xb+1)*stepx);
					Punkt D=new Punkt(xa+1,xb+1,(xa+1)*stepy,(xb+1)*stepx);
					
					
					Quadrilatere nouvo=new Quadrilatere(A,B,C,D);
					//System.out.println("Le carre "+nouvo); 
					QuadrillageDeBase.put(new Doublet(xa,xb), nouvo);
					
					
					// Setting the transformation from a square to its quadrilateral
					Transformation t=new Transformation(nouvo,lesCarres.get(new Doublet(xa,xb)));
			
					// For each pixel in the original image, compute its image
					// Parameter : mstepx, mstepy : distance between two adjacent points 
					
					double x=xa*stepy; //xa*stepyInt; 
					while(x<(xa+1)*stepy){//(x<(xa+1)*stepyInt){
						 
						double y=xb*stepx; //xb*stepxInt; 
						while (y<(xb+1)*stepx){ //(y<(xb+1)*stepxInt){
							// transform one point
							Punkt orig=new Punkt(0,0,x,y);
							//System.out.println("*** "+xa*stepyInt+" "+(xa+1)*stepyInt+" "+xb*stepxInt+" "+(xb+1)*stepxInt); 
							//System.out.println(orig); 
							Punkt dest=t.transforme(orig); 
							/*
							if(dest.getY()<minxx) minxx=dest.getY(); 
							if(dest.getY()>maxxx) maxxx=dest.getY(); 
							if(dest.getZ()<minyy) minyy=dest.getZ(); 
							if(dest.getZ()>maxyy) maxyy=dest.getZ();
							*/ 
							int col=primeIP.getPixel((int)x,(int)y);
							
							int col2=inv_ip.getPixel((int)(acoef*dest.getZ()+ccoef), (int)(vcoef*dest.getY()+wcoef));
							if(col2==1214)
							inv_ip.putPixel((int)(acoef*dest.getZ()+ccoef), (int)(vcoef*dest.getY()+wcoef), col);
							//else
							//inv_ip.putPixel((int)(acoef*dest.getZ()+ccoef), (int)(vcoef*dest.getY()+wcoef), (col+col2)/2);
							y=y+mstepy;
						}
						x=x+mstepx;
							}
				}
					
					
					 
			}// quadrillage image
		
		
		show();
		 
		/*
		inv_ip.setLineWidth(5); 
		for(Doublet a:lesCarres.keySet()){
			Quadrilatere q=lesCarres.get(a);
			inv_ip.setColor(Color.GREEN); 
			inv_ip.drawLine((int)(q.getUl().getZ()*acoef+ccoef),(int)(q.getUl().getY()*vcoef+wcoef),(int)(q.getUr().getZ()*acoef+ccoef),(int)(q.getUr().getY()*vcoef+wcoef));
			inv_ip.setColor(Color.RED); 
			inv_ip.drawLine((int)(q.getUl().getZ()*acoef+ccoef),(int)(q.getUl().getY()*vcoef+wcoef),(int)(q.getLl().getZ()*acoef+ccoef),(int)(q.getLl().getY()*vcoef+wcoef));
			//inv_ip.setColor(Color.RED);
			//inv_ip.drawLine((int)(q.getUr().getZ()*acoef+ccoef),(int)(q.getUr().getY()*vcoef+wcoef),(int)(q.getLr().getZ()*acoef+ccoef),(int)(q.getLr().getY()*vcoef+wcoef));
			//inv_ip.drawLine((int)(q.getLl().getZ()*acoef+ccoef),(int)(q.getLl().getY()*vcoef+wcoef),(int)(q.getLr().getZ()*acoef+ccoef),(int)(q.getLr().getY()*vcoef+wcoef));
			
		}
		*/
		/*
		
		System.out.println(minxx+" "+maxxx+" "+minyy+" "+maxyy); 
		
	
		int arrayProv[][]=new int[X][Y]; 
		
		// interpolation
		
		for(int i=1;i<X-1;i++)
			for(int j=1;j<Y;j++){
				int c=inv_ip.getPixel(i,j); 
				arrayProv[i][j]=c; 
				if(c==1214){
					int sum=0; 
					int nb=0; 
					for(int x=-1;x<2;x++)
						for(int y=-1;y<2;y++)
							if((x!=0)||(y!=0)){
								int c2=inv_ip.getPixel(i+x,j+y);
								if(c2!=1214){
									sum+=c2; 
									nb++; 
								}
							}// if x,y 
					if(nb!=0) arrayProv[i][j]=sum/nb; 
				}// if c
				
			}// for j
		for(int i=1;i<X-1;i++)
			for(int j=1;j<Y-1;j++) 
				inv_ip.putPixel(i, j, arrayProv[i][j]); 
		*/
	
		inverted.show();
	
		IJ.log("Copy the following line to Povray line 219:");
		IJ.log("scale<" + (ma * scaleb * 2) + ", 1, " + (mb * scaleb * 2) + ">");
		
		System.out.println("end !");
	
		
		}
	}	

	