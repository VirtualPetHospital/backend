package cn.vph.files.util;

import cn.vph.common.CommonErrorCode;
import cn.vph.common.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-03-31 20:30
 **/
@Component
public class FileUtil {
    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    /**
     * 能够转换avi,mov,mpg
     * @param videoPath
     * @throws IOException
     */
    @Async
    public void convertVideoToMp4(String videoPath) throws IOException {
        String fileSuffix = videoPath.substring(videoPath.lastIndexOf(".") + 1);
        if ("mp4".equals(fileSuffix)) {
            return;
        }
        List<String> command = new ArrayList<String>();
        command.add(ffmpegPath);
        command.add("-i");
        command.add(videoPath);
        command.add(videoPath.substring(0, videoPath.lastIndexOf(".")) + ".mp4");

        InputStream errorStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.start();
            errorStream = process.getErrorStream();
            inputStreamReader = new InputStreamReader(errorStream);
            br = new BufferedReader(inputStreamReader);
            while (br.readLine() != null) {
            }
        } catch (IOException e) {
            throw new CommonException(CommonErrorCode.FILE_CONVERT_ERROR);
        } finally {
            if (br != null) {
                br.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (errorStream != null) {
                errorStream.close();
            }
            //删除原来的文件
            File file = new File(videoPath);
            if (file.exists()) {
                file.delete();
            }
        }

    }

    private static void blockFfmpeg(BufferedReader br) throws IOException {
        String line;
        // 该方法阻塞线程，直至合成成功
        while ((line = br.readLine()) != null) {
            doNothing(line);
        }
    }

    private static void doNothing(String line) {
        System.out.println(line);
    }

    @Async
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
        // 删除原来的文件
        File file = new File(photoPath);
        if (file.exists()) {
            file.delete();
        }
    }

    public String generateFileNameByTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        // 获取当前时间
        Date currentDate = new Date();

        // 使用 SimpleDateFormat 对象格式化日期时间

        return sdf.format(currentDate);
    }
}
