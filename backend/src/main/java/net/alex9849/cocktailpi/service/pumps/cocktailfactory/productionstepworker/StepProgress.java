package net.alex9849.cocktailpi.service.pumps.cocktailfactory.productionstepworker;

public class StepProgress {
    private boolean finished;
    private int percentCompleted;
    private boolean ingredientEnded;

    public boolean hasIngredientEnded() {
        return ingredientEnded;
    }

    public void setHasIngredientEnded(boolean ended) {
        this.ingredientEnded = ended;
    }

    public int getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(int percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
