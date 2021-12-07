package me.protocol;

public class ProtocolMain {


    public ProtocolMain() {
    }

    public static void main(String[] args) {
        TCPConnection tcpConnection = new TCPConnection(80);
        tcpConnection.openConnection();
    }
}
