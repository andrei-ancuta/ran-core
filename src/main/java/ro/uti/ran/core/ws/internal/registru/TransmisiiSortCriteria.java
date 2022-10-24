package ro.uti.ran.core.ws.internal.registru;

public enum TransmisiiSortCriteria {

    dataTransmistere("dataTransmistere"),
    codSiruta("codSiruta"),
    denumire("denumire"),
    capitol("capitol"),
    codStareRegistru("codStareRegistru"),
    identificatorGospodarie("identificatorRegistru"),
    indexRegistru("indexRegistru");

    private String path;

    TransmisiiSortCriteria(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return getPath();
    }
}
