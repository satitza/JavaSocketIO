
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Client_1 {

    private final static String host = "localhost";

    private final static int port = 4444;

    private static final String path = "/media/Project/javasocketio/src/main/java/";
    private static final String file = "java_stream_2.pdf";

    private static final String fullPath = path + file;

    private static byte[] buffers;

    public static void main(String[] args) {

        try {

            Socket socket = new Socket(host, port);

            if  (socket.isConnected()){

                System.out.println("Connected to : " + socket.getRemoteSocketAddress());

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                SocketMessage socketMessage = new SocketMessage();

                while (true) {

                    String message;

                    socketMessage.recevieMessage(br);

                    Scanner scanner = new Scanner(System.in);
                    message = scanner.nextLine();


                    socketMessage.sendMessage(wr, message);

                    // get length of buffer
                    message = socketMessage.recevieMessage(br);

                    if (!message.equalsIgnoreCase("close")) {

                        buffers = new byte[Integer.valueOf(message)];

                        if (buffers.length == Integer.valueOf(message)) {

                            socketMessage.sendMessage(wr, "buffer_created");

                            if (recevieFile(socket, fullPath, buffers)) {

                                System.out.println("Write file success");
                                socket.close();
                                break;

                            }
                        }

                    } else {

                        socket.close();
                        break;

                    }

                }

            }

        } catch (IOException ex){

            System.out.println(ex.getMessage());

        }
    }

    public static boolean recevieFile (Socket socket, String fullPath, byte[] buffers) {

        try {

            // receive file
            InputStream in = socket.getInputStream();
            FileOutputStream out = new FileOutputStream(fullPath);

            for(int length = in.read(buffers, 0, 1024); length > 0; length = in.read(buffers, 0, 1024)){

                out.write(buffers, 0, length);
            }

            out.close();
            return true;


        } catch (IOException ex) {

            ex.printStackTrace();
            return false;

        }


    }

}
