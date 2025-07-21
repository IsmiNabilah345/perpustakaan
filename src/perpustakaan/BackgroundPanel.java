package perpustakaan;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        // Pastikan path-nya sesuai letak gambar relatif dari folder src
    	backgroundImage = new ImageIcon(getClass().getResource("/perpustakaan/image/4.jpg")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}