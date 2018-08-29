import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import Optimisation.AlgoGen;
import Optimisation.Carte;
import Optimisation.RecuitSimule;
import Optimisation.TabouSearch;
import OptimisationGenetique.Main;
import Optimisation.PVCtabou;
import jade.core.AID;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class main {
	static Random rnd;

	public static void main(String[] args) {
		rnd = new Random(40);
		// TODO Auto-generated method stub
		System.out.println("Entrez votre choix");
		System.out.println("1 : Problï¿½me de grande taille");
		System.out.println("2 : Recuit - Gï¿½nï¿½tique - Tabou");
		System.out.println("3 : Gï¿½nï¿½tique - Recuit - Tabou");
		System.out.println("4 : Cas simultanï¿½");
		System.out.println("5 : Cas simultanï¿½ PVC ");
		System.out.println("6 : Recuit - Gï¿½nï¿½tique - Tabou ");
		Scanner sc = new Scanner(System.in);
		try{
		 int entree = sc.nextInt();
		
		
		
		switch(entree){
			case 1:
				problemeDeGrandeTaille(150);
				break;
			case 2:
			//	lanceJadeCasRGT();
				break;
			case 3:
				lanceJadeCasLin(70);
				break;
			case 4:
				lanceJadeCasSimutane(70);
				break;
			case 5:
				lanceJadeCasSimutane2();
				break;
			case 6:
				lanceJadeCasRGT2();
				break;
			default:
				System.out.println("Mauvaise entrï¿½e --> Fin");
				break;
			}
		}
		catch(Exception e){
			System.out.println("Pas un nombre --> Fin");
		}
		
		
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Permet de gï¿½nï¿½rer et rï¿½soudre des problï¿½me de grande taille
	static private void problemeDeGrandeTaille(int n){
		int[] villes = new int[n];
		double[] longitudes = new double[n];
		double[] latitudes = new double[n];
		for (int i = 0 ;i<n;i++){
			longitudes[i]= rnd.nextDouble()*360-180;  //on tire alï¿½atoirement une longitude
			
			latitudes[i]=rnd.nextDouble()*180-90;  //on tire alï¿½atoirement une lattitude
			villes[i] =i;    //on ajoute la ville via le codage ASCII
		}
		
		Carte carte =new Carte(latitudes,longitudes,villes);
		
		
		AlgoGen gen =new AlgoGen(carte);  //on gï¿½nï¿½re l'algorithme
		System.out.println("Algorithme gï¿½nï¿½tique");
		gen.plusieursGen(2000);     //on rï¿½sout le problï¿½me
		System.out.println(gen.popFit[0]);
		
		TabouSearch tabou =new TabouSearch(carte);  //on gï¿½nï¿½re l'algorithme
		System.out.println("Recherche Tabou");
		tabou.arretSiPasAmelioration(100,0);     //on rï¿½sout le problï¿½me
		System.out.println(tabou.getValeurSolution());
		
		PVCtabou tabou2 =new PVCtabou(carte);  //on gï¿½nï¿½re l'algorithme
		System.out.println("Recherche PVCtabou");
		tabou2.fonctionPrincipale();     //on rï¿½sout le problï¿½me
	    System.out.println(tabou2.poid(tabou2.getSolution()));
		
		RecuitSimule recuit = new RecuitSimule(carte);
		System.out.println("Recuit Simulï¿½");
		recuit.recuit();
		System.out.println(recuit.getSolution());
		
		
	}
	
	

	
	/*static private void lanceJadeCasRGT(){
		double[][] Res = new double[20][20];
		
		Runtime rt = Runtime.instance();
		ProfileImpl pMain = new ProfileImpl( null, 2032, "PVC");
		AgentContainer mc = rt.createMainContainer(pMain);
		AgentController rma;
		AgentController genetique;
		AgentController recuit;
		AgentController tabou;
		AgentController lanceur;
		
		try {
			
			rma = mc.createNewAgent( "rma", "jade.tools.rma.rma", null);
			rma.start(); //lance la plateforme
			
			for(int j=1; j<21; j++) {
				for(int n =1; n<21; n++) {
			
			int[] villes = new int[n*10];
			double[] longitudes = new double[n*10];
			double[] latitudes = new double[n*10];
			for (int i = 0 ;i<n;i++){
				longitudes[i]=rnd.nextDouble()*360-180;  //on tire alï¿½atoirement une longitude
				
				latitudes[i]=rnd.nextDouble()*180-90;  //on tire alï¿½atoirement une lattitude
				villes[i] =i;    //on ajoute la ville via le codage ASCII
			}
			Carte carte = new Carte(latitudes,longitudes,villes);
			Object[] arguments = new Object[5];
			arguments[0] = carte;
			arguments[1] = 2;
			arguments[2] = j;
			arguments[3] = n;
			arguments[4] = Res;
			
			
			genetique = mc.createNewAgent( "genetique", "Agents.AgentsGenetique", arguments);
		
			for (int i = 0; i < j*10; i++){
				AgentController recuits = mc.createNewAgent( "recuit" + i, "Agents.AgentRecuit", arguments);
				recuits.start();
			}
			
			Object[] argchef =new Object[2];
			argchef[0] = 2;
			argchef[1] = j;
			AgentController chef = mc.createNewAgent( "chef", "Agents.Chef", argchef);
			chef.start();

			tabou = mc.createNewAgent("tabou", "Agents.AgentTabou", arguments);
			genetique.start();
			
			tabou.start();
			
			lanceur = mc.createNewAgent("lanceur", "Agents.AgentLanceur", null);
			lanceur.start();
			
			/*Attendez d'obtenir le résultat final !*/
			/*true : on peut continuer 
			
			Scanner sc1 = new Scanner(System.in);
			int entry = sc1.nextInt();
				}
			}
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // on crï¿½e l'agent qui permet l'interraction // (nom, addresse, parametre) ici la classe de l'agent est dï¿½ja crï¿½ï¿½e
		
		
		/************************************************
		 * Ecrire le fichier texte contenant le résultat
		 ************************************************/
		
	/*	System.out.println(Arrays.deepToString(Res));
		
		try {
			PrintWriter writer = new PrintWriter("resultatRGT.txt", "UTF-8");
			writer.println(Arrays.deepToString(Res));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	static private void lanceJadeCasRGT2(){
		double[][] Res = new double[20][20];
				
		Runtime rt = Runtime.instance();
		ProfileImpl pMain = new ProfileImpl( null, 2032, "PVC");
		AgentContainer mc = rt.createMainContainer(pMain);
		AgentController rma;
		AgentController genetique;
		AgentController recuit;
		AgentController tabou;
		AgentController lanceur;
		
		try {
			
			rma = mc.createNewAgent( "rma", "jade.tools.rma.rma", null);
			rma.start(); //lance la plateforme
			
			for(int j=1; j<21; j++) {
				for(int n =1; n<21; n++) {
					
			int[] villes = new int[n*10];
			double[] longitudes = new double[n*10];
			double[] latitudes = new double[n*10];
			for (int i = 0 ;i<n*10;i++){
				longitudes[i]=rnd.nextDouble()*360-180;  //on tire alï¿½atoirement une longitude
				
				latitudes[i]=rnd.nextDouble()*180-90;  //on tire alï¿½atoirement une lattitude
				villes[i] =i;    //on ajoute la ville via le codage ASCII
			}
			Carte carte = new Carte(latitudes,longitudes,villes);
			Object[] arguments = new Object[5];
			arguments[0] = carte;
			arguments[1] = 2;
			arguments[2] = j;
			arguments[3] = n;
			arguments[4] = Res;			
			
			genetique = mc.createNewAgent( "genetique", "Agents.AgentGenetique2", arguments);		//modifier agent Genetique pour tester les différents algos
		
			for (int i = 0; i < j*10; i++){
				AgentController recuits = mc.createNewAgent( "recuit" + i, "Agents.AgentRecuit", arguments);
				recuits.start();
			}
			
			Object[] argchef =new Object[2];
			argchef[0] = 2;
			argchef[1] = j;
			AgentController chef = mc.createNewAgent( "chef", "Agents.Chef", argchef);
			chef.start();

			tabou = mc.createNewAgent("tabou", "Agents.AgentTabou", arguments);			//modifier agent Tabou pour tester les différents algos
			genetique.start();
			
			tabou.start();
			
			lanceur = mc.createNewAgent("lanceur", "Agents.AgentLanceur", null);
			lanceur.start();
			
			/*Attendez d'obtenir le résultat final !*/
			/*int : on peut continuer */
			
			Scanner sc1 = new Scanner(System.in);
			int entry = sc1.nextInt();
				}
			}	
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // on crï¿½e l'agent qui permet l'interraction // (nom, addresse, parametre) ici la classe de l'agnet est dï¿½ja crï¿½ï¿½e

		/************************************************
		 * Ecrire le fichier texte contenant le résultat
		 ************************************************/
		
		System.out.println(Arrays.deepToString(Res));
		
		try {
			PrintWriter writer = new PrintWriter("resultatRGT2.txt", "UTF-8");
			writer.println(Arrays.deepToString(Res));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static private void lanceJadeCasLin(int n){
		Runtime rt = Runtime.instance();
		ProfileImpl pMain = new ProfileImpl( null, 2032, "PVC");
		AgentContainer mc = rt.createMainContainer(pMain);
		AgentController rma;
		AgentController genetique;
		AgentController recuit;
		AgentController tabou;
		
		try {
			
			rma = mc.createNewAgent( "rma", "jade.tools.rma.rma", null);
			rma.start(); //lance la plateforme
			
			int[] villes = new int[n];
			double[] longitudes = new double[n];
			double[] latitudes = new double[n];
			for (int i = 0 ;i<n;i++){
				longitudes[i]=rnd.nextDouble()*360-180;  //on tire alï¿½atoirement une longitude
				
				latitudes[i]=rnd.nextDouble()*180-90;  //on tire alï¿½atoirement une lattitude
				villes[i]=i;    //on ajoute la ville via le codage ASCII
			}
			Carte carte = new Carte(latitudes,longitudes,villes);
			Object arguments[] = new Object[2];
			arguments[0] = carte;
			arguments[1] = 1;
			genetique = mc.createNewAgent( "genetique", "Agents.AgentsGenetique", arguments);
			recuit = mc.createNewAgent( "recuit", "Agents.AgentRecuit", arguments);
			
			

			tabou = mc.createNewAgent("tabou", "Agents.AgentTabou", arguments);
			genetique.start();
			recuit.start();
			tabou.start();
			
			
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // on crï¿½e l'agent qui permet l'interraction // (nom, addresse, parametre) ici la classe de l'agnet est dï¿½ja crï¿½ï¿½e
		
	}
	
	
	static private void lanceJadeCasSimutane(int n){
		Runtime rt = Runtime.instance();
		ProfileImpl pMain = new ProfileImpl( null, 2032, "PVC");
		AgentContainer mc = rt.createMainContainer(pMain);
		AgentController rma;
		AgentController genetique;
		AgentController recuit;
		AgentController tabou;
		AgentController chef;
		
		try {
			
			rma = mc.createNewAgent( "rma", "jade.tools.rma.rma", null);
			rma.start(); //lance la plateforme
			
			int[] villes = new int[n];
			double[] longitudes = new double[n];
			double[] latitudes = new double[n];
			for (int i = 0 ;i<n;i++){
				longitudes[i]=rnd.nextDouble()*360-180;  //on tire alï¿½atoirement une longitude
				
				latitudes[i]=rnd.nextDouble()*180-90;  //on tire alï¿½atoirement une lattitude
				villes[i]=i;    //on ajoute la ville via le codage ASCII
			}
			Carte carte = new Carte(latitudes,longitudes,villes);
			Object arguments[] = new Object[2];
			arguments[0] = carte;
			arguments[1] = 3;
			
			genetique = mc.createNewAgent( "genetique", "Agents.AgentsGenetique", arguments);
			recuit = mc.createNewAgent( "recuit", "Agents.AgentRecuit", arguments);
			
			

			tabou = mc.createNewAgent("tabou", "Agents.AgentTabou", arguments);
			
			Object argchef[] =new Object[1];
			argchef[0] = 3;
			chef = mc.createNewAgent("chef", "Agents.Chef", argchef);
			genetique.start();
			recuit.start();
			tabou.start();
			chef.start();
			
			
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // on crï¿½e l'agent qui permet l'interraction // (nom, addresse, parametre) ici la classe de l'agnet est dï¿½ja crï¿½ï¿½e
		
	}
	
	static private void lanceJadeCasSimutane2(){
		int n = 100;
		Runtime rt = Runtime.instance();
		ProfileImpl pMain = new ProfileImpl( null, 2032, "PVC");
		AgentContainer mc = rt.createMainContainer(pMain);
		AgentController rma;
		AgentController genetique;
		AgentController recuit;
		AgentController tabou;
		AgentController chef;
		
		try {
			
			rma = mc.createNewAgent( "rma", "jade.tools.rma.rma", null);
			rma.start(); //lance la plateforme
			
			int[] villes = new int[n];
			double[] longitudes = new double[n];
			double[] latitudes = new double[n];
			for (int i = 0 ;i<n;i++){
				longitudes[i]=rnd.nextDouble()*360-180;  //on tire alï¿½atoirement une longitude
				
				latitudes[i]=rnd.nextDouble()*180-90;  //on tire alï¿½atoirement une lattitude
				villes[i]=i;    //on ajoute la ville via le codage ASCII
			}
			Carte carte = new Carte(latitudes,longitudes,villes);
			Object arguments[] = new Object[2];
			arguments[0] = carte;
			arguments[1] = 3;
			
			genetique = mc.createNewAgent( "genetique", "Agents.AgentsGenetique", arguments);
			recuit = mc.createNewAgent( "recuit", "Agents.AgentRecuit", arguments);
			
			

			tabou = mc.createNewAgent("tabou", "Agents.AgentTabou2", arguments);
			
			Object argchef[] =new Object[1];
			argchef[0] = 3;
			chef = mc.createNewAgent("chef", "Agents.Chef", argchef);
			genetique.start();
			recuit.start();
			tabou.start();
			chef.start();
			
			
			
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // on crï¿½e l'agent qui permet l'interraction // (nom, addresse, parametre) ici la classe de l'agnet est dï¿½ja crï¿½ï¿½e
		
	}
	
}

