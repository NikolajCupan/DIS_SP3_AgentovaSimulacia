package org.example.instantAssistants;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="150"
public class QueryKoniecPrestavkaPokladne extends Query
{
	public QueryKoniecPrestavkaPokladne(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}

}
