package br.com.srgenex.progressbar;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
@AllArgsConstructor
public class ProgressBarBuilder {

    private Long current, max;
    private ProgressBar bar;

    public String wrap() {
        if(current < 0) current = 0L;
        if(current > max) current = max;
        float percent = (float) current / max;
        int progressBars = (int) (bar.getBars() * percent);
        return Strings.repeat(translate(bar.getCompleted()), progressBars)
                + Strings.repeat(translate(bar.getUncompleted()), bar.getBars() - progressBars);
    }

    public String translate(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
