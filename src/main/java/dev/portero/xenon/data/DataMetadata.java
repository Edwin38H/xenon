package dev.portero.xenon.data;

public class DataMetadata {
    private Boolean connected;
    private Integer ping;

    public void connected(boolean connected) {
        this.connected = connected;
    }

    public void ping(int ping) {
        this.ping = ping;
    }

    public DataMetadata combine(DataMetadata other) {
        if (this.connected == null || (other.connected != null && !other.connected)) {
            this.connected = other.connected;
        }
        if (this.ping == null || (other.ping != null && other.ping > this.ping)) {
            this.ping = other.ping;
        }
        return this;
    }
}
