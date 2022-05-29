# Log4Shell Minecraft Workshop - Log4J Vurnabillity
This repostitory wraps all the code, information and credit for the log4shell workshop. It shows how to the log4j vurnabillity could be used to run code, specifically on minecraft clients

## Workshop details
The workshop aims to allow users to test out for themselves the log4j vurnabillity without having to deal with all the techincal details around it.
It contains the following things:
* LDAP server that is rigged to redirect to a controlled http server
* HTTP server that will return files uploaded to it and is running an ssh server on it in order to upload files
* Minecraft server that will deal with creating vulnerable bots for the user to try and attack
* Vurnable Client a simple minecraft bot that has a flag.txt in the main directory

The goal of the workshop is to receive the contents of the flag.txt file, that can be done via loggin into the minecraft server and sending the correct log4j payload with the correct java class.

When ever a user logs into the minecraft server a specialized bot will be summoned for him. The bot will only log things the user types into the chat (thus allowing multiple users in the workshop at the same time). The player can upload the payload class via sftp to the http client.

Server details:
* LDAP Server - Hosted at ```<server-ip>:8081``` will redirect all calls to the http server ```(ldap://<server-ip>:8081/[arg] -> http://<server-ip>:8080/[arg].class)```
* HTTP Server - Hosted at ```<server-ip>:8080```, has ssh enabled on port 2222 with the following login details:```username: file_uploader, password: file_uploader```
* Minecraft Server - Hosted at ```<server-ip>:25565```, the minecraft server itself is not supposed to be vulnerable to log4j.
 
## Setup Guide
In order to setup the workshop you must first build the vurnable client, go to the vurnable_client folder and run the following command:
```sh
docker build -t <image-name> .
```

Next you must setup the enviorment variables so the different services would know the IPs and names of the different containers.
First setup the SERVER-IP enviorment variable, it should contain the IP address of the server/host you are running the workshop on:
```sh
export SERVER_IP=<server-ip>
```

Next setup the CLIENT_IMAGE enviorment variable, it should contain the image-name of the vurnable_client
```sh
export CLIENT_IMAGE=<image-name>
```

Finally go back to the main folder and run docker-compose
```sh
docker-compose up
```

## Credits
In order to make this workshop I had to watch and read multiple resources online about the log4j vulnerability my favorite resources were:
* John Hammond's video - https://www.youtube.com/watch?v=7qoPDq41xhQ
* LiveOverflow's videos - https://www.youtube.com/watch?v=w2F67LbEtnk and https://www.youtube.com/watch?v=iI9Dz3zN4d8
* A Journey From JNDI/LDAP Manipulation to Remote Code Execution Dream Land - https://www.youtube.com/watch?v=Y8a5nB-vy78

I also used some code from the following website in order to setup the basis of the ldap server:
* https://www.programcreek.com/java-api-examples/?code=welk1n%2FJNDI-Injection-Exploit%2FJNDI-Injection-Exploit-master%2Fsrc%2Fmain%2Fjava%2Fjndi%2FLDAPRefServer.java

Finally I recommend reading about LDAP and Java Serialization attacks aswell.
  
## Disclaimer
The code in this workshop is intended for educational purposes only. Use it at your own risk.
