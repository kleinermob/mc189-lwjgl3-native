package net.minecraft.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

// TODO: Sound is stubbed out - paulscode removed. Re-implement with LWJGL3 OpenAL or patched paulscode.
public class SoundManager
{
    /** The marker used for logging */
    private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
    private static final Logger logger = LogManager.getLogger();

    /** A reference to the sound handler. */
    private final SoundHandler sndHandler;

    /** Reference to the GameSettings object. */
    private final GameSettings options;

    /** Set to true when the SoundManager has been initialised. */
    private boolean loaded;

    /** A counter for how long the sound manager has been running */
    private int playTime = 0;
    private final Map<String, ISound> playingSounds = HashBiMap.<String, ISound>create();
    private final Map<ISound, String> invPlayingSounds;
    private Map<ISound, SoundPoolEntry> playingSoundPoolEntries;
    private final Multimap<SoundCategory, String> categorySounds;
    private final List<ITickableSound> tickableSounds;
    private final Map<ISound, Integer> delayedSounds;
    private final Map<String, Integer> playingSoundsStopTime;

    public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_)
    {
        this.invPlayingSounds = ((BiMap)this.playingSounds).inverse();
        this.playingSoundPoolEntries = Maps.<ISound, SoundPoolEntry>newHashMap();
        this.categorySounds = HashMultimap.<SoundCategory, String>create();
        this.tickableSounds = Lists.<ITickableSound>newArrayList();
        this.delayedSounds = Maps.<ISound, Integer>newHashMap();
        this.playingSoundsStopTime = Maps.<String, Integer>newHashMap();
        this.sndHandler = p_i45119_1_;
        this.options = p_i45119_2_;
    }

    public void reloadSoundSystem()
    {
        this.unloadSoundSystem();
        this.loadSoundSystem();
    }

    private synchronized void loadSoundSystem()
    {
        if (!this.loaded)
        {
            this.loaded = true;
            logger.info(LOG_MARKER, "Sound engine started (STUBBED - no audio)");
        }
    }

    private float getSoundCategoryVolume(SoundCategory category)
    {
        return category != null && category != SoundCategory.MASTER ? this.options.getSoundLevel(category) : 1.0F;
    }

    public void setSoundCategoryVolume(SoundCategory category, float volume)
    {
    }

    public void unloadSoundSystem()
    {
        if (this.loaded)
        {
            this.stopAllSounds();
            this.loaded = false;
        }
    }

    public void stopAllSounds()
    {
        this.playingSounds.clear();
        this.delayedSounds.clear();
        this.tickableSounds.clear();
        this.categorySounds.clear();
        this.playingSoundPoolEntries.clear();
        this.playingSoundsStopTime.clear();
    }

    public void updateAllSounds()
    {
        ++this.playTime;

        Iterator<ITickableSound> tickIt = this.tickableSounds.iterator();
        while (tickIt.hasNext())
        {
            ITickableSound itickablesound = tickIt.next();
            itickablesound.update();

            if (itickablesound.isDonePlaying())
            {
                tickIt.remove();
                String s = (String)this.invPlayingSounds.get(itickablesound);
                if (s != null)
                {
                    this.playingSounds.remove(s);
                    this.playingSoundPoolEntries.remove(itickablesound);
                    this.playingSoundsStopTime.remove(s);
                }
            }
        }

        Iterator<Entry<ISound, Integer>> iterator1 = this.delayedSounds.entrySet().iterator();
        while (iterator1.hasNext())
        {
            Entry<ISound, Integer> entry1 = (Entry)iterator1.next();
            if (this.playTime >= ((Integer)entry1.getValue()).intValue())
            {
                ISound isound1 = (ISound)entry1.getKey();
                if (isound1 instanceof ITickableSound)
                {
                    ((ITickableSound)isound1).update();
                }
                this.playSound(isound1);
                iterator1.remove();
            }
        }
    }

    public boolean isSoundPlaying(ISound sound)
    {
        return false;
    }

    public void stopSound(ISound sound)
    {
        if (this.loaded)
        {
            String s = (String)this.invPlayingSounds.get(sound);
            if (s != null)
            {
                this.playingSounds.remove(s);
                this.playingSoundPoolEntries.remove(sound);
                this.playingSoundsStopTime.remove(s);
            }
        }
    }

    public void playSound(ISound p_sound)
    {
    }

    private float getNormalizedPitch(ISound sound, SoundPoolEntry entry)
    {
        return (float)MathHelper.clamp_double((double)sound.getPitch() * entry.getPitch(), 0.5D, 2.0D);
    }

    private float getNormalizedVolume(ISound sound, SoundPoolEntry entry, SoundCategory category)
    {
        return (float)MathHelper.clamp_double((double)sound.getVolume() * entry.getVolume(), 0.0D, 1.0D) * this.getSoundCategoryVolume(category);
    }

    public void pauseAllSounds()
    {
    }

    public void resumeAllSounds()
    {
    }

    public void playDelayedSound(ISound sound, int delay)
    {
        this.delayedSounds.put(sound, Integer.valueOf(this.playTime + delay));
    }

    public void setListener(EntityPlayer player, float p_148615_2_)
    {
    }
}
