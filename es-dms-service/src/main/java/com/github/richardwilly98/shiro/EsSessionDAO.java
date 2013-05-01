package com.github.richardwilly98.shiro;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import com.github.richardwilly98.api.ISession;
import com.github.richardwilly98.api.exception.ServiceException;
import com.github.richardwilly98.api.services.AuthenticationService;
import com.github.richardwilly98.services.AuthenticationProvider;
import com.google.inject.Inject;

public class EsSessionDAO extends AbstractSessionDAO {

	Logger log = Logger.getLogger(this.getClass());

	final AuthenticationService service;

	@Inject
	public EsSessionDAO(AuthenticationService service) {
		this.service = service;
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		try {
			if (session == null || session.getId() == null) {
				log.warn("Session id is null.");
				return;
			}
			ISession s = service.get(session.getId().toString());
			if (s != null) {
				if (session.getAttribute(AuthenticationProvider.ES_DMS_LOGIN_ATTRIBUTE) != null) {
					s.setUserId(session.getAttribute(AuthenticationProvider.ES_DMS_LOGIN_ATTRIBUTE).toString());
				}
				service.update(s);
			}
		} catch (ServiceException ex) {
			log.error("update failed", ex);
		}
	}

	@Override
	public void delete(Session session) {
		try {
			ISession s = service.get(session.getId().toString());
			if (s != null) {
				service.delete(s);
			}
		} catch (ServiceException ex) {
			log.error("delete failed", ex);
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		try {
			Set<com.github.richardwilly98.api.ISession> sessions = service
					.getItems("active:true");
			Set<Session> activeSessions = new HashSet<Session>();
			for (com.github.richardwilly98.api.ISession session : sessions) {
				activeSessions.add(new EsSession(session));
			}
			return activeSessions;
		} catch (ServiceException ex) {
			log.error("delete failed", ex);
		}
		return null;
	}

	@Override
	protected Serializable doCreate(Session session) {
		try {
			Serializable sessionId = generateSessionId(session);
			assignSessionId(session, sessionId);
			ISession s = new com.github.richardwilly98.api.Session();
			s.setId(sessionId.toString());
			s.setCreateTime(session.getStartTimestamp());
			s.setLastAccessTime(session.getLastAccessTime());
			s.setActive(true);
			s = service.create(s);
			EsSession esSession = new EsSession(s);
			return esSession.getId();
		} catch (ServiceException ex) {
			log.error("doCreate failed", ex);
		}
		return null;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		try {
			ISession session = service.get(sessionId.toString());
			if (session == null) {
				return null;
			}
			return new EsSession(session);
		} catch (ServiceException ex) {
			log.error("doReadSession failed", ex);
		}
		return null;
	}

}
