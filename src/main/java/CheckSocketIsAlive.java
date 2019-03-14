
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class CheckSocketIsAlive {

    private static String host = "192.168.1.125";

    public static boolean checkSocketIsAlive (String host, int port) {

        boolean isAlive = false;

        SocketAddress socketAddress = new InetSocketAddress(host, port);
        Socket socket = new Socket();

        int timeout = 3000; // 3 sec for try connect

        try {

            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;

        } catch (SocketTimeoutException ex) {

            //System.out.println("SocketTimeoutException " + host + " : " + port + " ." + ex.getMessage());

        } catch (IOException ex) {

            //System.out.println("IOException " + host + " : " + port + " ." + ex.getMessage());

        }
        return isAlive;
    }

    public static void main(String[] args) {

        for (int start = 0; start < 0xffff; start++) {

            if (checkSocketIsAlive(host, start) == true) {

                System.out.println("Port " + start +" on " + host +" is open");
            }

        }
    }

}
