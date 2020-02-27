package de.flxw;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class Main {
    public static void main(String[] args) throws GitAPIException {
        static int breakDays = 35;
        File repoDir = new File("/Users/f.wolff/testrepo");

        Git git = Git.init().setDirectory(repoDir).call();
    }
}
