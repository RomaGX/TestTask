import java.io.*;
import java.util.ArrayList;

/**
 * Класс, выполняющий операции с файлом
 * @author Roman Gorbunov
 */
class FileHandler {

    /**
     * @param fileName - строка с именем файла
     * @return - ArrayList из депозитов, считанных из указанного файла, либо null в случае неудачи
     */
    static ArrayList<Deposit> read(String fileName){
        ArrayList<Deposit> deposits;
        try{
            ObjectInputStream inputStream =  new ObjectInputStream(new FileInputStream(fileName));
            deposits = (ArrayList<Deposit>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return deposits;
    }

    /**
     * Сохраняет ArrayList в файл с указанным именем.
     * @param deposits - ArrayList из депозитов
     * @param fileName - имя файла
     * @return - в случае успеха true, неудачи - false
     */
    static boolean save(ArrayList<Deposit> deposits, String fileName){
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

            new File(fileName).delete();
            outputStream.writeObject(deposits);
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}