package aiss.githubminer.service;

import aiss.githubminer.model.Comment;
import aiss.githubminer.model.Commit;
import aiss.githubminer.model.Issue;
import aiss.githubminer.model.Project;
import aiss.githubminer.util.GitHubUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GitHubServiceTest {

    @Autowired
    GitHubService githubService;

    @Test
    void getProjectById() {
        ResponseEntity<Project> project = githubService.getProject("hcoles", "pitest");
        assertTrue(project.getBody() != null, "the project is empty");
        System.out.println(project.getBody());
    }

    @Test
    void getAllCommits() {
        ResponseEntity<Commit[]> commits = githubService.getAllCommits("hcoles", "pitest");
        assertTrue(commits.getBody() != null, "no commits have been found");
        System.out.println(Arrays.stream(commits.getBody()).toList());
    }

    @Test
    void getAllIssues() {
        ResponseEntity<Issue[]> issues = githubService.getAllIssues("hcoles", "pitest");
        assertTrue(issues.getBody() != null, "no issues have been found");
        System.out.println(Arrays.stream(issues.getBody()).toList());
    }

    //funcion modificada, para que devuelva ya una lista, ya tenemos el metodo que lo hace, el que se llama group.
    @Test
    void getIssueComments() {
        ResponseEntity<Comment[]> comments = githubService.getIssueComments("hcoles", "pitest");
        assertTrue(comments.getBody() != null, "no comments were found");
        System.out.println(Arrays.stream(comments.getBody()).toList());
    }

    @Test
    void groupAllCommits() {
        List<Commit> place = githubService.groupAllCommits("hcoles", "pitest", 20, 2);
        assertTrue(place.size() != 0, "the list of commits is empty");
        System.out.println(place);
    }

    @Test
    void groupAllIssues() {
        List<Issue> place = githubService.groupAllIssues("hcoles", "pitest", 20, 2);
        assertTrue(place.size() != 0, "the list of issues is empty");
        System.out.println(place);
    }

    @Test
    void groupIssueComments() {
        List<Comment> place = githubService.groupIssueComments("hcoles", "pitest", 2);
        assertTrue(place.size() != 0, "the list of comments is empty");
        System.out.println(place);
    }

}