package gui.utils;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Utility class for image manipulation in the UI.
 */
public class ImageUtils {
    
    /**
     * Creates a colored version of an avatar image.
     * 
     * @param imagePath The path to the source image
     * @param color The color to tint the image with
     * @param brightness How much to adjust brightness (-1.0 to 1.0)
     * @param contrast How much to adjust contrast (-1.0 to 1.0)
     * @param hue How much to adjust hue (-1.0 to 1.0)
     * @param saturation How much to adjust saturation (-1.0 to 1.0)
     * @return An ImageView with the colored avatar
     */
    public static ImageView createColoredAvatar(String imagePath, Color color, 
                                                double brightness, double contrast, 
                                                double hue, double saturation) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        
        // Create color adjustment effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(brightness);
        colorAdjust.setContrast(contrast);
        colorAdjust.setHue(hue);
        colorAdjust.setSaturation(saturation);
        
        // Apply effect to the imageView
        imageView.setEffect(colorAdjust);
        
        return imageView;
    }
    
    /**
     * Creates a player avatar with a specific team color.
     * 
     * @param imagePath The path to the avatar image
     * @param isPlayer1 Whether this is for player 1 (true) or player 2 (false)
     * @return An ImageView with the appropriate colored avatar
     */
    public static ImageView createPlayerAvatar(String imagePath, boolean isPlayer1) {
        if (isPlayer1) {
            // Blue tint for player 1
            return createColoredAvatar(imagePath, Color.BLUE, 0.0, 0.1, -0.2, 0.5);
        } else {
            // Red tint for player 2
            return createColoredAvatar(imagePath, Color.RED, 0.0, 0.1, 0.7, 0.5);
        }
    }
} 