package com.alpha.marketplace.repositories;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.repositories.base.GitHubRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GitHubRepositoryImpl implements GitHubRepository {
    private SessionFactory session;

    @Autowired
    public GitHubRepositoryImpl(SessionFactory session){
        this.session = session;
    }

    @Override
    public List<GitHubInfo> getAll() {
        List<GitHubInfo> infos = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            infos = sess.createQuery("FROM GitHubInfo", GitHubInfo.class).list();

            sess.getTransaction().commit();
            System.out.println("All git info retrieved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return infos;
    }

    @Override
    public GitHubInfo getById(int id) {
        GitHubInfo info = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            info = sess.get(GitHubInfo.class, (long)id);

            sess.getTransaction().commit();
            System.out.println("Extension retrieved successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return info;
    }

    @Override
    public boolean save(GitHubInfo gitHubInfo) {
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            sess.save(gitHubInfo);

            sess.getTransaction().commit();
            System.out.println("Git info saved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public boolean update(GitHubInfo gitHubInfo) {
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            sess.update(gitHubInfo);

            sess.getTransaction().commit();
            System.out.println("Git info updated successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(int id) {
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            sess.delete(getById(id));

            sess.getTransaction().commit();
            System.out.println("Git info deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public GitHubInfo getByExtensionId(long id) {
        GitHubInfo info = null;
        try (Session sess = session.openSession()) {
            sess.beginTransaction();

            info = (GitHubInfo) sess.createQuery("FROM GitHubInfo WHERE extension = :extensionId", GitHubInfo.class).setParameter("extensionId", id);

            sess.getTransaction().commit();
            System.out.println("All git info retrieved successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return info;
    }
}
