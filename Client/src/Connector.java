import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * Класс, объект которого отвечает за отправку и прием запросов с сервера
 * @author Roman Gorbunov
 */
class Connector {

    private int serverPort;
    private String serverIP;
    private Socket socket;
    private boolean connected = false;
    private DataInputStream socketIn;
    private DataOutputStream socketOut;
    private String enteredCommand,
            enteredParams[];

    private HashMap<String, String> requestsInfo = new HashMap<String, String>(){{
        put("list", "withoutParams");
        put("sum", "withoutParams");
        put("count", "withoutParams");
        put("info account", "withParams1");
        put("info depositor", "withParams1");
        put("show type", "withParams1");
        put("show bank", "withParams1");
        put("add", "withParams8");
        put("delete", "withParams1");
    }};

    Connector(int serverPort, String serverIP){
        this.serverPort = serverPort;
        this.serverIP = serverIP;
    }

    /**
     * Связывается с сервером, отправляет ему запросы и принимает их
     * @param requestText - запрос, введенный пользователем
     * @return - в случае, если команда и количество параметров введены корректно - ответ сервера,
     * в противном случае указывает на проблему
     */
    String sendRequest (String requestText) {
        String checkResult = checkRequest(requestText.trim());
        String formedRequest;
        switch (checkResult){
            case "withoutParams":
                formedRequest = enteredCommand;
                break;
            case "withParams":
                formedRequest = enteredCommand;
                for (String param : enteredParams){
                    formedRequest += "§" + param;
                }
                break;
            default:
                return checkResult;
        }

            try {
                if(!isConnected()){
                    socket = connect(serverIP, serverPort);
                    if (socket == null) return "Не удалось связатся с сервером. Попробуйте, пожалуйста, позже";

                    socketIn = new DataInputStream(socket.getInputStream());
                    socketOut = new DataOutputStream(socket.getOutputStream());
                    connected = true;
                }

                socketOut.writeUTF(formedRequest);
                socketOut.flush();
                return parseResponse(socketIn.readUTF());
            } catch (Exception x) { x.printStackTrace();
                connected = false;
                return "Ошибка соединения с сервером";
            }
    }

    /**
     * @param enteredRequest - запрос, введенный пользователем
     * @return - результат проверки запроса. В случае, если команда и количество параметров введены корректно -
     * указывает на тип команды (с параметрами / без параметров). В противном случае - на проблему
     */
    private String checkRequest(String enteredRequest){

        String preParams[] = null;
        boolean isEnteredCommandCorrect = false;

        for (String command : requestsInfo.keySet()){
            if (enteredRequest.startsWith(command)){
                enteredCommand = command;
                preParams = enteredRequest.replace(command, "").split(",");
                isEnteredCommandCorrect = true;
                break;
            }
        }

        if (!isEnteredCommandCorrect) return "Неизвестная команда";

        if (requestsInfo.get(enteredCommand).equals("withoutParams")){
            if(enteredRequest.trim().equals(enteredCommand))
                return "withoutParams";
            else return "Для данной команды не нужны параметры. Пожалуйста, введите снова";
        }

        enteredParams = new String[preParams.length];
        for (int i = 0; i < preParams.length; i++){
            enteredParams[i] = preParams[i].trim();
        }

        try{
            if (enteredParams.length != Integer.parseInt(requestsInfo.get(enteredCommand).replace("withParams", "")))
                return "Неверное количество параметров";
            else return "withParams";
        } catch (Exception e) {
            e.printStackTrace();
            return "Неизвестная ошибка";
        }
    }

    /**
     * @param response - ответ сервера
     * @return - отпарсеный ответ сервера, готовый к выводу в интерфейс пользователя
     */
    private String parseResponse(String response){
        for (int i = 0; response.contains("§"); i++){
            switch (i % Deposit.getParamsQuantity()){
                case 0:
                    response = response.replaceFirst("§", "Банк: ");
                    break;
                case 1:
                    response = response.replaceFirst("§", ". Страна: ");
                    break;
                case 2:
                    response = response.replaceFirst("§", ". Тип вклада: ");
                    break;
                case 3:
                    response = response.replaceFirst("§", ". Имя вкладчика: ");
                    break;
                case 4:
                    response = response.replaceFirst("§", ". Номер счета: ");
                    break;
                case 5:
                    response = response.replaceFirst("§", ". Сумма вклада: ");
                    break;
                case 6:
                    response = response.replaceFirst("§", ". Годовой процент: ");
                    break;
                case 7:
                    response = response.replaceFirst("§", ". Срок вклада, месяцев: ");
                    break;
            }
        }
        return response;
    }

    /**
     * @param serverIP - IP сервера
     * @param serverPort - предполагаемый порт сервера
     * @return - сокет, по которому можно отпралять запросы серверу, либо null в случае, если сервер не активен
     */
    private Socket connect(String serverIP, int serverPort){
        for(int i = 0; i < 5; i++) {
            try {
                return new Socket(serverIP, serverPort + i);
            } catch (Exception e) {
                if (i == 4) e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Закрывает сокет
     */
    void disconnect(){
        try{
            if (socket != null)
                socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerIP() {
        return serverIP;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return connected;
    }
}