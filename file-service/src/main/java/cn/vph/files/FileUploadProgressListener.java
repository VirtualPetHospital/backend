package cn.vph.files;

import cn.vph.files.pojo.Progress;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-03 14:46
 **/
@Component
public class FileUploadProgressListener implements ProgressListener {

  private HttpSession session;
  public void setSession(HttpSession session){
    this.session=session;
    Progress status = new Progress();//保存上传状态
    session.setAttribute("status", status);
  }
  @Override
  public void update(long bytesRead, long contentLength, int items) {
    Progress status = (Progress) session.getAttribute("status");
    status.setBytesRead(bytesRead);
    status.setContentLength(contentLength);
    status.setItems(items);
  }
}
