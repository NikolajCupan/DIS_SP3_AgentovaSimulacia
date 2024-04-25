package org.example.instantAssistants;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="148"
public class QueryKoniecPrestavkaObsluzneMiesta extends Query
{
	public QueryKoniecPrestavkaObsluzneMiesta(int id, Simulation mySim, CommonAgent myAgent)
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
