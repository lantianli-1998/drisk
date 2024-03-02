package com.roy.drisk.client.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: QC
 * Date: 2016-05-13
 * Time: 11:21
 */
public class Main {
    private static final String PROPERTIES_FILE = "drisk-cli.properties";

    private static void printHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: java com.roy.drisk.client.cli.Main pool/simple once\n")
                .append("   or: java com.roy.drisk.client.cli.Main pool/simple once ${testdata-file}\n")
                .append("   or: java com.roy.drisk.client.cli.Main pool/simple time\n")
                .append("   or: java com.roy.drisk.client.cli.Main pool/simple time ${thread} ${seconds}\n")
                .append("   or: java com.roy.drisk.client.cli.Main pool/simple time ${thread} ${seconds} ${testdata-file}\n")
                .append("   or: java com.roy.drisk.client.cli.Main message\n")
                .append("   or: java com.roy.drisk.client.cli.Main message ${topic} ${message}\n");
        System.out.println(sb);
    }

    private static Runner buildRunner(RunParam param, String[] args) throws IOException {
        Properties properties = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (is != null) {
                properties.load(is);
            }
        }

        if (param.getType() == RunParam.Type.MESSAGE) {
            if (args.length == 1) {
            } else if (args.length == 3) {
                param.setTopic(args[1]);
                param.setMessage(args[2]);
            } else {
                return null;
            }
            return new MessageRunner(param, properties);
        } else {
            param.setTcpMode(args[1]);
            if (param.getTcpMode() == RunParam.TCPMode.ONCE) {
                if (args.length == 2) {
                } else if (args.length == 3) {
                    param.setMessage(args[2]);
                } else {
                    return null;
                }
            } else if (param.getTcpMode() == RunParam.TCPMode.TIME) {
                if (args.length == 2) {
                } else if (args.length == 4) {
                    param.setThreadsNum(args[2]);
                    param.setSeconds(args[3]);
                } else if (args.length == 5) {
                    param.setThreadsNum(args[2]);
                    param.setSeconds(args[3]);
                    param.setMessage(args[4]);
                } else {
                    return null;
                }
            }
            return new TCPRunner(param, properties);
        }
    }

    public static void main(String[] args) throws Throwable {
        if (args.length < 1) {
            printHelp();
            return;
        }
        RunParam param = new RunParam(args[0]);
        Runner runner = buildRunner(param, args);
        if (runner == null) {
            printHelp();
            return;
        }
        try {
            System.out.println(param);
            runner.run();
        } finally {
            runner.close();
        }
    }
}