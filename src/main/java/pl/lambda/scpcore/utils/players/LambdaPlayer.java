package pl.lambda.scpcore.utils.players;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import pl.lambda.scpcore.SCPCore;
import pl.lambda.scpcore.utils.classes.LambdaClass;
import pl.lambda.scpcore.utils.enums.ChatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LambdaPlayer
{
    private UUID uuid;
    private String nickname;
    private long discordID;
    private boolean spyMode;
    private boolean realName;
    private ChatType chatType;
    private LambdaClass chosenClass;
    private List<LambdaClass> availableClasses;

    public LambdaPlayer(UUID uuid, String nickname, long discordID, ChatType chatType, LambdaClass chosenClass, List<LambdaClass> availableClasses) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.discordID = discordID;
        this.chatType = chatType;
        this.chosenClass = chosenClass;
        this.availableClasses = availableClasses;
        this.spyMode = false;
        this.realName = false;
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
        PlayerDataStorage playerDataStorage = SCPCore.getInstance().getPlayerDataStorage();
        CommentedConfigurationNode data = playerDataStorage.getNode();
        if(playerExist(uuid))
        {
            try
            {
                long discordID = data.getNode("players", uuid.toString(), "discordID").getLong();
                List<LambdaClass> availableLambdaClasses = new ArrayList<>();
                if(SCPCore.getInstance().getDiscordModule().getGuild().getMemberById(discordID) == null)
                {
                    return new LambdaPlayer(uuid, nickname, discordID, ChatType.GLOBAL, null, availableLambdaClasses);
                }

                Member member = SCPCore.getInstance().getDiscordModule().getGuild().getMemberById(discordID);
                if(member != null)
                {
                    List<Role> discordRoles = member.getRoles();
                    for(Role role : discordRoles)
                    {
                        for(LambdaClass lambdaClass : SCPCore.getInstance().getLambdaClasses().values())
                        {
                            if(role.getIdLong() == lambdaClass.getDiscordID())
                            {
                                availableLambdaClasses.add(lambdaClass);
                            }
                        }
                    }
                }
                return new LambdaPlayer(uuid, nickname, discordID, ChatType.GLOBAL, null, availableLambdaClasses);
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
        PlayerDataStorage playerDataStorage = SCPCore.getInstance().getPlayerDataStorage();
        CommentedConfigurationNode data = playerDataStorage.getNode();

        return !data.getNode("players", uuid.toString(), "discordID").isVirtual();
    }

    public void savePlayer()
    {
        PlayerDataStorage playerDataStorage = SCPCore.getInstance().getPlayerDataStorage();
        CommentedConfigurationNode data = playerDataStorage.getNode();
        List<String> classIDs = new ArrayList<>();

        for(LambdaClass lambdaClass : availableClasses)
        {
            classIDs.add(lambdaClass.getClassID());
        }

        data.getNode("players", this.uuid.toString(), "nickname").setValue(this.nickname);
        data.getNode("players", this.uuid.toString(), "discordID").setValue(this.discordID);
        playerDataStorage.save();
    }
}
