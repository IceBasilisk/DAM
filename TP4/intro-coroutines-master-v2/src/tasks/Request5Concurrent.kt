package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    val repos = service
        .getOrgReposCall(req.org)
        .execute()
        .also { logRepos(req, it) }
        .bodyList()


    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        async {
            log("starting loading for ${repo.name}")
            delay(3000)
            service.getRepoContributorsCall(req.org, repo.name)
                .execute()
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }
    deferreds.awaitAll().flatten().aggregate()
}