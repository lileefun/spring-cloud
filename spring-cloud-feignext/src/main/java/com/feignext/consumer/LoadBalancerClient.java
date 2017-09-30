package com.feignext.consumer;

import com.feignext.consumer.loadbalance.ClientLoadBalancer;
import com.feignext.consumer.loadbalance.Request;
import com.feignext.consumer.loadbalance.Response;
import com.feignext.consumer.loadbalance.SpringLoadBalancerCacheFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.io.IOException;

/**
 * Created by libin on 2017/9/26.
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
}
