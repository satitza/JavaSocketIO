import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SocketMessage {

    public String recevieMessage (BufferedReader br) throws IOException {

        String message = br.readLine();
        System.out.println(message);
        return message;

    }

    public void sendMessage (BufferedWriter wr, String message) throws IOException {

        wr.write(message);
        wr.newLine();
        wr.flush();

    }

}
