package mvc;
import mvc.panels.SelectPanel;
import mvc.panels.EnemyField;
import mvc.panels.Buttons;
import mvc.panels.MyField;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;

public class View extends JFrame {
    public static int port;
    private Controller controller;
    private Model model;
    private MyField myField; //панель нашего игрового поля
    private EnemyField enemyField; //панель игрового поля соперника
    private SelectPanel selectPanel; //панель выбора настроек при добавлении корабля
    private Buttons buttons; //панель кнопок

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Морской бой");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    //инициализация графического интерфейса
    public void init() {
        if (enemyField != null) {
            remove(enemyField);
            remove(myField);
            remove(buttons);
        }
        controller.loadEmptyMyField();
        add(selectPanel = new SelectPanel(this), BorderLayout.EAST);
        add(myField = new MyField(this), BorderLayout.WEST);
        add(buttons = new Buttons(this), BorderLayout.SOUTH);
        myField.setSelectPanel(selectPanel);
        pack();
        revalidate();
        setVisible(true);
    }

    //метод для вызова информационного диалогового окна с заданныйм текстом
    public static void callInformationWindow(String message) {
        JOptionPane.showMessageDialog(
                null, message,
                "Внимание!", JOptionPane.ERROR_MESSAGE
        );
    }

    //метод для загрузки нашего пустого игровоо поля
    public void loadEmptyMyField() {
        controller.loadEmptyMyField();
        myField.repaint();
    }

    //добавление корабля
    public void addShip(Ship ship) {
        controller.addShip(ship);
    }

    //удаление корабля с нашего поля по координатам
    public Ship removeShip(int x, int y) {
        return controller.removeShip(x, y);
    }


    //метод перерисовывает наше игровое поле
    public void repaintMyField(Graphics g) {
        mvc.Box[][] matrix = model.getMyField(); //получаем матрицу нашего поля
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                mvc.Box box = matrix[i][j]; //присваиваем боксу значение элемента матрицы
                if (box == null) continue;
                //подгружаем картинку на панель нашего игрового поля
                g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), myField);
            }
        }
    }

    //метод перерисовывает игровое поле соперника
    public void repaintEnemyField(Graphics g) {
        mvc.Box[][] matrix = model.getEnemyField(); //получаем матрицу поля соперника
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Box box = matrix[i][j];
                if (box == null) continue;
                //если значение картинки = пустой клетки или клетки с кораблем то...
                if ((box.getPicture() == Picture.EMPTY || box.getPicture() == Picture.SHIP)) {
                    //если бокс открыт и картинка = пустая клетка, то отрисовываем эту клетку картинкой "пустая клетка с точкой"
                    if (box.isOpen() && box.getPicture() == Picture.EMPTY) {
                        g.drawImage(Picture.getImage(Picture.POINT.name()), box.getX(), box.getY(), enemyField);
                    }
                    //иначе если бокс открыт и картинка = клетка с кораблем, то отрисовываем эту клетку картинкой "клетка с зачеркнутым кораблем"
                    else if ((box.isOpen() && box.getPicture() == Picture.SHIP)) {
                        g.drawImage(Picture.getImage(Picture.DESTROY_SHIP.name()), box.getX(), box.getY(), enemyField);
                    }
                    //в остальных случаях отрисовываем клетку картинкой "закрытая клетка"
                    else g.drawImage(Picture.getImage(Picture.CLOSED.name()), box.getX(), box.getY(), enemyField);
                }
                //иначе отрисовываем той картинкой которая хранится в матрице - для клеток нумерации столбцов и строк
                else g.drawImage(Picture.getImage(box.getPicture().name()), box.getX(), box.getY(), enemyField);
            }
        }
    }

    //метод который который отрабатывает после нажатия кнопки СтартИгры
    public void startGame() {
        if (controller.checkFullSetShips()) { // проверка на то, что игрок добавил полный комплект кораблей
            //вызываем окно куда нужно вводить номер игровой комнаты, а также нажать кнопки "создать комнату" либо "подключиться к комнате"
            String[] options = {"Создать комнату", "Подключиться к комнате"};
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Создайте комнату, введя 4-ех значный номер комнаты,");
            JLabel label2 = new JLabel("либо подключитесь к уже созданной:");
            JTextField field = new JTextField(25);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(label1);
            panel.add(label2);
            panel.add(field);

            int selectedOption = JOptionPane.showOptionDialog(null, panel, "Создание комнаты:",
                    JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            try {
                if (selectedOption == 0) { //если отжали кнопку "создать комнату"
                    port = Integer.parseInt(field.getText().trim());
                    controller.createGameRoom(port); //создается игровая комната (запускается сервер с сокетными соединениями)
                    buttons.revalidate();
                    View.callInformationWindow("Ожидаем соперника: после того как соперник подключиться к комнате, появится уведомление. Затем начнется игра. Ваш ход первый.");
                    controller.connectToRoom(port); //коннект клиента к серверу
                    View.callInformationWindow("Второй игрок подключился! Можно начинать сражение.");
                    refreshGuiAfterConnect(); //обновление интерфейса клиента после подключения второго игрока
                    buttons.getExitButton().setEnabled(true); //активация кнопки Выхода
                    enemyField.addListener(); //добавляем слушателя к объекту панели игрового поля соперника
                } else if (selectedOption == 1) { //если отжата кнопка "подключиться к комнате"
                    port = Integer.parseInt(field.getText().trim());
                    controller.connectToRoom(port); //коннект клиента к серверу
                    View.callInformationWindow("Вы успешно подключились к комнате. Ваш соперник ходит первым.");
                    refreshGuiAfterConnect(); //обновление интерфейса клиента после подключения
                    new ReceiveThread().start(); //запуск нити, которая ожидает сообщение от сервера
                }
            } catch (Exception e) {
                View.callInformationWindow("Произошла ошибка при создании комнаты, либо некорректный номер комнаты, попробуйте еще раз.");
                e.printStackTrace();
            }
        } else View.callInformationWindow("Вы добавили не все корабли на своем поле!");
    }

    public static int getPort(){
        return port;
    }

    //метод отключеия клиента от сервера
    public void disconnectGameRoom() {
        try {
            controller.disconnectGameRoom();
            View.callInformationWindow("Вы отключились от комнаты. Игра окончена. Вы потерпели техническое поражение.");
            enemyField.removeListener(); //ужаляем слушателя у панели игрового поля соперника
        } catch (Exception e) {
            View.callInformationWindow("Произошла ошибка при отключении от комнаты.");
        }
    }

    //обновляет интерфейс клиента после подключения обоих игроков
    public void refreshGuiAfterConnect() {
        MouseListener[] listeners = myField.getMouseListeners();
        for (MouseListener lis : listeners) {
            myField.removeMouseListener(lis); //удаление слушателя у панели нашего игрового поля
        }
        selectPanel.setVisible(false);
        remove(selectPanel);          //удаление панели настроек добавления корабля
        add(enemyField = new EnemyField(this), BorderLayout.EAST); //добавление панели игрового поля соперника
        enemyField.repaint(); //отрисовка поля соперника
        pack();  //репак формы
        buttons.getStartGameButton().setEnabled(false); //деактивация кнопки "Начать игру"
        revalidate();
    }

    //отправление на сервер сообщения с координатами отстреленной клетки
    public void sendShot(int x, int y) {
        try {
            boolean isSendShot = controller.sendMessage(x, y); //непосредственная отправка сообщения через контроллер
            if (isSendShot) { //если сообщение отправлено, то ...
                enemyField.repaint(); //переотрисовка поля соперника
                enemyField.removeListener(); //удаление слушателя у панели поля соперника
                buttons.getExitButton().setEnabled(false); //деактивация кнопки выхода
                new ReceiveThread().start(); //запуск нити, которая ожидает сообщение от сервера
            }
        } catch (Exception e) {
            View.callInformationWindow("Произошла ошибка при отправке выстрела.");
            e.printStackTrace();
        }
    }

    //класс-поток, который ожидает сообщение от сервера
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                boolean continueGame = controller.receiveMessage(); //контроллер принял сообщение
                myField.repaint();
                if (continueGame) { //если вернулось true, то...
                    buttons.getExitButton().setEnabled(true); //активация кнопки выход
                    enemyField.addListener();  //добавление слушателя к полю соперника
                } else { //если вернлось false то игра окончена
                    buttons.getExitButton().setEnabled(false);
                    enemyField.removeListener();
                    buttons.getRestartGameButton().setEnabled(true);
                }

            } catch (IOException | ClassNotFoundException e) {
                View.callInformationWindow("Произошла ошибка при приеме сообщения от сервера");
                e.printStackTrace();
            }
        }
    }
}
