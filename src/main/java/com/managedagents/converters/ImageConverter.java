/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.converters;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author james
 */
public abstract class ImageConverter {

    public static File scaleImage(UploadedFile uploadedFile, int maxWidth) throws IOException {
        File scaledFile;
        scaledFile = File.createTempFile("image_", ".tmp");
        BufferedImage bufferedImage = ImageIO.read(uploadedFile.getInputstream());
        int width = maxWidth;
        int calcHeight = width * bufferedImage.getHeight() / bufferedImage.getWidth();
        BufferedImage scaledBI = new BufferedImage(width, calcHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = scaledBI.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(bufferedImage, 0, 0, width, calcHeight, null);
        g.dispose();
        ImageIO.write(scaledBI, "jpeg", scaledFile);
        return scaledFile;
    }
}
