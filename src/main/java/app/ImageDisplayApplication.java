package main.java.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URL;

public class ImageDisplayApplication {
    enum ANIMAL {
        THANK_YOU("정말고마 워요", "https://drive.google.com/uc?export=view&id=10W6QyCD47bcWMMi5aBIYryEZhH_YH3EN")
        , HUG_ME("안아줘요", "https://drive.google.com/uc?export=view&id=1qNKXqObLise3FCK819cQ9qQD64Tqo_VP")
        , PLEASE_HUG_ME("제발 안아줘요", "https://drive.google.com/uc?export=view&id=1TNYMbVIXfuLHyu2memobbkBhtUH7zGt_")
        , I_LOVE_YOU("좋아해요", "https://drive.google.com/uc?export=view&id=1PdTqgh6XAy0eJuwapkF58Odpan_2U7uE")
        , GET_OUT_OF_HERE("나가줘요", "https://drive.google.com/uc?export=view&id=1qVgizqKa5cKQS0YoEEUT5J7fdGM_53qX")
        , END_ME("끝내줘요", "https://drive.google.com/uc?export=view&id=1NKJ5H5QBuPE0yswRZ_hVEXOxYm7sEZ7A");

        private String name;
        private String imageUrl;

        ANIMAL(String name, String imageUrl) {
            this.name = name;
            this.imageUrl = imageUrl;
        }
        public String getName() {
            return name;
        }
        public String getImageUrl() {
            return imageUrl;
        }
    }
    private static Point mouseOffset;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Maximum time between two clicks to be considered a double-click
    private static long lastClickTime = 0;
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setAlwaysOnTop(true);
        frame.setFocusableWindowState(false);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); // Center the frame on the screen


        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.NORTH);

        JLabel label = new JLabel("이미지 선택:");
        panel.add(label);

        String[] options = {
                ANIMAL.THANK_YOU.getName()
                , ANIMAL.I_LOVE_YOU.getName()
                , ANIMAL.HUG_ME.getName()
                , ANIMAL.PLEASE_HUG_ME.getName()
                , ANIMAL.GET_OUT_OF_HERE.getName()
                , ANIMAL.END_ME.getName()
        };
        JComboBox<String> comboBox = new JComboBox<>(options);
        panel.add(comboBox);

        JButton button = new JButton("가즈아");
        panel.add(button);

        // "숨기기" 버튼
        JButton hideButton = new JButton("숨겨줘요");
        panel.add(hideButton);

        // "종료" 버튼
        JButton exitButton = new JButton("종료해요");
        panel.add(exitButton);

        // "숨기기" 버튼의 클릭 이벤트 처리
        hideButton.addActionListener(e -> frame.setVisible(false));

        // "종료" 버튼의 클릭 이벤트 처리
        exitButton.addActionListener(e -> System.exit(0)); // 프로그램 종료

        button.addActionListener((e) -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            try {
                if (ANIMAL.THANK_YOU.getName().equals(selectedOption)) {
                    showImage(ANIMAL.THANK_YOU.getImageUrl());
                } else if (ANIMAL.I_LOVE_YOU.getName().equals(selectedOption)) {
                    showImage(ANIMAL.I_LOVE_YOU.getImageUrl());
                } else if (ANIMAL.HUG_ME.getName().equals(selectedOption)) {
                    showImage(ANIMAL.HUG_ME.getImageUrl());
                } else if (ANIMAL.PLEASE_HUG_ME.getName().equals(selectedOption)) {
                    showImage(ANIMAL.PLEASE_HUG_ME.getImageUrl());
                } else if (ANIMAL.GET_OUT_OF_HERE.getName().equals(selectedOption)) {
                    showImage(ANIMAL.GET_OUT_OF_HERE.getImageUrl());
                } else if (ANIMAL.END_ME.getName().equals(selectedOption)) {
                    showImage(ANIMAL.END_ME.getImageUrl());
                } else {
                    JOptionPane.showMessageDialog(null, "선택된 이미지가 없습니다.");
                }
            } catch (IOException ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }); // eListener

        frame.setVisible(true);
    }

    private static void showImage(String imageUrl) throws IOException {
        // URL 객체 생성
        URL url = new URL(imageUrl);

        // 이미지 읽기
        Image image = ImageIO.read(url);

        // ImageIcon 생성
        ImageIcon imageIcon;

        // Modify the image size
        int newWidth = 150; // Specify the desired width
        int newHeight = 150; // Specify the desired height
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage);


        // Get the screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Create a full-screen window
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Remove window frame
        frame.setBackground(new Color(0, 0, 0, 0)); // Make the window transparent
        frame.setSize(screenWidth, screenHeight);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Create a JLabel to display the image
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(imageIcon);
        frame.getContentPane().add(imageLabel);

        // Set the initial image position
        int centerX = screenWidth / 2 - imageIcon.getIconWidth() / 2;
        int centerY = screenHeight / 2 - imageIcon.getIconHeight() / 2;
        imageLabel.setBounds(centerX, centerY, imageIcon.getIconWidth(), imageIcon.getIconHeight());

        // Make the window always on top
        frame.setAlwaysOnTop(true);

        // Disable window focus traversal
        frame.setFocusableWindowState(false);

        // Handle mouse press event to start dragging
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = e.getPoint();
                long clickTime = System.currentTimeMillis();

                // Check for double-click
                if (clickTime - lastClickTime <= DOUBLE_CLICK_TIME_DELTA) {
                    System.exit(0); // Exit the program on double-click
                }

                lastClickTime = clickTime;
            }
        });

        // Handle mouse drag event to update image position
        imageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen() - mouseOffset.x;
                int y = e.getYOnScreen() - mouseOffset.y;
                frame.setLocation(x, y);
            }
        });

        // Show the window
        frame.setVisible(true);
    }
}