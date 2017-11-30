package com.ashwinchat.stockapp.model.dao.impl;

import com.ashwinchat.stockapp.model.dao.IDao;
import com.ashwinchat.stockapp.model.view.SystemConfigView;

public interface ISystemConfigDao extends IDao<SystemConfigView> {
    String findValue(String sysCd, String key);
}
