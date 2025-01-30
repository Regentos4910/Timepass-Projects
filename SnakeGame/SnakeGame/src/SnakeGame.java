import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private static final int TILE_SIZE = 25; // Size of each grid cell
    private static final int WIDTH = 800; // Width of the game board
    private static final int HEIGHT = 600; // Height of the game board
    private static final int GAME_SPEED = 100; // Game speed in milliseconds

    private final LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private Direction direction = Direction.RIGHT;
    private boolean running = false;
    private Timer timer;
    private int foodCounter = 0;
    private int bestScore = 0;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        startGame();
    }

    private void startGame() {
        running = true;
        direction = Direction.RIGHT;
        foodCounter = 0;
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        generateFood();
        timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    private void handleKeyPress(KeyEvent e) {
        if (!running) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                startGame();
            }
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != Direction.DOWN) direction = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                if (direction != Direction.UP) direction = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                if (direction != Direction.RIGHT) direction = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != Direction.LEFT) direction = Direction.RIGHT;
                break;
        }
    }

    private void generateFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        int y = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
        food = new Point(x, y);

        while (snake.contains(food)) {
            x = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
            y = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
            food = new Point(x, y);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, TILE_SIZE, TILE_SIZE);

            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x, point.y, TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Food: " + foodCounter, 10, 20);
        } else {
            showGameOver(g);
        }
    }

    private void showGameOver(Graphics g) {
        String gameOverMessage = "Game Over!";
        String scoreMessage = "Your Score: " + foodCounter;
        String bestScoreMessage = "Best Score: " + bestScore;
        String restartMessage = "Press Enter to Restart";

        if (foodCounter > bestScore) {
            bestScore = foodCounter;
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics metrics = getFontMetrics(g.getFont());

        int x = (WIDTH - metrics.stringWidth(gameOverMessage)) / 2;
        int y = HEIGHT / 3;
        g.drawString(gameOverMessage, x, y);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        metrics = getFontMetrics(g.getFont());
        x = (WIDTH - metrics.stringWidth(scoreMessage)) / 2;
        y += 50;
        g.drawString(scoreMessage, x, y);

        x = (WIDTH - metrics.stringWidth(bestScoreMessage)) / 2;
        y += 30;
        g.drawString(bestScoreMessage, x, y);

        x = (WIDTH - metrics.stringWidth(restartMessage)) / 2;
        y += 50;
        g.drawString(restartMessage, x, y);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = switch (direction) {
            case UP -> new Point(head.x, head.y - TILE_SIZE);
            case DOWN -> new Point(head.x, head.y + TILE_SIZE);
            case LEFT -> new Point(head.x - TILE_SIZE, head.y);
            case RIGHT -> new Point(head.x + TILE_SIZE, head.y);
        };

        // Check for collisions
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT || snake.contains(newHead)) {
            running = false;
            timer.stop();
        } else {
            snake.addFirst(newHead);

            if (newHead.equals(food)) {
                foodCounter++;
                generateFood();
            } else {
                snake.removeLast();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();
        frame.add(gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
