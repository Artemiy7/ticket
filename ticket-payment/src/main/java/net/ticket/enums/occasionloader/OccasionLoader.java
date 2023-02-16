package net.ticket.enums.occasionloader;

import java.io.File;

public enum OccasionLoader {
    CONCERT_CLUB_FILE("ticket-payment" + File.separator + "src" + File.separator + "main" + File.separator+"resources" + File.separator
                            + "occasion-loader" + File.separator + "concert" + File.separator + "concertClub.json"),
    CONCERT_CLUB_STADION_FILE("ticket-payment" + File.separator + "src" + File.separator + "main" + File.separator+"resources" + File.separator
                            + "occasion-loader" + File.separator + "concert" + File.separator + "concertStadion.json"),
    TRAIN_INTERCITY_FILE("ticket-payment" + File.separator + "src" + File.separator + "main" + File.separator+"resources" + File.separator
                            + "occasion-loader" + File.separator + "train" + File.separator + "trainIntercity.json");


    String filePath;

    OccasionLoader(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
