package com.space.game.levels;

import java.util.HashMap;
import java.util.Map;

import com.space.game.config.LevelConfig;
import com.space.game.config.LevelConfigBuilder;
import com.space.game.config.StandardLevelConfigBuilder;

public class LevelFactory {
    private Map<Integer, LevelConfig> levelConfigs;
    private LevelConfigDirector director;
    private LevelConfigBuilder builder;

    public LevelFactory() {
        this.levelConfigs = new HashMap<>();
        this.director = new LevelConfigDirector();
        this.builder = new StandardLevelConfigBuilder();
    }

    public Level createLevel(int levelNumber) {
        LevelConfig config = levelConfigs.get(levelNumber);
        if (config == null) {
            config = generateNewLevelConfig(levelNumber);
            levelConfigs.put(levelNumber, config);
        }
        return new DynamicLevel(config);
    }

    private LevelConfig generateNewLevelConfig(int levelNumber) {
        if (levelNumber == 1) {
            // Usa o Director para construir o primeiro nível
            return director.buildFirstLevel(builder);
        } else {
            // Usa o Director para construir níveis progressivos
            LevelConfig previousConfig = levelConfigs.get(levelNumber - 1);
            
            // Determina o tipo de nível baseado no número
            if (levelNumber % 10 == 0) {
                // A cada 10 níveis, cria um nível boss
                return director.buildBossLevel(builder, levelNumber);
            } else if (levelNumber % 7 == 0) {
                // A cada 5 níveis, cria um nível de desafio
                return director.buildChallengeLevel(builder, levelNumber, previousConfig);
            } else {
                // Níveis normais progressivos
                return director.buildProgressiveLevel(builder, levelNumber, previousConfig);
            }
        }
    }

    public void dispose() {
        levelConfigs.clear();
    }
}
