package com.hadoop.baiduSpider;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Created by v-shaoleili on 2016/1/14.
 */
public class ESClientHelper {
    private static Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("10.100.60.181",9300));
    protected static Client getClient(){
        return client;
    }
}
