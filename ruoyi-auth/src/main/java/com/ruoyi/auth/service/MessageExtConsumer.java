package com.ruoyi.auth.service;


import com.ruoyi.auth.form.EmailBody;
import com.ruoyi.common.core.constant.CacheConstants;
import com.ruoyi.common.redis.service.RedisService;
import edu.neu.base.constant.cc.MQTopic;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

@Service
@RocketMQMessageListener(topic = MQTopic.EMAIL_TOPIC, consumerGroup = "user-service-producer-group",messageModel= MessageModel.BROADCASTING)
public class MessageExtConsumer implements RocketMQListener<EmailBody> {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    RedisService redisService;

    @Override
    public void onMessage(EmailBody emailBody) {
        sendMail(emailBody.getEmail(), emailBody.getCode(),emailBody.getUserName());
    }

    private void sendMail(String email, String code,String userName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Xanadu登录验证码");
            helper.setFrom("1023118868@qq.com");
            helper.setText(code);
            helper.setTo(email);
            javaMailSender.send(message);
            redisService.setCacheObject(CacheConstants.EMAIL_CODE_KEY + email, code + ":" + userName, CacheConstants.EMAIL, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
