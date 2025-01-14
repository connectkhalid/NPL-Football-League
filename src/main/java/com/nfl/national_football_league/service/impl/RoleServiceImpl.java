package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.domain.RoleInfo;
import com.nfl.national_football_league.exception.RoleServiceException;
import com.nfl.national_football_league.repository.RoleInfoRepository;
import com.nfl.national_football_league.service.RoleService;
import com.nfl.national_football_league.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("RoleService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {

    private final RoleInfoRepository roleInfoRepository;
    @Override
    public RoleRegisterResponse registerRole(RoleRegisterInputParameter parameter) throws RoleServiceException {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRoleCode(parameter.getRoleCode());
        roleInfo.setRoleName(parameter.getRoleName());
        roleInfo.setDeleteFlag(false);
        roleInfo.setCreatedDt(DateUtil.currentTime());
        roleInfo.setUpdatedDt(DateUtil.currentTime());
        roleInfoRepository.save(roleInfo);

        return RoleRegisterResponse.builder()
                .roleCode(parameter.getRoleCode())
                .roleName(parameter.getRoleName())
                .createdDt(roleInfo.getCreatedDt())
                .updatedDt(roleInfo.getUpdatedDt())
                .build();
    }
}
