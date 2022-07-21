package bg.softuni.kickboxing.model.exception;

public class UsernameNotFoundException extends RuntimeException {
    private final String username;

    public UsernameNotFoundException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
