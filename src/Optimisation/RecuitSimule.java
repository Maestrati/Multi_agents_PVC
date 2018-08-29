package Optimisation;

import java.util.Arrays;

public class RecuitSimule {
	static private float temperatureInit = 1000;
	static private float temperatureMin = 10;
	static private float coeffRefroidissement =(float) 0.99;
	static private int nbIterations =70;
	static private float constanteBoltzman = 5;
	
	
	private float temperature;
	private int[] solution;
	private float energieSolution;
	
	private Carte carte;
	private int nbVilles;
	
	
	
	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] solution) {
		this.solution = solution;
	}
	

	public float getEnergieSolution() {
		return energieSolution;
	}

	public RecuitSimule(float[][] dist,int[] villes){
		carte = new Carte(dist,villes);
		nbVilles = carte.nbVilles;
		solution = Arrays.copyOf(carte.getListVilles(), nbVilles);
		genereSol();
		energieSolution = energie(solution);
		temperature = temperatureInit;
	}
	
	public RecuitSimule(double lat[],double lon[], int[] villes){
		carte = new Carte(lat,lon, villes);
		nbVilles = carte.nbVilles;
		solution = Arrays.copyOf(carte.getListVilles(), nbVilles);
		genereSol();
		energieSolution = energie(solution);
		temperature = temperatureInit;
	}
	
	public RecuitSimule(){
		carte = new Carte();
		nbVilles = carte.nbVilles;
		solution = Arrays.copyOf(carte.getListVilles(), nbVilles);
		genereSol();
		energieSolution = energie(solution);
		temperature = temperatureInit;
	}
	public RecuitSimule(Carte carte){
		this.carte = carte;
		nbVilles = carte.nbVilles;
		solution = Arrays.copyOf(carte.getListVilles(), nbVilles);
		genereSol();
		energieSolution = energie(solution);
		temperature = temperatureInit;
	}
	
	public void genereSol(){
		for (int j = 0; j <60; j++ ){     // chaque individus subit des permutations aléatoirement
			int a =(int)(Math.random()*nbVilles);
			int b = (int)(Math.random()*nbVilles);
			solution = permut(a ,b,Arrays.copyOf(solution, nbVilles));  //permutation
		}
		//on admet que la population générée est suffisament aléatoire
	}
	// Permet de calculer l'énergie d'une solution
	public float energie(int[] solution){
		float res = 0;
		for (int i=0; i < nbVilles; i++) //on parcourt la chaine de caractère
			res += carte.distance(solution[i], solution[(i+1) % nbVilles]);
		//on additionne toute les distances intermédiaires
		// Rq. l'utilisation du modulo permet de fermet le cycle. 
		return res;
	}
	
	public void updateEnergie(){
		energieSolution =energie(solution);
	}
	
	public int[] permut(int i,int j, int[] solution){
		
		int support = solution[i];
		solution[i]=solution[j];
		solution[j]=support;
		
		return solution;
	}
	
	public void refroidissement(){
		temperature = coeffRefroidissement*temperature;
	}
	
	public void rechauffer(){
		temperature = temperatureInit;
	}
	
	public void uneIteration(){
		//On genere une solution aleatoire
		int a =(int)(Math.random()*nbVilles);
		int b = (int)(Math.random()*nbVilles);
		int[] voisin = permut(a ,b,Arrays.copyOf(solution, nbVilles));
		float energieVoisin = energie(voisin);
		if(energieVoisin<energieSolution){
			solution = Arrays.copyOf(voisin, nbVilles);
			energieSolution = energieVoisin;
		}
		else{
			float proba = (float)Math.exp(-(energieVoisin-energieSolution)/(temperature*constanteBoltzman));
			//System.out.println(proba);
			if (Math.random()<proba){
				solution = Arrays.copyOf(voisin, nbVilles);
				energieSolution = energieVoisin;
			}
		}
	}
	
	public void recuit(){
		rechauffer();
		while(temperature>temperatureMin){
			for(int i = 0; i<nbIterations;i++){
				uneIteration();
				//System.out.println(energieSolution);
			}
			refroidissement();
			//System.out.println("Temperature " + temperature);
		}
		//System.out.println(energieSolution);
		//System.out.println(solution);
		
	}
			
	

}
