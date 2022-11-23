// This file was generated by Mendix Studio Pro.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package emailtemplate.proxies.constants;

import com.mendix.core.Core;

public class Constants
{
	// These are the constants for the EmailTemplate module

	public static java.lang.String getazure_emailServerHostURL()
	{
		return (java.lang.String)Core.getConfiguration().getConstantValue("EmailTemplate.azure_emailServerHostURL");
	}

	public static java.lang.String getEmailAddressSeparator()
	{
		return (java.lang.String)Core.getConfiguration().getConstantValue("EmailTemplate.EmailAddressSeparator");
	}

	/**
	* The amount of days to preserve error messages.
	*/
	public static java.lang.Long getEmailLogRetention()
	{
		return (java.lang.Long)Core.getConfiguration().getConstantValue("EmailTemplate.EmailLogRetention");
	}

	/**
	* The amount of days to preserve emails which were sent.
	*/
	public static java.lang.Long getEmailRetention()
	{
		return (java.lang.Long)Core.getConfiguration().getConstantValue("EmailTemplate.EmailRetention");
	}

	public static java.lang.String getLogNode()
	{
		return (java.lang.String)Core.getConfiguration().getConstantValue("EmailTemplate.LogNode");
	}

	/**
	* Using this path Redirect URI will formed. For eg. if constant value is "callback_et/"  then in case of Microsoft Azure AD the Redirect URI will be https://appRootURL/callback_et/azure.
	* 
	* Make sure value of this constant has just one "/" and it is placed at the end.
	*/
	public static java.lang.String getOAuthCallbackPath()
	{
		return (java.lang.String)Core.getConfiguration().getConstantValue("EmailTemplate.OAuthCallbackPath");
	}

	/**
	* This constant should be exposed to client.
	* 
	* Make sure value of this constant has just one "/" and it is placed at the end.
	*/
	public static java.lang.String getOAuthSignInPath()
	{
		return (java.lang.String)Core.getConfiguration().getConstantValue("EmailTemplate.OAuthSignInPath");
	}

	/**
	* Sending emails is enabled by default. 
	* When setting this value to false emails won't be send. This is useful on test environments for example.
	*/
	public static boolean getSendingEnabled()
	{
		return (java.lang.Boolean)Core.getConfiguration().getConstantValue("EmailTemplate.SendingEnabled");
	}
}