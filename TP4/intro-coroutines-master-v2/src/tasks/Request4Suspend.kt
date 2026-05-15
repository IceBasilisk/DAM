package tasks

import contributors.*

suspend fun loadContributorsSuspend(service: GitHubService, req: RequestData): List<User> {
    val repos = service
        .getOrgReposCall(req.org)
        .execute()
        .also { logRepos(req, it) }
        .body() ?: emptyList()

    return repos.flatMap { repo ->
        service.getRepoContributorsCall(req.org, repo.name)
            .execute()
            .also { logUsers(repo, it) }
            .bodyList()
    }.aggregate()
}