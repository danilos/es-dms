package test.github.richardwilly98.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;

import com.github.richardwilly98.api.Permission;
import com.github.richardwilly98.api.Role;
import com.github.richardwilly98.api.User;
import com.github.richardwilly98.api.exception.ServiceException;
import com.github.richardwilly98.api.services.DocumentService;
import com.github.richardwilly98.api.services.PermissionService;
import com.github.richardwilly98.api.services.RoleService;
import com.github.richardwilly98.api.services.UserService;
import com.google.inject.Inject;

/*
 * Base class for all test providers
 */
@Guice(modules = ProviderModule.class)
abstract class ProviderTestBase {

	Logger log = Logger.getLogger(this.getClass());
	final List<User> users = new ArrayList<User>();
	final List<Permission> permissions = new ArrayList<Permission>();
	final Set<Role> roles = new HashSet<Role>();

	@Inject
	UserService userService;

	@Inject
	DocumentService documentService;

	@Inject
	RoleService roleService;

	@Inject
	PermissionService permissionService;

	@BeforeSuite
	public void beforeSuite() throws Exception {
		createPermissions();
		createRoles();
		createUsers();
	}

	private void createUsers() {
		try {
		users.add(createUser("danilo", "Test use", false, "danilo@danilo.local", roles));
		for (User user : users) {
			userService.create(user);
		}
		} catch (ServiceException ex) {
			Assert.fail("createUsers failed", ex);
		}
	}

	private void createRoles() {
		try {
			Map<String, Permission> p = new HashMap<String, Permission>();
			for (Permission permission : permissions) {
				p.put(permission.getName(), permission);
			}
			
			roles.add(createRole("collaborator", "Collaborator", false, p));
			for (Role role : roles) {
				roleService.create(role);
			}
		} catch (ServiceException ex) {
			Assert.fail("createRoles failed", ex);
		}
	}

	private void createPermissions() {
		try {
			permissions.add(createPermission("document:create",
					"Create document", false, null));
			permissions.add(createPermission("document:delete",
					"Delete document", false, null));
			for (Permission permission : permissions) {
				permissionService.create(permission);
			}
		} catch (ServiceException ex) {
			Assert.fail("createPermissions failed", ex);
		}
	}

	@BeforeClass
	public void setupServer() {
	}

	@AfterClass
	public void closeServer() {
	}

	Permission createPermission(String name, String description,
			boolean disabled, Object property) throws ServiceException {
		Assert.assertTrue(! (name == null || name.isEmpty()));
		Permission permission = new Permission();
		String id = String.valueOf(name);
		permission.setId(id);
		permission.setName(name);
		permission.setDescription(description);
		permission.setDisabled(disabled);
		permission.setProperty(property);
		try {
			Permission newPermission = permissionService.create(permission);
			Assert.assertNotNull(newPermission);
			Assert.assertEquals(id, newPermission.getId());
			return newPermission;
		} catch (ServiceException e) {
			log.error("createPermission failed", e);
			throw e;
		}
	}

	User createUser(String name, String description, boolean disabled,
			String email, Set<Role> roles) throws ServiceException {
		Assert.assertTrue(! (name == null || name.isEmpty()));
		User user = new User();
		String id = String.valueOf(name);
		user.setId(id);
		user.setName(name);
		user.setDescription(description);
		user.setDisabled(disabled);
		user.setEmail(email);
		try {
			User newUser = userService.create(user);
			Assert.assertNotNull(newUser);
			Assert.assertEquals(id, newUser.getId());
			return newUser;
		} catch (ServiceException e) {
			log.error("createPermission failed", e);
			throw e;
		}
	}

	Role createRole(String name, String description, /*
													 * Set<Permission>
													 * permissions,
													 */
			boolean disabled, Map<String, Permission> permissions)
			throws ServiceException {
		Assert.assertTrue(! (name == null || name.isEmpty()));
		Role role = new Role();
		String id = String.valueOf(name);
		role.setId(id);
		role.setName(name);
		role.setDescription(description);
		role.setDisabled(disabled);
		role.setPermissions(permissions);
		try {
			Role newRole = roleService.create(role);
			Assert.assertNotNull(newRole);
			Assert.assertEquals(id, newRole.getId());
			return newRole;
		} catch (ServiceException e) {
			log.error("createPermission failed", e);
			throw e;
		}
	}
}