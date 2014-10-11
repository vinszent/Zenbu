package moe.zenbu.app.commands.recognition.utils;

import moe.zenbu.app.beans.Anime;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RecognitionMatcher
{
    private static final Logger log = LoggerFactory.getLogger(RecognitionMatcher.class);

    private static List<Anime> animeList;
    private static String[] titlesArr;
    private static float[] tvExtraScore;

    private static volatile String previousQuery = "";
    private static volatile Anime previousAnime = null;

    private static Levenshtein ls = new Levenshtein();
    private static QGramsDistance qg = new QGramsDistance();
    private static Jaro j = new Jaro();
    private static JaroWinkler jw = new JaroWinkler();

    public static void loadTitles(final List<Anime> arg)
    {
        animeList = arg;

        List<String> tempTitles = arg.stream().flatMap(a -> a.getTitles().stream()).map(t -> t.getTitle().replaceAll("[^a-zA-Z0-9!?]", "").toLowerCase()).collect(Collectors.toList());
        tempTitles.removeIf(t -> StringUtils.isBlank(t));
        titlesArr = tempTitles.toArray(new String[tempTitles.size()]);

        // Add bonus for tv titles
        AtomicInteger i = new AtomicInteger(0);
        tvExtraScore = new float[titlesArr.length];
        animeList.stream().flatMap(a -> a.getTitles().stream()).forEach(t ->
        {
            if(!StringUtils.isBlank(t.getTitle().replaceAll("[^a-zA-Z0-9!?]", "")))
            {
                int j = i.getAndIncrement();
                if(t.getAnime().getType().equals("TV") && t.isSelected())
                {
                    tvExtraScore[j] = 0.2f;
                }
                else
                {
                    tvExtraScore[j] = 0f;
                }
            }
        });
    }

    public static Anime match(final String parsed)
    {
        String edited = parsed.replaceAll("[^a-zA-Z0-9!?]", "").toLowerCase();

        if(edited.equals(previousQuery))
        {
            log.debug("Query {} is equal to previous query {}, returing anime {}", edited, previousQuery, previousAnime.getSelectedTitle());

            return previousAnime;
        }

        float[] substringScores = ArrayUtils.toPrimitive(Arrays.stream(titlesArr).map(t -> Math.min(((float) edited.length() / (float) t.length()), ((float) t.length() / (float) edited.length())) * ((float) longestCommonSubstring(edited, t).length() / (float) edited.length())).toArray(Float[]::new));
        substringScores = addBonus(substringScores, tvExtraScore);

        for(int i = 0; i < substringScores.length; i++)
        {
            if(Float.isInfinite(substringScores[i]))
            {
                substringScores[i] = 0f;
            }
        }

        if(NumberUtils.max(substringScores) > 0.85)
        {
            return setupPreviousQuery(getAnime(titlesArr[ArrayUtils.indexOf(substringScores, NumberUtils.max(substringScores))]), edited);
        }
        else
        {
            float[] lsScores = ls.batchCompareSet(titlesArr, edited);
            lsScores = addBonus(lsScores, tvExtraScore);

            float lsMax = 0f;
            if((lsMax = NumberUtils.max(lsScores)) > 0.85)
            {
                return setupPreviousQuery(getAnime(titlesArr[ArrayUtils.indexOf(lsScores, lsMax)]), edited);
            }
            else
            {
                float[] jScores = j.batchCompareSet(titlesArr, edited);
                float[] jwScores = jw.batchCompareSet(titlesArr, edited);
                float[] qgScores = qg.batchCompareSet(titlesArr, edited);

                return setupPreviousQuery(getAnime(titlesArr[getBestRated(substringScores, jScores, jwScores, qgScores)]), edited);
            }
        }
    }

    public static Anime matchEasy(final String parsed)
    {
        log.info("Matching anime easy {}", parsed);

        String edited = parsed.replaceAll("[^a-zA-Z0-9!?]", "").toLowerCase();

        float[] substringScores = ArrayUtils.toPrimitive(Arrays.stream(titlesArr).map(t -> Math.min(((float) edited.length() / (float) t.length()), ((float) t.length() / (float) edited.length())) * ((float) longestCommonSubstring(edited, t).length() / (float) edited.length())).toArray(Float[]::new));
        for(int i = 0; i < substringScores.length; i++)
        {
            if(Float.isInfinite(substringScores[i]))
            {
                substringScores[i] = 0f;
            }
        }

        if(NumberUtils.max(substringScores) > 0.85f)
        {
            log.debug("Found match with substrings");
            return getAnime(titlesArr[ArrayUtils.indexOf(substringScores, NumberUtils.max(substringScores))]);
        }
        else
        {
            float[] lsScores = ls.batchCompareSet(titlesArr, edited);

            if(NumberUtils.max(lsScores) > 0.85f)
            {
                log.debug("Found match with ls");
                return getAnime(titlesArr[ArrayUtils.indexOf(lsScores, NumberUtils.max(lsScores))]);
            }
        }

        log.debug("No match found");

        return null;
    }

    private static Anime setupPreviousQuery(final Anime anime, final String query)
    {
        previousQuery = query;
        previousAnime = anime;

        return anime;
    }

    private static Anime getAnime(final String matched)
    {
        return animeList.parallelStream().filter(a -> a.getTitles().stream().anyMatch(t -> t.getTitle().replaceAll("[^a-zA-Z0-9!?]", "").toLowerCase().equals(matched))).findFirst().orElse(null);
    }

    private static String longestCommonSubstring(final String first, final String second)
    {
        List<String> subStrings = new ArrayList<>();

        subStrings.add(first);

        for(int i = 0; i < first.length(); i++)
        {
            for(int j = i + 1; j < first.length(); j++)
            {
                subStrings.add(first.substring(i, j));
            }
        }

        return subStrings.parallelStream().filter(s -> second.contains(s)).max((a, b) -> Integer.compare(a.length(), b.length())).orElse("");
    }

    private static float[] addBonus(float[] orig, float[] bonus)
    {
        for(int i = 0; i < bonus.length; i++)
        {
            orig[i] += bonus[i];
        }

        return orig;
    }

    private static int getBestRated(float[]... scores)
    {
        Map<Integer, Float> summed = new HashMap<>();

        for(float[] arr : scores)
        {
            float max = NumberUtils.max(arr);
            int indx = ArrayUtils.indexOf(arr, max);

            if(summed.containsKey(indx))
            {
                summed.replace(indx, summed.get(indx) + max);
            }
            else
            {
                summed.put(indx, max);
            }
        }

        return summed.entrySet().stream().max((a, b) -> Float.compare(a.getValue(), b.getValue())).get().getKey();
    }
}
