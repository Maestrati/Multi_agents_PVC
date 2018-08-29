package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import OptimisationGenetique.Main;
import java.util.Arrays;

import Optimisation.Carte;
import Optimisation.Genetique;

public class AgentLanceur extends Agent {
	public void setup() {
		LanceurBehaviour b = new LanceurBehaviour(this);
		addBehaviour(b);
	}
}

class LanceurBehaviour extends OneShotBehaviour {
	
	public LanceurBehaviour (Agent a) {
		super(a);
	}
	
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID("Chef",AID.ISLOCALNAME));
		myAgent.send(msg);
		myAgent.doDelete();
	}
}