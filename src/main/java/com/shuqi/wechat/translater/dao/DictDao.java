package com.shuqi.wechat.translater.dao;

import com.shuqi.wechat.translater.bean.Dict;
import com.shuqi.wechat.translater.bean.Word;

public interface DictDao {
    public void add2Dict(String userId, Word word);

    public void deleteFormDict(String userId, Word word);

    public Dict getDict(String userId);

    public void createDict(String userId);

}
