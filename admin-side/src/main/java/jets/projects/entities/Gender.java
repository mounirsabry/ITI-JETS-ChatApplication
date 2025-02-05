package jets.projects.entities;

public enum Gender {
    MALE,
    FEMALE;

    @Override
    public String toString() {
        return this.name();
    }
}
