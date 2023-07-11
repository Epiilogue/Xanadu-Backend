package edu.neu.cc.listener;

import edu.neu.base.constant.cc.MQTopic;
import edu.neu.cc.entity.Operation;
import edu.neu.cc.service.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RocketMQMessageListener(topic = MQTopic.OPERATION_TOPIC, consumerGroup = "operation-service-xgs-group",messageModel= MessageModel.BROADCASTING)
public class OperationListener implements RocketMQListener<Operation> {
    @Autowired
    OperationService operationService;

    @Override
    public void onMessage(Operation operation) {
        operationService.save(operation);
    }
}
