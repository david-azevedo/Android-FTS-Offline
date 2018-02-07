package com.company.david.fts.Utils;
import java.util.Random;
import java.util.StringTokenizer;

public class LoremIpsum
{

    private static final String[] LINES = {
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do " +
                    "eiusmod tempor incididunt ut labore et dolore magna " +
                    "aliqua.",
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                    "laboris nisi ut aliquip ex ea commodo consequat.",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse " +
                    "cillum dolore eu fugiat nulla pariatur.",
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa " +
                    "qui officia deserunt mollit anim id est laborum."
    };

    private final String lines[];
    private String str = null;

    private final Random r;

    /**
     * Construct a new lorem ipsum corpus consisting of the given number of
     * sentences.
     *
     * @param sentences sentences to construct
     */
    public LoremIpsum(int sentences)
    {
        this.r = new Random();
        this.lines = new String[sentences];
        for (int i = 0; i < sentences; i++)
        {
            this.lines[i] = strFry(LINES[r.nextInt(LINES.length)]);
        }
    }

    public String
    toString()
    {
        if (this.str == null)
        {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < this.lines.length; i++)
            {
                b.append(this.lines[i]);
                if (i + 1 < this.lines.length)
                {
                    b.append(" ");
                }
            }
            this.str = b.toString();
        }
        return this.str;
    }

    /**
     * Get the lines which make up the generated corpus.
     *
     * @return lines
     */
    public String[]
    getLines()
    {
        return this.lines;
    }

    /**
     * Randomly replace some words of the given line with randomly-selected
     * words from the static lipsum text.
     *
     * @param line
     * @return
     */
    private final String
    strFry(String line)
    {
        final int REPLACE_RATE = 30;
        final int DROP_RATE = 5;

        StringBuilder builder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(line);

        while (tokenizer.hasMoreTokens())
        {
            String word = tokenizer.nextToken();
            if (r.nextInt(100) < REPLACE_RATE)
            {
                String newWord = randomLipsumWord();
                if (! word.toLowerCase().equals(word))
                { /* need to change case of replacement */
                    newWord = newWord.substring(0, 1).toUpperCase() +
                            newWord.substring(1);
                }

				/* match ending punctuation if necessary */
                word = stripTrailingPunc(newWord);
            }

			/* we'll only drop lower-case words -- lazy hack to avoid
			 * killing the first word in our sentence.
			 */
            if (r.nextInt(100) > DROP_RATE ||
                    ! word.toLowerCase().equals(word))
            {
                builder.append(word);


                if (tokenizer.hasMoreTokens())
                {
                    builder.append(" ");
                }
                else
                {
                    if (word.charAt(word.length() - 1) != '.')
                    {
                        builder.append('.');
                    }
                }
            }
        }

        return builder.toString();
    }


    private String
    stripTrailingPunc(String word)
    {
        if (word.charAt(word.length() - 1) == '.' ||
                word.charAt(word.length() - 1) == ',')
        {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    private String
    randomLipsumWord()
    {

        final String line = LINES[r.nextInt(LINES.length)];

        StringTokenizer tokenizer = new StringTokenizer(line);

        for (int chosenToken = r.nextInt(tokenizer.countTokens());
             chosenToken > 0;
             chosenToken--)
        {
            tokenizer.nextToken();
        }

        return stripTrailingPunc(tokenizer.nextToken().toLowerCase());
    }

    public static final String
    randomCorpus(int sentences)
    {
        return new LoremIpsum(sentences).toString();
    }
}

