package de.flxw;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class RandomCommiter {
    private int breakdays;
    private int days;
    private Git git;
    private TimeZone tz;

    public RandomCommiter() throws GitAPIException {
        Configuration cfg = Configuration.getInstance();

        days = (int) cfg.getStartDate().until(cfg.getEndDate(), ChronoUnit.DAYS);
        breakdays = cfg.getBreakdays();

        File repoDir = new File(cfg.getRepoDir());
        git = Git.init()
                 .setDirectory(repoDir)
                 .call();

        Calendar now = Calendar.getInstance();
        tz = now.getTimeZone();
    }

    public void executeCommits() throws IOException, GitAPIException {
        Configuration cfg = Configuration.getInstance();

        List<Integer> breakDayList = generateFreeDayList(days, breakdays);
        int nextFreeDay = breakDayList.remove(0);
        breakdays--;

        for (int d = 0; d < days; ++d) {
            if (d == nextFreeDay) {
                if (breakdays > 0) {
                    nextFreeDay = breakDayList.remove(0);
                    breakdays--;
                }
                continue;
            }

            LocalDate commitDate = cfg.getStartDate().plusDays(d);
            String fileName = commitDate.toString();

            File file = new File(cfg.getRepoDir(), fileName);
            file.createNewFile();

            Date date = java.sql.Date.valueOf(commitDate);
            PersonIdent committer = new PersonIdent(cfg.getName(), cfg.getEmail(), date, tz);

            git.add().addFilepattern(fileName).call();

            git.commit().setMessage("A commit for " + fileName).setCommitter(committer).call();
        }
    }

    public static List<Integer> generateFreeDayList(int range, int days) {
        ArrayList<Integer> list = new ArrayList<Integer>(range);

        for (int i=0; i<range; ++i) {
            list.add(i);
        }

        Collections.shuffle(list);
        List<Integer> returnValue = list.subList(0,days);
        Collections.sort(returnValue);

        return returnValue;
    }

    /*private static LocalDate generateRandomDateBetweenTwoDates (LocalDate begin, LocalDate end) {
        Random randomIntGenerator = new Random();

        int randomDateOffset = abs(randomIntGenerator.nextInt()) % days;
        LocalDate randomDate = begin.plusDays(randomDateOffset);
        return randomDate;
    }*/
    private static void writeToFile(String data, File file) {
        FileWriter fr = null;

        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*final int breakDays = 35;
        final LocalDate startDate = LocalDate.of(2019, 1, 1);
        final LocalDate endDate = LocalDate.of(2019,10,1);

        final int maximumCommitsPerDay;
        final String repoPath = "/Users/f.wolff/testrepo";

        File repoDir = new File(repoPath);

        Git git = Git.init()
                     .setDirectory(repoDir)
                     .call();

        String newRandomFile = UUID.randomUUID().toString();
        String dateString = "";

        for (int i=0; i < breakDays; ++i) {
            LocalDate randomDate = generateRandomDateBetweenTwoDates(startDate, endDate);
            dateString += randomDate.toString() + "\n";
        }

        writeToFile(dateString, new File(repoPath, newRandomFile));

        git.add().addFilepattern(".").call();
        git.commit()
           .setMessage("Commit all changes including additions")
           .call();*/
}
