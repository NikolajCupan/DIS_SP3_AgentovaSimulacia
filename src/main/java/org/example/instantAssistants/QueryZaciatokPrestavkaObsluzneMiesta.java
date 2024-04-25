package org.example.instantAssistants;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="146"
public class QueryZaciatokPrestavkaObsluzneMiesta extends Query
{
	public QueryZaciatokPrestavkaObsluzneMiesta(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}

}
