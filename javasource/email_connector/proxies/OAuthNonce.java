// This file was generated by Mendix Studio Pro.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package email_connector.proxies;

public class OAuthNonce
{
	private final com.mendix.systemwideinterfaces.core.IMendixObject oAuthNonceMendixObject;

	private final com.mendix.systemwideinterfaces.core.IContext context;

	/**
	 * Internal name of this entity
	 */
	public static final java.lang.String entityName = "Email_Connector.OAuthNonce";

	/**
	 * Enum describing members of this entity
	 */
	public enum MemberNames
	{
		State("State"),
		OAuthNonce_EmailAccount("Email_Connector.OAuthNonce_EmailAccount"),
		OAuthNonce_Session("Email_Connector.OAuthNonce_Session");

		private java.lang.String metaName;

		MemberNames(java.lang.String s)
		{
			metaName = s;
		}

		@java.lang.Override
		public java.lang.String toString()
		{
			return metaName;
		}
	}

	public OAuthNonce(com.mendix.systemwideinterfaces.core.IContext context)
	{
		this(context, com.mendix.core.Core.instantiate(context, "Email_Connector.OAuthNonce"));
	}

	protected OAuthNonce(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixObject oAuthNonceMendixObject)
	{
		if (oAuthNonceMendixObject == null)
			throw new java.lang.IllegalArgumentException("The given object cannot be null.");
		if (!com.mendix.core.Core.isSubClassOf("Email_Connector.OAuthNonce", oAuthNonceMendixObject.getType()))
			throw new java.lang.IllegalArgumentException("The given object is not a Email_Connector.OAuthNonce");

		this.oAuthNonceMendixObject = oAuthNonceMendixObject;
		this.context = context;
	}

	/**
	 * @deprecated Use 'OAuthNonce.load(IContext, IMendixIdentifier)' instead.
	 */
	@java.lang.Deprecated
	public static email_connector.proxies.OAuthNonce initialize(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixIdentifier mendixIdentifier) throws com.mendix.core.CoreException
	{
		return email_connector.proxies.OAuthNonce.load(context, mendixIdentifier);
	}

	/**
	 * Initialize a proxy using context (recommended). This context will be used for security checking when the get- and set-methods without context parameters are called.
	 * The get- and set-methods with context parameter should be used when for instance sudo access is necessary (IContext.createSudoClone() can be used to obtain sudo access).
	 */
	public static email_connector.proxies.OAuthNonce initialize(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixObject mendixObject)
	{
		return new email_connector.proxies.OAuthNonce(context, mendixObject);
	}

	public static email_connector.proxies.OAuthNonce load(com.mendix.systemwideinterfaces.core.IContext context, com.mendix.systemwideinterfaces.core.IMendixIdentifier mendixIdentifier) throws com.mendix.core.CoreException
	{
		com.mendix.systemwideinterfaces.core.IMendixObject mendixObject = com.mendix.core.Core.retrieveId(context, mendixIdentifier);
		return email_connector.proxies.OAuthNonce.initialize(context, mendixObject);
	}

	public static java.util.List<email_connector.proxies.OAuthNonce> load(com.mendix.systemwideinterfaces.core.IContext context, java.lang.String xpathConstraint) throws com.mendix.core.CoreException
	{
		java.util.List<email_connector.proxies.OAuthNonce> result = new java.util.ArrayList<email_connector.proxies.OAuthNonce>();
		for (com.mendix.systemwideinterfaces.core.IMendixObject obj : com.mendix.core.Core.retrieveXPathQuery(context, "//Email_Connector.OAuthNonce" + xpathConstraint))
			result.add(email_connector.proxies.OAuthNonce.initialize(context, obj));
		return result;
	}

	/**
	 * Commit the changes made on this proxy object.
	 */
	public final void commit() throws com.mendix.core.CoreException
	{
		com.mendix.core.Core.commit(context, getMendixObject());
	}

	/**
	 * Commit the changes made on this proxy object using the specified context.
	 */
	public final void commit(com.mendix.systemwideinterfaces.core.IContext context) throws com.mendix.core.CoreException
	{
		com.mendix.core.Core.commit(context, getMendixObject());
	}

	/**
	 * Delete the object.
	 */
	public final void delete()
	{
		com.mendix.core.Core.delete(context, getMendixObject());
	}

	/**
	 * Delete the object using the specified context.
	 */
	public final void delete(com.mendix.systemwideinterfaces.core.IContext context)
	{
		com.mendix.core.Core.delete(context, getMendixObject());
	}
	/**
	 * @return value of State
	 */
	public final java.lang.String getState()
	{
		return getState(getContext());
	}

	/**
	 * @param context
	 * @return value of State
	 */
	public final java.lang.String getState(com.mendix.systemwideinterfaces.core.IContext context)
	{
		return (java.lang.String) getMendixObject().getValue(context, MemberNames.State.toString());
	}

	/**
	 * Set value of State
	 * @param state
	 */
	public final void setState(java.lang.String state)
	{
		setState(getContext(), state);
	}

	/**
	 * Set value of State
	 * @param context
	 * @param state
	 */
	public final void setState(com.mendix.systemwideinterfaces.core.IContext context, java.lang.String state)
	{
		getMendixObject().setValue(context, MemberNames.State.toString(), state);
	}

	/**
	 * @return value of OAuthNonce_EmailAccount
	 */
	public final email_connector.proxies.EmailAccount getOAuthNonce_EmailAccount() throws com.mendix.core.CoreException
	{
		return getOAuthNonce_EmailAccount(getContext());
	}

	/**
	 * @param context
	 * @return value of OAuthNonce_EmailAccount
	 */
	public final email_connector.proxies.EmailAccount getOAuthNonce_EmailAccount(com.mendix.systemwideinterfaces.core.IContext context) throws com.mendix.core.CoreException
	{
		email_connector.proxies.EmailAccount result = null;
		com.mendix.systemwideinterfaces.core.IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.OAuthNonce_EmailAccount.toString());
		if (identifier != null)
			result = email_connector.proxies.EmailAccount.load(context, identifier);
		return result;
	}

	/**
	 * Set value of OAuthNonce_EmailAccount
	 * @param oauthnonce_emailaccount
	 */
	public final void setOAuthNonce_EmailAccount(email_connector.proxies.EmailAccount oauthnonce_emailaccount)
	{
		setOAuthNonce_EmailAccount(getContext(), oauthnonce_emailaccount);
	}

	/**
	 * Set value of OAuthNonce_EmailAccount
	 * @param context
	 * @param oauthnonce_emailaccount
	 */
	public final void setOAuthNonce_EmailAccount(com.mendix.systemwideinterfaces.core.IContext context, email_connector.proxies.EmailAccount oauthnonce_emailaccount)
	{
		if (oauthnonce_emailaccount == null)
			getMendixObject().setValue(context, MemberNames.OAuthNonce_EmailAccount.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.OAuthNonce_EmailAccount.toString(), oauthnonce_emailaccount.getMendixObject().getId());
	}

	/**
	 * @return value of OAuthNonce_Session
	 */
	public final system.proxies.Session getOAuthNonce_Session() throws com.mendix.core.CoreException
	{
		return getOAuthNonce_Session(getContext());
	}

	/**
	 * @param context
	 * @return value of OAuthNonce_Session
	 */
	public final system.proxies.Session getOAuthNonce_Session(com.mendix.systemwideinterfaces.core.IContext context) throws com.mendix.core.CoreException
	{
		system.proxies.Session result = null;
		com.mendix.systemwideinterfaces.core.IMendixIdentifier identifier = getMendixObject().getValue(context, MemberNames.OAuthNonce_Session.toString());
		if (identifier != null)
			result = system.proxies.Session.load(context, identifier);
		return result;
	}

	/**
	 * Set value of OAuthNonce_Session
	 * @param oauthnonce_session
	 */
	public final void setOAuthNonce_Session(system.proxies.Session oauthnonce_session)
	{
		setOAuthNonce_Session(getContext(), oauthnonce_session);
	}

	/**
	 * Set value of OAuthNonce_Session
	 * @param context
	 * @param oauthnonce_session
	 */
	public final void setOAuthNonce_Session(com.mendix.systemwideinterfaces.core.IContext context, system.proxies.Session oauthnonce_session)
	{
		if (oauthnonce_session == null)
			getMendixObject().setValue(context, MemberNames.OAuthNonce_Session.toString(), null);
		else
			getMendixObject().setValue(context, MemberNames.OAuthNonce_Session.toString(), oauthnonce_session.getMendixObject().getId());
	}

	/**
	 * @return the IMendixObject instance of this proxy for use in the Core interface.
	 */
	public final com.mendix.systemwideinterfaces.core.IMendixObject getMendixObject()
	{
		return oAuthNonceMendixObject;
	}

	/**
	 * @return the IContext instance of this proxy, or null if no IContext instance was specified at initialization.
	 */
	public final com.mendix.systemwideinterfaces.core.IContext getContext()
	{
		return context;
	}

	@java.lang.Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;

		if (obj != null && getClass().equals(obj.getClass()))
		{
			final email_connector.proxies.OAuthNonce that = (email_connector.proxies.OAuthNonce) obj;
			return getMendixObject().equals(that.getMendixObject());
		}
		return false;
	}

	@java.lang.Override
	public int hashCode()
	{
		return getMendixObject().hashCode();
	}

	/**
	 * @return String name of this class
	 */
	public static java.lang.String getType()
	{
		return "Email_Connector.OAuthNonce";
	}

	/**
	 * @return String GUID from this object, format: ID_0000000000
	 * @deprecated Use getMendixObject().getId().toLong() to get a unique identifier for this object.
	 */
	@java.lang.Deprecated
	public java.lang.String getGUID()
	{
		return "ID_" + getMendixObject().getId().toLong();
	}
}
