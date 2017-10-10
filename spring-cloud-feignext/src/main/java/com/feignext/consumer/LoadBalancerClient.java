package com.feignext.consumer;

import com.feignext.consumer.loadbalance.ClientLoadBalancer;
import com.feignext.consumer.loadbalance.Request;
import com.feignext.consumer.loadbalance.Response;
import com.feignext.consumer.loadbalance.SpringLoadBalancerCacheFactory;
import com.netflix.client.ClientException;
import com.netflix.client.config.IClientConfig;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.io.IOException;
import java.net.URI;

/**
 * Created by libin on 2017/9/26.
 * appname下的负载均衡配置客户端
 */
public class LoadBalancerClient implements Client {

    private final Client delegate;
    private SpringLoadBalancerCacheFactory lbClientFactory;
    private SpringClientFactory clientFactory;

    public LoadBalancerClient(Client delegate,
                              SpringLoadBalancerCacheFactory lbClientFactory,
                                   SpringClientFactory clientFactory) {
        this.delegate = delegate;
        this.lbClientFactory = lbClientFactory;
        this.clientFactory = clientFactory;
    }

    private ClientLoadBalancer lbClient(String clientName) {
        return this.lbClientFactory.create(clientName);
    }

    //执行方法
    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        URI uri = URI.create(request.url());
        String clientName = uri.getHost();
        URI uriWithoutHost = cleanUrl(request.url(), clientName);

        IClientConfig requestConfig = this.clientFactory.getClientConfig(clientName);

        ClientLoadBalancer.RibbonRequest ribbonRequest = new ClientLoadBalancer.RibbonRequest(this.delegate,request,uriWithoutHost);
        try {

            return lbClient(clientName).executeWithLoadBalancer(ribbonRequest,
                    requestConfig).toResponse();
        } catch (ClientException e) {
            e.printStackTrace();
        }
       /* try {
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            URI uriWithoutHost = cleanUrl(request.url(), clientName);
            FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                    this.delegate, request, uriWithoutHost);

            IClientConfig requestConfig = getClientConfig(options, clientName);
            return lbClient(clientName).executeWithLoadBalancer(ribbonRequest,
                    requestConfig).toResponse();
        }
        catch (ClientException e) {
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        }
*/
        return null;
    }

    static URI cleanUrl(String originalUrl, String host) {
        return URI.create(originalUrl.replaceFirst(host, ""));
    }
}
