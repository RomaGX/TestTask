import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Класс, объект которобо является потоком для обработки запросов одного клиента
 * @author Roman Gorbunov
 */
class ClientThread extends Thread{
    private Socket socket;
    private ArrayList<Deposit> deposits;
    private String fileName;

    /**
     * @param clientSocket - сокет для приема запросов и отправки ответов
     * @param deposits - список вкладов, относительно которого будут обрабатываться запросы
     * @param fileName - имя файла, в котором хранятся вклады
     */
    ClientThread(Socket clientSocket, ArrayList<Deposit> deposits, String fileName) {
        this.socket = clientSocket;
        this.deposits = deposits;
        this.fileName = fileName;
    }

    /**
     * Принимает запрос от клиента, и отправляет ему ответ
     */
    public void run() {
        RequestHandler requestHandler = new RequestHandler(fileName);
        DataInputStream socketIn;
        DataOutputStream socketOut;

        try {
            socketIn = new DataInputStream(socket.getInputStream());
            socketOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        String receivedRequest;
        String result;

        try {
            while (true) {
                receivedRequest = socketIn.readUTF();
                result = requestHandler.checkRequest(receivedRequest);

                if (!result.equals("OK")) {
                    socketOut.writeUTF(result);
                    socketOut.flush();
                    continue;
                }

                result = requestHandler.handleRequest(receivedRequest, deposits);
                socketOut.writeUTF(result);
                socketOut.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}