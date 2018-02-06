package tantmutti.labb2williaml;

/**
 * Created by mrx on 2018-02-05.
 */

public class Todo {
    private String todoTitle, totoContent;
    private int todoCategory;

    public Todo(){

    }
    public Todo (String todoTitle, String todoContent, int todoCategory){
        this.todoTitle = todoTitle;
        this.totoContent = todoContent;
        this.todoCategory = todoCategory;
    }
    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTotoContent() {
        return totoContent;
    }

    public void setTotoContent(String totoContent) {
        this.totoContent = totoContent;
    }

    public int getTodoCategory() {
        return todoCategory;
    }

    public void setTodoCategory(int todoCategory) {
        this.todoCategory = todoCategory;
    }

}
