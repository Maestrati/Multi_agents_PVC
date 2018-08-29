package Agents;

import Optimisation.Carte;
import Optimisation.PVCtabou;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentTabou2  extends Agent{
	PVCtabou tabou;
	int nbMessage = 1;

	public PVCtabou getTabou() {
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
		int j =(int) arguments[2];
		int n = (int) arguments[3];
		double[][] Res = (double[][]) arguments[4];
		
		tabou = new PVCtabou(carte);
	
		tabou.initChemin();
		
		switch (cas){
		case 1:
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					
					//cas 1 comportement linéaire
					try{
						ACLMessage msg = myAgent.receive();
						if(msg != null){
							System.out.println("Agent Tabout");
							int[] villes =(int[]) msg.getContentObject();
							tabou.setSolution(villes);
							System.out.println("Valeur Initiale" + tabou.poid(tabou.getSolution()));
							getTabou().fonctionPrincipale();
							System.out.println("Valeur Finale" + tabou.poid(tabou.getSolution()));
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
							int[] villes = new int[tabou.Nville];
							try {
								villes = (int[]) msg.getContentObject();
							} catch (UnreadableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//traduction 
							
							tabou.setSolution(villes);
							//System.out.println("Valeur Initiale" + getTabou().poid(getTabou().getSolution()));
							getTabou().fonctionPrincipale();
							System.out.println("Valeur Finale" + getTabou().poid(getTabou().getSolution()));
							Res[j-1][n-1] = getTabou().poid(getTabou().getSolution());
							
							myAgent.doDelete();
							
						} else block();
						
					
					
					}
					catch(Exception e){
					e.printStackTrace();
					}
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
							int i = ((AgentTabou2)myAgent).getNbMessage();
							if (i == 1){
								if(!msg.getContent().equals("rien")){
								System.out.println(msg.getContent());
								int[] sol =(int[]) msg.getContentObject();
								//traduction 
								
								float val =(float) getTabou().poid(sol);
								if (val < getTabou().poid(getTabou().getSolution())){
									getTabou().setSolution(sol);
									
								}
								}
								((AgentTabou2)myAgent).setNbMessage(i+1);
								System.out.println("Tabou " +  getTabou().poid(getTabou().getSolution()));
								getTabou().fonctionPrincipale();
								ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
								//traduction 
								
								notif.setContentObject(getTabou().getSolution());
								notif.addReceiver(new AID("genetique",AID.ISLOCALNAME));
								notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
								System.out.println("Message envoyé à l'agent génétique et au recuit");
								myAgent.send(notif);
								((AgentTabou2)myAgent).setNbMessage(0);
							}
							else{
								System.out.println(msg.getContent());
								int[] sol =(int[]) msg.getContentObject();
								//traduction 
								
								float val =(float) getTabou().poid(sol);
								if (val < getTabou().poid(getTabou().getSolution())){
									getTabou().setSolution(sol);
								}
								((AgentTabou2)myAgent).setNbMessage(i+1);
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



