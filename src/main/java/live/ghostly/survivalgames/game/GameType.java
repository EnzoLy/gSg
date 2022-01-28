package live.ghostly.survivalgames.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameType{
    POT("PotSG"),
    COMBO("ComboSG"),
    BUILD("BuildSG");

    String name;
}
