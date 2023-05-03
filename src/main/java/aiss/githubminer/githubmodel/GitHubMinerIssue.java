package aiss.githubminer.githubmodel;

import java.util.List;

public class GitHubMinerIssue {

    private String id;
    private String ref_id;
    private String title;
    private String description;
    private String state;
    private String created_at;
    private String updated_at;
    private String closed_at;
    private List<String> labels;
    private GitHubMinerUser author;
    private GitHubMinerUser assignee;
    private Integer upvotes;
    private Integer downvotes;
    private String web_url;
    private List<GitHubMinerComment> comments;

    public GitHubMinerIssue(String id, String ref_id, String title, String description,
                            String state, String created_at, String updated_at, String closed_at,
                            List<String> labels, GitHubMinerUser author, GitHubMinerUser assignee, Integer upvotes,
                            Integer downvotes, String web_url, List<GitHubMinerComment> comments) {
        this.id = id;
        this.ref_id = ref_id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.closed_at = closed_at;
        this.labels = labels;
        this.author = author;
        this.assignee = assignee;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.web_url = web_url;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public GitHubMinerUser getAuthor() {
        return author;
    }

    public void setAuthor(GitHubMinerUser author) {
        this.author = author;
    }

    public GitHubMinerUser getAssignee() {
        return assignee;
    }

    public void setAssignee(GitHubMinerUser assignee) {
        this.assignee = assignee;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public List<GitHubMinerComment> getComments() {
        return comments;
    }

    public void setComments(List<GitHubMinerComment> comments) {
        this.comments = comments;
    }
}
