package org.example.instantAssistants;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="119"
public class QueryVyprazdnenieFrontAutomat extends Query
{
	public QueryVyprazdnenieFrontAutomat(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentAutomat myAgent()
	{
		return (AgentAutomat)super.myAgent();
	}

}
