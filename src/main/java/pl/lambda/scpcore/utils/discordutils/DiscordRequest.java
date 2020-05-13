package pl.lambda.scpcore.utils.discordutils;

import net.dv8tion.jda.api.entities.Member;

public class DiscordRequest
{
    private Member member;
    private String requestName;
    private int step;

    public DiscordRequest(Member member, String requestName, int step) {
        this.member = member;
        this.requestName = requestName;
        this.step = step;
    }

    public void nextStep()
    {
        this.step++;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public int getStep() {
        return step;
    }
}
