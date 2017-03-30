import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Класс, объекты которого обрабатывают запросы
 * @author Roman Gorbunov
 */
class RequestHandler {
    private String fileName;

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

    /**
     * @param fileName - имя файла, с которого при необходимости нужно считать информацию о
     * вкладах, и в который сохранять изменения о них
     */
    RequestHandler(String fileName){
        this.fileName = fileName;
    }

    /**
     * Проверяет правильность написания команды и количество параметров.
     * @param receivedRequest - строка, присланная от клиента
     * @return - случае корректности "OK". В противном случае - причина некорректности
     */
    String checkRequest(String receivedRequest){
        String request[] = receivedRequest.split("§");
        if (!requestsInfo.containsKey(request[0])) return "Неизвестная команда";

        String paramsInfo = requestsInfo.get(request[0]);
        try{
            if (paramsInfo.startsWith("withParams") &&
                    (request.length - 1) != Integer.parseInt(paramsInfo.replace("withParams", "")))
                return "Неверное количество параметров";
        } catch (Exception e) {
            return "Ошибка сервера";
        }


        return "OK";
    }

    /**
     * @param receivedRequest - запрос, присланный от клиента
     * @param deposits - ArrayList из вкладов, относительно которого необходимо обработать запрос
     * @return - результат обработки запроса
     */
    String handleRequest(String receivedRequest, ArrayList<Deposit> deposits){
        String request[] = receivedRequest.split("§");
        try{
            switch (request[0]){
                case "list":
                    return arrayToOne(list(deposits));
                case "sum":
                    return sum(deposits);
                case "count":
                    return count(deposits);
                case "info account":
                    return infoAccount(Integer.parseInt(request[1]), deposits);
                case "info depositor":
                    return arrayToOne(infoDepositor(request[1], deposits));
                case "show type":
                    return arrayToOne(showType(request[1], deposits));
                case "show bank":
                    return arrayToOne(showBank(request[1], deposits));
                case "add":
                    return add(request, deposits);
                case "delete":
                    return delete(Integer.parseInt(request[1]), deposits);
                default:
                    return "Неизвестная команда";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Введены не корректные данные";
        }
    }

    /**
     * @param deposits - ArrayList из вкладов
     * @return - массив строк, содержащий информацию о всех вкладах
     */
    private String[] list(ArrayList<Deposit> deposits){
        if(deposits.size() == 0) {
            String[] result = {"Справочник пуст"};
            return result;
        }
        String[] result = new String[deposits.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = deposits.get(i).toString();
        return result;
    }

    /**
     * @param deposits - ArrayList из вкладов
     * @return - строка, содержащая в себе сумму всех вкладов
     */
    private String sum(ArrayList<Deposit> deposits){
        double result = 0;
        for(Deposit deposit : deposits)
            result += deposit.getAmountOnDeposit();
        return result + "";
    }

    /**
     * @param deposits - ArrayList из вкладов
     * @return - строка, содержащая информацию о размере ArrayListа
     */
    private String count(ArrayList<Deposit> deposits){
        return deposits.size() + "";
    }

    /**
     * @param accountID - ID вклада
     * @param deposits - ArrayList из вкладов
     * @return - строка, содержащая информацию о вкладе с указаным ID, либо об отстутствии такового
     */
    private String infoAccount(int accountID, ArrayList<Deposit> deposits){
        for(Deposit deposit : deposits){
            if(deposit.getAccountID() == accountID)
                return deposit.toString();
        }
        return "Такого счета нет в справочнике";
    }

    /**
     * @param depositorName - имя вкладчика
     * @param deposits - ArrayList из вкладов
     * @return - массив, содержащий в себе информацию о вкладах указанного вкладчика, либо об отсутствии такового
     */
    private String[] infoDepositor(String depositorName, ArrayList<Deposit> deposits){
        boolean isDepositorFound = false;
        ArrayList<String> result = new ArrayList<>();

        for (Deposit deposit : deposits){
            if (deposit.getDepositor().equals(depositorName)){
                result.add(deposit.toString());
                isDepositorFound = true;
            }
        }

        if (!isDepositorFound) {
            result.add("Вкладчика с таким именем нет в справочнике");
            return arrayListToArray(result);
        }

        return arrayListToArray(result);
    }

    /**
     * @param type - тип вклада
     * @param deposits - ArrayList из вкладов
     * @return - массив строк, содержащий информацию о вкладах такого типа, либо информацию об отсутствии таковых,
     * либо в случае неверно указанного типа вклада сообщает об этом
     */
    private String[] showType(String type, ArrayList<Deposit> deposits){
        ArrayList<String> result = new ArrayList<>();
        boolean isTypeCorrect = false,
                isTypeFound = false;

        for (String variant : Deposit.getTypes()){
            if (type.equals(variant)) isTypeCorrect = true;
        }
        if (!isTypeCorrect) {
            result.add("Неверно указан тип вклада");
            return arrayListToArray(result);
        }

        for (Deposit deposit : deposits){
            if (deposit.getType().equals(type)){
                result.add(deposit.toString());
                isTypeFound = true;
            }
        }

        if (!isTypeFound) {
            result.add("Вкладов такого типа нет в справочнике");
            return arrayListToArray(result);
        }

        return arrayListToArray(result);
    }

    /**
     * @param bank - название банка
     * @param deposits -  ArrayList из вкладов
     * @return - массив строк, содержащий информацию о вкладах в указанном банке, либо информацию об отсутствии таковых
     */
    private String[] showBank(String bank, ArrayList<Deposit> deposits){
        ArrayList<String> result = new ArrayList<>();
        boolean isBankFound = false;

        for (Deposit deposit : deposits){
            if (deposit.getName().equals(bank)){
                result.add(deposit.toString());
                isBankFound = true;
            }
        }

        if(!isBankFound) {
            result.add("Вкладов из банка с таким названием нет в справочнике");
            return arrayListToArray(result);
        }
        return arrayListToArray(result);
    }

    /**
     * @param request - параметры нового вклада
     * @param deposits - ArrayList из вкладов
     * @return - строка, содержащая в себе информацию о результате добавления либо попытки его совершения
     */
    private synchronized String add(String request[], ArrayList<Deposit> deposits){
        ArrayList<String> params = new ArrayList<>(Arrays.asList(request));
        params.remove(0);
        if(!Deposit.checkInfoCorrectness(params)) return "Ошибка. Перепроверьте, пожалуйста, введенные данные";

        int accountID = Integer.parseInt(request[5]);
        for (Deposit deposit : deposits){
            if (deposit.getAccountID() == accountID){
                return "Вклад с таким ID уже существует";
            }
        }

        try {
            deposits.add(new Deposit(request[1], request[2], request[3], request[4], Integer.parseInt(request[5]), Float.parseFloat(request[6]), Float.parseFloat(request[7]), Short.parseShort(request[8])));
            FileHandler.save(deposits, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка добавления";
        }

        return "Вклад успешно добавлен";
    }

    /**
     * @param accountID - ID счета, который необходимо удалить
     * @param deposits - ArrayList из вкладов
     * @return - строка, содержащая в себе информацию о результате удаления либо попытки его совершения
     */
    private synchronized String delete(int accountID, ArrayList<Deposit> deposits){
        for (int i = 0; i < deposits.size(); i++){
            if (deposits.get(i).getAccountID() == accountID){
                deposits.remove(i);
                FileHandler.save(deposits, fileName);
                return "Удаление успешно завершено";
            }
        }
        return "Вклада с таким ID нет в справочнике";
    }

    /**
     * @param array - массив из строк
     * @return - входящий массив, соединенный в одну строку. Между элементами массива и в конце ставится "\n"
     */
    private String arrayToOne(String array[]){
        String result = "";
        for (String one : array){
            if(result.equals("")) result += one;
            else result += "\n" + one;
        }
        return result;
    }

    /**
     * @param arrayList - коллекция из строк
     * @return - массив, состоящий из тех же строк
     */
    private String[] arrayListToArray(ArrayList<String> arrayList){
        String result[] = new String[arrayList.size()];
        return arrayList.toArray(result);
    }
}