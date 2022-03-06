package me.aleiv.gameengine.utilities;

import me.aleiv.gameengine.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Animation {

    private AtomicInteger frame;
    private final List<Character> frames;
    private final boolean loop;

    private boolean playing;

    private BukkitTask task;

    public Animation(List<Character> frames, boolean loop) {
        this.frame = new AtomicInteger(0);
        this.frames = frames;
        this.loop = loop;
        this.playing = false;
    }

    public void play() {
        if (playing) {
            stop();
        }

        this.playing = true;
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
            if (!this.playing) return;
            int f = this.frame.getAndIncrement();
            if (f >= this.frames.size()) {
                if (this.loop) {
                    this.frame.set(0);
                    f = 0;
                } else {
                    this.stop();
                    return;
                }
            }

            char c = this.frames.get(f);
            Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(String.valueOf(c), "", 0, 5, 5));
        }, 1L, 1L);
    }

    public void resume() {
        this.playing = true;
    }

    public void pause() {
        this.playing = false;
    }

    public void stop() {
        if (!this.playing) return;

        this.playing = false;
        this.task.cancel();
        this.task = null;
        this.frame.set(0);
    }

}
