package com.youtubetools.service;

import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class thumbnailservice {

    public String extractVideoId(String url) {
        // If input is already a video ID
        if (url.matches("^[a-zA-Z0-9_-]{11}$")) {
            return url;
        }

        // YouTube URL patterns
        String[] patterns = {
                "(?:https?:\\/\\/)?(?:www\\.)?youtube\\.com\\/watch\\?v=([a-zA-Z0-9_-]{11})",
                "(?:https?:\\/\\/)?(?:www\\.)?youtu\\.be\\/([a-zA-Z0-9_-]{11})",
                "(?:https?:\\/\\/)?(?:www\\.)?youtube\\.com\\/embed\\/([a-zA-Z0-9_-]{11})"
        };

        for (String pattern : patterns) {
            Matcher matcher = Pattern.compile(pattern).matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null; // No match found
    }
}
