// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package email_connector.actions;

import com.mendix.core.Core;
import com.mendix.core.objectmanagement.member.MendixString;
import com.mendix.datahub.connector.email.utils.SendMailsException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.IMendixObjectMember;
import com.mendix.webui.CustomJavaAction;
import email_connector.implementation.MxMailMapper;
import encryption.proxies.microflows.Microflows;
import mxmodelreflection.TokenReplacer;
import java.util.List;
import java.util.Map;

public class SendEmailWithTemplate extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject DataObject;
	private IMendixObject __EmailAccount;
	private email_connector.proxies.EmailAccount EmailAccount;
	private IMendixObject __EmailTemplate;
	private email_connector.proxies.EmailTemplate EmailTemplate;
	private java.lang.Boolean Queued;

	public SendEmailWithTemplate(IContext context, IMendixObject DataObject, IMendixObject EmailAccount, IMendixObject EmailTemplate, java.lang.Boolean Queued)
	{
		super(context);
		this.DataObject = DataObject;
		this.__EmailAccount = EmailAccount;
		this.__EmailTemplate = EmailTemplate;
		this.Queued = Queued;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.EmailAccount = __EmailAccount == null ? null : email_connector.proxies.EmailAccount.initialize(getContext(), __EmailAccount);

		this.EmailTemplate = __EmailTemplate == null ? null : email_connector.proxies.EmailTemplate.initialize(getContext(), __EmailTemplate);

		// BEGIN USER CODE
		if (this.EmailAccount == null)
			throw new SendMailsException("Account object cannot be null");

		if (Boolean.FALSE.equals(this.EmailAccount.getisOutgoingEmailConfigured()) || this.EmailAccount.getOutgoingEmailConfiguration_EmailAccount() == null)
			throw new SendMailsException("Outgoing server is not configured");

		if (this.EmailTemplate == null)
			throw new SendMailsException("Email template object cannot be null");

		if(Boolean.TRUE.equals(this.EmailTemplate.getSigned()) && Boolean.FALSE.equals(this.EmailAccount.getisP12Configured()))
			throw new SendMailsException("Digital Signature not configured for this account");

		if(Boolean.TRUE.equals(this.EmailTemplate.getEncrypted()) && Boolean.FALSE.equals(this.EmailAccount.getisLDAPConfigured()))
			throw new SendMailsException("LDAP not configured for this account");

        List<IMendixObject> tokenList = Core.retrieveByPath(getContext(), this.EmailTemplate.getMendixObject(), email_connector.proxies.EmailTemplate.MemberNames.EmailTemplate_Token.toString());
        var email = MxMailMapper.getEmailMessageFromTemplate(getContext(), this.EmailTemplate, this.Queued);

        Map<String, ? extends IMendixObjectMember<?>> members = email.getMendixObject().getMembers(getContext());
        for (Map.Entry<String, ? extends IMendixObjectMember<?>> entry : members.entrySet()) {
            IMendixObjectMember<?> m = entry.getValue();
            if (m instanceof MendixString && m.hasWriteAccess(this.getContext())) {
                MendixString member = (MendixString) m;
                member.setValue(this.getContext(), TokenReplacer.replaceTokens(this.getContext(), member.getValue(this.getContext()), tokenList, this.DataObject));
            }
        }
		this.EmailAccount.setPassword(Microflows.decrypt(getContext(), this.EmailAccount.getPassword()));
		email_connector.proxies.microflows.Microflows.aCT_SendEmailFromJavaAction(getContext(), this.EmailAccount, email, this.EmailTemplate);
        return null;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "SendEmailWithTemplate";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
