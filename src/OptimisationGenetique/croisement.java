package OptimisationGenetique;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class croisement {
private static int nbVilles=Main.nbVilles;	

private static Random r=Main.r;
private static int nbchemins=Main.nbchemins;


/* code les types de croisement.  */
	


public static void PMX(int[]T1,int[]T2){
	r=new Random();
	int a=r.nextInt(nbVilles);
    int b = a+r.nextInt(nbVilles-a);
    
	int aux1=0;
	int aux=a;
	HashMap<Integer,Integer> H1= new HashMap<Integer,Integer>();
	//on les utilise pour O(ln(n))
	HashMap<Integer,Integer> H2= new HashMap<Integer,Integer>();	
	while (aux<b+1){
	 aux1= T2[aux];
	 H1.put(T2[aux],T1[aux]); //Je garde les paires en mémoire
	 H2.put(T1[aux],T2[aux]);
	 T2[aux]=T1[aux];
	 T1[aux]=aux1;
	 aux++;}
	aux=aux%nbVilles;
	//A la fin aux vaut b+1, on part de là
	while ( (aux<a) || (aux>b) ){
		
		if (H1.containsKey(T1[aux])){
			int toSwitch=H1.get(T1[aux]);
			while(H1.containsKey(toSwitch)){ //je switche avec le dernier qui n'est pas associé à une valeur interdite
				toSwitch=H1.get(toSwitch);
			}
			T1[aux]=toSwitch;}
		if (H2.containsKey(T2[aux])){
			int toSwitch=H2.get(T2[aux]);
			while(H2.containsKey(toSwitch)){
				toSwitch=H2.get(toSwitch);
			}
			T2[aux]=toSwitch;}
		aux= (aux+1)%(nbVilles);}
			
			
			
		}
  public static void OX(int[]T1, int []T2){
	r=new Random();
    int a=r.nextInt(nbVilles);
	int b = a+r.nextInt(nbVilles-a);
	Set<Integer> S1= new TreeSet<Integer>();
	//on les utilise pour O(ln(n))
	Set<Integer> S2= new TreeSet<Integer>();
    int aux=a;
	int i=0;
    int j=0;
    int aux1=(b+1)%nbVilles;
    int aux2=(b+1)%nbVilles;
    int[]T1aux=new int[nbVilles];//on les copie
    //pour conserver les données
    T1aux= annexe.copietableau(T1, T1aux);
    int[]T2aux=new int[nbVilles];
    T2aux= annexe.copietableau(T2, T2aux);
    while (aux<b+1){
   	 
   	 S1.add(T2[aux]); //Je garde les éléments en mémoire
   	 S2.add(T1[aux]);
   	 aux= aux+1;}
    
    while ( (aux1<a) || (aux1>b) ){
		
		if (!S1.contains(T1aux[i])){
			T1[aux1]=T1aux[i];
			
			aux1=(aux1+1)%(nbVilles);}
		i=(i+1)%nbVilles;}
	
    while( (aux2<a) || (aux2>b)){
		
    	if (!(S2.contains(T2aux[j]))){
			T2[aux2]=T2aux[j];
		    aux2= (aux2+1)%(nbVilles);}
        j=(j+1)%nbVilles;}
    aux=a;
    while ( (aux<b+1)){
    	int pivot= T2[aux];
    	T2[aux]=T1[aux];
      	T1[aux]=pivot;
      	aux=aux+1;
      	//A present on peut modifier le milieu
    }
    
    
  }
	
	public static void LOX (int[]T1,int[]T2){
		r=new Random();
		int a=r.nextInt(nbVilles);
	    int b = a+r.nextInt(nbVilles-a);
		Set<Integer> S1= new TreeSet<Integer>();
		//on les utilise pour O(ln(n))
		Set<Integer> S2= new TreeSet<Integer>();
		Set<Integer> Spos= new TreeSet<Integer>();
	    int aux=a;
		int i=0;
	    int j=0;
	    int pos1=0;
	    int pos2=0;
	    
	    int[] T1aux=new int[nbVilles];
	    int[] T2aux=new int[nbVilles];
	    annexe.copietableau(T1, T1aux);
	    annexe.copietableau(T2, T2aux);
	    while (aux<b+1){
	   	 
	   	 S1.add(T2[aux]); //Je garde les éléments en mémoire
	   	 S2.add(T1[aux]);
	   	 Spos.add(aux);
	   	 aux++;}
	    
	    while ( pos1<nbVilles ){
			
	    	if (Spos.contains(pos1)){
	    		pos1++;
	    	}
	    	else{
	    		if (!S1.contains(T1aux[i])&&(!Spos.contains(pos1))){
	    		
				T1[pos1]=T1aux[i];
				pos1++;
				i=(i+1)%(nbVilles);}
				
	    		else{
				i=(i+1)%(nbVilles);}
	    }		
	    }
	    
		while ( pos2<nbVilles ){
					
			if (Spos.contains(pos2)){
			   pos2++;}
			
			else{ if(!S2.contains(T2aux[j])&&(!Spos.contains(pos2))){
				T2[pos2]=T2aux[j];
				pos2++;
				j=(j+1)%(nbVilles);}
						
			else{
						j=(j+1)%(nbVilles);}
			}
			}
		aux=a;		
	    while ( aux<b+1){
	    	int pivot= T2[aux];
	    	T2[aux]=T1[aux];
	      	T1[aux]=pivot;
	      	aux++;
	      	//A present on peut modifier le milieu
	    }
	}
	
	public static void UOX (int []T1,int[]T2,int a,int b){
		
	}
  
	/*public static void onepoint(int []T1, int []T2){ //inutilisable
	r=new Random();
    float r1=r.nextFloat();
	int k=nbVilles/2;
	int[]fils1= new int [nbVilles];
	fils1= annexe.copietableau(T1,fils1);//evite reference
	int[]fils2= new int [nbVilles];
	fils2= annexe.copietableau(T1,fils2);//evite reference
	if(r1<Pcross){
	  for(int j=k;j<nbVilles;j++){	
		  int pivot= T1[j];
		  T1[j]=T2[j];
		  T2[j]= pivot;}}}
	  
	

public static int [][] deuxpoints(int []T1, int []T2){ //inutilisable
	int k=nbVilles/3;
	int[]fils1= new int [nbVilles];
	int[]fils2= new int [nbVilles];
	for (int i=0;i<k;i++){
		fils1[i]=T1[i];
		fils2[i]=T2[i];}
	for(int j=k;j<2*k;j++){	
	   fils1[j]=T2[j];
	   fils2[j]=T1[j];}
	for (int l=2*k;l<nbVilles;l++){
		fils1[l]=T1[l];
		fils2[l]=T2[l];}
	int[][]R={fils1,fils2};
	return R;
	}
public static int [][] uniforme(int[]T1,int []T2){ //inutilisable
int[]fils1= new int [nbVilles];
int[]fils2= new int [nbVilles];
for (int i=0;i<nbVilles;i++){
	fils1[i]=T1[i];
	fils2[i]=T2[i];
	if (i<nbVilles-1){
		fils1[i+1]=T2[i+1];
		fils2[i+1]=T1[i+1];
		i++;}}
int[][]R={fils1,fils2};
return R;
	
}*/
  
  
  

	}









