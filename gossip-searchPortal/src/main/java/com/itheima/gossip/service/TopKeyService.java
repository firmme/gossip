package com.itheima.gossip.service;

import java.util.List;
import java.util.Map;

public interface TopKeyService {
    public List<Map<String,Object>> findByTopKey(Integer num);
}
