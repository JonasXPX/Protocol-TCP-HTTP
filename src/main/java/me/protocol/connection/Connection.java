package me.protocol.connection;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Level;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@Log4j
public class Connection extends Thread {

    private final Socket acceptSocket;

    private static final String OK = "HTTP/1.1 200 OK";
    private static final String NOT_FOUND = "HTTP/1.1 404 Not Found";
    private static final String INTERNAL_ERROR = "HTTP/1.1 500 Internal Server Error";
    private static final String DEFAULT_PATH_FILE = "web.html";

    public Connection(Socket acceptSocket) {
        this.acceptSocket = acceptSocket;
    }

    @Override
    public void run() {
        try {
            HTTPRequest request = new HTTPRequest(acceptSocket.getInputStream());
            if (request.getMethod() == null) {
                acceptSocket.close();
                return;
            }

            acceptSocket.setKeepAlive(true);
            acceptSocket.setSoTimeout(1000);
            log.log(Level.INFO, format("Received: %s to %s", request.getMethod(), request.getPath()));

            OutputStream outputStream = acceptSocket.getOutputStream();

            String filePath = request.getPath().substring(2);

            if ("".equals(filePath)) {
                filePath = DEFAULT_PATH_FILE;
            }

            InputStream webFile = getClass()
                    .getClassLoader()
                    .getResourceAsStream(filePath);

            if (webFile != null) {
                log.log(Level.INFO, format("resource length: %s", webFile.available()));
            }

            if (isNull(webFile)) {
                outputStream.write(NOT_FOUND.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
                return;
            }

            log.log(Level.DEBUG, request.getPath());

            outputStream.write(OK.getBytes(StandardCharsets.UTF_8));
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            webFile.transferTo(outputStream);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            createInternalServerErrorOutput();
            throw new RuntimeException(e);
        } finally {
            try {
                acceptSocket.close();
            } catch (IOException e) {
                log.error("Failed to close socket connection", e);
            }
        }
    }


    private void createInternalServerErrorOutput() {
        if (!acceptSocket.isConnected()) {
            return;
        }

        try {
            acceptSocket.getOutputStream().write(INTERNAL_ERROR.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
