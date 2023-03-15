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
				 * Step 1. 사용자 요청을 메인 Thread 가 처리하도록 한다.
				 */
				try (InputStream in = clientSocket.getInputStream(); OutputStream out = clientSocket.getOutputStream()) {
					// InputStream --> InputStreamReader --> BufferedReader (각 라인으로 읽도록)
					BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
					// OutputStream --> DataOutputStream
					DataOutputStream dos = new DataOutputStream(out);

					HttpRequest httpRequest = new HttpRequest(br);

					// GET /calculate?operand1=11&operator=*&operand2=55 HTTP/1.1
					if (httpRequest.isGetRequest() && httpRequest.matchPath("/calculate")) {
						QueryStrings queryStrings = httpRequest.getQueryString();

						int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
						String operator = queryStrings.getValue("operator");
						int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));

						int result = Calculator.calculate(new PositiveNumber(operand1), operator, new PositiveNumber(operand2));
						byte[] body = String.valueOf(result).getBytes();

						HttpResponse response = new HttpResponse(dos);
						response.response200Header("application/json", body.length);
						response.responseBody(body);
					}
				}
			}
		}
	}
}
