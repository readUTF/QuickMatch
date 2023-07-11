package com.readutf.proxy.balancing.impl;

import com.readutf.proxy.balancing.BalanceMode;
import com.readutf.quickmatch.shared.Server;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBalance implements BalanceMode {

    @Override
    public Server findServer(List<Server> serverList) {
        return serverList.get(ThreadLocalRandom.current().nextInt(serverList.size()));
    }
}
