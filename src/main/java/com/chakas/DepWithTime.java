package com.chakas;

import java.sql.Timestamp;

class DepWithTime {
    String groupId;
    String version;
    String artifactId;
    Timestamp timeStamp;

    public DepWithTime(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.version = version;
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "DepWithTime{" +
                "groupId='" + groupId + '\'' +
                ", version='" + version + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
