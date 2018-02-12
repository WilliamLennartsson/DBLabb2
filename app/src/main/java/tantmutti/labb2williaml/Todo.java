package tantmutti.labb2williaml;

/**
 * Created by mrx on 2018-02-05.
 */

public class Todo {
    private String todoTitle, totoContent;
    private int todoCategory;

    public int getTodoID() {
        return todoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }

    private int todoID;

    public String getCategoryString() {
        return categoryString;
    }

    public void setCategoryString(String categoryString) {
        this.categoryString = categoryString;
    }

    private String categoryString;
    private int todoUserID;

    public int getTodoUserID() {
        return todoUserID;
    }

    public void setTodoUserID(int todoUserID) {
        this.todoUserID = todoUserID;
    }

    public Todo(){

    }
    public Todo (String todoTitle, String todoContent, int todoCategory, int todoUserID){
        this.todoTitle = todoTitle;
        this.totoContent = todoContent;
        this.todoCategory = todoCategory;
        this.todoUserID = todoUserID;
    }
    public Todo (String todoTitle, String todoContent, int todoCategory){
        this.todoTitle = todoTitle;
        this.totoContent = todoContent;
        this.todoCategory = todoCategory;
    }
    public Todo (String todoTitle, String todoContent){
        this.todoTitle = todoTitle;
        this.totoContent = todoContent;
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
