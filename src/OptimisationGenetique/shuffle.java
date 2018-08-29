package OptimisationGenetique;

/* permet de coder des permutations aléatoires.  */
import java.util.Random;

public class shuffle {

public static void permut(int i, int choisis, int [] liste){
		int pivot= liste[i];
		liste[i]=liste [choisis];
		liste[choisis]= pivot;
	}

public static void permut2(int i, int choisis, int [][] liste){
	int []pivot= liste[i];
	liste[i]=liste [choisis];
	liste[choisis]= pivot;
}
	
	
public static int [] shuffler(int[] liste){ //utilisé dans newpopulation() de la classe Main

	
	int n = liste.length;
	
	for (int i=0; i<n;i++){
		
		
		int choisis = i+ Main.r.nextInt (n-i);
		permut(i,choisis,liste);}
	
	return (int[]) liste;
	}
}



