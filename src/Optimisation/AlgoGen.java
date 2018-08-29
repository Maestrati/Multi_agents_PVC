package Optimisation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AlgoGen {
	/********************************************************************
	 * Permet l'impl�mentation d'un algoritme g�n�tique, 
	 * pour la r�solution du probl�me du voyageur de commerce
	 ********************************************************************/
	
	/*
	 * *************** PARAMETRES STATIQUES ******************************
	 */
	static private int nbChrom = 30;             //taille de la population
	static private double probMut = 0.7;         // probabilit� pour un chromosome de muter
	static private int nbSelect = 8;             // nombre d'individus s�lectionn�s � chaque g�n�ration
	static private boolean simpleCross = false;  // D�finit type de croisement
	
	/*
	 * ********************** PARAMETRES *********************************
	 */
	private Carte carte;                         // d�crit le probl�me
	public int[][] pop;                         //ensemble des solutions
	public int[][] popNext;                     // population de la prochaine g�n�ration
	public float[] popFit;                       // fitness des individus de la population
	private int generation = 1;                  
	private int longueurChrom = 6;
	
	/*
	 * ************************ GETTERS ET SETTERS*************************
	 */
	
	public  void setLongueurChrom(int longueurChrom) {
		longueurChrom = longueurChrom;
	}

	public static void setNbChrom(int nbChrom) {
		AlgoGen.nbChrom = nbChrom;
	}

	public static void setProbMut(double probMut) {
		AlgoGen.probMut = probMut;
	}

	public static void setNbSelect(int nbSelect) {
		AlgoGen.nbSelect = nbSelect;
	}

	public static void setSimpleCross(boolean simpleCross) {
		AlgoGen.simpleCross = simpleCross;
	}
	
	public int[] getIndividus(int i){
		return pop[i];
	}

	public float[] getPopFit() {
		return popFit;
	}
	
	/*
	 *********************** CONSTRUCTEURS ***********************
	 */

	// G�n�r� � partir de l'exemple du TD
	public AlgoGen(){
		carte = new Carte();
		longueurChrom = carte.nbVilles;
		pop = new int[nbChrom][longueurChrom];
		popNext = new int[nbChrom][longueurChrom];
		popFit = new float[nbChrom];
		
		generePop();            // on g�n�re une population al�atoirement
		
		calculFitPop();         // on met � jour la fitness des individus
	}
	 
	
	// G�n�r� � partir d'une matrice de distance
	public AlgoGen(float[][] dist,int[] villes){
		carte = new Carte(dist,villes);
		longueurChrom = carte.nbVilles;
		pop = new int[nbChrom][longueurChrom];
		popNext = new int[nbChrom][longueurChrom];
		popFit = new float[nbChrom];
		generePop();
		calculFitPop();
	}
	
	// g�n�r� � partir d'une liste de coordonn�e GPS
	public AlgoGen(double lat[],double lon[], int[] villes){
		carte = new Carte(lat,lon,villes);
		longueurChrom = carte.nbVilles;
		pop = new int[nbChrom][longueurChrom];
		popNext = new int[nbChrom][longueurChrom];
		popFit = new float[nbChrom];
		generePop();
		calculFitPop();
	}
	
	//g�n�r� � partir d'une carte
	public AlgoGen(Carte carte){
		this.carte = carte;
		longueurChrom = carte.nbVilles;
		pop = new int[nbChrom][longueurChrom];
		popNext = new int[nbChrom][longueurChrom];
		popFit = new float[nbChrom];
		generePop();
		calculFitPop();
	}
	
	
	/*
	 * ******************** METHODES *******************************
	 */
	
	// permet l'affichage d'une population
	public void affiche(){
		System.out.println("Generation: " + generation); 
		System.out.println("Population");
		for (int i =0; i<nbChrom;i++)
			System.out.println(Arrays.toString(pop[i]) + '-' + popFit[i]);  // affiche les individus avec la valeur de leur fitness
	
	}
	
   /**************** OPERATEUR MUTATION *************************/	
	
	//permet de permuter deux �l�ment d'un individu
	public void permut(int ind, int i, int j){
		// on d�coupe la chaine de carat�re pour la reformer dans l'ordre souhait�
		int support = pop[ind][i];
		pop[ind][i] = pop[ind][j];
		pop[ind][j] = support;
		
	}
	
	
	// permet de g�n�rer une population al�atoirement
	public void generePop(){
		for (int i = 0; i <nbChrom; i++){    // on parcourt l'ensemble des individus
			pop[i] = Arrays.copyOf(carte.getListVilles(), longueurChrom);
			for (int j = 0; j <60; j++ ){     // chaque individus subit des permutations al�atoirement
				int a =(int)(Math.random()*longueurChrom);
				int b = (int)(Math.random()*longueurChrom);
				permut(i,a ,b);  //permutation
			}
		
			//on admet que la population g�n�r�e est suffisament al�atoire
		}
	}
	
	// Op�rateur de mutation
	public void mutation(int ind){
		if (Math.random()<probMut){    // la mutation est effective avec une probabilit� de probMut
			int a =(int)(Math.random()*longueurChrom);      // on s�lectionne les deux villes � permuter
			int b = (int)(Math.random()*longueurChrom);
			permut(ind,a ,b);
		}
	}
	
	/************************ OPERATEUR SELECTION *************************/
	// Permet de calculer la fitness d'un individus
	public float fitness(int[] chromosome){
		float res = 0;
		for (int i=0; i < longueurChrom; i++) //on parcourt la chaine de caract�re
			res += carte.distance(chromosome[i], chromosome[(i+1) % longueurChrom]);
		//on additionne toute les distances interm�diaires
		// Rq. l'utilisation du modulo permet de fermet le cycle. 
		return res;
	}
	
	//met � jour les valeurs de la fitness de chaque individu
	public void calculFitPop(){
		for (int i = 0; i <nbChrom; i++){
			popFit[i] = fitness(pop[i]);
		}
	}
	
	//Trie les individus par ordre d�croissant de distance
	// On utilise un trie par insertion car les population sont relativement petite
	public void trieInsert(){
		int i, j;
		   for (i = 1; i < nbChrom; ++i) {
		       float elemV = popFit[i];
		       int[] elem = Arrays.copyOf(pop[i], longueurChrom);
		       for (j = i; j > 0 && popFit[j-1] > elemV; j--){  //trie indirect
		           popFit[j] = popFit[j-1];
		           pop[j] = Arrays.copyOf(pop[j-1], longueurChrom);  
		       }
		       popFit[j] = elemV;  
		       pop[j] =  Arrays.copyOf(elem, longueurChrom);
		   }
	}
	
	
	
	
	
	/************************ OPERATEUR REPRODUCTION *************************/
	
	public ArrayList<Integer> tabToArray(int[] tab){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for( int i = 0; i<tab.length;i++){
			list.add(tab[i]);
		}
		return list;
	}
	
	public ArrayList<Integer> diffChaines(ArrayList<Integer> chaine1,ArrayList<Integer> chaine2){
		//retourne les �l�ments de la chaine1 qui ne sont pas dans chaine 2
		ArrayList<Integer> res = new ArrayList<Integer>();
		int n = chaine1.size();
		for (int i = 0; i  <n; i++)
			if (!chaine2.contains(chaine1.get(i)) && !res.contains(chaine1.get(i))) // on regarde si le caract�re est dans la chaine
				res.add(chaine1.get(i));
		return res;
	}
	
	
	// d�finie quel est la m�thode de croisement utilis�
	public void croisement(int ind1, int ind2, int rang){
		if (simpleCross)
			croisementSimple(ind1, ind2, rang);
		else
			croisementDouble(ind1, ind2, rang);
			
	}
	
	
	//effectue un croisement simple
	public void croisementSimple(int ind1, int ind2, int rang){
		if (ind1 != ind2){
		int i = (int)(Math.random()*longueurChrom);	//s�lection du pivot
		for(int k=0; k<i; k++) {
			popNext[rang][k] = pop[ind1][k];//on effectue l'�change de g�nes
			popNext[rang+1][k] = pop[ind2][k];
		}
		for(int k=i; k<longueurChrom; k++) {
			popNext[rang][k] = pop[ind2][k];//on effectue l'�change de g�nes
			popNext[rang+1][k] = pop[ind1][k];
			}
		//correction du premier enfant
		
		ArrayList<Integer> substitut = new ArrayList<Integer>();
		substitut = diffChaines(tabToArray(Arrays.copyOfRange(pop[ind1], i,longueurChrom)),tabToArray(Arrays.copyOfRange(pop[ind2], i,longueurChrom)));
		int k;
		for(int j = i; j < longueurChrom; j++ ){
			k = 0;
		// on parcourt la deuxi�me sous-chaine, si il y a un probl�me, on le remplace par le premier �l�ment qui convient dans l'autre individus
			if(tabToArray(Arrays.copyOf(popNext[rang], j)).contains(popNext[rang][j])){
				popNext[rang][j] = substitut.get(k);// on d�coupe la chaine pour faire le remplacement
				k++;
			}
		}
		//correction du deuxi�me enfant
		k = 0;
		substitut = diffChaines(tabToArray(Arrays.copyOfRange(pop[ind2], i,longueurChrom)),tabToArray(Arrays.copyOfRange(pop[ind1], i,longueurChrom)));
		for(int j = i; j < longueurChrom; j++ )
			if(tabToArray(Arrays.copyOf(popNext[rang+1], j)).contains(popNext[rang+1][j])){
				popNext[rang + 1][j] = substitut.get(k);
				k++;
			}
		}
	}
	
	//effectue un croisement double PMX (avec correction effectu�e sur le centre)
	public void croisementDouble(int ind1, int ind2, int rang){
		
		int i1 = (int)(Math.random()*longueurChrom);
		int i2 = (int)(Math.random()*longueurChrom);
		if (i1>i2){
			int it = i1;
			i1 = i2;
			i2 = it;			
		}
		for(int k=0; k<i1; k++) {
			popNext[rang][k] = pop[ind1][k];//on effectue l'�change de g�nes
			popNext[rang+1][k] = pop[ind2][k];
		}
		for(int k=i1; k<i2; k++) {
			popNext[rang][k] = pop[ind2][k];//on effectue l'�change de g�nes
			popNext[rang+1][k] = pop[ind1][k];
			}
		for(int k=i2; k<longueurChrom; k++) {
			popNext[rang][k] = pop[ind1][k];//on effectue l'�change de g�nes
			popNext[rang+1][k] = pop[ind2][k];
			}
		if (ind1 != ind2){
		//appliquer la correction
		//correction du premier enfant
		int k = 0;
		
		ArrayList<Integer> substitut = diffChaines(tabToArray(Arrays.copyOfRange(pop[ind1], i1,i2)),tabToArray(Arrays.copyOfRange(pop[ind2], i1,i2))); //on parcourt la chaine centrale, 
		
		//si il y a un redondance d�tect�e, on la remplace par un �l�ment de la chaine centrale de l'autre individu
		for(int j = i1; j < i2; j++ ){
			
			ArrayList<Integer> L1 =tabToArray(Arrays.copyOf(popNext[rang], i1));
			ArrayList<Integer> L2 =tabToArray(Arrays.copyOfRange(popNext[rang], i2, longueurChrom));
			L1.addAll(L2);
			if(substitut.size() != 0 && L1.size() != 0 && L1.contains(popNext[rang][j])){
				
				popNext[rang][j] = substitut.get(k);
				k++;
			}
		}

		k = 0;
		/*System.out.println(Arrays.toString(Arrays.copyOfRange(pop[ind2], i1,12)));
		System.out.println(tabToArray(Arrays.copyOfRange(pop[ind2], i1,12)));*/
		
		substitut = diffChaines(tabToArray(Arrays.copyOfRange(pop[ind2], i1,i2)),tabToArray(Arrays.copyOfRange(pop[ind1], i1,i2)));
	
		for(int j = i1; j < i2; j++ ){
			ArrayList<Integer> L1 =tabToArray(Arrays.copyOf(popNext[rang + 1], i1));
			ArrayList<Integer> L2 =tabToArray(Arrays.copyOfRange(popNext[rang + 1], i2, longueurChrom));
			L1.addAll(L2);
			if(substitut.size() != 0 && L1.size() != 0 && L1.contains(popNext[rang + 1][j])){
				popNext[rang+1][j] = substitut.get(k);
				k++;
			}
		}
		}
		
	
	}
	
	/********************Calcul d'optimisation ***************************/
	// Calcul la prochanine g�n�ration
	public void nextGen(){
		
		for (int i = 0; i <  nbChrom; i+=2){       // on effectue les croisements
			int pere = (int) (Math.random()*nbSelect);//on s�lectionne tire au sort les individus s�lectionn�s
			int mere = (int) (Math.random()*nbSelect);
			
			croisement(pere,mere,i);	//reproduction
		}
		
		for (int i = 0; i <  nbChrom; i++){ //les nouveaux individus mutent
			pop[i] = Arrays.copyOf(popNext[i],longueurChrom);
			mutation(i);
		}
		calculFitPop();  // mise � jour de la fitness
		trieInsert();    //trie,  ie s�lection
		generation++;
	}
	
	
	//calcul plusieurs g�n�rations
	public void plusieursGen(int n){
			trieInsert();
			//affiche();
			for (int i = 0; i < n; i++){
				nextGen();
				//affiche();
				//System.out.println(popFit[0]);
			}	
			
			

	}
	

	

}
