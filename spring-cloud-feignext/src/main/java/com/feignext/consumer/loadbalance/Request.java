package com.feignext.consumer.loadbalance;

/**
 * Created by libin on 2017/9/29.
 */

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * An immutable request to an http server.
 */
public final class Request {

    /**
     * No parameters can be null except {@code body} and {@code charset}. All parameters must be
     * effectively immutable, via safe copies, not mutating or otherwise.
     */
    public static Request create(String method, String url, Map<String, Collection<String>> headers,
                                 byte[] body, Charset charset) {
        return new Request(method, url, headers, body, charset);
    }

    private final String method;
    private final String url;
    private final Map<String, Collection<String>> headers;
    private final byte[] body;
    private final Charset charset;

    public Request(String method, String url, Map<String, Collection<String>> headers, byte[] body,
                   Charset charset) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body; // nullable
        this.charset = charset; // nullable
    }

    /* Method to invoke on the server. */
    public String method() {
        return method;
    }

    /* Fully resolved URL including query. */
    public String url() {
        return url;
    }

    /* Ordered list of headers that will be sent to the server. */
    public Map<String, Collection<String>> headers() {
        return headers;
    }

    /**
     * The character set with which the body is encoded, or null if unknown or not applicable.  When
     * this is present, you can use {@code new String(req.body(), req.charset())} to access the body
     * as a String.
     */
    public Charset charset() {
        return charset;
    }

    /**
     * If present, this is the replayable body to send to the server.  In some cases, this may be
     * interpretable as text.
     *
     * @see #charset()
     */
    public byte[] body() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(method).append(' ').append(url).append(" HTTP/1.1\n");
        for (String field : headers.keySet()) {
            for (String value : valuesOrEmpty(headers, field)) {
                builder.append(field).append(": ").append(value).append('\n');
            }
        }
        if (body != null) {
            builder.append('\n').append(charset != null ? new String(body, charset) : "Binary data");
        }
        return builder.toString();
    }

    public static <T> Collection<T> valuesOrEmpty(Map<String, Collection<T>> map, String key) {
        return map.containsKey(key) && map.get(key) != null ? map.get(key) : Collections.<T>emptyList();
    }

    /* Controls the per-request settings currently required to be implemented by all {@link Client clients} */
    public static class Options {

        private final int connectTimeoutMillis;
        private final int readTimeoutMillis;

        public Options(int connectTimeoutMillis, int readTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.readTimeoutMillis = readTimeoutMillis;
        }

        public Options() {
            this(10 * 1000, 60 * 1000);
        }

        /**
         * Defaults to 10 seconds. {@code 0} implies no timeout.
         *
         * @see java.net.HttpURLConnection#getConnectTimeout()
         */
        public int connectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        /**
         * Defaults to 60 seconds. {@code 0} implies no timeout.
         *
         * @see java.net.HttpURLConnection#getReadTimeout()
         */
        public int readTimeoutMillis() {
            return readTimeoutMillis;
        }
    }
}
