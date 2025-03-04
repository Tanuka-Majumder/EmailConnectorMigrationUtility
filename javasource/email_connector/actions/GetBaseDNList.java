// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package email_connector.actions;

import com.mendix.datahub.connector.email.model.LDAPConfiguration;
import com.mendix.datahub.connector.email.utils.LDAPHelper;
import com.mendix.datahub.connector.email.utils.ReceiveMailsException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import email_connector.proxies.LDAPBaseDN;
import encryption.proxies.microflows.Microflows;
import java.util.ArrayList;

public class GetBaseDNList extends CustomJavaAction<java.util.List<IMendixObject>>
{
	private IMendixObject __LDAPConfiguration;
	private email_connector.proxies.LDAPConfiguration LDAPConfiguration;

	public GetBaseDNList(IContext context, IMendixObject LDAPConfiguration)
	{
		super(context);
		this.__LDAPConfiguration = LDAPConfiguration;
	}

	@java.lang.Override
	public java.util.List<IMendixObject> executeAction() throws Exception
	{
		this.LDAPConfiguration = __LDAPConfiguration == null ? null : email_connector.proxies.LDAPConfiguration.initialize(getContext(), __LDAPConfiguration);

		// BEGIN USER CODE
		if (this.LDAPConfiguration == null)
			throw new ReceiveMailsException("LDAPConfiguration cannot be null.");

		java.util.List<IMendixObject> newList = new ArrayList<>();
		var ldapConfiguration = new LDAPConfiguration();
		ldapConfiguration.setHost(this.LDAPConfiguration.getLDAPHost());
		ldapConfiguration.setPort(this.LDAPConfiguration.getLDAPPort());
		ldapConfiguration.setSSL(this.LDAPConfiguration.getisSSL());
		ldapConfiguration.setUserName(this.LDAPConfiguration.getLDAPUsername());
		if(this.LDAPConfiguration.getLDAPPassword() != null && !this.LDAPConfiguration.getLDAPPassword().isBlank())
		{
			ldapConfiguration.setPassword(Microflows.decrypt(getContext(), this.LDAPConfiguration.getLDAPPassword()));
		}

		for (String baseDN : LDAPHelper.getBaseDN(ldapConfiguration)) {
			var baseDNObj = new LDAPBaseDN(this.getContext());
			baseDNObj.setName(baseDN);
			newList.add(baseDNObj.getMendixObject());
		}
		return newList;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "GetBaseDNList";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
