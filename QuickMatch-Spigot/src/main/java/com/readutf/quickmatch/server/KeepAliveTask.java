package com.readutf.quickmatch.server;

import com.github.readutf.hermes.Hermes;
import com.readutf.quickmatch.shared.Server;
import com.readutf.quickmatch.shared.ServerPing;
import com.readutf.quickmatch.shared.serializers.ServerPingSerializer;
import org.bukkit.Bukkit;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class KeepAliveTask extends TimerTask {

    private final Hermes hermes;
    private final Supplier<Server> serverSupplier;

    public KeepAliveTask(Hermes hermes, Supplier<Server> serverSupplier) {
        this.hermes = hermes;
        this.serverSupplier = serverSupplier;
        new Timer().scheduleAtFixedRate(this, 0, 5000);
    }

    @Override
    public void run() {
        Server server = serverSupplier.get();
        if(server == null) return;
        hermes.sendParcel("SERVER_PING", new ServerPing(server.getServerId(), Bukkit.getOnlinePlayers().size()), new ServerPingSerializer());
    }
}
