package cn.vph.files.util;

import org.springframework.stereotype.Component;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-31 20:30
 **/
@Component
public class FileUtil {
    public void convertVideoToMp4(String videoPath) throws IOException {
        String fileSuffix = videoPath.substring(videoPath.lastIndexOf(".") + 1);
        if ("mp4".equals(fileSuffix)) {
            return;
        }
        String dest = videoPath.substring(0, videoPath.lastIndexOf(".")) + ".mp4";
        ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
        ffmpeg.addArgument("-i");
        ffmpeg.addArgument(videoPath);
        ffmpeg.addArgument("-c:v");
        ffmpeg.addArgument("libx264");
        ffmpeg.addArgument("-crf");
        ffmpeg.addArgument("19");
        ffmpeg.addArgument("-strict");
        ffmpeg.addArgument("experimental");
        ffmpeg.addArgument(dest);
        ffmpeg.execute();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ffmpeg.getErrorStream()))) {
            blockFfmpeg(br);
        }
//        File file = new File();
//        file.createNewFile();
    }

    private static void blockFfmpeg (BufferedReader br) throws IOException {
        String line;
        // 该方法阻塞线程，直至合成成功
        while ((line = br.readLine()) != null) {
            doNothing(line);
        }
    }

    private static void doNothing (String line){
        System.out.println(line);
    }

    public void convertPhotoToJpeg(String photoPath) throws IOException {
        String fileSuffix = photoPath.substring(photoPath.lastIndexOf(".") + 1);
        if ("jpeg".equals(fileSuffix)) {
            return;
        }
        // 读取文件
        String dest = photoPath.substring(0, photoPath.lastIndexOf(".")) + ".jpeg";
        BufferedImage image = ImageIO.read(new File(photoPath));
        BufferedImage newBufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        ImageIO.write(newBufferedImage, "jpeg", new File(dest));
    }

    public String generateFileNameByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        // 获取当前时间
        Date currentDate = new Date();

        // 使用 SimpleDateFormat 对象格式化日期时间

        return sdf.format(currentDate);
    }
}
