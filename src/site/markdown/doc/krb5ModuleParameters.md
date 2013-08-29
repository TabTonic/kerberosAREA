Referenced from the [JavaSE6 documentation](http://docs.oracle.com/javase/6/docs/jre/api/security/jaas/spec/com/sun/security/auth/module/Krb5LoginModule.html).

 The following is a list of configuration options supported for Krb5LoginModule:

**refreshKrb5Config:**
    Set this to true, if you want the configuration to be refreshed before the login method is called.

**useTicketCache:**
    Set this to true, if you want the TGT to be obtained from the ticket cache. Set this option to false if you do not want this module to use the ticket cache. (Default is False). This module will search for the tickect cache in the following locations: For Windows 2000, it will use Local Security Authority (LSA) API to get the TGT. On Solaris and Linux it will look for the ticket cache in /tmp/krb5cc_uid where the uid is numeric user identifier. If the ticket cache is not available in either of the above locations, or if we are on a different Windows platform, it will look for the cache as {user.home}{file.separator}krb5cc_{user.name}. You can override the ticket cache location by using ticketCache

**ticketCache:**
    Set this to the name of the ticket cache that contains user's TGT. If this is set, useTicketCache must also be set to true; Otherwise a configuration error will be returned.

**renewTGT:**
    Set this to true, if you want to renew the TGT. If this is set, useTicketCache must also be set to true; otherwise a configuration error will be returned.

**doNotPrompt:**
    Set this to true if you do not want to be prompted for the password if credentials can not be obtained from the cache or keytab.(Default is false) If set to true authentication will fail if credentials can not be obtained from the cache or keytab.

**useKeyTab:**
    Set this to true if you want the module to get the principal's key from the the keytab.(default value is False) If keyatb is not set then the module will locate the keytab from the Kerberos configuration file. If it is not specifed in the Kerberos configuration file then it will look for the file {user.home}{file.separator}krb5.keytab.

**keyTab:**
    Set this to the file name of the keytab to get principal's secret key.

**storeKey:**
    Set this to true to if you want the principal's key to be stored in the Subject's private credentials. 

**principal:**
    The name of the principal that should be used. The principal can be a simple username such as "testuser" or a service name such as "host/testhost.eng.sun.com". You can use the principal option to set the principal when there are credentials for multiple principals in the keyTab or when you want a specific ticket cache only. The principal can also be set using the system property sun.security.krb5.principal. In addition, if this system property is defined, then it will be used. If this property is not set, then the principal name from the configuration will be used.

**isInitiator:**
    Set this to true, if initiator. Set this to false, if acceptor only. (Default is true). Note: Do not set this value to false for initiators. 

 This LoginModule also recognizes the following additional Configuration options that enable you to share username and passwords across different authentication modules:


**useFirstPass**   if, true, this LoginModule retrieves the
               username and password from the module's shared state,
               using "javax.security.auth.login.name" and
               "javax.security.auth.login.password" as the respective
               keys. The retrieved values are used for authentication.
               If authentication fails, no attempt for a retry
               is made, and the failure is reported back to the
               calling application.

**tryFirstPass**   if, true, this LoginModule retrieves the
               the username and password from the module's shared
               state using "javax.security.auth.login.name" and
               "javax.security.auth.login.password" as the respective
               keys.  The retrieved values are used for
               authentication.
               If authentication fails, the module uses the
               CallbackHandler to retrieve a new username
               and password, and another attempt to authenticate
               is made. If the authentication fails, 
               the failure is reported back to the calling application

**storePass**      if, true, this LoginModule stores the username and
               password obtained from the CallbackHandler in the
               modules shared state, using 
               "javax.security.auth.login.name" and 
               "javax.security.auth.login.password" as the respective
               keys.  This is not performed if existing values already
               exist for the username and password in the shared
               state, or if authentication fails.

**clearPass**     if, true, this LoginModule clears the
              username and password stored in the module's shared
              state  after both phases of authentication
              (login and commit)  have completed.
 