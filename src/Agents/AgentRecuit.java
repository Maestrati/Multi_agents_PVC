package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

import Optimisation.Carte;
import Optimisation.RecuitSimule;

public class AgentRecuit extends Agent{
	RecuitSimule recuit;
	int nbMessage = 1;

	public RecuitSimule getRecuit() {
		return recuit;
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
		
		recuit = new RecuitSimule(carte);
		
		switch (cas){
		case 1:
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					
					//cas 1 comportement linéaire
					try{
						ACLMessage msg = myAgent.receive();
						if(msg != null){
							System.out.println("Agent recuit");
							int[] villes =(int[]) msg.getContentObject();
							recuit.setSolution(villes);
							recuit.updateEnergie();
							System.out.println("Solution Initiale " +recuit.getEnergieSolution());
							getRecuit().recuit();
							System.out.println("Solution Finale " + recuit.getEnergieSolution());
							ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
							notif.setContentObject(recuit.getSolution());
							notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
							System.out.println("Message envoyé à l'agent tabou");
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
							//System.out.println("Solution Initiale " +recuit.getEnergieSolution());
							getRecuit().recuit();
							//System.out.println("Solution Finale " + recuit.getEnergieSolution());
							ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
							notif.setContentObject(recuit.getSolution());
							notif.addReceiver(new AID("genetique",AID.ISLOCALNAME));
							//System.out.println("Message envoyé à l'agent genetique");
							myAgent.send(notif);
							
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
							int i = ((AgentRecuit)myAgent).getNbMessage();
							if (i == 1){
								if(!msg.getContent().equals("rien")){
								int[] sol =(int[]) msg.getContentObject();
								float val = getRecuit().energie(sol);
								if (val < getRecuit().getEnergieSolution()){
									getRecuit().setSolution(sol);
									getRecuit().updateEnergie();
								}
								}
								System.out.println("Recuit " + getRecuit().getEnergieSolution());
								getRecuit().recuit();
								ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
								notif.setContentObject(getRecuit().getSolution());
								notif.addReceiver(new AID("genetique",AID.ISLOCALNAME));
								notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
								System.out.println("Message envoyé à l'agent génétique et au tabou");
								myAgent.send(notif);
								((AgentRecuit)myAgent).setNbMessage(0);
							}
							else{
								int[] sol =(int[]) msg.getContentObject();
								float val = getRecuit().energie(sol);
								if (val < getRecuit().getEnergieSolution()){
									getRecuit().setSolution(sol);
									getRecuit().updateEnergie();
								}
								((AgentRecuit)myAgent).setNbMessage(i+1);
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

