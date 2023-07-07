package com.ruoyi.auth.service;


import com.ruoyi.auth.form.EmailBody;
import edu.neu.base.constant.cc.MQTopic;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RocketMQMessageListener(topic = MQTopic.EMAIL_TOPIC, consumerGroup = "user-service-producer-group")
public class MessageExtConsumer implements RocketMQListener<EmailBody> {

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void onMessage(EmailBody emailBody) {
        System.out.println("接收到消息：" + emailBody);
        sendMail(emailBody.getEmail(), emailBody.getCode());
    }


    private void sendMail(String email, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Xanadu登录验证码");
            helper.setFrom("1023118868@qq.com");
            helper.setText(code);
            helper.setTo(email);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
