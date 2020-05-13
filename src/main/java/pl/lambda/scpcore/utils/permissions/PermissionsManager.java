package pl.lambda.scpcore.utils.permissions;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

import java.util.Map;
import java.util.Set;

public class PermissionsManager
{
    public void addPermission(Player p, String permission)
    {
        if(!p.hasPermission(permission))
        {
            p.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, permission, Tristate.TRUE);
        }
    }

    public void removePermission(Player p, String permission)
    {
        if(p.hasPermission(permission))
        {
            p.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, permission, Tristate.FALSE);
        }
    }

    public void clearPermissions(Player p)
    {
        for(Map.Entry<Set<Context>, Map<String, Boolean>> subjectData : p.getSubjectData().getAllPermissions().entrySet())
        {
            for(Map.Entry<String, Boolean> permissions : subjectData.getValue().entrySet())
            {
                if(permissions.getValue())
                {
                    p.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, permissions.getKey(), Tristate.FALSE);
                }
            }
        }
    }
}
