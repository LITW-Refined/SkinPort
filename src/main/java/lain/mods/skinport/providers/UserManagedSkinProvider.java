package lain.mods.skinport.providers;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.ObjectUtils;
import lain.mods.skinport.LegacyConversion;
import lain.mods.skinport.SkinData;
import lain.mods.skinport.api.ISkin;
import lain.mods.skinport.api.ISkinProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class UserManagedSkinProvider implements ISkinProvider
{

    public UserManagedSkinProvider()
    {
        File file1 = new File(Minecraft.getMinecraft().mcDataDir, "cachedImages");
        if (!file1.exists())
            file1.mkdirs();
        File file2 = new File(file1, "skins");
        if (!file2.exists())
            file2.mkdirs();
        File file3 = new File(file2, "uuid");
        if (!file3.exists())
            file3.mkdirs();
    }

    @Override
    public ISkin getSkin(AbstractClientPlayer player)
    {
        BufferedImage image = readImage(String.format("skins/uuid/%s.png", ObjectUtils.defaultIfNull(player.getGameProfile().getId(), player.getUniqueID()).toString().replaceAll("-", "")));
        if (image == null)
            image = readImage(String.format("skins/%s.png", ObjectUtils.defaultIfNull(player.getGameProfile().getName(), "")));
        if (image == null)
            return null;
        SkinData data = new SkinData();
        String type = SkinData.judgeSkinType(image);
        if ("legacy".equals(type))
            type = "default";
        image = new LegacyConversion().convert(image);
        data.put(image, type);
        return data;
    }

    private BufferedImage readImage(String name)
    {
        try
        {
            return ImageIO.read(new File(new File(Minecraft.getMinecraft().mcDataDir, "cachedImages"), name));
        }
        catch (Exception e)
        {
        }
        return null;
    }

}
