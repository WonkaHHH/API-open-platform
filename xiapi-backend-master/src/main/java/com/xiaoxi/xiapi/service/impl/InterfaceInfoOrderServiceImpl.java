package com.xiaoxi.xiapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxi.common.common.ErrorCode;
import com.xiaoxi.common.exception.BusinessException;
import com.xiaoxi.common.model.entity.InterfaceInfoOrder;
import com.xiaoxi.common.model.entity.InterfaceInfoProduct;
import com.xiaoxi.xiapi.mapper.InterfaceInfoOrderMapper;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderAddRequest;
import com.xiaoxi.xiapi.model.dto.interfaceinfoorder.InterfaceInfoOrderPayRequest;
import com.xiaoxi.xiapi.service.InterfaceInfoOrderService;
import com.xiaoxi.xiapi.service.InterfaceInfoProductService;
import com.xiaoxi.xiapi.service.UserInterfaceInfoService;
import com.xiaoxi.xiapi.utils.SnowFlakeCompone;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


@Service
public class InterfaceInfoOrderServiceImpl extends ServiceImpl<InterfaceInfoOrderMapper, InterfaceInfoOrder>
    implements InterfaceInfoOrderService{

    @Resource
    private SnowFlakeCompone snowFlakeCompone;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private InterfaceInfoProductService interfaceInfoProductService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    // MQ消息过期时间,10分钟
    public static final String MESSAGE_EXPRIE_TIME = String.valueOf(1 * 60 * 1000);

    @Override
    public void validInterfaceInfoOrder(InterfaceInfoOrder interfaceInfoOrder, boolean add) {

        if (interfaceInfoOrder == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = interfaceInfoOrder.getId();
        Long interfaceInfoId = interfaceInfoOrder.getInterfaceInfoId();
        Long userId = interfaceInfoOrder.getUserId();
        Long interfaceInfoProductId = interfaceInfoOrder.getInterfaceInfoProductId();
        Integer price = interfaceInfoOrder.getPrice();

        if (add){
            if(StringUtils.isAnyBlank(id.toString(), interfaceInfoId.toString(), userId.toString(), interfaceInfoProductId.toString(), price.toString())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (id == null || id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id设置不正确");
        }

        if (interfaceInfoId == null || interfaceInfoId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口id设置不正确");
        }

        if (userId == null || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id设置不正确");
        }

        if (interfaceInfoProductId == null || interfaceInfoProductId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口商品id设置不正确");
        }

        if (price == null || price <= 0 || price > 99999){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "价格设置不正确");
        }
    }

    @Override
    @Transactional
    public long addOrder(InterfaceInfoOrderAddRequest interfaceInfoOrderAddRequest, Long userId) {
        synchronized (userId.toString().intern()){
            // 判断是否有库存并先扣减库存
            InterfaceInfoProduct interfaceInfoProduct = interfaceInfoProductService.orderProduct(interfaceInfoOrderAddRequest.getInterfaceInfoId(), interfaceInfoOrderAddRequest.getInterfaceInfoProductId());

            InterfaceInfoOrder interfaceInfoOrder = new InterfaceInfoOrder();
            BeanUtils.copyProperties(interfaceInfoOrderAddRequest, interfaceInfoOrder);
            long id = snowFlakeCompone.getInstance().nextId();
            interfaceInfoOrder.setId(id);
            interfaceInfoOrder.setUserId(userId);
            interfaceInfoOrder.setPrice(interfaceInfoProduct.getPrice());
            // 校验
            validInterfaceInfoOrder(interfaceInfoOrder, true);

            boolean result = save(interfaceInfoOrder);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }

            // 存入MQ, 10分钟未支付,自动取消订单1 status
            MessagePostProcessor message = new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setExpiration(MESSAGE_EXPRIE_TIME);
                    return message;
                }
            };
            // 存入延迟队列,10分钟到期未支付回滚库存
            rabbitTemplate.convertAndSend("delay_exchange", "delay", String.valueOf(id), message);

            return interfaceInfoOrder.getId();
        }
    }

    @Override
    @Transactional
    public Long payOrder(InterfaceInfoOrderPayRequest interfaceInfoOrderPayRequest, Long userId) {

        // 更新订单status
        LambdaUpdateWrapper<InterfaceInfoOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(InterfaceInfoOrder::getId, interfaceInfoOrderPayRequest.getId());
        updateWrapper.eq(InterfaceInfoOrder::getInterfaceInfoId, interfaceInfoOrderPayRequest.getInterfaceInfoId());
        updateWrapper.eq(InterfaceInfoOrder::getInterfaceInfoProductId, interfaceInfoOrderPayRequest.getInterfaceInfoProductId());
        updateWrapper.eq(InterfaceInfoOrder::getUserId, userId);
        updateWrapper.set(InterfaceInfoOrder::getStatus, 2);
        updateWrapper.set(InterfaceInfoOrder::getPayTime, new Date());

        boolean isUpdateSuccess = update(updateWrapper);
        if (!isUpdateSuccess){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "支付失败");
        }

        // 需要增加的调用次数
        Integer specification = interfaceInfoProductService.getById(interfaceInfoOrderPayRequest.getInterfaceInfoProductId()).getSpecification();

        // 增加调用次数
        boolean isAddSuccess = userInterfaceInfoService.addInvokeCount(userId, interfaceInfoOrderPayRequest.getInterfaceInfoId(), specification);
        if (!isAddSuccess){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "增加调用次数失败");
        }
        return interfaceInfoOrderPayRequest.getId();
    }
}




