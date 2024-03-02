package com.roy.drisk.client;

import com.roy.drisk.message.RequestMessage;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author roy
 * @date 2021/10/31
 * @desc
 */
public class TCPMessage {
    public static final String DATA_FILE_SUFFIX = ".std";
    private static final HashMap<String, RequestMessage> data = new HashMap<>();

    static {
        File[] files = findDataFiles();
        printFileList(files);
        loadDataFiles(files);
    }

    private static File[] findDataFiles() {
        File dir = new File("./testdata");
        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(DATA_FILE_SUFFIX);
            }
        });
    }

    private static void printFileList(File[] files) {
        StringBuilder sb = new StringBuilder("Load sword-risk test data: [");
        for (File f : files) {
            sb.append(f.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        System.out.println(sb.toString());
    }

    private static void loadDataFiles(File[] files) {
        for (File f : files) {
            String name = StringUtils.split(f.getName(), ".")[0];
            RequestMessage message = loadDataFile(name, f);
            if (message != null) {
                data.put(name, message);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static RequestMessage loadDataFile(String name, File file) {
        if (!file.isFile() || !file.canRead()) {
            return null;
        }
        try {
            RequestMessage message = new RequestMessage();
            message.setBaseName(name + "KB");
            message.setSessionName(name + "KS");
            message.setRequestId(UUID.randomUUID().toString());
            message.setData(new HashMap<String, Object>());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                parseLine(message, line);
            }
            Map<String, Object> bsinf = (Map<String, Object>) message.getData().get("BSINF");
            if (bsinf != null) {
                message.setUserNo((String) bsinf.get("USR_NO"));
                message.setMobileNo((String) bsinf.get("MBL_NO"));
            }
            return message;
        } catch (Exception e) {
            return null;
        }
    }

    private static void parseLine(RequestMessage message, String line) {
        if (StringUtils.isBlank(line)) {
            return;
        }
        String[] kv = StringUtils.split(line, "=");
        if (kv == null || kv.length == 0) {
            return;
        }
        String key = kv[0];
        if ("BaseName".equals(key)) {
            message.setBaseName(kv[1]);
        } else if ("SessionName".equals(key)) {
            message.setSessionName(kv[1]);
        } else {
            String[] keyParts = StringUtils.split(key, ".");
            if (keyParts == null || keyParts.length == 0) {
                return;
            }
            String value = "";
            if (kv.length > 1) {
                value = kv[1];
            }
            setData(message.getData(), keyParts, 0, value);
        }
    }

    @SuppressWarnings("unchecked")
    private static void setData(Map<String, Object> dataArea, String[] keyParts, int idx, String value) {
        if (keyParts.length == (idx + 1)) {
            dataArea.put(keyParts[idx], value);
            return;
        }
        String key = keyParts[idx];
        if (dataArea.get(key) == null) {
            dataArea.put(key, new HashMap<String, Object>());
        }
        setData((Map<String, Object>) dataArea.get(key), keyParts, idx + 1, value);
    }

    public static RequestMessage newMessage(String name) {
        RequestMessage message = data.get(name);
        if (message == null) {
            throw new RuntimeException(name + DATA_FILE_SUFFIX + " test data not loaded.");
        }
        RequestMessage newMessage = new RequestMessage();
        newMessage.setBaseName(message.getBaseName());
        newMessage.setSessionName(message.getSessionName());
        newMessage.setRequestId(UUID.randomUUID().toString());
        newMessage.setClientId(Thread.currentThread().getName());
        newMessage.setUserNo(message.getUserNo());
        newMessage.setMobileNo(message.getMobileNo());
        newMessage.setData(message.getData());
        return newMessage;
    }
}
