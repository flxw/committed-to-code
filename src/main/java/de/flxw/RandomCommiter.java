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
    private Configuration cfg;

    public RandomCommiter() throws GitAPIException {
        cfg = Configuration.getInstance();

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
            executeDayCommits(commitDate);
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

    private void executeDayCommits(LocalDate commitDate) throws IOException, GitAPIException {
        Date date = java.sql.Date.valueOf(commitDate);
        PersonIdent committer = new PersonIdent(cfg.getName(), cfg.getEmail(), date, tz);

        String fileName = commitDate.toString();
        File file = new File(cfg.getRepoDir(), fileName);
        int nCommits = generateNumberOfCommitsForDay();

        file.createNewFile();

        for (; nCommits > 0; --nCommits) {
            String lineContent = UUID.randomUUID().toString() + "\n";
            appendToFile(lineContent, file);
            git.add().addFilepattern(fileName).call();
            git.commit().setMessage("A commit for " + fileName).setCommitter(committer).call();
        }
    }

    private int generateNumberOfCommitsForDay() {
        Random r = new Random();
        int min = cfg.getLowerFrequencyBound();
        int max = cfg.getUpperFrequencyBound();
        return r.nextInt((max - min) + 1) + min;
    }

    private static void appendToFile(String data, File file) {
        FileWriter fr = null;

        try {
            fr = new FileWriter(file, true);
            fr.write(data);
        } catch (IOException e) {
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
            }
        }
    }
}
