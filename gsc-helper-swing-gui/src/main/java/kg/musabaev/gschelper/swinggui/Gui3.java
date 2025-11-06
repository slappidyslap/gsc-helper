package kg.musabaev.gschelper.swinggui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Gui3 extends JFrame {

    public Gui3() throws HeadlessException {
        FlatDarkLaf.setup();
        FlatRobotoFont.install();
        Font robotoFont = new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16);
        UIManager.put("defaultFont", robotoFont);
        UIManager.put("TextComponent.arc", 7);
        UIManager.put("Button.arc", 7);
        UIManager.put("Label.background", Color.RED);
        UIManager.put("Button.background", Color.BLUE);
        UIManager.put("Button.margin", new Insets(10,10,10,10));

        double width = getWidth1(robotoFont, "Test Test:");

        JPanel parentPanel = new JPanel(new MigLayout("wrap 1, fillx, novisualpadding")) {{
            add(new JPanel(new MigLayout("novisualpadding", "[" + width + "][]", "")) {{
                add(new Label("Test:") {{
                    setOpaque(true);
                    setBackground(Color.RED);
                }});
                add(new JButton("inner panel 1"));
            }});
            add(new JPanel(new MigLayout("", "[" + width + "][]", "")) {{
                add(new Label("Test Test:") {{
                    setOpaque(true);
                    setBackground(Color.RED);
                }});
                add(new JButton("inner panel 2"));
            }});
        }};
        System.out.println(width);


        super.add(parentPanel);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(650, 200);
        super.setLocationRelativeTo(null);
    }

    private double getWidth2(Font font, String text) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D bounds = font.getStringBounds(text, frc);
        g2d.dispose();

        return bounds.getWidth();
    }

    private double getWidth1(Font font, String text) {
        FontMetrics fm = new Canvas().getFontMetrics(font);
        return fm.stringWidth(text);
    }

    public void showFrame() {
        super.setVisible(true);
    }
}
