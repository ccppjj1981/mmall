package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

public interface IOrderService {
    ServerResponse aliCallback(Map<String, String> params);
    ServerResponse pay(Long orderNo,Integer userId,String path);
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
