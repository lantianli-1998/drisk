package com.roy.drisk.connector.http;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class LoggingCallback implements FutureCallback<HttpResponse> {
    private String tag;
    private Logger logger;
    private Level level;
    private String encoding;

    public LoggingCallback(String tag, Logger logger) {
        this(tag, logger, Level.DEBUG, null);
    }

    public LoggingCallback(String tag, Logger logger, Level level) {
        this(tag, logger, level, null);
    }

    public LoggingCallback(String tag, Logger logger, String encoding) {
        this(tag, logger, Level.DEBUG, encoding);
    }

    public LoggingCallback(String tag, Logger logger, Level level, String encoding) {
        this.tag = tag;
        this.logger = logger;
        this.level = level;
        this.encoding = encoding;
    }

    private String loggingString(String prefix, HttpResponse result) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(tag)
                .append(", response: ")
                .append(result.getStatusLine().getStatusCode())
                .append(", body: ");
        try {
            if (encoding == null || "".equals(encoding)) {
                sb.append(EntityUtils.toString(result.getEntity()));
            } else {
                sb.append(EntityUtils.toString(result.getEntity(), encoding));
            }
        } catch (Exception e) {
            sb.append("LoggingCallbackException: ").append(e.getMessage());
        }
        return sb.toString();
    }

    @Override
    public void completed(HttpResponse result) {
        switch (this.level) {
            case DEBUG:
                logger.debug(loggingString("LoggingCallbackCompleted: ", result));
                break;
            case ERROR:
                logger.error(loggingString("LoggingCallbackCompleted: ", result));
                break;
            case INFO:
                logger.info(loggingString("LoggingCallbackCompleted: ", result));
                break;
            case TRACE:
                logger.trace(loggingString("LoggingCallbackCompleted: ", result));
                break;
            case WARN:
                logger.warn(loggingString("LoggingCallbackCompleted: ", result));
                break;
        }
    }

    @Override
    public void failed(Exception ex) {
        logger.error("LoggingCallbackFailed: " + tag, ex);
    }

    @Override
    public void cancelled() {
        logger.error("LoggingCallbackCancelled: " + tag);
    }
}
