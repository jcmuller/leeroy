/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hudson.plugins.campfire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MatchNotFoundException extends Exception {
    public MatchNotFoundException(String str) {
        super(str);
    }
}

/**
 *
 * @author jcmuller
 */
public class MemeGenerator {
    private String templateID = "440017";
    private String generatorName = "Jacques-Cousteau";

    /**
     *
     * @param templateID
     * @param generatorName
     */
    public MemeGenerator(String templateID, String generatorName) {
        if (! "".equals(templateID)) {
            this.templateID = templateID;
        }

        if (! "".equals(generatorName)) {
            this.generatorName = generatorName;
        }
    }

    /**
     * Default constructor
     */
    public MemeGenerator() {
    }

    /**
     * This will generate a meme from http://memegenerator.net/Instance/CreateOrEdit
     * @param topString
     * @param bottomString
     * @return memeUrlString
     * @throws MatchNotFoundException
     */
    public String generate(String topString, String bottomString) throws MatchNotFoundException {
        URL url;
        String result = "";

        // Always use AdviceDogSpinoff
        String templateType = "AdviceDogSpinoff";

        try {
            // Construct data
            String data = URLEncoder.encode("templateType", "UTF-8")  + "=" + URLEncoder.encode(templateType, "UTF-8");
            data += "&" + URLEncoder.encode("text0", "UTF-8")         + "=" + URLEncoder.encode(topString, "UTF-8");
            data += "&" + URLEncoder.encode("text1", "UTF-8")         + "=" + URLEncoder.encode(bottomString, "UTF-8");
            data += "&" + URLEncoder.encode("templateID", "UTF-8")    + "=" + URLEncoder.encode(templateID, "UTF-8");
            data += "&" + URLEncoder.encode("generatorName", "UTF-8") + "=" + URLEncoder.encode(generatorName, "UTF-8");

            // Send data
            url = new URL("http://memegenerator.net/Instance/CreateOrEdit");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.contains("<img class=\"large rotated\"")) {
                    Pattern p = Pattern.compile("src=\"(.+?)\"");
                    Matcher m = p.matcher(line);

                    if (m.find()) {
                        result = m.group(1);
                    } else {
                        throw new MatchNotFoundException("Result not found");
                    }
                }
            }
            wr.close();
            rd.close();
        } catch (UnsupportedEncodingException e) {
            String log_warn_prefix = "Unsupported encoding: ";
            System.err.println(log_warn_prefix.concat(e.getMessage()));
        } catch (MalformedURLException e) {
            String log_warn_prefix = "Malformed URL: ";
            System.err.println(log_warn_prefix.concat(e.getMessage()));
        } catch (IOException e) {
            String log_warn_prefix = "IO Exception: ";
            System.err.println(log_warn_prefix.concat(e.getMessage()));
        }

        return result;
    }

    public static void main(String[] args) {
        try {
            MemeGenerator meme;

            meme = new MemeGenerator("440017", "Jacques-Cousteau");
            System.out.println(meme.generate("Hello", "World!"));

            meme = new MemeGenerator("457085", "Jacques-Cousteau-FAILURE");
            System.out.println(meme.generate("Hello", "World!"));
        } catch (MatchNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
