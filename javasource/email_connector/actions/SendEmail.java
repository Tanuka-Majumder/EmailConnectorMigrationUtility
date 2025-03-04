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
import com.mendix.datahub.connector.email.model.SendEmailAccount;
import com.mendix.datahub.connector.email.model.SendEmailMessage;
import com.mendix.datahub.connector.email.service.EmailServiceWorker;
import com.mendix.datahub.connector.email.utils.SendMailsException;
import com.mendix.datahub.connector.eventtracking.Metrics;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import email_connector.implementation.MxMailMapper;
import email_connector.proxies.Attachment;
import email_connector.proxies.LDAPConfiguration;
import email_connector.proxies.Pk12Certificate;
import encryption.proxies.microflows.Microflows;
import java.util.List;

public class SendEmail extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __EmailAccount;
	private email_connector.proxies.EmailAccount EmailAccount;
	private IMendixObject __EmailMessage;
	private email_connector.proxies.EmailMessage EmailMessage;

	public SendEmail(IContext context, IMendixObject EmailAccount, IMendixObject EmailMessage)
	{
		super(context);
		this.__EmailAccount = EmailAccount;
		this.__EmailMessage = EmailMessage;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.EmailAccount = __EmailAccount == null ? null : email_connector.proxies.EmailAccount.initialize(getContext(), __EmailAccount);

		this.EmailMessage = __EmailMessage == null ? null : email_connector.proxies.EmailMessage.initialize(getContext(), __EmailMessage);

		// BEGIN USER CODE
		if (this.EmailAccount == null)
			throw new SendMailsException("Account object cannot be null");
		if (Boolean.FALSE.equals(this.EmailAccount.getisOutgoingEmailConfigured()) || this.EmailAccount.getOutgoingEmailConfiguration_EmailAccount() == null)
			throw new SendMailsException("Outgoing server is not configured");
		if (this.EmailMessage == null)
			throw new SendMailsException("Email Message object cannot be null");

		List<Attachment> attachments = new java.util.ArrayList<>();
		List<IMendixObject> attachmentLists = Core.retrieveByPath(getContext(), this.EmailMessage.getMendixObject(), Attachment.MemberNames.Attachment_EmailMessage.toString());

		for (IMendixObject __AttachmentListElement : attachmentLists)
			attachments.add(email_connector.proxies.Attachment.initialize(getContext(), __AttachmentListElement));
		var pk12Certificate = getPk12Certificate();
		var ldapConfiguration = getLdapConfiguration();
		var serverAccount = new SendEmailAccount(this.EmailAccount.getOutgoingEmailConfiguration_EmailAccount().getServerHost(), this.EmailAccount.getOutgoingEmailConfiguration_EmailAccount().getServerPort(), this.EmailAccount.getUsername(), Microflows.decrypt(getContext(), this.EmailAccount.getPassword()));
		var sendEmailMsg = new SendEmailMessage();
		MxMailMapper.setSendAccountConfigurations(this.EmailAccount, serverAccount, pk12Certificate, ldapConfiguration, context());
		MxMailMapper.setServerSendEmailMessage(this.EmailMessage, sendEmailMsg, attachments, context());
		var emailService = new EmailServiceWorker(serverAccount);
		emailService.sendMail(sendEmailMsg);

		Metrics.createCounter("dnl_connectors_ec_send_email")
				.addTag("encrypted", String.valueOf(sendEmailMsg.isEncrypted()))
				.addTag("signed", String.valueOf(sendEmailMsg.isSigned()))
				.addTag("templateused", String.valueOf(this.EmailMessage.getEmailMessage_EmailTemplate() != null))
				.setDescription("App sends email with plain text")
				.build()
				.increment();


        return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "SendEmail";
	}

	// BEGIN EXTRA CODE
	private Pk12Certificate getPk12Certificate() throws SendMailsException {
		if(Boolean.TRUE.equals(this.EmailMessage.getisSigned())){
			var pkcs12Certficates=  Core.retrieveByPath(getContext(), this.EmailAccount.getMendixObject(), Pk12Certificate.MemberNames.Pk12Certificate_EmailAccount.toString());
			if(pkcs12Certficates==null || pkcs12Certficates.isEmpty()){
				throw new SendMailsException("Email signing certificate cannot be empty");
			}
			var pk12Certificate = Pk12Certificate.initialize(getContext(),pkcs12Certficates.get(0));
			if(pk12Certificate.getPassphrase()==null || pk12Certificate.getPassphrase().isBlank()){
				throw new SendMailsException("Email signing certificate passphrase cannot be empty");
			}
			pk12Certificate.setPassphrase(Microflows.decrypt(getContext(), pk12Certificate.getPassphrase()));
			return pk12Certificate;
		}
		return null;
	}

	private LDAPConfiguration getLdapConfiguration() throws SendMailsException {
		if(Boolean.TRUE.equals(this.EmailMessage.getisEncrypted())){
			var ldapConfig=  Core.retrieveByPath(getContext(), this.EmailAccount.getMendixObject(), LDAPConfiguration.MemberNames.EmailAccount_LDAPConfiguration.toString());
			if(ldapConfig==null || ldapConfig.isEmpty()){
				throw new SendMailsException("LDAP configuration cannot be empty");
			}
			var ldapConfiguration = LDAPConfiguration.initialize(getContext(),ldapConfig.get(0));
			if(ldapConfiguration.getLDAPHost()==null || ldapConfiguration.getLDAPHost().isBlank()){
				throw new SendMailsException("LDAP host cannot be empty");
			}
			if(ldapConfiguration.getLDAPPort()==null){
				throw new SendMailsException("LDAP port cannot be empty");
			}
			if(ldapConfiguration.getLDAPPassword() != null && !ldapConfiguration.getLDAPPassword().isBlank())
			{
				ldapConfiguration.setLDAPPassword(Microflows.decrypt(getContext(), ldapConfiguration.getLDAPPassword()));
			}
			return ldapConfiguration;
		}
		return null;
	}
	// END EXTRA CODE
}
