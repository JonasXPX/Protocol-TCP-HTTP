package me.protocol;

import lombok.extern.log4j.Log4j;
import me.protocol.connection.Connection;
import org.apache.log4j.Level;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.String.format;

@Log4j
public record TCPConnection(int port) {

    public void openConnection() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.log(Level.INFO, format("Server is running at port %s", port));
            Socket accept;
            while ((accept = serverSocket.accept()) != null) {
                new Connection(accept).start();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
