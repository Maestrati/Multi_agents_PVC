package Optimisation;
import java.lang.Math;
import java.util.ArrayList;

public class PVCtabou {
	int[] solution;
	
	public int Nville;
	
	public Carte carte;
	
	static double[][] distances;
	public ArrayList<Float> scores = new ArrayList<Float>(); 
	
	//On crée une matrice "tabou" de taille le nombre de villes par le nombre de villes. La taille de la liste
    // tabou correspondra au nombre d'entiers nons nuls de cette matrice (plutôt la moitié comme la matrice est symétrique)
    int[][] tabou; 
	
	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] solution) {
		this.solution = solution;
	}

	// Constructeur//
	
	public PVCtabou (float[][] dist, int[] villes) {
		carte = new Carte(dist, villes);
		Nville = carte.nbVilles;
		tabou = new int[distances.length][distances.length];
		
	}
	
	public PVCtabou (double lat[],double lon[], int[] villes) {
		carte = new Carte(lat, lon, villes);
		Nville = carte.nbVilles;
		distances = carte.distances;
		tabou = new int[distances.length][distances.length];
		
	}
	
	public PVCtabou (Carte carte) {
		this.carte = carte;
		Nville = carte.nbVilles;
		distances = carte.distances;
		tabou = new int[distances.length][distances.length];
		solution = new int[Nville];
		initChemin();
	}
	// On initialise le tableau des distances entre les villes
	/*static int[][] distances = 
		{{0,780,320,580,480,660},
		 {780, 0, 700,460,300,200},
		 {320,700,0,380,820, 630},
		 {580,460,380,0,750,310},
		 {480,300,820,750,0,500},
		 {660,200,630,310,500,0}}; */
	
 

	public static double poid(int chemin[]){ 
    	//Renvoie le poid (en km) d'un chemin. 
        double rep1 = 0;
   
        for(int i = 0 ; i < chemin.length-1; i++){
            rep1+= distances[chemin[i]][chemin[i+1]];
        }
        //On n'oublie pas la distance du retour
        return rep1+distances[chemin[chemin.length-1]][chemin[0]];
        
    }
    
    //Echange les indices de deux villes dans le chemin étudié
    public static int[] ichange(int ville1, int ville2, int[] chemin) {
        int aux = chemin[ville1];
        chemin[ville1] = chemin[ville2];
        chemin[ville2] = aux;
        return chemin;
    }
    
    
    
    //Rend le mouvement de l'échange des villes ville1 et ville2 tabou
    public void ModifTabou1(int ville1, int ville2, int t){ 
        tabou[ville1][ville2]= t;
        tabou[ville2][ville1]= t;
        
    }
    
    //Modifie les valeurs de la liste tabou: on bout de taille_tabou itérations, le mouvement n'est plus tabou
    public void ModifTabou2(){
        for(int i = 0; i<tabou.length; i++){
           for(int j = 0; j<tabou.length; j++){
            int tab = tabou[i][j];
            if (tab> 0) tabou[i][j] -= 1;
           }
         } 
     }
    
    
  
    //On cherche ici le plus petit voisin pas tabou de s ou tabou s'il minimise le coût le plus faible actuel (fonction d'aspiration)
    public int[] ppvoisin(int[][] tabou,int[] chemin, double pmin) {
    	
        int[] sm = new int[chemin.length]; 
        System.arraycopy(chemin, 0, sm, 0, sm.length); //Le meilleur chemin voisin de s est initialisé à lui-même
        double min = 100000;
        int ville1=0;
        int ville2=1;

        for (int i = 0; i < sm.length ; i++) {
            for (int j = 1; j < sm.length ; j++) {
                if (i == j) continue; //Le cas i==j n'est pas intéressant
                
                int[] voisin = new int[sm.length]; //La meilleure solution pour le moment
                System.arraycopy(sm, 0, voisin, 0, voisin.length);
                int[] aux = new int[sm.length];
                System.arraycopy(chemin, 0, aux, 0, voisin.length);
                voisin = ichange(i, j, aux); //On essaie d'échanger i et j pour voir si la solution est meilleure
               
                double poid = poid(voisin);

                //Si le nouveau poid est inférieur, on le garde
                if ((poid < min) && tabou[i][j] == 0) {
                	ville1 = i;
                	ville2 = j;
                    System.arraycopy(voisin, 0, sm, 0, voisin.length);
                    min = poid;
                }
                else if(poid<pmin) {// fonction d'aspiration : si le mouvement est interdit mais qu'il minimise le minimum global, on le garde
                	ville1 = i;
                	ville2 = j;
                    System.arraycopy(voisin, 0, sm, 0, voisin.length);
                    min = poid;
                    pmin=min;
                }
            }
        }
        
        //On met à jour la liste tabou
            ModifTabou2();
            ModifTabou1(ville1, ville2,taille_tabou);
        

        return sm;

    }

    static int taille_tabou;
    public void initChemin(){
    	solution = new int[Nville];   //On initialise le chemin au chemin trivial
        for (int h=0; h<Nville/2; h++) {
        	solution[2*h+1]=2*h;
        }
        for (int h=0; h<Nville/2; h++) {
        	solution[2*h]=2*h+1;
        } 
    }
    public void fonctionPrincipale(){
    	
        int[] s= solution;
        
        taille_tabou = 50; // On choisit la taille de la liste tabou
        int nbIterations = 500;
        
        int[] s1 = new int[s.length]; //meilleur chemin trouvé
        // On copie s dans un nouveau tableau, que l'on va modifier pour trouver son meilleur voisin
        System.arraycopy(s, 0, s1, 0, s1.length);
        double pmin = poid(s1); // poid minimal trouvé

        //A chaque itération, on choisit le meilleur voisin et on ajoute ce mouvement à  la liste tabou
        for (int i = 0; i < nbIterations; i++) { 
        	scores.add((float)poid(s));
            s = ppvoisin(tabou, s, pmin);
            double poid = poid(s);
            
            //On garde le meilleur poid et donc le chemin otpimal à cette étape
            if (poid < pmin) {
                System.arraycopy(s, 0, s1, 0, s1.length);
                pmin = poid;
            }
        }

        System.out.print("Le chemin fait "+pmin+" km. \n");
		for (int k=0; k<s1.length; k++) {
			System.out.print(s1[k]+" ");

		}
		solution = s1;
		

    }
    
    
    
}
