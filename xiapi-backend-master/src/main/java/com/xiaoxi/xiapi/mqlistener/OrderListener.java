package com.xiaoxi.xiapi.mqlistener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rabbitmq.client.Channel;
import com.xiaoxi.common.model.entity.InterfaceInfoOrder;
import com.xiaoxi.common.model.entity.InterfaceInfoProduct;
import com.xiaoxi.xiapi.service.InterfaceInfoOrderService;
import com.xiaoxi.xiapi.service.InterfaceInfoProductService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class OrderListener implements ChannelAwareMessageListener {

    @Resource
    private InterfaceInfoProductService interfaceInfoProductService;

    @Resource
    private InterfaceInfoOrderService interfaceInfoOrderService;

    @Override
    @Transactional
    @RabbitListener(queues = "dlx_delay_queue")
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            String orderId = new String(message.getBody());
            // 查询订单是否支付
            InterfaceInfoOrder order = interfaceInfoOrderService.getById(Long.valueOf(orderId));
            // 订单已支付
            if (order.getStatus() == 2){
                channel.basicAck(deliveryTag, true);
                return;
            }
            // 未支付
            // 回滚库存
            LambdaUpdateWrapper<InterfaceInfoProduct> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(InterfaceInfoProduct::getId, order.getInterfaceInfoProductId());
            wrapper.setSql("stock = stock + 1");
            boolean isUpdateStockSuccess = interfaceInfoProductService.update(wrapper);
            if (!isUpdateStockSuccess){
                channel.basicNack(deliveryTag, true, true);
            }
            // 修改订单状态为取消
            LambdaUpdateWrapper<InterfaceInfoOrder> orderWrapper = new LambdaUpdateWrapper<>();
            orderWrapper.eq(InterfaceInfoOrder::getId, order.getId());
            orderWrapper.set(InterfaceInfoOrder::getStatus, 3);//这里代表支付出现故障
            boolean isUpdateStatusSuccess = interfaceInfoOrderService.update(orderWrapper);
            if (!isUpdateStatusSuccess){
                channel.basicNack(deliveryTag, true, true);
            }
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            System.out.println("错误"+e);
            channel.basicNack(deliveryTag, true, true);
        }
    }
}
