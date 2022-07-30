package bg.softuni.kickboxing.model.dto.post;

public class SearchPostDTO {

    private String query;

    public SearchPostDTO() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isEmpty() {
        return this.query == null;
    }
}
