package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * custom tomcat
 */
public class CustomWebApplicationServer {

	private final int port;

	private final ExecutorService executorService = Executors.newFixedThreadPool(10);

	private static final Logger logger = LoggerFactory.getLogger(CustomWebApplicationServer.class);

	public CustomWebApplicationServer(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		// 해당하는 포트로 서버를 만든다.
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			logger.info("[CustomWebApplicationServer] started {} port.", port);

			Socket clientSocket;
			logger.info("[CustomWebApplicationServer] waiting for client.");

			// 서버 소켓이 클라이언트를 기다린다.
			// 클라이언트 소켓이 만들어지면, while 문이 실행된다. --> 클라이언트가 연결됐다.
			while ((clientSocket = serverSocket.accept()) != null) {
				logger.info("[CustomWebApplicationServer] client connected!");

				/**
				 * Step 3. Thread Pool 을 적용해서 안정적인 서비스가 가능하도록 한다.
				 */
				executorService.execute(new ClientRequestHandler(clientSocket));
			}
		}
	}
}
