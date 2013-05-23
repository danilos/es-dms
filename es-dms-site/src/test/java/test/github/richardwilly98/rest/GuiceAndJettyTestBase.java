package test.github.richardwilly98.rest;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;

import test.github.richardwilly98.inject.TestEsClientModule;
import test.github.richardwilly98.web.TestRestGuiceServletConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.richardwilly98.api.Credential;
import com.github.richardwilly98.api.services.UserService;
import com.github.richardwilly98.rest.RestAuthencationService;
import com.google.inject.Inject;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/*
 * TODO: Investigate why SSL does not work.
 */
@Guice(modules = TestEsClientModule.class)
public class GuiceAndJettyTestBase {

	protected final Logger log = Logger.getLogger(this.getClass());
	protected final static Credential adminCredential = new Credential(
			UserService.DEFAULT_ADMIN_LOGIN, UserService.DEFAULT_ADMIN_PASSWORD);
	private final Server server;
	final static ObjectMapper mapper = new ObjectMapper();
	protected static String adminToken;
	protected static Cookie adminCookie;
	private final Client restClient;
	// private final Client securedClient;
	private static final int HTTP_PORT = 8081;
	private static final int HTTPS_PORT = 50443;

	@Inject org.elasticsearch.client.Client client;

	GuiceAndJettyTestBase() throws Exception {
		server = new Server(HTTP_PORT);
		// Connector secureConnector = createSecureConnector();
		// server.setConnectors(new Connector[] {secureConnector});
		restClient = Client.create(new DefaultClientConfig(
				JacksonJaxbJsonProvider.class));
		// securedClient = createSecuredClient();
	}

	// private Client createSecuredClient() throws Exception {
	// TrustManager[ ] certs = new TrustManager[ ] {
	// new X509TrustManager() {
	// @Override
	// public X509Certificate[] getAcceptedIssuers() {
	// return null;
	// }
	// @Override
	// public void checkServerTrusted(X509Certificate[] chain, String authType)
	// throws CertificateException {
	// }
	// @Override
	// public void checkClientTrusted(X509Certificate[] chain, String authType)
	// throws CertificateException {
	// }
	// }
	// };
	//
	// ClientConfig config = new DefaultClientConfig();
	// SSLContext ctx = SSLContext.getInstance("SSL");
	// ctx.init(null, certs, new SecureRandom());
	// config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new
	// HTTPSProperties(new HostnameVerifier() {
	// @Override
	// public boolean verify(String hostname, SSLSession session) {
	// return true;
	// }
	// }, ctx));
	// return Client.create(config);
	// }

	@BeforeSuite
	public void initTestContainer() throws Exception {
		log.info("*** initTestContainer ***");
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		webAppContext.setResourceBase("src/test/webapp/");
		webAppContext.setParentLoaderPriority(true);
		webAppContext.addEventListener(new TestRestGuiceServletConfig());
		webAppContext.addFilter(GuiceFilter.class, "/*", null);

		server.setHandler(webAppContext);
		server.start();
		loginAdminUser();
	}

	// private Connector createSecureConnector() {
	// SslSocketConnector connector = new SslSocketConnector();
	// connector.setPort(HTTPS_PORT);
	// connector.setKeystore(".keystore");
	// connector.setKeyPassword("secret");
	// return connector;
	// }

	protected URI getBaseURI(boolean secured) {
		if (secured) {
			return UriBuilder.fromUri("https://localhost/").port(HTTPS_PORT)
					.build();
		} else {
			return UriBuilder.fromUri("http://localhost/").port(HTTP_PORT)
					.build();
		}
	}

	/**
	 * Create a web resource whose URI refers to the base URI the Web
	 * application is deployed at.
	 * 
	 * @return the created web resource
	 */
	protected WebResource resource() {
		return restClient.resource(getBaseURI(false));
	}

	// public WebResource securedResource() {
	// return securedClient.resource(getBaseURI(true));
	// }

	/**
	 * Get the client that is configured for this test.
	 * 
	 * @return the configured client.
	 */
	protected Client client() {
		return restClient;
	}

	private void loginAdminUser() {
		try {
			log.debug("*** loginAdminUser ***");
			adminCookie = login(adminCredential);
			Assert.assertNotNull(adminCookie);
			adminToken = adminCookie.getValue();
			Assert.assertNotNull(adminToken);
		} catch (Throwable t) {
			log.error("loginAdminUser failed", t);
			Assert.fail("loginAdminUser failed", t);
		}
	}

	protected Cookie login(Credential credential) {
		try {
			log.debug("*** login ***");
			WebResource webResource = resource().path("auth").path("login");
			log.debug(webResource);
			ClientResponse response = webResource.type(
					MediaType.APPLICATION_JSON).post(ClientResponse.class,
					credential);
			log.debug("status: " + response.getStatus());
			Assert.assertTrue(response.getStatus() == Status.OK.getStatusCode());
			for (NewCookie cookie : response.getCookies()) {
				if (RestAuthencationService.ES_DMS_TICKET.equals(cookie
						.getName())) {
					return new Cookie(cookie.getName(), cookie.getValue());
				}
			}
		} catch (Throwable t) {
			log.error("login failed", t);
			Assert.fail("login failed", t);
		}
		return null;
	}

	protected void logout(Cookie cookie) {
		log.debug("*** logout ***");
		checkNotNull(cookie);
		WebResource webResource = resource().path("auth").path("logout");
		ClientResponse response = webResource.cookie(cookie).post(
				ClientResponse.class);
		log.debug("status: " + response.getStatus());
		Assert.assertTrue(response.getStatus() == Status.OK.getStatusCode());
	}

	private void logoutAdminUser() {
		try {
			log.debug("*** logoutAdminUser ***");
			logout(adminCookie);
		} catch (Throwable t) {
			Assert.fail("logoutAdminUser failed", t);
		}
	}

	@AfterSuite
	public void tearDownTestContainer() throws Exception {
		log.info("*** tearDownTestContainer ***");
		logoutAdminUser();
		server.stop();
		tearDownElasticsearch();
	}
	
	private void tearDownElasticsearch() throws Exception {
		log.info("*** tearDownElasticsearch ***");
		client.admin().indices().prepareDelete().execute().actionGet();
		client.close();
	}
}