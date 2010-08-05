package org.opensixen;

import org.compiere.Adempiere;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class OpenSixen implements IApplication {

	
	private void initOpensixen()	{
		Adempiere.startup(true);
		
		String className = "org.compiere.apps.AMenu";
		try
		{
			Class<?> startClass = Class.forName(className);
			startClass.newInstance();
		}
		catch (Exception e)
		{
			System.err.println("Opensixen starting: " + className + " - " + e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		initOpensixen();
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
