package form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ColorExtraction extends JFrame {

	JPanel np, cp, sp, np_op, np_sel;
	JRadioButton colorOption_Original, colorOption_Black, redSelect, greenSelect, blueSelect;
	JButton loadPath, outImage, saveImage, resetImage;
	ButtonGroup select = new ButtonGroup(), option = new ButtonGroup();
	JLabel im, jl;

	BufferedImage m_image = null, m_imageChange = null;

	public ColorExtraction() {
		setTitle("���� �����");
		setDefaultCloseOperation(2);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0); // ������ ���� ����
			}
		});
		add(np = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		add(cp = new JPanel(new FlowLayout(FlowLayout.RIGHT)), BorderLayout.CENTER);
		add(sp = new JPanel(), BorderLayout.SOUTH);

		np.add(np_sel = new JPanel(new FlowLayout(FlowLayout.LEFT)), BorderLayout.NORTH);
		np.add(np_op = new JPanel(new FlowLayout(FlowLayout.LEFT)), BorderLayout.CENTER);

		np_sel.add(jl = new JLabel("������ ��"));
		np_sel.add(redSelect = new JRadioButton("����"));
		np_sel.add(greenSelect = new JRadioButton("�ʷ�"));
		np_sel.add(blueSelect = new JRadioButton("�Ķ�"));

		select.add(redSelect);
		select.add(greenSelect);
		select.add(blueSelect);

		np_op.add(jl = new JLabel("���� �ɼ�"));
		np_op.add(colorOption_Original = new JRadioButton("����"));
		np_op.add(colorOption_Black = new JRadioButton("������"));

		option.add(colorOption_Original);
		option.add(colorOption_Black);

		cp.add(loadPath = new JButton("�̹��� �ҷ�����"));
		cp.add(outImage = new JButton("�̹��� �����ϱ�"));
		cp.add(saveImage = new JButton("�̹��� �����ϱ�"));
		cp.add(resetImage = new JButton("���� �̹�����"));

		redSelect.setSelected(true);
		colorOption_Original.setSelected(true);

		sp.add(im = new JLabel());

		loadPath.addActionListener(e -> {
			JFrame jf = new JFrame();

			FileDialog f = new FileDialog(jf, "�ҷ�����", FileDialog.LOAD);
			f.setVisible(true);
			if (f.getDirectory() != null) {
				try {
					m_image = ImageIO.read(new File(f.getDirectory().toString() + f.getFile()));
					im.setPreferredSize(new Dimension(m_image.getWidth(), m_image.getHeight()));
					im.setIcon(new ImageIcon(m_image));
					repaint();
					revalidate();
					pack();
					setLocationRelativeTo(null);
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "�̹��� ������ �ƴմϴ�.", "����", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		outImage.addActionListener(e -> {
			if (m_image == null) {
				JOptionPane.showMessageDialog(null, "�̹����� �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (redSelect.isSelected()) {
				m_imageChange = ImageCopy(m_image);
				GetRed(m_imageChange);
			} else if (greenSelect.isSelected()) {
				m_imageChange = ImageCopy(m_image);
				GetGreen(m_imageChange);
			} else if (blueSelect.isSelected()) {
				m_imageChange = ImageCopy(m_image);
				GetBlue(m_imageChange);
			}

			im.setIcon(new ImageIcon(m_imageChange));
			repaint();
			revalidate();
		});

		saveImage.addActionListener(e -> {
			if (m_image == null) {
				JOptionPane.showMessageDialog(null, "�̹����� �����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JFrame jf = new JFrame();
			FileDialog f = new FileDialog(jf, "�����ϱ�", FileDialog.SAVE);
			f.setVisible(true);
			if (f.getFile() != null) {
				try {
					ImageIO.write(m_imageChange, "jpg",
							new File(f.getDirectory().toString() + f.getFile().toString() + ".jpg"));
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});

		resetImage.addActionListener(e -> {
			im.setIcon(new ImageIcon(m_image));
			pack();
			repaint();
			revalidate();
		});

		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public void GetRed(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int maxrgb = 0, r = 0, g = 0, b = 0;
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				r = new Color(image.getRGB(w, h)).getRed();
				g = new Color(image.getRGB(w, h)).getGreen();
				b = new Color(image.getRGB(w, h)).getBlue();

				maxrgb = MaxRGB(r, g, b);// ���� ū RGB

				image = ColorEx(image, maxrgb, w, h, r, g, b, Color.red); // col1 = ���� ��
			}
		}
		m_imageChange = image;
	}

	public void GetGreen(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int maxrgb = 0, r = 0, g = 0, b = 0;
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				r = new Color(image.getRGB(w, h)).getRed();
				g = new Color(image.getRGB(w, h)).getGreen();
				b = new Color(image.getRGB(w, h)).getBlue();

				maxrgb = MaxRGB(r, g, b);

				image = ColorEx(image, maxrgb, w, h, g, r, b, Color.green); // col1 = ���� ��
			}
		}
		m_imageChange = image;
	}

	public void GetBlue(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int maxrgb = 0, r = 0, g = 0, b = 0;
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				r = new Color(image.getRGB(w, h)).getRed();
				g = new Color(image.getRGB(w, h)).getGreen();
				b = new Color(image.getRGB(w, h)).getBlue();

				maxrgb = MaxRGB(r, g, b);

				image = ColorEx(image, maxrgb, w, h, b, r, g, Color.blue);
			}
		}
		m_imageChange = image;
	}

	int MaxRGB(int r, int g, int b) { // ���� ū R G B
		int maxrgb = 0;
		if (maxrgb < r)
			maxrgb = r;
		if (maxrgb < g)
			maxrgb = g;
		if (maxrgb < b)
			maxrgb = b;
		return maxrgb;
	}

	BufferedImage ColorEx(BufferedImage image, int maxrgb, int w, int h, int col1, int col2, int col3, Color ch) {
		// ���� ����
		if (maxrgb < 200 && maxrgb > 190 && col1 < 70)
			image.setRGB(w, h, Color.white.getRGB());

		if (maxrgb < 160 && col1 < 40)
			image.setRGB(w, h, Color.white.getRGB());

		if (col1 == 255)
			image.setRGB(w, h, ReturnRGB(ch));
		if (col2 == 255)
			image.setRGB(w, h, Color.white.getRGB());
		if (col3 == 255)
			image.setRGB(w, h, Color.white.getRGB());

		int rgb = new Color(image.getRGB(w, h)).getRGB();

		if (rgb <= new Color(200, 200, 200).getRGB())
			image.setRGB(w, h, ReturnRGB(ch));

		return image;
	}

	int ReturnRGB(Color col) { // RGB �� ����
		if (colorOption_Black.isSelected())
			return Color.black.getRGB();
		else
			return col.getRGB();

	}

	static BufferedImage ImageCopy(BufferedImage bi) { // ���� ����
		ColorModel cm = bi.getColorModel();
		boolean alp = cm.isAlphaPremultiplied();
		WritableRaster wr = bi.copyData(null);
		return new BufferedImage(cm, wr, alp, null);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ColorExtraction();
	}

}
