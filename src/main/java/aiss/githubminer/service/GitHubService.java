package aiss.githubminer.service;

import aiss.githubminer.model.*;
import aiss.githubminer.util.GitHubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GitHubService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${githubminer.token}")
    private String token;

    public ResponseEntity<Project> getProject(String owner, String repo){
        String uri = "https://api.github.com/repos/"+ owner + "/" + repo;

        return restTemplate.exchange(uri, HttpMethod.GET, null, Project.class);
    }

    public ResponseEntity<Commit[]> getAllCommits(String owner, String repo) {
        String uri = "https://api.github.com/repos/"+ owner + "/" + repo + "/commits";

        return getResponseEntity(uri,Commit[].class);
    }

    public List<Commit> groupAllCommits(String owner, String repo, Integer since, Integer maxPages) throws HttpClientErrorException {
        List<Commit> commits = new ArrayList<>();
        Integer defaultPages;
        String uri = "https://api.github.com/repos/"+ owner + "/" + repo + "/commits";

        if (since != null) {
            uri += "?since=" + LocalDateTime.now().minusDays(since);
        } else {
            uri += "?since=" + LocalDateTime.now().minusDays(2);
        }

        ResponseEntity<Commit[]> response = getResponseEntity(uri, Commit[].class);
        List<Commit> pageCommits = Arrays.stream(response.getBody()).toList();
        commits.addAll(pageCommits);

        //2..n pages
        String nextPageURL = GitHubUtil.getNextPageUrl(response.getHeaders());

        if(maxPages!=null){
            defaultPages=maxPages;
        }
        else{
            defaultPages=2;
        }

        int page = 2;
        while (nextPageURL != null && page <= defaultPages) {
            response = getResponseEntity(nextPageURL,Commit[].class);
            pageCommits = Arrays.stream(response.getBody()).toList();
            commits.addAll(pageCommits);

            nextPageURL = GitHubUtil.getNextPageUrl(response.getHeaders());
            page++;

        }
        return commits;

    }

    public ResponseEntity<Issue[]> getAllIssues(String owner, String repo){
        String uri = "https://api.github.com/repos/"+ owner + "/" + repo + "/issues";

        return getResponseEntity(uri,Issue[].class);
    }

    public List<Issue> groupAllIssues(String owner, String repo, Integer since, Integer maxPages) throws HttpClientErrorException {
        List<Issue> issues = new ArrayList<>();
        Integer defaultPages;
        String uri = "https://api.github.com/repos/"+ owner + "/" + repo + "/issues";

        if (since != null) {
            uri += "?since=" + LocalDateTime.now().minusDays(since);
        } else {
            uri += "?since=" + LocalDateTime.now().minusDays(20);
        }

        ResponseEntity<Issue[]> response = getResponseEntity(uri, Issue[].class);
        List<Issue> pageIssues = Arrays.stream(response.getBody()).toList();
        issues.addAll(pageIssues);

        //2..n pages
        String nextPageURL = GitHubUtil.getNextPageUrl(response.getHeaders());

        if(maxPages!=null){
            defaultPages=maxPages;
        }
        else{
            defaultPages=2;
        }

        int page = 2;
        while (nextPageURL != null && page <= defaultPages) {
            response = getResponseEntity(nextPageURL,Issue[].class);
            pageIssues = Arrays.stream(response.getBody()).toList();
            issues.addAll(pageIssues);
            nextPageURL = GitHubUtil.getNextPageUrl(response.getHeaders());
            page++;
        }
        return issues;
    }

    public ResponseEntity<Comment[]> getIssueComments(String owner, String repo){

        String uri = "https://api.github.com/repos/"+ owner + "/" + repo + "/issues/comments";

        return getResponseEntity(uri,Comment[].class);
    }

    public List<Comment> groupIssueComments(String owner, String repo, String number, Integer maxPages) throws HttpClientErrorException{
        List<Comment> comments = new ArrayList<>();
        Integer defaultPages;
        String uri = "https://api.github.com/repos/" + owner + "/" + repo + "/issues/" + number + "/comments";

        ResponseEntity<Comment[]> response = getResponseEntity(uri, Comment[].class);
        List<Comment> pageComments = Arrays.stream(response.getBody()).toList();
        comments.addAll(pageComments);

        //2..n pages
        String nextPageURL = GitHubUtil.getNextPageUrl(response.getHeaders());

        if(maxPages!=null){
            defaultPages=maxPages;
        }
        else{
            defaultPages=2;
        }

        int page = 2;
        while(nextPageURL != null && page <= defaultPages){
            response = getResponseEntity(nextPageURL, Comment[].class);
            pageComments = Arrays.stream(response.getBody()).toList();
            comments.addAll(pageComments);

            nextPageURL = GitHubUtil.getNextPageUrl(response.getHeaders());
            page++;
        }

        return comments;
    }

    public FullAuthor getFullAuthor(String login){
        
        String uri = "https://api.github.com/users/" + login;

        return restTemplate.exchange(uri, HttpMethod.GET, null, FullAuthor.class).getBody();

    }

    private <T1> ResponseEntity<T1[]> getResponseEntity(String uri, Class<T1[]> clase) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " + token);

        HttpEntity<T1[]> request = new HttpEntity<>(null,headers);

        return restTemplate.exchange(uri,
                HttpMethod.GET,
                request,
                clase);
    }
}

