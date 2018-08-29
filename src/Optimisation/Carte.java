package Optimisation;

public class Carte {
	/*********************************
	 * Cette Classe � pour but de d�crire un probl�me
	 * On d�finit la liste des villes � l'aide d'une chaine de caract�res 
	 * et les distances entre les villes � l'aide d'une matrice des distances
	 ********************************/
	
	/*
	 **************** PARAMETRES***********************
	 */
	 public int nbVilles;
	 
	 public double[][] distances; 
	 public int[] listVilles;
	 
	 /*
	  ***************** GETTERS ET SETTERS*************
	  */
	 
	 public int[] getListVilles() {
		return listVilles;
	}

	 /*
	  * ***************** CONSTRUCTEURS***************
	  */
	
	 //permet de construire la carte du TD
	 public Carte(){
		 double[][] dist = new  double[][]{{0, 780, 320, 580, 480, 660},
			 							{780, 0, 700, 460, 300, 200},
			 							{320, 700, 0, 380, 820, 630},
			 							{580, 460, 380, 0, 750, 310},
			 							{480, 300, 820, 750, 0, 500},
			 							{660, 200, 630, 310, 500, 0}};
		 listVilles = new int[]{0,1,2,3,4,5};
		 nbVilles = dist.length;
		 distances = new  double[nbVilles][nbVilles];
		 for (int i = 0;i < nbVilles; i++)
			 for (int j = 0;j < nbVilles; j++)
				 distances[i][j] = dist[i][j];
	 }
	//permet de construire une carte � partir d'une matrice des distances
	 public Carte(float[][] dist, int[] villes){
		 nbVilles = dist.length; 
		 listVilles = villes;
		 nbVilles = dist.length;
		 distances = new  double[nbVilles][nbVilles];
		 for (int i = 0;i < nbVilles; i++)         
			 for (int j = 0;j < nbVilles; j++)
				 distances[i][j] = dist[i][j];
	 }
	 
	 //permet de construire une carte � partir d'une liste des coordonn�es GPS
	 public Carte(double lat[],double lon[], int[] villes){
		 distances =  gpsListToMatDist(lat,lon);  //on cr�e la matrice des distances � partir des coordonn�es GPS
		 listVilles = villes;
		 nbVilles = distances.length;
	 }
	 
	 /*
	  * **********************METHODES STATIQUES*************************
	  */
	 
	 //permet de calculer une distance entre 2 villes � partir de leurs coordonn�es GPS
	 public static double gpsToDist(double lat1, double lon1, double lat2, double lon2){
		 //entr�e en degr�e
		 //sortie en km
		 //conversion
		 lat1 = Math.PI*lat1/180;  //conversion degr�es -> radians
		 lon1 = Math.PI*lon1/180;
		 lat2 = Math.PI*lat2/180;
		 lon2 = Math.PI*lon2/180;
		 return 6366*(Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)));
		 //formule permettant de calculer les distances entre deux villes
	 }
	 
	 //on calcul une matrice des distances � partir d'une liste de coordonn�es GPS
	 public static double[][] gpsListToMatDist(double lat[],double lon[]){
		 int n = lat.length;
		 double dist[][] = new double[n][n];
		 for (int i = 0; i <n;i++)
			 for (int j = i+1; j < n;j++){  //on parcourt la matrice
				 dist[i][j] = gpsToDist(lat[i],lon[i],lat[j],lon[j]);//calcul de la distance
				 dist[j][i] = dist[i][j];  //matrice sym�trique
			 }
		 return dist;
	 }
	 
	 /*
	  * *****************************METHODES DYNAMIQUES*****************
	  */
	
	 
	 //calcul la distance entre deux ville connaissant leurs identifiant
	 public  double distance(int a, int b){
		 return distances[a][b]; 
		 }

}
