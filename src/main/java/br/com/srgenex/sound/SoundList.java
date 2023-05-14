package br.com.srgenex.sound;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

@Data
public class SoundList {

    private final List<String> sounds;

    public void play(Player p){
        sounds.forEach(s -> {
            Sound sound = Sound.valueOf(s.split(";")[0].toUpperCase());
            float volume = Float.parseFloat(s.split(";")[1]);
            float pitch = Float.parseFloat(s.split(";")[2]);
            p.playSound(p.getLocation(), sound, volume, pitch);
        });
    }

    public void broadcast(){
        Bukkit.getOnlinePlayers().forEach(this::play);
    }

}
