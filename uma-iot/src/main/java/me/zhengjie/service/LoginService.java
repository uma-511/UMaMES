package me.zhengjie.service;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.uma_mes.domain.FactoryName;
import me.zhengjie.uma_mes.repository.FactoryNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private FactoryNameRepository factoryNameRepository;


    public void login(){

    }

    public void cancel(){

    }

    public String getFactory(Integer id){
        FactoryName name = factoryNameRepository.findById(id).orElseGet(FactoryName::new);
        return name.getName();
    }
}
