# TestTask

_Автор:_ _Горбунов_ _Роман_
### Контактные данные:

* **e-mail**: romagx16@gmail.com
* **skype**: romang.ru
* **телефон**: +380632644952, +380994663338

---

## Структура решения

### Клиент:
**Client/src/**

* [ClientWindow.java](/Client/src/ClientWindow.java) - Содержит класс, реализующий интерфейс пользователя
* [Connector.java](/Client/src/Connector.java) - Содержит класс, объект которого отвечает за отправку и прием запросов с сервера
* [Deposit.java](/Client/src/Deposit.java) - Содержит класс, обьекты которого хранят в себе информацию о вкладах

### Сервер:
**PrivatServer/src/**

* [ClientThread.java](/PrivatServer/src/ClientThread.java) - Содержит класс, объект которого является потоком для обработки запросов одного клиента
* [Deposit.java](/PrivatServer/src/Deposit.java) - Содержит класс, обьекты которого хранят в себе информацию о вкладах
* [FileHandler.java](/PrivatServer/src/FileHandler.java) - Содержит класс, выполняющий операции с файлом
* [RequestHandler.java](/PrivatServer/src/RequestHandler.java) - Содержит класс, объекты которого обрабатывают запросы
* [Server.java](/PrivatServer/src/Server.java) - Содержит класс, который создает слушатель и подключает клиентов

---
## Описание решения

Данное решение создано при помощи IDE IntelliJ IDEA и состоит из друх приложений - серверного и клиентского, реализующих функциональность справочника по банковским вкладам.

Чтоб воспользоватся решением, необходимо запустить серверную _(Server.jar)_ и клиентскую _(Client.jar)_ части. 
В файле _Deposits.bin_ записано 5 вкладов, и сервер при запуске считает с него данные, если положить их в одну папку.

После запуска клиентская часть предоставит интерфейс пользователя для ввода команд и отображения ответов сервера.

---
## Допустимые команды:
1. **list** - выдать список всех вкладов.
Не имеет параметров

2. **sum** - выдать общую сумму вкладов.
Не имеет параметров

3. **count** - выдать количество вкладов.
Не имеет параметров

4. **info account <account id>** - выдать информацию о вкладе по его номеру. Имеет 1 параметр - номер вклада. 

Пример команды: 

***`info account 4832`***

5. **info depositor <depositor>** - выдать информацию по имени вкладчика. Имеет 1 параметр - имя вкладчика.

Пример команды: 

***`info depositor Иванов Иван`***

6. **show type <type>** - выдать список всех вкладов указанного типа. Имеет 1 параметр - тип вклада. 

Пример команды: 

***`show type срочный`***

7. **show bank <name>** - выдать список всех вкладов в указанном банке. Имеет 1 параметр - название банка. 

Пример команды: 

***`show bank ПриватБанк`***

8. **add <deposit info>** - добавить информацию о вкладе. Имеет 8 параметров, а именно:

* _Name_ — название банка;
* _Country_ — страна регистрации;
* _Type_ — тип вклада (до востребования, срочный, расчетный, накопительный, сберегательный, металлический);
* _Depositor_ — имя вкладчика;
* _Account id_ — номер счета;
* _Amount on deposit_ — сумма вклада;
* _Profitability_ — годовой процент;
* _Time constraints_ — срок вклада (в месяцах).

Пример команды: 

***`add ПриватБанк, Украина, расчетный, Иванов Иван, 6435, 42000, 22.5, 24`***

9. **delete <account id>** - удалить счет по его номеру. Имеет 1 параметр - номер вклада. 

Пример команды: 

***`delete 4832`***
