package pl.lambda.scpcore.utils.discordutils;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;

public class DiscordRequestsManager
{
    private static DiscordRequestsManager instance;

    private HashMap<Member, DiscordRequest> pendingRequests;

    public DiscordRequestsManager()
    {
        instance = this;
        pendingRequests = new HashMap<>();
    }

    public void registerNewRequest(Member member, DiscordRequest request)
    {
        pendingRequests.put(member, request);
    }

    public void clearRequest(Member member)
    {
        pendingRequests.remove(member);
    }

    public DiscordRequest getRequest(Member member)
    {
        return pendingRequests.get(member);
    }

    public static DiscordRequestsManager getInstance()
    {
        return instance;
    }
}
