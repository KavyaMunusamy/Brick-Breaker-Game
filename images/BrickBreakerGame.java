import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

class BrickMap {
    public int brickLayout[][];
    public int brickWidth;
    public int brickHeight;
    private Image carrotImage; // Added for carrot image

    public BrickMap(int rows, int columns, Image carrotImage) {
        this.carrotImage = carrotImage; // Initialize image
        brickLayout = new int[rows][columns];
        for (int i = 0; i < brickLayout.length; i++) {
            for (int j = 0; j < brickLayout[0].length; j++) {
                brickLayout[i][j] = 1;
            }
        }
        brickWidth = 500 / columns;
        brickHeight = 200 / rows;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < brickLayout.length; i++) {
            for (int j = 0; j < brickLayout[0].length; j++) {
                if (brickLayout[i][j] > 0) {
                    g.drawImage(carrotImage, j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight, null);
                    g.setStroke(new BasicStroke(5));
                    g.setColor(Color.GRAY);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        brickLayout[row][col] = value;
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private boolean isPlaying = true;
    private int playerScore = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;
    private BrickMap brickMap;
    private Image forestImage;
    private Image carrotImage;
    private Image rabbitImage;

    public GamePanel() {
        forestImage = new ImageIcon("C:\\Users\\Kavya\\OneDrive\\Desktop\\Project1\\Arial_View[1](1).jpg").getImage();
        carrotImage = new ImageIcon("C:\\Users\\Kavya\\OneDrive\\Desktop\\Project1\\lab.jpeg").getImage();
        rabbitImage = new ImageIcon("C:\\Users\\Kavya\\OneDrive\\Desktop\\Project1\\Kavya image.jpg").getImage();
        brickMap = new BrickMap(3, 7, carrotImage);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Ensure proper painting
        g.drawImage(forestImage, 0, 0, getWidth(), getHeight(), this); // Draw the background image
        brickMap.draw((Graphics2D) g);
        g.setColor(new Color(0x000000)); // Paddle color (Royal Blue)
        g.fillRect(playerX, 550, 100, 12);
        g.drawImage(rabbitImage, ballPosX, ballPosY, 20, 20, this); // Draw the rabbit image (ball)
        g.setColor(Color.blue);
        g.setFont(new Font("MV Boli", Font.BOLD, 25));
        g.drawString("Score: " + playerScore, 520, 30);
        if (totalBricks <= 0) {
            isPlaying = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(new Color(0xFFFFFF));
            g.setFont(new Font("MV Boli", Font.BOLD, 30));
            g.drawString("You Won!!!, Score: " + playerScore, 190, 300);
            g.setFont(new Font("MV Boli", Font.BOLD, 20));
            g.drawString("Press Enter to Restart!!!", 230, 350);
        }
        if (ballPosY > 570) {
            isPlaying = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("MV Boli", Font.BOLD, 30));
            g.drawString("GAME OVER!!!, Score: " + playerScore, 190, 300);
            g.setFont(new Font("MV Boli", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (isPlaying) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 12))) {
                ballYDir = -ballYDir;
            }
            for (int i = 0; i < brickMap.brickLayout.length; i++) {
                for (int j = 0; j < brickMap.brickLayout[0].length; j++) {
                    if (brickMap.brickLayout[i][j] > 0) {
                        int brickX = j * brickMap.brickWidth + 80;
                        int brickY = i * brickMap.brickHeight + 50;
                        int brickWidth = brickMap.brickWidth;
                        int brickHeight = brickMap.brickHeight;
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        if (ballRect.intersects(rect)) {
                            brickMap.setBrickValue(0, i, j);
                            totalBricks--;
                            playerScore += 5;
                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + rect.width)
                                ballXDir = -ballXDir;
                            else
                                ballYDir = -ballYDir;
                        }
                    }
                }
            }
            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if (ballPosX < 0 || ballPosX > getWidth() - 20) {
                ballXDir = -ballXDir;
            }
            if (ballPosY < 0) {
                ballYDir = -ballYDir;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= getWidth() - 100) {
                playerX = getWidth() - 100;
            } else {
                moveRight();
            }
        }
        if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!isPlaying) {
                isPlaying = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                playerScore = 0;
                totalBricks = 21;
                brickMap = new BrickMap(3, 7, carrotImage);
                repaint();
            }
        }
    }

    public void moveRight() {
        isPlaying = true;
        playerX += 50;
    }

    public void moveLeft() {
        isPlaying = true;
        playerX -= 50;
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }
}

class BrickBreakerGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel();
        frame.setBounds(10, 10, 700, 600);
        frame.setTitle("Brick Breaker");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePanel);
    }
}
