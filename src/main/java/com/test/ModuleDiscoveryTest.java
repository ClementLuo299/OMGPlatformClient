package com.test;

import com.games.GameModule;
import com.games.sourcing.ModuleLoader;
import com.utils.error_handling.Logging;

import java.util.List;

/**
 * Test class to verify that the module discovery system works correctly.
 * This can be run to test if games are being discovered from the modules directory.
 * 
 * @authors Clement Luo
 * @date January 2025
 * @since 1.0
 */
public class ModuleDiscoveryTest {
    
    public static void main(String[] args) {
        Logging.info("🧪 Starting Module Discovery Test");
        
        try {
            // Test module loading
            List<GameModule> modules = ModuleLoader.loadAllModules();
            
            Logging.info("📊 Module Discovery Results:");
            Logging.info("Found " + modules.size() + " modules");
            
            if (modules.isEmpty()) {
                Logging.warning("⚠️ No modules found. This might be expected if:");
                Logging.warning("   - Modules are not compiled yet");
                Logging.warning("   - Modules directory doesn't exist");
                Logging.warning("   - Module classes are not in the classpath");
            } else {
                for (GameModule module : modules) {
                    Logging.info("✅ " + module.getGameName() + " (" + module.getGameId() + ")");
                    Logging.info("   Category: " + module.getGameCategory());
                    Logging.info("   Difficulty: " + module.getDifficulty().getDisplayName());
                    Logging.info("   Players: " + module.getMinPlayers() + "-" + module.getMaxPlayers());
                    Logging.info("   Duration: " + module.getEstimatedDuration() + " minutes");
                }
            }
            
        } catch (Exception e) {
            Logging.error("❌ Test failed: " + e.getMessage(), e);
        }
        
        Logging.info("🏁 Module Discovery Test completed");
    }
} 