package org.example.simulation;

import OSPABA.*;
import org.example.agents.*;

public class MySimulation extends Simulation
{
	public MySimulation()
	{
		init();
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModel(new AgentModel(Id.agentModel, this, null));
		setAgentOkolie(new AgentOkolie(Id.agentOkolie, this, agentModel()));
		setAgentSystem(new AgentSystem(Id.agentSystem, this, agentModel()));
		setAgentAutomat(new AgentAutomat(Id.agentAutomat, this, agentSystem()));
		setAgentObsluzneMiesta(new AgentObsluzneMiesta(Id.agentObsluzneMiesta, this, agentSystem()));
		setAgentPokladne(new AgentPokladne(Id.agentPokladne, this, agentSystem()));
		setAgentPrevzatieTovar(new AgentPrevzatieTovar(Id.agentPrevzatieTovar, this, agentSystem()));
	}

	private AgentModel _agentModel;

public AgentModel agentModel()
	{ return _agentModel; }

	public void setAgentModel(AgentModel agentModel)
	{_agentModel = agentModel; }

	private AgentOkolie _agentOkolie;

public AgentOkolie agentOkolie()
	{ return _agentOkolie; }

	public void setAgentOkolie(AgentOkolie agentOkolie)
	{_agentOkolie = agentOkolie; }

	private AgentSystem _agentSystem;

public AgentSystem agentSystem()
	{ return _agentSystem; }

	public void setAgentSystem(AgentSystem agentSystem)
	{_agentSystem = agentSystem; }

	private AgentAutomat _agentAutomat;

public AgentAutomat agentAutomat()
	{ return _agentAutomat; }

	public void setAgentAutomat(AgentAutomat agentAutomat)
	{_agentAutomat = agentAutomat; }

	private AgentObsluzneMiesta _agentObsluzneMiesta;

public AgentObsluzneMiesta agentObsluzneMiesta()
	{ return _agentObsluzneMiesta; }

	public void setAgentObsluzneMiesta(AgentObsluzneMiesta agentObsluzneMiesta)
	{_agentObsluzneMiesta = agentObsluzneMiesta; }

	private AgentPokladne _agentPokladne;

public AgentPokladne agentPokladne()
	{ return _agentPokladne; }

	public void setAgentPokladne(AgentPokladne agentPokladne)
	{_agentPokladne = agentPokladne; }

	private AgentPrevzatieTovar _agentPrevzatieTovar;

public AgentPrevzatieTovar agentPrevzatieTovar()
	{ return _agentPrevzatieTovar; }

	public void setAgentPrevzatieTovar(AgentPrevzatieTovar agentPrevzatieTovar)
	{_agentPrevzatieTovar = agentPrevzatieTovar; }
	//meta! tag="end"
}
