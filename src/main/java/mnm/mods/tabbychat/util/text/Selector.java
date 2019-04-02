package mnm.mods.tabbychat.util.text;

public enum Selector {

    PLAYER('p'),
    ALL('a'),
    ENTITY('e'),
    RANDOM('r');

    private char id;

    Selector(char c) {
        this.id = c;
    }

    @Override
    public String toString() {
        return "@" + id;
    }
}
