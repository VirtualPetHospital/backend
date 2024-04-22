package cn.vph.files.common;

/**
 * @program: vph-backend
 * @description:
 * @author: astarforbae
 * @create: 2024-04-03 15:38
 **/
//@Component
//public class CustomMultipartResolver extends CommonsMultipartResolver {
//
//
//    @Override
//    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
//        String encoding = determineEncoding(request);
//        FileUpload fileUpload = prepareFileUpload(encoding);
//        // fileUpload.setFileSizeMax(1024 * 1024 * 500);// 单个文件最大500M
//        // fileUpload.setSizeMax(1024 * 1024 * 500);// 一次提交总文件最大500M
//        try {
//            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
//            return parseFileItems(fileItems, encoding);
//        } catch (FileUploadBase.SizeLimitExceededException ex) {
//            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
//        } catch (FileUploadException ex) {
//            throw new MultipartException("Could not parse multipart servlet request", ex);
//        }
//    }
//}
