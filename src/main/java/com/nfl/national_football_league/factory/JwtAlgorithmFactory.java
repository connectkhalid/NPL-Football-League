package com.nfl.national_football_league.factory;

import com.auth0.jwt.algorithms.Algorithm;

import java.util.Optional;

public abstract class JwtAlgorithmFactory {
    public abstract Optional<Algorithm> create();
}
