package Optimisation;
import java.util.ArrayList;
import java.util.Arrays;

public class TabouSearch {
	static public int tailleTabou = 75;
	
	private ArrayList listeTabou;
	private Carte carte;
	private int nbVilles;
	private int[] courante;
	private float valeurCourante;
	private int[] solution;
	private float valeurSolution;
	
	
	
	
	
	public int getNbVilles() {
		return nbVilles;
	}

	public ArrayList getListeTabou() {
		return listeTabou;
	}

	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] solution) {
		this.solution = solution;
	}

	public void setCourante(int[] courante) {
		this.courante = courante;
	}
	

	public float getValeurSolution() {
		return valeurSolution;
	}

	public TabouSearch(){
		carte = new Carte();
		nbVilles = carte.nbVilles;
		listeTabou = new ArrayList();
		courante = carte.getListVilles();
		genereSol();
		valeurCourante = valeur(courante);
		solution = courante;
		valeurSolution = valeurCourante;
	}
	
	public TabouSearch(float[][] dist,int[] villes){
		carte = new Carte(dist,villes);
		nbVilles = carte.nbVilles;
		listeTabou = new ArrayList();
		courante = carte.getListVilles();
		genereSol();
		valeurCourante = valeur(courante);
		solution = courante;
		valeurSolution = valeurCourante;
	}
	
	public TabouSearch(double lat[],double lon[], int[] villes){
		carte = new Carte(lat,lon, villes);
		nbVilles = carte.nbVilles;
		listeTabou = new ArrayList();
		courante = carte.getListVilles();
		genereSol();
		valeurCourante = valeur(courante);
		solution = courante;
		valeurSolution = valeurCourante;
	}
	
	public TabouSearch(Carte carte){
		this.carte = carte;
		nbVilles = carte.nbVilles;
		listeTabou = new ArrayList();
		courante = carte.getListVilles();
		genereSol();
		valeurCourante = valeur(courante);
		solution = courante;
		valeurSolution = valeurCourante;
	}
	
	
	
	
	
	// permet de générer une population aléatoirement
		public void genereSol(){
				for (int j = 0; j <60; j++ ){     // chaque individus subit des permutations aléatoirement
					int a =(int)(Math.random()*nbVilles);
					int b = (int)(Math.random()*nbVilles);
					courante = permut(a ,b,courante);  //permutation
				}
				//on admet que la population générée est suffisament aléatoire
		}
	
	
	
	
	
	// Permet de calculer la valeur d'une solution
		public float valeur(int[] solution){
			float res = 0;
			for (int i=0; i < nbVilles; i++) //on parcourt la chaine de caractère
				res += carte.distance(solution[i], solution[(i+1) % nbVilles]);
			//on additionne toute les distances intermédiaires
			// Rq. l'utilisation du modulo permet de fermet le cycle. 
			return res;
		}
		
		public void updateValeurs(){
			valeurCourante = valeur(courante);
			valeurSolution = valeur(solution);
		}
		
		public int[] permut(int i,int j, int[] solution){
			if (i>j){     // on fait en sorte que i<j
				int k = i;
				i = j;
				j = k;
			}
			if(i != j) {
				// on découpe la chaine de caratère pour la reformer dans l'ordre souhaité
				int support = solution[i];
			solution[i]=solution[j];
			solution[j]=support;
			}
			return solution;
		}
		
		public boolean isInTabou(int i,int j){
			boolean inTabou = false;
			int it = 0;
			int n = listeTabou.size();
			int m = listeTabou.size();
			if (tailleTabou < n)
				n = tailleTabou;
			while (it < n && ! inTabou){
				if (i == (int)((ArrayList)listeTabou.get(m-it-1)).get(0) && j == (int)((ArrayList)listeTabou.get(m-it-1)).get(1))
					inTabou = true;
				it++;
			
			}
			return inTabou;
			
		}
		
		public void prochaineIteration(){
			int permuti = 0;
			int permutj = 0;
			float valeurChoisie = 1000000000;
			for (int i = 0; i < nbVilles; i++)
				for(int j = 0; j < i; j++){
					int[] voisin = permut(i,j,Arrays.copyOf(courante, nbVilles));
					float valeur = valeur(voisin);
					boolean tabou = isInTabou(i,j);
					if (! tabou)
						if (valeur < valeurChoisie){
							permuti = i;
							permutj = j;
							valeurChoisie = valeur;
						}
					
					
					if (tabou && valeur < valeurSolution && valeur < valeurChoisie){    //aspiration
						
						permuti = i;
						permutj = j;
						valeurChoisie = valeur;
						
					}
					
				}
			//fin du parcourt des voisins
			courante = permut(permuti,permutj,Arrays.copyOf(courante, nbVilles));
			valeurCourante = valeur(courante);
			ArrayList<Integer> tabou = new ArrayList<Integer>();
			tabou.add(permuti);
			tabou.add(permutj);
			listeTabou.add(tabou);
			if (valeurCourante < valeurSolution){
				solution = courante;
				valeurSolution = valeurCourante;
			}
					
		}
		
		
		public void plusieursIterations(int n){
			for(int i = 0; i < n; i++){
				prochaineIteration();
				//System.out.println(valeurCourante);
			}
			//System.out.println(solution);
			//System.out.println(valeurSolution);
		}
		
		public void arretSiPasAmelioration(int n, float borneInf){
			int i = 0;
			float memoire = valeurSolution;
			while(i<n && memoire>borneInf){
				prochaineIteration();
				//System.out.println(valeurCourante);
				i++;
				if (memoire > valeurSolution){
					i = 0;
					memoire = valeurSolution;
				}
					
			}
			//System.out.println(solution);
			//System.out.println(valeurSolution);
		}

}
