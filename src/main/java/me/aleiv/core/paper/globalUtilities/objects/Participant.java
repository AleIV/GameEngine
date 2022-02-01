package me.aleiv.core.paper.globalUtilities.objects;

import java.util.UUID;

import lombok.Getter;

public class Participant {

    @Getter UUID uuid;
    
    public Participant(UUID uuid){
        this.uuid = uuid;

    }

    public boolean is(UUID uuid){
        return this.uuid.getMostSignificantBits() == uuid.getMostSignificantBits();
    }



}
