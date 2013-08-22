package com.tabtonic.kerberosAREA;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestKerbAuth {
	/**
	 * for log output
	 */
	final Logger logger = LoggerFactory.getLogger(TestKerbAuth.class);

	@BeforeClass
	public static void setUp() throws Exception {
		Properties prop = new Properties();
		InputStream in = TestKerbAuth.class.getResourceAsStream("/auth.properties");
		try {
			prop.load(in);
		} catch (IOException e1) {
			fail("Failed to load authorization properties file (auth.properties)");
		}
		//We need to convert the auth.login.config value before passing it to JAAS
		String loginConfigFile=prop.getProperty("java.security.auth.login.config");
		String loginConfigFileExForm = TestKerbAuth.class.getResource(loginConfigFile).toExternalForm();
		prop.setProperty("java.security.auth.login.config", loginConfigFileExForm);
		
		

		//iterate over the kerberos properties and add them to the system properties
		Enumeration<Object> propenum = prop.keys();
		while(propenum.hasMoreElements()){
			String property = (String) propenum.nextElement();
			System.setProperty(property, prop.getProperty(property));
		}
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGoodAuth() {
		String userName = "testUser";
		char[] password = "12345".toCharArray();
		LoginContext lc;
		try {
			lc = new LoginContext("primaryLoginContext", new UAndPCallbackHandler(userName, password));
			logger.debug("testGoodAuth: Attempting Login");
			lc.login();
			logger.debug("testGoodAuth: Authorization Success");
		} catch (LoginException e) {
			logger.error("testGoodAuth: Authorization Failure");
			e.printStackTrace();
			fail("Expected Authorization Failure");
		}			
	}
	@Test
	public void testBadAuth() {
		String userName = "testUser";
		char[] password = "abcde".toCharArray();
		LoginContext lc;
		try {
			lc = new LoginContext("primaryLoginContext", new UAndPCallbackHandler(userName, password));
			logger.debug("testBadAuth: Attempting Login");
			lc.login();
			logger.error("testBadAuth: Authorization Success");
			fail("Unexpected Authorization Success");
		} catch (LoginException e) {
			logger.debug("testBadAuth: Authorization Failure");			
		}			
	}
}
