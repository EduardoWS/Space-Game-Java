package com.space.game.managers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import com.space.game.SpaceGame;


import static com.mongodb.client.model.Filters.eq;

public class ScoreManager {

    // pegar password no drive
    private static final String CONNECTION_STRING = loadConnectionString();
    private static final String DATABASE_NAME = "game_database";
    private static final String COLLECTION_NAME = "scores";
    private static final String FILE_PATH = "data/scores.csv";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private boolean error;

    // Método estático para carregar a connection string do arquivo de propriedades
    private static String loadConnectionString() {
        Properties properties = new Properties();
        try {
            // Usa o sistema de arquivos do LibGDX para ler o arquivo dos assets
            FileHandle fileHandle = Gdx.files.internal("game.properties");
            if (!fileHandle.exists()) {
                Gdx.app.error("ScoreManager", "Arquivo game.properties não encontrado");
                return null;
            }
            properties.load(fileHandle.reader());
            String connectionString = properties.getProperty("DB_CONNECTION_STRING");
            if (connectionString == null || connectionString.trim().isEmpty()) {
                Gdx.app.error("ScoreManager", "DB_CONNECTION_STRING não encontrada ou vazia no game.properties");
                return null;
            }
            
            // Remove as aspas se existirem e decodifica caracteres escapados
            connectionString = connectionString.trim();
            if (connectionString.startsWith("\"") && connectionString.endsWith("\"")) {
                connectionString = connectionString.substring(1, connectionString.length() - 1);
            }
            
            // Decodifica caracteres escapados comuns em properties files
            connectionString = connectionString.replace("\\:", ":");
            connectionString = connectionString.replace("\\=", "=");
            connectionString = connectionString.replace("\\&", "&");
            
            Gdx.app.log("ScoreManager", "Connection string carregada: " + 
                connectionString.substring(0, Math.min(50, connectionString.length())) + "...");
            
            return connectionString;
        } catch (IOException e) {
            Gdx.app.error("ScoreManager", "Não foi possível carregar game.properties", e);
            return null;
        }
    }

    public static class ScoreEntry {
        public String playerName;
        public int score;

        public ScoreEntry(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }
    }

    public ScoreManager() {
        // Verifica se a connection string está disponível antes de tentar conectar
        if (CONNECTION_STRING == null || CONNECTION_STRING.trim().isEmpty()) {
            Gdx.app.log("ScoreManager", "Connection string não disponível. Rodando apenas com scores locais.");
            this.error = true;
            return;
        }

        try {
            Gdx.app.log("ScoreManager", "Tentando conectar ao banco de dados...");
            ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(serverApi)
                    .build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(DATABASE_NAME);
            collection = database.getCollection(COLLECTION_NAME);
            
            // Testa a conexão fazendo uma operação simples
            collection.countDocuments();
            
            Gdx.app.log("ScoreManager", "Conectado com sucesso ao MongoDB!");
            this.error = false;
        } catch (Exception e) {
            Gdx.app.error("ScoreManager", "Erro ao conectar com o banco de dados: " + e.getMessage());
            e.printStackTrace();
            this.error = true;
            
            // Fecha recursos se foram parcialmente inicializados
            if (mongoClient != null) {
                try {
                    mongoClient.close();
                } catch (Exception closeException) {
                    Gdx.app.error("ScoreManager", "Erro ao fechar conexão: " + closeException.getMessage());
                }
                mongoClient = null;
                database = null;
                collection = null;
            }
        }
    }

    public void saveGlobalScore(String playerName, int score) {
        if (this.error || collection == null) {
            Gdx.app.log("ScoreManager", "Banco de dados não disponível. Score global não será salvo.");
            return;
        }
        
        try{
            List<ScoreEntry> scoresList = loadGlobalScores();

            if (scoresList.size() < 10 || score > scoresList.get(scoresList.size() - 1).score) {
                scoresList.add(new ScoreEntry(playerName, score));
            }

            scoresList.sort(Comparator.comparingInt(o -> -o.score));

            if (scoresList.size() > 10) {
                scoresList = scoresList.subList(0, 10);
            }

            collection.drop(); // Clear the collection before inserting new scores
            for (ScoreEntry entry : scoresList) {
                Document doc = new Document("playerName", entry.playerName)
                        .append("score", entry.score);
                collection.insertOne(doc);
            }
            Gdx.app.log("ScoreManager", "Score global de " + score + " salvo para o jogador " + playerName);
        } catch (Exception e) {
            Gdx.app.error("ScoreManager", "Erro ao salvar score global: " + e.getMessage());
            e.printStackTrace();
            this.error = true;
        }
    }

    public List<ScoreEntry> loadGlobalScores() {
        if (this.error || collection == null) {
            Gdx.app.log("ScoreManager", "Banco de dados não disponível. Retornando lista vazia.");
            return new ArrayList<>();
        }
        
        try{
            SpaceGame.getLogger().debug("Loading global scores");
            List<ScoreEntry> scoresList = new ArrayList<>();
            Consumer<Document> processDocument = document -> {
                String playerName = document.getString("playerName");
                int score = document.getInteger("score");
                scoresList.add(new ScoreEntry(playerName, score));
            };
            collection.find().forEach(processDocument);
            scoresList.sort(Comparator.comparingInt(o -> -o.score)); // Ordenate in descending order
            SpaceGame.getLogger().debug("Global scores loaded");
            return scoresList;
        } catch (Exception e) {
            Gdx.app.error("ScoreManager", "Erro ao carregar scores globais: " + e.getMessage());
            e.printStackTrace();
            this.error = true;
            return new ArrayList<>();
        }
    }

    public void saveLocalScore(String playerName, int score) {
        FileHandle file = Gdx.files.local(FILE_PATH);
        List<ScoreEntry> scoresList = loadLocalScores();

        if (scoresList.size() < 10 || score > scoresList.get(scoresList.size() - 1).score) {
            scoresList.add(new ScoreEntry(playerName, score));
        }

        scoresList.sort(Comparator.comparingInt(o -> -o.score));

        if (scoresList.size() > 10) {
            scoresList = scoresList.subList(0, 10);
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(file.write(false))) {
            writer.append("PlayerName,Score\n");
            for (ScoreEntry entry : scoresList) {
                writer.append(entry.playerName)
                        .append(",")
                        .append(String.valueOf(entry.score))
                        .append("\n");
            }
            System.out.println("Local score of " + score + " saved for player " + playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ScoreEntry> loadLocalScores() {
        List<ScoreEntry> scoresList = new ArrayList<>();
        FileHandle file = Gdx.files.local(FILE_PATH);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()))) {
                String line;
                boolean isFirstLine = true;
                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String playerName = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        scoresList.add(new ScoreEntry(playerName, score));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return scoresList;
    }

    public boolean isHighScore(int score) {
        if (this.error || collection == null) {
            return false; // Se não há conexão, não pode ser high score global
        }
        
        List<ScoreEntry> scoresList = loadGlobalScores();
        return scoresList.size() < 10 || score > scoresList.get(scoresList.size() - 1).score;
    }

    public boolean isLocalHighScore(int score) {
        List<ScoreEntry> scoresList = loadLocalScores();
        return scoresList.size() < 10 || score > scoresList.get(scoresList.size() - 1).score;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public boolean isError() {
        return error;
    }
    
    public boolean isDatabaseAvailable() {
        return !error && collection != null;
    }
}
