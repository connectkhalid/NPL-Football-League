package com.nfl.national_football_league.factory;

import com.nfl.national_football_league.provider.JwtWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtWrapperFactory {
    public JwtWrapper create(JwtAlgorithmFactory jwtAlgorithmFactory) {
        return new JwtWrapper(jwtAlgorithmFactory);
    }
}
