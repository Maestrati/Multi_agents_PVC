package Agents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Arrays;

import Optimisation.AlgoGen;
import Optimisation.Carte;

public class AgentsGenetique extends Agent{
	AlgoGen algoGen;
	int nbMessage = 1;

	public int getNbMessage() {
		return nbMessage;
	}


	public void setNbMessage(int nbMessage) {
		this.nbMessage = nbMessage;
	}


	public AlgoGen getAlgoGen() {
		return algoGen;
	}


	public void setup(){
		Object []arguments = getArguments();
		Carte carte =(Carte) arguments[0];
		int cas =(int)arguments[1];
		int j = (int) arguments[2];
		
		algoGen = new AlgoGen(carte);
		
		switch (cas){
		case 1:
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					
					//cas 1 comportement linéaire
					try{
					ACLMessage msg = myAgent.receive();
					if(msg != null){
						System.out.println(" Agent Genetique");
						System.out.println("Valeur Initiale" + algoGen.popFit[0]);
						getAlgoGen().plusieursGen(1000);
						System.out.println("Valeur finale" + algoGen.popFit[0]);
						ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
						notif.setContentObject(algoGen.pop[0]);
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
						int i = ((AgentsGenetique)myAgent).getNbMessage();
						//System.out.println(i);
						if ( i> j*10-1){
							getAlgoGen().calculFitPop();
							getAlgoGen().affiche();
							getAlgoGen().plusieursGen(1000);
							//System.out.println("Valeur finale" + algoGen.popFit[0]);
							ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
							notif.setContentObject(algoGen.pop[0]);
							notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
							//System.out.println("Message envoyé à l'agent tabou");
							myAgent.send(notif);
							
							myAgent.doDelete();
						}
						else{
							//System.out.println("Message reçu");
							getAlgoGen().pop[i-1] = (int[]) msg.getContentObject();
							((AgentsGenetique)myAgent).setNbMessage(i+1);
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
					//cas 3 recherche simultanée
					ACLMessage msg = myAgent.receive();
					if(msg != null ){
						int i = ((AgentsGenetique)myAgent).getNbMessage();
						if (i == 1){
							if(!msg.getContent().equals("rien")){
							int[] sol =(int[]) msg.getContentObject();
							float val = getAlgoGen().fitness(sol);
							if (val < getAlgoGen().popFit[0]){
								getAlgoGen().pop[0] = sol;
								getAlgoGen().calculFitPop();
							}
							((AgentsGenetique)myAgent).setNbMessage(i+1);
							}
							
							System.out.println("Genetique " + getAlgoGen().popFit[0]);
							getAlgoGen().plusieursGen(1000);
							ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
							notif.setContentObject(algoGen.pop[0]);
							notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
							notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
							System.out.println("Message envoyé à l'agent tabou et au recuit");
							myAgent.send(notif);
							((AgentsGenetique)myAgent).setNbMessage(0);
						}
						else{
							int[] sol =(int[]) msg.getContentObject();
							float val = getAlgoGen().fitness(sol);
							if (val < getAlgoGen().popFit[0]){
								getAlgoGen().pop[0] = sol;
								getAlgoGen().calculFitPop();
							}
							((AgentsGenetique)myAgent).setNbMessage(i+1);
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
