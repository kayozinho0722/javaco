// src/SnakeGame.java
import javax.swing.*; // Importa componentes da interface gráfica do Swing
import java.awt.*; // Importa classes para manipulação de gráficos
import java.awt.event.ActionEvent; // Importa classe para eventos de ação
import java.awt.event.ActionListener; // Importa interface para ouvir eventos de ação
import java.awt.event.KeyAdapter; // Importa classe para adaptação de eventos de teclado
import java.awt.event.KeyEvent; // Importa classe para eventos de teclado
import java.util.ArrayList; // Importa a classe ArrayList para manipulação de listas dinâmicas
import java.util.Random; // Importa a classe Random para geração de números aleatórios

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 30; // Tamanho de cada bloco da cobrinha e comida
    private final int WIDTH = 600; // Largura da tela do jogo
    private final int HEIGHT = 600; // Altura da tela do jogo
    private final ArrayList<Point> snake = new ArrayList<>(); // Lista que representa a cobrinha
    private Point food; // Ponto que representa a comida
    private char direction = ' '; // Direção atual da cobrinha (U, D, L, R)
    private boolean running = false; // Indica se o jogo está em execução
    private Timer timer; // Temporizador para atualizar o jogo

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Define o tamanho preferido do painel
        setBackground(Color.YELLOW); // Define a cor de fundo do painel
        setFocusable(true); // Permite que o painel receba foco de entrada
        addKeyListener(new KeyAdapter() { // Adiciona um ouvinte para eventos de teclado
            @Override
            public void keyPressed(KeyEvent e) { // Método chamado quando uma tecla é pressionada
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> direction = 'U'; // Para cima
                    case KeyEvent.VK_DOWN -> direction = 'D'; // Para baixo
                    case KeyEvent.VK_LEFT -> direction = 'L'; // Para a esquerda
                    case KeyEvent.VK_RIGHT -> direction = 'R'; // Para a direita
                }
            }
        });
        startGame(); // Inicia o jogo
    }

    private void startGame() {
        snake.clear(); // Limpa a lista da cobrinha
        snake.add(new Point(WIDTH / 2, HEIGHT / 2)); // Adiciona a cabeça da cobrinha no centro
        direction = ' '; // Reinicia a direção
        running = true; // O jogo está em execução
        timer = new Timer(100, this); // Cria um temporizador que chama actionPerformed a cada 100 ms
        timer.start(); // Inicia o temporizador
        spawnFood(); // Gera a primeira comida
    }

    private void spawnFood() {
        Random random = new Random(); // Cria um gerador de números aleatórios
        int x = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE; // Gera coordenada x da comida
        int y = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE; // Gera coordenada y da comida
        food = new Point(x, y); // Define a posição da comida
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Chama o método da superclasse para limpar o painel
        g.setColor(Color.BLACK); // Define a cor da cobrinha
        for (Point p : snake) { // Para cada segmento da cobrinha
            g.fillRect(p.x, p.y, TILE_SIZE, TILE_SIZE); // Desenha o bloco
        }
        g.setColor(Color.RED); // Define a cor da comida
        g.fillRect(food.x, food.y, TILE_SIZE, TILE_SIZE); // Desenha a comida
        if (!running) { // Se o jogo não estiver em execução
            showGameOver(g); // Exibe a mensagem de Game Over
        }
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.BLACK); // Define a cor do texto
        g.setFont(new Font("Arial", Font.BOLD, 40)); // Define a fonte do texto
        g.drawString("Game Over", WIDTH / 4, HEIGHT / 2); // Exibe a mensagem de Game Over
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) { // Se o jogo está em execução
            moveSnake(); // Move a cobrinha
            checkCollision(); // Verifica se houve colisão
            repaint(); // Repaint o painel para atualizar a tela
        }
    }

    private void moveSnake() {
        Point head = snake.get(0); // Pega a cabeça da cobrinha
        Point newHead = new Point(head); // Cria um novo ponto para a nova cabeça
        switch (direction) { // Atualiza a posição da cabeça com base na direção
            case 'U' -> newHead.y -= TILE_SIZE; // Move para cima
            case 'D' -> newHead.y += TILE_SIZE; // Move para baixo
            case 'L' -> newHead.x -= TILE_SIZE; // Move para a esquerda
            case 'R' -> newHead.x += TILE_SIZE; // Move para a direita
        }
        snake.add(0, newHead); // Adiciona a nova cabeça na frente da lista
        if (newHead.equals(food)) { // Se a nova cabeça está na mesma posição que a comida
            spawnFood(); // Gera nova comida
        } else {
            snake.remove(snake.size() - 1); // Remove o último segmento da cobrinha
        }
    }

    private void checkCollision() {
        Point head = snake.get(0); // Pega a cabeça da cobrinha
        // Verifica se a cabeça colidiu com as bordas ou com o próprio corpo
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT || snake.subList(1, snake.size()).contains(head)) {
            running = false; // Para o jogo
            timer.stop(); // Para o temporizador
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jogo da Cobrinha"); // Cria uma nova janela
        SnakeGame game = new SnakeGame(); // Cria uma instância do jogo
        frame.add(game); // Adiciona o jogo à janela
        frame.pack(); // Ajusta o tamanho da janela
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define a operação de fechamento da janela
        frame.setVisible(true); // Torna a janela visível
    }
}
