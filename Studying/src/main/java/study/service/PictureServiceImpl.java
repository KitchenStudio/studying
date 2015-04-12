package study.service;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import study.util.FileTypeJudge;

@Service
public class PictureServiceImpl implements PictureService {

	public boolean isPicture(String filename) {
		String imgeArray[] = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg",
				"jpg", "png", "tif", "tiff", "ico" };

		for (int i = 0; i < imgeArray.length; i++) {
			try {
				System.out.println(FileTypeJudge.getType(filename) + "文件类型");
				if (FileTypeJudge.getType(filename) != null) {
					if ((FileTypeJudge.getType(filename).toString())
							.equalsIgnoreCase(imgeArray[i])) {
						return true;
					}
				}else{
					return false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * scale image
	 * 
	 * @param sbi
	 *            image to scale
	 * @param imageType
	 *            type of image
	 * @param dWidth
	 *            width of destination image
	 * @param dHeight
	 *            height of destination image
	 * @param fWidth
	 *            x-factor for transformation / scaling
	 * @param fHeight
	 *            y-factor for transformation / scaling
	 * @return scaled image
	 */
	public BufferedImage scale(BufferedImage sbi, int imageType, int dWidth,
			int dHeight, double fWidth, double fHeight) {
		BufferedImage dbi = null;
		if (sbi != null) {
			dbi = new BufferedImage(dWidth, dHeight, imageType);
			Graphics2D g = dbi.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance(fWidth,
					fHeight);
			g.drawRenderedImage(sbi, at);
		}
		return dbi;
	}

	public boolean isSound(String filename) {
		// TODO Auto-generated method stub
		if (filename.split("\\.")[1].equals("mp4")) {
			return true;
		}
		return false;
	}
}
