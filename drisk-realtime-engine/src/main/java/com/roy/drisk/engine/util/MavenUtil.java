package com.roy.drisk.engine.util;

/**
 * @author roy
 * @date 2021/10/27
 * @desc
 */
public class MavenUtil {
    /**
     * Maven坐标
     */
    public static class Coordinates {
        private String groupId;
        private String artifactId;
        private String version;

        public Coordinates(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    /**
     * 从字符串解析Maven坐标，字符串格式为：
     * groupId:artifactId:version
     *
     * @param coordinates 坐标的字符串表示
     * @return Coordinates 如无法解析则返回null
     */
    public static Coordinates parseCoordinatesString(String coordinates) {
        if (coordinates == null || "".equals(coordinates)) {
            return null;
        }
        String[] parts = coordinates.split(":");
        if (parts.length != 3) {
            return null;
        }
        for (String part : parts) {
            if (part == null || "".equals(part)) {
                return null;
            }
        }
        return new Coordinates(parts[0], parts[1], parts[2]);
    }
}
