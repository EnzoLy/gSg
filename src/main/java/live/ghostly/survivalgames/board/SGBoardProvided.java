package live.ghostly.survivalgames.board;

import com.google.common.collect.Lists;
import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.GameState;
import live.ghostly.survivalgames.game.GameType;
import live.ghostly.survivalgames.game.SGGame;
import live.ghostly.survivalgames.game.vote.Vote;
import me.joeleoli.nucleus.scoreboard.ScoreboardConfiguration;
import me.joeleoli.nucleus.scoreboard.TitleGetter;
import me.joeleoli.nucleus.scoreboard.provide.ScoreProvider;
import me.joeleoli.nucleus.util.Style;
import org.bukkit.entity.Player;

import java.util.List;

public class SGBoardProvided implements ScoreProvider {

    public static ScoreboardConfiguration create(){
        ScoreboardConfiguration scoreboardConfiguration = new ScoreboardConfiguration();
        scoreboardConfiguration.setTitleGetter(new TitleGetter("&cSG &fGhostly"));
        scoreboardConfiguration.setScoreGetter(new SGBoardProvided());
        return scoreboardConfiguration;
    }

    @Override
    public List<String> getScores(Player player) {
        List<String> lines = Lists.newArrayList();

        SGGame game = SG.get().getSgGame();
        lines.add(Style.BORDER_LINE_SCOREBOARD);

        if(game.getGameState() == GameState.VOTING){
            lines.add("&fVotes&7:");
            lines.add("&c Pot&7:&f " + Vote.getVotesType(GameType.POT));
            //lines.add("&c Combo&7:&f " + Vote.getVotesType(GameType.COMBO));
            lines.add("&c Build&7:&f " + Vote.getVotesType(GameType.BUILD));
        }

        lines.add("&f" + Style.BORDER_LINE_SCOREBOARD);

        return lines;
    }
}
