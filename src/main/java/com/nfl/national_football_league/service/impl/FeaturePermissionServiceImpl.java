package com.nfl.national_football_league.service.impl;

import com.nfl.national_football_league.service.FeaturePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("FeaturePermissionService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeaturePermissionServiceImpl implements FeaturePermissionService {
    public static FeaturePermissionObject getFeaturePermissionObject(MainFeature mainFeature, SubFeature subFeature, Permission permission) {
        return FeaturePermissionObject.builder()
                .featurePermissionMap(new FeaturePermissionMap(mainFeature, subFeature, permission))
                .code(subFeature.name() + "_" + permission.name())
                .build();
    }

}
