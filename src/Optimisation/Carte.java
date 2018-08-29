package Optimisation;

public class Carte {
	/*********************************
	 * Cette Classe à pour but de décrire un problème
	 * On définit la liste des villes à l'aide d'une chaine de caractères 
	 * et les distances entre les villes à l'aide d'une matrice des distances
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
	//permet de construire une carte à partir d'une matrice des distances
	 public Carte(float[][] dist, int[] villes){
		 nbVilles = dist.length; 
		 listVilles = villes;
		 nbVilles = dist.length;
		 distances = new  double[nbVilles][nbVilles];
		 for (int i = 0;i < nbVilles; i++)         
			 for (int j = 0;j < nbVilles; j++)
				 distances[i][j] = dist[i][j];
	 }
	 
	 //permet de construire une carte à partir d'une liste des coordonnées GPS
	 public Carte(double lat[],double lon[], int[] villes){
		 distances =  gpsListToMatDist(lat,lon);  //on crée la matrice des distances à partir des coordonnées GPS
		 listVilles = villes;
		 nbVilles = distances.length;
	 }
	 
	 /*
	  * **********************METHODES STATIQUES*************************
	  */
	 
	 //permet de calculer une distance entre 2 villes à partir de leurs coordonnées GPS
	 public static double gpsToDist(double lat1, double lon1, double lat2, double lon2){
		 //entrée en degrée
		 //sortie en km
		 //conversion
		 lat1 = Math.PI*lat1/180;  //conversion degrées -> radians
		 lon1 = Math.PI*lon1/180;
		 lat2 = Math.PI*lat2/180;
		 lon2 = Math.PI*lon2/180;
		 return 6366*(Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)));
		 //formule permettant de calculer les distances entre deux villes
	 }
	 
	 //on calcul une matrice des distances à partir d'une liste de coordonnées GPS
	 public static double[][] gpsListToMatDist(double lat[],double lon[]){
		 int n = lat.length;
		 double dist[][] = new double[n][n];
		 for (int i = 0; i <n;i++)
			 for (int j = i+1; j < n;j++){  //on parcourt la matrice
				 dist[i][j] = gpsToDist(lat[i],lon[i],lat[j],lon[j]);//calcul de la distance
				 dist[j][i] = dist[i][j];  //matrice symétrique
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
