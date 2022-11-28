package com.moshui.reggie.controller;

import com.moshui.reggie.common.R;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传和下载
 */

@RestController
@RequestMapping("/common")
public class CommonController {
    
    @Value("${reggie.path}")
    public String basePath;
    private String currentDate;
    
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //拼接文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        
        //加上上传文件的日期
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // String format = simpleDateFormat.format(new Date());
        // if (!format.equals(currentDate)){
        //     basePath += format;
        //     currentDate = format;
        // }

        //判断basePath是否存在
        File baseFile = new File(basePath);
        if (!baseFile.exists()){
            baseFile.mkdirs();
        }

        //file被存储在当前系统的临时文件中，要转存
        try {
            //将临时文件转存
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }
    
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //输入流，读取文件内容
            inputStream = new FileInputStream(basePath + name);

            //输出流，输出到浏览器
            outputStream = response.getOutputStream();
            
            response.setContentType("image/jpeg");
            
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
