package study.service;

import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService{

	public boolean isPicture(String filename) {
		String imgeArray[] = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg",
				"jpg", "png", "tif", "tiff", "ico" };

		for (int i = 0; i < imgeArray.length; i++) {
			if (filename.split("\\.")[1].equals(imgeArray[i])) {
				return true;
			}
		}
		return false;
	}

}
