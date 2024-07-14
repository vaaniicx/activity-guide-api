package at.vaaniicx.dao;

import java.time.LocalDate;

public record ResultRequest(float latitude, float longitude, int radius, LocalDate startDate, LocalDate endDate) {
}