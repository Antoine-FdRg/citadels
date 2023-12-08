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

        /**
         * @param name
         * @return the district type that contains name or null if not
         */
        public static DistrictType getDistrictTypeByString(String name){
                for(DistrictType districtType : DistrictType.values()){
                        if(name.contains(districtType.getName())){
                                return districtType;
                        }
                }
                return null;
        }

        @Override
        public String toString() {
                return this.name;
        }
}
