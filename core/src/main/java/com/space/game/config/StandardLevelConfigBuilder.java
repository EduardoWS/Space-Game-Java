package com.space.game.config;

import java.util.List;

/**
 * Builder concreto para construção de configurações padrão de níveis
 * Implementa a interface LevelConfigBuilder
 */
public class StandardLevelConfigBuilder implements LevelConfigBuilder {
    
    private LevelConfig.Builder configBuilder;
    
    public StandardLevelConfigBuilder() {
        reset();
    }
    
    @Override
    public LevelConfigBuilder setBasicInfo(int levelNumber) {
        configBuilder.levelNumber(levelNumber);
        return this;
    }
    
    @Override
    public LevelConfigBuilder setEnemyConfiguration(int enemyCount, float enemySpeed) {
        configBuilder.enemyCount(enemyCount);
        configBuilder.enemySpeed(enemySpeed);
        return this;
    }
    
    @Override
    public LevelConfigBuilder setMovementPatterns(List<Integer> patterns) {
        configBuilder.enemyMovementPatterns(patterns);
        return this;
    }
    
    @Override
    public LevelConfigBuilder setPlayerResources(int ammunitions) {
        configBuilder.ammunitions(ammunitions);
        return this;
    }
    
    @Override
    public LevelConfigBuilder setPlayerStats(int kills, int streak, int consecutiveKills) {
        configBuilder.kills(kills);
        configBuilder.streak(streak);
        configBuilder.consecutiveKills(consecutiveKills);
        return this;
    }
    
    @Override
    public LevelConfig build() {
        return configBuilder.build();
    }
    
    @Override
    public void reset() {
        configBuilder = LevelConfig.builder();
    }
} 