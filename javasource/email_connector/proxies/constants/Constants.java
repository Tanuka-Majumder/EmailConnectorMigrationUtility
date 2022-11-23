// This file was generated by Mendix Studio Pro.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package email_connector.proxies.constants;

import com.mendix.core.Core;

public class Constants
{
	// These are the constants for the Email_Connector module

	public static java.lang.String getazure_defaultConfig()
	{
		return (java.lang.String)Core.getConfiguration().getConstantValue("Email_Connector.azure_defaultConfig");
	}

	/**
	* The amount of days to preserve error messages.
	*/
	public static java.lang.Long getEmailLogRetention()
	{
		return (java.lang.Long)Core.getConfiguration().getConstantValue("Email_Connector.EmailLogRetention");
	}

	public static java.lang.String getLogNode()
	{
		return (java.lang.String)Core.getConfiguration().getConstantValue("Email_Connector.LogNode");
	}
}