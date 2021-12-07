package me.protocol.connection;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

    private final InputStream inputStream;

    private final StringBuilder request;

    @Getter
    private String path;

    @Getter
    private HTTPMethod method;

    @Getter
    private final Map<String, String> httpHeaders;

    public HTTPRequest(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        request = translateInput();
        httpHeaders = translateHeaders();
        translateStartLine();
    }

    private void translateStartLine() {
        if(request.isEmpty()) {
            return;
        }
        String firstLine = request.substring(0, request.indexOf("\n"));

        int firstSpace = firstLine.indexOf(" ");
        method = HTTPMethod.valueOf(firstLine.substring(0, firstSpace).toUpperCase());
        path = firstLine.substring(firstSpace, firstLine.indexOf(" ", firstSpace + 1));
    }

    private StringBuilder translateInput() throws IOException {
        byte[] bytes = new byte[inputStream.available()];
        int bytesReaded = inputStream.read(bytes);
        if (bytesReaded == -1) {
            throw new RuntimeException("header request is empty");
        }
        return new StringBuilder(new String(bytes));
    }

    private Map<String, String> translateHeaders() {
        Map<String, String> headers = new HashMap<>();

        if (request.isEmpty()) {
            return headers;
        }

        String substring = request.substring(request.indexOf("\n"));
        String[] split = substring.split("\n");

        for (String header : split) {
            if (header.trim().isBlank()){
                continue;
            }

            String key = header.substring(0, header.indexOf(":")).trim();
            String value = header.substring(header.indexOf(":") + 1).trim();

            headers.put(key, value);
        }

        return headers;
    }

}
