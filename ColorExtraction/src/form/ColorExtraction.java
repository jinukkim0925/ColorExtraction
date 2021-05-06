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
		setTitle("색깔 추출기");
		setDefaultCloseOperation(2);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0); // 오류시 강제 종료
			}
		});
		add(np = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		add(cp = new JPanel(new FlowLayout(FlowLayout.RIGHT)), BorderLayout.CENTER);
		add(sp = new JPanel(), BorderLayout.SOUTH);

		np.add(np_sel = new JPanel(new FlowLayout(FlowLayout.LEFT)), BorderLayout.NORTH);
		np.add(np_op = new JPanel(new FlowLayout(FlowLayout.LEFT)), BorderLayout.CENTER);

		np_sel.add(jl = new JLabel("추출할 색"));
		np_sel.add(redSelect = new JRadioButton("빨강"));
		np_sel.add(greenSelect = new JRadioButton("초록"));
		np_sel.add(blueSelect = new JRadioButton("파랑"));

		select.add(redSelect);
		select.add(greenSelect);
		select.add(blueSelect);

		np_op.add(jl = new JLabel("추출 옵션"));
		np_op.add(colorOption_Original = new JRadioButton("원색"));
		np_op.add(colorOption_Black = new JRadioButton("검정색"));

		option.add(colorOption_Original);
		option.add(colorOption_Black);

		cp.add(loadPath = new JButton("이미지 불러오기"));
		cp.add(outImage = new JButton("이미지 추출하기"));
		cp.add(saveImage = new JButton("이미지 저장하기"));
		cp.add(resetImage = new JButton("원래 이미지로"));

		redSelect.setSelected(true);
		colorOption_Original.setSelected(true);

		sp.add(im = new JLabel());

		loadPath.addActionListener(e -> {
			JFrame jf = new JFrame();

			FileDialog f = new FileDialog(jf, "불러오기", FileDialog.LOAD);
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
					JOptionPane.showMessageDialog(null, "이미지 형식이 아닙니다.", "오류", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		outImage.addActionListener(e -> {
			if (m_image == null) {
				JOptionPane.showMessageDialog(null, "이미지가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "이미지가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JFrame jf = new JFrame();
			FileDialog f = new FileDialog(jf, "저장하기", FileDialog.SAVE);
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

				maxrgb = MaxRGB(r, g, b);// 가장 큰 RGB

				image = ColorEx(image, maxrgb, w, h, r, g, b, Color.red); // col1 = 추출 색
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

				image = ColorEx(image, maxrgb, w, h, g, r, b, Color.green); // col1 = 추출 색
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

	int MaxRGB(int r, int g, int b) { // 가장 큰 R G B
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
		// 색깔 추출
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

	int ReturnRGB(Color col) { // RGB 값 리턴
		if (colorOption_Black.isSelected())
			return Color.black.getRGB();
		else
			return col.getRGB();

	}

	static BufferedImage ImageCopy(BufferedImage bi) { // 얕은 복사
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
