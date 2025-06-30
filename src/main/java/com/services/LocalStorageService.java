package com.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.utils.error_handling.Logging;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.prefs.Preferences;

/**
 * Service for securely storing data locally on the client's computer.
 * Provides both encrypted file storage and Java Preferences storage.
 *
 * @authors Clement Luo
 * @date June 29, 2025
 * @since 1.0
 */
public class LocalStorageService {
    
    // ==================== CONSTANTS ====================
    
    private static final String APP_NAME = "OMGPlatform";
    private static final String ENCRYPTED_DATA_DIR = "encrypted_data";
    private static final String SALT_FILE = "salt.dat";
    private static final String KEY_FILE = "key.dat";
    
    // Encryption settings
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int SALT_SIZE = 32;
    
    // ==================== INSTANCE FIELDS ====================
    
    private final Preferences preferences;
    private final Path appDataDir;
    private final Path encryptedDataDir;
    private final Gson gson;
    private SecretKey secretKey;
    
    // ==================== SINGLETON ====================
    
    private static LocalStorageService instance;
    
    public static synchronized LocalStorageService getInstance() {
        if (instance == null) {
            instance = new LocalStorageService();
        }
        return instance;
    }
    
    // ==================== CONSTRUCTOR ====================
    
    private LocalStorageService() {
        Logging.info("Initializing LocalStorageService");
        
        // Initialize Java Preferences
        this.preferences = Preferences.userRoot().node(APP_NAME);
        
        // Initialize application data directory
        this.appDataDir = getAppDataDirectory();
        this.encryptedDataDir = appDataDir.resolve(ENCRYPTED_DATA_DIR);
        
        // Initialize Gson for JSON serialization with custom type adapters
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        
        // Initialize encryption
        initializeEncryption();
        
        Logging.info("LocalStorageService initialized successfully");
    }
    
    // ==================== PREFERENCES STORAGE (Unencrypted) ====================
    
    /**
     * Store a string value in Java Preferences (unencrypted, suitable for non-sensitive data).
     *
     * @param key The key to store the value under
     * @param value The string value to store
     */
    public void storePreference(String key, String value) {
        try {
            Logging.debug("Storing preference: " + key + " = " + (value != null ? "[REDACTED]" : "null"));
            preferences.put(key, value);
            Logging.debug("Successfully stored preference: " + key);
        } catch (Exception e) {
            Logging.error("Failed to store preference: " + key, e);
            throw new RuntimeException("Failed to store preference", e);
        }
    }
    
    /**
     * Retrieve a string value from Java Preferences.
     *
     * @param key The key to retrieve
     * @param defaultValue Default value if key doesn't exist
     * @return The stored value or default value
     */
    public String getPreference(String key, String defaultValue) {
        try {
            String value = preferences.get(key, defaultValue);
            Logging.debug("Retrieved preference: " + key + " = " + (value != null ? "[REDACTED]" : "null"));
            return value;
        } catch (Exception e) {
            Logging.error("Failed to retrieve preference: " + key, e);
            return defaultValue;
        }
    }
    
    /**
     * Store a boolean value in Java Preferences.
     *
     * @param key The key to store the value under
     * @param value The boolean value to store
     */
    public void storePreference(String key, boolean value) {
        try {
            Logging.debug("Storing boolean preference: " + key + " = " + value);
            preferences.putBoolean(key, value);
            Logging.debug("Successfully stored boolean preference: " + key);
        } catch (Exception e) {
            Logging.error("Failed to store boolean preference: " + key, e);
            throw new RuntimeException("Failed to store boolean preference", e);
        }
    }
    
    /**
     * Retrieve a boolean value from Java Preferences.
     *
     * @param key The key to retrieve
     * @param defaultValue Default value if key doesn't exist
     * @return The stored value or default value
     */
    public boolean getPreference(String key, boolean defaultValue) {
        try {
            boolean value = preferences.getBoolean(key, defaultValue);
            Logging.debug("Retrieved boolean preference: " + key + " = " + value);
            return value;
        } catch (Exception e) {
            Logging.error("Failed to retrieve boolean preference: " + key, e);
            return defaultValue;
        }
    }
    
    /**
     * Store an integer value in Java Preferences.
     *
     * @param key The key to store the value under
     * @param value The integer value to store
     */
    public void storePreference(String key, int value) {
        try {
            Logging.debug("Storing integer preference: " + key + " = " + value);
            preferences.putInt(key, value);
            Logging.debug("Successfully stored integer preference: " + key);
        } catch (Exception e) {
            Logging.error("Failed to store integer preference: " + key, e);
            throw new RuntimeException("Failed to store integer preference", e);
        }
    }
    
    /**
     * Retrieve an integer value from Java Preferences.
     *
     * @param key The key to retrieve
     * @param defaultValue Default value if key doesn't exist
     * @return The stored value or default value
     */
    public int getPreference(String key, int defaultValue) {
        try {
            int value = preferences.getInt(key, defaultValue);
            Logging.debug("Retrieved integer preference: " + key + " = " + value);
            return value;
        } catch (Exception e) {
            Logging.error("Failed to retrieve integer preference: " + key, e);
            return defaultValue;
        }
    }
    
    /**
     * Remove a preference by key.
     *
     * @param key The key to remove
     */
    public void removePreference(String key) {
        try {
            Logging.debug("Removing preference: " + key);
            preferences.remove(key);
            Logging.debug("Successfully removed preference: " + key);
        } catch (Exception e) {
            Logging.error("Failed to remove preference: " + key, e);
            throw new RuntimeException("Failed to remove preference", e);
        }
    }
    
    // ==================== ENCRYPTED FILE STORAGE ====================
    
    /**
     * Store an object as encrypted JSON file.
     *
     * @param filename The filename to store the data under
     * @param data The object to store (will be serialized to JSON)
     * @param <T> The type of the object
     */
    public <T> void storeEncryptedData(String filename, T data) {
        try {
            Logging.debug("Storing encrypted data: " + filename);
            
            // Ensure encrypted data directory exists
            if (!Files.exists(encryptedDataDir)) {
                Files.createDirectories(encryptedDataDir);
            }
            
            // Serialize object to JSON
            String json = gson.toJson(data);
            
            // Encrypt the JSON string
            String encryptedData = encrypt(json);
            
            // Write to file
            Path filePath = encryptedDataDir.resolve(filename + ".enc");
            Files.write(filePath, encryptedData.getBytes(StandardCharsets.UTF_8));
            
            Logging.debug("Successfully stored encrypted data: " + filename);
        } catch (Exception e) {
            Logging.error("Failed to store encrypted data: " + filename, e);
            throw new RuntimeException("Failed to store encrypted data", e);
        }
    }
    
    /**
     * Retrieve an object from encrypted JSON file.
     *
     * @param filename The filename to retrieve the data from
     * @param classType The class type of the object
     * @param <T> The type of the object
     * @return The deserialized object or null if not found
     */
    public <T> T getEncryptedData(String filename, Class<T> classType) {
        try {
            Logging.debug("Retrieving encrypted data: " + filename);
            
            Path filePath = encryptedDataDir.resolve(filename + ".enc");
            if (!Files.exists(filePath)) {
                Logging.debug("Encrypted data file not found: " + filename);
                return null;
            }
            
            // Read encrypted data from file
            String encryptedData = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            
            // Decrypt the data
            String json = decrypt(encryptedData);
            
            // Deserialize JSON to object
            T data = gson.fromJson(json, classType);
            
            Logging.debug("Successfully retrieved encrypted data: " + filename);
            return data;
        } catch (Exception e) {
            Logging.error("Failed to retrieve encrypted data: " + filename, e);
            return null;
        }
    }
    
    /**
     * Check if encrypted data file exists.
     *
     * @param filename The filename to check
     * @return true if the file exists, false otherwise
     */
    public boolean hasEncryptedData(String filename) {
        Path filePath = encryptedDataDir.resolve(filename + ".enc");
        return Files.exists(filePath);
    }
    
    /**
     * Delete encrypted data file.
     *
     * @param filename The filename to delete
     */
    public void deleteEncryptedData(String filename) {
        try {
            Logging.debug("Deleting encrypted data: " + filename);
            
            Path filePath = encryptedDataDir.resolve(filename + ".enc");
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                Logging.debug("Successfully deleted encrypted data: " + filename);
            } else {
                Logging.debug("Encrypted data file not found for deletion: " + filename);
            }
        } catch (Exception e) {
            Logging.error("Failed to delete encrypted data: " + filename, e);
            throw new RuntimeException("Failed to delete encrypted data", e);
        }
    }
    
    // ==================== ENCRYPTION METHODS ====================
    
    /**
     * Initialize encryption by generating or loading encryption key.
     */
    private void initializeEncryption() {
        try {
            Logging.debug("Initializing encryption");
            
            Path saltPath = appDataDir.resolve(SALT_FILE);
            Path keyPath = appDataDir.resolve(KEY_FILE);
            
            if (Files.exists(saltPath) && Files.exists(keyPath)) {
                // Load existing key
                loadEncryptionKey(saltPath, keyPath);
            } else {
                // Generate new key
                generateEncryptionKey(saltPath, keyPath);
            }
            
            Logging.debug("Encryption initialized successfully");
        } catch (Exception e) {
            Logging.error("Failed to initialize encryption", e);
            throw new RuntimeException("Failed to initialize encryption", e);
        }
    }
    
    /**
     * Generate a new encryption key and save it.
     */
    private void generateEncryptionKey(Path saltPath, Path keyPath) throws Exception {
        Logging.debug("Generating new encryption key");
        
        // Generate salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        
        // Generate key from salt (in a real app, you'd use a password-based key derivation)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] keyBytes = digest.digest();
        
        // Create secret key
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        
        // Save salt and key
        Files.write(saltPath, salt);
        Files.write(keyPath, keyBytes);
        
        Logging.debug("New encryption key generated and saved");
    }
    
    /**
     * Load existing encryption key.
     */
    private void loadEncryptionKey(Path saltPath, Path keyPath) throws Exception {
        Logging.debug("Loading existing encryption key");
        
        byte[] keyBytes = Files.readAllBytes(keyPath);
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
        
        Logging.debug("Existing encryption key loaded");
    }
    
    /**
     * Encrypt a string using AES encryption.
     */
    private String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    /**
     * Decrypt a string using AES decryption.
     */
    private String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Get the application data directory.
     */
    private Path getAppDataDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        Path appDataPath;
        
        if (os.contains("win")) {
            // Windows
            appDataPath = Paths.get(System.getenv("APPDATA"), APP_NAME);
        } else if (os.contains("mac")) {
            // macOS
            appDataPath = Paths.get(System.getProperty("user.home"), "Library", "Application Support", APP_NAME);
        } else {
            // Linux and others
            appDataPath = Paths.get(System.getProperty("user.home"), ".config", APP_NAME);
        }
        
        // Create directory if it doesn't exist
        try {
            if (!Files.exists(appDataPath)) {
                Files.createDirectories(appDataPath);
            }
        } catch (IOException e) {
            Logging.error("Failed to create app data directory: " + appDataPath, e);
            // Fallback to user home directory
            appDataPath = Paths.get(System.getProperty("user.home"), "." + APP_NAME.toLowerCase());
            try {
                if (!Files.exists(appDataPath)) {
                    Files.createDirectories(appDataPath);
                }
            } catch (IOException ex) {
                Logging.error("Failed to create fallback app data directory: " + appDataPath, ex);
                throw new RuntimeException("Failed to create app data directory", ex);
            }
        }
        
        Logging.debug("App data directory: " + appDataPath);
        return appDataPath;
    }
    
    /**
     * Clear all stored data (both preferences and encrypted files).
     */
    public void clearAllData() {
        try {
            Logging.info("Clearing all local storage data");
            
            // Clear preferences
            preferences.clear();
            
            // Delete encrypted data directory
            if (Files.exists(encryptedDataDir)) {
                Files.walk(encryptedDataDir)
                        .sorted((a, b) -> b.compareTo(a)) // Delete files before directories
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                Logging.warning("Failed to delete file: " + path, e);
                            }
                        });
            }
            
            Logging.info("All local storage data cleared successfully");
        } catch (Exception e) {
            Logging.error("Failed to clear all local storage data", e);
            throw new RuntimeException("Failed to clear all local storage data", e);
        }
    }
    
    /**
     * Get the path to the encrypted data directory.
     */
    public Path getEncryptedDataDirectory() {
        return encryptedDataDir;
    }
    
    // ==================== CUSTOM TYPE ADAPTERS ====================
    
    /**
     * Custom type adapter for LocalDateTime serialization and deserialization.
     */
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(FORMATTER));
        }
        
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), FORMATTER);
        }
    }
} 