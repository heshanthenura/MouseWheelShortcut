package com.heshanthenura.mousewheelshortcut;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot {
    public void takeAndSaveScreenshot() {
        try {
            BufferedImage screenshot = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            String picturesFolder;
            String userHome = System.getProperty("user.home");
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) {
                picturesFolder = userHome + File.separator + "Pictures";
            } else if (osName.contains("mac")) {
                picturesFolder = userHome + File.separator + "Pictures";
            } else {
                picturesFolder = userHome + File.separator + "Pictures";
            }
            File picturesDir = new File(picturesFolder);
            if (!picturesDir.exists()) {
                picturesDir.mkdirs();
            }

            // Generate a unique filename using a timestamp
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
            String timestamp = dateFormat.format(new Date());
            String filename = "screenshot_" + timestamp + ".png";

            File outputFile = new File(picturesFolder + File.separator + filename);
            ImageIO.write(screenshot, "png", outputFile);

            System.out.println("Screenshot saved to: " + outputFile.getAbsolutePath());
        } catch (AWTException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
