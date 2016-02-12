package com.awesome.awesomefifteenpuzzle;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <h1>Web Service Connector</h1>
 * The class responsible for all internet connections and retrievals
 *
 * @see <a href="http://developer.android.com/training/basics/network-ops/connecting.html">The reference used while creating this class</a>
 *
 * @author Team Awesome 2.0
 * @version 1.0
 * @since 12/2/2016
 */
public class WebServiceConnection  {

    int webService;
    //The Domain of the webservice
    String baseUrl = "http://puzzle15.ddns.net:8888/puzzle/";

    Board board = Board.getInstance();


    private static WebServiceConnection instance = null;
    private WebServiceConnection() {}

    /**
     * A getter for the connector
     * @return The Single instance of WebServiceConnection
     */
    public static WebServiceConnection getInstance() {
        if(instance == null) {
            instance = new WebServiceConnection();
        }
        return instance;
    }

    /**
     * Downloads the web page in the background and then calls either
     * the {@link Board#updatePuzzle(String)} for shuffling the puzzle, or
     * {@link Board#solve(String)} passing the result obtained
     */
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        /**
         * Goes to the url passed and downloads the content of the web page by calling
         * {@link #downloadUrl(String)}
         * @param urls The address of the service
         * @return The result of reading the web page or an <i>error</i> message
         */
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        /**
         * Depending on the value of {@link #webService} it calls a method
         * <ul>
         *     <li>1: It means that the operation was a shuffle so it calls {@link Board#updatePuzzle(String)} </li>
         *     <li>2: It means that the operation was a solve so it calls {@link Board#solve(String)}</li>
         * </ul>
         * @param result the result obtained from reading the web page
         */
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            switch (webService) {
                case 1:
                    board.updatePuzzle(result);
                    webService = 0;
                    break;
                case 2:
                    board.solve(result);
                    webService = 0;
                    break;
            }
        }
    }


    /**
     * Goes to the url passed as parameter and download its content. It also specifies the
     * number of characters to download, the time out, and the type of webservice command
     * @param myurl the url of the web page
     * @return The content of the web page
     * @throws IOException
     */
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000 );
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    /**
     *
     * @param stream the input stream that contains the content of the page
     * @param len number of characters read from the web
     * @return the contents of the page as a string
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    /**
     * Sends a request of a shuffled puzzle to the webservice
     * @param connMgr
     * @return Suffling if there was a connection or a failure method otherwise
     */
    public String shuffleWeb(ConnectivityManager connMgr ) {
        String stringUrl = baseUrl + "newShuffled?n=4";
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            webService = 1;
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            return "No network connection available.";
        }
        return "Shuffling";
    }


    /**
     * Sends a request to solve the current puzzle to the webservice
     * @param connMgr
     * @return solving if there was a connection or a failure method otherwise
     */
    public String solveWeb(ConnectivityManager connMgr) {
        String stringUrl = baseUrl + "solve?puzzle=" + board.toString();
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            webService = 2;
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            return "No network connection available.";
        }
        return "solving";
    }
}
