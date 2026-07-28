package be.vlaanderen.vip.magda.magdamock.soap;

import java.util.List;

public record Domain(String name, List<Service> services) {}
