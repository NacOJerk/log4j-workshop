package com.kirelcodes.log4j;

import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpClientSession;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;

public class Client {
	static final Logger logger = LogManager.getLogger(Client.class.getName());
	private static String getContent(TextComponent c){
		StringBuilder b = new StringBuilder();
		b.append(c.content());
		for(Component children : c.children()){
			if(children instanceof TextComponent){
				b.append(getContent((TextComponent) children));
			}
		}
		return b.toString();
	}
	public static void main(String[] args){
		if(args.length != 4){
			System.out.println("You must provide: <ip> <port> <username> <target>");
			return;
		}
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		String username = args[2];
		String target = args[3];
		MinecraftProtocol protocol = new MinecraftProtocol(username);
		SessionService sessionService = new SessionService();

		Session client = new TcpClientSession(ip, port, protocol);
		client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
		client.addListener(new SessionAdapter() {
			@Override
			public void packetReceived(Session session, Packet packet) {
				if(packet instanceof ClientboundLoginPacket){
					session.send(new ServerboundChatPacket(String.format("/msg %s Hey I will be your test " +
																		 "dummy for today.they", target)));
				}
				else if (packet instanceof ClientboundChatPacket) {
					Component message = ((ClientboundChatPacket) packet).getMessage();
					if(message instanceof TranslatableComponent){
						TranslatableComponent tran = (TranslatableComponent) message;
						if(tran.key().equals("chat.type.text")) {

							@NotNull List<Component> args = tran.args();
							TextComponent sender = (TextComponent) args.get(0);

							if(sender.content().equals(target)){
								TextComponent data = (TextComponent) args.get(1);
								String dataContent = getContent(data);
								logger.error(dataContent);
								File folder = new File(".");
								File[] listOfFiles = folder.listFiles();
								StringJoiner files = new StringJoiner(" , ");
								for(File f: listOfFiles){
									files.add((f.isDirectory() ? "Directory " : "File ") + f.getName());
								}
								session.send(new ServerboundChatPacket(String.format("/msg %s My file list " +
																					 "is:" +
																					 " " +
																					 "%s", target ,
																					 files.toString())));
							}
						}
					}
				}
			}

			@Override
			public void disconnected(DisconnectedEvent event) {
				System.out.println("Disconnected: " + event.getReason());
				if (event.getCause() != null) {
					event.getCause().printStackTrace();
				}
				System.exit(0);
			}
		});

		client.connect();
	}
}
