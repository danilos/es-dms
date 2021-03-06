package com.github.richardwilly98.esdms.web;

import java.security.Principal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.github.richardwilly98.esdms.UserImpl;
import com.github.richardwilly98.esdms.api.Session;

public class EsSecurityContext implements SecurityContext {

	private final UserImpl user;
	private final Session session;

	public EsSecurityContext(Session session, UserImpl user) {
		this.session = session;
		this.user = user;
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

	@Override
	public Principal getUserPrincipal() {
		return user;
	}

	@Override
	public boolean isSecure() {
		return (null != session) ? session.isSecure() : false;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (null == session || !session.isActive()) {
			// Forbidden
			Response denied = Response.status(Response.Status.FORBIDDEN)
					.entity("Permission Denied").build();
			throw new WebApplicationException(denied);
		}

		try {
			// this user has this role?
			return user.getRoles().contains(role);
		} catch (Exception e) {
		}

		return false;
	}

}
