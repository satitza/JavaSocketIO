
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

import org.apache.log4j.Logger;

public class Server  {

    private static final int port = 4444;

    private static ServerSocket server;

    private static final Logger logger = Logger.getLogger(Server.class.getName());



    public static void main(String[] args)  {

        try {

            server = new ServerSocket(port);
            logger.info("Server is started on port : " + server.getLocalPort());

            while (true) {

                final Socket socket = server.accept();
                new Thread(new ThreadRequest(socket)).start();

            }

        } catch (IOException ex) {

            logger.error(ex.getMessage());

        }
    }

}

class ThreadRequest implements Runnable {


    private  Socket socket;

    private static final Logger logger = Logger.getLogger(ThreadRequest.class.getName());

    private static final String path = "/media/Project/JavaSocketIO/src/main/java/";
    private static final String file = "java_stream.pdf";

    private static final String fullPath = path + file;

    private byte[] bytes;

    ThreadRequest(Socket socket) {

        this.socket = socket;

    }

    @Override
    public void run() {


            try {

                if (socket.isConnected()) {

                    logger.info("Connected from : " + socket.getRemoteSocketAddress());

                    final BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    SocketMessage socketMessage = new SocketMessage();


                    while (true){

                        String message = "Do you want to download the "+ file + " [ yes / no ] : ";

                        socketMessage.sendMessage(wr, message);
                        message = br.readLine();

                        if (message.equalsIgnoreCase("yes")) {

                            File file = new File (fullPath);
                            bytes  = new byte [(int)file.length()];

                            message = Integer.toString(bytes.length);

                            logger.info("sent length of byte : " + message + " to client : " + socket.getRemoteSocketAddress());

                            // sent length buffer
                            socketMessage.sendMessage(wr, message);

                            // wait for confirm created buffer
                            message = socketMessage.recevieMessage(br);

                            if (message.equalsIgnoreCase("buffer_created")) {

                                  if (sendFile(socket, file)){

                                      logger.info("Sent file success");

                                      br.close();
                                      wr.close();

                                      socket.close();
                                      logger.info("Client " + socket.getRemoteSocketAddress() + " connection is closed");
                                      break;

                                  }

                            }

                        } else {

                            socketMessage.sendMessage(wr, "close");

                            br.close();
                            wr.close();

                            socket.close();
                            logger.info("Client " + socket.getRemoteSocketAddress() + " connection is closed");
                            break;

                        }

                    }
                }


            } catch (IOException ex){

                ex.printStackTrace();
                logger.error(ex.getMessage());

            }

    }

    public boolean sendFile (Socket socket, File file) {

        try {

            //loop send file

            FileInputStream in = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();
            System.out.println("Sending...");

            byte[] Mb = new byte[1024];
            for(int length = in.read(Mb); length > -1; length = in.read(Mb))
            {
                out.write(Mb, 0, length);
            }

            in.close();
            out.close();
            return true;

        } catch (IOException ex) {

            logger.error(ex.getMessage());
            return false;

        }

    }

}


