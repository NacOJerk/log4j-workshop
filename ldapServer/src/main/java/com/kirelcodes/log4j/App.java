package com.kirelcodes.log4j;

public class App {
	public static void main(String args[]) throws Exception{
		Server.run(args[0], Integer.parseInt(args[1]), args[2]);
	}
}
