package Agents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.Arrays;

import Optimisation.Carte;
import Optimisation.Genetique;

public class AgentGenetique2 extends Agent{
	Genetique Genetique;
	int nbMessage = 1;

	public int getNbMessage() {
		return nbMessage;
	}


	public void setNbMessage(int nbMessage) {
		this.nbMessage = nbMessage;
	}


	public Genetique getGenetique() {
		return Genetique;
	}


	public void setup(){
		Object []arguments = getArguments();
		Carte carte =(Carte)arguments[0];
		int cas =(int)arguments[1];
		int j = (int) arguments[2];
		
		Genetique = new Genetique(carte);
		
		
		
		switch (cas){
		case 1: //comportement linéaire
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					
					//cas 1 comportement lin�aire
					try{
						//cas 1 comportement lin�aire
						ACLMessage msg = myAgent.receive();
						if(msg != null){
							System.out.println(" Agent Genetique");
							System.out.println("Valeur Initiale" + Genetique.fitness(Genetique.pop[0]));
							Genetique.AlgoGenetique(1024);
							System.out.println("Valeur finale" + Genetique.fitness(Genetique.pop[0]));
							ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
							notif.setContentObject(Genetique.pop[0]);
							notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
							System.out.println("Message envoy� � l'agent recuit");
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
							int i = ((AgentGenetique2)myAgent).getNbMessage();
							//System.out.println(i);
							if ( i> j*10-1){
								Genetique.AlgoGenetique(1024);
								//System.out.println("Valeur finale" + Genetique.fitness(Genetique.pop[0]));
								ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
								notif.setContentObject(Genetique.pop[0]);
								notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
								//System.out.println("Message envoy� � l'agent tabou");
								myAgent.send(notif);
								
								myAgent.doDelete();
							}
							else{
								System.out.println("Message re�u"+i);
								Genetique.pop[i-1] = (int[]) msg.getContentObject();
								((AgentGenetique2)myAgent).setNbMessage(i+1);
							}
							
						
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
					//cas 3 recherche simultan�e
						ACLMessage msg = myAgent.receive();
						if(msg != null ){
							int i = ((AgentGenetique2)myAgent).getNbMessage();
							if (i == 1){
								if(!msg.getContent().equals("rien")){
								int[] sol =(int[]) msg.getContentObject();
								float val =(float) Genetique.fitness(sol);
								if (val < Genetique.fitness(Genetique.pop[0])){
									Genetique.pop[0] = sol;
								}
								((AgentGenetique2)myAgent).setNbMessage(i+1);
								}
								
								System.out.println("Genetique " + Genetique.fitness(Genetique.pop[0]));
								Genetique.AlgoGenetique(1024);
								ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
								notif.setContentObject(Genetique.pop[0]);
								notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
								notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
								System.out.println("Message envoy� � l'agent tabou et au recuit");
								myAgent.send(notif);
								((AgentGenetique2)myAgent).setNbMessage(0);
							}
							else{
								int[] sol =(int[]) msg.getContentObject();
								float val =(float) Genetique.fitness(sol);
								if (val < Genetique.fitness(Genetique.pop[0])){
									Genetique.pop[0] = sol;
								}
								((AgentGenetique2)myAgent).setNbMessage(i+1);
							}
							
						}else block();
					}
					catch(Exception e){
						
					}
				}
				});
			break;
		
		}
		
		addBehaviour(new CyclicBehaviour(){
							@Override
							public void action() {
								//cas 1 comportement lin�aire
								
								
								//cas 2 R-S -> G -> T
								
								
								//cas 3 recherche simultan�e
								
							}
						});
		
	}
}