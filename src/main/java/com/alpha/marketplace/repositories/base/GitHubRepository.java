package com.alpha.marketplace.repositories.base;

import com.alpha.marketplace.models.GitHubInfo;

import java.util.List;

public interface GitHubRepository {

    List<GitHubInfo> getAll();

    GitHubInfo getById(int id);

    boolean save(GitHubInfo gitHubInfo);

    boolean update(GitHubInfo gitHubInfo);

    boolean delete(int id);

    GitHubInfo getByExtensionId(long id);
}
