package com.seinksansdoozebank.fr.model.cards;

public enum DistrictType {
        RELIGION("Religion"),
        NOBILITY("Noblesse"),
        TRADE_AND_CRAFTS("Commerce et artisanat"),
        SOLDIERLY("Soldatesque"),
        PRESTIGE("Prestige");

        private final String name;

        DistrictType(String name){
                this.name=name;
        }

        /**
         * @return the name of the districtType
         */
        public String getName() {
                return name;
        }

        @Override
        public String toString() {
                return this.name;
        }
}
