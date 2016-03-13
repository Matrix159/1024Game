package game1024.ui;

import game1024.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by Joshua Eldridge
 */
public class GUI1024 implements KeyListener, ActionListener {

    private final static int DEFAULT_SIZE = 4;
    private JFrame top;
    private JPanel gamePanel;
    private JLabel[][] gameBoardUI;
    public NumberSlider gameLogic;
    private JMenuItem reset, quit;

    public GUI1024()
    {
        top = new JFrame ("Welcome to 1024!");
        gameLogic = new GameLogic(DEFAULT_SIZE, DEFAULT_SIZE, 2048);
        gameLogic.resizeBoard(DEFAULT_SIZE, DEFAULT_SIZE, 2048);
        gamePanel = new JPanel();
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gamePanel.setLayout(new GridLayout(DEFAULT_SIZE, DEFAULT_SIZE));
        gamePanel.addKeyListener(this);
        top.add(gamePanel, BorderLayout.CENTER);

        gameBoardUI = new JLabel[DEFAULT_SIZE][DEFAULT_SIZE];
        Font myTextFont = new Font(Font.SERIF, Font.BOLD, 40);
        for (int k = 0; k < gameBoardUI.length; k++)
            for (int m = 0; m < gameBoardUI[k].length; m++)
            {
                gameBoardUI[k][m] = new JLabel("X", JLabel.CENTER);
                gameBoardUI[k][m].setFont(myTextFont);
                gameBoardUI[k][m].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                gameBoardUI[k][m].setPreferredSize(new Dimension (200, 200));
                gamePanel.add(gameBoardUI[k][m]);
            }

        JMenuBar mb = new JMenuBar();
        top.setJMenuBar(mb);
        JMenu game = new JMenu("Game");
        mb.add(game);
        reset = new JMenuItem ("Reset");
        reset.addActionListener(this);
        quit = new JMenuItem("Quit");
        quit.addActionListener(this);
        game.add(reset);
        game.addSeparator();
        game.add(quit);

        updateBoard();
        //top.setPreferredSize(new Dimension(800, 600));
        top.pack();
        top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        top.setVisible(true);
        gamePanel.requestFocus();

    }

    private void updateBoard()
    {
        for (JLabel[] row : gameBoardUI)
            for (JLabel s : row) {
                s.setText("");
            }

        ArrayList<Cell> out = gameLogic.getNonEmptyTiles();
        if (out == null) {
            JOptionPane.showMessageDialog(top, "Incomplete implementation getNonEmptyTiles()");
            return;
        }
        for (Cell c : out) {
            JLabel z = gameBoardUI[c.row][c.column];
            z.setText(String.valueOf(Math.abs(c.value)));
            z.setForeground(c.value > 0 ? Color.BLACK : Color.RED);
            z.setBackground(Color.BLUE);
        }
    }
    public static void main (String[] args)
    {
        GUI1024 gui = new GUI1024();
        /*try {
            Robot robot = new Robot();


            int delay = 50; //milliseconds
            long start = System.nanoTime();
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if(gui.gameLogic.getStatus() == GameStatus.USER_WON)
                    {
                        System.out.println("Game won after: " + (System.nanoTime() - start) / 1000000000 +
                                " seconds");
                        System.exit(0);
                    }
                    robot.keyPress(KeyEvent.VK_UP);
                    robot.keyRelease(KeyEvent.VK_UP);
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                    robot.keyPress(KeyEvent.VK_DOWN);
                    robot.keyRelease(KeyEvent.VK_DOWN);
                    robot.keyPress(KeyEvent.VK_LEFT);
                    robot.keyRelease(KeyEvent.VK_LEFT);
                }
            };
            new Timer(delay, taskPerformer).start();

        } catch (AWTException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_R)
        {
            gameLogic.reset();
            updateBoard();
            return;
        }
        boolean moved = false;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                moved = gameLogic.slide(SlideDirection.UP);
                break;
            case KeyEvent.VK_LEFT:
                moved = gameLogic.slide(SlideDirection.LEFT);
                break;
            case KeyEvent.VK_DOWN:
                moved = gameLogic.slide(SlideDirection.DOWN);
                break;
            case KeyEvent.VK_RIGHT:
                moved = gameLogic.slide(SlideDirection.RIGHT);
                break;
        }
        if (moved) {
            updateBoard();
            if (gameLogic.getStatus().equals(GameStatus.USER_WON))
                JOptionPane.showMessageDialog(null, "You won");
            else if (gameLogic.getStatus().equals(GameStatus.USER_LOST)) {
                int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over!",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    gameLogic.reset();
                    updateBoard();
                }
                else {
                    System.exit(0);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed (ActionEvent e)
    {
        Object which = e.getSource();
        if (which == reset) {
            gameLogic.reset();
            updateBoard();
        }
        else {
            System.exit(1);
        }
    }


}