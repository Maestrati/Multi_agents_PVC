package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;

public class Chef extends Agent {
	public void setup(){
		Object []arguments = getArguments();
		int cas =(int)arguments[0];
		int j = (int) arguments[1];
		switch (cas){
		case 2:
			addBehaviour(new CyclicBehaviour(){
				@Override
				public void action() {
					
					//cas 1 comportement linéaire
					try{
						ACLMessage msg = myAgent.receive();
						if(msg != null){
						ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
						notif.setContent("rien");
						for (int i = 0; i < j*10; i++){
							notif.addReceiver(new AID("recuit"+i,AID.ISLOCALNAME));
						}
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
					//cas 2 R-S -> G -> T
					try{
						ACLMessage msg = myAgent.receive();
						if(msg != null){
							ACLMessage notif =  new ACLMessage(ACLMessage.INFORM);
							notif.setContent("rien");
							notif.addReceiver(new AID("tabou",AID.ISLOCALNAME));
							notif.addReceiver(new AID("recuit",AID.ISLOCALNAME));
							notif.addReceiver(new AID("genetique",AID.ISLOCALNAME));
							myAgent.send(notif);
							
						} else block();
						
					
					
					}
					catch(Exception e){}
				}});
			break;
			
		
			
		
		}
		
		

	}

}
