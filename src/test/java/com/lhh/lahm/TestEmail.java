package com.lhh.lahm;

import com.lhh.lahm.email.EmailSendUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEmail {

    @Value("${spring.mail.username}")
    private String form;

    private String to="2594811780@qq.com";




    @Resource
    private EmailSendUtil emailSendUtil;

    @Test
    public void test(){
        emailSendUtil.sendTextEmail(form,to,"spring boot邮箱","这是一个文本邮箱");
        System.out.println("发送成功！");
    }

    @Test
    public void testHtmlEmail() throws Exception{
        StringBuffer sb=new StringBuffer();
        sb.append("<h1>");
        sb.append("HTML格式的邮箱");
        sb.append("</h1>");
        sb.append("<hr/>");
        emailSendUtil.sendHtmlEmail(form,to,"spring boot-HTMl邮箱",sb.toString());
        System.out.println("发送成功！");
    }

    @Test
    public void testAttachmentEmail() throws Exception{
       String context="附加文件！";
        String fileName="D:\\MyDownloads\\IntelliJIDEA\\IdeaWork\\lahm\\src\\test\\test.doc";
        emailSendUtil.sendAttachmentEmail(form,to,"spring boot-附件邮箱",context,fileName);
        System.out.println("发送成功！");
    }

    @Test
    public void sendImgEmail() throws Exception{
        String resid="img1";
        String context="<html><body><img src=\'cid:"+resid+"\'></img><body></html>";
        String fileName="D:\\MyDownloads\\IntelliJIDEA\\IdeaWork\\lahm\\src\\test\\9741.jpg";
        emailSendUtil.sendImgEmail(form,to,"spring boot-图片邮箱",context,fileName,resid);
        System.out.println("发送成功！");
    }

    @Test
    public void sendMouldEmail() throws Exception{
       emailSendUtil.sendMouldEmail(form,to);
        System.out.println("成功！");
    }

}
