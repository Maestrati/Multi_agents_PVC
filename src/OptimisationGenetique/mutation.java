package OptimisationGenetique;

import java.util.Random;

public class mutation {
private static int nbVilles=Main.nbVilles;

/* code les types de mutation.  */

public static void transposition(int[]T){
	Random r=new Random();
	int r1=r.nextInt(nbVilles);
	if (r1==0){
	 shuffle.permut(0,1,T);}
	else{
		shuffle.permut(r1-1,r1,T);}
	
	}
	


public static void transpositionalea(int[]T){
	 Random r=new Random();
	 int r1=r.nextInt(nbVilles);
	 int r2=r.nextInt(nbVilles);
	 shuffle.permut(r1,r2,T);
	 }

public static void inversion(int []T){
	Random r=new Random();
	int r1=r.nextInt(nbVilles);
	int r2=r.nextInt(nbVilles);
	while(r1<r2){
		shuffle.permut(r1,r2,T);
		r1=r1+1;
		r2=r2-1;}
	
	}







}
