package com.rdq.ruiji.controller;

import com.rdq.ruiji.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${ruiji.path}")
    private String basePath;
    //文件上传功能实现
    @PostMapping("/upload")
    public R<String> upload(MultipartFile  file) {
        log.info("上传的文件是{}",file);

        //获取原始文件名
        final String originalFilename = file.getOriginalFilename();
        String  suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid生产文件名防止文件覆盖
        final String fileName = UUID.randomUUID().toString() +suffix;

        //创建一个目录对象，
        File dir = new File(basePath);
        //判断目录是否存在
        if (!dir.exists()) {
            //如果目录不存在需要创建
            dir.mkdirs();
        }

        //转存到那个位置、
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void  download(String name, HttpServletResponse response) {
        //输入流通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //通过输出流写回文件浏览器
            ServletOutputStream out = response.getOutputStream();
            //设置响应回去的文件类型
            response.setContentType("image/jpeg");


            byte[] bytes = new byte[1024];
            int len = 0;
            //读流放到byte数组里去
            while ((len = fileInputStream.read(bytes)) != -1) {
                //把byte数组里的东西输入到浏览器，从0开始输出，到len个结束
                out.write(bytes, 0, len);
                out.flush();
            }
            out.close();
            fileInputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
