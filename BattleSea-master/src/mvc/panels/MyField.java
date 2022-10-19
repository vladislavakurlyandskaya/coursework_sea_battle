package mvc.panels;

import mvc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Класс, реализующий поле игрока
 * @author Курляндская Владислава
 * @version 2.1
 */
public class MyField extends JPanel {
    private View view;
    private SelectPanel selectPanel;

    public void setSelectPanel(SelectPanel selectPanel) {
        this.selectPanel = selectPanel;
    }

    /**
     * Конструктор класса, в котором с помощью метода setPreferredSize() устанавливается размер поля
     * @param view
     */
    public MyField(View view) {
        this.view = view;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
        this.addMouseListener(new ActionMouse());
    }

    /** Переопределяем метод paintComponent, который вызывается при отрисовке компонентов*/
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.repaintMyField(g);
    }

    /**
     * Класс-слушатель на нажатие кнопкой мыши по панели
     */
    private class ActionMouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            //получаем координаты и округляем
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            //получаем количество палуб и ориентацию корабля
            int countDeck = selectPanel.getCountDeck();
            int placement = selectPanel.getPlacement();
            Ship ship;
            Ship removedShip;
            //если была нажата ЛКМ то добавляем корабль
            if (e.getButton() == MouseEvent.BUTTON1 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE)) {
                if (countDeck > 0 && countDeck <= 4) {
                    switch (placement) {
                        case 1: {
                            ship = new Ship(countDeck, false);
                            ship.createVerticalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        case 2: {
                            ship = new Ship(countDeck, true);
                            ship.createHorizontalShip(x, y);
                            view.addShip(ship);
                            break;
                        }
                        default:
                            View.callInformationWindow("Не выбрана ориентацию размещения корабля!");
                    }
                } else {
                    View.callInformationWindow("Не выбрано количество палуб корабля!");
                    return;
                }
                //если ПКМ то удаляем орабль
            } else if (e.getButton() == MouseEvent.BUTTON3 && (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) &&
                    (removedShip = view.removeShip(x, y)) != null) {
            }
            repaint(); //переотрисовываем панель
        }
    }
}
