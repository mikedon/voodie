package com.voodie.mapping;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.List;

/**
 * Voodie
 * User: MikeD
 */
@ApplicationScoped
public class DozerProducer {

    private static List<String> mappingFiles = Lists.newArrayList("dozer-mapping.xml");
    private Mapper dozerMapper;

    // ---------------------------------

    @Produces
    public Mapper getMapper(){
        if(dozerMapper == null){
            dozerMapper = new DozerBeanMapper();
            ((DozerBeanMapper)dozerMapper).setMappingFiles(mappingFiles);
        }
        return dozerMapper;
    }
}
