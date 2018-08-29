package Agents;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import OptimisationGenetique.Main;
import java.util.Arrays;

import Optimisation.Carte;
import Optimisation.Genetique;

	public class AgentGenetique3 extends Agent{
		Main main;
		int nbMessage = 1;

		public int getNbMessage() {
			return nbMessage;
		}


		public void setNbMessage(int nbMessage) {
			this.nbMessage = nbMessage;
		}


		public Main getGenetique() {
			return main;
		}


		public void setup(){
			/*Object []arguments = getArguments();
			double[] latitudes =(double[])arguments[0];
			double[] longitudes =(double[])arguments[1];
			int[] villes = (int[]) arguments[2];*/
			Object []arguments = getArguments();
			Carte carte =(Carte)arguments[0];
			int cas =(int)arguments[1];
			//main = new Main(latitudes,longitudes,villes);
			main=new Main(carte);
			
			switch (cas){
			case 1:
			
			addBehaviour(new CyclicBehaviour(){
								@Override
								public void action() {
									
								try{
									//cas 1 comportement lin�aire
									ACLMessage msg = myAgent.receive();
									if(msg != null){
										System.out.println(" Agent Genetique 3");
										System.out.println("Valeur Initiale" + Main.longchemin(Main.meilleurchemin(main.population)));
										Main.factory("transposition", "LOX", "selectiondeterministe",1f,1f,1024,
												0, 10, 0);
										System.out.println("Valeur finale" + Main.longchemin(Main.meilleurchemin(main.population)));
										ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
										notif.setContentObject(Main.meilleurchemin(main.population));
										notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
										System.out.println("Message envoy� � l'agent recuit");
										myAgent.send(notif);
									} else block();
								}
								catch(Exception e){}
								}});
						break;
			            case 2:			
							//cas 2 R-S -> G -> T
			            	addBehaviour(new CyclicBehaviour(){
			    				@Override
			    				public void action() {
			    					try{
			    				
							          ACLMessage msg = myAgent.receive();
							          if(msg != null){
										int i = ((AgentGenetique3)myAgent).getNbMessage();
										System.out.println(i);
										if ( i>= 29){
											Main.factory("transposition", "LOX", "selectiondeterministe",1f,1f,1024,
													0, 10, 0);;
											System.out.println("Valeur finale" + Main.longchemin(Main.meilleurchemin(main.population)));
											ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
											notif.setContentObject(Main.meilleurchemin(main.population));
											notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
											System.out.println("Message envoy� � l'agent tabou");
											myAgent.send(notif);
										}
										else{
											System.out.println("Message re�u");
											main.population[i-1] = (int[]) msg.getContentObject();
											((AgentGenetique3)myAgent).setNbMessage(i+1);
										}
										
									
									} else block();}
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
										int i = ((AgentGenetique3)myAgent).getNbMessage();
										if (i == 1){
											if(!msg.getContent().equals("rien")){
											int[] sol =(int[]) msg.getContentObject();
											float val =(float) Main.longchemin(sol);
											if (val < Main.longchemin(Main.meilleurchemin(main.population))){
												main.population[0] = sol;
											}
											((AgentGenetique3)myAgent).setNbMessage(i+1);
											}
											
											System.out.println("Genetique " + Main.longchemin(Main.meilleurchemin(main.population)));
											Main.factory("transposition", "LOX", "selectiondeterministe",1f,1f,1024,
													0, 10, 0);
											ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
											notif.setContentObject(Main.meilleurchemin(main.population));
											notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
											notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
											System.out.println("Message envoy� � l'agent tabou et au recuit");
											myAgent.send(notif);
											((AgentGenetique3)myAgent).setNbMessage(0);
										}
										else{
											int[] sol =(int[]) msg.getContentObject();
											double val =(double) Main.longchemin(sol);
											if (val < Main.longchemin(Main.meilleurchemin(main.population))){
												main.population[0] = sol;
											}
											((AgentGenetique3)myAgent).setNbMessage(i+1);
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



