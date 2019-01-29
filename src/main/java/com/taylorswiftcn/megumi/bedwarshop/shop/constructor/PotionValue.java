package com.taylorswiftcn.megumi.bedwarshop.shop.constructor;

import lombok.Data;

@Data
public class PotionValue {

    private Integer effect;
    private Integer duration;
    private Integer amplifier;
    private Boolean ambient;
    private Boolean hasParticles;

    public PotionValue(Integer effect, Integer duration, Integer amplifier, Boolean ambient, Boolean hasParticles) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
        this.ambient = ambient;
        this.hasParticles = hasParticles;
    }
}
