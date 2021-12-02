package me.protocol.connection;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Log4j
public class Connection extends Thread {
    private final Socket acceptSocket;

    private static final String OK = "HTTP/1.1 200 OK";

    public Connection(Socket acceptSocket) {
        this.acceptSocket = acceptSocket;
    }

    @SneakyThrows
    @Override
    public void run() {
        HTTPRequest request = new HTTPRequest(acceptSocket.getInputStream());
        log.log(Level.INFO, request.getHttpHeaders().toString());

        OutputStream outputStream = acceptSocket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream("web.html");
        outputStream.write(OK.getBytes(StandardCharsets.UTF_8));
        fileInputStream.transferTo(outputStream);

        outputStream.flush();
        outputStream.close();
        acceptSocket.close();
    }
}
