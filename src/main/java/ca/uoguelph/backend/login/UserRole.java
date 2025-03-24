package ca.uoguelph.backend.login;

public enum UserRole {
    ADMIN, STUDENT, FACULTY;

    @Override
    public String toString() {
        return switch (this) {
            case ADMIN -> "Administrator";
            case STUDENT -> "Student";
            case FACULTY -> "Faculty";
        };
    }
}
