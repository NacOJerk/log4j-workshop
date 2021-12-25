package com.kirelcodes.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
	static final Logger logger = LogManager.getLogger(Client.class.getName());

	public static void main(String[] args){
		logger.error(args[1]);
	}
}
