package aiss.githubminer.controller;

import aiss.githubminer.githubmodel.*;
import aiss.githubminer.model.FullAuthor;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import aiss.githubminer.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Comment;
@RestController
@RequestMapping("/github")
public class ProjectController {

    @Autowired
    GitHubService gitHubService;

    @Autowired
    RestTemplate restTemplate;

    // GET http://localhost:8080/github/{owner}/{repo}?since={since}&maxPages={maxPages}
    @GetMapping("/{owner}/{repo}")
    public GitHubMinerProject findOneProject(@PathVariable String owner,
                                             @PathVariable String repo,
                                             @RequestParam(required = false, name = "sinceCommmits") Integer sinceCommits,
                                             @RequestParam(required = false, name = "sinceIssues") Integer sinceIssues,
                                             @RequestParam(required = false) Integer maxPages){

        Project project = gitHubService.getProject(owner, repo).getBody();
        String projectId = project.getId().toString();
        String projectName = project.getName();
        String project_webUrl = project.getHtmlUrl();
        List<GitHubMinerCommit> commits = gitHubService.groupAllCommits(owner, repo, sinceCommits, maxPages).stream().map(x->formatCommit(x)).toList();
        List<GitHubMinerIssue> issues = gitHubService.groupAllIssues(owner, repo, sinceIssues, maxPages).stream().map(x->formatIssue(x,owner,repo,maxPages)).toList();

        return new GitHubMinerProject(projectId,projectName,project_webUrl,commits,issues);
    }

    private GitHubMinerCommit formatCommit(Commit commit){
        return new GitHubMinerCommit(commit.getSha(), "", commit.getCommit().getMessage(),
                commit.getCommit().getAuthor().getName(), commit.getCommit().getAuthor().getEmail(), commit.getCommit().getAuthor().getDate(),
                commit.getCommit().getCommitter().getName(), commit.getCommit().getCommitter().getEmail(), commit.getCommit().getCommitter().getDate(),
                commit.getHtmlUrl());
    }

    private GitHubMinerIssue formatIssue(Issue issue, String owner, String repo, Integer maxPages){
        String issueId = issue.getId().toString();
        String ref_id = issue.getNumber().toString();
        String title = issue.getTitle();
        String description = issue.getBody();
        String state = issue.getState();
        String created_at = issue.getCreatedAt();
        String updated_at = issue.getUpdatedAt();
        String closed_at = issue.getClosedAt();
        List<String> labels = issue.getLabels().stream().map(x->x.toString()).toList();

        FullAuthor fullAuthor = gitHubService.getFullAuthor(issue.getUser().getLogin());
        GitHubMinerUser author = new GitHubMinerUser(fullAuthor.getId().toString(), fullAuthor.getLogin(), fullAuthor.getName(), fullAuthor.getAvatarUrl(),fullAuthor.getHtmlUrl());

        FullAuthor fullAssignee=  issue.getAssignee()==null?null:gitHubService.getFullAuthor(issue.getAssignee().getLogin());
        GitHubMinerUser assignee = issue.getAssignee()==null?null:new GitHubMinerUser(fullAssignee.getId().toString(), fullAssignee.getLogin(), fullAssignee.getName(), fullAssignee.getAvatarUrl(),fullAssignee.getHtmlUrl());

        Integer upvotes = issue.getReactions().getPositive();
        Integer downvotes = issue.getReactions().getNegative();
        String web_url = issue.getHtmlUrl();
        List<GitHubMinerComment> comments= gitHubService.groupIssueComments(owner,repo,issue.getNumber().toString(),maxPages).stream().map(x->formatComment(x)).toList();


        return new GitHubMinerIssue(issueId, ref_id, title, description, state, created_at, updated_at, closed_at, labels,
                author,assignee,upvotes,downvotes,web_url,comments);
    }

    private GitHubMinerComment formatComment(Comment comment){
        String id = comment.getId().toString();
        String body = comment.getBody();
        FullAuthor fullAuthor = gitHubService.getFullAuthor(comment.getUser().getLogin());
        GitHubMinerUser author = new GitHubMinerUser(fullAuthor.getId().toString(), fullAuthor.getLogin(), fullAuthor.getName(), fullAuthor.getAvatarUrl(),fullAuthor.getHtmlUrl());
        String created_at = comment.getCreatedAt();
        String updated_at = comment.getUpdatedAt();
        return new GitHubMinerComment(id, body, author, created_at, updated_at);
    }

    @PostMapping("/{owner}/{repo}")
    public GitHubMinerProject SendProject(@PathVariable String owner,
                                          @PathVariable String repo,
                                          @RequestParam(required = false,name="sinceCommits") Integer since,
                                          @RequestParam(required = false, name = "sinceIssues") Integer updatedAfter,
                                          @RequestParam(required = false, name = "max_pages") Integer maxPages){

        Project project = gitHubService.getProject(owner,repo).getBody();
        String projectId = project.getId().toString();
        String projectName = project.getName();
        String project_webUrl = project.getHtmlUrl();
        List<GitHubMinerCommit> commits = gitHubService.groupAllCommits(owner, repo, since, maxPages).stream().map(x->formatCommit(x)).toList();
        List<GitHubMinerIssue> issues = gitHubService.groupAllIssues(owner, repo, since, maxPages).stream().map(x->formatIssue(x,owner,repo,maxPages)).toList();

        GitHubMinerProject proyectoFormateado = new GitHubMinerProject(projectId, projectName, project_webUrl, commits, issues);
        GitHubMinerProject sentProject = restTemplate.postForObject("http://localhost:8080/gitminer/projects", proyectoFormateado,GitHubMinerProject.class);

        return sentProject;

    }
}
