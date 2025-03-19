import java.io.*;
import java.net.Socket;

public class ClientHandler implements  Runnable{
    private Socket socket;

    ClientHandler(Socket socket)
    {
        this.socket = socket;
    }
    @Override
    public void run() {
        System.out.println("hellow world");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String request;
            while ((request = in.readLine()) != null) {
                System.out.println(request);
                out.println(request);
                out.flush();
            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
