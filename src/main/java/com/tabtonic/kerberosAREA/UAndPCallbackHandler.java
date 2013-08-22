package com.tabtonic.kerberosAREA;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UAndPCallbackHandler implements CallbackHandler {
	/**
	 * for log output
	 */
	final Logger logger = LoggerFactory.getLogger(UAndPCallbackHandler.class);
	
	private String _userName;
	private char[] _password;
	
	public UAndPCallbackHandler(String userName, char[] password){
		_userName = userName;
		_password = password;
	}
	
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		logger.trace("Callback Handler Handle called");
		for(Callback callback : callbacks){
			if(callback instanceof NameCallback && _userName != null){
				logger.trace("Name callback handler called for {}", _userName);
				((NameCallback) callback).setName(_userName);
			} else if (callback instanceof PasswordCallback && _password != null){
				logger.trace("Password callback handler called for {}", _userName);
				((PasswordCallback) callback).setPassword(_password);
			}
		}
	}
}
