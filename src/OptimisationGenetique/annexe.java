package OptimisationGenetique;


/*  outils */

public class annexe {
	public static int[]copietableau(int []t1,int []t2){
		int n=t2.length;
		for (int i=0;i<n;i++){
			t2[i]=t1[i];}
		return t2;
		
		}
	
	public static double[]copietableau(double []t1,double []t2){
		int n=t2.length;
		for (int i=0;i<n;i++){
			t2[i]=t1[i];}
		return t2;
		
		} 




public static double sommelongchemin(int [][]T){
	double somme=0;
	int n= T.length;
	for(int j=0; j<n;j++){
		somme=somme + Main.longchemin(T[j]);}
	return somme;
	
}
public static float valref(float y){
	return y;
}
}