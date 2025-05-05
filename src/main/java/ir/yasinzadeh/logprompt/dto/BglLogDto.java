package ir.yasinzadeh.logprompt.dto;

import java.util.Optional;

public class BglLogDto {
    public Optional<String> epochTimestamp = Optional.empty();
    public Optional<String> shortDate = Optional.empty();
    public Optional<String> location = Optional.empty();
    public Optional<String> fullTimestamp = Optional.empty();
    public Optional<String> repeatedLocation = Optional.empty();
    public Optional<String> ras = Optional.empty();
    public Optional<String> category = Optional.empty();
    public Optional<String> level = Optional.empty();
    public Optional<String> message = Optional.empty();

    @Override
    public String toString() {
        return "BglLogDto{" +
                "epochTimestamp=" + epochTimestamp +
                ", shortDate=" + shortDate +
                ", location=" + location +
                ", fullTimestamp=" + fullTimestamp +
                ", repeatedLocation=" + repeatedLocation +
                ", ras=" + ras +
                ", category=" + category +
                ", level=" + level +
                ", message=" + message +
                '}';
    }
}

