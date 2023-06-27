package com.example.springsecurityjwt.system.service;

import com.example.springsecurityjwt.system.model.SysModel;

/**
 * @author dake malone
 * @date 2023年05月18日 下午 1:59
 */
public interface SysModelService {
    /**
     * get one by id
     * @author dake malone
     * @date 18/5/2023 下午 2:06
     * @param
     * @param id
     *
     * @return com.example.springsecurityjwt.system.model.SysModel
     */
    SysModel getById(Long id);
}
