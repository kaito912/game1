import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Shooting_Game extends JFrame {
    final int windowWidth = 600;
    final int windowHeight = 700;

    public static void main(String[] args) {
        new Shooting_Game();
    }

    public Shooting_Game() {
        Dimension dimOfScreen = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(dimOfScreen.width / 2 - windowWidth / 2, dimOfScreen.height / 2 - windowHeight / 2, windowWidth, windowHeight);
        setResizable(false);
        setTitle("Software Development II");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        MyJPanel panel = new MyJPanel();
        Container c = getContentPane();
        c.add(panel);
        setVisible(true);
    }

    public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
        /* 全体の設定に関する変数 */
        Dimension dimOfPanel;
        Timer timer;
        ImageIcon iconBackground,iconMe, iconEnemy;
        Image imgBackground,imgMe, imgEnemy;
        JLabel Titele,START;
        int Mode = 1;
        boolean isButtonClicked = false;

        /* 自機に関する変数 */
        int numOfMyMissile = 3;
        int myHeight, myWidth;
        int myX, myY, tempMyX;
        int gap = 100;
        int[] myMissileX = new int[numOfMyMissile];
        int[] myMissileY = new int[numOfMyMissile];
        boolean[] isMyMissileActive = new boolean[numOfMyMissile];

        /* 敵機に関する変数 */
        int numOfEnemy = 12;
        int numOfAlive = numOfEnemy;
        int enemyWidth, enemyHeight;
        int enemyMoveCount = 0;
        int enemyComeSpeed = 10;
        int[] enemyX = new int[numOfEnemy];
        int[] enemyY = new int[numOfEnemy];
        int[] enemyMove = new int[numOfEnemy];
        int[] enemyMissileX = new int[numOfEnemy];
        int[] enemyMissileY = new int[numOfEnemy];
        int[] enemyMissileSpeed = new int[numOfEnemy];
        boolean[] isEnemyAlive = new boolean[numOfEnemy];
        boolean[] isEnemyMissileActive = new boolean[numOfEnemy];

        /* コンストラクタ（ゲーム開始時の初期化） */
        public MyJPanel() {
            // 全体の設定
            
            addMouseListener(this);
            addMouseMotionListener(this);
            timer = new Timer(50, this);
            timer.start();

            // 画像の取り込み
            imgBackground = getImg("haikei.jpg");

            imgMe = getImg("jiki.jpg");
            myWidth = imgMe.getWidth(this);
            myHeight = imgMe.getHeight(this);

            imgEnemy = getImg("teki.jpg");
            enemyWidth = imgEnemy.getWidth(this);
            enemyHeight = imgEnemy.getHeight(this);

            // 自機と敵機の初期化
            initMyPlane();
            initEnemyPlane();
        }

        /* パネル上の描画 */
        public void paintComponent(Graphics g) {
            
            dimOfPanel = getSize();
            super.paintComponent(g);
            // スタート画面の描写
            if(Mode == 1){
                drawMenu(); //メニューボタン
                drawBackground(g); // 背景

            }else if (Mode == 2){
                // 各要素の描画
                drawBackground(g); // 背景
                drawMyPlane(g); // 自機
                drawMyMissile(g); // 自機のミサイル
                drawEnemyPlane(g); // 敵機
                drawEnemyMissile(g); // 敵機のミサイル
                
                // 敵機を全機撃墜した時の終了処理
                if (numOfAlive == 0) {
                    removeAll();
                    repaint();
                    Mode = 1;
                }
            }   
        }

        /* 一定時間ごとの処理（ActionListener に対する処理） */
        public void actionPerformed(ActionEvent e) {
            //String cmd = e.getActionCommand();
            if(Mode == 1){
                if(isButtonClicked){
                    removeAll();
                    Mode = 2;
                    repaint();
                    isButtonClicked = false;
                }
            }else if (Mode == 2){
                repaint();
            }
        }

        /* MouseListener に対する処理 */    
        // マウスボタンをクリックする
        public void mouseClicked(MouseEvent e) {
            if(Mode == 1)
                isButtonClicked = true;
        }
        
        public void mousePressed(MouseEvent e) {
            for (int i = 0; i < numOfMyMissile; i++) {
                if (!isMyMissileActive[i]) {
                    myMissileX[i] = tempMyX + myWidth / 2;
                    myMissileY[i] = myY;
                    isMyMissileActive[i] = true;
                    break;
                }
            }
        }

        // マウスボタンを離す
        public void mouseReleased(MouseEvent e) {
        }

        // マウスが領域外へ出る
        public void mouseExited(MouseEvent e) {
        }

        // マウスが領域内に入る
        public void mouseEntered(MouseEvent e) {
            /* repaint(); */
        }

        /* MouseMotionListener に対する処理 */
        // マウスを動かす
        public void mouseMoved(MouseEvent e) {
           /*  repaint(); */
            myX = e.getX();
        }

        // マウスをドラッグする
        public void mouseDragged(MouseEvent e) {
            /* repaint(); */
            myX = e.getX();
        }

        /* 画像ファイルから Image クラスへの変換 */
        public Image getImg(String filename) {
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage();

            return img;
        }

        /* 自機の初期化 */
        public void initMyPlane() {
            myX = windowWidth / 2;
            myY = windowHeight - 100;
            tempMyX = windowWidth / 2;

            for (int i = 0; i < numOfMyMissile; i++) {
                isMyMissileActive[i] = false;
            }
        }

        /* 敵機の初期化 */
        public void initEnemyPlane() {
            for (int i = 0; i < 7; i++) {
                enemyX[i] = 70 * i;
                enemyY[i] = 50;
            }

            for (int i = 7; i < numOfEnemy; i++) {
                enemyX[i] = 70 * (i - 6);
                enemyY[i] = 100;
            }

            for (int i = 0; i < numOfEnemy; i++) {
                isEnemyAlive[i] = true;
                enemyMove[i] = 1;
            }

            for (int i = 0; i < numOfEnemy; i++) {
                isEnemyMissileActive[i] = true;
                enemyMissileX[i] = enemyX[i] + enemyWidth / 2;
                enemyMissileY[i] = enemyY[i];
                enemyMissileSpeed[i] = 20 + (i % 4);
            }
        }

        /* 背景の描写 */
        public void drawBackground(Graphics g){
            g.drawImage(imgBackground, 0, 0, this);
        }

        /* 自機の描画 */
        public void drawMyPlane(Graphics g) {
            if (Math.abs(tempMyX - myX) < gap) {
                if (myX < 0) {
                    myX = 0;
                } else if (myX + myWidth > dimOfPanel.width) {
                    myX = dimOfPanel.width - myWidth;
                }
                tempMyX = myX;
                g.drawImage(imgMe, tempMyX, myY, this);
            } else {
                g.drawImage(imgMe, tempMyX, myY, this);
            }
        }


        /* 自機のミサイルの描画 */
        public void drawMyMissile(Graphics g) {
            for (int i = 0; i < numOfMyMissile; i++) {
                if (isMyMissileActive[i]) {
                    // ミサイルの配置
                    myMissileY[i] -= 15;
                    g.setColor(Color.white);
                    g.fillRect(myMissileX[i], myMissileY[i], 6, 10);

                    // 自機のミサイルの敵機各機への当たり判定,およびスコアの表示
                    for (int j = 0; j < numOfEnemy; j++) {
                        if (isEnemyAlive[j]) {
                            if ((myMissileX[i] >= enemyX[j]) && (myMissileX[i] <= enemyX[j] + enemyWidth)
                                    && (myMissileY[i] >= enemyY[j]) && (myMissileY[i] <= enemyY[j] + enemyHeight)) {
                                isEnemyAlive[j] = false;
                                isMyMissileActive[i] = false;
                                numOfAlive--;
                            }
                        }
                    }
                    // ミサイルがウィンドウ外に出たときのミサイルの再初期化
                    if (myMissileY[i] < 0)
                        isMyMissileActive[i] = false;
                }
            }
        }

        /* 敵機の描画 */
        public void drawEnemyPlane(Graphics g) {
            for (int i = 0; i < numOfEnemy; i++) {
                if (isEnemyAlive[i]) {
                    if (enemyX[i] > dimOfPanel.width - enemyWidth) {
                        enemyMove[i] = -1;
                    } else if (enemyX[i] < 0) {
                        enemyMove[i] = 1;
                    }
                    enemyX[i] += enemyMove[i] * 10;
                    if(enemyMoveCount< 100){
                        enemyMoveCount += 1;
                    }else if (enemyMoveCount >= 100){
                        enemyMoveCount += 3;
                    }
                    g.drawImage(imgEnemy, enemyX[i], enemyY[i], this);
                }
                if (enemyMoveCount % 7 == 0){
                    enemyY[i] += enemyComeSpeed;
                }

                //敵機が自機に当たった時の当たり判定
                if((enemyX[i] >= myX) && (enemyX[i] <= myX + myWidth)
                    && (enemyY[i] >= myY) && (enemyY[i] <= myY + myHeight)){
                        removeAll();
                        repaint();
                        initMyPlane();
                        initEnemyPlane();
                        Mode = 1;
                
                }
            }
        }

        /* 敵機のミサイルの描画 */
        public void drawEnemyMissile(Graphics g) {
            for (int i = 0; i < numOfEnemy; i++) {
                // ミサイルの配置
                if (isEnemyMissileActive[i]) {
                    enemyMissileY[i] += enemyMissileSpeed[i];
                    g.setColor(Color.red);
                    g.fillRect(enemyMissileX[i], enemyMissileY[i], 6, 10);
                }

                // 敵機のミサイルの自機への当たり判定
                if ((enemyMissileX[i] >= tempMyX) && (enemyMissileX[i] <= tempMyX + myWidth)
                        && (enemyMissileY[i] + 5 >= myY) && (enemyMissileY[i] + 5 <= myY + myHeight)) {
                            removeAll();
                            initMyPlane();
                            initEnemyPlane();
                            Mode = 1;
                            repaint();
                }

                // ミサイルがウィンドウ外に出たときのミサイルの再初期化
                if (enemyMissileY[i] > dimOfPanel.height) {
                    if (isEnemyAlive[i]) {
                        enemyMissileX[i] = enemyX[i] + enemyWidth / 2;
                        enemyMissileY[i] = enemyY[i] + enemyHeight;
                    } else {
                        isEnemyMissileActive[i] = false;
                    }
                }
            }
        }

        /* メニュー画面の表示 */
        public void drawMenu() {

            Titele = new JLabel("SPACE WARS");
            Titele.setFont(new Font("MS ゴシック", Font.PLAIN, 26));
            Titele.setForeground(Color.white);
            Titele.setBounds(windowWidth / 3, windowHeight / 6, windowWidth / 3, windowHeight / 6);

            START = new JLabel("GAME START");
            START.setFont(new Font("MS ゴシック",Font.PLAIN, 26));
            START.setForeground(Color.white);
            START.setBounds(windowWidth / 3, windowHeight * 3 / 5, windowWidth / 3, windowHeight / 6);
            
            add(Titele);
            add(START);
        }
    }
}