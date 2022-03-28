import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        try {
            // Client: telnet localhost 9001
            ServerSocket sock = new ServerSocket(9001);
            System.out.println("Connection on port 9001 open");
            while (true){
                Socket client = sock.accept();

                Thread t = new Thread(() -> {
                    try{
                        System.out.println("New client: " + client.toString());
                        PrintWriter clientWriter = new PrintWriter(client.getOutputStream(), true);

                        clientWriter.println("Welcome at HTL echo server at " + new java.util.Date().toString());
                        clientWriter.println("What's your name?");

                        BufferedReader clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String name = clientReader.readLine();
                        System.out.println(name + " connected.");
                        clientWriter.println("Hi " + name);

                        String input = "";
                        while (!input.toLowerCase().contains("bye")){
                            try {
                                input = clientReader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("==> " +name + ": " + input);
                            clientWriter.println("==> " + name + ": " + input);
                        }
                        clientWriter.println("Bye");
                        try {
                            System.out.println("Client " + name + " closed");
                            client.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                });
                t.start();
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
