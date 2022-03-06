package me.aleiv.gameengine.globalUtilities;

import lombok.EqualsAndHashCode;
import me.aleiv.gameengine.Core;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerSecondEvent;
import me.aleiv.gameengine.globalUtilities.events.timerEvents.GlobalTimerStopEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

@EqualsAndHashCode(callSuper = false)
public class GlobalTimer extends BukkitRunnable {
    Core instance;

    private boolean running;

    private long timer;
    private long timeAddition;
    private long timerLimit;

    private BossBar bossBar;

    private Runnable finishRunnable;

    public GlobalTimer(Core instance) {
        this.instance = instance;
        this.timer = 0;
        this.timeAddition = 1;

        this.bossBar = Bukkit.createBossBar(new NamespacedKey(instance, "TIMER"), "", BarColor.WHITE, BarStyle.SOLID);
        bossBar.setVisible(false);
    }

    @Override
    public void run() {
        if (!this.running) return;

        long oldTime = this.timer;
        this.timer += this.timeAddition;
        update();
        if (this.timer == this.timerLimit) {
            Bukkit.getScheduler().runTask(instance, this.finishRunnable);
            stop();
        } else {
            Bukkit.getPluginManager().callEvent(new GlobalTimerSecondEvent(this, (int) this.timer));
        }
    }

    public void runTimer(long time, Runnable whenFinish) {
        if (this.running) this.stop();

        this.timer = 0;
        this.timeAddition = 1;
        this.timerLimit = time;
        this.finishRunnable = whenFinish;

        this.start();
    }

    public void runCountdown(long time, Runnable whenFinish) {
        if (this.running) this.stop();

        this.timer = time;
        this.timeAddition = -1;
        this.timerLimit = 0;
        this.finishRunnable = whenFinish;

        this.start();
    }

    private void start() {
        this.update();
        this.bossBar.setVisible(true);
        Bukkit.getOnlinePlayers().forEach(this.bossBar::addPlayer);
        this.running = true;
    }

    private void update() {
        String formattedTimer = String.format("%02d:%02d", this.timer / 60, this.timer % 60);
        this.bossBar.setTitle(formattedTimer);
    }

    public void stop() {
        this.stop(false);
    }

    public void stop(boolean forced) {
        this.timeAddition = 0;
        this.running = false;
        this.finishRunnable = null;
        this.bossBar.setVisible(false);

        Bukkit.getPluginManager().callEvent(new GlobalTimerStopEvent(this, forced));
    }

    public boolean isRunning() {
        return this.running;
    }
}