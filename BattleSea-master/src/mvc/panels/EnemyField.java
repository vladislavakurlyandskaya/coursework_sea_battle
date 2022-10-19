package mvc.panels;

import mvc.Picture;
import mvc.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Класс, реализующий поле соперника
 * @author Курляндская Владислава
 * @version 2.1
 */
public class EnemyField extends JPanel {
    private View view;

    /**
     * Конструктор класса, в котором с помощью метода setPreferredSize() устанавливается размер поля, где отрисовываются корабли
     * @param view
     */
    public EnemyField(View view) {
        this.view = view;
        this.setPreferredSize(new Dimension(Picture.COLUMNS * Picture.IMAGE_SIZE, Picture.ROWS * Picture.IMAGE_SIZE));
    }

    /** Переопределяем метод paintComponent, который вызывается при отрисовке компонентов*/
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.repaintEnemyField(g);
    }

    /**
     * Процедура, которая добавляет слушателя к панели поля соперника
     */
    public void addListener() {
        addMouseListener(new ActionMouse());
    }

    /**
     * Процедура, которая удаляет слушателя у панели поля соперника
     */
    public void removeListener() {
        MouseListener[] listeners = getMouseListeners();
        for (MouseListener lis : listeners) {
            removeMouseListener(lis);
        }
    }

    /**
     * Класс-слушатель на нажатие кнопкой мыши по панели.
     * Получает координаты , округляет их,  проверяет, не задействованы ли они на поле, и отправляет координаты сопернику
     */
    private class ActionMouse extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int x = (e.getX() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            int y = (e.getY() / Picture.IMAGE_SIZE) * Picture.IMAGE_SIZE;
            if (x >= Picture.IMAGE_SIZE && y >= Picture.IMAGE_SIZE) {
                view.sendShot(x, y);
            }
        }
    }
}
