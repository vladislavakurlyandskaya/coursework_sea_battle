package mvc.panels;

import mvc.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс, реализующий панель кнопок
 * @author Курляндская Владислава
 * @version 2.1
 */
public class Buttons extends JPanel {
    private View view;
    /** Кнопка "Начать игру"*/
    private JButton startGameButton;
    /** Кнопка "Прервать игру"*/
    private JButton exitButton;
    /** Кнопка "Играть еще"*/
    private JButton restartGameButton;

    /**
     * Конструктор класса, в котором мы набрасываем на форму элементы интерфейса
     * @param view
     */
    public Buttons(View view) {
        this.view = view;
        setLayout(null);
        setPreferredSize(new Dimension(300, 50));

        startGameButton = new JButton("Начать игру");
        startGameButton.setBounds(15, 5, 160, 40);
        startGameButton.addActionListener(new ActionButtonStartClass());

        exitButton = new JButton("Прервать игру");
        exitButton.setBounds(195, 5, 160, 40);
        exitButton.addActionListener(new ActionButtonDisconnect());
        exitButton.setEnabled(false);

        restartGameButton = new JButton("Играть еще");
        restartGameButton.setBounds(390, 5, 160, 40);
        restartGameButton.addActionListener(new ActionButtonRestartGame());
        restartGameButton.setEnabled(false);

        add(startGameButton);
        add(exitButton);
        add(restartGameButton);
    }

    public JButton getStartGameButton() {
        return startGameButton;
    }

    public JButton getRestartGameButton() {
        return restartGameButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    /**
     * Класс-слушатель для кнопки "Начать игру"
     */
    private class ActionButtonStartClass implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.startGame();
        }
    }

    /**
     * Класс-слушатель для кнопки "Прервать игру"
     */
    private class ActionButtonDisconnect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.disconnectGameRoom();
            exitButton.setEnabled(false);
            restartGameButton.setEnabled(true);
        }
    }

    /**
     * Класс-слушатель для кнопки "Играть еще"
     */
    private class ActionButtonRestartGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.init();
        }
    }
}
