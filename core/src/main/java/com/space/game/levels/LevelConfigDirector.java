package com.space.game.levels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.space.game.SpaceGame;
import com.space.game.config.LevelConfig;
import com.space.game.config.LevelConfigBuilder;

/**
 * Director que conhece as receitas para construir diferentes tipos de níveis
 * Organiza a construção step-by-step seguindo o padrão Builder + Director
 */
public class LevelConfigDirector {
    
    private final Random random;
    private float factorSpeedInitial;
    private float speed;
    private boolean initialized = false;
    
    public LevelConfigDirector() {
        this.random = new Random();
        // Removida a inicialização aqui para evitar NullPointerException
        // A inicialização será feita lazy quando necessário
    }
    
    /**
     * Inicializa os valores que dependem do SpaceGame estar pronto
     */
    private void ensureInitialized() {
        if (!initialized) {
            this.speed = SpaceGame.getGame().getWorldWidth();
            this.factorSpeedInitial = 70f;
            this.initialized = true;
        }
    }
    
    /**
     * Constrói a configuração do primeiro nível
     */
    public LevelConfig buildFirstLevel(LevelConfigBuilder builder) {
        ensureInitialized(); // Garante que está inicializado
        builder.reset();
        
        int enemyCount = 7;
        List<Integer> movementPatterns = generateMovementPatterns(enemyCount);
        
        return builder
            .setBasicInfo(1)
            .setEnemyConfiguration(enemyCount, speed / factorSpeedInitial)
            .setMovementPatterns(movementPatterns)
            .setPlayerResources(49)
            .setPlayerStats(0, 1, 0)
            .build();
    }
    
    /**
     * Constrói a configuração para níveis progressivos baseado no nível anterior
     */
    public LevelConfig buildProgressiveLevel(LevelConfigBuilder builder, int levelNumber, LevelConfig previousConfig) {
        ensureInitialized(); // Garante que está inicializado
        builder.reset();
        
        // Calcula novos valores baseados no nível anterior
        int newEnemyCount = calculateEnemyCount(previousConfig);
        float newEnemySpeed = calculateEnemySpeed(previousConfig);
        List<Integer> movementPatterns = generateMovementPatterns(newEnemyCount);
        
        // Obtém estatísticas atuais do jogador
        PlayerStats stats = getCurrentPlayerStats();
        
        return builder
            .setBasicInfo(levelNumber)
            .setEnemyConfiguration(newEnemyCount, newEnemySpeed)
            .setMovementPatterns(movementPatterns)
            .setPlayerResources(stats.ammunitions + 7)
            .setPlayerStats(stats.kills, stats.streak, stats.consecutiveKills)
            .build();
    }
    
    /**
     * Constrói um nível de desafio especial (mais difícil)
     */
    public LevelConfig buildChallengeLevel(LevelConfigBuilder builder, int levelNumber, LevelConfig baseConfig) {
        ensureInitialized(); // Garante que está inicializado
        builder.reset();
        
        int challengeEnemyCount = baseConfig.getEnemyCount() + random.nextInt(5) + 5; // Mais inimigos
        float challengeSpeed = baseConfig.getEnemySpeed() * 1.3f; // Mais rápido
        List<Integer> challengePatterns = generateChallengeMovementPatterns(challengeEnemyCount);
        
        PlayerStats stats = getCurrentPlayerStats();
        
        return builder
            .setBasicInfo(levelNumber)
            .setEnemyConfiguration(challengeEnemyCount, challengeSpeed)
            .setMovementPatterns(challengePatterns)
            .setPlayerResources(stats.ammunitions + 10) // Mais munição para o desafio
            .setPlayerStats(stats.kills, stats.streak, stats.consecutiveKills)
            .build();
    }
    
    /**
     * Constrói um nível boss (configuração especial)
     */
    public LevelConfig buildBossLevel(LevelConfigBuilder builder, int levelNumber) {
        ensureInitialized(); // Garante que está inicializado
        builder.reset();
        
        int bossEnemyCount = 15 + random.nextInt(5); // Muitos inimigos
        float bossSpeed = speed / (factorSpeedInitial - 5f); // Velocidade alta
        List<Integer> bossPatterns = generateBossMovementPatterns(bossEnemyCount);
        
        PlayerStats stats = getCurrentPlayerStats();
        
        return builder
            .setBasicInfo(levelNumber)
            .setEnemyConfiguration(bossEnemyCount, bossSpeed)
            .setMovementPatterns(bossPatterns)
            .setPlayerResources(stats.ammunitions + 15)
            .setPlayerStats(stats.kills, stats.streak, stats.consecutiveKills)
            .build();
    }
    
    // Métodos auxiliares para cálculos específicos
    
    private int calculateEnemyCount(LevelConfig previousConfig) {
        return previousConfig.getEnemyCount() + random.nextInt(4) + 3; // Aumenta de 3 a 6 inimigos
    }
    
    private float calculateEnemySpeed(LevelConfig previousConfig) {
        // Aumenta a velocidade a cada nível par
        if (previousConfig.getLevelNumber() % 2 == 0 && factorSpeedInitial >= 15f) {
            factorSpeedInitial -= 1f;
            return speed / factorSpeedInitial;
        } else {
            return previousConfig.getEnemySpeed();
        }
    }
    
    private PlayerStats getCurrentPlayerStats() {
        var spaceship = SpaceGame.getGame().getMapManager().getSpaceship();
        return new PlayerStats(
            spaceship.getAmmunitions(),
            spaceship.getKillCount(),
            spaceship.getStreakCount(),
            spaceship.getConsecutiveKills()
        );
    }
    
    // Geração de padrões de movimento
    
    private List<Integer> generateMovementPatterns(int enemyCount) {
        List<Integer> patterns = new ArrayList<>();
        List<Integer> weightedPatterns = createWeightedPatterns(enemyCount);

        for (int i = 0; i < enemyCount; i++) {
            patterns.add(weightedPatterns.get(random.nextInt(weightedPatterns.size())));
        }
        return patterns;
    }
    
    private List<Integer> generateChallengeMovementPatterns(int enemyCount) {
        List<Integer> patterns = new ArrayList<>();
        // Para níveis de desafio, usar mais padrões complexos
        for (int i = 0; i < enemyCount; i++) {
            patterns.add(random.nextInt(2) + 1); // Padrões 1 e 2 (mais difíceis)
        }
        return patterns;
    }
    
    private List<Integer> generateBossMovementPatterns(int enemyCount) {
        List<Integer> patterns = new ArrayList<>();
        // Para níveis boss, usar principalmente o padrão mais difícil
        for (int i = 0; i < enemyCount; i++) {
            if (random.nextFloat() < 0.7f) {
                patterns.add(2); // 70% padrão mais difícil
            } else {
                patterns.add(1); // 30% padrão médio
            }
        }
        return patterns;
    }
    
    private List<Integer> createWeightedPatterns(int enemyCount) {
        List<Integer> weightedPatterns = new ArrayList<>();
        int weightFor0 = (int) (enemyCount * 0.45f);
        int weightFor1 = (int) (enemyCount * 0.35f);
        int weightFor2 = enemyCount - weightFor0 - weightFor1;

        for (int i = 0; i < weightFor0; i++) weightedPatterns.add(0);
        for (int i = 0; i < weightFor1; i++) weightedPatterns.add(1);
        for (int i = 0; i < weightFor2; i++) weightedPatterns.add(2);

        return weightedPatterns;
    }
    
    // Classe auxiliar para estatísticas do jogador
    private static class PlayerStats {
        final int ammunitions;
        final int kills;
        final int streak;
        final int consecutiveKills;
        
        PlayerStats(int ammunitions, int kills, int streak, int consecutiveKills) {
            this.ammunitions = ammunitions;
            this.kills = kills;
            this.streak = streak;
            this.consecutiveKills = consecutiveKills;
        }
    }
} 