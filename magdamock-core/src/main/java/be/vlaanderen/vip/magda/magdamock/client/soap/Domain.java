package be.vlaanderen.vip.magda.magdamock.client.soap;

import java.util.List;

public record Domain(String name, List<Service> services) {}
