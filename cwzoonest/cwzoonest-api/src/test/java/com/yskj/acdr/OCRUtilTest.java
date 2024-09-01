package com.yskj.acdr;

import com.yskj.acdr.utils.OCRUtil;

public class OCRUtilTest {
    public static void main(String[] args) {
        // 设置 Tesseract 的数据路径，如果 Tesseract 没有添加到系统路径中，需要指定 tessdata 的路径。
        OCRUtil.setTessdataPath("C:/Program Files/Tesseract-OCR/tessdata");

        // 从图像中提取文本
        String extractedText = OCRUtil.imageToText("D:\\App\\Work\\addr\\acdr\\src\\test\\resources\\images\\2.jpg", "eng");
        String extractedText2 = OCRUtil.imageToText("D:\\App\\Work\\addr\\acdr\\src\\test\\resources\\images\\2.jpg", "chi_sim");

        if (extractedText != null) {
            System.out.println("Extracted Text:");
            System.out.println(extractedText);
            System.out.println(extractedText2);
        } else {
            System.out.println("Failed to extract text.");
        }
    }
}
