package com.company.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
@Component
public class MapperUtils {
    private final ModelMapper modelMapper;

    public MapperUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
//    public <T> T convert (Object objectToBeConverted, T convertedObject){
//        return modelMapper.map(objectToBeConverted,(Type)convertedObject.getClass());
//    }
    public <T> T convert (Object objectToBeConverted, Class<T> convertedObject){
        return modelMapper.map(objectToBeConverted,convertedObject);
    }
}
