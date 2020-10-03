package me.mederp.rpcore.utils.players;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import me.mederp.rpcore.RPCore;
import me.mederp.rpcore.utils.classes.LambdaClass;
import me.mederp.rpcore.utils.config.LambdaConfig;
import me.mederp.rpcore.utils.enums.ChatType;

import java.util.*;

public class LambdaPlayer
{
    private UUID uuid;
    private String nickname;
    private long discordID;
    private boolean spyMode;
    private boolean realName;
    private boolean grammarMode;
    private ChatType chatType;
    private LambdaClass chosenClass;
    private List<LambdaClass> availableClasses;

    public LambdaPlayer(UUID uuid, String nickname, long discordID, ChatType chatType, LambdaClass chosenClass, List<LambdaClass> availableClasses) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.discordID = discordID;
        this.chatType = chatType;
        this.availableClasses = availableClasses;
        this.chosenClass = null;
        if(availableClasses.contains(chosenClass))
        {
            this.chosenClass = chosenClass;
        }
        this.spyMode = false;
        this.realName = false;
        this.grammarMode = true;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public long getDiscordID() {
        return discordID;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public LambdaClass getChosenClass() {
        return chosenClass;
    }

    public List<LambdaClass> getAvailableClasses() {
        return availableClasses;
    }

    public boolean isSpyMode() {
        return spyMode;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDiscordID(long discordID) {
        this.discordID = discordID;
    }

    public void setSpyMode(boolean spyMode) {
        this.spyMode = spyMode;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public void setChosenClass(LambdaClass chosenClass) {
        this.chosenClass = chosenClass;
    }

    public void setAvailableClasses(List<LambdaClass> availableClasses) {
        this.availableClasses = availableClasses;
    }

    public boolean getRealName() {
        return realName;
    }

    public void setRealName(boolean realName) {
        this.realName = realName;
    }

    public boolean isGrammarMode() {
        return grammarMode;
    }

    public void setGrammarMode(boolean grammarMode) {
        this.grammarMode = grammarMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LambdaPlayer that = (LambdaPlayer) o;
        return discordID == that.discordID &&
                uuid.equals(that.uuid) &&
                nickname.equals(that.nickname) &&
                chatType == that.chatType &&
                chosenClass.equals(that.chosenClass) &&
                availableClasses.equals(that.availableClasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, nickname, discordID, chatType, chosenClass, availableClasses);
    }

    public static LambdaPlayer getOrCreateLambdaPlayer(UUID uuid, String nickname)
    {
        PlayerDataStorage playerDataStorage = RPCore.getInstance().getPlayerDataStorage();
        CommentedConfigurationNode data = playerDataStorage.getNode();
        if(playerExist(uuid))
        {
            try
            {
                long discordID = data.getNode("players", uuid.toString(), "discordID").getLong();
                String lastClassID = data.getNode("players", uuid.toString(), "lastClass").getString();
                if(lastClassID.equals("none")) lastClassID = null;
                List<LambdaClass> availableLambdaClasses = new ArrayList<>();
                if(RPCore.getInstance().getDiscordModule().getGuild().getMemberById(discordID) == null)
                {
                    return new LambdaPlayer(uuid, nickname, discordID, ChatType.GLOBAL, RPCore.getInstance().getLambdaClasses().get(lastClassID), availableLambdaClasses);
                }

                Member member = RPCore.getInstance().getDiscordModule().getGuild().getMemberById(discordID);
                if(member != null)
                {
                    List<Role> discordRoles = member.getRoles();
                    for(Role role : discordRoles)
                    {
                        for(LambdaClass lambdaClass : RPCore.getInstance().getLambdaClasses().values())
                        {
                            if(role.getIdLong() == lambdaClass.getDiscordID())
                            {
                                availableLambdaClasses.add(lambdaClass);
                            }
                        }
                    }
                }
                return new LambdaPlayer(uuid, nickname, discordID, ChatType.GLOBAL, RPCore.getInstance().getLambdaClasses().get(lastClassID), availableLambdaClasses);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        data.getNode("players", uuid.toString(), "discordID").setValue(0);
        playerDataStorage.save();

        return new LambdaPlayer(uuid, nickname, 0, ChatType.GLOBAL, null, new ArrayList<>());
    }

    public static boolean playerExist(UUID uuid)
    {
        PlayerDataStorage playerDataStorage = RPCore.getInstance().getPlayerDataStorage();
        CommentedConfigurationNode data = playerDataStorage.getNode();

        return !data.getNode("players", uuid.toString(), "discordID").isVirtual();
    }

    public void savePlayer()
    {
        PlayerDataStorage playerDataStorage = RPCore.getInstance().getPlayerDataStorage();
        CommentedConfigurationNode data = playerDataStorage.getNode();

        data.getNode("players", this.uuid.toString(), "nickname").setValue(this.nickname);
        data.getNode("players", this.uuid.toString(), "discordID").setValue(this.discordID);
        data.getNode("players", this.uuid.toString(), "lastClass").setValue("none");
        if(this.chosenClass != null)
        {
            data.getNode("players", this.uuid.toString(), "lastClass").setValue(this.chosenClass.getClassID());
        }
        //data.getNode("players", this.uuid.toString(), "grammarMode").setValue(this.grammarMode);
        playerDataStorage.save();
    }
}
