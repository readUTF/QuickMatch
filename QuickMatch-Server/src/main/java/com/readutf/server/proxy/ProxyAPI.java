package com.readutf.server.proxy;

import com.readutf.quickmatch.shared.ProxyInfo;
import com.readutf.quickmatch.shared.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/private/proxy")
@AllArgsConstructor
public class ProxyAPI {

    private final ProxyManager proxyManager;

    @GetMapping("/register")
    public ResponseData<ProxyInfo> registerProxy(String address, int port) {
        return ResponseData.success(proxyManager.registerProxy(UUID.randomUUID(), address, port));
    }

    @DeleteMapping("/unregister")
    public ResponseData<String> unregisterProxy(UUID proxyId) {
        proxyManager.unregisterProxy(proxyId);
        return ResponseData.success("Successfully unregistered proxy");
    }

    @GetMapping("/list")
    public ResponseData<List<ProxyInfo>> getProxies() {
        return ResponseData.success(proxyManager.getProxies());
    }

}
