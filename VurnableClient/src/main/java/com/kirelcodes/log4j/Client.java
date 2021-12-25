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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
	static final Logger logger = LogManager.getLogger(Client.class.getName());

	public static void main(String[] args){
		MinecraftProtocol protocol = new MinecraftProtocol("Hey");
		SessionService sessionService = new SessionService();

		Session client = new TcpClientSession("127.0.0.1", 25565, protocol);
		client.setFlag(MinecraftConstants.SESSION_SERVICE_KEY, sessionService);
		client.addListener(new SessionAdapter() {
			@Override
			public void packetReceived(Session session, Packet packet) {
				if (packet instanceof ClientboundLoginPacket) {
					session.send(new ServerboundChatPacket("Hello, this is a test of MCProtocolLib."));
				} else if (packet instanceof ClientboundChatPacket) {
					Component message = ((ClientboundChatPacket) packet).getMessage();
					System.out.println("Received Message: " + message);
					logger.error(message);
				}
			}

			@Override
			public void disconnected(DisconnectedEvent event) {
				System.out.println("Disconnected: " + event.getReason());
				if (event.getCause() != null) {
					event.getCause().printStackTrace();
				}
			}
		});

		client.connect();
	}
}
