package com.space.game.config;

import java.util.List;

/**
 * Interface Builder para construção de configurações de nível
 * Seguindo o padrão Builder com etapas bem definidas
 */
public interface LevelConfigBuilder {
    
    /**
     * Define as configurações básicas do nível
     */
    LevelConfigBuilder setBasicInfo(int levelNumber);
    
    /**
     * Configura os inimigos do nível
     */
    LevelConfigBuilder setEnemyConfiguration(int enemyCount, float enemySpeed);
    
    /**
     * Define os padrões de movimento dos inimigos
     */
    LevelConfigBuilder setMovementPatterns(List<Integer> patterns);
    
    /**
     * Configura os recursos do jogador
     */
    LevelConfigBuilder setPlayerResources(int ammunitions);
    
    /**
     * Define as estatísticas do jogador
     */
    LevelConfigBuilder setPlayerStats(int kills, int streak, int consecutiveKills);
    
    /**
     * Constrói o objeto LevelConfig final
     */
    LevelConfig build();
    
    /**
     * Reseta o builder para permitir reutilização
     */
    void reset();
} 