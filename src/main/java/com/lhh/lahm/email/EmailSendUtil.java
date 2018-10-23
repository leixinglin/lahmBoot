package com.lhh.lahm.email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailSendUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendTextEmail(String form,String to,String title,String content){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(form);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
    }

    public void sendHtmlEmail(String form,String to,String title,String content) throws Exception{
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(form);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content,true);
        javaMailSender.send(mimeMessage);
    }

    public void sendAttachmentEmail(String form,String to,String title,String content,String filePath) throws Exception{
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(form);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content,true);
        FileSystemResource fileSystemResource=new FileSystemResource(new File(filePath));
        mimeMessageHelper.addAttachment(fileSystemResource.getFilename(),fileSystemResource);

        javaMailSender.send(mimeMessage);
    }

    public void sendImgEmail(String form,String to,String title,String content,String filePath,String resId) throws Exception{
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(form);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content,true);
        FileSystemResource fileSystemResource=new FileSystemResource(new File(filePath));
        mimeMessageHelper.addInline(resId,fileSystemResource);

        javaMailSender.send(mimeMessage);
    }

    @Autowired
    private Configuration freeMarkerConfiguration;
    public void sendMouldEmail(String form,String to) throws Exception{
        Template t=freeMarkerConfiguration.getTemplate("mail.ftl");
        Map<String, String> map=new HashMap<String, String>();
        map.put("user", "admin");
        String text= FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true,"utf-8");
        helper.setFrom(form);
        helper.setTo(to);
        helper.setSubject("freeMarker");
        helper.setText(text,true);
        javaMailSender.send(message);
    }

}
