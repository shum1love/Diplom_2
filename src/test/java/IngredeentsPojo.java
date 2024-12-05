import java.util.List;

public class IngredeentsPojo {
    private List<String> ingredients;

    public IngredeentsPojo(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}