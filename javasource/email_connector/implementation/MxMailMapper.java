package email_connector.implementation;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.datahub.connector.email.model.Attachment;
import com.mendix.datahub.connector.email.model.LDAPConfiguration;
import com.mendix.datahub.connector.email.model.*;
import com.mendix.datahub.connector.email.model.autoconfig.EmailProvider;
import com.mendix.datahub.connector.email.utils.SendMailsException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import email_connector.proxies.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static email_connector.implementation.Commons.*;

public class MxMailMapper {

    static final String ADDRESSSEPARATORREGEX = "\\s*;\\s*";

    private MxMailMapper() {
    }

    public static List<IMendixObject> mapEmails(EmailAccount mxEmailAccount, List<Message> serverEmailList, IContext context) {

        ArrayList<IMendixObject> mxMailList = new ArrayList<>();
        for (Message serverMessage : serverEmailList) {
            var mxEmailMessage = getMxEmailMessage(mxEmailAccount, serverMessage, context);
            mxMailList.add(mxEmailMessage.getMendixObject());
        }
        return mxMailList;
    }

    public static void setReceiveAccountConfigurations(EmailAccount mxEmailAccount, ReceiveEmailAccount serverAccount) throws CoreException {
        serverAccount.setFolder(mxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getFolder());
        serverAccount.setBatchSize(mxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getBatchSize());
        serverAccount.setHandling(getHandling(mxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getHandling()));
        serverAccount.setMoveFolder(mxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getMoveFolder());
        serverAccount.setUseInlineImages(mxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getProcessInlineImage());
        serverAccount.setUseBatchImport(mxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getUseBatchImport());
        serverAccount.setFetchStrategy(getFetchStrategy(mxEmailAccount.getIncomingEmailConfiguration_EmailAccount().getFetchStrategy()));
        setCommonAccountConfiguration(mxEmailAccount, serverAccount);

    }

    private static void setCommonAccountConfiguration(EmailAccount mxEmailAccount, Account serverAccount) {
        serverAccount.setSanitizeEmailBodyForXSSScript(mxEmailAccount.getsanitizeEmailBodyForXSSScript());
        serverAccount.setConnectionTimeout(String.valueOf(mxEmailAccount.getTimeout()));
        serverAccount.setFromDisplayName(mxEmailAccount.getFromDisplayName());
        serverAccount.setUseSSLServerCheckIdentity(mxEmailAccount.getUseSSLCheckServerIdentity());
        serverAccount.setOAuthUsed(mxEmailAccount.getisOAuthUsed());
        if (serverAccount.isOAuthUsed() && mxEmailAccount.getOAuthSetupComplete().equals(true)) {
            serverAccount.setoAuthAccessTokenWorker(new OAuthTokenWorker(mxEmailAccount));
        }
    }

    private static void processAttachment(Message serverMessage, EmailMessage mxEmailMessage, IContext context) {
        List<Attachment> serverAttachmentList = serverMessage.getAttachment();
        for (Attachment serverAttachment : serverAttachmentList) {
            var mxAttachment = new email_connector.proxies.Attachment(context);
            mxAttachment.setHasContents(true);
            mxAttachment.setAttachment_EmailMessage(mxEmailMessage);
            mxAttachment.setContentID(serverAttachment.getAttachmentContentID());
            mxAttachment.setName(serverAttachment.getAttachmentName());
            mxAttachment.setSize(Long.valueOf(serverAttachment.getAttachmentSize()));
            mxAttachment.setattachmentContentType(serverAttachment.getAttachmentType());
            mxAttachment.setattachmentName(serverAttachment.getAttachmentName());
            mxAttachment.setattachmentSize(serverAttachment.getAttachmentSize());
            if (serverAttachment.getAttachmentPosition() != null)
                mxAttachment.setPosition(getPosition(serverAttachment.getAttachmentPosition()));

            var byteStream = new ByteArrayInputStream(serverAttachment.getAttachmentContent());
            Core.storeFileDocumentContent(context, mxAttachment.getMendixObject(), byteStream);
            if (serverAttachment.getAttachmentPosition() != null) {
                displayInlineAttachment(mxEmailMessage, serverAttachment, mxAttachment);
            }
        }
    }


    private static void displayInlineAttachment(EmailMessage mxEmailMessage, Attachment serverAttachment, email_connector.proxies.Attachment mxAttachment) {
        var url = "/file?target=window&fileID=";
        if (mxEmailMessage.getisHTML().equals(true) && serverAttachment.getAttachmentPosition().equalsIgnoreCase("inline")) {
            String source = "cid:" + serverAttachment.getAttachmentContentID();
            String target = url + mxAttachment.getFileID();
            mxEmailMessage.setContent(mxEmailMessage.getContent().replaceAll(source, target));
        }
    }

    private static EmailMessage getMxEmailMessage(EmailAccount mxEmailAccount, Message serverMessage, IContext context) {
        var mxEmailMessage = new EmailMessage(context);
        mxEmailMessage.setEmailMessage_EmailAccount(mxEmailAccount);
        mxEmailMessage.setSubject(serverMessage.getSubject());
        mxEmailMessage.setSentDate(serverMessage.getSentDate());
        mxEmailMessage.setFrom(String.join(";", serverMessage.getFrom()));
        mxEmailMessage.setTo(String.join(";", serverMessage.getTo()));
        mxEmailMessage.setCC(String.join(";", serverMessage.getCc()));
        mxEmailMessage.setBCC(String.join(";", serverMessage.getBcc()));
        mxEmailMessage.setContent(serverMessage.getContent());
        mxEmailMessage.setisHTML(serverMessage.isHTML());
        mxEmailMessage.sethasAttachments(serverMessage.isHasAttachments());
        mxEmailMessage.setStatus(ENUM_EmailStatus.RECEIVED);
        mxEmailMessage.setReplyTo(serverMessage.getReplyTo());
        mxEmailMessage.setFromDisplayName(serverMessage.getFromDisplayName());
        if (serverMessage.isHasAttachments()) {
            processAttachment(serverMessage, mxEmailMessage, context);
        }
        mxEmailMessage.setSize(serverMessage.getSize());
        return mxEmailMessage;
    }

    public static void setSendAccountConfigurations(EmailAccount mxEmailAccount, SendEmailAccount serverAccount, Pk12Certificate pkcsCertficate, email_connector.proxies.LDAPConfiguration mxLdapConfiguration, IContext context) throws CoreException, SendMailsException {
        serverAccount.setUseSSLSMTP(mxEmailAccount.getOutgoingEmailConfiguration_EmailAccount().getSSL());
        serverAccount.setUseTLSSMTP(mxEmailAccount.getOutgoingEmailConfiguration_EmailAccount().getTLS());
        if (pkcsCertficate != null) {
            try (
                    var inputStream = Core.getFileDocumentContent(context, pkcsCertficate.getMendixObject());
                    var byteArrayOutputStream = new ByteArrayOutputStream()
            ) {
                inputStream.transferTo(byteArrayOutputStream);
                serverAccount.setP12Certificate(byteArrayOutputStream.toByteArray());
                serverAccount.setPassphrase(pkcsCertficate.getPassphrase());
            } catch (IOException ex) {
                throw new SendMailsException("Error fetching certificate content");
            }
        }
        if (mxLdapConfiguration != null) {
            var ldapConfigurations = new LDAPConfiguration();
            ldapConfigurations.setHost(mxLdapConfiguration.getLDAPHost());
            ldapConfigurations.setPort(mxLdapConfiguration.getLDAPPort());
            ldapConfigurations.setSearchBaseName(mxLdapConfiguration.getBaseDN());
            ldapConfigurations.setSSL(mxLdapConfiguration.getisSSL());
            ldapConfigurations.setUserName(mxLdapConfiguration.getLDAPUsername());
            ldapConfigurations.setPassword(mxLdapConfiguration.getLDAPPassword());
            serverAccount.setLdapConfigurations(ldapConfigurations);
        }
        setCommonAccountConfiguration(mxEmailAccount, serverAccount);
    }

    public static void setServerSendEmailMessage(EmailMessage emailMessage, SendEmailMessage sendEmailMsg, List<email_connector.proxies.Attachment> attachmentList, IContext context) throws SendMailsException {
        sendEmailMsg.setSubject(emailMessage.getSubject());
        sendEmailMsg.setFromDisplayName(emailMessage.getFromDisplayName());
        setRecipients(emailMessage, sendEmailMsg);

        sendEmailMsg.setContent(emailMessage.getContent());
        sendEmailMsg.setPlainBody(emailMessage.getPlainBody());
        sendEmailMsg.setHTML(emailMessage.getisHTML());
        sendEmailMsg.setSigned(emailMessage.getisSigned());

        sendEmailMsg.setEncrypted(emailMessage.getisEncrypted());

        if (emailMessage.getReplyTo() != null && !emailMessage.getReplyTo().isEmpty())
            sendEmailMsg.setReplyTo(emailMessage.getReplyTo());

        if (attachmentList != null && !attachmentList.isEmpty()) {
            List<Attachment> serverAttachmentList = new ArrayList<>();
            for (email_connector.proxies.Attachment mxAttachment : attachmentList) {
                var attachment = new Attachment();
                attachment.setAttachmentName(mxAttachment.getName());
                try (
                        var inputStream = Core.getFileDocumentContent(context, mxAttachment.getMendixObject());
                        var byteArrayOutputStream = new ByteArrayOutputStream()
                ) {
                    inputStream.transferTo(byteArrayOutputStream);
                    attachment.setAttachmentContent(byteArrayOutputStream.toByteArray());
                    serverAttachmentList.add(attachment);
                } catch (IOException ex) {
                    throw new SendMailsException("Error fetching attachment content");
                }

            }
            sendEmailMsg.setAttachment(serverAttachmentList);
        }
    }

    private static void setRecipients(EmailMessage emailMessage, SendEmailMessage sendEmailMsg) {
        if (emailMessage.getFrom() != null && !emailMessage.getFrom().isEmpty())
            sendEmailMsg.setFrom(Arrays.asList(emailMessage.getFrom().trim().split(ADDRESSSEPARATORREGEX)));

        if (emailMessage.getTo() != null && !emailMessage.getTo().isEmpty())
            sendEmailMsg.setTo(Arrays.asList(emailMessage.getTo().trim().split(ADDRESSSEPARATORREGEX)));

        if (emailMessage.getCC() != null && !emailMessage.getCC().isEmpty())
            sendEmailMsg.setCc(Arrays.asList(emailMessage.getCC().trim().split(ADDRESSSEPARATORREGEX)));

        if (emailMessage.getBCC() != null && !emailMessage.getBCC().isEmpty())
            sendEmailMsg.setBcc(Arrays.asList(emailMessage.getBCC().trim().split(ADDRESSSEPARATORREGEX)));
    }

    public static void getMappedEmailProvider(IContext context, EmailProvider emailProviders, email_connector.proxies.EmailProvider mxEmailProvider) {

        for (com.mendix.datahub.connector.email.model.autoconfig.IncomingServer incomingServer : emailProviders.getIncomingServers()) {
            var mxIncomingServer = new IncomingServer(context);
            mxIncomingServer.setIncomingServer_EmailProvider(mxEmailProvider);
            mxIncomingServer.setHostName(incomingServer.getHostname());
            mxIncomingServer.setPort(incomingServer.getPort());
            mxIncomingServer.setSocketType(incomingServer.getSocketType());
            mxIncomingServer.setIncomingProtocol(getIncomingProxyProtocol(incomingServer.getType()));
        }


        for (com.mendix.datahub.connector.email.model.autoconfig.OutgoingServer outgoingServer : emailProviders.getOutgoingServers()) {
            var mxOutgoingServer = new OutgoingServer(context);
            mxOutgoingServer.setOutgoingServer_EmailProvider(mxEmailProvider);
            mxOutgoingServer.setHostName(outgoingServer.getHostname());
            mxOutgoingServer.setPort(outgoingServer.getPort());
            mxOutgoingServer.setSSL(outgoingServer.isUseSSL());
            mxOutgoingServer.setTLS(outgoingServer.isUseTLS());
            if (outgoingServer.getType().name().equals("SMTP"))
                mxOutgoingServer.setOutgoingProtocol(ENUM_OutgoingProtocol.SMTP);
        }
    }

    public static EmailMessage getEmailMessageFromTemplate(IContext context, EmailTemplate emailTemplate, Boolean queued) {
        var emailMessage = new EmailMessage(context);
        emailMessage.setTo(emailTemplate.getTo());
        emailMessage.setCC(emailTemplate.getCC());
        emailMessage.setBCC(emailTemplate.getBCC());
        emailMessage.setSubject(emailTemplate.getSubject());
        emailMessage.setContent(emailTemplate.getContent());
        emailMessage.setSentDate(emailTemplate.getSentDate());
        emailMessage.setPlainBody(emailTemplate.getPlainBody());
        emailMessage.setisHTML(emailTemplate.getisHTML());
        emailMessage.setReplyTo(emailTemplate.getReplyTo());
        emailMessage.setFromDisplayName(emailTemplate.getFromDisplayName());
        if (Boolean.TRUE.equals(queued)) {
            emailMessage.setQueuedForSending(true);
            emailMessage.setStatus(ENUM_EmailStatus.QUEUED);
        }
        emailMessage.setEmailMessage_EmailTemplate(emailTemplate);
        emailMessage.setisSigned(emailTemplate.getSigned());
        emailMessage.setisEncrypted(emailTemplate.getEncrypted());
        return emailMessage;
    }
}
