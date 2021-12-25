package com.kirelcodes.log4j;

public class App {
	public static void main(String args[]) throws Exception{
		System.out.println(String.format("Listening at: ldap://%s:%d and codebase is: ", args[0], Integer.parseInt( args[1]),
										 args[2]));
		Server.run(args[0], Integer.parseInt(args[1]), args[2]);
	}
}
