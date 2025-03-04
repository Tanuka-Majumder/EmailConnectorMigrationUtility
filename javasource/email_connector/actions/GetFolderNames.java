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
import email_connector.proxies.Folder;
import encryption.proxies.microflows.Microflows;
import java.util.ArrayList;
import static email_connector.implementation.Commons.getProtocol;

public class GetFolderNames extends CustomJavaAction<java.util.List<IMendixObject>>
{
	private IMendixObject __MxEmailAccount;
	private email_connector.proxies.EmailAccount MxEmailAccount;

	public GetFolderNames(IContext context, IMendixObject MxEmailAccount)
	{
		super(context);
		this.__MxEmailAccount = MxEmailAccount;
	}

	@java.lang.Override
	public java.util.List<IMendixObject> executeAction() throws Exception
	{
		this.MxEmailAccount = __MxEmailAccount == null ? null : email_connector.proxies.EmailAccount.initialize(getContext(), __MxEmailAccount);

		// BEGIN USER CODE
        if (this.MxEmailAccount == null)
            throw new ReceiveMailsException("MxEmailAccount cannot be null.");
		if (Boolean.FALSE.equals(this.MxEmailAccount.getisIncomingEmailConfigured()) || this.MxEmailAccount.getIncomingEmailConfiguration_EmailAccount() == null )
			throw new ReceiveMailsException("Incoming server is not configured");
        java.util.List<IMendixObject> newList = new ArrayList<>();
		var account = new ReceiveEmailAccount(getProtocol(this.MxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getIncomingProtocol()), this.MxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getServerHost(), this.MxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getServerPort(), this.MxEmailAccount.getUsername(), Microflows.decrypt(getContext(), this.MxEmailAccount.getPassword()));		MxMailMapper.setReceiveAccountConfigurations(this.MxEmailAccount, account);
        var emailServiceWorker = new EmailServiceWorker(account);
        for (String fd : emailServiceWorker.getFolders()) {
            var newFolder = new Folder(this.getContext());
            newFolder.setName(fd);
            newList.add(newFolder.getMendixObject());
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
		return "GetFolderNames";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
