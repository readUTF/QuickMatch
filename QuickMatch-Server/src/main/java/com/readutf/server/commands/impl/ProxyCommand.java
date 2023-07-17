package com.readutf.server.commands.impl;

import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.Server;
import com.readutf.server.commands.Command;
import com.readutf.server.proxy.ProxyManager;
import com.readutf.server.servers.ServerManager;

import java.util.List;
import java.util.stream.Collectors;

public class ProxyCommand extends Command {

    private final ProxyManager proxyManager;

    public ProxyCommand(ProxyManager proxyManager) {
        super("proxys", 0, "");
        this.proxyManager = proxyManager;
    }


    @Override
    public void onCommand(String[] args) {
        List<ProxyInfo> servers = proxyManager.getProxies();
        if(servers.isEmpty()) {
            System.out.println("No servers found!");
            return;
        }
        System.out.println("Servers: " + servers.stream().map(ProxyInfo::getName).collect(Collectors.joining(", ")));
    }
}
