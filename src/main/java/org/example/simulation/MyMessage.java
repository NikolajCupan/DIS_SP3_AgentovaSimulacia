package org.example.simulation;

import OSPABA.*;

public class MyMessage extends MessageForm
{
	// Vlastne
	private boolean vypnutieAutomatNastavene;
	private boolean vypnutieAutomat;

	public boolean getVypnutieAutomat()
	{
		if (!this.vypnutieAutomatNastavene)
		{
			throw new RuntimeException("Atribut nie je nastaveny!");
		}

		return this.vypnutieAutomat;
	}

	public void setVypnutieAutomat(boolean vypnutieAutomat)
	{
		if (this.vypnutieAutomatNastavene)
		{
			throw new RuntimeException("Atribut uz je nastaveny!");
		}

		this.vypnutieAutomatNastavene = true;
		this.vypnutieAutomat = vypnutieAutomat;
	}
	// Vlastne koniec


	public MyMessage(Simulation sim)
	{
		super(sim);
	}

	public MyMessage(MyMessage original)
	{
		super(original);
		// copy() is called in superclass
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		// Copy attributes
	}
}
