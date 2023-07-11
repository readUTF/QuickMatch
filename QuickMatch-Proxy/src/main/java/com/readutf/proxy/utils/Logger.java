package com.readutf.proxy.utils;

import com.readutf.proxy.QuickMatchProxy;
import lombok.Getter;

public class Logger {

    private @Getter static final Logger GENERAL = new Logger("(debug)");
    private @Getter static final Logger SERVER = new Logger("(debug/server)");
    private @Getter static final Logger QUEUE = new Logger("(debug/queue)");
    private @Getter static final Logger LOGIN = new Logger("(debug/login)");

    private final String prefix;
    private final org.slf4j.Logger logger;

    public Logger(String prefix) {
        this.prefix = prefix;
        this.logger = QuickMatchProxy.getInstance().getLogger();
    }

    public void info(String message) {
        logger.info("%s %s".formatted(prefix, message));
    }

    public void debug(String message) {
        logger.debug("%s %s".formatted(prefix, message));
    }

    public void error(String message) {
        logger.error("%s %s".formatted(prefix, message));
    }

    public void warn(String message) {
        logger.warn("%s %s".formatted(prefix, message));
    }


}
