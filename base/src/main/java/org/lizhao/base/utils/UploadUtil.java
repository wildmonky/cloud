//package org.lizhao.base.utils;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.RandomUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.lizhao.base.configurer.mybatis.DbTransactionManager;
//import org.lizhao.base.entity.UploadFile;
//import org.lizhao.base.mapper.UploadFileMapper;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Objects;
//
//@Slf4j
//@Component
//public class UploadUtil {
//
//    @Resource
//    private UploadFileMapper uploadFileMapper;
//
//    @Resource
//    private DbTransactionManager dbTransactionManager;
//
//    // 所有上传文件的根目录
//    private String root;
//    // 默认上传文件目录，在 root 根目录下
//    private String dir;
//
//    @PostConstruct
//    public void init() {
//        this.root = "";
//        this.dir = this.root + File.separator + "upload";
//    }
//
//    /**
//     * 保存上传文件
//     *
//     * @param file 文件
//     * @param targetDir 文件目标目录
//     * @param operationWhenExistedEnum 文件已存在时，逻辑
//     * @return 文件按
//     */
//    public UploadFile uploadFile(MultipartFile file, String targetDir, OperationWhenExistedEnum operationWhenExistedEnum) throws IOException {
//        return uploadFile(file.getInputStream(), Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().lastIndexOf(".")), FileTypeEnum.getBySuffix(Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1)).getSuffix(), targetDir, operationWhenExistedEnum);
//    }
//
//    /**
//     * 保存上传文件
//     *
//     * @param file 文件
//     * @param fileTypeEnum 文件类型
//     * @param targetDir 文件目标目录
//     * @param operationWhenExistedEnum 文件已存在时，逻辑
//     * @return 文件按
//     * @throws IOException
//     */
//    public UploadFile uploadFile(MultipartFile file, FileTypeEnum fileTypeEnum, String targetDir, OperationWhenExistedEnum operationWhenExistedEnum) throws IOException {
//        return uploadFile(file.getInputStream(), file.getOriginalFilename(), fileTypeEnum.getSuffix(), targetDir, operationWhenExistedEnum);
//    }
//
//    /**
//     * 保存文件
//     *
//     * @param inputStream 文件输入流
//     * @param fileName 文件名
//     * @param fileSuffix 文件后缀
//     * @param operationWhenExistedEnum 文件已存在时，逻辑
//     * @return 文件保存信息
//     */
//    public UploadFile uploadFile(InputStream inputStream, String fileName, String fileSuffix, String targetDir, OperationWhenExistedEnum operationWhenExistedEnum) {
//        return dbTransactionManager.transaction(() -> {
//            UploadFile uploadFile = new UploadFile();
//            uploadFile.setId(new JdbcDao().getSeqNextVal());
//            uploadFile.setName(fileName);
//            uploadFile.setUploadTime(LocalDateTime.now());
//            uploadFile.setSuffix(fileSuffix);
//            Path path = calcPath(fileName, fileSuffix, targetDir);
//            uploadFile.setDir(path.getParent().toString());
//            uploadFile.setUri(path.toString());
//
//            // 查看文件是否已存在，文件名、后缀、保存地址
//            List<UploadFile> existedUploadFiles = this.uploadFileMapper.selectList(Wrappers.<UploadFile>lambdaQuery()
//                    .eq(UploadFile::getName, uploadFile.getName())
//                    .eq(UploadFile::getSuffix, uploadFile.getSuffix())
//                    .eq(UploadFile::getDir, uploadFile.getDir())
//            );
//
//            boolean save = true;
//            if (!existedUploadFiles.isEmpty()) {
//                log.warn("上传文件，存在同名且保存文件夹相同的文件数量：{}", existedUploadFiles.size());
//                switch (operationWhenExistedEnum) {
//                    case NEW:
//                        this.uploadFileMapper.insert(uploadFile);
//                        log.warn("上传文件，新增记录：{}", uploadFile);
//                        break;
//                    case COVERAGE:
//                        UploadFile latest = existedUploadFiles.get(existedUploadFiles.size() - 1);
//                        uploadFile.setId(latest.getId());
//                        this.uploadFileMapper.updateById(uploadFile);
//                        log.warn("上传文件覆盖记录，原记录：{}，修改后记录：{}", latest, uploadFile);
//                        break;
//                    case DO_NOTHING:;
//                    default:
//                        save = false;
//                        log.warn("上传文件，无操作");
//                        return null;
//                }
//            }
//
//            if (save) {
//                try {
//                    // 文件保存到本地
//                    saveFile(inputStream, Paths.get(uploadFile.getUri()));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            return uploadFile;
//        });
//    }
//
//    /**
//     * 计算文件保存路径
//     *
//     * @param fileName 文件名（不包含后缀）
//     * @param fileSuffix 文件后缀
//     * @param targetDir 目标文件夹
//     * @return 文件保存在服务器上的路径
//     */
//    public Path calcPath(String fileName, String fileSuffix, String targetDir) {
//        // 保存文件所在文件夹路径
//        String targetDirPath = StringUtils.isBlank(targetDir) ? this.dir : ( targetDir.contains(":") ? targetDir : this.root + File.separator + targetDir);
//
//        // 保存文件路径
//        Path targetFilePath = Paths.get(targetDirPath, fileName + "." + fileSuffix);
//
//        // 最多尝试3次，找到可用的文件名，即可新建（当前不存在）的文件名
//        byte retry = 3;
//        while (Files.exists(targetFilePath) && retry > 0) {
//            targetFilePath = Paths.get(targetDirPath, String.valueOf(RandomUtils.nextInt()));
//            retry--;
//        }
//
//        return targetFilePath;
//    }
//
//    /**
//     * 保存文件到服务器
//     *
//     * @param inputStream 文件输入流
//     * @param fileName 文件名
//     * @param fileSuffix 文件后缀
//     * @param targetDir 目标文件夹
//     * @return 文件保存在服务器上的路径
//     */
//    public String saveFile(InputStream inputStream, String fileName, String fileSuffix, String targetDir) throws IOException {
//
//        // 保存文件路径
//        Path targetFilePath = calcPath(fileName, fileSuffix, targetDir);
//
////        // 最多尝试3次创建文件
////        File file = targetFilePath.toFile();
////        while (!file.createNewFile() && retry > 0) {
////            retry--;
////        }
////
////        // 文件创建失败
////        if (!file.exists()) {
////            throw new FileNotFoundException(targetFilePath.toString());
////        }
//
//        Files.copy(inputStream, targetFilePath);
//
//        return targetFilePath.toString();
//    }
//
//    public String saveFile(InputStream inputStream, Path targetPath) throws IOException {
//        Files.copy(inputStream, targetPath);
//        return targetPath.toString();
//    }
//
//    public void saveFileRecoveryWhenExisted(InputStream inputStream, Path targetPath) throws IOException {
//        File file = targetPath.toFile();
//        if (!file.exists() && !file.getParentFile().mkdirs() && !file.createNewFile()) {
//            throw new RuntimeException("目标路径不存在且创建失败");
//        }
//        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
//    }
//
//    public void clearDir(Path dir) {
//        if (dir == null) {
//            return;
//        }
//
//        File directory = dir.toFile();
//        if (!directory.exists()) {
//            return;
//        }
//
//        if (!directory.isDirectory()) {
//            throw new RuntimeException("不是文件夹，无法清空");
//        }
//
//        File[] files = directory.listFiles();
//
//        if (files == null) {
//            return;
//        }
//
//        for (File file : files) {
//            file.delete();
//        }
//    }
//
//
//    /**
//     * 根据文件名获取服务器上的文件
//     *
//     * @param fileName 文件名
//     * @return 文件名匹配的文件列表
//     */
//    public List<UploadFile> getUploadFile(String fileName) {
//        return this.uploadFileMapper.selectList(Wrappers.<UploadFile>lambdaQuery().eq(UploadFile::getName, fileName));
//    }
//
//    public File getFile(String path) {
//        return Paths.get(path).toFile();
//    }
//
//    public enum OperationWhenExistedEnum {
//        NEW, // 尝试新建
//        COVERAGE, // 覆盖最新的一条
//        COVERAGE_ALL, // 覆盖所有
//        DO_NOTHING; // 什么都不做
//    }
//
//
//}
