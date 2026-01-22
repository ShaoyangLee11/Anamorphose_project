// Povray file to build anamorposes
#include "colors.inc"
#include "stones.inc"
#include "stones2.inc"
#include "woods.inc"
#include "glass.inc"
#include "textures.inc"
#include "metals.inc"
#include "skies.inc"
#include "transforms.inc"
#include "shapes.inc"

global_settings { 
    max_trace_level 255
    photons {
        spacing 0.005 // 数值越小，光子越密集，反射光影越细腻
        autostop 0
    }
     
}

/* Variables you can tune */
// rendering=0 : first step --> computing the grid and outputting the coordinates data 
// rendering=1 : final step --> rendering the anamorphosis 
#declare rendering=1;

// Controls the position of the screen between the eye and the mirror
#declare boing=1.5; 
#declare hauteurEcran=1; 
#declare visionPoint=<2,1,-5>; 
#declare centreObjet=<0,1.5,0>; 
#declare diamCylinder=0.028; 
#declare diamSphere=2.5*diamCylinder; 

/* No more tuning beyond this line ! */
#declare myAngle=-180/pi*atan2(visionPoint.z,visionPoint.x);
#declare direc=visionPoint-centreObjet;
#declare normdirec=vnormalize(direc);

sky_sphere {
    pigment {
        gradient y          // 沿 Y 轴（上下）渐变
        color_map {
            [0.0 color rgb <0.6, 0.7, 1.0>] // 底部颜色（地平线）
            [0.7 color rgb <0.1, 0.3, 0.8>] // 中间颜色
            [1.0 color rgb <0.0, 0.1, 0.4>] // 顶部颜色（天顶）
        }
    }
}  //这是HDMI天空盒

// 修复后的灯光循环
#declare ra=0;
#declare nb=3;
#while (ra < nb)
    light_source {
        <0, 10, 0>
        color rgb <1,1,1>
        area_light <5, 0, 0>, <0, 0, 5>, 8, 8 
        adaptive 0.2 
        jitter 
        circular 
        orient 
        translate <ra*10 - 10, 0, 0> 
        photons { refraction on reflection on }
        media_attenuation on 
        shadowless 
    }
    #declare ra = ra + 1;
#end

// 坐标输出宏
#macro afficheVecteur(vv) 
    concat(" ",str(vv.x,0,-1)," ",str(vv.z,0,-1)," \n") 
#end

// Cylinder shape definition
#declare Sphere_Mirror = sphere {
    <0, 1.5, 0>, 1.5   
    scale <1.2, 2.5, 1.2>
    texture { T_Chrome_5E }
    finish { 
        Metallic_Finish 
        reflection { 0.9 } 
    }
}
// Image mapping
#declare image=box{<0,0,0>,<1,0.0000001,1> 
    pigment{ 
        image_map{ png "Inverted image.png" map_type 0 } // modify this line
        rotate 90*x 
    } 
    finish{ambient 0.8 diffuse 1} 
    translate<-0.5,0,-0.5> scale 1 
}

#declare lePlan=plane{y,0 texture{pigment{color Gray90}} }
#declare rond=object {Sphere_Mirror}

#declare tournicotis=transform{ 
    rotate atan2(direc.y,sqrt(direc.x*direc.x+direc.z*direc.z))*180/pi*z 
    rotate -atan2(direc.z,direc.x)*180/pi*y 
    translate centreObjet 
    translate boing*normdirec 
}

#declare axeEcran1=cylinder{ <0,-hauteurEcran/2,-hauteurEcran/2>, <-0.01,hauteurEcran/2,hauteurEcran/2>,0.015 texture{pigment{color Red}} } 
#declare axeEcran2=cylinder{ <0,hauteurEcran/2,-hauteurEcran/2>, <-0.01,-hauteurEcran/2,hauteurEcran/2>,0.015 texture{pigment{color Red}} } 
#declare ecran=box{<0,-hauteurEcran/2,-hauteurEcran/2>,<-0.01,hauteurEcran/2,hauteurEcran/2> texture{pigment{color rgbt<0,1,1,0.8>}} } 
#declare ecranTotal=union{ object{ecran} object{axeEcran1} object{axeEcran2} transform{tournicotis} }

// Scene Rendering
object{lePlan } 
object{rond }

#if(rendering=1) 
    object{image scale<17.070073999999998, 1, 12.498315> rotate (-90)*y }//modify this line 
#end 

#if(rendering=0)
    #declare inc=0.01; 
    #declare array_needed = int(hauteurEcran / inc) + 10; 
    
    #declare depart = array[array_needed]; 
    #declare arrivee = array[array_needed]; 
    
    #declare bb = 0; 
    #while(bb < array_needed) 
        #declare depart[bb] = <-1,-1,-1>; 
        #declare arrivee[bb] = <-1,-1,-1>; 
        #declare bb = bb + 1; 
    #end

    #declare imax=hauteurEcran/2; 
    #declare indexy=-imax; 
     
    #declare normy=<1,1,1>; 
    #declare normy2=<1,1,1>; 

    #debug concat(str(hauteurEcran,3,3)," ",str(inc,3,3),"\n") 

    #declare indicey=0; 
    #while(indexy<=imax) 
        #declare indexz=-imax; 
        #declare indicez=0; 
        #while(indexz<=imax) 
            #declare p1=vtransform(<0,indexy,indexz>, transform{tournicotis}); 
            #declare punkt=<-visionPoint.x+p1.x,-visionPoint.y+p1.y,-visionPoint.z+p1.z>; 
            #declare poInter=trace(rond,visionPoint,punkt,normy); 
            
            #if(vlength(normy)!=0) 
                #declare normPlan=vcross(punkt,normy); 
                #declare angola=vdot(punkt,normy); 
                #declare angola=acos(angola/(vlength(punkt)*vlength(normy)))*180/pi; 
                #declare reflechi=vaxis_rotate(normy,normPlan,angola); 
                #declare onPlane=trace(lePlan,poInter,-reflechi,normy2); 
                
                #if(vlength(normy2)!=0) 
                    sphere{onPlane,diamSphere texture{pigment{color Yellow}}} 
                    #declare depart[indicez]=onPlane; 
                    #debug concat (str(indicey,0,0)," ",str(indicez,0,0)," ",afficheVecteur(depart[indicez])) 
                    
                    #if(indicez!=0) 
                        #if(depart[indicez-1].y!=-1) 
                            cylinder{depart[indicez-1],depart[indicez],diamCylinder texture{pigment{color Green}}} 
                        #end 
                        #if((indicey!=0)&(arrivee[indicez].y!=-1)) 
                            cylinder{arrivee[indicez],depart[indicez],diamCylinder texture{pigment{color Green}}} 
                        #end 
                    #end 
                #end 
            #end 
            #declare indicez=indicez+1; 
            #declare indexz=indexz+inc; 
        #end 

        #declare bb=0; 
        #while(bb<array_needed) 
            #if(depart[bb].y!=-1) 
                #declare arrivee[bb]=depart[bb]; 
                #declare depart[bb]=<-1,-1,-1>; 
            #end 
            #declare bb=bb+1; 
        #end 
        #declare indexy=indexy+inc; 
        #declare indicey=indicey+1;
    #end
#end 

camera{ location visionPoint look_at centreObjet }