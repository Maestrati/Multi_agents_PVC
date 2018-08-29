package OptimisationGenetique;
//J'ai opté pour un tris fusion (O(n*ln(n)))
public class tris {
//On Effectue un tris fusion (en O(n*ln(n)))
public static void reunionfusion(double[][]t, int d, int m, int f){
	double aux[][]=new double[m-d+1][2];
	int i,j,k;
	for(i=0;i<m-d+1;i++){

		aux[i]=t[d+i];} //On fait une copie de la première partie
	i=0;j=m+1;
	for (k=d;k<f+1;k++){
		if(i==m-d+1){
			t[k]=t[j];				
			j++;
		}
		else if (j==f+1){
			t[k]=aux[i];
			i++;
		}
		
		else if(aux[i][1]<=t[j][1]){
		   
			t[k]=aux[i];
			i++;
			}
		
		else{
			t[k]=t[j];				
			j++;
		}}
	}
public static void fusion(double[] []t, int iDebut, int iFin) {
	if (iDebut < iFin) {  
	    int m = (iDebut + iFin)/2; 
		fusion(t, iDebut, m); 
		fusion(t, m + 1, iFin); 
		reunionfusion(t, iDebut, m, iFin);
	}}

public static void trifusion (double[][]t){
	fusion (t,0, t.length-1);
}
}
