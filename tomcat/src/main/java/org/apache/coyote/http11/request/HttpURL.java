package org.apache.coyote.http11.request;

import org.apache.coyote.http11.common.NameValuePairs;
import org.apache.coyote.http11.common.QueryParamNameValuePairs;

public record HttpURL(String fullUrl, String path, NameValuePairs queryParameters) {

    private static final String PATH_QUERY_DELIMITER = "\\?";
    private static final int PATH_INDEX = 0;
    private static final int QUERY_INDEX = 1;
    private static final int PATH_QUERY_LENGTH = 2;

    public static HttpURL from(String fullUrl) {
        return new HttpURL(fullUrl, parsePath(fullUrl), parseQueryParameters(fullUrl));
    }

    private static String parsePath(String url) {
        return url.split(PATH_QUERY_DELIMITER)[PATH_INDEX];
    }

    private static NameValuePairs parseQueryParameters(String url) {
        String[] pathAndQuery = url.split(PATH_QUERY_DELIMITER, PATH_QUERY_LENGTH);
        if (isQueryPartExist(pathAndQuery)) {
            return NameValuePairs.empty();
        }
        return new QueryParamNameValuePairs(pathAndQuery[QUERY_INDEX]);
    }

    private static boolean isQueryPartExist(String[] pathAndQuery) {
        return pathAndQuery.length != PATH_QUERY_LENGTH || pathAndQuery[QUERY_INDEX].isEmpty();
    }
}
