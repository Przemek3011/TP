package checkers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;

    public Client (String serverAddress, int port)
            throws IOException {
        this.socket = new Socket(serverAddress, port);
        this.in = new Scanner(socket.getInputStream());
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessageToServer (String message) {
        out.println(message);
    }

    public String receiveMessageFromServer () throws IOException {
        return in.nextLine();
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public void sendNumberOfPlayers(int number) {
        out.println(number);
    }

    public void launch() {
        try {
            Scanner userScanner = new Scanner(System.in);
            while(in.hasNextLine()) {
                String line = receiveMessageFromServer();
                if (line == null) break;
                if (line.equalsIgnoreCase("make your move")) {
                    System.out.println("Your Turn! Make your move.");
                    String move = userScanner.nextLine();
                    sendMessageToServer(move);
                } else if (line.startsWith("update:")) {
                    System.out.println("Board update:\n" + line.substring(7));
                } else if (line.equalsIgnoreCase("game over")) {
                    System.out.println("Game is over. The results are: " + receiveMessageFromServer());
                } else {
                    System.out.println(line);
                }
            }
            closeConnection();
        } catch (IOException e) {
            System.out.println("Connection lost.");
        }
    }
}
