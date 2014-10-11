package moe.zenbu.app.commands.recognition.utils;

import moe.zenbu.app.beans.Anime;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RecognitionParser
{
    private static final Logger log = LoggerFactory.getLogger(RecognitionParser.class);

    private static List<Character> charsToClean = new ArrayList<>(Arrays.asList('.', ',', '-', '_', '+'));

    // @formatter:off
    private static final String[] VIDEO_FLAGS =
    {
        "h\\s*264",
        "x\\s*264",
        "dvd",
        "dvd\\s*rip",
        "web",
        "web(\\s*rip)*",
        "(hd)*tv(\\s*rip)*",
        "blu\\s*ray",
        "blu-ray",
        "bd(\\s*rip)*",
        "hi10p",
        "divx",
        "xvid",
        "\\d{1,2}\\s*bit",
        "\\d{3,4}\\s*p",
        "1080",
        "720",
        "576",
        "480",
        "400",
        "\\d{3,4}x\\d{3,4}",
        "r2",
        "dvd\\s*scr",
        "hard\\s*sub",
        "soft\\s*sub"
    };

    private static final String[] AUDIO_FLAGS =
    {
        "flac",
        "aac",
        "mp3",
        "ogg",
        "vorbis",
        "ac3",
        "dts",
        "dts5 1",
        "5 1ch",
        "5\\s*\\d+\\s*\\d*",
        "dual\\s*audio",
        "truehd51",
        "eng",
        "jp",
        "fr",
        "br"
    };

    private static final String[] STRIP_KEYS =
    {
        "thora"
    };

    private static final String[] IGNORED_KEYS =
    {
        "op",
        "ncop",
        "opening",
        "ed",
        "nced",
        "ending",
        "prev",
        "preview",
        "trailer",
        "intro",
        "introduction",
        "digest",
        "audio",
        "pv",
        "omake"
    };

    private static final String[] CLEANUP_KEYS =
    {
        "ep",
        "episode",
        "theatrical",
        "version",
        "rs2"
    };
    // @formatter:on

    private static String videoFlagsRegex;
    private static String audioFlagsRegex;
    private static String cleanupRegex;
    private static String stripRegex;
    private static String ignoredRegex;

    static
    {
        videoFlagsRegex = "(?i)" + Arrays.stream(VIDEO_FLAGS).map(s -> "(?<!\\w)" + s + "(?!\\w)").collect(Collectors.joining("|"));
        audioFlagsRegex = "(?i)" + Arrays.stream(AUDIO_FLAGS).map(s -> "(?<!\\w)" + s + "(?!\\w)").collect(Collectors.joining("|"));
        cleanupRegex = "(?i)" + Arrays.stream(CLEANUP_KEYS).map(s -> "(?<!\\w)" + s + "(?!\\w)").collect(Collectors.joining("|"));
        stripRegex = "(?i)" + Arrays.stream(STRIP_KEYS).map(s -> "(?<!\\w)" + s + "(?!\\w)").collect(Collectors.joining("|"));
        ignoredRegex = "(?i)" + Arrays.stream(IGNORED_KEYS).map(s -> "\\s" + s + "\\d*\\s*((v\\w*\\s*\\d+(\\s|$))|(\\s|$))").collect(Collectors.joining("|"));
    }

    public static Map parse(final String filename)
    {
        String edited = filename;

        StringJoiner titleJoiner = new StringJoiner(" ");
        List<Integer> episodes = new ArrayList<>();
        List<String> videoFlags = new ArrayList<>();
        List<String> audioFlags = new ArrayList<>();
        List<String> subgroups = new ArrayList<>();
        StringJoiner crc32 = new StringJoiner("");

        Map returned = new HashMap();

        /*
         * Clean filename
         */
        if(filename.endsWith(".mkv") || filename.endsWith(".mp4") || filename.endsWith(".avi"))
        {
            edited = filename.substring(0, filename.length() - 4);
        }

        char delimter = getDelimiter(edited);
        for(char c : charsToClean)
        {
            edited = replaceCharUnenclosed(edited, c, " ");
        }
        edited = StringUtils.replace(edited, String.valueOf(delimter), " ");
        edited = edited.replaceAll("(?<=\\S)\\[", " \\[");
        edited = edited.replaceAll("(?<=\\S)\\(", " \\(");
        edited = edited.replaceAll("\\](?=\\S)", "\\] ");
        edited = edited.replaceAll("\\)(?=\\S)", "\\) ");

        edited = edited.replaceAll(stripRegex, " ");

        edited = StringUtils.normalizeSpace(edited);

        if(Pattern.compile(ignoredRegex).matcher(edited).find())
        {
            log.debug("File {} contains ignored keyword, not scanning", filename);
            return null;
        }

        log.debug("Title before parsing is {}", edited);

        List<String> tokenStrings = splitUnenclosed(edited, ' ');
        List<Token> tokens = tokenStrings.stream().map(s -> new Token(s, isTokenEnclosed(s))).collect(Collectors.toList());

        // Remove non alphanumeric chars
        for(int i = 0; i < tokens.size(); i++)
        {
            String value = tokens.get(i).getToken();
            if(!StringUtils.isAlphanumeric(value) && value.length() < 2)
            {
                tokens.remove(i);
                i--;
            }
        }

        for(int i = 0; i < tokens.size(); i++)
        {
            Token token = tokens.get(i);

            log.debug("Analysing token {}", token.getToken());

            Matcher video = Pattern.compile(videoFlagsRegex).matcher(token.getToken());
            while(video.find())
            {
                if(!videoFlags.contains(video.group())) 
                {
                    videoFlags.add(video.group());
                };
                token.setTouched(true);
            }

            Matcher audio = Pattern.compile(audioFlagsRegex).matcher(token.getToken());
            while(audio.find())
            {
                if(!audioFlags.contains(audio.group())) 
                {
                    audioFlags.add(audio.group());
                };
                token.setTouched(true);
            }

            Matcher crc32Matcher = Pattern.compile("(?i)\\p{XDigit}{8}").matcher(token.getToken());
            if(crc32Matcher.find())
            {
                crc32.add(crc32Matcher.group());
                token.setTouched(true);
            }
        }

        // Populate numbers list
        List<TitleNumber> numbers = new ArrayList<>();
        for(int i = 0; i < tokens.size(); i++)
        {
            Token token = tokens.get(i);
            if(token.getToken().matches(".*\\d+.*"))
            {
                if(!token.isTouched())
                {
                    String tokenValue = token.getToken();
                    tokenValue = tokenValue.replaceAll("(?<=\\d+)(\\s*(v|ver|version)\\d)", " ");
                    //                    tokenValue = tokenValue.replaceAll("(?i)(?:^|\\s)(?<ep>ep|episode|ova)(?:\\s*\\d+)", " ${ep}");
                    tokenValue = Pattern.compile("(?i)(?:^|\\s)(?<ep>ep|episode|ova)(?<num>\\d+)").matcher(tokenValue).replaceAll("${ep} ${num}");
                    tokenValue = StringUtils.normalizeSpace(tokenValue);
                    numbers.add(new TitleNumber(tokenValue, i));
                }
            }
        }

        for(int i = 0; i < numbers.size(); i++)
        {
            // Remove numbers greater than 1000 which are unrealistic episode numbers
            Matcher numberMatcher = Pattern.compile("\\d+").matcher(numbers.get(i).getValue());
            if(numberMatcher.find())
            {
                if(Integer.parseInt(numberMatcher.group()) > 1000)
                {
                    log.debug("Removing number over realistic episode limit");
                    numbers.remove(i);
                    i--;
                    continue;
                }
            }

            //Remove numbers which are directly next to a word
            if(numbers.get(i).getValue().matches(".*([a-zA-Z-_?!~]\\d|\\d[a-zA-Z-_?!~]).*") && !numbers.get(i).getValue().matches("\\d+-\\d+"))
            {
                log.debug("Removing number next to word character");
                numbers.remove(i);
                i--;
            }
        }

        log.debug("Total possible episode numbers is {}", numbers.size());

        // Get episode number
        if(numbers.size() == 0)
        {
            // TODO: Check title then check if totalUnits is 1
            Anime anime;
            if((anime = RecognitionMatcher.matchEasy(tokens.stream().filter(t -> !t.isTouched() && !t.isEnclosed()).map(t -> t.getToken()).collect(Collectors.joining(" ")))) != null && anime.getTotalUnits() < 2)
            {
                episodes.add(1);
            }
        }
        if(numbers.size() == 1)
        {
            episodes.addAll(checkRange(numbers.get(0), tokens));

            Anime anime;

            if(episodes.size() == 0)
            {
                int episode;

                if((episode = checkEpisodeTag(numbers.get(0), tokens)) != -1)
                {
                    episodes.add(episode);

                    for(int j = numbers.get(0).getPosition(); j < tokens.size(); j++)
                    {
                        log.debug("Removing token {}", tokens.get(j).getToken());
                        tokens.remove(j);
                        j--;
                    }
                }
                else if(checkIfSurroundedUntouchedUnenclosed(numbers.get(0), tokens) && ((anime = RecognitionMatcher.matchEasy(tokens.stream().filter(t -> !t.isTouched() && !t.isEnclosed()).map(t -> t.getToken()).collect(Collectors.joining(" ")))) != null && anime.getTotalUnits() < 2))
                {
                    log.debug("Number surrounded by untouched");
                    // TODO: Check if part of title, otherwise it's episode number + episode name
                    episodes.add(1);
                }
                else
                {
                    tokens.get(numbers.get(0).getPosition()).setTouched(true);
                    episodes.add(Integer.parseInt(numbers.get(0).getValue().replaceAll("\\D", "")));
                }
            }
        }
        else if(numbers.size() > 1)
        {
            for(int i = 0; i < numbers.size(); i++)
            {
                episodes.addAll(checkRange(numbers.get(i), tokens));

                if(episodes.size() == 0)
                {
                    int episode;

                    if((episode = checkEpisodeTag(numbers.get(i), tokens)) != -1)
                    {
                        episodes.add(episode);

                        for(int j = numbers.get(i).getPosition(); j < tokens.size(); j++)
                        {
                            log.debug("Removing token {}", tokens.get(j).getToken());
                            tokens.remove(j);
                            j--;
                        }
                        break;
                    }
                }
            }

            if(episodes.size() == 0)
            {
                // TODO: Checking if first number part of title

                // Safest bet is that 2nd number is episode
                tokens.get(numbers.get(1).getPosition()).setTouched(true);
                episodes.add(Integer.parseInt(numbers.get(1).getValue().replaceAll("\\D", "")));

                for(int i = numbers.get(1).getPosition(); i < tokens.size(); i++)
                {
                    tokens.remove(i);
                    i--;
                }
            }
        }

        // All tokens are enclosed, default to [Sub][Title][Episode]
        if(!tokens.stream().filter(t -> !t.isTouched()).anyMatch((t -> !t.isEnclosed())))
        {
            if(numbers.get(0).getPosition() < 2)
            {
                titleJoiner.add(tokens.get(0).getToken());
            }
            else
            {
                subgroups.add(tokens.get(0).getToken());
                titleJoiner.add(tokens.get(1).getToken());
            }

            if(episodes.isEmpty() && numbers.size() >= 1)
            {
                //                episodes.add(numbers.get(0).getValue())
            }
        }
        else
        {
            for(int i = 0; i < tokens.size(); i++)
            {
                Token token = tokens.get(i);

                if(!token.isTouched())
                {
                    if(token.isEnclosed())
                    {
                        boolean left = i != 0 && !tokens.get(i - 1).isEnclosed();
                        boolean right = i != tokens.size() - 1 && !tokens.get(i + 1).isEnclosed();

                        if(left && right)
                        {
                            titleJoiner.add(token.getToken());
                            token.setTouched(true);
                        }
                        else
                        {
                            // TODO: Use original tokens
                            subgroups.add(token.getToken());
                            token.setTouched(true);
                        }
                    }
                    else
                    {
                        titleJoiner.add(token.getToken());
                        token.setTouched(true);
                    }
                }
            }
        }

        log.info("Parsed title is \"{}\", with episodes {}, with subgroups {}, with video flags {}, with audio flags {} and crc32 \"{}\"", titleJoiner, episodes, subgroups, videoFlags, audioFlags, crc32);

        returned.put("parsed_title", titleJoiner.toString());
        returned.put("episode_numbers", episodes);
        returned.put("subgroups", subgroups);
        returned.put("video_flags", videoFlags);
        returned.put("audio_flags", audioFlags);
        returned.put("crc32", crc32.toString());

        return returned;
    }

    // Episode checkers
    private static List<Integer> checkRange(final TitleNumber number, final List<Token> tokens)
    {
        List<Integer> episodes = new ArrayList<>();

        if(number.getValue().matches("\\d+-\\d+"))
        {
            int start = Integer.parseInt(number.getValue().split("-")[0]);
            int end = Integer.parseInt(number.getValue().split("-")[1]);
            
            if(start > end)
            {
                return episodes;
            }

            tokens.get(number.getPosition()).setTouched(true);

            for(int i = start; i < end + 1; i++)
            {
                episodes.add(i);
            }
        }

        return episodes;
    }

    private static int checkEpisodeTag(final TitleNumber number, final List<Token> tokens)
    {
        if(number.getValue().matches("(?i)(^|\\s)(ep|episode|ova)\\s*\\d+"))
        {
            tokens.get(number.getPosition()).setTouched(true);
            return Integer.parseInt(number.getValue().replaceAll("\\D", ""));
        }
        else if(number.getPosition() != 0 && tokens.get(number.getPosition() - 1).getToken().matches("(?i)(^|\\s)(ep|episode|ova)(\\s|$)"))
        {
            tokens.get(number.getPosition() - 1).setTouched(true);
            tokens.get(number.getPosition()).setTouched(true);
            return Integer.parseInt(number.getValue().replaceAll("\\D", ""));
        }
        else
        {
            return -1;
        }
    }

    private static boolean checkIfSurroundedUntouchedUnenclosed(final TitleNumber number, final List<Token> tokens)
    {
        boolean left = number.getPosition() == 0 || !tokens.get(number.getPosition() - 1).isTouched() && !tokens.get(number.getPosition() - 1).isEnclosed();
        boolean right = number.getPosition() + 1 == tokens.size() || !tokens.get(number.getPosition() + 1).isTouched() && !tokens.get(number.getPosition() + 1).isEnclosed();

        return left && right;
    }

    // String utils
    private static char getDelimiter(final String arg)
    {
        Map<Integer, Long> counts = arg.chars().filter(c -> c == ' ' || c == '-' || c == '_' || c == '.' || c == ',').boxed().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        if(counts.isEmpty())
        {
            return 'x';
        }
        else
        {
            return (char) counts.entrySet().stream().max((a, b) -> Integer.compare(a.getValue().intValue(), b.getValue().intValue())).get().getKey().intValue();
        }
    }

    private static String replaceChar(final String arg, final char search, final String replacement)
    {
        String edited = arg;

        int prevPos = 0;
        while(true)
        {
            int pos = edited.indexOf(search, prevPos);

            if(pos != -1)
            {
                // Don't replace decimal points
                if((search == '.' || search == '-') && Character.isDigit(arg.charAt(pos - 1)) && Character.isDigit(arg.charAt(pos + 1)))
                {
                    prevPos = pos + 1;
                    continue;
                }

                if(pos - prevPos > 1 && Character.isLetterOrDigit(arg.charAt(pos - 2)))
                {
                    edited = edited.substring(0, pos) + replacement + edited.substring(pos + 1);
                    prevPos = 0;
                }
                else
                {
                    prevPos = pos + 1;
                }
            }
            else
            {
                break;
            }
        }

        return edited;
    }

    private static String replaceCharUnenclosed(final String arg, final char search, final String replacement)
    {
        String edited = arg;

        int prevPos = 0;
        while(true)
        {
            int pos = edited.indexOf(search, prevPos);

            if(pos != -1)
            {
                // Don't replace decimal points
                if((search == '.' || search == '-') && Character.isDigit(arg.charAt(pos - 1)) && Character.isDigit(arg.charAt(pos + 1)))
                {
                    prevPos = pos + 1;
                    continue;
                }

                if(!isCharEnclosed(edited, pos) && pos - prevPos > 1 && Character.isLetterOrDigit(arg.charAt(pos - 2)))
                {
                    edited = edited.substring(0, pos) + replacement + edited.substring(pos + 1);
                    prevPos = 0;
                }
                else
                {
                    prevPos = pos + 1;
                }
            }
            else
            {
                break;
            }
        }

        return edited;
    }

    private static List<String> splitUnenclosed(final String arg, final char c)
    {
        String edited = arg;

        List<String> tokens = new ArrayList<>();

        int prevPos = 0;
        while(true)
        {
            int pos = edited.indexOf(c, prevPos);

            if(pos != -1)
            {
                //                if(!isCharEnclosed(edited, pos) && (pos - prevPos > 1 || (edited.charAt(pos - 1) != '-' || edited.charAt(pos - 1) != '.')))
//                if(!isCharEnclosed(edited, pos) && (pos - prevPos > 1 || (pos - prevPos < 2 && Character.isDigit(edited.charAt(pos - 1)))))
                if(!isCharEnclosed(edited, pos))
                {
                    tokens.add(edited.substring(0, pos));
                    edited = edited.substring(pos + 1);
                    prevPos = 0;
                }
                else
                {
                    prevPos = pos + 1;
                }
            }
            else
            {
                tokens.add(edited);
                break;
            }
        }

        return tokens;
    }

    private static boolean isTokenEnclosed(final String token)
    {
        return (token.startsWith("[") || token.startsWith("(")) && (token.endsWith("]") || token.endsWith(")"));
    }

    private static boolean isCharEnclosed(final String filename, final int pos)
    {
        boolean right = false;
        boolean left = false;

        for(int i = 0; i < pos; i++)
        {
            char c = filename.charAt(i);
            if(c == '[' || c == '(')
            {
                left = true;
            }
            else if(c == ']' || c == ')')
            {
                break;
            }
        }

        for(int i = pos; i < filename.length(); i++)
        {
            char c = filename.charAt(i);
            if(c == ']' || c == ')')
            {
                right = true;
            }
            else if(c == '[' || c == '(')
            {
                break;
            }
        }

        return left && right;
    }
}
