package mvc.panels;

import mvc.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс, предназначенный для создания окна приложения
 * @author Курляндская Владислава
 * @version 2.1
 */
public class SelectPanel extends JPanel {
    private View view;
    /** Контейнер для выбора количества палуб корабля*/
    private JPanel panelDeck;
    /** Контейнер для выбора ориентации корабля*/
    private JPanel panelOrientation;
    /** Переключатель - Однопалубный корабль*/
    private JRadioButton oneDeck;
    /** Переключатель - Двухпалубный корабль*/
    private JRadioButton twoDeck;
    /** Переключатель - Трехпалубный корабль*/
    private JRadioButton threeDeck;
    /** Переключатель - Четырехпалубный корабль*/
    private JRadioButton fourDeck;
    /** Переключатель - Вертикальная ориентация*/
    private JRadioButton vertical;
    /** Переключатель - Горизонтальная ориентация*/
    private JRadioButton horizontal;
    /** Кнопка - Очистить поле */
    private JButton clearField;
    private ButtonGroup groupDeck;
    private ButtonGroup groupOrientation;
    /** Однострочное текстовое поле для ввода имени игрока */
    private TextField textFieldName;
    private JLabel jLabelName;

    /**
     * Конструктор класса, в котором мы набрасываем на форму все элементы интерфейса
     * @param view
     */
    public SelectPanel(View view) {
        this.view = view;
        setLayout(null);
        this.setPreferredSize(new Dimension(220, 400));

        panelDeck = new JPanel();
        panelDeck.setLayout(new BoxLayout(panelDeck, BoxLayout.Y_AXIS));
        panelDeck.setBounds(13, 40, 200, 115);

        panelOrientation = new JPanel();
        panelOrientation.setLayout(new BoxLayout(panelOrientation, BoxLayout.Y_AXIS));
        panelOrientation.setBounds(13, 180, 200, 70);

        clearField = new JButton("Очистить поле");
        clearField.setBounds(13, 270, 200, 40);
        clearField.addActionListener(new ActionClearField());

        panelDeck.setBorder(BorderFactory.createTitledBorder("Выберить количество палуб корабля"));
        panelOrientation.setBorder(BorderFactory.createTitledBorder("Выберите ориентацию корабля"));

        oneDeck = new JRadioButton("Однопалубный");
        twoDeck = new JRadioButton("Двухпалубный");
        threeDeck = new JRadioButton("Трехпалубный");
        fourDeck = new JRadioButton("Четырехпалубный");
        vertical = new JRadioButton("Вертикальная");
        horizontal = new JRadioButton("Горизонтальная");

        textFieldName = new TextField();
        jLabelName = new JLabel("Ваше имя");

        textFieldName.setBounds(13,340,200,20);
        jLabelName.setBounds(13,315,200,20);

        groupDeck = new ButtonGroup();
        groupOrientation = new ButtonGroup();

        panelDeck.add(oneDeck);
        panelDeck.add(twoDeck);
        panelDeck.add(threeDeck);
        panelDeck.add(fourDeck);
        panelOrientation.add(vertical);
        panelOrientation.add(horizontal);

        add(panelDeck);
        add(panelOrientation);
        add(clearField);
        add(jLabelName);
        add(textFieldName);
        groupDeck.add(oneDeck);
        groupDeck.add(twoDeck);
        groupDeck.add(threeDeck);
        groupDeck.add(fourDeck);
        groupOrientation.add(vertical);
        groupOrientation.add(horizontal);
    }

    /**
     * Функция, которая возвращает количество палуб в зависимости от того, какой переключатель выбран
     * @return Возвращает количество палуб корабля
     */
    public int getCountDeck() {
        if (oneDeck.isSelected()) return 1;
        else if (twoDeck.isSelected()) return 2;
        else if (threeDeck.isSelected()) return 3;
        else if (fourDeck.isSelected()) return 4;
        else return 0;
    }

    /**
     * Функция, которая возвращает число, обозначающее какая ориентация корабля выбрана
     * @return Возвращает ориетацию корабля
     */
    public int getPlacement() {
        if (vertical.isSelected()) return 1;
        else if (horizontal.isSelected()) return 2;
        else return 0;
    }

     /**
     * Класс-слушатель, который загружает пустое поле при нажатии кнопки "Убрать все корабли"
     */
    private class ActionClearField implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.loadEmptyMyField();
        }
    }

    /**
     * Функция, которая возвращает введенное имя игрока
     * @return Возвращает имя
     */
    public String getNamePlayer(){
        return textFieldName.getText();
    }

    /**
     * Функция, которая возвращает значение true, если текстовое поле имени игрока не содержит значения и false, есди содержит
     * @return Возвращает true или false
     */
    public boolean checkEnterNamePlayer(){
        if(textFieldName.getText().isEmpty())
            return false;
        else
            return true;
    }
}
