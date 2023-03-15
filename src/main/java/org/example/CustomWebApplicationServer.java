package org.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.example.calculator.domain.Calculator;
import org.example.calculator.domain.PositiveNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * custom tomcat
 */
public class CustomWebApplicationServer {

	private final int port;

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
				 * Step 2. 사용자 요청이 들어올 때마다 Thread 를 새로 생성해서 사용자 요청을 처리하도록 한다.
				 * Thread 를 새로 생성할 때마다 독립적 stack 메모리를 할당 받는다. --> 성능이 매우 저하될 수 있
				 */
				new Thread(new ClientRequestHandler(clientSocket)).start();

			}
		}
	}
}
