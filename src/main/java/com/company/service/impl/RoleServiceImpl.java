package com.company.service.impl;

import com.company.dto.RoleDTO;
import com.company.mapper.MapperUtils;
import com.company.mapper.RoleMapper;
import com.company.repository.RoleRepository;
import com.company.service.RoleService;
import com.company.entity.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MapperUtils mapperUtils;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, MapperUtils mapperUtils) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.mapperUtils = mapperUtils;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //controller --> request all RoleDTO to see in ui
        // go to DB and get all roles from the table
        //use repository layer
        /*
        List<RoleDTO> roleDtoList  = new ArrayList<>();
        List<Role> roleList = roleRepository.findAll();// we need to convert Role to the RoleDTO
        for (Role role : roleList) {
          roleDtoList.add(roleMapper.convertToDTO(role));
       }
        roleDtoList= roleList.stream().map(roleMapper::convertToDTO).toList();
        return roleDtoList;
        or
        List<Role> roleList = roleRepository.findAll();
        return  roleList.stream().map(roleMapper::convertToDTO).collect(Collectors.toUnmodifiableList());
         */
      // return roleRepository.findAll().stream().map(roleMapper::convertToDTO).toList();
       //  return roleRepository.findAll().stream().map(role->mapperUtils.convert(role,new RoleDTO())).toList();// use with (Type)
         return roleRepository.findAll().stream().map(role->mapperUtils.convert(role,RoleDTO.class)).toList();// use with Class<T>

    }

    @Override
    public RoleDTO findById(Long id) {
//       Role role = roleRepository.findById(id).get();
//        return roleMapper.convertToDTO(role);
      return roleMapper.convertToDTO(roleRepository.findById(id).get());
    }
}
