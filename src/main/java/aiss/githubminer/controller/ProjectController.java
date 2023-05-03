package aiss.githubminer.controller;

import aiss.githubminer.githubmodel.GitHubMinerProject;
import aiss.githubminer.model.Project;
import aiss.githubminer.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/github")
public class ProjectController {
    /*
    TODO: Finish the ProjectController
     */

    @Autowired
    GitHubService gitHubService;

    @Autowired
    RestTemplate restTemplate;

    // GET http://localhost:8080/github/{owner}/{repo}?since={since}&maxPages={maxPages}
    @GetMapping("/{owner}/{repo}")
    public GitHubMinerProject findOneProject(@PathVariable String owner,
                                             @PathVariable String repo,
                                             @RequestParam(required = false) Integer since,
                                             @RequestParam(required = false) Integer maxPages){

        Project project = gitHubService.getProject(owner, repo).getBody();
        return null;
    }
}
