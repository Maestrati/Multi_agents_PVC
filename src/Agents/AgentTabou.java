package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.Arrays;

import Optimisation.Carte;
import Optimisation.TabouSearch;

public class AgentTabou extends Agent{
	TabouSearch tabou;
	int nbMessage = 1;

	public TabouSearch getTabou() {
		return tabou;
	}
	

	public int getNbMessage() {
		return nbMessage;
	}


	public void setNbMessage(int nbMessage) {
		this.nbMessage = nbMessage;
	}


	public void setup(){
		Object []arguments = getArguments();
		Carte carte =(Carte)arguments[0];
		int cas =(int)arguments[1];
		int j = (int) arguments[2];
		int n = (int) arguments[3];
		double[][] Res = (double[][]) arguments[4];
		
		tabou = new TabouSearch(carte);
		
		switch (cas){
		case 1:
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					
					//cas 1 comportement linéaire
					try{
						//cas 1 comportement linéaire
						ACLMessage msg = myAgent.receive();
						if(msg != null){
							System.out.println("Agent Tabout");
							int[] villes =(int[]) msg.getContentObject();
							tabou.setCourante(villes);
							tabou.setSolution(villes);
							tabou.updateValeurs();
							System.out.println("Valeur Initiale" + tabou.getValeurSolution());
							getTabou().arretSiPasAmelioration(60,0);
							System.out.println("Valeur Finale" + tabou.getValeurSolution());
							ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
							notif.setContentObject(tabou.getSolution());
							notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
							System.out.println("Message envoyé à l'agent recuit");
							myAgent.send(notif);
						} else block();
					}
					catch(Exception e){}
					}});
			break;
		case 2:
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					//cas 2 R-S -> G -> T
					try{
						ACLMessage msg = myAgent.receive();
						if(msg != null){
							//System.out.println("Agent Tabou");
							int[] villes =(int[]) msg.getContentObject();
							tabou.setCourante(villes);
							tabou.setSolution(villes);
							tabou.updateValeurs();
							//System.out.println("Valeur Initiale" + tabou.getValeurSolution());
							getTabou().arretSiPasAmelioration(60,0);
							System.out.println("Valeur Finale" + tabou.getValeurSolution());
							Res[j-1][n-1] = tabou.getValeurSolution();
							
							myAgent.doDelete();
						} else block();
						
					
					
					}
					catch(Exception e){}
				}});
			break;
			
		case 3:
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					try{
					//cas 3 recherche simultanée
						ACLMessage msg = myAgent.receive();
						if(msg != null){
							int i = ((AgentTabou)myAgent).getNbMessage();
							if (i == 1){
								if(!msg.getContent().equals("rien")){
								System.out.println(msg.getContent());
								int[] sol = new int [tabou.getNbVilles()];
								try {
									sol = (int[]) msg.getContentObject();
								} catch (UnreadableException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								float val = getTabou().valeur(sol);
								if (val < getTabou().getValeurSolution()){
									getTabou().setSolution(sol);
									getTabou().setCourante(sol);
									getTabou().updateValeurs();
								}
								}
								((AgentTabou)myAgent).setNbMessage(i+1);
								System.out.println("Tabou " + getTabou().getValeurSolution());
								getTabou().arretSiPasAmelioration(60,0);;
								ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
								notif.setContentObject(getTabou().getSolution());
								notif.addReceiver(new AID("genetique",AID.ISLOCALNAME));
								notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
								System.out.println("Message envoyé à l'agent génétique et au recuit");
								myAgent.send(notif);
								((AgentTabou)myAgent).setNbMessage(0);
							}
							else{
								int[] sol =(int[]) msg.getContentObject();
								float val = getTabou().valeur(sol);
								if (val < getTabou().getValeurSolution()){
									getTabou().setSolution(sol);
									getTabou().setCourante(sol);
									getTabou().updateValeurs();
								}
								((AgentTabou)myAgent).setNbMessage(i+1);
							}
							
						}else block();
					}
					catch(Exception e){
						
					}
				}
				});
			break;
		
		}
		
		
		
		
	}
}

