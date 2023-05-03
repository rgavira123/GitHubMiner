package aiss.githubminer.githubmodel;

import java.util.List;

public class GitHubMinerProject {

    public String id;
    public String name;
    public String web_url;
    private List<GitHubMinerCommit> commits;
    private List<GitHubMinerIssue> issues;

    public GitHubMinerProject(String id, String name, String webUrl, List<GitHubMinerCommit> commits, List<GitHubMinerIssue> issues) {
        this.id = id;
        this.name = name;
        this.web_url = webUrl;
        this.commits = commits;
        this.issues = issues;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public List<GitHubMinerCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<GitHubMinerCommit> commits) {
        this.commits = commits;
    }

    public List<GitHubMinerIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<GitHubMinerIssue> issues) {
        this.issues = issues;
    }

}
