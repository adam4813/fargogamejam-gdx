package fgm.fgj.gamejamgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

import static fgm.fgj.gamejamgame.IconType.BUTTON1;
import static fgm.fgj.gamejamgame.IconType.BUTTON2;
import static fgm.fgj.gamejamgame.IconType.BUTTON3;
import static fgm.fgj.gamejamgame.IconType.BUTTON4;
import static fgm.fgj.gamejamgame.IconType.BUTTON5;
import static fgm.fgj.gamejamgame.IconType.EXIT_BUTTON;
import static fgm.fgj.gamejamgame.IconType.INFO_BUTTON;
import static fgm.fgj.gamejamgame.IconType.PLAY_BUTTON;
import static fgm.fgj.gamejamgame.IconType.RESOURCE_BLUE;
import static fgm.fgj.gamejamgame.IconType.RESOURCE_BROWN;
import static fgm.fgj.gamejamgame.IconType.RESOURCE_GRAY;
import static fgm.fgj.gamejamgame.IconType.RESOURCE_GREEN;
import static fgm.fgj.gamejamgame.IconType.RESOURCE_PURPLE;
import static fgm.fgj.gamejamgame.IconType.RESOURCE_YELLOW;
import static fgm.fgj.gamejamgame.IconType.SHIP_EQUIP_BUTTON;
import static fgm.fgj.gamejamgame.IconType.SHIP_INFO_BUTTON;
import static fgm.fgj.gamejamgame.IconType.SOLAR_SYSTEM_BUTTON;
import static fgm.fgj.gamejamgame.IconType.SPECIALIZATION_BLUE;
import static fgm.fgj.gamejamgame.IconType.SPECIALIZATION_BROWN;
import static fgm.fgj.gamejamgame.IconType.SPECIALIZATION_YELLOW;
import static fgm.fgj.gamejamgame.IconType.SPECIES_BLACK;
import static fgm.fgj.gamejamgame.IconType.SPECIES_BROWN;
import static fgm.fgj.gamejamgame.IconType.SPECIES_DARK_BLUE;
import static fgm.fgj.gamejamgame.IconType.SPECIES_DARK_PURPLE;
import static fgm.fgj.gamejamgame.IconType.SPECIES_GREEN;
import static fgm.fgj.gamejamgame.IconType.SPECIES_LIGHT_BLUE;
import static fgm.fgj.gamejamgame.IconType.SPECIES_PINK;
import static fgm.fgj.gamejamgame.IconType.SPECIES_PURPLE;
import static fgm.fgj.gamejamgame.IconType.SPECIES_TEAL;
import static fgm.fgj.gamejamgame.IconType.SPECIES_WHITE;
import static fgm.fgj.gamejamgame.IconType.STARMAP_BUTTON;

public class Icons {
	TextureRegion[] icons;

	private static EnumMap<IconType, TextureRegion> iconTypeMap = new EnumMap<>(IconType.class);

	public static void initIconTextures(GameJamGame game) {
		Texture texture = game.getAsset("data/icons.png");
		TextureRegion[][] tmpFrames = TextureRegion.split(texture, 64, 64);
		int i = 0;
		iconTypeMap.put(SOLAR_SYSTEM_BUTTON, tmpFrames[0][i++]);
		iconTypeMap.put(STARMAP_BUTTON, tmpFrames[0][i++]);
		iconTypeMap.put(INFO_BUTTON, tmpFrames[0][i++]);
		iconTypeMap.put(SHIP_EQUIP_BUTTON, tmpFrames[0][i++]);
		iconTypeMap.put(BUTTON1, tmpFrames[0][i++]);
		iconTypeMap.put(BUTTON2, tmpFrames[0][i++]);
		iconTypeMap.put(BUTTON3, tmpFrames[0][i++]);
		iconTypeMap.put(BUTTON4, tmpFrames[0][i++]);
		iconTypeMap.put(BUTTON5, tmpFrames[0][i++]);
		iconTypeMap.put(SHIP_INFO_BUTTON, tmpFrames[0][i++]);
		iconTypeMap.put(PLAY_BUTTON, tmpFrames[0][i++]);
		iconTypeMap.put(EXIT_BUTTON, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_DARK_PURPLE, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_WHITE, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_GREEN, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_BROWN, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_TEAL, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_BLACK, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_LIGHT_BLUE, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_DARK_BLUE, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_PINK, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIES_PURPLE, tmpFrames[0][i++]);
		iconTypeMap.put(RESOURCE_YELLOW, tmpFrames[0][i++]);
		iconTypeMap.put(RESOURCE_BROWN, tmpFrames[0][i++]);
		iconTypeMap.put(RESOURCE_BLUE, tmpFrames[0][i++]);
		iconTypeMap.put(RESOURCE_GRAY, tmpFrames[0][i++]);
		iconTypeMap.put(RESOURCE_GREEN, tmpFrames[0][i++]);
		iconTypeMap.put(RESOURCE_PURPLE, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIALIZATION_BROWN, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIALIZATION_BLUE, tmpFrames[0][i++]);
		iconTypeMap.put(SPECIALIZATION_YELLOW, tmpFrames[0][i++]);
	}

	public static TextureRegion getIcon(IconType iconType) {
		return iconTypeMap.get(iconType);
	}
}
