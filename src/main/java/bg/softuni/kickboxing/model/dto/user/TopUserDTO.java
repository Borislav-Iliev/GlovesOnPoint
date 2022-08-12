package bg.softuni.kickboxing.model.dto.user;

public class TopUserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private int postsCount;
    private int commentsCount;

    public TopUserDTO() {
    }

    public TopUserDTO(String username, String firstName, String lastName, int postsCount, int commentsCount) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postsCount = postsCount;
        this.commentsCount = commentsCount;
    }

    public String getUsername() {
        return username;
    }

    public TopUserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public TopUserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public TopUserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getPostsCount() {
        return postsCount;
    }

    public TopUserDTO setPostsCount(int postsCount) {
        this.postsCount = postsCount;
        return this;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public TopUserDTO setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
        return this;
    }
}
