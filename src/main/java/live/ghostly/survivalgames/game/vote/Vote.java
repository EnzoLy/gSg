package live.ghostly.survivalgames.game.vote;

import com.google.common.collect.Maps;
import live.ghostly.survivalgames.game.GameType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Vote {

    @Getter private static Map<GameType, Vote> voteList = Maps.newHashMap();

    private GameType type;
    private int votes;

    public Vote(GameType type){
        this.type = type;
        votes = 0;
        voteList.put(type, this);
    }

    public void remove(){
        votes--;
        if(votes < 0) votes = 0;
    }

    public void add(){
        votes++;
    }

    public static void addVote(GameType type){
        voteList.get(type).add();
    }

    public static void removeVote(GameType type){
        voteList.get(type).remove();
    }

    public static int getVotesType(GameType type){
        return voteList.get(type).getVotes();
    }
}
