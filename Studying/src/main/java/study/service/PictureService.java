package study.service;

import java.awt.image.BufferedImage;

public interface PictureService {
	public boolean isPicture(String filename);

	public BufferedImage scale(BufferedImage sbi, int imageType, int dWidth,
			int dHeight, double fWidth, double fHeight);
}
