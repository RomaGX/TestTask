import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Класс, который создает слушатель и подключает клиентов
 * @author Roman Gorbunov
 */
public class Server {

    static private int port = 9856;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket;
        String fileName = "Deposits.bin";
        ArrayList<Deposit> deposits;

        deposits = FileHandler.read(fileName);
        if (deposits == null) deposits = new ArrayList<>();

        for (int i = 0; i < 5; i++)
        {
            try{
                serverSocket = new ServerSocket(port + i);
                port += i;
            } catch (Exception e){
                if (i == 4) e.printStackTrace();
            }
            if (serverSocket != null) break;
        }

        if (serverSocket == null) System.exit(0);

        while (true) {
            try {
                socket = serverSocket.accept();
                new ClientThread(socket, deposits, fileName).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
