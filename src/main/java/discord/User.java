package discord;

import net.dv8tion.jda.api.entities.Member;

public class User {
    private Member member;

    public User(Member member){
        this.member = member;
    }
}
