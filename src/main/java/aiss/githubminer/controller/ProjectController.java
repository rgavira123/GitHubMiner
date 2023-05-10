package aiss.githubminer.controller;

import aiss.githubminer.githubmodel.*;
import aiss.githubminer.model.FullAuthor;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import aiss.githubminer.service.GitHubService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
@Tag(name = "Github Project", description = "Github project management API")
@RestController
@RequestMapping("/github")
public class ProjectController {

    @Autowired
    GitHubService gitHubService;

    @Autowired
    RestTemplate restTemplate;

    // GET http://localhost:8080/github/{owner}/{repo}?since={since}&maxPages={maxPages}
    @Operation(
            summary = "Retrieve a project",
            description = "Find one project by specifying its owner and repository name",
            tags = {"get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = { @Content(schema = @Schema(implementation = GitHubMinerProject.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema())})
    })
    @GetMapping("/{owner}/{repo}")
    public GitHubMinerProject findOneProject(@Parameter(description = "Owner of the project to be retrieved") @PathVariable String owner,
                                             @Parameter(description = "Repository of the project to be retrieved") @PathVariable String repo,
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

    private String[] parseTitleMessage(String s){
        String[] parsedString = new String[]{s,""};
        if(s.contains("\n\n")){
            parsedString = s.split("\\n\\n");
        }
        return parsedString;
    }

    private GitHubMinerCommit formatCommit(Commit commit){
        return new GitHubMinerCommit(commit.getSha(), parseTitleMessage(commit.getCommit().getMessage())[0], parseTitleMessage(commit.getCommit().getMessage())[1],
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
        List<String> labels = issue.getLabels().stream().map(x->x.getName()).toList();

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
    @Operation(
            summary = "Insert a project",
            description = "Add a new project whose data is built from the GitHub API",
            tags = {"post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Success", content = { @Content(schema = @Schema(implementation = GitHubMinerProject.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema())})
    })
    @PostMapping("/{owner}/{repo}")
    @ResponseStatus(HttpStatus.CREATED)
    public GitHubMinerProject SendProject(@Parameter(description = "Owner of the project to be sent") @PathVariable String owner,
                                          @Parameter(description = "Repository of the project to be sent") @PathVariable String repo,
                                          @RequestParam(required = false,name="sinceCommits") Integer since,
                                          @RequestParam(required = false, name = "sinceIssues") Integer updatedAfter,
                                          @RequestParam(required = false, name = "max_pages") Integer maxPages){

        Project project = gitHubService.getProject(owner,repo).getBody();
        String projectId = project.getId().toString();
        String projectName = project.getName();
        String project_webUrl = project.getHtmlUrl();
        List<GitHubMinerCommit> commits = gitHubService.groupAllCommits(owner, repo, since, maxPages).stream().map(x->formatCommit(x)).toList();
        List<GitHubMinerIssue> issues = gitHubService.groupAllIssues(owner, repo, since, maxPages).stream().map(x->formatIssue(x,owner,repo,maxPages)).toList();

        GitHubMinerProject formattedProject = new GitHubMinerProject(projectId, projectName, project_webUrl, commits, issues);
        GitHubMinerProject sentProject = restTemplate.postForObject("http://localhost:8080/gitminer/projects", formattedProject,GitHubMinerProject.class);

        return sentProject;

    }
}
