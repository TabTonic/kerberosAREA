/**
 *    Copyright (C) 2013 TabTonic LLC
 * 
 *    This file is part of kerberosAREA.
 *
 *    kerberosAREA is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    kerberosAREA is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with kerberosAREA.  If not, see <http://opensource.org/licenses/gpl-3.0.html>.
 * 
 */
	
package com.tabtonic.kerberosAREA;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.pluginsvr.plugins.AREAPlugin;
import com.bmc.arsys.pluginsvr.plugins.AREAResponse;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.arsys.pluginsvr.plugins.ARPluginInfo;

/**
 * <h4>kerberosAREA</h4>
 * This is an AREA plugin designed for use within the BMC Remedy Action Request System.
 * <br>AREA plugins are a used for delegating authentication responsibility to third party tools.  This plugin receives 
 * authentication credentials and passes them to MIT Kerberos for validation.
 * 
 * @author      Steve Kallestad, <a href="http://www.tabtonic.com/">TabTonic LLC</a>
 *
 * @version     0.0.1-SNAPSHOT      
 */
public class kerberosAREA extends AREAPlugin {
	/**
	 * for log output
	 */
	final Logger logger = LoggerFactory.getLogger(kerberosAREA.class);
	private ARPluginInfo pluginInfo = new ARPluginInfo("kerberosAREA", this);
	private static HashMap<String, Integer> trustedIPs = new HashMap<String, Integer>();
	/**
	 * Constructor
	 */
	public kerberosAREA(){
		
	}
	
	/**
	 * The initialize function is called one time on system startup.
	 * 
	 * It is being utilized to load configuration parameters.
	 * 
	 * @param context
	 * @throws ARException
	 */
	@Override
	public void initialize(ARPluginContext context) throws ARException {
		context.logMessage(pluginInfo, ARPluginContext.PLUGIN_LOG_LEVEL_INFO, "Initializing kerberosAREA");


		Properties prop = new Properties();
		
		InputStream in = getClass().getResourceAsStream("/kerberosAREA.config");
		try {
			prop.load(in);
		} catch (IOException e1) {
			logger.error("Failed to load configuration file: kerberosAREA.config");
		}
		//We need to convert the auth.login.config value before passing it to JAAS
		String loginConfigFile=prop.getProperty("java.security.auth.login.config");
		String loginConfigFileExForm = getClass().getResource(loginConfigFile).toExternalForm();
		prop.setProperty("java.security.auth.login.config", loginConfigFileExForm);
		
		

		//iterate over the kerberos properties and add them to the system properties
		Enumeration<Object> propenum = prop.keys();
		while(propenum.hasMoreElements()){
			String property = (String) propenum.nextElement();
			if(property.startsWith("java.security") ||
			   property.startsWith("sun.security")){
				System.setProperty(property, prop.getProperty(property));
			} //add any trusted ip addresses to the hashmap
			else if(property.startsWith("trusted")) {
				trustedIPs.put(prop.getProperty(property), 1);
			}
		}
		
		
		super.initialize(context);
	}
	
	/**
	 * The AR Server will check with the AREA plugin to determine if user information has expired.
		 it does this when it receives a second authentication request at least five minutes after the
		 previous authentication request.
		
		 We always return true in this instance, invalidating any existing user cache info.
	 * @param context
	 * @return
	 * @throws ARException
	 */
	@Override
	public boolean areaNeedSync(ARPluginContext context) throws ARException {
		 
		return true;
	}

	/**
	 * The areaVerifyLogin function is called by Remedy with the following parameters.  
	 * 
	 * We return an AREAResponse object which will tell remedy if the authentication is a success or a failure.
	 * 
	 * @param context
	 * @param user
	 * @param password
	 * @param networkAddress
	 * @param authString
	 * @return
	 * @throws ARException
	 */
	@Override
	public AREAResponse areaVerifyLogin(ARPluginContext context, String user,
			String password, String networkAddress, String authString) throws ARException {
		logger.trace("Verify login for {} from {}", user, networkAddress);

		//we need to return an AREAResponse object
		AREAResponse response = new AREAResponse();
		
		//for trusted IPs (i.e. midtier), accept the given login
		if(trustedIPs.containsKey(networkAddress)){
			response.setLoginStatus(AREAResponse.AREA_LOGIN_SUCCESS);
			return response;
		}

		
		LoginContext lc;
		try {
			lc = new LoginContext("primaryLoginContext", new UAndPCallbackHandler(user, password.toCharArray()));
			//verify login credentials	
			lc.login();
			response.setLoginStatus(AREAResponse.AREA_LOGIN_SUCCESS);
		} catch (LoginException e) {
			response.setLoginStatus(AREAResponse.AREA_LOGIN_FAILED);
			logger.error("Authorization Failure for user {}", user);
		}			
		

		return response;
		
	}
}
