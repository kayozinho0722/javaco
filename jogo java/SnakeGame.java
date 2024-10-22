// src/SnakeGame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 30;
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private char direction = ' ';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.YELLOW);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> direction = 'U';
                    case KeyEvent.VK_DOWN -> direction = 'D';
                    case KeyEvent.VK_LEFT -> direction = 'L';
                    case KeyEvent.VK_RIGHT -> direction = 'R';
                }
            }
        });
        startGame();
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        direction = ' ';
        running = true;
        timer = new Timer(100, this);
        timer.start();
        spawnFood();
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        int y = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for (Point p : snake) {
            g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x, food.y, TILE_SIZE, TILE_SIZE);
        if (!running) {
            showGameOver(g);
        }
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH / 4, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();
            checkCollision();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = new Point(head);
        switch (direction) {
            case 'U' -> newHead.y -= TILE_SIZE;
            case 'D' -> newHead.y += TILE_SIZE;
            case 'L' -> newHead.x -= TILE_SIZE;
            case 'R' -> newHead.x += TILE_SIZE;
        }
        snake.add(0, newHead);
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT || snake.subList(1, snake.size()).contains(head)) {
            running = false;
            timer.stop();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogo da Cobrinha");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
