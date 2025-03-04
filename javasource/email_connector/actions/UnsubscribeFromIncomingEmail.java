// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package email_connector.actions;

import com.mendix.datahub.connector.email.model.ReceiveEmailAccount;
import com.mendix.datahub.connector.email.service.EmailServiceWorker;
import com.mendix.datahub.connector.email.utils.ReceiveMailsException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import email_connector.implementation.MxMailMapper;
import encryption.proxies.microflows.Microflows;
import static email_connector.implementation.Commons.getProtocol;

public class UnsubscribeFromIncomingEmail extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __account;
	private email_connector.proxies.EmailAccount account;

	public UnsubscribeFromIncomingEmail(IContext context, IMendixObject account)
	{
		super(context);
		this.__account = account;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.account = __account == null ? null : email_connector.proxies.EmailAccount.initialize(getContext(), __account);

		// BEGIN USER CODE
		if (this.account == null)
			throw new ReceiveMailsException("Account object cannot be null");
		if (Boolean.FALSE.equals(this.account.getisIncomingEmailConfigured()) || this.account.getIncomingEmailConfiguration_EmailAccount() == null )
			throw new ReceiveMailsException("Incoming server is not configured");
		var serverAccount = new ReceiveEmailAccount(getProtocol(this.account.getIncomingEmailConfiguration_EmailAccount().getIncomingProtocol()), this.account.getIncomingEmailConfiguration_EmailAccount().getServerHost(), this.account.getIncomingEmailConfiguration_EmailAccount().getServerPort(), this.account.getUsername(), Microflows.decrypt(getContext(), this.account.getPassword()));		MxMailMapper.setReceiveAccountConfigurations(this.account, serverAccount);
		var emailService = new EmailServiceWorker(serverAccount);
		emailService.deregisterOnNewMailListener();
		return null;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "UnsubscribeFromIncomingEmail";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
