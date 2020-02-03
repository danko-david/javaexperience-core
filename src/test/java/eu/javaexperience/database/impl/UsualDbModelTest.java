package eu.javaexperience.database.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.database.annotations.Length;
import eu.javaexperience.database.annotations.Unique;
import eu.javaexperience.database.jdbc.Id;
import eu.javaexperience.database.pojodb.SqlDatabase;
import eu.javaexperience.time.TimeCalc;

public abstract class UsualDbModelTest
{
	public static class User extends UsualDbModel
	{
		@Id
		public int id;
		
		@Length(length = 64)
		public String visible_name;
		
		@Unique
		@Length(length = 64)
		public String email;
		
		public boolean disabled;

		public String extra_data;
		
		public Date created_at;
		
		@Override
		public String getTable()
		{
			return "user";
		}
	}

	public abstract SqlDatabase createConnection(boolean resetDatabase) throws Exception;
	
	@Test
	public void testCreateInsertDeleteFromTable() throws Exception
	{
		SqlDatabase db = createConnection(true);
		
		db.ensureTable(User.class);
		
		Date now = TimeCalc.setToDate(new Date(), -1, -1, -1, -1, -1, -1, 0);
		{
			User u = new User();
			u.visible_name = "Dankó Dávid";
			u.email = "info@dankodavid.hu";
			u.extra_data = "{\"born_time\":708306900000}";
			u.disabled = false;
			u.created_at = now;
			db.insert(u);
			
			assertEquals(1, u.id);
		}
		
		List<User> us = db.getWhere(User.class, "");
		
		assertEquals(1, us.size());
		
		assertEquals(1, us.get(0).id);
		assertEquals("Dankó Dávid", us.get(0).visible_name);
		assertEquals("info@dankodavid.hu", us.get(0).email);
		assertFalse(us.get(0).disabled);
		assertEquals("{\"born_time\":708306900000}", us.get(0).extra_data);
		assertEquals(now.getTime(), us.get(0).created_at.getTime());
		
		db.delete(us.get(0));
		assertTrue(db.getWhere(User.class, "").isEmpty());
	}
	
	@Test
	public void testInsertId() throws Exception
	{
		SqlDatabase db = createConnection(true);
		
		db.ensureTable(User.class);
		
		Date now = TimeCalc.setToDate(new Date(), -1, -1, -1, -1, -1, -1, 0);
		{
			User u = new User();
			u.id = 12;
			u.visible_name = "Dankó Dávid";
			u.email = "info@dankodavid.hu";
			u.extra_data = "{\"born_time\":708306900000}";
			u.disabled = false;
			u.created_at = now;
			db.insert(u);
			
			assertEquals(12, u.id);
		}
		
		List<User> us = db.getWhere(User.class, null);
		
		assertEquals(1, us.size());
		
		assertEquals(12, us.get(0).id);
		assertEquals("Dankó Dávid", us.get(0).visible_name);
		assertEquals("info@dankodavid.hu", us.get(0).email);
		assertFalse(us.get(0).disabled);
		assertEquals("{\"born_time\":708306900000}", us.get(0).extra_data);
		assertEquals(now.getTime(), us.get(0).created_at.getTime());
	}
	
	public static class UserUserGroup extends UsualDbModel
	{
		public int user;
		public int group;
		
		@Override
		public String getTable()
		{
			return "user__user_group";
		}
	}
	
	@Test
	public void testSaveSwitchTable() throws Exception
	{
		SqlDatabase db = createConnection(true);
		
		db.ensureTable(UserUserGroup.class);
		
		{
			UserUserGroup uug = new UserUserGroup();
			uug.user = 1;
			uug.group = 10;
			db.insert(uug);
		}
		
		{
			UserUserGroup uug = new UserUserGroup();
			uug.user = 1;
			uug.group = 1024;
			db.insert(uug);
		}
		
		List<UserUserGroup> uugs = db.getWhere(UserUserGroup.class, "");
		
		Set<Integer> grps = new HashSet<>();
		CollectionTools.convert(grps, uugs, (uug)->uug.group);
		
		assertTrue(grps.contains(10));
		assertTrue(grps.contains(1024));
	}
	
	public static class ExtendUser extends User
	{
		public Date last_login;
	}
	
	@Test
	public void testAlterTableAndUpdate() throws Exception
	{
		SqlDatabase db = createConnection(true);
		
		db.ensureTable(User.class);
		
		Date now = TimeCalc.setToDate(new Date(), -1, -1, -1, -1, -1, -1, 0);
		
		{
			User u = new User();
			u.visible_name = "Dankó Dávid";
			u.email = "info@dankodavid.hu";
			u.extra_data = "{\"born_time\":708306900000}";
			u.disabled = false;
			
			u.created_at = now;
			db.insert(u);
			assertEquals(1, u.id);
		}

		//alter table, get model
		
		Date login = TimeCalc.setToDate(new Date(), 2015, -1, -1, -1, -1, -1, 0);
		db.ensureTable(ExtendUser.class);
		
		{
			List<ExtendUser> us = db.getWhere(ExtendUser.class, "");
			
			assertEquals(1, us.size());
			ExtendUser eu = us.get(0);
			
			assertEquals(1, eu.id);
			assertEquals("Dankó Dávid", eu.visible_name);
			assertEquals("info@dankodavid.hu", eu.email);
			assertFalse(eu.disabled);
			assertEquals("{\"born_time\":708306900000}", eu.extra_data);
			assertEquals(now.getTime(), us.get(0).created_at.getTime());
			
			eu.last_login = login;
			db.updateById(eu);
		}
		
		
		//load again 
		{
			List<ExtendUser> us = db.getWhere(ExtendUser.class, "");
			
			assertEquals(1, us.size());
			ExtendUser eu = us.get(0);
			
			assertEquals(1, eu.id);
			assertEquals("Dankó Dávid", eu.visible_name);
			assertEquals("info@dankodavid.hu", eu.email);
			assertFalse(eu.disabled);
			assertEquals("{\"born_time\":708306900000}", eu.extra_data);
			assertEquals(now.getTime(), us.get(0).created_at.getTime());
			assertEquals(login.getTime(), us.get(0).last_login.getTime());
		}
	}
	
	//TODO alter table
	
	//TODO test String id
	
	//TODO model without id
	
	
	
	
	
	
	
	//TODO test @Ignore annotation
	
	//TODO compaund index
	
	//TODO test indexes are present
}
