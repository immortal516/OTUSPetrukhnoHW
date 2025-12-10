package animals;

public enum Color {

    WHITE("белый"),
    BLACK("черный");

    private String value;

    Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}