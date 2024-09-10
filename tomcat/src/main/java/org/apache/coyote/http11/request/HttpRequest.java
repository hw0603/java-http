package org.apache.coyote.http11.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.coyote.http11.common.HttpHeader;
import org.apache.coyote.http11.common.HttpMethod;

public class HttpRequest {

    private final RequestLine requestLine;
    private final HttpHeader header;
    private RequestBody body;

    public HttpRequest(RequestLine requestLine, HttpHeader header, RequestBody body) {
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
    }

    // TODO: refactor
    public Map<String, String> parseFormBody() {
        Map<String, String> result = new HashMap<>();
        if (body == null) {
            return result;
        }
        String[] params = body.getBody().split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }

    public Optional<String> getFormValue(String key) {
        Map<String, String> formBody = parseFormBody();
        if (!formBody.containsKey(key)) {
            return Optional.empty();
        }
        return Optional.of(formBody.get(key));
    }

    public HttpMethod getMethod() {
        return requestLine.method();
    }

    public String getPath() {
        return requestLine.path();
    }

    public String getVersion() {
        return requestLine.version();
    }

    public Map<String, String> getHeaders() {
        return header.headers();
    }

    public String getBody() {
        return body.getBody();
    }

    public void setBody(String body) {
        this.body = new RequestBody(body);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "body=" + body +
                ", requestLine=" + requestLine +
                ", header=" + header +
                '}';
    }

    // TODO: refactor
    public String getSessionId() {
        Map<String, String> headers = getHeaders();
        String cookie = headers.getOrDefault("Cookie", "");
        String[] cookies = cookie.split(";");
        for (String c : cookies) {
            if (c.contains("JSESSIONID")) {
                return c.split("=")[1];
            }
        }
        return "";
    }
}
