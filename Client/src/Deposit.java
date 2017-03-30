import java.io.Serializable;
import java.util.ArrayList;

/**
 * Класс, обьекты которого хранят в себе информацию о вкладах
 * @author Roman Gorbunov
 */
public class Deposit implements Serializable {
    private String name, // название банка
            country, // страна регистрации
            type, // тип вклада (до востребования, срочный, расчетный, накопительный, сберегательный, металлический)
            depositor; // имя вкладчика
    private int accountID; // номер счета
    private float amountOnDeposit; // сумма вклада
    private float profitability; // годовой процент
    private short timeConstraints; // срок вклада, в месяцах

    private static String types [] = {"до востребования", "срочный", "расчетный", "накопительный", "сберегательный", "металлический"};
    private static short stringParamsQuantity = 4, //количество строковых параметров
            numberParamsQuantity = 4,
            paramsQuantity = (short) (stringParamsQuantity + numberParamsQuantity);

    Deposit(String name, String country, String type, String depositor, int accountID, float amountOnDeposit,
            float profitability, short timeConstraints)
    {
        this.name = name;
        this.country = country;
        this.type = type;
        this.depositor = depositor;
        this.accountID = accountID;
        this.amountOnDeposit = amountOnDeposit;
        this.profitability = profitability;
        this.timeConstraints = timeConstraints;
    }

    /**
     * Принимает список параметров вклада, и проверяет их на корректность
     * @param params - список параметров вклада
     * @return - в случае корректности входящего списка - true, в противном случае - false
     */
    static boolean checkInfoCorrectness(ArrayList<String> params){
        if (params.size() != paramsQuantity) return false;

        try {
            if (Integer.parseInt(params.get(4)) <= 0 || Float.parseFloat(params.get(5)) <= 0 ||
                    Float.parseFloat(params.get(6)) <= 0 || Short.parseShort(params.get(7)) <= 0) return false;
        } catch (Exception e) {
            return false;
        }

        for(String type : types){
            if(type.equals(params.get(stringParamsQuantity - 2))) return true;
        }
        return false;
    }

    /**
     * @return - обьект в виде строки. Все поля разделены запятой
     */
    @Override
    public String toString(){
        return "§" + name + "§" + country + "§" + type + "§" + depositor + "§" +
                accountID + "§" + amountOnDeposit + "§" + profitability + "§" +
                timeConstraints;
    }

    static String[] getTypes(){ return types; }

    public static short getStringParamsQuantity() { return stringParamsQuantity; }

    public static short getNumberParamsQuantity() { return numberParamsQuantity; }

    public static short getParamsQuantity() { return paramsQuantity; }

    public String getName() { return name; }

    public String getCountry() { return country; }

    public String getType() { return type; }

    public String getDepositor() { return depositor; }

    public int getAccountID() { return accountID; }

    public float getAmountOnDeposit() {
        return amountOnDeposit;
    }

    public float getProfitability() {
        return profitability;
    }

    public short getTimeConstraints() {
        return timeConstraints;
    }
}