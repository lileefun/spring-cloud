package com.feignext.consumer.loadbalance;

import com.feignext.consumer.Client;
import com.netflix.client.*;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 *
 * 单个appname下的负载均衡配置器
 * Created by libin on 2017/9/29.
 */
public class ClientLoadBalancer extends AbstractLoadBalancerAwareClient<ClientLoadBalancer.RibbonRequest,ClientLoadBalancer.RibbonResponse> {

    protected int connectTimeout;
    protected int readTimeout;
    protected IClientConfig clientConfig;
    protected ServerIntrospector serverIntrospector;

    public ClientLoadBalancer(ILoadBalancer lb, IClientConfig clientConfig,
                             ServerIntrospector serverIntrospector) {
        super(lb, clientConfig);
        this.setRetryHandler(RetryHandler.DEFAULT);
        this.clientConfig = clientConfig;
        this.connectTimeout = clientConfig.get(CommonClientConfigKey.ConnectTimeout);
        this.readTimeout = clientConfig.get(CommonClientConfigKey.ReadTimeout);
        this.serverIntrospector = serverIntrospector;
    }



    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(
            RibbonRequest request, IClientConfig requestConfig) {
        if (this.clientConfig.get(CommonClientConfigKey.OkToRetryOnAllOperations,
                false)) {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }
        if (!request.toRequest().method().equals("GET")) {
            return new RequestSpecificRetryHandler(true, false, this.getRetryHandler(),
                    requestConfig);
        }
        else {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }
    }

    @Override
    public RibbonResponse execute(RibbonRequest request, IClientConfig configOverride)
            throws IOException {
        Request.Options options;
        if (configOverride != null) {
            options = new Request.Options(
                    configOverride.get(CommonClientConfigKey.ConnectTimeout,
                            this.connectTimeout),
                    (configOverride.get(CommonClientConfigKey.ReadTimeout,
                            this.readTimeout)));
        }
        else {
            options = new Request.Options(this.connectTimeout, this.readTimeout);
        }
        Response response = request.client().execute(request.toRequest(), options);
        return new RibbonResponse(request.getUri(), response);
    }


    public static class RibbonRequest extends ClientRequest implements Cloneable {
        private final Request request;
        private final Client client;

        public RibbonRequest(Client client, Request request, URI uri) {
            this.client = client;
            setUri(uri);
            this.request = toRequest(request);
        }

        private Request toRequest(Request request) {
            Map<String, Collection<String>> headers = new LinkedHashMap<>(
                    request.headers());
            return Request.create("",request.method(),getUri().toASCIIString(),headers,request.body(),request.charset());
        }

        Request toRequest() {
            return toRequest(this.request);
        }

        Client client() {
            return this.client;
        }

        HttpRequest toHttpRequest() {
            return new HttpRequest() {
                @Override
                public HttpMethod getMethod() {
                    return HttpMethod.resolve(RibbonRequest.this.toRequest().method());
                }

                @Override
                public URI getURI() {
                    return RibbonRequest.this.getUri();
                }

                @Override
                public HttpHeaders getHeaders() {
                    Map<String, List<String>> headers = new HashMap<String, List<String>>();
                    Map<String, Collection<String>> feignHeaders = RibbonRequest.this.toRequest().headers();
                    for(String key : feignHeaders.keySet()) {
                        headers.put(key, new ArrayList<String>(feignHeaders.get(key)));
                    }
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(headers);
                    return httpHeaders;

                }
            };
        }


        @Override
        public Object clone() {
            return new RibbonRequest(this.client, this.request, getUri());
        }

    }

    public static class RibbonResponse implements IResponse {

        private final URI uri;
        private final Response response;

        public RibbonResponse(URI uri, Response response) {
            this.uri = uri;
            this.response = response;
        }

        @Override
        public Object getPayload() throws ClientException {
            return this.response.body();
        }

        @Override
        public boolean hasPayload() {
            return this.response.body() != null;
        }

        @Override
        public boolean isSuccess() {
            return this.response.status() == 200;
        }

        @Override
        public URI getRequestedURI() {
            return this.uri;
        }

        @Override
        public Map<String, Collection<String>> getHeaders() {
            return this.response.headers();
        }

        public Response toResponse() {
            return this.response;
        }

        @Override
        public void close() throws IOException {
            if (this.response != null && this.response.body() != null) {
                this.response.body().close();
            }
        }

    }

}
