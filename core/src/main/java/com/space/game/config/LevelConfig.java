package com.space.game.config;

import java.util.List;

public class LevelConfig {
    private final int levelNumber;
    private final int enemyCount;
    private final float enemySpeed;
    private final List<Integer> enemyMovementPatterns;
    private final int ammunitions;
    private final int kills;
    private final int streak;
    private final int consecutiveKills;

    private LevelConfig(Builder builder) {
        this.levelNumber = builder.levelNumber;
        this.enemyCount = builder.enemyCount;
        this.enemySpeed = builder.enemySpeed;
        this.enemyMovementPatterns = builder.enemyMovementPatterns;
        this.ammunitions = builder.ammunitions;
        this.kills = builder.kills;
        this.streak = builder.streak;
        this.consecutiveKills = builder.consecutiveKills;
    }

    // Getters
    public int getLevelNumber() { return levelNumber; }
    public int getEnemyCount() { return enemyCount; }
    public float getEnemySpeed() { return enemySpeed; }
    public List<Integer> getEnemyMovementPatterns() { return enemyMovementPatterns; }
    public int getAmmunitions() { return ammunitions; }
    public int getKills() { return kills; }
    public int getStreak() { return streak; }
    public int getConsecutiveKills() { return consecutiveKills; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int levelNumber;
        private int enemyCount;
        private float enemySpeed;
        private List<Integer> enemyMovementPatterns;
        private int ammunitions;
        private int kills;
        private int streak;
        private int consecutiveKills;

        public Builder levelNumber(int levelNumber) {
            this.levelNumber = levelNumber;
            return this;
        }

        public Builder enemyCount(int enemyCount) {
            this.enemyCount = enemyCount;
            return this;
        }

        public Builder enemySpeed(float enemySpeed) {
            this.enemySpeed = enemySpeed;
            return this;
        }

        public Builder enemyMovementPatterns(List<Integer> enemyMovementPatterns) {
            this.enemyMovementPatterns = enemyMovementPatterns;
            return this;
        }

        public Builder ammunitions(int ammunitions) {
            this.ammunitions = ammunitions;
            return this;
        }

        public Builder kills(int kills) {
            this.kills = kills;
            return this;
        }

        public Builder streak(int streak) {
            this.streak = streak;
            return this;
        }

        public Builder consecutiveKills(int consecutiveKills) {
            this.consecutiveKills = consecutiveKills;
            return this;
        }

        public LevelConfig build() {
            // Validação movida para o método build
            if (enemyMovementPatterns != null && enemyMovementPatterns.size() != enemyCount) {
                throw new IllegalArgumentException("The size of enemyMovementPatterns must match enemyCount");
            }
            return new LevelConfig(this);
        }
    }
}
