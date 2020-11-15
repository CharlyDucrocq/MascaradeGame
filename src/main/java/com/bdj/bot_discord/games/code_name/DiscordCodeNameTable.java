package com.bdj.bot_discord.games.code_name;

import com.bdj.bot_discord.games.code_name.CodeNameTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class DiscordCodeNameTable extends CodeNameTable {
    private Message global;
    private Message[] privates;

    public DiscordCodeNameTable(boolean blueFirst) {
        super(blueFirst);
    }

    private MessageEmbed getEmbed(boolean allReveled){
        EmbedBuilder bd = new EmbedBuilder();
        bd.setDescription("-----------------------------------------------------------------------------------------------");
        bd.setColor(whoStart.getColor());
        for(Word word : table) bd.addField("", allReveled ? word.toStringWithEmote() : word.toString(),true);
        return bd.build();
    }

    public MessageEmbed getEmbedWithReveled() {
        return getEmbed(true);
    }

    public MessageEmbed getEmbedWithoutReveled() {
        return getEmbed(false);
    }

    public void send(MessageChannel global, MessageChannel... privates){
        this.global = global.sendMessage(getEmbedWithoutReveled()).complete();
        MessageEmbed secret = getEmbedWithReveled();
        this.privates = new Message[privates.length];
        int i=0;
        for (MessageChannel channel : privates) this.privates[i++]=channel.sendMessage(secret).complete();
    }

    public void update(){
        global.getChannel().editMessageById(global.getId(), getEmbedWithoutReveled()).queue();
        MessageEmbed secret = getEmbedWithReveled();
        for(Message msg : privates) msg.getChannel().editMessageById(msg.getId(), secret).queue();
    }

    @Override
    protected boolean reveal(Word word) {
        boolean result = super.reveal(word);
        if(result) update();
        return result;
    }
}
