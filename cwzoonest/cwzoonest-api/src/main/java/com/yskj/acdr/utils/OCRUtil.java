package com.yskj.acdr.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRUtil {

    private static final ITesseract tesseract = new Tesseract();

    static {
        // 如果需要设置 Tesseract 的数据路径（即 tessdata 目录的路径），可以在这里设置
         tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
    }

    /**
     * 从图像文件中提取文本
     * @param imagePath 图像文件的路径
     * @param language  识别语言的代码。默认为英语 ('eng')
     * @return 识别出的文本
     */
    public static String imageToText(String imagePath, String language) {
        File imageFile = new File(imagePath);
        tesseract.setLanguage(language);

        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            System.err.println("Error processing image " + imagePath + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * 设置 Tesseract 的数据路径
     * @param tessdataPath tessdata 目录的路径
     */
    public static void setTessdataPath(String tessdataPath) {
        tesseract.setDatapath(tessdataPath);
    }
}
