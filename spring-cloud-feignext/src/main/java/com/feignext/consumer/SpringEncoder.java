package com.feignext.consumer;

import com.feignext.consumer.loadbalance.Request;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;

import javax.websocket.EncodeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by libin on 2017/10/9.
 */
public class SpringEncoder {

    public static final String CONTENT_LENGTH = "Content-Length";

    private ObjectFactory<HttpMessageConverters> messageConverters;

    public SpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        this.messageConverters = messageConverters;
    }

    public void encode(Object requestBody, Request request )
            throws EncodeException {
        // template.body(conversionService.convert(object, String.class));
        if (requestBody != null) {
            Class<?> requestType = requestBody.getClass();
            for (HttpMessageConverter<?> messageConverter : this.messageConverters
                    .getObject().getConverters()) {
                if (messageConverter.canWrite(requestType, null)) {

                    FeignOutputMessage outputMessage = new FeignOutputMessage(new HttpHeaders());
                    try {
                        @SuppressWarnings("unchecked")
                        HttpMessageConverter<Object> copy = (HttpMessageConverter<Object>) messageConverter;
                        copy.write(requestBody, null, outputMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    byte[] bytes = outputMessage.getOutputStream().toByteArray();
                    request.setBody(bytes);
                    request.setHeaders(getHeaders(outputMessage.getHeaders()));
                    request.setCharset(Charset.forName("UTF-8"));

                    int bodyLength = bytes != null ? bytes.length : 0;
                    String values = String.valueOf(bodyLength);
                        List<String> headersa = new ArrayList<String>();
                    headersa.addAll(Arrays.asList(values));
                    request.headers().put(CONTENT_LENGTH, headersa);
                    // copy.write(requestBody, requestContentType, outputMessage);
                   /* // do not use charset for binary data
                    if (messageConverter instanceof ByteArrayHttpMessageConverter) {
                        request.body(outputMessage.getOutputStream().toByteArray(), null);
                    } else {
                        request.body(outputMessage.getOutputStream().toByteArray(), Charset.forName("UTF-8"));
                    }*/
                }
            }
        }
    }

    static Map<String, Collection<String>> getHeaders(HttpHeaders httpHeaders) {
        LinkedHashMap<String, Collection<String>> headers = new LinkedHashMap<>();

        for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
            headers.put(entry.getKey(), entry.getValue());
        }

        return headers;
    }

    private class FeignOutputMessage implements HttpOutputMessage {

        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        private final HttpHeaders httpHeaders;

        private FeignOutputMessage(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
        }

        @Override
        public OutputStream getBody() throws IOException {
            return this.outputStream;
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.httpHeaders;
        }

        public ByteArrayOutputStream getOutputStream() {
            return this.outputStream;
        }

    }


}
