package com.example.springsecurityjwt.system.service.impl;

import com.example.springsecurityjwt.system.mapper.SysModelMapper;
import com.example.springsecurityjwt.system.model.SysModel;
import com.example.springsecurityjwt.system.service.SysModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dake malone
 * @date 2023年05月18日 下午 2:01
 */
@Service
public class SysModelServiceImpl implements SysModelService {
    @Autowired
    private SysModelMapper sysModelMapper;

    /**
     * get one by id
     *
     * @return com.example.springsecurityjwt.system.model.SysModel
     * @author dake malone
     * @date 18/5/2023 下午 2:00
     */
    @Override
    public SysModel getById(Long id) {
        return sysModelMapper.getOne(id);
    }
}
