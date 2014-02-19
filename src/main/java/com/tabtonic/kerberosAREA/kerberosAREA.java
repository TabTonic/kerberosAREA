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
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

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
	private static final Logger logger = LoggerFactory.getLogger(kerberosAREA.class);
	
	private ARPluginInfo pluginInfo = new ARPluginInfo(Messages.getString("kerberosAREA.pluginInfo"), this); //$NON-NLS-1$
	private static ConcurrentHashMap<String, Integer> trustedIPs = new ConcurrentHashMap<String, Integer>();
	private ConcurrentHashMap<String, Integer> instanceTrustedIPs = new ConcurrentHashMap<String, Integer>();
	/**
	 * Constructor
	 */
	public kerberosAREA(){
		logger.trace(Messages.getString("kerberosAREA.log.KAConstructor")); //$NON-NLS-1$
		if(trustedIPs.size() > 0){
			logger.trace(Messages.getString("kerberosAREA.log.addingTrustedIPs")); //$NON-NLS-1$
			instanceTrustedIPs.putAll(trustedIPs);
		}
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
		context.logMessage(pluginInfo, ARPluginContext.PLUGIN_LOG_LEVEL_INFO, Messages.getString("kerberosAREA.log.KAInit")); //$NON-NLS-1$
		logger.trace(Messages.getString("kerberosAREA.log.KAInit")); //$NON-NLS-1$

		Properties prop = new Properties();
		
		logger.trace(Messages.getString("kerberosAREA.log.loadingConfig")); //$NON-NLS-1$
		InputStream in = getClass().getResourceAsStream(Messages.getString("kerberosAREA.configFile")); //$NON-NLS-1$
		try {
			prop.load(in);
		} catch (IOException e1) {
			logger.error(Messages.getString("kerberosAREA.log.configFileLoadFailure"), e1.getMessage()); //$NON-NLS-1$
		}
		
		//We need to convert the auth.login.config value before passing it to JAAS
		String loginConfigFile=prop.getProperty(Messages.getString("kerberosAREA.jsa.loginConfig")); //$NON-NLS-1$
		String loginConfigFileExForm = getClass().getResource(loginConfigFile).toExternalForm();
		prop.setProperty(Messages.getString("kerberosAREA.jsa.loginConfig"), loginConfigFileExForm); //$NON-NLS-1$
		
		

		//iterate over the kerberos properties and add them to the system properties
		Enumeration<Object> propenum = prop.keys();
		while(propenum.hasMoreElements()){
			String property = (String) propenum.nextElement();
			if(property.startsWith(Messages.getString("kerberosAREA.java.security")) || //$NON-NLS-1$
			   property.startsWith(Messages.getString("kerberosAREA.sun.security"))){ //$NON-NLS-1$
				logger.trace(Messages.getString("kerberosAREA.log.setSystemProperty"), property, prop.getProperty(property)); //$NON-NLS-1$
				System.setProperty(property, prop.getProperty(property));
			} //add any trusted ip addresses to the hashmap
			else if(property.startsWith(Messages.getString("kerberosAREA.TRUSTED"))) { //$NON-NLS-1$
				logger.trace(Messages.getString("kerberosAREA.log.TrustingAuthInfo"), prop.getProperty(property)); //$NON-NLS-1$
				trustedIPs.put(prop.getProperty(property), 1);
			}
		}
		
		instanceTrustedIPs.putAll(trustedIPs);
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
		logger.trace(Messages.getString("kerberosAREA.log.areaNeedSyncCalled"), context.getUser());		 //$NON-NLS-1$
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
		logger.trace(Messages.getString("kerberosAREA.log.verifyLogin"), user, networkAddress); //$NON-NLS-1$
		if(instanceTrustedIPs.size() == 0 && trustedIPs.size() > 0){
			instanceTrustedIPs.putAll(trustedIPs);
		}
		//we need to return an AREAResponse object
		AREAResponse response = new AREAResponse();
		
		if(null != networkAddress && !(networkAddress.isEmpty())){
			//for trusted IPs (i.e. midtier), accept the given login
			if(instanceTrustedIPs.containsKey(networkAddress)){
				logger.trace(Messages.getString("kerberosAREA.log.AddressIsTrusted"),networkAddress,user ); //$NON-NLS-1$
				response.setLoginStatus(AREAResponse.AREA_LOGIN_SUCCESS);			
				return response;
			}
			
			logger.trace(Messages.getString("kerberosAREA.log.AddressIsNotTrusted")); //$NON-NLS-1$
		}
		LoginContext lc = null;
		try {
			logger.trace(Messages.getString("kerberosAREA.log.callingCallbackHandler")); //$NON-NLS-1$
			lc = new LoginContext(Messages.getString("kerberosAREA.PRIMARYLOGINCONTEXT"), new UAndPCallbackHandler(user, password.toCharArray())); //$NON-NLS-1$
			//verify login credentials	
			logger.trace(Messages.getString("kerberosAREA.log.testingLogin")); //$NON-NLS-1$
			lc.login();
			logger.trace(Messages.getString("kerberosAREA.log.SuccessfulAuth")); //$NON-NLS-1$
			response.setLoginStatus(AREAResponse.AREA_LOGIN_SUCCESS);
			
		} catch (LoginException e) {
			logger.error(Messages.getString("kerberosAREA.log.FailedAuth"), user); //$NON-NLS-1$
			response.setLoginStatus(AREAResponse.AREA_LOGIN_FAILED);
		}			
		if(null != lc){
			logger.trace(Messages.getString("kerberosAREA.log.executeLogOut")); //$NON-NLS-1$
			try {
				lc.logout();
				logger.trace(Messages.getString("kerberosAREA.log.clearLoginContext")); //$NON-NLS-1$
				lc = null;
			} catch (LoginException e) {
				logger.debug(Messages.getString("kerberosAREA.log.logoutFailed"), e.getMessage()); //$NON-NLS-1$
			}
		}
		
		logger.trace(Messages.getString("kerberosAREA.log.ReturningResponse")); //$NON-NLS-1$
		return response;
		
	}
}
