package by.epamtc.zarutski.dao.search;

public enum SearchPattern {

    BY_PASSPORT("^[a-zA-Z]{2}\\d{7}$");

    String searchPattern;

    SearchPattern(String searchPattern) {
        this.searchPattern = searchPattern;
    }
}