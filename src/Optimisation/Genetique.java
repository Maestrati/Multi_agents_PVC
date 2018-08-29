package Optimisation;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;

public class Genetique {
	
// Paramètres liés au problèmes :
	
	public int  Nville;
	
	//private static int Nville=100;
	
	//private static int[][] Distances={{0,780,320,580,480,660},{780,0,700,460,300,200},{320,700,0,380,820,630},{580,460,380,0,750,310},{480,300,820,750,0,500},{660,200,630,310,500,0}};
	
	public Carte Distances;
	
	public int[][] pop;
	
	public ArrayList<Double> score;
	
// Paramètres de l'algorithme à modifier lors des tests:
	
	private static int Npop=512;				// Taille de la population.
	
	private static int Nselection=128;		// Taille de la population sélectionnée.
	
	private static double Pcross=0.5;		// Probabilité de croisement.
	
	private static double Pmut=0.5;			// Probabilité de mutation.

	// Constructeur //
	
	public Genetique (float[][] dist, int[] villes) {
		Distances = new Carte(dist, villes);
		Nville = Distances.nbVilles;
		pop = GeneratePopulation();
	}
	
	public Genetique (double lat[],double lon[], int[] villes) {
		Distances = new Carte(lat, lon, villes);
		Nville = Distances.nbVilles;
		pop = GeneratePopulation();
	}
	public Genetique (Carte carte) {
		Distances = carte;
		Nville = Distances.nbVilles;
		pop = GeneratePopulation();
	}
	
// Génération de la population de manière aléatoire :
	public int[][] GeneratePopulation() {
		int tab[][]= new int[Npop][Nville];
		for(int i=0; i<Npop; i++) {
			int[] Liste= new int[Nville];
			for (int k = 0; k <Nville; k++) { Liste[k] = k; }
		    tab[i]= ShuffleArray.shuffleArray(Liste);	// On rajoute dans le tableau de population la liste des 6 éléments mélangés de manières aléatoire.
		}
		return(tab);
	}
	
// Calcul de la fonction d'évaluation/d'adaptation :
	public double[] Fitness(int[][] tab){			// Ici la fonction de Fitness est la fonction qui calcule la distance du chemin parcouru.
		double[] sum= new double [Npop];
		for(int i=0; i<Npop; i++){						// Boucle selon chaque membre de la population.
			sum[i]=0;
					for(int j=0; j<Nville-1; j++){		// Boucle où l'on calcule la distance du chemin d'un individu de la population.
						sum[i] = sum[i] + Distances.distance(tab[i][j],tab[i][j+1]);
					}
			sum[i] = sum[i] + Distances.distance(tab[i][Nville-1],tab[i][0]);	// On fait ceci pour tenir compte du retour vers le point de départ.
		}
		return(sum);
	}

	public double fitness(int[] tab){
		double sum= 0;
		for(int j=0; j<Nville-1; j++){
			sum = sum + Distances.distance(tab[j],tab[j+1]);
		}
		sum = sum + Distances.distance(tab[Nville-1],tab[0]);
		return(sum);
	}
	
// Différents méthodes de sélection de la population :
	
	public int[][] SelectionDeterministe(int[][] tab){
		int[][] newtab = new int [Nselection][Nville];
		int[][] select = new int [Npop][Nville];
		double[] fit = Fitness(tab);
		double[] fit2 = Fitness(tab);
		Arrays.sort(fit2);							// tri par ordre croissant des éléments. 
		for(int i=0; i<Nselection; i++){
			int j=0;
			while(fit[j]!=fit2[i]){
				j=j+1;
			}
			newtab[i]=tab[j];
		}
		for(int k=0; k<Npop; k++){
			select[k]=newtab[k%Nselection];
		}	
		return(select);
	}
	
	public int[][] SelectionStochastique(int[][] tab){
		int[][] newtab = new int [Npop][Nville];
		double[] fit = Fitness(tab);
		
		double Sfit = 0;
		for(int i=0; i<Npop; i++){
			Sfit = Sfit + fit[i];
		}
		Random random = new Random();
		
		for (int j=0; j<Npop; j++){
			double SPfit=fit[0];
			double y = random.nextFloat();
			int k=0;
			while (SPfit/Sfit<y){
				k=k+1;
				SPfit = SPfit + fit[k];
			}
			newtab[j]=tab[k];
		}
		return(newtab);
	}
	
// Différents méthodes de croisement :
	
	public void CroisementLOX (int[][] select, int parent1, int parent2){
		ArrayList<Integer> Parent1 = new ArrayList<Integer>();
		ArrayList<Integer> Parent2= new ArrayList<Integer>();
		for(int k=0; k<Nville; k++) {
			Parent1.add(select[parent1][k]);
			Parent2.add(select[parent2][k]);
		}
		Random random = new Random();
		int x = random.nextInt(Nville-1);
		int y = x+random.nextInt(Nville-x-1);
		int[] enfant1 = new int[Nville];
		int[] enfant2 = new int[Nville];
		int m=0;
		int n=0;
		for(int i=x; i<y+1; i++) {
			enfant1[i]=Parent2.get(i);
			enfant2[i]=Parent1.get(i);
		}
		for(int p=x; p<y+1; p++) {
			m = Parent1.indexOf(enfant1[p]);
			n = Parent2.indexOf(enfant2[p]);
			Parent1.remove(m);
			Parent2.remove(n);
		}
		for(int j=0; j<Parent1.size(); j++) {
			if(j<x) {
				enfant1[j]=Parent1.get(j);
				enfant2[j]=Parent2.get(j);
			}
			else {
				enfant1[y+j-x+1]=Parent1.get(j);
				enfant2[y+j-x+1]=Parent2.get(j);
			}
		}
		select[parent1]=enfant1;
		select[parent2]=enfant2;
	}
	
	public void CroisementOX (int[][] select, int parent1, int parent2) {
		ArrayList<Integer> Parent1 = new ArrayList<Integer>();
		ArrayList<Integer> Parent2= new ArrayList<Integer>();
		for(int k=0; k<Nville; k++) {
			Parent1.add(select[parent1][k]);
			Parent2.add(select[parent2][k]);
		}
		Random random = new Random();
		int x = random.nextInt(Nville-1);
		int y = x+random.nextInt(Nville-x-1);
		int[] enfant1 = new int[Nville];
		int[] enfant2 = new int[Nville];
		int m=0;
		int n=0;
		for(int i=x; i<y+1; i++) {
			enfant1[i]=Parent2.get(i);
			enfant2[i]=Parent1.get(i);
		}
		for(int p=x; p<y+1; p++) {
			m = Parent1.indexOf(enfant1[p]);
			n = Parent2.indexOf(enfant2[p]);
			Parent1.remove(m);
			Parent2.remove(n);
		}
		for(int j=0; j<Parent1.size(); j++) {
			enfant1[(y+j)%Nville]=Parent1.get(j);
			enfant2[(y+j)%Nville]=Parent2.get(j);;
		}
	}
	
	public void CroisementCX (int[][] select, int parent1, int parent2) {
		ArrayList<Integer> Parent1 = new ArrayList<Integer>();
		ArrayList<Integer> Parent2= new ArrayList<Integer>();
		for(int k=0; k<Nville; k++) {
			Parent1.add(select[parent1][k]);
			Parent2.add(select[parent2][k]);
		}
		int[] enfant1 = new int[Nville];
		int[] enfant2 = new int[Nville];
		Random random = new Random();
		ArrayList<Integer> Aux = new ArrayList<Integer>();
		int x = random.nextInt(Nville);
		int l = Parent2.indexOf(Parent1.get(x));
		Aux.add(l);
		enfant1[l]=Parent1.get(l);
		enfant2[l]=Parent2.get(l);
		while (l!=x) {
			l=Parent2.indexOf(Parent1.get(l));
			enfant1[l]=Parent1.get(l);
			enfant2[l]=Parent2.get(l);
			Aux.add(l);
		}
		for (int i=0; i<Nville; i++) {
			if (Aux.indexOf(i)==-1) {
				enfant1[i]=Parent2.get(i);
				enfant2[i]=Parent1.get(i);
			}
		}
		
		select[parent1]=enfant1;
		select[parent2]=enfant2;
		
	}
	
// Différents méthodes de mutation :

	public void ExchangeMutation (int[][] select, int i) {
		Random random = new Random();
		int x = random.nextInt(Nville);
		int y = x+random.nextInt(Nville-x);
		int support = select[i][x];
		select[i][x]=select[i][y];
		select[i][y]=support;
	}
	
	public void InversionMutation (int[][] select, int i) {
		Random random = new Random();
		int x = random.nextInt(Nville);
		int y = x+random.nextInt(Nville-x);
		for(int j=0; j<Math.floor((y-x)/2); j++) {
			int support = select[i][x+j];
			select[i][x+j]=select[i][y-j];
			select[i][y-j]=support;
		}
	}
		
	public void InsertionMutation (int[][] select, int i) {
		Random random = new Random();
		int x = random.nextInt(Nville);
		int y = random.nextInt(Nville);
		ArrayList<Integer> Mutation = new ArrayList<Integer>();
		for(int j=0; j<Nville; j++) {
			Mutation.add(select[i][j]);
		}
		int support = Mutation.get(x);
		Mutation.remove(Mutation.indexOf(support));
		Mutation.add(y,support);
		for(int k=0; k<Nville; k++) {
			select[i][k]=Mutation.get(k);
		}
	}
	
	public void AlgoGenetique (int Niter) {
		Random random = new Random();
		for(int k=0; k<Niter; k++) {								// boucle sur le nombre d'itération
			pop= SelectionDeterministe(pop);						// sélection et tri des individus de la population (tri ordre croissant de fitness)
			score.add(fitness(pop[0]));
			for(int i=Nselection; i<((Npop+Nselection)/2); i++) {	// On conserve les Nselection individus les plus adaptés et on effectue une un croisement avec une probabilité Pcross
				double x = random.nextFloat();
				if(x<Pcross) {
					CroisementCX(pop, i, Npop+Nselection-(i+1));	// Croisements disponibles : CroisementLOX, CroisementOX, CroisementCX
				}
			}
			for(int j=Nselection; j<Npop; j++) {					// On conserve de la même manière les individus les plus adaptés et on effectue une mutation avec une probabilité Pmut
				double y = random.nextFloat();
				if(y<Pmut) {
					ExchangeMutation(pop, j);						// Mutations disponibles : ExchangeMutation, InversionMutation, InsertionMutation.
				}
			}
			
		}
		SelectionDeterministe(pop);									// cette sélection ne sert pas à sélectionner mais plus à trier la population en fonction du fitness.
	}	

}
