package TCP;

public class ConnectionConfig {

    public static final String D_HOST = "localhost";

    public static int D_POST = 228;


    private final String host;

    private final int port;

    public ConnectionConfig(String host, int port) {
        this.host = validateHost(host);
        this.port = validatePort(port);
    }

    private String validateHost(String host) {
        if (host == null || host.trim().isEmpty()) {
            System.out.println("Host is empty, using default: " + D_HOST);
            return D_HOST;
        }
        return host.trim();
    }

    private int validatePort(int port) {
        if (port < 1024 || port > 65535) {

            System.err.println("Port " + port + " is out of recommended range. Using default: " + D_POST);
            return D_POST;
        }
        return port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("ConnectionConfig[host= ", host + " ; port= " + port);
    }
    
}
