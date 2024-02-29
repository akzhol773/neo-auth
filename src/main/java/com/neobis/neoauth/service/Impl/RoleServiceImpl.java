package com.neobis.neoauth.service.Impl;

import com.neobis.neoauth.repository.RoleRepository;
import com.neobis.neoauth.repository.UserRepository;
import com.neobis.neoauth.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
   private final RoleRepository roleRepository;

}
